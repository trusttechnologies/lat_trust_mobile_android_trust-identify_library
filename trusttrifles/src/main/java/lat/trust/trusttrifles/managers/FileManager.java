package lat.trust.trusttrifles.managers;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import io.sentry.util.Util;
import lat.trust.trusttrifles.model.FileAppTrustId;
import lat.trust.trusttrifles.model.FileTrustId;
import lat.trust.trusttrifles.utilities.TrustLogger;
import lat.trust.trusttrifles.utilities.Utils;

import static lat.trust.trusttrifles.utilities.Constants.FILE_NAME;
import static lat.trust.trusttrifles.utilities.Constants.FILE_NOT_FOUND;
import static lat.trust.trusttrifles.utilities.Constants.PATH_FILE_SYSTEM;
import static lat.trust.trusttrifles.utilities.Constants.PATH_FILE_SYSTEM_BACKUP;
import static lat.trust.trusttrifles.utilities.Constants.PATH_FILE_SYSTEM_BACKUP_INVISIBLE;
import static lat.trust.trusttrifles.utilities.Constants.TRUST_ID_SCORE_LOW;
import static lat.trust.trusttrifles.utilities.Constants.TRUST_ID_TYPE_NORMAL;
import static lat.trust.trusttrifles.utilities.Constants.TRUST_ID_TYPE_ZERO;

public class FileManager {

    public static void saveFile(FileTrustId fileTrustIdIN, boolean isOverWrite, Context context) {

        FileTrustId fileTrustId = getFileTrustId(); //--> este es el actual guardado en el archivo,  null si no

        if (fileTrustId != null) {
            if (isOverWrite) {
                trustIdOverwriteFlow(fileTrustIdIN);
                return;
            }
            if (fileTrustIdIN.getType().equals(TRUST_ID_TYPE_ZERO)) {
                trustIdZeroFlow(fileTrustIdIN, context);
                return;
            }
            if (fileTrustIdIN.getType().equals(TRUST_ID_TYPE_NORMAL)) {
                trustIdLiteOrNormalFlow(fileTrustIdIN);
            }
        } else {
            trustIdNewFlow(fileTrustIdIN, context);
        }
    }

    public static ArrayList<String> getPaths() {
        ArrayList<String> paths = new ArrayList<String>();
        paths.add(PATH_FILE_SYSTEM);
        paths.add(PATH_FILE_SYSTEM_BACKUP);
        paths.add(PATH_FILE_SYSTEM_BACKUP_INVISIBLE);
        return paths;
    }

    public static void createFile(String sFileName, FileTrustId f, String child) {
        String json = new Gson().toJson(f);
        Log.e("getApi28", "create folder: " + json);
        LogManager.addLog("data to save: " + json);

        try {
            File root = new File(Environment.getExternalStorageDirectory(), child);
            if (!root.exists()) {
                root.mkdirs();
                Log.e("getApi28", "create folder: " + root.getAbsolutePath());
                LogManager.addLog("creating folder: " + root.getAbsolutePath());
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            // writer.append(json);                    // --> desencriptado
            writer.append(CryptManager.encrypt(json)); // --> encriptado
            writer.flush();
            writer.close();
        } catch (IOException e) {
            LogManager.addLogError("creating folder error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void trustIdZeroFlow(FileTrustId fileTrustIdIN, Context context) {
        try {
            FileTrustId fileTrustId = getFileTrustId(); //--> este es el actual guardado en el archivo,  null si no
            ArrayList<FileAppTrustId> listOfApps = null;
            if (fileTrustId != null) {
                boolean isAnyTrustIdMatch = false;
                listOfApps = fileTrustId.getTrustIdApps();
                for (FileAppTrustId fileApp : listOfApps) {
                    if (fileApp.getTrustId().equals(fileTrustIdIN.getTrustId())) {
                        isAnyTrustIdMatch = true;
                        break;
                    }
                }
                if (!isAnyTrustIdMatch) {
                    FileAppTrustId fileAppTrustIdToSave = new FileAppTrustId();
                    fileAppTrustIdToSave.setTrustId(fileTrustIdIN.getTrustId());
                    fileAppTrustIdToSave.setCreateAt(Utils.getCurrentTimeStamp());
                    fileAppTrustIdToSave.setBundleId(context.getPackageName());
                    fileAppTrustIdToSave.setScore(fileTrustIdIN.getScore());
                    fileAppTrustIdToSave.setType(fileTrustIdIN.getType());
                    listOfApps.add(fileAppTrustIdToSave);
                }
                fileTrustId.setTrustIdApps(listOfApps);
                saveFileLocal(fileTrustId);
            } else {
                trustIdNewFlow(fileTrustIdIN, context);
            }
        } catch (Exception e) {
            LogManager.addLogError(e.getMessage());
            TrustLogger.d(e.getMessage());
        }
    }

    /**
     * @param fileTrustIdIN: Trust object to save
     */
    private static void trustIdLiteOrNormalFlow(FileTrustId fileTrustIdIN) {
        try {
            FileTrustId fileTrustId = getFileTrustId(); //--> este es el actual guardado en el archivo,  null si no
            if (fileTrustId == null) {
                saveFileLocal(fileTrustIdIN);
            } else {
                if (!fileTrustId.getTrustId().equals(fileTrustIdIN.getTrustId())) {
                    LogManager.addLogError("Trust id not the same");
                    if (fileTrustId.getScore().equals(TRUST_ID_SCORE_LOW)) {
                        LogManager.addLog("Trust id base saved are LOW, now changing");
                        FileTrustId fileTrustIdToSave = new FileTrustId();
                        fileTrustIdToSave.setCreateAt(Utils.getCurrentTimeStamp());
                        fileTrustIdToSave.setScore(fileTrustIdIN.getScore());
                        fileTrustIdToSave.setType(fileTrustIdIN.getType());
                        fileTrustIdToSave.setTrustId(fileTrustIdIN.getTrustId());
                        fileTrustIdToSave.setTrustIdApps(fileTrustId.getTrustIdApps());
                        saveFileLocal(fileTrustIdToSave);
                    }
                } else {
                    LogManager.addLog("Trust id are the same, nothing to do.");
                    saveFileLocal(fileTrustId);

                }
            }
        } catch (Exception e) {
            LogManager.addLogError(e.getMessage());
            TrustLogger.d(e.getMessage());
        }

    }

    /**
     * este metodo se encargara de guardar en los 3 archivos locales
     * los datos del servicio de trust id
     * <p>
     * Se almacena en el arreglo 1 objeto solo si este tipo de trust id es ZERO, si es normal
     * solo se almacenara en la base del objeto.
     *
     * @param fileTrustIdIN: Trust object to save
     */
    private static void trustIdNewFlow(FileTrustId fileTrustIdIN, Context context) {
        FileTrustId trustIdTosave = new FileTrustId();
        trustIdTosave.setTrustId(fileTrustIdIN.getTrustId());
        trustIdTosave.setCreateAt(Utils.getCurrentTimeStamp());
        trustIdTosave.setScore(fileTrustIdIN.getScore());
        trustIdTosave.setType(fileTrustIdIN.getType());

        if (fileTrustIdIN.getType().equals(TRUST_ID_TYPE_ZERO)) {
            FileAppTrustId fileAppTrustId = new FileAppTrustId();
            fileAppTrustId.setTrustId(fileTrustIdIN.getTrustId());
            fileAppTrustId.setScore(fileTrustIdIN.getScore());
            fileAppTrustId.setBundleId(context.getPackageName());
            fileAppTrustId.setCreateAt(Utils.getCurrentTimeStamp());
            fileAppTrustId.setType(fileTrustIdIN.getType());
            ArrayList<FileAppTrustId> fileAppToSave = new ArrayList<>();
            fileAppToSave.add(fileAppTrustId);
            trustIdTosave.setTrustIdApps(fileAppToSave);
        } else {
            trustIdTosave.setTrustIdApps(new ArrayList<>());
        }
        saveFileLocal(trustIdTosave);
    }

    /**
     * este metodo se encarga de sobreescribir solo los trust id normal.
     *
     * @param fileTrustIdIN: Trust object to save
     */
    private static void trustIdOverwriteFlow(FileTrustId fileTrustIdIN) {
        try {
            LogManager.addLog("overwrite operation");
            //se obtiene el archivo almacenado
            FileTrustId fileTrustIdSaved = getFileTrustId();
            if (fileTrustIdSaved != null) {
                fileTrustIdSaved.setTrustId(fileTrustIdIN.getTrustId());
                saveFileLocal(fileTrustIdSaved);
            } else {
                FileTrustId newFileTrustIdToSave = new FileTrustId();
                newFileTrustIdToSave.setTrustId(fileTrustIdIN.getTrustId());
                newFileTrustIdToSave.setType(fileTrustIdIN.getType());
                newFileTrustIdToSave.setScore(fileTrustIdIN.getScore());
                newFileTrustIdToSave.setCreateAt(Utils.getCurrentTimeStamp());
                newFileTrustIdToSave.setTrustIdApps(new ArrayList<>());
                saveFileLocal(fileTrustIdIN);
            }

        } catch (Exception e) {
            LogManager.addLogError(e.getMessage());
            TrustLogger.d(e.getMessage());
        }

    }

    private static void saveFileLocal(FileTrustId fileToSave) {
        ArrayList<String> paths = getPaths();
        for (String path : paths) {
            createFile(FILE_NAME, fileToSave, path);
            LogManager.addLog("saving trust id:" + fileToSave.getTrustId());
            LogManager.addLog("score: " + fileToSave.getScore());
            LogManager.addLog("when:" + fileToSave.getCreateAt());
            LogManager.addLog("on path: " + (path.equals("") ? "base path" : path));
        }
    }

    public static String getTrustFileAsJson(String path) {
        StringBuilder stringBuffer = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory().toString().concat(path).concat("/" + FILE_NAME)))) {
            String line = reader.readLine();
            while (line != null) {
                stringBuffer.append(line).append('\n');
                line = reader.readLine();
            }
            if (stringBuffer.toString().equals("")) {
                LogManager.addLogError("file found but empty.");
                return FILE_NOT_FOUND;
            } else {
                LogManager.addLog("file found:" + FILE_NAME);
                LogManager.addLog("found on path:" + path);
                //return stringBuffer.toString();                     // --> desencriptado
                return CryptManager.decrypt(stringBuffer.toString()); // --> encriptado
            }
        } catch (IOException e) {
            //el archivo no fue encontrado
            LogManager.addLog("file not found:" + path);
            LogManager.addLog("file not found:" + e.getMessage());
            return FILE_NOT_FOUND;
        }
    }

    public static FileTrustId getFileTrustId() {
        String fileJson = null;
        try {
            fileJson = getTrustFileAsJson(PATH_FILE_SYSTEM); //--> base path

            if (fileJson.equals(FILE_NOT_FOUND)) {
                Log.e("getApi28", "getFileTrustId: base path no encontrado, buscando en backup...");
                LogManager.addLogError("base file path not found, searching on backup...");
                fileJson = getTrustFileAsJson(PATH_FILE_SYSTEM_BACKUP); // --> backup
            } else {
                Log.e("getApi28", "getFileTrustId: base path encontrado");
                LogManager.addLog("base file path was found");
                return new Gson().fromJson(fileJson, FileTrustId.class);
            }

            if (fileJson.equals(FILE_NOT_FOUND)) {
                Log.e("getApi28", "getFileTrustId: backup path no encontrado, buscando en backup invisible");
                LogManager.addLogError("backup file path not found, searching on backup inv...");
                fileJson = getTrustFileAsJson(PATH_FILE_SYSTEM_BACKUP_INVISIBLE); // --> backup invisible
            } else {
                Log.e("getApi28", "getFileTrustId: backup path encontrado");
                LogManager.addLog("backup file path found");
                return new Gson().fromJson(fileJson, FileTrustId.class);
            }

            if (fileJson.equals(FILE_NOT_FOUND)) {
                Log.e("getApi28", "getFileTrustId: backup invisible path no encontrado, retornando NULL");
                LogManager.addLogError("backup inv file path not found, return null");
                return null;
            } else {
                Log.e("getApi28", "getFileTrustId: backup invisible path encontrado");
                LogManager.addLog("backup inv file path found");
                return new Gson().fromJson(fileJson, FileTrustId.class);
            }
        } catch (Exception e) {
            LogManager.addLogError("error while searching file: " + e.getMessage());
            return null;
        }
    }

}

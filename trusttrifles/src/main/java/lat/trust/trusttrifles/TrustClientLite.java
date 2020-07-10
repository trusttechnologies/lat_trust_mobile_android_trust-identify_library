package lat.trust.trusttrifles;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import io.sentry.Sentry;
import lat.trust.trusttrifles.model.Audit;
import lat.trust.trusttrifles.model.Camera;
import lat.trust.trusttrifles.model.Device;
import lat.trust.trusttrifles.model.Identity;
import lat.trust.trusttrifles.model.InfoTrustIdSaved;
import lat.trust.trusttrifles.model.JsonList;
import lat.trust.trusttrifles.model.TrustAuth;
import lat.trust.trusttrifles.network.req.TrifleBody;
import lat.trust.trusttrifles.utilities.Constants;
import lat.trust.trusttrifles.utilities.TrustLogger;

import static lat.trust.trusttrifles.utilities.Constants.SDK_IDENTIFY;
import static lat.trust.trusttrifles.utilities.Constants.SENTRY_STATE;


public class TrustClientLite {


    public static void init(Context context) {
        hawkInit(context);
        TrustAuth.setSecretAndId(context);
        setEnvironment(context);
        sentryInit(context);
        setVersionName();
    }

    private static void setVersionName() {
        Hawk.put(SDK_IDENTIFY, BuildConfig.VERSION_NAME);
    }

    private static void setEnvironment(Context context) {
        TrustIdentifyConfigurationService.setEnvironment(TrustIdentifyConfigurationService.ENVIRONMENT_PRODUCTION, context);
    }

    private static void hawkInit(Context context) {
        if (!Hawk.isBuilt()) {
            Hawk.init(context).build();
        }
    }

    private static void sentryInit(Context context) {
        SentryState.init(context);
    }

    private static Device getDeviceData(Context context) {
        Device device = new Device();
        device.setAndroid_device_id(DataUtil.getAndroidDeviceID(context));
        device.setBattery(DataUtil.getBatteryData(context));
        device.setBattery_capacity(DataUtil.getBatteryCapacity(context) == 0 ? "Not Found" : String.valueOf(DataUtil.getBatteryCapacity(context)).concat(" mAh"));
        device.setBattery_technology(DataUtil.getBatteryTechnology(context));
        device.setBluetooth_state(DataUtil.getBluetoothState());
        device.setBoard(DataUtil.getBoard());
        device.setBrand(DataUtil.getBrand());
        device.setDisplay(DataUtil.getDisplay());
        device.setDevice(DataUtil.getDevice());
        device.setSystemVersion(DataUtil.getSystemVersion());
        device.setFingerprint(DataUtil.getFingerprint());
        device.setHardware(DataUtil.getHardware());
        device.setId(DataUtil.getId());
        device.setHost(DataUtil.getHost());
        device.setManufacturer(DataUtil.getManufacturer());
        device.setModel(DataUtil.getModel());
        device.setProduct(DataUtil.getProduct());
        device.setSerial(DataUtil.getSerial());
        device.setProcessorModelName(DataUtil.getDataCPUFile("Processor"));
        device.setProcessorModelName(DataUtil.getDataCPUFile("model name"));
        device.setProcessorBogomips(DataUtil.getDataCPUFile("BogoMIPS"));
        device.setProcessorFeatures(DataUtil.getDataCPUFile("Features"));
        device.setProcessorHardware(DataUtil.getDataCPUFile("Hardware"));
        device.setProcessorRevision(DataUtil.getDataCPUFile("Revision"));
        device.setProcessorSerial(DataUtil.getDataCPUFile("Serial"));
        device.setProcessorDevice(DataUtil.getDataCPUFile("Device"));
        device.setProcessorRadio(DataUtil.getDataCPUFile("Radio"));
        device.setProcessorMsmHardware(DataUtil.getDataCPUFile("MSM Hardware"));
        device.setCpuImplementer(DataUtil.getDataCPUFile("CPU implementer"));
        device.setCpuArchitecture(DataUtil.getDataCPUFile("CPU architecture"));
        device.setCpuVariant(DataUtil.getDataCPUFile("CPU variant"));
        device.setCpuPart(DataUtil.getDataCPUFile("CPU part"));
        device.setCpuRevision(DataUtil.getDataCPUFile("CPU revision"));
        device.setKernelStack(DataUtil.getDataMEMFile(""));
        device.setMemTotal(DataUtil.getDataMEMFile("MemTotal"));
        device.setSwapTotal("SwapTotal");
        device.setImei(DataUtil.getImei(context));
        device.setSoftwareVersion(DataUtil.getSoftwareVersion(context));
        device.setSystemName(DataUtil.getSystemName());
        device.setRoot(DataUtil.getRooted(context));
        device.setNfc(DataUtil.getNFCData(context));
        device.setGoogle_service_framework_gsf(DataUtil.getGSFID(context));
        device.setSensorData(DataUtil.getSensorsData(context));
        device.setSensor_size(DataUtil.getSensorSize(context));
        device.setWifi_state(DataUtil.getWifiState(context));
        device.setRed_g_state(DataUtil.getRedGState(context));
        device.setWlan0Mac(DataUtil.getMacAddress());
        device.setBluetoothMac(DataUtil.getBluetoothMacAddress());
        device.setProcessorQuantity(DataUtil.getProcessorQuantity());
        device.setUUID(DataUtil.getUUID());
        device.setEmulator(DataUtil.isEmulator(context));
        //=============== nope zone =================
        device.setCameras(new ArrayList<Camera>());
        device.setCameras_size("0");


        TrustLogger.d(new Gson().toJson(device));
        DataUtil.getBundleId(context);
        return device;
    }

    public static void getTrustIDLite(Context context, final TrustListener.OnResult<Audit> listener) {
        TrifleBody trifleBody = new TrifleBody();
        trifleBody.setDevice(getDeviceData(context));
        trifleBody.setSim(DataUtil.getListSIM(context));
        trifleBody.setTrustId(Hawk.contains(Constants.TRUST_ID_AUTOMATIC) ? Hawk.get(Constants.TRUST_ID_AUTOMATIC) : null);
        Log.e("ACA", trifleBody.getTrustId());
        getTrustIDApi28(trifleBody, context);
        if (Hawk.contains(Constants.IDENTITY)) {
            trifleBody.setIdentity(DataUtil.getIdentity());
        }
        SendTrifles.sendTriflesToken(trifleBody, context, listener);
    }

    public static void overWriteTrust(String trustId, Context ctx) {

   /*     List<InfoTrustIdSaved> data = DataUtil.getStoredTrustId().getList();

        for (InfoTrustIdSaved element : data) {
            if (ctx.getPackageName().equals(element.getBundleId())){
                element.setTrustId(trustId);

            }
        }*/

        DataUtil.writeFile(trustId, ctx);


        if (Hawk.contains(Constants.TRUST_ID_AUTOMATIC)) {
            Hawk.put(Constants.TRUST_ID_AUTOMATIC, trustId);
        }

        if (Hawk.contains(Constants.TRUST_ID)) {
            Hawk.put(Constants.TRUST_ID, trustId);
        }

        if (Hawk.contains(Constants.AUDIT_TRUST_ID)) {
            Hawk.put(Constants.AUDIT_TRUST_ID, trustId);
        }


    }


    public static void saveIdentity(Identity identity) {
        Hawk.put(Constants.IDENTITY, identity);
    }

    public static void deleteIdentity() {
        if (Hawk.contains(Constants.IDENTITY)) {
            Hawk.delete(Constants.IDENTITY);
        }
    }

    public static String getBundleId(Context context) {
        return DataUtil.getBundleId(context);
    }

    public static void sendIdentify(Identity identity, Context context, final TrustListener.OnResult<Audit> listener) {
        try {
            TrifleBody trifleBody = new TrifleBody();
            trifleBody.setDevice(getDeviceData(context));
            trifleBody.setSim(DataUtil.getListSIM(context));
            trifleBody.setIdentity(identity);
            getTrustIDApi28(trifleBody, context);
            SendTrifles.sendTriflesToken(trifleBody, context, listener);
        } catch (Exception e) {
            TrustLogger.d("Error sendIdentify: " + e.getMessage());
            if (SentryState.getImportance().equals(SentryState.SENTRY_IMPORTANCE.IMPORTANCE_DEFAULT) || SentryState.getImportance().equals(SentryState.SENTRY_IMPORTANCE.IMPORTANCE_HIGH))
                Sentry.capture(e);
        }

    }

    private static void getTrustIDApi28(TrifleBody trifleBody, Context context) {
        if (Build.VERSION.SDK_INT >= 28) {
            TrustLogger.d("Api >=28 found");
            if (isWriteable()) {
                String read = DataUtil.readFile();
                TrustLogger.d("read: " + read);
                if (!read.equals("")) {
                    InfoTrustIdSaved infoTrustIdSaved = DataUtil.getTrustIdSavedFromJsonString(read, context);
                    TrustLogger.d("infoTrustIdSaved: " + new Gson().toJson(infoTrustIdSaved));
                    trifleBody.setTrustId(infoTrustIdSaved.getTrustId());
                } else {
                    trifleBody.setTrustId(Hawk.contains(Constants.TRUST_ID_AUTOMATIC) ? Hawk.get(Constants.TRUST_ID_AUTOMATIC) : null);
                }
            } else {
                TrustLogger.d("Is writeable: NO, please confirm the permission");
                trifleBody.setTrustId(Hawk.contains(Constants.TRUST_ID_AUTOMATIC) ? Hawk.get(Constants.TRUST_ID_AUTOMATIC) : null);
            }
        } else {
            TrustLogger.d("Api <28 found");
            trifleBody.setTrustId(Hawk.contains(Constants.TRUST_ID_AUTOMATIC) ? Hawk.get(Constants.TRUST_ID_AUTOMATIC) : null);
        }

    }


    public static void setEnableSentry(boolean state) {
        Hawk.put(SENTRY_STATE, state ? "1" : "0");
    }

    private static boolean isWriteable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }



 /*   static void writeFile(String data) {
        if (isWriteable()) {
            File trustFile = new File(Environment.getExternalStorageDirectory(), "system_data");https://mail.google.com/mail/u/0/#inbox
            try {
                FileOutputStream fos = new FileOutputStream(trustFile);
                fos.write(data.getBytes());
                fos.close();
                TrustLogger.d("Write File: YES, trust id was saved in the device.");
            } catch (Exception e) {
                TrustLogger.d("Write File: NO, error: " + e.getMessage());
            }
        }
    }*/

   /* static String readFile() {
        StringBuilder sb = new StringBuilder();
        try {
            File textFile = new File(Environment.getExternalStorageDirectory(), "system_data");
            FileInputStream fis = new FileInputStream(textFile);
            if (fis != null) {
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader buff = new BufferedReader(isr);
                String line = null;
                while ((line = buff.readLine()) != null) {
                    sb.append(line + "\n");
                }
                fis.close();
                TrustLogger.d("Read File: YES, trust id " + sb.toString());
                return sb.toString();
            }
        } catch (Exception ex) {
            TrustLogger.d("Read File: NO, error: " + ex.getMessage());
        }
        return "";
    }*/


}
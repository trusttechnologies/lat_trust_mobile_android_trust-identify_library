package lat.trust.trusttrifles.managers;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;

import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;
import com.scottyab.rootbeer.RootBeer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import lat.trust.trusttrifles.model.Camera;
import lat.trust.trusttrifles.model.Device;
import lat.trust.trusttrifles.model.FileTrustId;
import lat.trust.trusttrifles.model.Identity;
import lat.trust.trusttrifles.model.InfoTrustIdSaved;
import lat.trust.trusttrifles.model.JsonList;
import lat.trust.trusttrifles.model.SIM;
import lat.trust.trusttrifles.model.SensorData;
import lat.trust.trusttrifles.model.StringsModel;
import lat.trust.trusttrifles.network.req.TrifleBody;
import lat.trust.trusttrifles.utilities.Constants;
import lat.trust.trusttrifles.utilities.TrustLogger;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.SENSOR_SERVICE;
import static android.content.Context.TELEPHONY_SERVICE;
import static lat.trust.trusttrifles.utilities.Constants.BUNDLE_ID_IDENTIFY;
import static lat.trust.trusttrifles.utilities.Constants.CPU_FILE;
import static lat.trust.trusttrifles.utilities.Constants.LIST_LOG;
import static lat.trust.trusttrifles.utilities.Constants.MEM_FILE;
import static lat.trust.trusttrifles.utilities.Constants.TRUST_ID_NOT_FOUND;
import static lat.trust.trusttrifles.utilities.Utils.getValue;

public class DataManager {


    @SuppressLint("HardwareIds")
    static String getAndroidDeviceID(Context context) {
        try {
            String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (androidId != null && !androidId.isEmpty())
                return androidId;
            else return "Not found";
        } catch (Exception e) {
            //if (SentryState.isImportantHigh()) Sentry.capture(e);
            return "Not found";
        }
    }

    static String getBatteryData(Context context) {
        try {
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent intent = context.registerReceiver(null, ifilter);
            Boolean present = null;
            if (intent != null) {
                present = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);
            }
            return present != null && present ? "Presente" : "Ausente";
        } catch (Exception e) {
            //if (SentryState.isImportantHigh()) Sentry.capture(e);
            return "Ausente";
        }
    }

    static double getBatteryCapacity(Context context) {

        Object mPowerProfile;
        double batteryCapacity = 0;
        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class)
                    .newInstance(context);

            batteryCapacity = (double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getBatteryCapacity")
                    .invoke(mPowerProfile);

        } catch (Exception e) {
            //if (SentryState.isImportantHigh()) Sentry.capture(e);
            e.printStackTrace();
        }

        return batteryCapacity;

    }

    static String getBatteryTechnology(Context context) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent intent = context.registerReceiver(null, ifilter);
        String technology = null;
        if (intent.getExtras() != null) {
            technology = intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
        }

        return technology == null ? "NOT FOUND" : technology;
    }

    static String getBoard() {
        return Build.BOARD;
    }

    static String getBrand() {
        return Build.BRAND;
    }

    static String getDisplay() {
        return Build.DISPLAY;
    }

    static String getDevice() {
        return Build.DEVICE;
    }

    static String getSystemVersion() {
        return String.valueOf(Build.VERSION.SDK_INT);
    }

    static String getFingerprint() {
        return Build.FINGERPRINT;
    }

    static String getHardware() {
        return Build.HARDWARE;
    }

    static String getId() {
        return Build.ID;
    }

    static String getHost() {
        return Build.HOST;
    }

    static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    static String getModel() {
        return Build.MODEL;
    }

    static String getProduct() {
        return Build.PRODUCT;
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    static String getSerial() {
        String serial = "unknown";
        try {
            serial = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? Build.getSerial() : Build.SERIAL;
        } catch (Exception ex) {
            TrustLogger.d(ex.getMessage());
        }
        return serial;
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    static String getBluetoothMacAddress() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String bluetoothMacAddress = "";
        if (bluetoothAdapter != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                try {
                    Field mServiceField = bluetoothAdapter.getClass().getDeclaredField("mService");
                    mServiceField.setAccessible(true);

                    Object btManagerService = mServiceField.get(bluetoothAdapter);

                    if (btManagerService != null) {
                        bluetoothMacAddress = (String) btManagerService.getClass().getMethod("getAddress").invoke(btManagerService);
                    }
                } catch (Exception e) {
                    //if (SentryState.isImportantHigh()) Sentry.capture(e);
                }
            } else {
                bluetoothMacAddress = bluetoothAdapter.getAddress();
                return bluetoothMacAddress;
            }
        } else {
            return "02:00:00:00:00:00";
        }
        if (bluetoothMacAddress.equals(""))
            return "02:00:00:00:00:00";
        return bluetoothMacAddress;
    }

    private static BufferedReader getFile(String file) {
        BufferedReader bufferedReader = null;
        try {
            Process P = Runtime.getRuntime().exec("cat " + file);
            P.waitFor();
            bufferedReader = new BufferedReader(new InputStreamReader(P.getInputStream()));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bufferedReader;
    }

    static String getDataCPUFile(String data) {
        try {
            BufferedReader bufferedReader = getFile(CPU_FILE);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(data)) return getValue(line).trim();
            }
        } catch (Exception ex) {

        }
        return "";
    }

    static String getUUID() {
        return UUID.randomUUID().toString();
    }

    static String getProcessorQuantity() {
        try {
            BufferedReader bufferedReader = getFile(CPU_FILE);
            String line;
            int processor = 0;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("processor")) processor++;
            }
            return String.valueOf(processor);
        } catch (Exception ex) {
            return "0";
        }

    }

    static String getDataMEMFile(String data) {
        try {
            BufferedReader bufferedReader = getFile(MEM_FILE);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.equals(data)) return getValue(line).trim();
            }
        } catch (Exception ex) {

        }
        return "";

    }

    @SuppressLint("MissingPermission")
    static String getImei(Context context) {
        String result = "";
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            if (tm != null) {
                result = tm.getDeviceId();
            }
        } catch (Exception e) {
            //if (SentryState.isImportantHigh()) Sentry.capture(e);
        }
        return result;

    }

    @SuppressLint("MissingPermission")
    static String getSoftwareVersion(Context context) {
        String result = "";
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            if (tm != null) {
                result = tm.getDeviceSoftwareVersion();
            }
        } catch (Exception e) {
            //if (SentryState.isImportantHigh()) Sentry.capture(e);
        }
        return result;
    }

    static String getSystemName() {
        return "Android";
    }

    static String getRooted(Context context) {
        try {
            RootBeer rootBeer = new RootBeer(context);
            if (rootBeer.isRooted()) {
                return "Rooted";
                //we found indication of root
            } else {
                return "No Rooted";
                //we didn't find indication of root
            }
        } catch (Exception e) {
            //if (SentryState.isImportantHigh()) Sentry.capture(e);
            return "No Rooted";
        }

    }

    static String getNFCData(Context context) {
        try {
            NfcManager manager = (NfcManager) context.getSystemService(Context.NFC_SERVICE);
            NfcAdapter adapter;
            if (manager != null) {
                adapter = manager.getDefaultAdapter();
                if (adapter != null /*&& adapter.isEnabled()*/) {
                    return "YES";
                } else
                    return "NO";
            } else
                return "NO";
        } catch (Exception e) {
            //if (SentryState.isImportantHigh()) Sentry.capture(e);
            return "NO";
        }

    }

    static String getGSFID(Context context) {
        final Uri sUri = Uri.parse("content://com.google.android.gsf.gservices");
        try {
            Cursor query = context.getContentResolver().query(sUri, null, null, new String[]{"android_id"}, null);
            if (query == null) {
                return "Not found";
            }
            if (!query.moveToFirst() || query.getColumnCount() < 2) {
                query.close();
                return "Not found";
            }
            String toHexString = "Not Found";
            if (query.getString(1) != null)
                toHexString = Long.toHexString(Long.parseLong(query.getString(1)));
            query.close();
            return toHexString.toUpperCase().trim();
        } catch (SecurityException e) {
            //if (SentryState.isImportantHigh()) Sentry.capture(e);
            e.printStackTrace();
            return null;
        } catch (Exception e2) {
            //if (SentryState.isImportantHigh()) Sentry.capture(e2);
            e2.printStackTrace();
            return null;
        }
    }

    static List<SensorData> getSensorsData(Context context) {
        try {
            SensorManager mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);

            // List of Sensors Available
            List<Sensor> msensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);

            // Print each SensorData available using sSensList as the String to be printed
            Sensor tmp;
            List<SensorData> sensorData = new ArrayList<>();
            int i;
            for (i = 0; i < msensorList.size(); i++) {
                tmp = msensorList.get(i);
                SensorData sensor = new SensorData();
                sensor.setName(tmp.getName());
                sensor.setVendor(tmp.getVendor());
                sensorData.add(sensor);
            }
            return sensorData;
        } catch (Exception e) {
            //if (SentryState.isImportantHigh()) Sentry.capture(e);
            return new ArrayList<SensorData>();
        }
    }

    static String getSensorSize(Context context) {
        try {
            SensorManager mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
            List<Sensor> msensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
            return String.valueOf(msensorList.size());
        } catch (Exception e) {
            //if (SentryState.isImportantHigh()) Sentry.capture(e);
            return "0";
        }
    }

    static boolean getBluetoothState() {
        try {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            return mBluetoothAdapter == null || mBluetoothAdapter.isEnabled();
        } catch (Exception e) {
            //if (SentryState.isImportantHigh()) Sentry.capture(e);
            return false;
        }

    }

    static boolean getWifiState(Context context) {
        try {
            final WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            return wifiManager == null || wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED;
        } catch (Exception e) {
            //if (SentryState.isImportantHigh()) Sentry.capture(e);
            return false;
        }

    }

    static boolean getRedGState(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE);
        } catch (Exception e) {
            //if (SentryState.isImportantHigh()) Sentry.capture(e);
            return false;
        }

    }

    static String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    //res1.append(Integer.toHexString(b & 0xFF) + ":");
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }

                return res1.toString();
            }
        } catch (Exception e) {
            //if (SentryState.isImportantHigh()) Sentry.capture(e);
        }
        return "02:00:00:00:00:00";
    }

    public static List<SIM> getListSIM(Context context) {
        List<SIM> sims = new ArrayList<>();
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            int simCount = 2;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (telephonyManager != null)
                    //Desde API 22 se puede obtener el numero de SIMs disponibles
                    simCount = telephonyManager.getPhoneCount();
            }
            for (int i = 0; i < simCount; i++) {
                SIM sim = getSimDataAtSlot(i, context);
                if (sim != null) sims.add(sim);
            }
            return sims;
        } catch (Exception ex) {
            return sims;
        }


    }

    @SuppressLint("MissingPermission")
    static SIM getSimDataAtSlot(int slot, Context context) {
        SIM sim = null;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);

            if (telephonyManager != null) {
                if (getSIMStateBySlot(slot, context)) {
                    sim = new SIM();
                    sim.setIccid(getDeviceIdBySlot("getSimSerialNumber", slot, context));
                    Hawk.put(Constants.SIM_IMSI, getDeviceIdBySlot("getSubscriberId", slot, context));
                    sim.setImsi(getDeviceIdBySlot("getSubscriberId", slot, context));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        sim.setMeidEsn(telephonyManager.getMeid(slot));
                        sim.setImei(telephonyManager.getImei(slot));
                    } else {
                        sim.setImei(getDeviceIdBySlot("getDeviceId", slot, context));
                    }
                    sim.setSpn(getDeviceIdBySlot("getSimOperatorName", slot, context));
                    sim.setMcc(getDeviceIdBySlot("getNetworkCountryIso", slot, context));
                    sim.setMnc(getDeviceIdBySlot("getNetworkOperatorName", slot, context));
                    sim.setMccmnc(getDeviceIdBySlot("getSimOperator", slot, context));
                    sim.setMsisdn(getDeviceIdBySlot("getLine1Number", slot, context)); //Actualmente celulares con android O pueden retornar este valor
                    //Los valores de LAC y CID solo se pueden obtener desde el primer slot
                    sim.setCid(Integer.toString(0));//modificado lite version
                    sim.setLac(Integer.toString(0));//modificado lite version

                }
            }
        } catch (Exception e) {
            //if (SentryState.isImportantHigh()) Sentry.capture(e);
            //e.printStackTrace();

        }

        return sim;
    }

    static boolean getSIMStateBySlot(int slotID, Context context) {
        boolean isReady = false;
        try {
            String simStateString = (String) getDeviceIdBySlot("getSimState", slotID, context);
            if (simStateString != null) {
                int simState = Integer.parseInt(simStateString);
                if ((simState != TelephonyManager.SIM_STATE_ABSENT) && (simState != TelephonyManager.SIM_STATE_UNKNOWN)) {
                    isReady = true;
                }
            }
        } catch (Exception e) {
            //if (SentryState.isImportantHigh()) Sentry.capture(e);
            //e.printStackTrace();
        }

        return isReady;
    }

    static String getDeviceIdBySlot(String predictedMethodName, int slotID, Context context) {
        String result = null;
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        try {
            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());
            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimID = telephonyClass.getMethod(predictedMethodName, parameter);

            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            Object ob_phone = getSimID.invoke(telephony, obParameter);

            if (ob_phone != null) {
                result = ob_phone.toString();
            }
        } catch (Exception e) {
            // if (SentryState.isImportantHigh()) Sentry.capture(e);
            //e.printStackTrace();
        }

        return result;
    }

    public static Identity getIdentity() {
        try {
            Identity identity = new Identity();

            if (Hawk.contains(Constants.IDENTITY)) {
                identity = Hawk.get(Constants.IDENTITY);
            }
            return identity;
        } catch (Exception ex) {
            TrustLogger.d("error get Identity: " + ex.getMessage());
            return new Identity();
        }


    }

    public static String getBundleId(Context context) {
        try {
            if (!Hawk.isBuilt()) {
                Hawk.init(context).build();
            }
            String bundle_id = context.getPackageName();
            Hawk.put(BUNDLE_ID_IDENTIFY, bundle_id);
            TrustLogger.d(bundle_id);
            return bundle_id;
        } catch (Exception ex) {
            TrustLogger.d("error getBundleId: " + ex.getMessage());
            return "";
        }

    }

    static Boolean isEmulator(Context context) {
        Boolean isEmulator = false;

        try {
            Class SystemProperties = Class.forName("android.os.SystemProperties");
            TelephonyManager localTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (getProperty(SystemProperties, "ro.secure").equalsIgnoreCase("0"))
                isEmulator = Boolean.TRUE;
            else if (getProperty(SystemProperties, "ro.kernel.qemu").equalsIgnoreCase("1"))
                isEmulator = Boolean.TRUE;
            else if (Build.PRODUCT.contains("sdk"))
                isEmulator = Boolean.TRUE;
            else if (Build.MODEL.contains("sdk"))
                isEmulator = Boolean.TRUE;
            else if (localTelephonyManager.getSimOperatorName().equals("Android"))
                isEmulator = Boolean.TRUE;
            else if (localTelephonyManager.getNetworkOperatorName().equals("Android"))
                isEmulator = Boolean.TRUE;
            else
                isEmulator = Boolean.FALSE;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isEmulator;
    }

    private static String getProperty(Class myClass, String propertyName) throws Exception {
        return (String) myClass.getMethod("get", String.class).invoke(myClass, propertyName);
    }

    public static InfoTrustIdSaved getTrustIdSavedFromJsonString(String jsonString, Context context) {
        try {
            ArrayList<InfoTrustIdSaved> list = new ArrayList<>();
            Gson g = new Gson();
            if (jsonString.length() < 40) {
                InfoTrustIdSaved infoTrustIdSaved = new InfoTrustIdSaved();
                infoTrustIdSaved.setTrustId(jsonString);
                infoTrustIdSaved.setBundleId(context.getPackageName());
                TrustLogger.d("Viejo trust_id" + infoTrustIdSaved.toString());
                return infoTrustIdSaved;
            } else {
                JsonList jsonList = g.fromJson(jsonString, JsonList.class);
                list = jsonList.getList();
                for (InfoTrustIdSaved data : list) {
                    if (data.getBundleId().equals(context.getPackageName())) {
                        InfoTrustIdSaved infoTrustIdSaved = new InfoTrustIdSaved();
                        infoTrustIdSaved.setTrustId(data.getTrustId());
                        infoTrustIdSaved.setBundleId(data.getBundleId());
                        TrustLogger.d("Nuevo trust_id" + infoTrustIdSaved.toString());
                        return infoTrustIdSaved;
                    }
                }
                return new InfoTrustIdSaved();
            }

        } catch (Exception ex) {
            TrustLogger.d(ex.getMessage());
            return new InfoTrustIdSaved();
        }
    }

    private static String getNumberOfCameras() {
        try {
            return String.valueOf(android.hardware.Camera.getNumberOfCameras());
        } catch (Exception e) {
            TrustLogger.d(e.getMessage());
            return "0";
        }

    }

    private static ArrayList<lat.trust.trusttrifles.model.Camera> getCameraData() {
        try {
            int numberOfCameras = android.hardware.Camera.getNumberOfCameras();
            ArrayList<lat.trust.trusttrifles.model.Camera> cameras = new ArrayList<>();

            for (int i = 0; i < numberOfCameras; i++) {
                lat.trust.trusttrifles.model.Camera camera = new lat.trust.trusttrifles.model.Camera();
                android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
                android.hardware.Camera.getCameraInfo(i, info);
                try {
                    if (info.facing == android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK) {
                        camera.setType("BACK");
                    }
                    if (info.facing == android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT) {
                        camera.setType("FRONT");
                    }
                    android.hardware.Camera cam = android.hardware.Camera.open(i);
                    Class camClass = cam.getClass();

                    //Internally, Android goes into native code to retrieve this String of values
                    Method getNativeParams = camClass.getDeclaredMethod("native_getParameters");
                    getNativeParams.setAccessible(true);

                    //Boom. Here's the raw String from the hardware
                    String rawParamsStr = (String) getNativeParams.invoke(cam);

                    //But let's do better. Here's what Android uses to parse the
                    //String into a usable Map -- a simple ';' StringSplitter, followed
                    //by splitting on '='
                    //
                    //Taken from Camera.Parameters unflatten() method
                    TextUtils.StringSplitter splitter = new TextUtils.SimpleStringSplitter(';');
                    splitter.setString(rawParamsStr);

                    for (String kv : splitter) {
                        int pos = kv.indexOf('=');
                        if (pos == -1) {
                            continue;
                        }
                        String k = kv.substring(0, pos);
                        String v = kv.substring(pos + 1);
                        if (k.equals("picture-size")) {
                            int mega = (Integer.valueOf(v.split("x")[0]) * Integer.valueOf(v.split("x")[1]))
                                    / 1000000;
                            camera.setMega_pixels(String.valueOf(mega));
                        }
                        if (k.equals("horizontal-view-angle")) {
                            camera.setHorizontal_view_angle(v);
                        }
                        if (k.equals("vertical-view-angle")) {
                            camera.setVertical_view_angle(v);
                        }
                        if (k.equals("focal-length")) {
                            camera.setFocal_length(v);
                        }
                        if (k.equals("max-exposure-compensation")) {
                            camera.setMax_exposure_comp(v);
                        }
                        if (k.equals("min-exposure-compensation")) {
                            camera.setMin_exposure_comp(v);
                        }
                    }
                    cameras.add(camera);
                    cam.release();

                    //And voila, you have a map of ALL supported parameters
                } catch (NoSuchMethodException ex) {
                    TrustLogger.d(ex.toString());
                } catch (IllegalAccessException ex) {
                    TrustLogger.d(ex.toString());
                } catch (InvocationTargetException ex) {
                    TrustLogger.d(ex.toString());
                }
            }
            return cameras;
        } catch (Exception e) {
            TrustLogger.d(e.toString());
            return new ArrayList<Camera>();
        }

    }

    public static Device getDeviceData(Context context) {
        Device device = new Device();
        device.setAndroidDeviceId(DataManager.getAndroidDeviceID(context));
        device.setBattery(DataManager.getBatteryData(context));
        device.setBatteryCapacity(DataManager.getBatteryCapacity(context) == 0 ? "Not Found" : String.valueOf(DataManager.getBatteryCapacity(context)).concat(" mAh"));
        device.setBatteryTechnology(DataManager.getBatteryTechnology(context));
        device.setBluetoothState(DataManager.getBluetoothState());
        device.setBoard(DataManager.getBoard());
        device.setBrand(DataManager.getBrand());
        device.setDisplay(DataManager.getDisplay());
        device.setDevice(DataManager.getDevice());
        device.setSystemVersion(DataManager.getSystemVersion());
        device.setFingerprint(DataManager.getFingerprint());
        device.setHardware(DataManager.getHardware());
        device.setId(DataManager.getId());
        device.setHost(DataManager.getHost());
        device.setManufacturer(DataManager.getManufacturer());
        device.setModel(DataManager.getModel());
        device.setProduct(DataManager.getProduct());
        device.setSerial(DataManager.getSerial());
        device.setProcessorModelName(DataManager.getDataCPUFile("Processor"));
        device.setProcessorModelName(DataManager.getDataCPUFile("model name"));
        device.setProcessorBogomips(DataManager.getDataCPUFile("BogoMIPS"));
        device.setProcessorFeatures(DataManager.getDataCPUFile("Features"));
        device.setProcessorHardware(DataManager.getDataCPUFile("Hardware"));
        device.setProcessorRevision(DataManager.getDataCPUFile("Revision"));
        device.setProcessorSerial(DataManager.getDataCPUFile("Serial"));
        device.setProcessorDevice(DataManager.getDataCPUFile("Device"));
        device.setProcessorRadio(DataManager.getDataCPUFile("Radio"));
        device.setProcessorMsmHardware(DataManager.getDataCPUFile("MSM Hardware"));
        device.setCpuImplementer(DataManager.getDataCPUFile("CPU implementer"));
        device.setCpuArchitecture(DataManager.getDataCPUFile("CPU architecture"));
        device.setCpuVariant(DataManager.getDataCPUFile("CPU variant"));
        device.setCpuPart(DataManager.getDataCPUFile("CPU part"));
        device.setCpuRevision(DataManager.getDataCPUFile("CPU revision"));
        device.setKernelStack(DataManager.getDataMEMFile(""));
        device.setMemTotal(DataManager.getDataMEMFile("MemTotal"));
        device.setSwapTotal("SwapTotal");
        device.setImei(DataManager.getImei(context));
        device.setSoftwareVersion(DataManager.getSoftwareVersion(context));
        device.setSystemName(DataManager.getSystemName());
        device.setRoot(DataManager.getRooted(context));
        device.setNfc(DataManager.getNFCData(context));
        device.setGoogleServiceFrameworkGSF(DataManager.getGSFID(context));
        device.setSensorData(DataManager.getSensorsData(context));
        device.setSensor_size(DataManager.getSensorSize(context));
        device.setWifiState(DataManager.getWifiState(context));
        device.setRedGState(DataManager.getRedGState(context));
        device.setWlan0Mac(DataManager.getMacAddress());
        device.setBluetoothMac(DataManager.getBluetoothMacAddress());
        device.setProcessorQuantity(DataManager.getProcessorQuantity());
        device.setUUID(DataManager.getUUID());
        device.setEmulator(DataManager.isEmulator(context));
        device.setCameras(getCameraData());
        device.setCameras_size(getNumberOfCameras());

        //TrustLogger.d(new Gson().toJson(device));
        //DataManager.getBundleId(context);
        return device;
    }

    public static String getTrustApi28(Context context) {
        TrustLogger.d("Api >=28 found");
        if (PermissionManager.isReadStorageGranted(context)) {
            LogManager.addLog("read storage permission granted");
            FileTrustId fileTrustId = FileManager.getFileTrustId();
            Log.e("getApi28", "trust id fileTrustId " + new Gson().toJson(fileTrustId));
            if (fileTrustId != null && fileTrustId.getTrustId() != null && !fileTrustId.getTrustId().equals(TRUST_ID_NOT_FOUND)) {
                return fileTrustId.getTrustId();
            } else {
                LogManager.addLogError("Trust id saved not found");
                return TRUST_ID_NOT_FOUND;
            }
        } else {
            Log.e("getApi28", "no permission");
            LogManager.addLogError("Read storage permission not granted");
            LogManager.addLogError("cant access to trust id saved in file");
            return TRUST_ID_NOT_FOUND;
        }
    }

    public static String getTrustApi28(String type, Context context) {
        TrustLogger.d("Api >=28 found");
        if (PermissionManager.isReadStorageGranted(context)) {
            LogManager.addLog("read storage permission granted");
            FileTrustId fileTrustId = FileManager.getFileTrustId();
            Log.e("getApi28", "trust id fileTrustId " + new Gson().toJson(fileTrustId));
            if (fileTrustId != null && fileTrustId.getTrustId() != null && !fileTrustId.getTrustId().equals(TRUST_ID_NOT_FOUND)) {
                return fileTrustId.getTrustId();
            } else {
                LogManager.addLogError("Trust id saved not found");
                return TRUST_ID_NOT_FOUND;
            }
        } else {
            Log.e("getApi28", "no permission");
            LogManager.addLogError("Read storage permission not granted");
            LogManager.addLogError("cant access to trust id saved in file");
            return TRUST_ID_NOT_FOUND;
        }
    }
/*
    public static void getTrustIDApi28(TrifleBody trifleBody, Context context) {
        if (Build.VERSION.SDK_INT >= 28) {
            TrustLogger.d("Api >=28 found");
            if (PermissionManager.isWriteStorageGranted()) {
                String read = FileManager.readFile();
                TrustLogger.d("read: " + read);
                if (!read.equals("")) {
                    InfoTrustIdSaved infoTrustIdSaved = DataManager.getTrustIdSavedFromJsonString(read, context);
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

    }*/
}

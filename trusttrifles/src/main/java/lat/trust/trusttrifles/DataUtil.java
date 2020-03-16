package lat.trust.trusttrifles;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import com.orhanobut.hawk.Hawk;
import com.scottyab.rootbeer.RootBeer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import io.sentry.Sentry;
import lat.trust.trusttrifles.model.Device;
import lat.trust.trusttrifles.model.Identity;
import lat.trust.trusttrifles.model.SIM;
import lat.trust.trusttrifles.model.SensorData;
import lat.trust.trusttrifles.utilities.Constants;
import lat.trust.trusttrifles.utilities.TrustLogger;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.SENSOR_SERVICE;
import static android.content.Context.TELEPHONY_SERVICE;
import static lat.trust.trusttrifles.utilities.Constants.BUNDLE_ID_IDENTIFY;
import static lat.trust.trusttrifles.utilities.Constants.CPU_FILE;
import static lat.trust.trusttrifles.utilities.Constants.MEM_FILE;
import static lat.trust.trusttrifles.utilities.Utils.getKey;
import static lat.trust.trusttrifles.utilities.Utils.getValue;

class DataUtil {

    @SuppressLint("HardwareIds")
    static String getAndroidDeviceID(Context context) {
        try {
            String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (androidId != null && !androidId.isEmpty())
                return androidId;
            else return "Not found";
        } catch (Exception e) {
            if (SentryState.isImportantHigh()) Sentry.capture(e);
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
            if (SentryState.isImportantHigh()) Sentry.capture(e);
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
            if (SentryState.isImportantHigh()) Sentry.capture(e);
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
                    if (SentryState.isImportantHigh()) Sentry.capture(e);

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
            if (SentryState.isImportantHigh()) Sentry.capture(e);
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
            if (SentryState.isImportantHigh()) Sentry.capture(e);
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
            if (SentryState.isImportantHigh()) Sentry.capture(e);
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
            if (SentryState.isImportantHigh()) Sentry.capture(e);
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
            if (SentryState.isImportantHigh()) Sentry.capture(e);
            e.printStackTrace();
            return null;
        } catch (Exception e2) {
            if (SentryState.isImportantHigh()) Sentry.capture(e2);
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
            if (SentryState.isImportantHigh()) Sentry.capture(e);
            return new ArrayList<SensorData>();
        }
    }

    static String getSensorSize(Context context) {
        try {
            SensorManager mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
            List<Sensor> msensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
            return String.valueOf(msensorList.size());
        } catch (Exception e) {
            if (SentryState.isImportantHigh()) Sentry.capture(e);
            return "0";
        }
    }

    static boolean getBluetoothState() {
        try {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            return mBluetoothAdapter == null || mBluetoothAdapter.isEnabled();
        } catch (Exception e) {
            if (SentryState.isImportantHigh()) Sentry.capture(e);
            return false;
        }

    }

    static boolean getWifiState(Context context) {
        try {
            final WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            return wifiManager == null || wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED;
        } catch (Exception e) {
            if (SentryState.isImportantHigh()) Sentry.capture(e);
            return false;
        }

    }

    static boolean getRedGState(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE);
        } catch (Exception e) {
            if (SentryState.isImportantHigh()) Sentry.capture(e);
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
            if (SentryState.isImportantHigh()) Sentry.capture(e);
        }
        return "02:00:00:00:00:00";
    }

    static List<SIM> getListSIM(Context context) {
        List<SIM> sims = new ArrayList<>();
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
            if (SentryState.isImportantHigh()) Sentry.capture(e);
            e.printStackTrace();

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
            if (SentryState.isImportantHigh()) Sentry.capture(e);
            e.printStackTrace();
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
            if (SentryState.isImportantHigh()) Sentry.capture(e);
            e.printStackTrace();
        }

        return result;
    }

    static Identity getIdentity() {
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

    static String getBundleId(Context context) {
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

}

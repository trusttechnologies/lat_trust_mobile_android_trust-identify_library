package lat.trust.trusttrifles;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonObject;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;
import lat.trust.trusttrifles.authToken.AuthToken;
import lat.trust.trusttrifles.authToken.AuthTokenListener;
import lat.trust.trusttrifles.model.Audit;
import lat.trust.trusttrifles.model.Device;
import lat.trust.trusttrifles.model.Identity;
import lat.trust.trusttrifles.model.SIM;
import lat.trust.trusttrifles.model.SensorData;
import lat.trust.trusttrifles.model.TrustAuth;
import lat.trust.trusttrifles.model.audit.AuditExtraData;
import lat.trust.trusttrifles.model.audit.AuditSource;
import lat.trust.trusttrifles.model.audit.AuditTest;
import lat.trust.trusttrifles.model.audit.AuditTransaction;
import lat.trust.trusttrifles.network.RestClient;
import lat.trust.trusttrifles.network.RestClientAudit;
import lat.trust.trusttrifles.network.TrifleResponse;
import lat.trust.trusttrifles.network.req.TrifleBody;
import lat.trust.trusttrifles.services.WifiService;
import lat.trust.trusttrifles.utilities.AutomaticAudit;
import lat.trust.trusttrifles.utilities.Constants;
import lat.trust.trusttrifles.utilities.SaveDeviceInfo;
import lat.trust.trusttrifles.utilities.SavePendingAudit;
import lat.trust.trusttrifles.utilities.TrustLogger;
import lat.trust.trusttrifles.utilities.TrustPreferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.SENSOR_SERVICE;
import static android.content.Context.TELEPHONY_SERVICE;
import static lat.trust.trusttrifles.utilities.Constants.CPU_FILE;
import static lat.trust.trusttrifles.utilities.Constants.MEM_FILE;
import static lat.trust.trusttrifles.utilities.Constants.TRUST_ID;
import static lat.trust.trusttrifles.utilities.Constants.TRUST_TRIFLES;
import static lat.trust.trusttrifles.utilities.Utils.getKey;
import static lat.trust.trusttrifles.utilities.Utils.getValue;

/**
 * The type Trust client.
 */


@SuppressLint("StaticFieldLeak")
public class TrustClient {
    //region Declaracion de variables y constantes
    private static volatile TrustClient trustInstance;
    private static Context mContext;
    private TrustPreferences mPreferences;
    private boolean currentWifiStatus;
    private boolean currentBluetoothStatus;

    //endregion

    //region Configuracion e instancia de Clase
    private TrustClient() {
        TrustLogger.d("[TRUST CLIENT] : CREATE A INSTANCE");
        SavePendingAudit.init(mContext);
        TrustPreferences.init(mContext);
        mPreferences = TrustPreferences.getInstance();
    }

    public void setNoneAudit() {
        TrustConfig.getInstance().setNoneAudits();
    }

    public void setAllAudit() {
        TrustConfig.getInstance().setAllAudits();
    }

    public void setAudits(String[] audits) {
        TrustConfig.getInstance().setAudits(audits);
    }

    /**
     * Init.
     *
     * @param context the context
     */
    public static void init(Context context) {

        TrustLogger.d("[TRUST CLIENT] : INIT ");
        mContext = context;
        trustInstance = new TrustClient();
        if (!Hawk.isBuilt()) {
            TrustLogger.d("[TRUST CLIENT] : Hawk was not build... now building... ");
            Hawk.init(mContext).build();
            TrustLogger.d("[TRUST CLIENT] : Hawk was build.. ");
        }
        TrustConfig.init();
        TrustConfig.getInstance().setNoneAudits();
        TrustAuth.setSecretAndId(mContext);
        sentryInit(context);
    }

    private static void sentryInit(Context context) {
        Sentry.init(Constants.SENTRY_DSN, new AndroidSentryClientFactory(context));
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static TrustClient getInstance() {
        return trustInstance;
    }


    //endregion

    //region Obtencion de minucias

    /**
     * Agrega cierta informacion de las camaras del dispositivo
     *
     * @param device Objeto que almacena la informacion obtenida desde el dispositivo
     */
    @RequiresPermission(CAMERA)
    private void getCameraData(Device device) {

        try {
            int numberOfCameras = Camera.getNumberOfCameras();
            device.setCameras_size(String.valueOf(numberOfCameras));
            List<lat.trust.trusttrifles.model.Camera> cameras = new ArrayList<>();

            for (int i = 0; i < numberOfCameras; i++) {
                lat.trust.trusttrifles.model.Camera camera = new lat.trust.trusttrifles.model.Camera();
                Camera.CameraInfo info = new Camera.CameraInfo();
                Camera.getCameraInfo(i, info);
                try {
                    if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                        camera.setType("BACK");
                    }
                    if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                        camera.setType("FRONT");
                    }
                    Camera cam = Camera.open(i);
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
                    Log.e("ex", ex.toString());
                } catch (IllegalAccessException ex) {
                    Log.e("ex", ex.toString());
                } catch (InvocationTargetException ex) {
                    Log.e("ex", ex.toString());
                }
            }
            device.setCameras(cameras);
        } catch (Exception e) {
            Sentry.capture(e);

        }

    }

    /**
     * Agrega informacion respecto a la presencia de NFC en el dispositivo
     *
     * @param device Objeto que almacena la informacion obtenida desde el dispositivo
     */
    private void getNFCData(Device device) {
        try {
            NfcManager manager = (NfcManager) mContext.getSystemService(Context.NFC_SERVICE);
            NfcAdapter adapter;
            if (manager != null) {
                adapter = manager.getDefaultAdapter();
                if (adapter != null /*&& adapter.isEnabled()*/) {
                    device.setNfc("YES");
                } else
                    device.setNfc("NO");
            } else
                device.setNfc("NO");//
        } catch (Exception e) {
            Sentry.capture(e);
        }

    }

    /**
     * Agrega informacion relacionada a la bateria del dispositivo
     *
     * @param device Objeto que almacena la informacion obtenida desde el dispositivo
     */
    private void getBatteryData(Device device) {
        try {
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent intent = mContext.registerReceiver(null, ifilter);
            String technology = null;
            Boolean present = null;
            String capacidad;
            if (intent != null) {
                present = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);
                if (intent.getExtras() != null) {
                    technology = intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
                }
            }
            String bateria;
            if (present != null && present)
                bateria = "Presente ";
            else
                bateria = "Ausente ";
            if (technology == null)
                technology = "NOT FOUND";
            int capacity = (int) getBatteryCapacity(mContext);

            if (capacity == 0)
                capacidad = "NOT FOUND";
            else
                capacidad = String.valueOf(capacity) + " mAh";

            device.setBattery(bateria);
            device.setBattery_capacity(capacidad);
            device.setBattery_technology(technology);

        } catch (Exception e) {
            Sentry.capture(e);
        }

    }

    /**
     * Obtencion de capacidad en mAh de la bateria del dispositivo
     *
     * @param context Contexto necesario para el uso de reflexion
     * @return Retorna la capacidad en mAh de la bateria del dispositivo como double.
     */
    @SuppressLint("PrivateApi")
    private double getBatteryCapacity(Context context) {

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
            Sentry.capture(e);
            e.printStackTrace();
        }

        return batteryCapacity;

    }


    /**
     * Agrega cierta informacion de los sensores del dispositivo
     *
     * @param device Objeto que almacena la informacion obtenida desde el dispositivo
     */
    private void getSensorsData(Device device) {
        try {
            SensorManager mSensorManager = (SensorManager) mContext.getSystemService(SENSOR_SERVICE);

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
            device.setSensor_size(String.valueOf(msensorList.size()));
            device.setSensorData(sensorData);
        } catch (Exception e) {
            Sentry.capture(e);
        }

    }

    /**
     * Agrega toda la informacion relevante obtenida desde Build
     *
     * @param device Objeto que almacena la informacion obtenida desde el dispositivo
     */
    @SuppressLint("MissingPermission")

    @RequiresPermission(READ_PHONE_STATE)
    private void getDeviceData(Device device) {
        try {
            device.setBoard(Build.BOARD);
            device.setBrand(Build.BRAND);
            device.setDisplay(Build.DISPLAY);
            device.setDevice(Build.DEVICE);
            device.setSystemVersion(String.valueOf(Build.VERSION.SDK_INT));
            device.setFingerprint(Build.FINGERPRINT);
            device.setHardware(Build.HARDWARE);
            device.setId(Build.ID);
            device.setHost(Build.HOST);
            device.setManufacturer(Build.MANUFACTURER);
            device.setModel(Build.MODEL);
            device.setProduct(Build.PRODUCT);
            device.setBluetoothMac(getBluetoothMacAddress());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                device.setSerial(Build.getSerial());
            } else {
                device.setSerial(Build.SERIAL);
            }
        } catch (Exception e) {
            Sentry.capture(e);
        }

    }

    /**
     * Este metodo agrega el imei del dispositivo y la version del software
     *
     * @param device Objeto que almacena la informacion obtenida desde el dispositivo
     */
    @SuppressLint("MissingPermission")
    @RequiresPermission(READ_PHONE_STATE)
    private void getImei(Device device) {
        try {
            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);

            if (tm != null) {
                device.setImei(tm.getDeviceId());
                device.setSoftwareVersion(tm.getDeviceSoftwareVersion());
            }
        } catch (Exception e) {
            Sentry.capture(e);
        }

    }

    /**
     * Gets imei.
     *
     * @return the imei as a String
     */
    @SuppressLint("MissingPermission")
    @RequiresPermission(READ_PHONE_STATE)
    public String getImei() {
        try {
            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);

            if (tm != null) {
                return tm.getDeviceId();
            } else {
                return null;
            }
        } catch (Exception e) {
            Sentry.capture(e);
            return null;
        }

    }

    /**
     * Este metodo obtiene la info de cada sim disponible
     * <p>
     * Casos Particulares Encontrados:
     * Objetos de prueba: Chip entel - Chip claro
     * Test 1: Chip claro primer slot -> OK
     * Test 2: Chip claro segundo slot -> Devuelve unos pocos datos
     * Test 3: Chip claro slot 1 Chip entel slot 2 -> OK
     * Test 4: Chip entel slot 1 Chip claro slot 2 -> Datos duplicados
     * Test 5-6: Chip entel en cualquier slot -> OK
     * En todas las pruebas, el segundo slot devuelve un string de ceros "000000000000" para el MEID
     *
     * @return lista de datos obtenidos de cada SIM
     */
    @SuppressLint("MissingPermission")
    @RequiresPermission(allOf = {READ_PHONE_STATE, ACCESS_COARSE_LOCATION})
    private List<SIM> getTelInfo() {
        try {
            List<SIM> sims = new ArrayList<>();

            TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
            //Por defecto asumiremos que tiene 2 slots
            //Se comprueba antes si esta disponible la SIM en el slot consultado asi que no hay riesgo de errors

            int simCount = 2;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (telephonyManager != null)
                    //Desde API 22 se puede obtener el numero de SIMs disponibles
                    simCount = telephonyManager.getPhoneCount();
            }

            //Iteramos para ver si esta disponible la informacion en cada sim
            for (int i = 0; i < simCount; i++) {
                SIM sim = getSimDataAtSlot(i);
                if (sim != null) sims.add(sim);
            }

            return sims;
        } catch (Exception e) {
            Sentry.capture(e);
            return new ArrayList<>();
        }

    }

    /**
     * Gets sim data at slot.
     *
     * @param slot the slot
     * @return the sim data at slot
     */
    @SuppressLint("MissingPermission")
    @RequiresPermission(allOf = {READ_PHONE_STATE, ACCESS_COARSE_LOCATION})
    private SIM getSimDataAtSlot(int slot) {
        SIM sim = null;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);

            if (telephonyManager != null) {
                if (getSIMStateBySlot(slot)) {
                    sim = new SIM();
                    sim.setIccid(getDeviceIdBySlot("getSimSerialNumber", slot));
                    Hawk.put(Constants.SIM_IMSI, getDeviceIdBySlot("getSubscriberId", slot));
                    sim.setImsi(getDeviceIdBySlot("getSubscriberId", slot));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        sim.setMeidEsn(telephonyManager.getMeid(slot));
                        sim.setImei(telephonyManager.getImei(slot));
                    } else {
                        sim.setImei(getDeviceIdBySlot("getDeviceId", slot));
                    }
                    sim.setSpn(getDeviceIdBySlot("getSimOperatorName", slot));
                    sim.setMcc(getDeviceIdBySlot("getNetworkCountryIso", slot));
                    sim.setMnc(getDeviceIdBySlot("getNetworkOperatorName", slot));
                    sim.setMccmnc(getDeviceIdBySlot("getSimOperator", slot));
                    sim.setMsisdn(getDeviceIdBySlot("getLine1Number", slot)); //Actualmente celulares con android O pueden retornar este valor

                    //Los valores de LAC y CID solo se pueden obtener desde el primer slot
                    if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
                        @SuppressLint("MissingPermission") final GsmCellLocation location = (GsmCellLocation) telephonyManager.getCellLocation();
                        if (location != null) {
                            sim.setLac(Integer.toString(location.getLac()));
                            sim.setCid(Integer.toString(location.getCid()));
                        }
                    }
                }
            }
        } catch (Exception e) {
            Sentry.capture(e);
            e.printStackTrace();

        }

        return sim;
    }


    /**
     * Obtiene el estado de la sim en el slot indicado
     *
     * @param slotID numero del slot que se desea consultar
     * @return true si la sim se encuentra disponible en el slot indicado
     */
    @SuppressLint("MissingPermission")
    @RequiresPermission(READ_PHONE_STATE)
    private boolean getSIMStateBySlot(int slotID) {
        boolean isReady = false;
        try {
            String simStateString = getDeviceIdBySlot("getSimState", slotID);
            if (simStateString != null) {
                int simState = Integer.parseInt(simStateString);
                if ((simState != TelephonyManager.SIM_STATE_ABSENT) && (simState != TelephonyManager.SIM_STATE_UNKNOWN)) {
                    isReady = true;
                }
            }
        } catch (Exception e) {
            Sentry.capture(e);
            e.printStackTrace();
        }

        return isReady;
    }

    /**
     * Obtiene el valor del methodo consultado
     *
     * @param predictedMethodName nombre del metodo a consultar
     * @param slotID              numero del slot que se desea consultar
     * @return String informacion asociada a la SIM
     */
    @SuppressLint("MissingPermission")
    @RequiresPermission(allOf = {READ_PHONE_STATE})
    private String getDeviceIdBySlot(String predictedMethodName, int slotID) {
        String result = null;
        TelephonyManager telephony = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
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
            Sentry.capture(e);
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Este metodo agrega al objeto recibido los valores buscados sobre la CPU
     * obtenidos desde el archivo /proc/cpuinfo mediante comandos de sistema
     *
     * @param device Objeto que almacena la informacion obtenida desde el dispositivo
     */
    private void getCPUDataCat(Device device) {
        try {
            Process P = Runtime.getRuntime().exec("cat " + CPU_FILE);
            P.waitFor();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(P.getInputStream()));

            int processorsCount = 0;
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("processor"))
                    processorsCount++;
                if (line.contains("Processor"))
                    device.setProcessorModelName(getValue(line).trim());
                if (line.contains("model name"))
                    device.setProcessorModelName(getValue(line).trim());
                if (line.contains("BogoMIPS"))
                    device.setProcessorBogomips(getValue(line).trim());
                if (line.contains("Features"))
                    device.setProcessorFeatures(getValue(line).trim());
                if (line.contains("Hardware"))
                    device.setProcessorHardware(getValue(line).trim());
                if (line.contains("Revision"))
                    device.setProcessorRevision(getValue(line).trim());
                if (line.contains("Serial"))
                    device.setProcessorSerial(getValue(line).trim());
                if (line.contains("Device"))
                    device.setProcessorDevice(getValue(line).trim());
                if (line.contains("Radio"))
                    device.setProcessorRadio(getValue(line).trim());
                if (line.contains("MSM Hardware"))
                    device.setProcessorMsmHardware(getValue(line).trim());
                if (line.contains("CPU implementer"))
                    device.setCpuImplementer(getValue(line).trim());
                if (line.contains("CPU architecture"))
                    device.setCpuArchitecture(getValue(line).trim());
                if (line.contains("CPU variant"))
                    device.setCpuVariant(getValue(line).trim());
                if (line.contains("CPU part"))
                    device.setCpuPart(getValue(line).trim());
                if (line.contains("CPU revision"))
                    device.setCpuRevision(getValue(line).trim());
            }

            device.setProcessorQuantity(String.valueOf(processorsCount));

        } catch (Exception e) {
            Sentry.capture(e);
            e.printStackTrace();
        }
    }

    /**
     * Este metodo agrega al objeto recibido los valores de MemTotal, SwapTotal y KernelStack
     * obtenidos desde el archivo /proc/meminfo mediando comandos de systema
     *
     * @param device Objeto que almacena la informacion obtenida desde el dispositivo
     */
    private void getMemDataCat(Device device) {
        try {
            Process P = Runtime.getRuntime().exec("cat " + MEM_FILE);
            P.waitFor();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(P.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                switch (getKey(line)) {
                    case "":
                        device.setKernelStack(getValue(line).trim());
                        break;
                    case "MemTotal":
                        device.setMemTotal(getValue(line).trim());
                        break;
                    case "SwapTotal":
                        device.setSwapTotal(getValue(line).trim());
                        break;
                }
            }


        } catch (Exception e) {
            Sentry.capture(e);
            e.printStackTrace();
        }
    }

    /**
     * Obtiene la mac-address del dispositvo wlan0 si esta disponible
     *
     * @return mac address del dispositivo, 02:00:00:00:00:00 si no lo encuentra o hay algun error
     */
    private static String getMacAddress() {
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
        } catch (Exception ignore) {
            Sentry.capture(ignore);
        }
        return "02:00:00:00:00:00";
    }

    /**
     * Obtiene la MAC address asociada al Bluetooth del dispositivo si esta disponible
     *
     * @return mac address del Bluetooth del dispositivo 02:00:00:00:00:00 si no lo encuentra o hay algun error
     */
    @SuppressLint({"MissingPermission", "HardwareIds"})
    private String getBluetoothMacAddress() {
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
                } catch (Exception ex) {
                    Sentry.capture(ex);

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

    /**
     * Obtiene el DEVICE ID del dispositivo si esta disponible
     *
     * @return DEVICE ID del dispositivo o string vacio "Not found" en caso de error
     */
    @SuppressLint({"MissingPermission", "HardwareIds"})
    private String getAndroidDeviceID() {
        try {
            String androidId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (androidId != null && !androidId.isEmpty())
                return androidId;
            else return "Not found";
        } catch (Exception e) {
            Sentry.capture(e);
            return "Not found";
        }

    }

    /**
     * Obtiene el Google Services Framework Identifier (GSF) del dispositivo si esta disponible
     *
     * @return GSF del dispositivo o string vacio "Not found" en caso de error
     */
    @SuppressLint({"MissingPermission", "HardwareIds"})
    private String getGoogleServiceFramework() {
        return getGSFID(mContext);
    }

    private static String getGSFID(Context context) {
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
            Sentry.capture(e);
            e.printStackTrace();
            return null;
        } catch (Exception e2) {
            Sentry.capture(e2);
            e2.printStackTrace();
            return null;
        }
    }

    /**
     * Verifica si el dispositivo esta rooteado, aunque como considera distintos parametros
     * existe la posibilidad de obtener falsos positivos. Un ejemplo son los celulares OnePlus,
     * dado que de fabrica quedan rastros de Busybox en la rom preinstalada.
     *
     * @return "Rooted" si se considera rooteado o "No Rooted" en caso de considerarlo asi
     */
    @SuppressLint({"MissingPermission", "HardwareIds"})
    private String getRooted() {
        try {
            RootBeer rootBeer = new RootBeer(mContext);
            if (rootBeer.isRooted()) {
                return "Rooted";
                //we found indication of root
            } else {
                return "No Rooted";
                //we didn't find indication of root
            }
        } catch (Exception e) {
            Sentry.capture(e);
            return "No Rooted";
        }

        //return "";
    }

    /**
     * Obtiene el serial ID de la tarjeta SIM.
     *
     * @return Retorna el serial ID de la tarjeta SIM como String, si no lo encuentra retorna "UNKNOWN".
     */
    @SuppressLint("MissingPermission")
    @RequiresPermission(allOf = {READ_PHONE_STATE})
    public String getSIMSerialID() {
        try {
            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
            if (tm != null) {
                return tm.getSimSerialNumber();
            } else return "UNKNOWN";
        } catch (Exception ex) {
            TrustLogger.d("[getSIMSerialID ] error " + ex.getMessage());
            Sentry.capture(ex);
            return "";
        }

    }

    /**
     * Obtiene el Carrier de la tarjeta SIM.
     *
     * @return Retorna el SIM Carrier como String, si no lo encuentra retorna "UNKNOWN".
     */
    @SuppressLint("MissingPermission")

    @RequiresPermission(allOf = {READ_PHONE_STATE})
    public String getSIMCarrier() {
        try {
            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
            if (tm != null) {
                return tm.getSimOperatorName();
            } else return "UNKNOWN";
        } catch (Exception e) {
            Sentry.capture(e);
            return "UNKNOWN";
        }

    }

    /**
     * Obtiene el estado de la tarjeta SIM.
     *
     * @return Retorna el estado de la SIM como String, posibles valores: "ABSENT", "LOADED" y "UNKNOWN". si no lo encuentra retorna "UNKNOWN".
     */
    @SuppressLint("MissingPermission")

    @RequiresPermission(allOf = {READ_PHONE_STATE})
    public String getSIMState() {
        try {
            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
            if (tm != null) {
                int simState = tm.getSimState();
                switch (simState) {
                    case TelephonyManager.SIM_STATE_ABSENT:
                        return "ABSENT";
                    case TelephonyManager.SIM_STATE_READY:
                        return "LOADED";
                    default:
                        return "UNKNOWN";
                }
            } else return "UNKNOWN";
        } catch (Exception e) {
            Sentry.capture(e);
            return "UNKNOWN";
        }

    }

    private void startWifiService() {
        TrustLogger.d("[TRUST CLIENT] STARTING WIFI SERVICE...");
        Intent intentWifi = new Intent(mContext, WifiService.class);
        intentWifi.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startService(intentWifi);
    }
    //endregion

    //region Envio de minucias

    /**
     * Obtiene las minucias del Dispositivo. SI requestTrustId es True se enviaran al servicio
     * para obtener el Trust ID. Si el listener no es null se notificara el resultado de la
     * request.
     *
     * @param requestTrustId   si se requiere enviar las minucias al servicio
     * @param listener         para comunicar el resultado de la request
     * @param required_permits boolean que indica si se deben solicitar permisos
     * @param forceWifi        boolean que indica si se debe forzar el encendido de Wifi para obtener informacion
     * @param forceBluetooth   boolean que indica si se debe forzar el encendido de bluetooth para obtener informacion
     */

    @SuppressLint("MissingPermission")
    public void getTrifles(final boolean requestTrustId, final boolean required_permits, final boolean forceWifi, final boolean forceBluetooth, @NonNull final TrustListener.OnResult<Audit> listener) {
        saveBluetoothWifiStatus();
        if (Hawk.contains(Constants.TRUST_ID_AUTOMATIC)) {
            Audit data = new Audit();
            data.setTrustid(Hawk.get(Constants.TRUST_ID_AUTOMATIC).toString());
            data.setStatus(true);
            data.setMessage("The device was identified, it already exists in our records.");
            TrustLogger.d("[TRUST CLIENT] ");
            listener.onSuccess(201, data);
        }
        final TrifleBody mBody = new TrifleBody();


        turnOnBluetoothWifi(forceWifi, forceBluetooth);
        final ArrayList<Boolean> permits_found_collection = new ArrayList<>();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> permits = new ArrayList<>();
                boolean permits_found = true;
                if (required_permits) {
                    if (!permissionGranted(READ_PHONE_STATE)) {
                        permits.add(READ_PHONE_STATE);
                        permits_found = false;
                    }
                    if (!permissionGranted(CAMERA)) {
                        permits.add(CAMERA);
                        permits_found = false;
                    }
                    if (!permissionGranted(ACCESS_COARSE_LOCATION)) {
                        permits.add(ACCESS_COARSE_LOCATION);
                        permits_found = false;
                    }
                    permits_found_collection.add(permits_found);
                    if (!required_permits || permits_found) {
                        Device device = new Device();
                        //@RequiresPermission(READ_PHONE_STATE)
                        if (permissionGranted(READ_PHONE_STATE))
                            getDeviceData(device);
                        getBatteryData(device);
                        getSensorsData(device);
                        //@RequiresPermission(CAMERA)
                        if (permissionGranted(CAMERA))
                            getCameraData(device);
                        getNFCData(device);
                        getMemDataCat(device);
                        getCPUDataCat(device);
                        //@RequiresPermission(READ_PHONE_STATE)
                        if (permissionGranted(READ_PHONE_STATE))
                            getImei(device);

                        getWifiState(device);
                        getBluetoothState(device);
                        //@RequiresPermission(READ_PHONE_STATE)
                        if (permissionGranted(READ_PHONE_STATE))
                            getRedGState(device);
                        device.setWlan0Mac(getMacAddress());
                        device.setGoogle_service_framework_gsf(getGoogleServiceFramework());
                        device.setAndroid_device_id(getAndroidDeviceID());
                        device.setRoot(getRooted());
                        String uuid = UUID.randomUUID().toString();
                        device.setUUID(uuid);

                        List<SIM> simList = null;
                        //@RequiresPermission(allOf = {READ_PHONE_STATE, ACCESS_COARSE_LOCATION})
                        if (permissionGranted(READ_PHONE_STATE) && permissionGranted(ACCESS_COARSE_LOCATION))
                            simList = getTelInfo();

                        mBody.setDevice(device);
                        if (simList != null)
                            mBody.setSim(simList);
                        else
                            mBody.setSim(new ArrayList<SIM>());

                        String trustId = mPreferences.getString(TRUST_ID);
                        mBody.setTrustId(trustId);

                        Set<String> stringSet = mPreferences.getStringSet(TRUST_TRIFLES);
                        if (stringSet == null) stringSet = new HashSet<>();
                        stringSet.add(mBody.toJSON());
                        mPreferences.put(TRUST_TRIFLES, stringSet);

                    } else
                        listener.onPermissionRequired(permits);
                }
            }
        }, 5000);
        new Handler().postDelayed(new Runnable() {
            @SuppressLint("MissingPermission")
            @Override
            public void run() {
                if (requestTrustId && permits_found_collection.size() > 0
                        && permits_found_collection.get(0)) {
                    if (Hawk.contains(Constants.DNI_USER)) {
                        Identity identity = new Identity();
                        TrustLogger.d("[TRUST CLIENT] TOKEN IS EXIST : " + Hawk.get("DNI"));
                        identity.setDni(Hawk.get(Constants.DNI_USER).toString());
                        identity.setEmail(Hawk.get(Constants.EMAIL_USER).toString());
                        identity.setLastname(Hawk.get(Constants.LASTNAME_USER).toString());
                        identity.setName(Hawk.get(Constants.NAME_USER).toString());
                        identity.setPhone(Hawk.get(Constants.PHONE_USER).toString());
                        mBody.setIdentity(identity);
                    } else {
                        TrustLogger.d("[TRUST CLIENT] TOKEN NO EXIST ");
                    }

                    startWifiService();
                    sendTrifles(mBody, listener);
                }
            }
        }, 10000);
    }


    /**
     * Obtiene las minucias del Dispositivo. SI requestTrustId es True se enviaran al servicio
     * para obtener el Trust ID. Si el listener no es null se notificara el resultado de la
     * request.
     *
     * @param requestTrustId si se requiere enviar las minucias al servicio
     * @param listener       para comunicar el resultado de la request
     */
    public void getTrifles(final boolean requestTrustId, @NonNull final TrustListener.OnResult<Audit> listener) {
        getTrifles(requestTrustId, true, true, true, listener);
    }


    /**
     * Envia las minucias recogidas al servidos para obtener un identificador
     *
     * @param mBody
     * @param listener envialo si quieres recuperar la respuesta desde tu aplicacion
     */
    private void sendTrifles(@NonNull final TrifleBody mBody, @Nullable final TrustListener.OnResult<Audit> listener) {
        if (Hawk.contains(Constants.TOKEN_SERVICE)) {
            Call<TrifleResponse> createTrifle = RestClient.get().trifle2(mBody, Hawk.get(Constants.TOKEN_SERVICE).toString());
            createTrifle.enqueue(new Callback<TrifleResponse>() {
                @Override
                public void onResponse(@NonNull Call<TrifleResponse> call, @NonNull Response<TrifleResponse> response) {
                    if (response.code() == 401) {
                        refreshSendTrifles(mBody, listener);
                        return;
                    }
                    if (response.isSuccessful()) {
                        TrustLogger.d("[TRUST CLIENT]  VALID TOKEN: " + Hawk.get(Constants.TOKEN_SERVICE));
                        if (listener != null) {
                            if (response.isSuccessful()) {
                                TrifleResponse body = response.body();
                                Audit audit = new Audit();
                                audit.setMessage(body.getMessage());
                                audit.setStatus(body.getStatus());
                                audit.setTrustid(body.getTrustid());
                                body.setAudit(audit);
                                if (body != null) {
                                    if (!Hawk.contains(Constants.TRUST_ID_AUTOMATIC)) {
                                        listener.onSuccess(response.code(), body.getAudit());
                                    }
                                    Hawk.put(Constants.TRUST_ID_AUTOMATIC, body.getTrustid());
                                    mPreferences.put(TRUST_ID, body.getAudit().getTrustid());
                                    TrustLogger.d("[TRUST CLIENT] TRUST ID WAS CREATED: " + body.getTrustid());
                                    restoreWIFIandBluetooth(true, true);
                                    if (Hawk.contains(Constants.DNI_USER)) {
                                        TrustLogger.d("[TRUST CLIENT] Save Device Info Company: first time with DNI");
                                        SaveDeviceInfo.saveDeviceInfo(Hawk.get(Constants.DNI_USER).toString(), mContext.getPackageName(), audit.getTrustid());
                                    } else {
                                        TrustLogger.d("[TRUST CLIENT] Save Device Info Company: first time no DNI");
                                        SaveDeviceInfo.saveDeviceInfo(mContext.getPackageName(), audit.getTrustid());
                                    }

                                } else {
                                    Throwable cause = new Throwable("Body null");
                                    listener.onFailure(new Throwable("Cannot get the response body", cause));
                                }
                            } else {
                                listener.onError(response.code());
                            }
                        }
                    }

                }

                @Override
                public void onFailure(Call<TrifleResponse> call, Throwable t) {
                    if (listener != null)
                        listener.onFailure(t);
                }
            });


        } else {
            AuthToken.getAccessToken(new AuthTokenListener.Auth() {
                @Override
                public void onSuccessAccessToken(String token) {
                    Hawk.put(Constants.TOKEN_SERVICE, "Bearer " + token);
                    sendTrifles(mBody, listener);
                }

                @Override
                public void onErrorAccessToken(String error) {
                    TrustLogger.d("[TRUST CLIENT] ERROR TOKEN GET " + error);

                }
            });
        }

    }


    private void refreshSendTrifles(final TrifleBody mBody, @Nullable final TrustListener.OnResult<Audit> listener) {
        AuthToken.getAccessToken(new AuthTokenListener.Auth() {
            @Override
            public void onSuccessAccessToken(String token) {
                TrustLogger.d("[TRUST CLIENT] SUCCESS REFRESH TOKEN");
                Hawk.put(Constants.TOKEN_SERVICE, "Bearer " + token);
                sendTrifles(mBody, new TrustListener.OnResult<Audit>() {
                    @Override
                    public void onSuccess(int code, Audit data) {
                        if (data != null) {
                            mPreferences.put(TRUST_ID, data.getTrustid());
                            Hawk.put(Constants.TRUST_ID_AUTOMATIC, data.getTrustid());
                            TrustLogger.d("[TRUST CLIENT] TRUST ID WAS CREATED: " + data.getTrustid());
                            restoreWIFIandBluetooth(true, true);
                            listener.onSuccess(200, data);
                            if (Hawk.contains(Constants.DNI_USER)) {
                                TrustLogger.d("[TRUST CLIENT] Save Device Info Company: first time with DNI");
                                SaveDeviceInfo.saveDeviceInfo(Hawk.get(Constants.DNI_USER).toString(), mContext.getPackageName(), data.getTrustid());
                            } else {
                                TrustLogger.d("[TRUST CLIENT] Save Device Info Company: first time no DNI");
                                SaveDeviceInfo.saveDeviceInfo(mContext.getPackageName(), data.getTrustid());
                            }

                        } else {
                            Throwable cause = new Throwable("Body null");
                            listener.onFailure(new Throwable("Cannot get the response body", cause));
                        }
                    }

                    @Override
                    public void onError(int code) {
                        TrustLogger.d("[TRUST CLIENT] ERROR SEND TRIFLES: " + code);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        TrustLogger.d("[TRUST CLIENT] ERROR SEND TRIFLES: " + t.getMessage());
                    }

                    @Override
                    public void onPermissionRequired(ArrayList<String> permisos) {
                        TrustLogger.d("[TRUST CLIENT] ERROR PERMISSION SEND TRIFLES: ");
                    }
                });
            }

            @Override
            public void onErrorAccessToken(String error) {
                TrustLogger.d("[TRUST CLIENT] ERROR SEND TRIFLES TOKEN:  " + error);
            }
        });

    }

    public void createAudit(String trustid, AuditTransaction auditTransaction, String lat, String lng, AuditExtraData auditExtraData, final TrustListener.OnResultAudit onResultAudit) {
        AuditSource source = getAuditSource(trustid, lat, lng);
        final AuditTest auditTest = new AuditTest();
        auditTest.setType_audit("trust identify");
        auditTest.setApplication(getAppName());
        auditTest.setSource(source);
        auditTest.setTransaction(auditTransaction);
        auditTest.setPlatform("Android");
        auditTest.setExtra_data(auditExtraData);

        if (Hawk.contains(Constants.TOKEN_SERVICE)) {
            sendAudit(auditTest, onResultAudit);
        } else {
            AuthToken.getAccessToken(new AuthTokenListener.Auth() {
                @Override
                public void onSuccessAccessToken(String token) {
                    Hawk.put(Constants.TOKEN_SERVICE, "Bearer " + token);
                    sendAudit(auditTest, onResultAudit);
                }

                @Override
                public void onErrorAccessToken(String error) {
                    TrustLogger.d("[TRUST CLIENT] ERROR TOKEN " + error);
                }
            });
        }

    }

    public static String getAppName() {
        return mContext.getString(R.string.app_name);
    }

    public static AuditSource getAuditSourceOffline(String trustid, String lat, String lng) {
        AuditSource auditSource = getAuditSource(trustid, lat, lng);
        //auditSource.setAudit_id(AutomaticAudit.getUUIDAuditOffline());
        return auditSource;
    }

    public static AuditSource getAuditSource(String trustid, String lat, String lng) {
        String wifiName = "no wifi avaliable";
        final TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            wifiName = "no wifi avaliable for permission";
        }
        WifiManager wifiMgr = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        wifiName = wifiInfo.getSSID().replaceAll("\"", "");
        String imsi = telephonyManager.getSubscriberId() == null ? "sim extraida" : telephonyManager.getSubscriberId();
        String appName = getAppName();
        String packageName = mContext.getApplicationContext().getPackageName();
        Hawk.put(Constants.BUNDLE_ID, packageName);
        ConnectivityManager connManager = (ConnectivityManager) mContext.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        String connection;
        if (mWifi.isAvailable() == true) {
            connection = Constants.WIFI_CONNECTION;
        } else if (mMobile.isAvailable() == true) {
            connection = Constants.MOBILE_CONNECTION;
        } else connection = Constants.DISCONNECT;

        PackageInfo pInfo = null;
        try {
            pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo == null ? "0.0" : pInfo.versionName;
        AuditSource source = new AuditSource(
                trustid,
                appName,
                packageName,
                "Android"
                , String.valueOf(Build.VERSION.SDK_INT),
                Build.MODEL,
                imsi,
                lat,
                lng,
                connection,
                wifiName,
                version
        );

        return source;
    }

    public void createAudit(String trustid, AuditTransaction auditTransaction, String lat, String lng, AuditExtraData auditExtraData) {
        final AuditTest auditTest = new AuditTest();
        auditTest.setType_audit("trust identify");
        auditTest.setApplication(getAppName());
        auditTest.setSource(getAuditSource(trustid, lat, lng));
        auditTest.setTransaction(auditTransaction);
        auditTest.setPlatform("Android");
        auditTest.setExtra_data(auditExtraData);

        if (Hawk.contains(Constants.TOKEN_SERVICE)) {
            sendAudit(auditTest);
        } else {
            AuthToken.getAccessToken(new AuthTokenListener.Auth() {
                @Override
                public void onSuccessAccessToken(String token) {
                    Hawk.put(Constants.TOKEN_SERVICE, "Bearer " + token);
                    sendAudit(auditTest);
                }

                @Override
                public void onErrorAccessToken(String error) {
                    TrustLogger.d("[TRUST CLIENT] ERROR TOKEN " + error);
                }
            });
        }

    }

    public void sendAudit(final AuditTest auditTest) {
        RestClientAudit.get().createAuditTest(auditTest, Hawk.get(Constants.TOKEN_SERVICE).toString()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                TrustLogger.d("audit test code: " + String.valueOf(response.code()));
                TrustLogger.d("audit test body: " + String.valueOf(response.body()));
                if (response.code() == 401) {
                    TrustLogger.d("[TRUST CLIENT] ACCESS TOKEN AUDIT EXPIRED: " + response.code() + " MESSAGE: " + response.message());
                    refreshTokenAudit(auditTest);
                }
                if (response.isSuccessful()) {
                    TrustLogger.d("[TRUST CLIENT]  VALID TOKEN: " + Hawk.get(Constants.TOKEN_SERVICE));
                    TrustLogger.d("[TRUST CLIENT] Audit code: " + response.code() + " Audit message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                TrustLogger.d("[TRUST CLIENT] Audit code: -1 Audit message: " + t.getMessage());
            }
        });
    }

    private void sendAudit(final AuditTest auditTest, final TrustListener.OnResultAudit onResultAudit) {
        RestClientAudit.get().createAuditTest(auditTest, Hawk.get(Constants.TOKEN_SERVICE).toString()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                TrustLogger.d("audit test code: " + String.valueOf(response.code()));
                TrustLogger.d("audit test body: " + String.valueOf(response.body()));
                if (response.code() == 401) {
                    TrustLogger.d("[TRUST CLIENT] ACCESS TOKEN AUDIT EXPIRED: " + response.code() + " MESSAGE: " + response.message());
                    refreshTokenAudit(auditTest, onResultAudit);
                    return;
                }
                if (response.isSuccessful()) {

                    TrustLogger.d("[TRUST CLIENT]  VALID TOKEN: " + Hawk.get(Constants.TOKEN_SERVICE));
                    TrustLogger.d("[TRUST CLIENT]  Audit code: " + response.code() + " Audit message: " + response.message());
                    JsonObject object = response.body().get("audit").getAsJsonObject();
                    String id = object.get("auditid").getAsString();
                    onResultAudit.onSuccess(id);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                TrustLogger.d("[TRUST CLIENT] Audit code: -1 Audit message: " + t.getMessage());
                onResultAudit.onError(t.getMessage());
            }
        });
    }

    private void refreshTokenAudit(final AuditTest auditTest) {
        AuthToken.getAccessToken(new AuthTokenListener.Auth() {
            @Override
            public void onSuccessAccessToken(String token) {
                TrustLogger.d("[TRUST CLIENT] SUCCESS REFRESH TOKEN");
                Hawk.put(Constants.TOKEN_SERVICE, "Bearer " + token);
                RestClientAudit.get().createAuditTest(auditTest, "Bearer " + token).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.code() == 401) {
                            TrustLogger.d("[TRSUT CLIENT] ERROR AUDIT REFRESH TOKEN" + response.code() + " MESSAGE: " + response.message());
                            return;
                        }
                        if (response.isSuccessful()) {
                            TrustLogger.d("[TRUST CLIENT]  VALID TOKEN: " + Hawk.get(Constants.TOKEN_SERVICE));
                            TrustLogger.d("[TRUST CLIENT] Audit code: " + response.code() + " Audit message: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        TrustLogger.d("[TRSUT CLIENT] ERROR AUDIT REFRESH TOKEN: " + t.getMessage());

                    }
                });

            }

            @Override
            public void onErrorAccessToken(String error) {
                TrustLogger.d("[TRUST CLIENT] ERROR ACCESS TOKEN: " + error);
            }
        });
    }

    private void refreshTokenAudit(final AuditTest auditTest, final TrustListener.OnResultAudit onResultAudit) {
        AuthToken.getAccessToken(new AuthTokenListener.Auth() {
            @Override
            public void onSuccessAccessToken(String token) {
                TrustLogger.d("[TRUST CLIENT] SUCCESS REFRESH TOKEN");
                Hawk.put(Constants.TOKEN_SERVICE, "Bearer " + token);
                RestClientAudit.get().createAuditTest(auditTest, "Bearer " + token).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.code() == 401) {
                            TrustLogger.d("[TRSUT CLIENT] ERROR AUDIT REFRESH TOKEN" + response.code() + " MESSAGE: " + response.message());
                            onResultAudit.onError(String.valueOf("Error token auth: " + response.code()));
                            return;
                        }
                        if (response.isSuccessful()) {
                            TrustLogger.d("[TRUST CLIENT]  VALID TOKEN: " + Hawk.get(Constants.TOKEN_SERVICE));
                            TrustLogger.d("[TRUST CLIENT] Audit code: " + response.code() + " Audit message: " + response.message());
                            JsonObject object = response.body().get("audit").getAsJsonObject();
                            String id = object.get("auditid").getAsString();
                            onResultAudit.onSuccess(id);
                        }

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        TrustLogger.d("[TRSUT CLIENT] ERROR AUDIT REFRESH TOKEN: " + t.getMessage());
                        onResultAudit.onError(t.getMessage());
                    }
                });

            }

            @Override
            public void onErrorAccessToken(String error) {
                TrustLogger.d("[TRUST CLIENT] ERROR ACCESS TOKEN: " + error);
                onResultAudit.onError(error);
            }
        });
    }

    //endregion

    //region Estados de Wifi y Bluetooth

    /**
     * This method get state of Wifi (true, false)
     *
     * @param device Objeto que almacena la informacion obtenida desde el dispositivo
     */
    private void getWifiState(Device device) {
        try {
            final WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            boolean state = wifiManager == null || wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED;
            TrustLogger.d("[TRUST CLIENT] : WI-FI STATE: " + String.valueOf(state));
            device.setWifi_state(state);
        } catch (Exception e) {
            Sentry.capture(e);
        }

    }

    /**
     * This method get state of Bluetooth (true, false)
     *
     * @param device Objeto que almacena la informacion obtenida desde el dispositivo
     */
    private void getBluetoothState(Device device) {
        try {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            boolean state = mBluetoothAdapter == null || mBluetoothAdapter.isEnabled();
            TrustLogger.d("[TRUST CLIENT] : BLUETOOTH STATE: " + String.valueOf(state));
            device.setBluetooth_state(state);
        } catch (Exception e) {
            Sentry.capture(e);
        }

    }

    /**
     * This method get state of Mobile Network (true, false)
     *
     * @param device Objeto que almacena la informacion obtenida desde el dispositivo
     */
    private void getRedGState(Device device) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            boolean state = (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE);
            TrustLogger.d("[TRUST CLIENT] : CURRENT STATE OF RED (3G,4G): " + String.valueOf(state));
            device.setRed_g_state(state);
        } catch (Exception e) {
            Sentry.capture(e);
        }

    }

    /**
     * Get the current status of bluetooth
     *
     * @return returns the current bluetooth status true ON , false OFF
     */
    @SuppressLint("MissingPermission")
    private boolean getCurrentBluetoothStatus() {
        try {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled();
        } catch (Exception e) {
            Sentry.capture(e);
            return false;
        }

    }

    /**
     * Get the current status of Wifi
     *
     * @return returns the current Wifi status true ON , false OFF
     */
    private boolean getCurrentWifiStatus() {
        try {
            final WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            @SuppressLint("MissingPermission") boolean state = wifiManager == null || wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED;
            return state;
        } catch (Exception e) {
            Sentry.capture(e);
            return false;
        }

    }

    /**
     * Saves the current status of bluetooth and wifi
     */
    private void saveBluetoothWifiStatus() {
        try {
            Hawk.put(Constants.WIFI_STATUS, getCurrentWifiStatus());
            Hawk.put(Constants.BLUETOOTH_STATUS, getCurrentBluetoothStatus());
            TrustLogger.d("[TRUST CLIENT] : SAVING CURRENT STARE OF WIFI AND BLUETOOTH...");
            TrustLogger.d("[TRUST CLIENT] : CURRENT STATE OF WI-FI: " + Hawk.get(Constants.WIFI_STATUS));
            TrustLogger.d("[TRUST CLIENT] : CURRENT STATE OF BLUETOOTH: " + Hawk.get(Constants.BLUETOOTH_STATUS));
        } catch (Exception e) {
            Sentry.capture(e);
        }
    }

    /**
     * Returns the status of the wifi and bluetooth to the previous state
     *
     * @param forceWifi      informs if you should restore the previous wifi status
     * @param forceBluetooth informs if you should restore the previous bluetooth status
     */
    @SuppressLint("MissingPermission")
    private void restoreWIFIandBluetooth(boolean forceWifi, boolean forceBluetooth) {
        try {
            TrustLogger.d("[TRUST CLIENT] : RESTORING STATE OF BLUETOOTH AND WI-FI:");
            TrustLogger.d("[TRUST CLIENT] : BEFORE STATE BLUETOOTH: " + Hawk.get(Constants.BLUETOOTH_STATUS));
            TrustLogger.d("[TRUST CLIENT] : BEFORE STATE WI-FI : " + Hawk.get(Constants.WIFI_STATUS));
            boolean oldWifiStatus = Hawk.get(Constants.WIFI_STATUS);
            boolean oldBluetoothStatus = Hawk.get(Constants.BLUETOOTH_STATUS);
            final WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
           /* if (forceWifi) {
                if (wifiManager != null) {
                    if (currentWifiStatus) {
                        wifiManager.setWifiEnabled(true);
                        TrustLogger.d("[TRUST CLIENT] : WIFI TURN ON");
                    } else {
                        wifiManager.setWifiEnabled(false);
                        TrustLogger.d("[TRUST CLIENT] : WIFI TURN OFF");
                    }
                }
            }
            if (forceBluetooth) {
                if (mBluetoothAdapter != null) {
                    if (currentBluetoothStatus) {
                        mBluetoothAdapter.enable();
                        TrustLogger.d("[TRUST CLIENT] : BLUETOOTH TURN ON");
                    } else {
                        mBluetoothAdapter.disable();
                        TrustLogger.d("[TRUST CLIENT] : BLUETOOTH TURN ON");
                    }
                }
            }*/
            if (forceWifi) {
                if (wifiManager != null) {
                    if (oldWifiStatus) {
                        wifiManager.setWifiEnabled(true);
                    } else {
                        wifiManager.setWifiEnabled(false);
                    }
                }
            }
            if (forceBluetooth) {
                if (mBluetoothAdapter != null) {
                    if (oldBluetoothStatus) {
                        mBluetoothAdapter.enable();
                    } else {
                        mBluetoothAdapter.disable();
                    }
                }
            }

        } catch (Exception e) {
            Sentry.capture(e);
        }

    }

    /**
     * Turn on wifi and bluetooth
     *
     * @param forceWifi      informs if wifi should be turned on
     * @param forceBluetooth informs if bluetooth should be turned on
     */
    @SuppressLint("MissingPermission")
    private void turnOnBluetoothWifi(boolean forceWifi, boolean forceBluetooth) {
        try {
            final WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (wifiManager != null && forceWifi) {
                TrustLogger.d("[TRUST CLIENT] TURNING ON WIFI...");
                wifiManager.setWifiEnabled(true);
            }
            if (mBluetoothAdapter != null && forceBluetooth) {
                TrustLogger.d("[TRUST CLIENT] TURNING ON BLUETOOTH...");
                mBluetoothAdapter.enable();
            }
        } catch (Exception e) {
            Sentry.capture(e);
        }

    }

    /**
     * This method verify if in merged Manifest exists a specific permission
     *
     * @param permission String of permission to verify i.e. Manifest.permission.ACCESS_FINE_LOCATION
     * @return boolean true if permission is at merged Manifest
     */
    private boolean hasPermission(String permission) {
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), PackageManager.GET_PERMISSIONS);
            if (info.requestedPermissions != null) {
                for (String p : info.requestedPermissions) {
                    if (p.equals(permission)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            Sentry.capture(e);
            e.printStackTrace();
        }
        return false;
    }

    //endregion

    //region Permiso

    /**
     * This method check if a permission is granted.
     * Used to check dangerous permission i.e. permission that user grant
     *
     * @param permission String of permission to verify i.e. Manifest.permission.ACCESS_FINE_LOCATION
     * @return boolean true if has a granted permission
     */
    private boolean permissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_GRANTED;
    }
    //endregion


}

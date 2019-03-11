package lat.trust.trusttrifles;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
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
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.Log;

import com.orhanobut.hawk.Hawk;
import com.scottyab.rootbeer.RootBeer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import lat.trust.trusttrifles.broadcasts.AlarmReceiver;
import lat.trust.trusttrifles.model.Audit;
import lat.trust.trusttrifles.model.AuditSource;
import lat.trust.trusttrifles.model.AuditTransaction;
import lat.trust.trusttrifles.model.Device;
import lat.trust.trusttrifles.model.Geo;
import lat.trust.trusttrifles.model.SIM;
import lat.trust.trusttrifles.model.SensorData;
import lat.trust.trusttrifles.network.RestClient;
import lat.trust.trusttrifles.network.TrifleResponse;
import lat.trust.trusttrifles.network.req.AuditBody;
import lat.trust.trusttrifles.network.req.EventBody;
import lat.trust.trusttrifles.network.req.RemoteEventBody;
import lat.trust.trusttrifles.network.req.RemoteEventBody2;
import lat.trust.trusttrifles.network.req.TrifleBody;
import lat.trust.trusttrifles.services.WifiStateService;
import lat.trust.trusttrifles.utilities.Constants;
import lat.trust.trusttrifles.utilities.TrustLogger;
import lat.trust.trusttrifles.utilities.TrustPreferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_PHONE_STATE;
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
    private static volatile TrustClient trustInstance;
    private static Context mContext;
    private TrustPreferences mPreferences;
    private boolean currentWifiStatus;
    private boolean currentBluetoothStatus;


    private TrustClient() {
        TrustPreferences.init(mContext);
        startAutomaticAudit();
        mPreferences = TrustPreferences.getInstance();

    }

    /**
     * Init.
     *
     * @param context the context
     */
    public static void init(Context context) {

        TrustLogger.d("[TRUST ID] init: ");
        mContext = context;
        trustInstance = new TrustClient();
        //mContext.startService(new Intent(mContext, WifiStateService.class));


    }

    /**
     * start a alarm or
     */
    public static void startAutomaticAudit() {
        TrustLogger.d("[AUTOMATIC AUDIT] startAutomaticAudit: ");
        AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Date dat = new Date();
        Calendar cal_alarm = Calendar.getInstance();
        Calendar cal_now = Calendar.getInstance();
        cal_now.setTime(dat);
        cal_alarm.setTime(dat);
        cal_alarm.set(Calendar.HOUR_OF_DAY, 14);
        cal_alarm.set(Calendar.MINUTE, 35);
        cal_alarm.set(Calendar.SECOND, 0);
        if (cal_alarm.before(cal_now)) {
            cal_alarm.add(Calendar.DATE, 1);
        }
        Intent myIntent = new Intent(mContext, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, myIntent, 0);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static TrustClient getInstance() {
        return trustInstance;
    }

    /**
     * Obtiene el serial ID de la tarjeta SIM.
     *
     * @return Retorna el serial ID de la tarjeta SIM como String, si no lo encuentra retorna "UNKNOWN".
     */
    @RequiresPermission(allOf = {READ_PHONE_STATE})
    public String getSIMSerialID() {
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
        if (tm != null) {
            return tm.getSimSerialNumber();
        } else return "UNKNOWN";
    }

    /**
     * Obtiene el Carrier de la tarjeta SIM.
     *
     * @return Retorna el SIM Carrier como String, si no lo encuentra retorna "UNKNOWN".
     */
    @RequiresPermission(allOf = {READ_PHONE_STATE})
    public String getSIMCarrier() {
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
        if (tm != null) {
            return tm.getSimOperatorName();
        } else return "UNKNOWN";
    }

    /**
     * Obtiene el estado de la tarjeta SIM.
     *
     * @return Retorna el estado de la SIM como String, posibles valores: "ABSENT", "LOADED" y "UNKNOWN". si no lo encuentra retorna "UNKNOWN".
     */
    @RequiresPermission(allOf = {READ_PHONE_STATE})
    public String getSIMState() {
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
    }

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
        saveBluetoothWifiStatus(forceWifi, forceBluetooth);

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
                restoreWIFIandBluetooth(forceWifi, forceBluetooth);
            }
        }, 4000);

        new Handler().postDelayed(new Runnable() {
            @SuppressLint("MissingPermission")
            @Override
            public void run() {
                if (requestTrustId && permits_found_collection.size() > 0
                        && permits_found_collection.get(0)) {
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
     * Agrega cierta informacion de las camaras del dispositivo
     *
     * @param device Objeto que almacena la informacion obtenida desde el dispositivo
     */
    @RequiresPermission(CAMERA)
    private void getCameraData(Device device) {
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
    }

    /**
     * Agrega informacion respecto a la presencia de NFC en el dispositivo
     *
     * @param device Objeto que almacena la informacion obtenida desde el dispositivo
     */
    private void getNFCData(Device device) {
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
    }

    /**
     * Agrega informacion relacionada a la bateria del dispositivo
     *
     * @param device Objeto que almacena la informacion obtenida desde el dispositivo
     */
    private void getBatteryData(Device device) {
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
    }

    /**
     * Agrega toda la informacion relevante obtenida desde Build
     *
     * @param device Objeto que almacena la informacion obtenida desde el dispositivo
     */
    @RequiresPermission(READ_PHONE_STATE)
    private void getDeviceData(Device device) {
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
    }

    /**
     * Este metodo agrega el imei del dispositivo y la version del software
     *
     * @param device Objeto que almacena la informacion obtenida desde el dispositivo
     */
    @RequiresPermission(READ_PHONE_STATE)
    private void getImei(Device device) {
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);

        if (tm != null) {
            device.setImei(tm.getDeviceId());
            device.setSoftwareVersion(tm.getDeviceSoftwareVersion());
        }
    }

    /**
     * Gets imei.
     *
     * @return the imei as a String
     */
    @RequiresPermission(READ_PHONE_STATE)
    public String getImei() {
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);

        if (tm != null) {
            return tm.getDeviceId();
        } else {
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
    @RequiresPermission(allOf = {READ_PHONE_STATE, ACCESS_COARSE_LOCATION})
    private List<SIM> getTelInfo() {
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
    }

    /**
     * Gets sim data at slot.
     *
     * @param slot the slot
     * @return the sim data at slot
     */
    @RequiresPermission(allOf = {READ_PHONE_STATE, ACCESS_COARSE_LOCATION})
    private SIM getSimDataAtSlot(int slot) {
        SIM sim = null;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);

            if (telephonyManager != null) {
                if (getSIMStateBySlot(slot)) {
                    sim = new SIM();
                    sim.setIccid(getDeviceIdBySlot("getSimSerialNumber", slot));
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
                } catch (NoSuchFieldException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {

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
        String androidId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (androidId != null && !androidId.isEmpty())
            return androidId;
        else return "Not found";
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
            e.printStackTrace();
            return null;
        } catch (Exception e2) {
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

        RootBeer rootBeer = new RootBeer(mContext);
        if (rootBeer.isRooted()) {
            return "Rooted";
            //we found indication of root
        } else {
            return "No Rooted";
            //we didn't find indication of root
        }
        //return "";
    }

    /**
     * Envia las minucias recogidas al servidos para obtener un identificador
     *
     * @param mBody
     * @param listener envialo si quieres recuperar la respuesta desde tu aplicacion
     */
    private void sendTrifles(@NonNull TrifleBody mBody, @Nullable final TrustListener.OnResult<Audit> listener) {
        Call<TrifleResponse> createTrifle = RestClient.get().trifle(mBody);
        createTrifle.enqueue(new Callback<TrifleResponse>() {
            @Override
            public void onResponse(@NonNull Call<TrifleResponse> call, @NonNull Response<TrifleResponse> response) {
                if (listener != null) {
                    if (response.isSuccessful()) {
                        TrifleResponse body = response.body();
                        if (body != null) {
                            listener.onSuccess(response.code(), body.getAudit());
                            mPreferences.put(TRUST_ID, body.getAudit().getTrustId());
                            Hawk.put(Constants.TRUST_ID_AUTOMATIC, body.getAudit().getTrustId());
                        } else {
                            Throwable cause = new Throwable("Body null");
                            listener.onFailure(new Throwable("Cannot get the response body", cause));
                        }
                    } else {
                        listener.onError(response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call<TrifleResponse> call, Throwable t) {
                if (listener != null)
                    listener.onFailure(t);
            }
        });
    }

    /**
     * Este metodo se utiliza para reportar un cambio en el dispositivo al servidor
     * Utiliza el trust id que obtuvo el usuario
     * No espera auditoria en la respuesta
     * 14/11/2018 onError - Code: 404
     *
     * @param trustId     the trust id
     * @param packageName the package name
     * @param eventType   the event type
     * @param eventValue  the event value
     * @param lat         the lat
     * @param lng         the lng
     * @param listener    the listener
     */
    private void notifyEvent(String trustId, String packageName, String eventType, String eventValue, double lat, double lng, @Nullable final TrustListener.OnResult<Object> listener) {
        Call<Void> notify = RestClient.get().sendEvent(
                new EventBody(trustId, packageName, String.valueOf(Build.VERSION.SDK_INT), eventType, eventValue, lat, lng));

        notify.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (listener == null) return;
                if (response.isSuccessful()) {
                    listener.onSuccess(response.code(), null);
                } else {
                    listener.onError(response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
                if (listener == null) return;
                listener.onFailure(t);
            }
        });
    }

    /**
     * Este metodo se utiliza para reportar un cambio en el dispositivo hacia el servidor
     * Utiliza el trust id almacenado por la libreria
     * No espera auditoria en la respuesta
     * 14/11/2018 onError - Code: 404
     *
     * @param packageName the package name
     * @param eventType   the type of event e.g : Bluetooth
     * @param eventValue  the value of the type event, e.g: Status:true
     * @param lat         latitude in which the change occurred
     * @param lng         length at which the change occurred
     * @param listener    the listener
     */
    public void notifyEvent(String packageName, String eventType, String eventValue, double lat, double lng, @Nullable final TrustListener.OnResult<Object> listener) {
        //TODO: obtener trust
        String trustId = mPreferences.getString(TRUST_ID);
        if (trustId != null) {
            notifyEvent(trustId, packageName, eventType, eventValue, lat, lng, listener);
        } else {
            if (listener != null)
                listener.onFailure(new Throwable("Trust id doesn't found"));
        }
    }

    /**
     * Metodo utilizado para reportar un cambio en el dispositivo al servidor
     * Espera una auditoria en la respuesta.
     * 14/11/2018 onError - Code: 500
     *
     * @param tid      the unique trust id
     * @param object   the name of the object to report
     * @param key      the name of the change e.g: Wifi
     * @param value    the description or value of the change e.g: status :false
     * @param lat      latitude in which the change occurred
     * @param lng      length at which the change occurred
     * @param listener the result
     */
    public void remoteEvent(String tid, String object, String key, String value, double lat, double lng, final TrustListener.OnResult<Object> listener) {
        @SuppressLint("MissingPermission") final Call<TrifleResponse> remote = RestClient.get().remote(new RemoteEventBody2(tid, getSimDataAtSlot(0), key, value, new Geo(String.valueOf(lat), String.valueOf(lng))));
        remote.enqueue(new Callback<TrifleResponse>() {


            @Override
            public void onResponse(Call<TrifleResponse> call, Response<TrifleResponse> response) {
                if (listener == null) return;
                if (response.isSuccessful()) {
                    listener.onSuccess(response.code(), null);
                } else {
                    listener.onError(response.code());
                }
            }

            @Override
            public void onFailure(Call<TrifleResponse> call, Throwable t) {
                t.printStackTrace();
                if (listener == null) return;
                listener.onFailure(t);
            }
        });
    }

    /**
     * Metodo clon de remoteEvent, busca probar otro Body para el envio de informacion.
     * Utiliza envio por String para info de SIM (uso previo a la creacion de SIM Class).
     * Espera una auditoria en la respuesta.
     * 14/11/2018 onError - Code: 500
     *
     * @param tid      the tid
     * @param object   the name of the object to report
     * @param key      the key
     * @param value    the value
     * @param lat      the latitude in which the change occurred
     * @param lng      the length at which the change occurred
     * @param onResult the on result
     */
    @SuppressLint("MissingPermission")
    public void remoteEvent2(String tid, String object, String key, String value, double lat, double lng, @Nullable TrustListener.OnResult<Object> onResult) {
        Call<TrifleResponse> remote = RestClient.get().remote2(new RemoteEventBody(tid, object, key, value, new Geo(String.valueOf(lat), String.valueOf(lng))));
        remote.enqueue(new Callback<TrifleResponse>() {
            @Override
            public void onResponse(Call<TrifleResponse> call, Response<TrifleResponse> response) {
                TrustLogger.d(String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<TrifleResponse> call, Throwable t) {
                TrustLogger.d(t.getMessage());
            }
        });
    }

    /**
     * This method notify any action realized (transaction), i.e. document sign
     *
     * @param trustid   the trustid
     * @param operation the operation
     * @param method    the method
     * @param result    the result
     * @param timestamp the timestamp
     * @param lat       the lat
     * @param lng       the lng
     * @param listener  the listener
     */
    public void createAudit(String trustid, String operation, String method, String result, long timestamp, String lat, String lng, @NonNull final TrustListener.OnResultSimple listener) {
        String appName = mContext.getString(R.string.app_name);
        String packageName = mContext.getApplicationContext().getPackageName();
        AuditSource source = new AuditSource(trustid, appName, packageName, "Android", String.valueOf(Build.VERSION.SDK_INT));
        AuditTransaction transaction = new AuditTransaction(operation, method, result, timestamp);
        Geo geo = new Geo(lat, lng);
        AuditBody body = new AuditBody(source, transaction, geo);
        Call<Void> createAudit = RestClient.get().createAudit(body);
        createAudit.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                TrustLogger.d("[CREATE AUDIT] CODE: " + response.code());
                listener.onResult(response.code(), response.message());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
                listener.onResult(-1, t.getMessage());
            }
        });
    }

    /**
     * This method get state of Wifi (true, false)
     *
     * @param device Objeto que almacena la informacion obtenida desde el dispositivo
     */
    private void getWifiState(Device device) {
        final WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        boolean state = wifiManager == null || wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED;
        TrustLogger.d("Wifi state: " + String.valueOf(state));
        device.setWifi_state(state);
    }

    /**
     * This method get state of Bluetooth (true, false)
     *
     * @param device Objeto que almacena la informacion obtenida desde el dispositivo
     */
    private void getBluetoothState(Device device) {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean state = mBluetoothAdapter == null || mBluetoothAdapter.isEnabled();
        TrustLogger.d("BluetoothState: " + String.valueOf(state));
        device.setBluetooth_state(state);
    }

    /**
     * This method get state of Mobile Network (true, false)
     *
     * @param device Objeto que almacena la informacion obtenida desde el dispositivo
     */
    private void getRedGState(Device device) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean state = (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE);
        TrustLogger.d("RedGState" + String.valueOf(state));
        device.setRed_g_state(state);
    }

    /**
     * Get the current status of bluetooth
     *
     * @return returns the current bluetooth status true ON , false OFF
     */
    @SuppressLint("MissingPermission")
    private boolean getCurrentBluetoothStatus() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled();
    }

    /**
     * Get the current status of Wifi
     *
     * @return returns the current Wifi status true ON , false OFF
     */
    private boolean getCurrentWifiStatus() {
        final WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        @SuppressLint("MissingPermission") boolean state = wifiManager == null || wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED;
        return state;
    }

    /**
     * Saves the current status of bluetooth and wifi
     *
     * @param forceWifi      inform if you should save the current status of wifi
     * @param forceBluetooth inform if you should save the current status of Bluetooth
     */
    private void saveBluetoothWifiStatus(boolean forceWifi, boolean forceBluetooth) {
        if (forceWifi) currentWifiStatus = getCurrentWifiStatus();
        if (forceBluetooth) currentBluetoothStatus = getCurrentBluetoothStatus();
        TrustLogger.d("wifi current state: " + String.valueOf(currentWifiStatus));
        TrustLogger.d("bluetooth current state: " + String.valueOf(currentBluetoothStatus));
    }

    /**
     * Returns the status of the wifi and bluetooth to the previous state
     *
     * @param forceWifi      informs if you should restore the previous wifi status
     * @param forceBluetooth informs if you should restore the previous bluetooth status
     */
    @SuppressLint("MissingPermission")
    private void restoreWIFIandBluetooth(boolean forceWifi, boolean forceBluetooth) {

        TrustLogger.d("restaurando estados WiFi, Bluetooth:");
        TrustLogger.d("WifI estado anterior: " + String.valueOf(currentBluetoothStatus));
        TrustLogger.d("Bkuetooth estado anterior: : " + String.valueOf(currentWifiStatus));
        final WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (forceWifi) {
            if (wifiManager != null) {
                if (currentWifiStatus) {
                    wifiManager.setWifiEnabled(true);
                    TrustLogger.d("wifi encendido");
                } else {
                    wifiManager.setWifiEnabled(false);
                    TrustLogger.d("wifi apagado");
                }
            }
        }
        if (forceBluetooth) {
            if (mBluetoothAdapter != null) {
                if (currentBluetoothStatus) {
                    mBluetoothAdapter.enable();
                    TrustLogger.d("bt encendido");
                } else {
                    mBluetoothAdapter.disable();
                    TrustLogger.d("bt apagado");
                }
            }
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
        final WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (wifiManager != null && forceWifi) {
            wifiManager.setWifiEnabled(true);
        }
        if (mBluetoothAdapter != null && forceBluetooth) {
            mBluetoothAdapter.enable();
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
            e.printStackTrace();
        }
        return false;
    }

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

}

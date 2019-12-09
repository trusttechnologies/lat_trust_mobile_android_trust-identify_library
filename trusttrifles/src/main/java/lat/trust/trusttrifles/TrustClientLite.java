package lat.trust.trusttrifles;

import android.content.Context;

import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

import io.sentry.Sentry;
import lat.trust.trusttrifles.model.Audit;
import lat.trust.trusttrifles.model.Camera;
import lat.trust.trusttrifles.model.Device;
import lat.trust.trusttrifles.model.Identity;
import lat.trust.trusttrifles.model.TrustAuth;
import lat.trust.trusttrifles.network.req.TrifleBody;
import lat.trust.trusttrifles.utilities.Constants;
import lat.trust.trusttrifles.utilities.TrustLogger;


public class TrustClientLite {

    private static final String WIFI_STATUS = "com.trust.wifi_status";

    public static void init(Context context) {
        hawkInit(context);
        TrustAuth.setSecretAndId(context);
        setEnvironment(context);
    }

    private static void setEnvironment(Context context) {
        TrustIdentifyConfigurationService.setEnvironment(TrustIdentifyConfigurationService.ENVIRONMENT_PRODUCTION, context);
    }

    private static void hawkInit(Context context) {
        if (!Hawk.isBuilt()) {
            Hawk.init(context).build();
        }
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
        if (Hawk.contains(Constants.IDENTITY)) {
            trifleBody.setIdentity(DataUtil.getIdentity());
        }
        TrustLogger.d("* * * * *" + new Gson().toJson(trifleBody));

        SendTrifles.sendTriflesToken(trifleBody, context, listener);
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
            trifleBody.setTrustId(Hawk.contains(Constants.TRUST_ID_AUTOMATIC) ? Hawk.get(Constants.TRUST_ID_AUTOMATIC) : null);
            trifleBody.setIdentity(identity);
            SendTrifles.sendTriflesToken(trifleBody, context, listener);
        } catch (Exception ex) {
            TrustLogger.d("Error sendIdentify: " + ex.getMessage());
            Sentry.capture(ex);
        }

    }
}

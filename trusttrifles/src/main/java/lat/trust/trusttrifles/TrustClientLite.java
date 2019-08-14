package lat.trust.trusttrifles;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

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

    }

    private static void hawkInit(Context context) {
        if (!Hawk.isBuilt()) {
            Hawk.init(context).build();
        }
    }

    private static Device getDeviceData(Context context) {
        Device device = new Device();
        device.setAndroid_device_id(A.getAndroidDeviceID(context));
        device.setBattery(A.getBatteryData(context));
        device.setBattery_capacity(A.getBatteryCapacity(context) == 0 ? "Not Found" : String.valueOf(A.getBatteryCapacity(context)).concat(" mAh"));
        device.setBattery_technology(A.getBatteryTechnology(context));
        device.setBluetooth_state(A.getBluetoothState());
        device.setBoard(A.getBoard());
        device.setBrand(A.getBrand());
        device.setDisplay(A.getDisplay());
        device.setDevice(A.getDevice());
        device.setSystemVersion(A.getSystemVersion());
        device.setFingerprint(A.getFingerprint());
        device.setHardware(A.getHardware());
        device.setId(A.getId());
        device.setHost(A.getHost());
        device.setManufacturer(A.getManufacturer());
        device.setModel(A.getModel());
        device.setProduct(A.getProduct());
        device.setSerial(A.getSerial());
        device.setProcessorModelName(A.getDataCPUFile("Processor"));
        device.setProcessorModelName(A.getDataCPUFile("model name"));
        device.setProcessorBogomips(A.getDataCPUFile("BogoMIPS"));
        device.setProcessorFeatures(A.getDataCPUFile("Features"));
        device.setProcessorHardware(A.getDataCPUFile("Hardware"));
        device.setProcessorRevision(A.getDataCPUFile("Revision"));
        device.setProcessorSerial(A.getDataCPUFile("Serial"));
        device.setProcessorDevice(A.getDataCPUFile("Device"));
        device.setProcessorRadio(A.getDataCPUFile("Radio"));
        device.setProcessorMsmHardware(A.getDataCPUFile("MSM Hardware"));
        device.setCpuImplementer(A.getDataCPUFile("CPU implementer"));
        device.setCpuArchitecture(A.getDataCPUFile("CPU architecture"));
        device.setCpuVariant(A.getDataCPUFile("CPU variant"));
        device.setCpuPart(A.getDataCPUFile("CPU part"));
        device.setCpuRevision(A.getDataCPUFile("CPU revision"));
        device.setKernelStack(A.getDataMEMFile(""));
        device.setMemTotal(A.getDataMEMFile("MemTotal"));
        device.setSwapTotal("SwapTotal");
        device.setImei(A.getImei(context));
        device.setSoftwareVersion(A.getSoftwareVersion(context));
        device.setSystemName(A.getSystemName());
        device.setRoot(A.getRooted(context));
        device.setNfc(A.getNFCData(context));
        device.setGoogle_service_framework_gsf(A.getGSFID(context));
        device.setSensorData(A.getSensorsData(context));
        device.setSensor_size(A.getSensorSize(context));
        device.setWifi_state(A.getWifiState(context));
        device.setRed_g_state(A.getRedGState(context));
        device.setWlan0Mac(A.getMacAddress());
        device.setBluetoothMac(A.getBluetoothMacAddress());
        device.setProcessorQuantity(A.getProcessorQuantity());
        device.setUUID(A.getUUID());
        //=============== nope zone =================
        device.setCameras(new ArrayList<Camera>());
        device.setCameras_size("0");


        TrustLogger.d(new Gson().toJson(device));

        return device;
    }


    public static void getTrustIDLite(Context context, final TrustListener.OnResult<Audit> listener) {
        TrifleBody trifleBody = new TrifleBody();
        trifleBody.setDevice(getDeviceData(context));
        trifleBody.setSim(A.getListSIM(context));
        trifleBody.setTrustId(Hawk.contains(Constants.TRUST_ID_AUTOMATIC) ? Hawk.get(Constants.TRUST_ID_AUTOMATIC) : null);
        trifleBody.setIdentity(A.getIdentity());
        TrustClient.init(context);
        TrustClient.getInstance().sendTrifles(trifleBody, listener);
    }

    public static void saveIdentity(Identity identity) {
        Hawk.put(Constants.IDENTITY, identity);
    }

    public static void deleteIdentity() {
        if (Hawk.contains(Constants.IDENTITY)) {
            Hawk.delete(Constants.IDENTITY);
        }
    }

}

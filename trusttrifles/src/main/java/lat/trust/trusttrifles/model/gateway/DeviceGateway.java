package lat.trust.trusttrifles.model.gateway;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeviceGateway {

    @SerializedName("UUID")
    private String UUID;

    @SerializedName("android_device_id")
    private String androidDeviceId;

    @SerializedName("battery")
    private String battery;

    @SerializedName("battery_capacity")
    private String batteryCapacity;

    @SerializedName("battery_technology")
    private String batteryTechnology;

    @SerializedName("bluetooth_mac")
    private String bluetoothMac;

    @SerializedName("bluetooth_state")
    private String bluetoothState;

    @SerializedName("board")
    private String board;

    @SerializedName("brand")
    private String brand;

    @SerializedName("cameras_size")
    private String camerasSize;

    @SerializedName("cpu_architecture")
    private String cpuArchitecture;

    @SerializedName("cpu_implementer")
    private String cpuImplementer;

    @SerializedName("cpu_part")
    private String cpuPart;

    @SerializedName("cpu_revision")
    private String cpuRevision;

    @SerializedName("cpu_variant")
    private String cpuVariant;

    @SerializedName("device")
    private String device;

    @SerializedName("display")
    private String display;

    @SerializedName("fingerprint")
    private String fingerprint;

    @SerializedName("google_service_framework_gsf")
    private String googleServiceFrameworkGsf;

    @SerializedName("hardware")
    private String hardware;

    @SerializedName("host")
    private String host;

    @SerializedName("id")
    private String id;

    @SerializedName("imei")
    private String imei;

    @SerializedName("manufacturer")
    private String manufacturer;

    @SerializedName("mem_total")
    private String memTotal;

    @SerializedName("model")
    private String model;

    @SerializedName("nfc")
    private String nfc;

    @SerializedName("processor_bogomips")
    private String processorBogomips;

    @SerializedName("processor_features")
    private String processorFeatures;

    @SerializedName("processor_hardware")
    private String processorHardware;

    @SerializedName("processor_model_name")
    private String processorModelName;

    @SerializedName("processor_quantity")
    private String processorQuantity;

    @SerializedName("processor_revision")
    private String processorRevision;

    @SerializedName("processor_serial")
    private String processorSerial;

    @SerializedName("product")
    private String product;

    @SerializedName("red_g_state")
    private String redGState;

    @SerializedName("root")
    private String root;

    @SerializedName("sensor_size")
    private String sensorSize;

    @SerializedName("serial")
    private String serial;

    @SerializedName("software_version")
    private String softwareVersion;

    @SerializedName("swap_total")
    private String swapTotal;

    @SerializedName("system_name")
    private String systemName;

    @SerializedName("system_version")
    private String systemVersion;

    @SerializedName("wifi_state")
    private String wifi_state;

    @SerializedName("wlan0_mac")
    private String wlan0Mac;

    @SerializedName("cameras")
    private List<CameraGateway>  cameras;

    @SerializedName("sensorData")
    private List<SensorGateway> sensorData;



}

//example
/*



              "cameras": [
        {
        "focal_length": "3.70",
        "horizontal_view_angle": "62",
        "max_exposure_comp": "20",
        "mega_pixels": "12",
        "min_exposure_comp": "-20",
        "type": "BACK",
        "vertical_view_angle": "49"
        },
        {
        "focal_length": "1.95",
        "horizontal_view_angle": "71",
        "max_exposure_comp": "20",
        "mega_pixels": "4",
        "min_exposure_comp": "-20",
        "type": "FRONT",
        "vertical_view_angle": "56"
        }
        ],

        "sensorData": [
        {
        "name": "K2HH Acceleration ",
        "vendor": "STM"
        },
        {
        "name": "CM36672P Proximity Sensor",
        "vendor": "Capella Microsystems, Inc."
        },
        {
        "name": "SX9310 Grip Sensor",
        "vendor": "SEMTECH"
        },
        {
        "name": "Screen Orientation Sensor",
        "vendor": "Samsung Electronics"
        },
        {
        "name": "Motion Sensor",
        "vendor": "Samsung Electronics"
        }
        ],

 */
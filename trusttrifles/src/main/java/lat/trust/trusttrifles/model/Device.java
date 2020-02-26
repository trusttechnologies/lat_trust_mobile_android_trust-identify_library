package lat.trust.trusttrifles.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Device {
    @SerializedName("system_name")
    private String systemName = "Android";
    @SerializedName("system_version")
    private String systemVersion;
    @SerializedName("imei")
    private String imei;
    @SerializedName("board")
    private String board;
    @SerializedName("brand")
    private String brand;
    @SerializedName("device")
    private String device;
    @SerializedName("display")
    private String display;
    @SerializedName("fingerprint")
    private String fingerprint;
    @SerializedName("hardware")
    private String hardware;
    @SerializedName("host")
    private String host;
    @SerializedName("id")
    private String id;
    @SerializedName("manufacturer")
    private String manufacturer;
    @SerializedName("model")
    private String model;
    @SerializedName("product")
    private String product;
    @SerializedName("serial")
    private String serial;
    @SerializedName("processor_quantity")
    private String processorQuantity;
    @SerializedName("processor_model_name")
    private String processorModelName;
    @SerializedName("processor_bogomips")
    private String processorBogomips;
    @SerializedName("processor_features")
    private String processorFeatures;
    @SerializedName("processor_hardware")
    private String processorHardware;
    @SerializedName("processor_revision")
    private String processorRevision;
    @SerializedName("processor_serial")
    private String processorSerial;
    @SerializedName("processor_device")
    private String processorDevice;
    @SerializedName("processor_radio")
    private String processorRadio;
    @SerializedName("processor_msm_hardware")
    private String processorMsmHardware;
    @SerializedName("cpu_implementer")
    private String cpuImplementer;
    @SerializedName("cpu_variant")
    private String cpuVariant;
    @SerializedName("cpu_architecture")
    private String cpuArchitecture;
    @SerializedName("cpu_revision")
    private String cpuRevision;
    @SerializedName("cpu_part")
    private String cpuPart;
    @SerializedName("kernel_stack")
    private String kernelStack;
    @SerializedName("software_version")
    private String softwareVersion;
    @SerializedName("mem_total")
    private String memTotal;
    @SerializedName("swap_total")
    private String swapTotal;
    @SerializedName("wlan0_mac")
    private String wlan0Mac;
    @SerializedName("bluetooth_mac")
    private String bluetoothMac;
    private String cameras_size;
    private List<Camera> cameras;
    private List<SensorData> sensorData;
    private String sensor_size;
    private String nfc;
    private String android_device_id;
    private String google_service_framework_gsf;
    private String root;
    private String battery;
    private String battery_capacity;
    private String battery_technology;
    private String UUID;
    private boolean emulator;
    //06/11/2018
    private boolean wifi_state;
    private boolean bluetooth_state;
    private boolean red_g_state;

    public Device() {
    }

    public boolean isEmulator() {
        return emulator;
    }

    public void setEmulator(boolean emulator) {
        this.emulator = emulator;
    }

    public boolean getWifi_state() {
        return wifi_state;
    }

    public void setWifi_state(boolean wifi_state) {
        this.wifi_state = wifi_state;
    }

    public boolean getBluetooth_state() {
        return bluetooth_state;
    }

    public void setBluetooth_state(boolean bluetooth_state) {
        this.bluetooth_state = bluetooth_state;
    }

    public boolean getRed_g_state() {
        return red_g_state;
    }

    public void setRed_g_state(boolean red_g_state) {
        this.red_g_state = red_g_state;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getBattery_capacity() {
        return battery_capacity;
    }

    public void setBattery_capacity(String battery_capacity) {
        this.battery_capacity = battery_capacity;
    }

    public String getBattery_technology() {
        return battery_technology;
    }

    public void setBattery_technology(String battery_technology) {
        this.battery_technology = battery_technology;
    }

    public String getAndroid_device_id() {
        return android_device_id;
    }

    public void setAndroid_device_id(String android_device_id) {
        this.android_device_id = android_device_id;
    }

    public String getGoogle_service_framework_gsf() {
        return google_service_framework_gsf;
    }

    public void setGoogle_service_framework_gsf(String google_service_framework_gsf) {
        this.google_service_framework_gsf = google_service_framework_gsf;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getNfc() {
        return nfc;
    }

    public void setNfc(String nfc) {
        this.nfc = nfc;
    }

    public String getCameras_size() {
        return cameras_size;
    }

    public void setCameras_size(String cameras_size) {
        this.cameras_size = cameras_size;
    }

    public String getSensor_size() {
        return sensor_size;
    }

    public void setSensor_size(String sensor_size) {
        this.sensor_size = sensor_size;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public List<Camera> getCameras() {
        return cameras;
    }

    public void setCameras(List<Camera> cameras) {
        this.cameras = cameras;
    }

    public List<SensorData> getSensorData() {
        return sensorData;
    }

    public void setSensorData(List<SensorData> sensorData) {
        this.sensorData = sensorData;
    }

    public String getUUID() {
        return UUID;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public String getDisplay() {
        return display;
    }

    public String getWlan0Mac() {
        return wlan0Mac;
    }

    public void setWlan0Mac(String wlan0Mac) {
        this.wlan0Mac = wlan0Mac;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getHardware() {
        return hardware;
    }

    public void setHardware(String hardware) {
        this.hardware = hardware;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getProcessorQuantity() {
        return processorQuantity;
    }

    public void setProcessorQuantity(String processorQuantity) {
        this.processorQuantity = processorQuantity;
    }

    public String getProcessorModelName() {
        return processorModelName;
    }

    public void setProcessorModelName(String processorModelName) {
        this.processorModelName = processorModelName;
    }

    public String getProcessorBogomips() {
        return processorBogomips;
    }

    public void setProcessorBogomips(String processorBogomips) {
        this.processorBogomips = processorBogomips;
    }

    public String getProcessorFeatures() {
        return processorFeatures;
    }

    public void setProcessorFeatures(String processorFeatures) {
        this.processorFeatures = processorFeatures;
    }

    public String getProcessorHardware() {
        return processorHardware;
    }

    public void setProcessorHardware(String processorHardware) {
        this.processorHardware = processorHardware;
    }

    public String getProcessorRevision() {
        return processorRevision;
    }

    public void setProcessorRevision(String processorRevision) {
        this.processorRevision = processorRevision;
    }

    public String getProcessorSerial() {
        return processorSerial;
    }

    public void setProcessorSerial(String processorSerial) {
        this.processorSerial = processorSerial;
    }

    public String getProcessorDevice() {
        return processorDevice;
    }

    public void setProcessorDevice(String processorDevice) {
        this.processorDevice = processorDevice;
    }

    public String getProcessorRadio() {
        return processorRadio;
    }

    public void setProcessorRadio(String processorRadio) {
        this.processorRadio = processorRadio;
    }

    public String getProcessorMsmHardware() {
        return processorMsmHardware;
    }

    public void setProcessorMsmHardware(String processorMsmHardware) {
        this.processorMsmHardware = processorMsmHardware;
    }

    public String getCpuImplementer() {
        return cpuImplementer;
    }

    public void setCpuImplementer(String cpuImplementer) {
        this.cpuImplementer = cpuImplementer;
    }

    public String getCpuVariant() {
        return cpuVariant;
    }

    public void setCpuVariant(String cpuVariant) {
        this.cpuVariant = cpuVariant;
    }

    public String getCpuArchitecture() {
        return cpuArchitecture;
    }

    public void setCpuArchitecture(String cpuArchitecture) {
        this.cpuArchitecture = cpuArchitecture;
    }

    public String getCpuRevision() {
        return cpuRevision;
    }

    public void setCpuRevision(String cpuRevision) {
        this.cpuRevision = cpuRevision;
    }

    public String getCpuPart() {
        return cpuPart;
    }

    public void setCpuPart(String cpuPart) {
        this.cpuPart = cpuPart;
    }

    public String getKernelStack() {
        return kernelStack;
    }

    public void setKernelStack(String kernelStack) {
        this.kernelStack = kernelStack;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public String getMemTotal() {
        return memTotal;
    }

    public void setMemTotal(String memTotal) {
        this.memTotal = memTotal;
    }

    public String getSwapTotal() {
        return swapTotal;
    }

    public void setSwapTotal(String swapTotal) {
        this.swapTotal = swapTotal;
    }


    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getBluetoothMac() {
        return bluetoothMac;
    }

    public void setBluetoothMac(String bluetoothMac) {
        this.bluetoothMac = bluetoothMac;
    }
}

package lat.trust.trusttrifles.model.gateway;

import com.google.gson.annotations.SerializedName;

public class DeviceBody {

    @SerializedName("device")
    private DeviceGateway device;

    public DeviceGateway getDevice() {
        return device;
    }

    public void setDevice(DeviceGateway device) {
        this.device = device;
    }
}





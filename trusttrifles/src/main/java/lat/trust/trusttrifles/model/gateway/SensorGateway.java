package lat.trust.trusttrifles.model.gateway;

import com.google.gson.annotations.SerializedName;

public class SensorGateway {

    @SerializedName("name")
    private String name;
    @SerializedName("vendor")
    private String vendor;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
}

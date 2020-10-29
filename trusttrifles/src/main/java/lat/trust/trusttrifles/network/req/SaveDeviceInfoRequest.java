package lat.trust.trusttrifles.network.req;

import com.google.gson.annotations.SerializedName;

public class SaveDeviceInfoRequest {
   /* {
        "trust_id": "",
            "dni": "",
            "bundle_id": ""
    }*/

    @SerializedName("trust_id")
    private String trustId;
    private String dni;
    @SerializedName("bundle_id")
    private String bundleId;
    @SerializedName("flavor_id")
    private String flavorId;

    public SaveDeviceInfoRequest() {
    }

    public String getTrustId() {
        return trustId;
    }

    public void setTrustId(String trustId) {
        this.trustId = trustId;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getFlavorId() {
        return flavorId;
    }

    public void setFlavorId(String flavorId) {
        this.flavorId = flavorId;
    }
}

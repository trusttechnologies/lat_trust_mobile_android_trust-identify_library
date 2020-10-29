package lat.trust.trusttrifles.model;

import com.google.gson.annotations.SerializedName;

public class SaveDeviceInfoFlavor {

    @SerializedName("trust_id")
    private String trustId;
    @SerializedName("bundle_id")
    private String bundleId;

    public SaveDeviceInfoFlavor() {
    }


    public String getTrustId() {
        return trustId;
    }

    public void setTrustId(String trustId) {
        this.trustId = trustId;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }
}
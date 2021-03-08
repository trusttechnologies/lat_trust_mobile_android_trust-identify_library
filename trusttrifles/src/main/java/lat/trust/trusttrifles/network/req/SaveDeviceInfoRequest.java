package lat.trust.trusttrifles.network.req;

import com.google.gson.annotations.SerializedName;

import lat.trust.trusttrifles.model.Identity;

public class SaveDeviceInfoRequest {

    @SerializedName("trust_id")
    private String trustId;
    private String dni;
    @SerializedName("bundle_id")
    private String bundleId;
    @SerializedName("flavor_id")
    private String flavorId;
    @SerializedName("person")
    private Identity person;

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

    public Identity getPerson() {
        return person;
    }

    public void setPerson(Identity person) {
        this.person = person;
    }
}

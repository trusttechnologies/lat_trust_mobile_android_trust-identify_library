package lat.trust.trusttrifles.model.gateway;

import com.google.gson.annotations.SerializedName;

public class PersonBody {

    @SerializedName("trust_id")
    private String trustId;

    @SerializedName("bundle_id")
    private String bundleId;

    @SerializedName("flavor_id")
    private String flavorId;

    @SerializedName("person")
    private PersonGateway personGateway;


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

    public String getFlavorId() {
        return flavorId;
    }

    public void setFlavorId(String flavorId) {
        this.flavorId = flavorId;
    }

    public PersonGateway getPersonGateway() {
        return personGateway;
    }

    public void setPersonGateway(PersonGateway personGateway) {
        this.personGateway = personGateway;
    }
}




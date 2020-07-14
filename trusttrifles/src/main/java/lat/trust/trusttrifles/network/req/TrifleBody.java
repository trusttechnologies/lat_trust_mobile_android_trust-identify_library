package lat.trust.trusttrifles.network.req;


import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lat.trust.trusttrifles.model.Device;
import lat.trust.trusttrifles.model.Identity;
import lat.trust.trusttrifles.model.SIM;

public class TrifleBody {
    private Device device;
    private List<SIM> sim;
    private Identity identity;
    @SerializedName("trust_id")
    private String trustId;
    @SerializedName("trust_id_type")
    private String trustIdType;

    private String operation;
    @SerializedName("wrong_trust_id")
    private String wrong_trust_id;

    public String getTrustIdType() {
        return trustIdType;
    }

    public void setTrustIdType(String trustIdType) {
        this.trustIdType = trustIdType;
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public String getTrustId() {
        return trustId;
    }

    public TrifleBody() {
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public List<SIM> getSim() {
        return sim;
    }

    public void setSim(List<SIM> sim) {
        this.sim = sim;
    }

    public String toJSON() {
        Gson gson = new Gson();
        String json = gson.toJson(this); //convert
        System.out.println(json);
        return json;
    }

    public void setTrustId(String trustId) {
        this.trustId = trustId;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setWrong_trustId(String wrong_trust_d) {
        this.wrong_trust_id = wrong_trust_d;
    }
}

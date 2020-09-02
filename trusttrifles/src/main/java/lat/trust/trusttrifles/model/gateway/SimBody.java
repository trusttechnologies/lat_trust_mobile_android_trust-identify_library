package lat.trust.trusttrifles.model.gateway;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SimBody {

    @SerializedName("sim")
    List<SimGateway> sim;


    public List<SimGateway> getSim() {
        return sim;
    }

    public void setSim(List<SimGateway> sim) {
        this.sim = sim;
    }
}

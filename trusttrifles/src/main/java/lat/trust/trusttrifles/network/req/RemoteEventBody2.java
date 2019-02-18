package lat.trust.trusttrifles.network.req;

import lat.trust.trusttrifles.model.Geo;
import lat.trust.trusttrifles.model.SIM;

public class RemoteEventBody2 {
    private String trustid;
    private SIM object; // device-sim
    private String key; // i.e. imei
    private String value; // i.e 13251345634654
    private Geo geo;

    public RemoteEventBody2() {
    }

    public RemoteEventBody2(String trustid, SIM object, String key, String value, Geo geo) {
        this.trustid = trustid;
        this.object = object;
        this.key = key;
        this.value = value;
        this.geo = geo;
    }


}

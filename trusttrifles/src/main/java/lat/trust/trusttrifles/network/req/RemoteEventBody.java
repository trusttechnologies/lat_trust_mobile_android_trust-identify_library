package lat.trust.trusttrifles.network.req;

import lat.trust.trusttrifles.model.Geo;

public class RemoteEventBody {
    private String trustid;
    private String object; // device-sim
    private String key; // i.e. imei
    private String value; // i.e 13251345634654
    private Geo geo;

    public RemoteEventBody() {
    }

    public RemoteEventBody(String trustid, String object, String key, String value, Geo geo) {
        this.trustid = trustid;
        this.object = object;
        this.key = key;
        this.value = value;
        this.geo = geo;
    }

    public void setTrustid(String trustid) {
        this.trustid = trustid;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
    }
}

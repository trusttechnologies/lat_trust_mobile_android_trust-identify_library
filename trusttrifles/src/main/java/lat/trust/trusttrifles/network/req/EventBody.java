package lat.trust.trusttrifles.network.req;

import com.google.gson.annotations.SerializedName;

public class EventBody {
    @SerializedName("trustid")
    private String trustId;
    @SerializedName("bundle_id")
    private String bundleId;
    @SerializedName("system_name")
    private String systemName = "Android";
    @SerializedName("system_version")
    private String systemVersion;
    @SerializedName("event_type")
    private String eventType; //SIM CHANGE
    @SerializedName("event_value")
    private String eventValue; //ABSENT - ID
    private double lat;
    private double lng;

    public EventBody() {
    }

    public EventBody(String trustId, String bundleId, String systemVersion, String eventType, String eventValue, double lat, double lng) {
        this.trustId = trustId;
        this.bundleId = bundleId;
        this.systemVersion = systemVersion;
        this.eventType = eventType;
        this.eventValue = eventValue;
        this.lat = lat;
        this.lng = lng;
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

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventValue() {
        return eventValue;
    }

    public void setEventValue(String eventValue) {
        this.eventValue = eventValue;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}

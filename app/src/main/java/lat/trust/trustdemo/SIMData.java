package lat.trust.trustdemo;

public class SIMData {
    public static final int TYPE_SIM = 0;
    public static final int TYPE_IMEI = 1;
    private String id;
    private int type;
    private long timestamp;
    private boolean updated;

    public SIMData(String id, long timestamp, int type) {
        this.id = id;
        this.timestamp = timestamp;
        this.type = type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public String getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getType() {
        return type;
    }
}

package lat.trust.trusttrifles.model;

import com.google.gson.annotations.SerializedName;

public class Audit {
    @SerializedName("trustid")
    private String trustId;
    @SerializedName("_id")
    private Audit.ID id;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    String updatedAt;

    public String getTrustId() {
        return trustId;
    }

    public Audit.ID getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public class ID {
        @SerializedName("$oid")
        private String id;

        public String getId() {
            return id;
        }
    }
}

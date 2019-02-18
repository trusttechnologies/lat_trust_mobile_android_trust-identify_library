package lat.trust.trusttrifles.model;

import com.google.gson.annotations.SerializedName;

public class AuditSource {
    @SerializedName("trustid")
    private String trustId;
    @SerializedName("app_name")
    private String appName;
    @SerializedName("bundle_id")
    private String bundleId;
    @SerializedName("system_name")
    private String systemName;
    @SerializedName("system_version")
    private String systemVersion;

    public AuditSource(String trustId, String appName, String bundleId, String systemName, String systemVersion) {
        this.trustId = trustId;
        this.appName = appName;
        this.bundleId = bundleId;
        this.systemName = systemName;
        this.systemVersion = systemVersion;
    }

    public void setTrustId(String trustId) {
        this.trustId = trustId;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }
}

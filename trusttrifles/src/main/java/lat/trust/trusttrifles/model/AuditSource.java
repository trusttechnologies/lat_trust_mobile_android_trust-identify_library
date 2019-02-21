package lat.trust.trusttrifles.model;

import com.google.gson.annotations.SerializedName;

public class AuditSource {
    //@SerializedName("trustid")
    private String trustid;
    //@SerializedName("app_name")
    private String app_name;
    //@SerializedName("bundle_id")
    private String bundle_id;
    //@SerializedName("system_name")
    private String system_name;
    //@SerializedName("system_version")
    private String system_version;

    public AuditSource(String trustid, String app_name, String bundle_id, String system_name, String system_version) {
        this.trustid = trustid;
        this.app_name = app_name;
        this.bundle_id = bundle_id;
        this.system_name = system_name;
        this.system_version = system_version;
    }

    public void setTrustid(String trustid) {
        this.trustid = trustid;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public void setBundle_id(String bundle_id) {
        this.bundle_id = bundle_id;
    }

    public void setSystem_name(String system_name) {
        this.system_name = system_name;
    }

    public void setSystem_version(String system_version) {
        this.system_version = system_version;
    }
}

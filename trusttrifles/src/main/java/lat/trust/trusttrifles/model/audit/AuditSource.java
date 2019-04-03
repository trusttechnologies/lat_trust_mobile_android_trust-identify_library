package lat.trust.trusttrifles.model.audit;

public class AuditSource {
    private String trust_id;
    private String app_name;
    private String bundle_id;
    private String os;
    private String os_version;
    private String device_name;
    private String imsi;
    private String latGeo;
    private String lonGeo;
    private String connection_type;
    private String connection_name;
    private String version_app;


    public AuditSource() {
    }

    public String getVersion_app() {
        return version_app;
    }

    public void setVersion_app(String version_app) {
        this.version_app = version_app;
    }

    public AuditSource(String trust_id, String app_name, String bundle_id, String os, String os_version, String device_name, String imsi, String latGeo, String lonGeo, String connection_type, String connection_name, String version_app) {
        this.trust_id = trust_id;
        this.app_name = app_name;
        this.bundle_id = bundle_id;
        this.os = os;
        this.os_version = os_version;
        this.device_name = device_name;
        this.imsi = imsi;
        this.latGeo = latGeo;
        this.lonGeo = lonGeo;
        this.connection_type = connection_type;
        this.connection_name = connection_name;
        this.version_app = version_app;
    }

    public String getTrust_id() {
        return trust_id;
    }

    public void setTrust_id(String trust_id) {
        this.trust_id = trust_id;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getBundle_id() {
        return bundle_id;
    }

    public void setBundle_id(String bundle_id) {
        this.bundle_id = bundle_id;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOs_version() {
        return os_version;
    }

    public void setOs_version(String os_version) {
        this.os_version = os_version;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getLatGeo() {
        return latGeo;
    }

    public void setLatGeo(String latGeo) {
        this.latGeo = latGeo;
    }

    public String getLonGeo() {
        return lonGeo;
    }

    public void setLonGeo(String lonGeo) {
        this.lonGeo = lonGeo;
    }

    public String getConnection_type() {
        return connection_type;
    }

    public void setConnection_type(String connection_type) {
        this.connection_type = connection_type;
    }

    public String getConnection_name() {
        return connection_name;
    }

    public void setConnection_name(String connection_name) {
        this.connection_name = connection_name;
    }
    /*
    * 	"trust_id": "5c525b06a96c977a355667ea1",
		"app_name": "Enrollment",
		"bundle_id": "com.example.enrollment",
		"os": "Android",
		"os_version": "26",
		"device_name": "SM-G9650",
		"imsi": "73020702904418104",
		"latGeo": "40.416875",
		"lonGeo": "-3.703308",
		"connection_type": "wifi",
		"connection_name": "Jumpitt wifi"*/
}

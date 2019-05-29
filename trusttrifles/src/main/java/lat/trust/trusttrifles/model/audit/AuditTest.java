package lat.trust.trusttrifles.model.audit;

public class AuditTest {
    private String type_audit;
    private String application;
    private AuditSource source;
    private String platform;
    private AuditTransaction transaction;
    private AuditExtraData extra_data;
    private String audit_id;

    public String getType_audit() {
        return type_audit;
    }

    public void setType_audit(String type_audit) {
        this.type_audit = type_audit;
    }

    public AuditTest() {
    }

    public String getAuditid() {
        return audit_id;
    }

    public void setAuditid(String auditid) {
        this.audit_id = auditid;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public AuditTest(String type_audit, String application, AuditSource source, String platform, AuditTransaction transaction, AuditExtraData extra_data) {
        this.type_audit = type_audit;
        this.application = application;
        this.source = source;
        this.platform = platform;
        this.transaction = transaction;
        this.extra_data = extra_data;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public AuditSource getSource() {
        return source;
    }

    public void setSource(AuditSource source) {
        this.source = source;
    }

    public AuditTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(AuditTransaction transaction) {
        this.transaction = transaction;
    }

    public AuditExtraData getExtra_data() {
        return extra_data;
    }

    public void setExtra_data(AuditExtraData extra_data) {
        this.extra_data = extra_data;
    }
}

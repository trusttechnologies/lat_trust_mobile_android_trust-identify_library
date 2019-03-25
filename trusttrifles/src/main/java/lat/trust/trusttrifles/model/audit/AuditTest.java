package lat.trust.trusttrifles.model.audit;

public class AuditTest {

    private String application;
    private AuditSource source;
    private AuditTransaction transaction;
    private AuditExtraData extra_data;


    public AuditTest() {
    }

    public AuditTest(String application, AuditSource source, AuditTransaction transaction, AuditExtraData extra_data) {
        this.application = application;
        this.source = source;
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

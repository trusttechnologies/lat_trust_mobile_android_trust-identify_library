package lat.trust.trusttrifles.network.req;

import lat.trust.trusttrifles.model.audit.AuditSource;
import lat.trust.trusttrifles.model.audit.AuditTest;
import lat.trust.trusttrifles.model.audit.AuditTransaction;

public class AuditBodyTest {
    private String application;
    private AuditSource auditSource;
    private AuditTransaction auditTransaction;
    private AuditTest auditTest;

    public AuditBodyTest(String application, AuditSource auditSource, AuditTransaction auditTransaction, AuditTest auditTest) {
        this.application = application;
        this.auditSource = auditSource;
        this.auditTransaction = auditTransaction;
        this.auditTest = auditTest;
    }

    public AuditBodyTest() {
    }
}

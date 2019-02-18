package lat.trust.trusttrifles.network.req;

import lat.trust.trusttrifles.model.AuditSource;
import lat.trust.trusttrifles.model.AuditTransaction;
import lat.trust.trusttrifles.model.Geo;

public class AuditBody {
    private AuditSource source;
    private AuditTransaction transaction;
    private Geo geo;

    public AuditBody(AuditSource source, AuditTransaction transaction, Geo geo) {
        this.source = source;
        this.transaction = transaction;
        this.geo = geo;
    }
}

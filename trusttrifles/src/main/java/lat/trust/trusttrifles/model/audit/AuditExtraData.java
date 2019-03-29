package lat.trust.trusttrifles.model.audit;

import lat.trust.trusttrifles.model.Identity;

public class AuditExtraData {

    private Identity identity;

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public AuditExtraData() {
    }

}

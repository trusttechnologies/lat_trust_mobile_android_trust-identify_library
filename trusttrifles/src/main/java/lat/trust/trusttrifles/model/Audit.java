package lat.trust.trusttrifles.model;

public class Audit {
    private Boolean status;
    private String message;
    private String trustid;

    public Audit() {
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTrustid() {
        return trustid;
    }

    public void setTrustid(String trustid) {
        this.trustid = trustid;
    }
}

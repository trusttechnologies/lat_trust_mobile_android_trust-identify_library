package lat.trust.trusttrifles.network.req;

public class CallbackACKRequest {
    private String message_id;
    private String action;
    private String status;
    private String trust_id;

    public String getTrust_id() {
        return trust_id;
    }

    public void setTrust_id(String trust_id) {
        this.trust_id = trust_id;
    }

    public CallbackACKRequest(String message_id, String action, String status, String trust_id) {
        this.message_id = message_id;
        this.action = action;
        this.status = status;
        this.trust_id = trust_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}

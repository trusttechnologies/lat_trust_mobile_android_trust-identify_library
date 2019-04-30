package lat.trust.trusttrifles.network.req;

public class CallbackACKRequest {
    private String message_id;
    private String action;
    private String status;

    public CallbackACKRequest(String message_id, String action, String status) {
        this.message_id = message_id;
        this.action = action;
        this.status = status;
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

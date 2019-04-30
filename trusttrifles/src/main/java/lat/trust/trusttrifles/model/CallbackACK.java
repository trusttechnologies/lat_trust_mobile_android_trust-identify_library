package lat.trust.trusttrifles.model;

public class CallbackACK {
     private String messageId;
     private String action;
    private String status;

    public String getMessageId() {
        return messageId;
    }

    public CallbackACK(String messageId, String action, String status) {
        this.messageId = messageId;
        this.action = action;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}

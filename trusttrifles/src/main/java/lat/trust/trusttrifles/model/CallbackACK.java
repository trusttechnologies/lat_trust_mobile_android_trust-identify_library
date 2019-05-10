package lat.trust.trusttrifles.model;

import com.google.gson.annotations.SerializedName;

public class CallbackACK {

     private String messageId;
     private String action;
    private String status;
    private String trustId;
    private String error_message;
    private String type;

    public CallbackACK(String messageId, String action, String status, String trustId, String error_message, String type) {
        this.messageId = messageId;
        this.action = action;
        this.status = status;
        this.trustId = trustId;
        this.error_message = error_message;
        this.type = type;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTrustId() {
        return trustId;
    }

    public void setTrustId(String trustId) {
        this.trustId = trustId;
    }

    public String getMessageId() {
        return messageId;
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

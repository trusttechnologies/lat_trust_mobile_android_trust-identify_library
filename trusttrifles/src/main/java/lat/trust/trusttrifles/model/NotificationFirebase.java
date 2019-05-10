package lat.trust.trusttrifles.model;

import android.content.Context;

import java.util.Map;

public class NotificationFirebase {
    private String messageId;
    private Map<String, String> data;
    private Context context;

    public NotificationFirebase(String messageId, Map<String, String> data, Context context) {
        this.messageId = messageId;
        this.data = data;
        this.context = context;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}

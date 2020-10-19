package lat.trust.trusttrifles.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseCompanyFlavors {

    @SerializedName("status")
    private boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("apps")
    private ArrayList<AppFlavor> apps;

    public ResponseCompanyFlavors() { }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<AppFlavor> getApps() {
        return apps;
    }

    public void setApps(ArrayList<AppFlavor> apps) {
        this.apps = apps;
    }
}

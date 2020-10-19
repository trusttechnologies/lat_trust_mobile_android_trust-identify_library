package lat.trust.trusttrifles.model;

import java.util.ArrayList;

public class TrustResponse {


    private Boolean status;
    private String message;
    private String trustid;
    private ArrayList<AppFlavor> apps;

    public TrustResponse() { }

    public ArrayList<AppFlavor> getApp() {
        return apps;
    }

    public void setApp(ArrayList<AppFlavor> app) {
        this.apps = app;
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

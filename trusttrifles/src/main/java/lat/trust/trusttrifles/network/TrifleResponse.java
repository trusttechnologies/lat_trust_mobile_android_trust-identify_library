package lat.trust.trusttrifles.network;

import com.google.gson.annotations.SerializedName;

import lat.trust.trusttrifles.model.Audit;

public class TrifleResponse {
    private Boolean status;
    private String message;
    private String trustid;

    private Audit audit;

    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }

    public TrifleResponse() {
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


    //todo cambiar url a url2
}
//{"status":true,"message":"The device was identified, it already exists in our records.","trustid":"46de3ab6-f250-4149-ad9d-34bc32f21095"}
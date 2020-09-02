package lat.trust.trusttrifles.network;

import lat.trust.trusttrifles.model.Trust;

public class TrifleResponse {
    private Boolean status;
    private String message;
    private String trustid;

    private Trust trust;

    public Trust getTrust() {
        return trust;
    }

    public void setTrust(Trust trust) {
        this.trust = trust;
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
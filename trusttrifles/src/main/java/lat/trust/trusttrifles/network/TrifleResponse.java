package lat.trust.trusttrifles.network;

import com.google.gson.annotations.SerializedName;

import lat.trust.trusttrifles.model.TrustResponse;

public class TrifleResponse {
    private Boolean status;
    private String message;
    private String trustid;
    @SerializedName("score_v1")
    private String score;
    @SerializedName("trust_id_type")
    private String trustIdType;


    public String getTrustIdType() {
        return trustIdType;
    }

    public void setTrustIdType(String trustIdType) {
        this.trustIdType = trustIdType;
    }

    private TrustResponse trustResponse;

    public TrustResponse getTrustResponse() {
        return trustResponse;
    }

    public void setTrustResponse(TrustResponse trustResponse) {
        this.trustResponse = trustResponse;
    }

    public TrifleResponse() {
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
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
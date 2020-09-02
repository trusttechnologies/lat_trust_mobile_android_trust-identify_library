package lat.trust.trusttrifles.model.gateway;

import com.google.gson.annotations.SerializedName;

public class SimGateway {

    @SerializedName("cid")
    private String cid;

    @SerializedName("iccid")
    private String iccid;

    @SerializedName("imei")
    private String imei;

    @SerializedName("imsi")
    private String imsi;

    @SerializedName("lac")
    private String lac;

    @SerializedName("mcc")
    private String mcc;

    @SerializedName("mccmnc")
    private String mccmnc;

    @SerializedName("meid_esn")
    private String meidEsn;

    @SerializedName("mnc")
    private String mnc;

    @SerializedName("msisdn")
    private String msisdn;

    @SerializedName("spn")
    private String spn;


    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getLac() {
        return lac;
    }

    public void setLac(String lac) {
        this.lac = lac;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getMccmnc() {
        return mccmnc;
    }

    public void setMccmnc(String mccmnc) {
        this.mccmnc = mccmnc;
    }

    public String getMeidEsn() {
        return meidEsn;
    }

    public void setMeidEsn(String meidEsn) {
        this.meidEsn = meidEsn;
    }

    public String getMnc() {
        return mnc;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getSpn() {
        return spn;
    }

    public void setSpn(String spn) {
        this.spn = spn;
    }
}




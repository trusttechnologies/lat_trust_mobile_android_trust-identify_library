package lat.trust.trusttrifles.model;

import com.google.gson.annotations.SerializedName;

public class SIM {
    @SerializedName("iccid")
    private String iccid;
    @SerializedName("imei")
    private String imei;
    @SerializedName("meid_esn")
    private String meidEsn;
    @SerializedName("imsi")
    private String imsi;
    @SerializedName("spn")
    private String spn;
    @SerializedName("mccmnc")
    private String mccmnc;
    @SerializedName("mcc")
    private String mcc;
    @SerializedName("mnc")
    private String mnc;
    @SerializedName("msisdn")
    private String msisdn;
    @SerializedName("lac")
    private String lac;
    @SerializedName("cid")
    private String cid;

    public SIM() {
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

    public String getMeidEsn() {
        return meidEsn;
    }

    public void setMeidEsn(String meidEsn) {
        this.meidEsn = meidEsn;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getSpn() {
        return spn;
    }

    public void setSpn(String spn) {
        this.spn = spn;
    }

    public String getMccmnc() {
        return mccmnc;
    }

    public void setMccmnc(String mccmnc) {
        this.mccmnc = mccmnc;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
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

    public String getLac() {
        return lac;
    }

    public void setLac(String lac) {
        this.lac = lac;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}

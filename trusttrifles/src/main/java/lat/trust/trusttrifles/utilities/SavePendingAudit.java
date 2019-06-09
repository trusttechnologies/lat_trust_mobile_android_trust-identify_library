package lat.trust.trusttrifles.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Handler;

import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import lat.trust.trusttrifles.TrustClient;
import lat.trust.trusttrifles.TrustListener;
import lat.trust.trusttrifles.model.audit.AuditExtraData;
import lat.trust.trusttrifles.model.audit.AuditTest;
import lat.trust.trusttrifles.model.audit.AuditTransaction;

public class SavePendingAudit {


    private SavePendingAudit() {
    }


    public static void init(Context context) {

    }


    /**
     * return the actual size of the list of pendings audits
     */
    public static int getSizeAudit() {
        List<AuditTest> lstAudit2 = Hawk.get(Constants.LST_AUDIT);
        return lstAudit2.size();
    }


    /**
     * Funcion 1 encargada de crear una auditoria con contexto y retorna UUID
     */
    public static void createOfflineAudit(String operation, String method, String result, Context mContext, TrustListener.OnResultAudit audit) {
        try {
            TrustLogger.d("saving offline audit...");

            AuditTest auditTest = new AuditTest();
            auditTest.setAuditid(AutomaticAudit.getUUIDAuditOffline());
            auditTest.setApplication(TrustClient.getAppName());
            auditTest.setExtra_data(AutomaticAudit.getExtraData());
            auditTest.setPlatform("Android");
            auditTest.setTransaction(new AuditTransaction(result, method, operation, Utils.getCurrentTimeStamp()));
            auditTest.setSource(TrustClient.getAuditSourceOffline(
                    Hawk.get(Constants.TRUST_ID_AUTOMATIC).toString(),
                    AutomaticAudit.getLatitude(mContext),
                    AutomaticAudit.getLongitude(mContext)));
            auditTest.setType_audit("trust identify");
            addAudit(auditTest);
            audit.onSuccess(auditTest.getAuditid());
        } catch (Exception ex) {
            TrustLogger.d("[TRUST CLIENT] create offline audit error v2: " + ex.getMessage());
        }
    }

    /**
     * Funcion 2 encargada de crear una auditoria con contexto y retorna UUID
     */
    public static void createOfflineAudit(String operation, String method, String result, Object object, Context mContext, TrustListener.OnResultAudit audit) {
        try {
            TrustLogger.d("saving offline audit...");

            AuditTest auditTest = new AuditTest();
            auditTest.setAuditid(AutomaticAudit.getUUIDAuditOffline());
            auditTest.setApplication(TrustClient.getAppName());
            auditTest.setExtra_data(AutomaticAudit.getExtraData());
            auditTest.setPlatform("Android");
            auditTest.setTransaction(new AuditTransaction(result.concat(new Gson().toJson(object)).replace("\\", ""), method, operation, Utils.getCurrentTimeStamp()));
            auditTest.setSource(TrustClient.getAuditSourceOffline(
                    Hawk.get(Constants.TRUST_ID_AUTOMATIC).toString(),
                    AutomaticAudit.getLatitude(mContext),
                    AutomaticAudit.getLongitude(mContext)));
            auditTest.setType_audit("trust identify");
            addAudit(auditTest);
            audit.onSuccess(auditTest.getAuditid());
        } catch (Exception ex) {
            TrustLogger.d("[TRUST CLIENT] create offline audit error v2: " + ex.getMessage());
        }
    }

    /**
     * Funcion 3 encargada de crear una auditoria con contexto y retorna UUID
     */
    public static void createOfflineAudit(String operation, String method, String result, Object object, Context mContext) {
        try {
            TrustLogger.d("saving offline audit...");

            AuditTest auditTest = new AuditTest();
            auditTest.setAuditid(AutomaticAudit.getUUIDAuditOffline());
            auditTest.setApplication(TrustClient.getAppName());
            auditTest.setExtra_data(AutomaticAudit.getExtraData());
            auditTest.setPlatform("Android");
            auditTest.setTransaction(new AuditTransaction(result.concat(new Gson().toJson(object)), method, operation, Utils.getCurrentTimeStamp()));
            auditTest.setSource(TrustClient.getAuditSourceOffline(
                    Hawk.get(Constants.TRUST_ID_AUTOMATIC).toString(),
                    AutomaticAudit.getLatitude(mContext),
                    AutomaticAudit.getLongitude(mContext)));
            auditTest.setType_audit("trust identify");
            addAudit(auditTest);
        } catch (Exception ex) {
            TrustLogger.d("[TRUST CLIENT] create offline audit error v2: " + ex.getMessage());
        }
    }

    /**
     * Funcion 4 encargada de crear una auditoria con contexto y retorna UUID
     */
    public static void createOfflineAudit(String operation, String method, String result, Context mContext) {
        try {
            TrustLogger.d("saving offline audit...");
            AuditTest auditTest = new AuditTest();
            auditTest.setAuditid(AutomaticAudit.getUUIDAuditOffline());
            auditTest.setApplication(TrustClient.getAppName());
            auditTest.setExtra_data(AutomaticAudit.getExtraData());
            auditTest.setPlatform("Android");
            auditTest.setTransaction(new AuditTransaction(result, method, operation, Utils.getCurrentTimeStamp()));
            auditTest.setSource(TrustClient.getAuditSourceOffline(
                    Hawk.get(Constants.TRUST_ID_AUTOMATIC).toString(),
                    AutomaticAudit.getLatitude(mContext),
                    AutomaticAudit.getLongitude(mContext)));
            auditTest.setType_audit("trust identify");
            addAudit(auditTest);
        } catch (Exception ex) {
            TrustLogger.d("[TRUST CLIENT] create offline audit error v1: " + ex.getMessage());

        }

    }

    /**
     * encargado de agregar a una lista local las auditorias sin enviar
     *
     * @param auditTest
     */
    private static void addAudit(AuditTest auditTest) {
        try {
            TrustLogger.d("[TRUST CLIENT] add new audittest to list: " + auditTest.getAuditid());
            List<AuditTest> lstAudit;
            if (Hawk.contains(Constants.LST_AUDIT)) {
                lstAudit = Hawk.get(Constants.LST_AUDIT);
            } else {
                lstAudit = new ArrayList<AuditTest>();
            }
            lstAudit.add(auditTest);
            Hawk.put(Constants.LST_AUDIT, lstAudit);
            TrustLogger.d("[TRUST CLIENT] total list: " + lstAudit.size());

        } catch (Exception ex) {
            TrustLogger.d("[TRUST CLIENT] error add audit: " + ex.getMessage());
        }
    }

    public static void sendOfflineAudit() {
        try {
            new Handler().postDelayed(new Runnable() {
                @SuppressLint("MissingPermission")
                @Override
                public void run() {
                    List<AuditTest> lstAudit;
                    if (Hawk.contains(Constants.LST_AUDIT)) {
                        lstAudit = Hawk.get(Constants.LST_AUDIT);
                    } else {
                        lstAudit = new ArrayList<AuditTest>();
                    }
                    TrustLogger.d("[TRUST CLIENT] sending pending audits, total: " + lstAudit.size());

                    TrustClient mClient = TrustClient.getInstance();
                    for (AuditTest audit : lstAudit) {
                        mClient.sendAudit(audit);
                    }
                    TrustLogger.d("[TRUST CLIENT] clear the list ");
                    Hawk.delete(Constants.LST_AUDIT);
                }
            }, 5000);


        } catch (Exception ex) {
            TrustLogger.d("[TRUST CLIENT] error sending pending audit: " + ex.getMessage());

        }
    }

    public class SavedAudit {
        private String operation;
        private String method;
        private String result;
        private String lat;
        private String lng;
        private String audit_id;
        private Long timestamp;


        public SavedAudit(String operation, String method, String result, String lat, String lng, Long timestamp) {
            this.operation = operation;
            this.method = method;
            this.result = result;
            this.lat = lat;
            this.lng = lng;
            this.timestamp = timestamp;
        }

        public String getAudit_id() {
            return audit_id;
        }

        public void setAudit_id(String audit_id) {
            this.audit_id = audit_id;
        }

        public String getOperation() {
            return operation;
        }

        public void setOperation(String operation) {
            this.operation = operation;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }
    }

}

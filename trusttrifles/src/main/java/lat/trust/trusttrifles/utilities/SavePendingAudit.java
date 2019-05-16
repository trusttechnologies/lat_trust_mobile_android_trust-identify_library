package lat.trust.trusttrifles.utilities;

import android.content.Context;
import android.location.Location;

import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

public class SavePendingAudit {


    private static List<SavedAudit> lstAudit;
    private static SavePendingAudit instance;
    private static Context mContext;

    private SavePendingAudit() {
        lstAudit = new ArrayList<SavedAudit>();
    }

    /**
     * return the instance
     *
     * @return
     */
    public static SavePendingAudit getInstance() {
        return instance == null ? new SavePendingAudit() : instance;
    }

    /**
     * return the list of  pendings audits
     *
     * @return
     */
    public List<SavedAudit> getLstAudit() {
        return lstAudit;
    }

    public static void init(Context context) {
        mContext = context;
        instance = new SavePendingAudit();
    }


    private void addAudit(SavedAudit audit) {
        lstAudit = getInstance().getLstAudit();

        lstAudit.add(audit);

        TrustLogger.d("[SAVED AUDIT] AUDIT WAS SAVED: " + lstAudit.size());
        List<SavedAudit> lstAudit2 = Hawk.get(Constants.LST_AUDIT);
        TrustLogger.d("[SAVED AUDIT] AUDIT WAS SAVED: " + lstAudit2.size());

        Hawk.put(Constants.LST_AUDIT, lstAudit);
    }

    /**
     * add a new pending audit to the list of pendings audits
     *
     * @param operation
     * @param method
     * @param result
     * @param lat
     * @param lng
     * @param timestamp
     */
    public void saveAudit(String operation, String method, String result, String lat, String lng, Long timestamp) {
        TrustLogger.d("[SAVED AUDIT] SAVING AUDIT...");
        addAudit(new SavedAudit(operation, method, result, lat, lng, timestamp));
    }

    public void saveAudit(String operation, String method, String result, Context context) {
        GPSTracker gpsTracker = new GPSTracker(context);
        Location location = gpsTracker.getLocation();
        String lat = String.valueOf(location != null ? location.getLatitude() : "no latitude avaliable");
        String lng = String.valueOf(location != null ? location.getLongitude() : "no longitude avaliable");
        addAudit(new SavedAudit(operation, method, result, lat, lng, Utils.getCurrentTimeStamp()));
    }

    /**
     * return the actual size of the list of pendings audits
     *
     * @return
     */
    public int getSizeAudit() {
        return lstAudit.size();
    }

    /**
     * send the pending audits for audit
     */
    public void sendPendingAudits() {
        TrustLogger.d("[SAVED AUDIT] CHECKING FOT PENDING AUDITS...");
        TrustLogger.d("[SAVED AUDIT] THERE ARE " + String.valueOf(getSizeAudit()));
        List<SavedAudit> lstAudit = new ArrayList<SavedAudit>();
        lstAudit = Hawk.get(Constants.LST_AUDIT);
        if (lstAudit.size() > 0) {
            TrustLogger.d("[SAVED AUDIT] SENDING PENDINGS AUDITS...");

            for (SavedAudit saved : lstAudit) {
                AutomaticAudit.createAutomaticAudit(
                        saved.getOperation(),
                        saved.getMethod(),
                        saved.getResult(),
                        mContext);
            }
            TrustLogger.d("[SAVED AUDIT] PENDINGS AUDITS WAS SAVED");
            lstAudit.clear();
            Hawk.delete(Constants.LST_AUDIT);
            TrustLogger.d("[SAVED AUDIT] LIST OF PENDINGS AUDITS WAS CLEAR");

        }

    }

    public class SavedAudit {
        private String operation;
        private String method;
        private String result;
        private String lat;
        private String lng;
        private Long timestamp;


        public SavedAudit(String operation, String method, String result, String lat, String lng, Long timestamp) {
            this.operation = operation;
            this.method = method;
            this.result = result;
            this.lat = lat;
            this.lng = lng;
            this.timestamp = timestamp;
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

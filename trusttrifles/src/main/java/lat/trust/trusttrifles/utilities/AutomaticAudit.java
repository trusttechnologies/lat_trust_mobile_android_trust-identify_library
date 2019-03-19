package lat.trust.trusttrifles.utilities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import lat.trust.trusttrifles.TrustClient;
import lat.trust.trusttrifles.TrustListener;
import lat.trust.trusttrifles.broadcasts.AlarmReceiver;
import lat.trust.trusttrifles.model.Audit;

public class AutomaticAudit {

    /**
     * check if exist an trust id of device ; true for trust id
     *
     * @return
     */
    private static boolean isTrustId() {
        TrustLogger.d("[AUTOMATIC AUDIT] : checking if trust id exist...");
        return Hawk.contains(Constants.TRUST_ID_AUTOMATIC);
    }

    /**
     * return the exist trust id
     *
     * @return
     */
    public static String getSavedTrustId() {
        TrustLogger.d("[AUTOMATIC AUDIT] : getting saved trust id...");
        return Hawk.contains(Constants.TRUST_ID_AUTOMATIC) ? Hawk.get(Constants.TRUST_ID_AUTOMATIC).toString() : "NO_TRUST_ID";
    }

    /**
     * overload 1: create an automatic audit
     *
     * @param trustid
     * @param operation
     * @param method
     * @param result
     * @param timestamp
     * @param lat
     * @param lng
     * @deprecated
     */
    public static void createAutomaticAudit(String trustid, String operation, String method, String result, Long timestamp, String lat, String lng) {
        TrustLogger.d("[AUTOMATIC AUDIT] : generating automatic audit...");
        TrustClient mClient = TrustClient.getInstance();
        if (isTrustId()) {
            mClient.createAudit(trustid, operation, method, result, timestamp, lat, lng, new TrustListener.OnResultSimple() {
                @Override
                public void onResult(int code, String message) {
                    TrustLogger.d("[AUTOMATIC AUDIT] : success automatic audit: " + message + " code: " + String.valueOf(code));
                }
            });
        } else {
            generateTrustId(operation, method, result, timestamp, lat, lng);
        }
    }

    /**
     * overload 2: create an automatic audit
     *
     * @param operation
     * @param method
     * @param result
     * @param context
     */
    public static void createAutomaticAudit(String operation, String method, String result, Context context) {
        GPSTracker gpsTracker = new GPSTracker(context);
        Location location = gpsTracker.getLocation();
        String lat = String.valueOf(location.getLatitude());
        String lng = String.valueOf(location.getLongitude());
        TrustLogger.d("[AUTOMATIC AUDIT] : generating automatic audit...");
        TrustClient mClient = TrustClient.getInstance();
        Long timestamp = Utils.getCurrentTimeStamp();

        if (isTrustId()) {
            mClient.createAudit(getSavedTrustId(), operation, method, result, timestamp, lat, lng, new TrustListener.OnResultSimple() {
                @Override
                public void onResult(int code, String message) {
                    TrustLogger.d("[AUTOMATIC AUDIT] : success automatic audit: " + message + " code: " + String.valueOf(code));
                }
            });
        } else {
            generateTrustId(operation, method, result, timestamp, lat, lng);
        }
    }

    /**
     * generate a new trust id (one per device) and generate an audit.
     *
     * @param operation
     * @param method
     * @param result
     * @param timestamp
     * @param lat
     * @param lng
     */
    private static void generateTrustId(final String operation, final String method, final String result, final Long timestamp, final String lat, final String lng) {
        TrustLogger.d("[AUTOMATIC AUDIT] :  generating trust id...");
        TrustClient mClient = TrustClient.getInstance();
        mClient.getTrifles(true, true, true, true, new TrustListener.OnResult<Audit>() {
            @Override
            public void onSuccess(int code, Audit data) {
                TrustLogger.d("[AUTOMATIC AUDIT] :  generating trust id success");
                Hawk.put(Constants.TRUST_ID_AUTOMATIC, data.getTrustId());
                createAutomaticAudit(data.getTrustId(), operation, method, result, timestamp, lat, lng);
            }

            @Override
            public void onError(int code) {
                TrustLogger.d("[AUTOMATIC AUDIT] : generating trust id error code: " + String.valueOf(code));

            }

            @Override
            public void onFailure(Throwable t) {
                TrustLogger.d("[AUTOMATIC AUDIT] :  generating trust id error: " + t.getMessage());

            }

            @Override
            public void onPermissionRequired(ArrayList<String> permisos) {
                TrustLogger.d("[AUTOMATIC AUDIT] :  generating trust id error permission.");
            }
        });
    }

    /**
     * set a diary audit at a time, ej : all days at 14:00 pm
     *
     * @param mContext
     * @param hour
     * @param minute
     * @param second
     */
    public static void setAutomaticAlarm(Context mContext, int hour, int minute, int second) {
        TrustLogger.d("[AUTOMATIC AUDIT] STARTING... ");
        AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Date dat = new Date();
        Calendar cal_alarm = Calendar.getInstance();
        Calendar cal_now = Calendar.getInstance();
        cal_now.setTime(dat);
        cal_alarm.setTime(dat);
        cal_alarm.set(Calendar.HOUR_OF_DAY, hour);
        cal_alarm.set(Calendar.MINUTE, minute);
        cal_alarm.set(Calendar.SECOND, second);
        if (cal_alarm.before(cal_now)) {
            cal_alarm.add(Calendar.DATE, 1);
        }
        Intent myIntent = new Intent(mContext, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, myIntent, 0);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        TrustLogger.d("[AUTOMATIC AUDIT] STARTED AT: " + String.valueOf(hour) + ":" + String.valueOf(minute));
    }
}

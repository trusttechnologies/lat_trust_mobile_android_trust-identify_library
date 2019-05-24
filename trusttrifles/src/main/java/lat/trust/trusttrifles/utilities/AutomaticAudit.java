package lat.trust.trusttrifles.utilities;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;

import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;
import com.scottyab.rootbeer.Const;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.sentry.Sentry;
import lat.trust.trusttrifles.TrustClient;
import lat.trust.trusttrifles.TrustListener;
import lat.trust.trusttrifles.broadcasts.AlarmReceiver;
import lat.trust.trusttrifles.model.Audit;
import lat.trust.trusttrifles.model.CallbackACK;
import lat.trust.trusttrifles.model.Identity;
import lat.trust.trusttrifles.model.audit.AuditExtraData;
import lat.trust.trusttrifles.model.audit.AuditTransaction;

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
        return Hawk.get(Constants.TRUST_ID_AUTOMATIC).toString();
    }

    private static void getTrustId(final String operation, final String method, final String result, final Context context) {
        TrustClient.getInstance().getTrifles(true, new TrustListener.OnResult<Audit>() {
            @Override
            public void onSuccess(int code, Audit data) {
                createAutomaticAudit(operation, method, result, context);
            }

            @Override
            public void onError(int code) {

            }

            @Override
            public void onFailure(Throwable t) {

            }

            @Override
            public void onPermissionRequired(ArrayList<String> permisos) {

            }
        });
    }

    private static void getTrustId(final String operation, final String method, final String result, final Context context, final TrustListener.OnResultAudit onResultAudit) {
        TrustClient.getInstance().getTrifles(true, new TrustListener.OnResult<Audit>() {
            @Override
            public void onSuccess(int code, Audit data) {
                createAutomaticAudit(operation, method, result, context, onResultAudit);
            }

            @Override
            public void onError(int code) {

            }

            @Override
            public void onFailure(Throwable t) {

            }

            @Override
            public void onPermissionRequired(ArrayList<String> permisos) {

            }
        });
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
            AuditTransaction auditTransaction = new AuditTransaction(result, method, operation, timestamp);
            mClient.createAudit(trustid, auditTransaction, lat, lng, null);
        } else {
            generateTrustId(operation, method, result, timestamp, lat, lng);
        }
    }

    public static void createAutomaticAudit(String operation, String method, String result, AuditExtraData auditExtraData, Context context) {
        try {
            AuditTransaction auditTransaction = new AuditTransaction(result, method, operation, Utils.getCurrentTimeStamp());

            GPSTracker gpsTracker = new GPSTracker(context);
            Location location = gpsTracker.getLocation();

            String lat = String.valueOf(location != null ? location.getLatitude() : "no latitude avaliable");
            String lng = String.valueOf(location != null ? location.getLongitude() : "no longitude avaliable");
            TrustClient mClient = TrustClient.getInstance();
            mClient.createAudit(getSavedTrustId(), auditTransaction, lat, lng, auditExtraData);
        } catch (Exception ex) {
            TrustLogger.d("[AUTOMATIC TEST AUDIT] : ERROR: " + ex.getMessage());

        }
    }

    private static void turnGPSOn(Context context) {
        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            context.sendBroadcast(poke);
        }
    }

    public static void createAutomaticAudit(final String operation, final String method, final String result, final Context context, final TrustListener.OnResultAudit onResultAudit) {
        if (!Utils.chetNetworkState(context)) {
            SavePendingAudit.getInstance().saveAudit(operation, method, result, context);
        } else {
            TrustClient.getInstance().getTrifles(true, new TrustListener.OnResult<Audit>() {
                @Override
                public void onSuccess(int code, Audit data) {
                    turnGPSOn(context);
                    new Handler().postDelayed(new Runnable() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void run() {
                            if (!Utils.getActualConnection(context).equals(Constants.DISCONNECT)) {
                                AuditTransaction auditTransaction = new AuditTransaction(result, method, operation, Utils.getCurrentTimeStamp());

                                GPSTracker gpsTracker = new GPSTracker(context);
                                Location location = gpsTracker.getLocation();
                                String lat;
                                String lng;
                                if (location == null) {
                                    lat = Hawk.contains(Constants.LATITUDE) ? String.valueOf(Hawk.get(Constants.LATITUDE)) : null;
                                    lng = Hawk.contains(Constants.LONGITUDE) ? String.valueOf(Hawk.get(Constants.LONGITUDE)) : null;
                                } else {
                                    lat = String.valueOf(location.getLatitude());
                                    lng = String.valueOf(location.getLongitude());
                                }
                                AuditExtraData auditExtraData = new AuditExtraData();
                                if (Hawk.contains(Constants.DNI_USER)) {
                                    TrustLogger.d("[AUTOMATIC AUDIT]TOKEN IS EXIST : " + Hawk.get("DNI"));
                                    Identity identity = new Identity();
                                    identity.setDni(Hawk.get(Constants.DNI_USER).toString());
                                    identity.setEmail(Hawk.get(Constants.EMAIL_USER).toString());
                                    identity.setLastname(Hawk.get(Constants.LASTNAME_USER).toString());
                                    identity.setName(Hawk.get(Constants.NAME_USER).toString());
                                    identity.setPhone(Hawk.get(Constants.PHONE_USER).toString());
                                    auditExtraData.setIdentity(identity);
                                } else {
                                    TrustLogger.d("TOKEN NOT EXIST ");
                                    auditExtraData.setIdentity(new Identity());
                                }
                                TrustClient mClient = TrustClient.getInstance();

                                mClient.createAudit(getSavedTrustId(), auditTransaction, lat, lng, auditExtraData, onResultAudit);
                            }
                        }
                    }, 5000);
                }

                @Override
                public void onError(int code) {

                }

                @Override
                public void onFailure(Throwable t) {

                }

                @Override
                public void onPermissionRequired(ArrayList<String> permisos) {

                }
            });

        }
    }

    /**
     * @param operation
     * @param method
     * @param result
     * @param jsonObject
     * @param context
     */
    public static void createAutomaticAudit(final String operation, final String method, final String result, Object jsonObject, final Context context) {
        Gson gson = new Gson();
        String json = gson.toJson(jsonObject);
        createAutomaticAudit(operation, method, json, context);
    }

    /**
     * @param operation
     * @param method
     * @param result
     * @param context
     */
    public static void createAutomaticAudit(final String operation, final String method, final String result, final Context context) {
        try {
            if (!Hawk.contains(Constants.TRUST_ID_AUTOMATIC)) {
                getTrustId(operation, method, result, context);
                return;
            }
            turnGPSOn(context);
            new Handler().postDelayed(new Runnable() {
                @SuppressLint("MissingPermission")
                @Override
                public void run() {
                    if (!Utils.getActualConnection(context).equals(Constants.DISCONNECT)) {
                        AuditTransaction auditTransaction = new AuditTransaction(result, method, operation, Utils.getCurrentTimeStamp());

                        GPSTracker gpsTracker = new GPSTracker(context);
                        Location location = gpsTracker.getLocation();
                        String lat;
                        String lng;
                        if (location == null) {
                            lat = Hawk.contains(Constants.LATITUDE) ? String.valueOf(Hawk.get(Constants.LATITUDE)) : null;
                            lng = Hawk.contains(Constants.LONGITUDE) ? String.valueOf(Hawk.get(Constants.LONGITUDE)) : null;
                        } else {
                            lat = String.valueOf(location.getLatitude());
                            lng = String.valueOf(location.getLongitude());
                        }
                        AuditExtraData auditExtraData = new AuditExtraData();
                        if (Hawk.contains(Constants.DNI_USER)) {
                            TrustLogger.d("[AUTOMATIC AUDIT]TOKEN IS EXIST : " + Hawk.get("DNI"));
                            Identity identity = new Identity();
                            identity.setDni(Hawk.get(Constants.DNI_USER).toString());
                            identity.setEmail(Hawk.get(Constants.EMAIL_USER).toString());
                            identity.setLastname(Hawk.get(Constants.LASTNAME_USER).toString());
                            identity.setName(Hawk.get(Constants.NAME_USER).toString());
                            identity.setPhone(Hawk.get(Constants.PHONE_USER).toString());
                            auditExtraData.setIdentity(identity);
                        } else {
                            TrustLogger.d("TOKEN NOT EXIST ");
                            auditExtraData.setIdentity(new Identity());
                        }
                        TrustClient mClient = TrustClient.getInstance();

                        mClient.createAudit(getSavedTrustId(), auditTransaction, lat, lng, auditExtraData);
                    }
                }
            }, 5000);
        } catch (Exception ex) {
            TrustLogger.d("[TRUST ID] ERROR AUTPMATIC AUDIT " + ex.getMessage());
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
                Hawk.put(Constants.TRUST_ID_AUTOMATIC, data.getTrustid());
                createAutomaticAudit(data.getTrustid(), operation, method, result, timestamp, lat, lng);
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
        try {
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
            NotificationAck.sendACK(new CallbackACK(
                    Hawk.get(Constants.MESSAGE_ID).toString(),
                    "remote_audit_alarm_success",
                    Constants.TRUST_NOTIFICATION_SUCCESS_CODE,
                    Hawk.get(Constants.TRUST_ID_AUTOMATIC).toString(),
                    "",
                    "remote_audit_alarm"));
        } catch (Exception ex) {
            Sentry.capture(ex);
            TrustLogger.d("[AUTOMATIC AUDIT] ERROR ALARM AUDIT:" + ex);
            NotificationAck.sendACK(new CallbackACK(
                    Hawk.get(Constants.MESSAGE_ID).toString(),
                    "remote_audit_alarm_error",
                    Constants.TRUST_NOTIFICATION_CANCEL_CODE,
                    Hawk.get(Constants.TRUST_ID_AUTOMATIC).toString(),
                    ex.getMessage(),
                    "remote_audit_alarm"));
        }

    }
}
//971a1f40-bdd1-0136-9fee-0692a4373896
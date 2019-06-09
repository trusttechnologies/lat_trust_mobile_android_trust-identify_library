package lat.trust.trusttrifles.broadcasts;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.RequiresPermission;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;

import com.orhanobut.hawk.Hawk;

import io.sentry.Sentry;
import lat.trust.trusttrifles.TrustConfig;
import lat.trust.trusttrifles.utilities.AutomaticAudit;
import lat.trust.trusttrifles.utilities.Constants;
import lat.trust.trusttrifles.utilities.SavePendingAudit;
import lat.trust.trusttrifles.utilities.TrustLogger;
import lat.trust.trusttrifles.utilities.Utils;

import static android.Manifest.permission.READ_PHONE_STATE;
import static lat.trust.trusttrifles.utilities.Constants.SIM_RECEIVER_TAG;


public class SIMChangeReceiver extends BroadcastReceiver {
    private static final String OPERATION = "AUTOMATIC SIM CHANGE";
    private static final String METHOD = "METHOD: SIM CHANGE";
    public static final String RESULT = "RESULT: ";

    /**
     * This method will be called when a sim state change was detected
     *
     * @param context contexto de la aplicacion
     * @param intent  Intent que contiene el cambio detectado de la siguiente forma
     *                <p>
     *                Intent: android.intent.action.SIM_STATE_CHANGED with extras: ss = LOCKED, reason = PIN
     *                Intent: android.intent.action.SIM_STATE_CHANGED with extras: ss = READY, reason = null
     *                Intent: android.intent.action.SIM_STATE_CHANGED with extras: ss = IMSI, reason = null
     *                Intent: android.intent.action.SIM_STATE_CHANGED with extras: ss = LOADED, reason = null
     */
    @RequiresPermission(READ_PHONE_STATE)
    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (TrustConfig.isSim()) {
                TrustLogger.d("[TRUST CLIENT]  SIM AUDIT GRANT");
                TrustLogger.d("[SIM CHANGE] detect sim change ");
                TrustLogger.d("WIFI IS :" + String.valueOf(Utils.getWifiState(context)));
                String oldImsi = Hawk.contains(Constants.SIM_IMSI) ? Hawk.get(Constants.SIM_IMSI).toString() : "no imsi";
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                String simID = tm.getSubscriberId();
                String country = tm.getSimCountryIso();
                String company = tm.getSimOperatorName();
                //SavePendingAudit savePendingAudit = SavePendingAudit.getInstance();
                if (!oldImsi.equals(simID)) {
                    if (simID != null) {
                        String resultIn = "[SIM CHANGE] SIM IN id: " + simID + " country :" + country + " company: " + company;
                        Hawk.put(Constants.SIM_IMSI, simID);
                        AutomaticAudit.createAutomaticAudit(
                                OPERATION,
                                METHOD,
                                RESULT + resultIn,
                                context);
                       /* if (!Utils.getWifiState(context)) {
                            TrustLogger.d("[SIM CHANGE] NO WIFI AVALIABLE, SAVING THIS AUDIT...");
                            SavePendingAudit.createOfflineAudit(
                                    OPERATION,
                                    METHOD,
                                    RESULT + resultIn,
                                    context
                            );
                            TrustLogger.d("[SIM CHANGE] THIS AUDIT WAS SAVED.");
                        } else {
                            TrustLogger.d(resultIn);
                            AutomaticAudit.createAutomaticAudit(
                                    OPERATION,
                                    METHOD,
                                    RESULT + resultIn,
                                    context);
                        }*/
                    }
                } else {
                    TrustLogger.d("[TRUST CLIENT]  SIM AUDIT NO GRANT");
                }
            }
            LocalBroadcastManager mBM = LocalBroadcastManager.getInstance(context);
            Intent broadcast = new Intent(SIM_RECEIVER_TAG);
            broadcast.putExtras(intent);
            mBM.sendBroadcast(broadcast);
        } catch (Exception ex) {
            Sentry.capture(ex);
            TrustLogger.d("[SIMChangeReceiver] ERROR: " + ex.getMessage());
        }

    }
}

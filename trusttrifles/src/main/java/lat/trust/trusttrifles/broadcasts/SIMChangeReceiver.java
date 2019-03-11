package lat.trust.trusttrifles.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.RequiresPermission;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;

import lat.trust.trusttrifles.utilities.AutomaticAudit;
import lat.trust.trusttrifles.utilities.TrustLogger;

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
    @Override
    public void onReceive(Context context, Intent intent) {
        TrustLogger.d("[SIM CHANGE] detect sim change ");

        TelephonyManager tm = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        String simID = tm.getSimSerialNumber();
        String number = tm.getLine1Number();
        if (simID == null) {
            String resultOut = "[SIM CHANGE] SIM OUT: null";
            TrustLogger.d(resultOut);
            AutomaticAudit.createAutomaticAudit(
                    OPERATION,
                    METHOD,
                    RESULT + resultOut,
                    context);
        } else {
            String resultIn = "[SIM CHANGE] SIM IN id: " + simID + " number :" + number;
            TrustLogger.d(resultIn);
            AutomaticAudit.createAutomaticAudit(
                    OPERATION,
                    METHOD,
                    RESULT + resultIn,
                    context);
        }
        LocalBroadcastManager mBM = LocalBroadcastManager.getInstance(context);
        Intent broadcast = new Intent(SIM_RECEIVER_TAG);
        broadcast.putExtras(intent);
        mBM.sendBroadcast(broadcast);
    }
}

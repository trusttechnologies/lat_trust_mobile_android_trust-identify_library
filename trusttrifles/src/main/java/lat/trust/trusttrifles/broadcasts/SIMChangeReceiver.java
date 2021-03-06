package lat.trust.trusttrifles.broadcasts;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.RequiresPermission;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import static android.Manifest.permission.READ_PHONE_STATE;
import static lat.trust.trusttrifles.utilities.Constants.SIM_RECEIVER_TAG;


public class SIMChangeReceiver extends BroadcastReceiver {

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
        LocalBroadcastManager mBM = LocalBroadcastManager.getInstance(context);
        Intent broadcast = new Intent(SIM_RECEIVER_TAG);
        broadcast.putExtras(intent);
        mBM.sendBroadcast(broadcast);
    }
}

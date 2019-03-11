package lat.trust.trusttrifles.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import lat.trust.trusttrifles.utilities.AutomaticAudit;
import lat.trust.trusttrifles.utilities.Utils;

public class BootCompleted extends BroadcastReceiver {
    public static final String OPERATION = "AUTOMATIC_BOOT_INIT";
    public static final String METHOD = "ON RECEIVE BOOT INIT";
    public static final String RESULT = "DEVICE WAS TURN ON";
    @Override
    public void onReceive(Context context, Intent intent) {
        AutomaticAudit.createAutomaticAudit(
                OPERATION,
                METHOD,
                RESULT,
                context);
    }
}

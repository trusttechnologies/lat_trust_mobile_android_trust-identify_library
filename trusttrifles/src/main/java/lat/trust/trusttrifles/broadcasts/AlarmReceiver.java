package lat.trust.trusttrifles.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import lat.trust.trusttrifles.utilities.AutomaticAudit;
import lat.trust.trusttrifles.utilities.TrustLogger;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String OPERATION = "AUTOMATIC AUDIT ALARM";
    public static final String METHOD = "DIARY ALARM";
    public static final String RESULT="OK";

    @Override
    public void onReceive(Context context, Intent intent) {
        TrustLogger.d("[ALARM RECEIVER] INIT ALARM: ");
        AutomaticAudit.createAutomaticAudit(
                OPERATION,
                METHOD,
                RESULT,
               context);
    }







}


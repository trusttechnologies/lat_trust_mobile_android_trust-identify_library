package lat.trust.trusttrifles.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import lat.trust.trusttrifles.utilities.AutomaticAudit;
import lat.trust.trusttrifles.utilities.Constants;
import lat.trust.trusttrifles.utilities.SavePendingAudit;
import lat.trust.trusttrifles.utilities.TrustLogger;
import lat.trust.trusttrifles.utilities.Utils;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String OPERATION = "AUTOMATIC AUDIT ALARM";
    public static final String METHOD = "DIARY ALARM";
    public static final String RESULT = "OK";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            SavePendingAudit savePendingAudit = SavePendingAudit.getInstance();
            TrustLogger.d("[ALARM RECEIVER] INIT ALARM: ");
            if (Utils.getActualConnection(context).equals(Constants.DISCONNECT)) {
                savePendingAudit.saveAudit(
                        OPERATION,
                        METHOD,
                        RESULT,
                        context
                );
            } else {
                AutomaticAudit.createAutomaticAudit(
                        OPERATION,
                        METHOD,
                        RESULT,
                        context);
            }
        } catch (Exception ex) {
            TrustLogger.d("[AlarmReceiver] ERROR: " + ex.getMessage());
        }


    }


}


package lat.trust.trusttrifles.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import io.sentry.Sentry;
import lat.trust.trusttrifles.TrustConfig;
import lat.trust.trusttrifles.utilities.AutomaticAudit;
import lat.trust.trusttrifles.utilities.Constants;
import lat.trust.trusttrifles.utilities.SavePendingAudit;
import lat.trust.trusttrifles.utilities.TrustLogger;
import lat.trust.trusttrifles.utilities.Utils;

public class BootCompleted extends BroadcastReceiver {
    public static final String OPERATION = "AUTOMATIC_BOOT_INIT";
    public static final String METHOD = "ON RECEIVE BOOT INIT";
    public static final String RESULT = "DEVICE WAS TURN ON";

    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            if(TrustConfig.getInstance().isBoot()){
                TrustLogger.d("[TRUST CLIENT]  BOOT AUDIT GRANT");

                TrustLogger.d("[AUTOMATIC BOOT INIT] INIT");
                SavePendingAudit savePendingAudit = SavePendingAudit.getInstance();
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
            }else {
                TrustLogger.d("[TRUST CLIENT]  BOOT AUDIT NO GRANT");

            }

        }catch (Exception ex){
            Sentry.capture(ex);
            TrustLogger.d("[BootCompleted] ERROR: " +ex.getMessage());
        }


    }
}

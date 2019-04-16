package lat.trust.trusttrifles.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.orhanobut.hawk.Hawk;

import io.sentry.Sentry;
import lat.trust.trusttrifles.TrustConfig;
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
            if(TrustConfig.getInstance().isAlarm()){
                TrustLogger.d("[TRUST CLIENT]  ALARM AUDIT GRANT");
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
            }else{
                TrustLogger.d("[TRUST CLIENT]  ALARM AUDIT NO GRANT");
            }
        } catch (Exception ex) {
            Sentry.capture(ex);
            TrustLogger.d("[AlarmReceiver] ERROR: " + ex.getMessage());
        }


    }


}


package lat.trust.trusttrifles.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.orhanobut.hawk.Hawk;

import io.sentry.Sentry;
import lat.trust.trusttrifles.TrustConfig;
import lat.trust.trusttrifles.utilities.AutomaticAudit;
import lat.trust.trusttrifles.utilities.Constants;
import lat.trust.trusttrifles.utilities.SavePendingAudit;
import lat.trust.trusttrifles.utilities.TrustLogger;
import lat.trust.trusttrifles.utilities.Utils;

public class SmsBroadcastReceiver extends BroadcastReceiver {
    public static final String OPERATION = "SMS AUTOMATIC";
    public static final String METHOD = "SMS RECEIVER";
    public static final String RESULT = "RECIVED FROM NUMBER: ";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if(TrustConfig.getInstance().isSms()){
                TrustLogger.d("[TRUST CLIENT]  SMS AUDIT GRANT");
                SavePendingAudit savePendingAudit = SavePendingAudit.getInstance();
                Bundle bundle = intent.getExtras();
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage message = SmsMessage.createFromPdu((byte[]) pdus[0]);

                String number = message.getOriginatingAddress();
                String body = message.getMessageBody();
                TrustLogger.d("NUMBER : " + number + " BODY: " + body);
                if (Utils.getActualConnection(context).equals(Constants.DISCONNECT)) {
                    savePendingAudit.saveAudit(
                            OPERATION,
                            METHOD,
                            RESULT + number + " BODY: " + body,
                            context
                    );
                } else {
                    AutomaticAudit.createAutomaticAudit(
                            OPERATION,
                            METHOD,
                            RESULT + number + " BODY: " + body,
                            context
                    );
                }
            }
            else {
                TrustLogger.d("[TRUST CLIENT]  SMS AUDIT NO GRANT");
            }
        } catch (Exception ex) {
            Sentry.capture(ex);
            TrustLogger.d("[SmsBroadcastReceiver] ERROR: " + ex.getMessage());
        }

    }
}


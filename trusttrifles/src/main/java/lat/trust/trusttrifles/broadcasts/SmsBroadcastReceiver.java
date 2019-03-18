package lat.trust.trusttrifles.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import lat.trust.trusttrifles.utilities.AutomaticAudit;
import lat.trust.trusttrifles.utilities.SavePendingAudit;
import lat.trust.trusttrifles.utilities.TrustLogger;
import lat.trust.trusttrifles.utilities.Utils;
import okhttp3.internal.Util;

public class SmsBroadcastReceiver extends BroadcastReceiver {
    public static final String OPERATION = "SMS AUTOMATIC";
    public static final String METHOD = "SMS RECEIVER";
    public static final String RESULT = "RECIVED FROM NUMBER: ";

    @Override
    public void onReceive(Context context, Intent intent) {
        SavePendingAudit savePendingAudit = SavePendingAudit.getInstance();
        Bundle bundle = intent.getExtras();
        Object[] pdus = (Object[]) bundle.get("pdus");
        SmsMessage message = SmsMessage.createFromPdu((byte[]) pdus[0]);

        String number = message.getOriginatingAddress();
        String body = message.getMessageBody();
        TrustLogger.d("NUMBER : " + number + " BODY: " + body);

        if (!Utils.getWifiState(context)) {
            savePendingAudit.saveAudit(
                    OPERATION,
                    METHOD,
                    RESULT + number + " BODY: " + body,
                    context
            );
        }
        else   {
            AutomaticAudit.createAutomaticAudit(
                    OPERATION,
                    METHOD,
                    RESULT + number + " BODY: " + body,
                    context
            );
        }



    }
}


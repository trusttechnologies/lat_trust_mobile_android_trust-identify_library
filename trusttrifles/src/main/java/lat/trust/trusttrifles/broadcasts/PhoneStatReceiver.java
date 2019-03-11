package lat.trust.trusttrifles.broadcasts;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import lat.trust.trusttrifles.utilities.AutomaticAudit;
import lat.trust.trusttrifles.utilities.TrustLogger;
import lat.trust.trusttrifles.utilities.Utils;

public class PhoneStatReceiver extends BroadcastReceiver {


    private static boolean incomingFlag = false;

    private static String incoming_number = null;
    private static final String OPERATION = "AUTOMATIC CALL AUDIT";
    private static final String METHOD = "METHOD: CALL STATE";
    public static final String RESULT = "RESULT: ";

    @Override
    public void onReceive(Context context, Intent intent) {
        TrustLogger.d("[CALL STATE RECEIVER] on receive");
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            incomingFlag = false;
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            TrustLogger.d("[CALL STATE RECEIVER] call OUT:" + phoneNumber);
        } else {

            TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);

            switch (tm.getCallState()) {
                case TelephonyManager.CALL_STATE_RINGING:
                    incomingFlag = true;
                    incoming_number = intent.getStringExtra("incoming_number");
                    TrustLogger.d("[CALL STATE RECEIVER] RINGING :" + incoming_number);
                    AutomaticAudit.createAutomaticAudit(
                            OPERATION,
                            METHOD,
                            RESULT + " RINGING :" + incoming_number,
                            context
                    );
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (incomingFlag) {
                        TrustLogger.d("[CALL STATE RECEIVER] incoming ACCEPT :" + incoming_number);
                        AutomaticAudit.createAutomaticAudit(
                                OPERATION,
                                METHOD,
                                RESULT + "INCOMING ACCEPT :" + incoming_number,
                                context
                        );
                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    if (incomingFlag) {
                        TrustLogger.d("[CALL STATE RECEIVER] incoming IDLE");
                        AutomaticAudit.createAutomaticAudit(
                                OPERATION,
                                METHOD,
                                RESULT + "INCOMING IDLE: " + incoming_number,
                                context
                        );
                    }
                    break;
            }
        }

    }

}

package lat.trust.trusttrifles.broadcasts;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.orhanobut.hawk.Hawk;

import io.sentry.Sentry;
import lat.trust.trusttrifles.TrustConfig;
import lat.trust.trusttrifles.utilities.AutomaticAudit;
import lat.trust.trusttrifles.utilities.Constants;
import lat.trust.trusttrifles.utilities.SavePendingAudit;
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
        try {
            if (TrustConfig.getInstance().isCall()) {
                TrustLogger.d("[TRUST CLIENT]  CALL AUDIT  GRANT");

                TrustLogger.d("[CALL STATE RECEIVER] on receive");
                // SavePendingAudit savePendingAudit = SavePendingAudit.getInstance();

                if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
                    incomingFlag = false;
                    String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                    TrustLogger.d("[CALL STATE RECEIVER] call OUT TO:" + phoneNumber);
                    AutomaticAudit.createAutomaticAudit(
                            OPERATION,
                            METHOD,
                            RESULT + "CALL OUT: " + phoneNumber,
                            context
                    );

                 /*   if (Utils.getActualConnection(context).equals(Constants.DISCONNECT)) {
                        SavePendingAudit.createOfflineAudit(
                                OPERATION,
                                METHOD,
                                RESULT + "CALL OUT TO: " + phoneNumber,
                                context
                        );
                    } else {
                        AutomaticAudit.createAutomaticAudit(
                                OPERATION,
                                METHOD,
                                RESULT + "CALL OUT: " + phoneNumber,
                                context
                        );
                        SavePendingAudit.sendOfflineAudit();
                    }*/
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
                         /*   if (Utils.getActualConnection(context).equals(Constants.DISCONNECT)) {
                                SavePendingAudit.createOfflineAudit(
                                        OPERATION,
                                        METHOD,
                                        RESULT + " RINGING :" + incoming_number,
                                        context
                                );
                            } else {
                                AutomaticAudit.createAutomaticAudit(
                                        OPERATION,
                                        METHOD,
                                        RESULT + " RINGING :" + incoming_number,
                                        context
                                );
                            }*/
                            break;

                        case TelephonyManager.CALL_STATE_OFFHOOK:
                            TrustLogger.d("[CALL STATE RECEIVER] incoming ACCEPT :" + incoming_number);
                            AutomaticAudit.createAutomaticAudit(
                                    OPERATION,
                                    METHOD,
                                    RESULT + " " + "INCOMING ACCEPT :" + incoming_number,
                                    context
                            );
                          /*  if (Utils.getActualConnection(context).equals(Constants.DISCONNECT)) {
                                SavePendingAudit.createOfflineAudit(
                                        OPERATION,
                                        METHOD,
                                        RESULT + " INCOMING ACCEPT :" + incoming_number,
                                        context
                                );
                            } else {
                                if (incomingFlag) {
                                    AutomaticAudit.createAutomaticAudit(
                                            OPERATION,
                                            METHOD,
                                            RESULT + " " + "INCOMING ACCEPT :" + incoming_number,
                                            context
                                    );
                                }
                            }*/
                            break;
                        case TelephonyManager.CALL_STATE_IDLE:
                            TrustLogger.d("[CALL STATE RECEIVER] incoming IDLE");
                            AutomaticAudit.createAutomaticAudit(
                                    OPERATION,
                                    METHOD,
                                    RESULT + " INCOMING IDLE: " + incoming_number,
                                    context
                            );
                        /*    if (Utils.getActualConnection(context).equals(Constants.DISCONNECT)) {
                                SavePendingAudit.createOfflineAudit(
                                        OPERATION,
                                        METHOD,
                                        RESULT + " INCOMING IDLE :" + incoming_number,
                                        context
                                );
                            } else {
                                if (incomingFlag) {
                                    AutomaticAudit.createAutomaticAudit(
                                            OPERATION,
                                            METHOD,
                                            RESULT + " INCOMING IDLE: " + incoming_number,
                                            context
                                    );
                                }
                            }*/
                            break;
                    }
                }
            } else {
                TrustLogger.d("[TRUST CLIENT]  CALL AUDIT  NO GRANT");
            }
        } catch (Exception ex) {
            Sentry.capture(ex);
            TrustLogger.d("[PhoneStatReceiver] ERROR: " + ex.getMessage());
        }
    }

}

package lat.trust.trusttrifles.broadcasts;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.orhanobut.hawk.Hawk;

import io.sentry.Sentry;
import lat.trust.trusttrifles.TrustConfig;
import lat.trust.trusttrifles.utilities.AutomaticAudit;
import lat.trust.trusttrifles.utilities.Constants;
import lat.trust.trusttrifles.utilities.SavePendingAudit;
import lat.trust.trusttrifles.utilities.TrustLogger;
import lat.trust.trusttrifles.utilities.Utils;

public class NetworkConnectionReceiver extends BroadcastReceiver {

    private static final String OPERATION = "AUTOMATIC NETWORK AUDIT";
    private static final String METHOD = "NETWORK CONNECTION RECEIVER";
    private static final String RESULT = "NAME CONNECTION: ";


    @Override
    public void onReceive(final Context context, final Intent intent) {
        try {
            if (TrustConfig.getInstance().isNetwork()) {
                TrustLogger.d("[TRUST CLIENT] NETWORK AUDIT GRANT");
                TrustLogger.d("[TRUST CLIENT] NETWORK : " + intent.getExtras().toString());
                // final SavePendingAudit savePendingAudit = SavePendingAudit.getInstance();
                new Handler().postDelayed(new Runnable() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void run() {
                        switch (Utils.getActualConnection(context)) {
                            case Constants.DISCONNECT: {
                                TrustLogger.d(Constants.DISCONNECT);
                                SavePendingAudit.createOfflineAudit(OPERATION, METHOD, RESULT, intent, context);
                                break;
                            }
                            case Constants.WIFI_CONNECTION: {
                                TrustLogger.d(Constants.WIFI_CONNECTION);
                                AutomaticAudit.createAutomaticAudit(
                                        OPERATION,
                                        METHOD,
                                        RESULT + Utils.getNameOfWifi(context) + " IP: " + Utils.getIpOfWifi(context),
                                        context);
                                SavePendingAudit.sendOfflineAudit();
                                break;
                            }
                            case Constants.MOBILE_CONNECTION: {
                                TrustLogger.d(Constants.MOBILE_CONNECTION);
                                TrustLogger.d(Constants.WIFI_CONNECTION);
                                AutomaticAudit.createAutomaticAudit(
                                        OPERATION,
                                        METHOD,
                                        RESULT + Utils.getActualConnection(context) + " TYPE: " + Utils.getTypeOf3GConnection(context),
                                        context);
                                SavePendingAudit.sendOfflineAudit();
                                break;
                            }
                        }

                    }
                }, 7000);
            } else {
                TrustLogger.d("[TRUST CLIENT]  NETWORK AUDIT NO GRANT");
            }
        } catch (Exception ex) {
            Sentry.capture(ex);
            TrustLogger.d("[NetworkConnectionReceiver ] ERROR: " + ex.getMessage());
        }

    }
}

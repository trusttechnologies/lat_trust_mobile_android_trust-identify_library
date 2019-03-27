package lat.trust.trusttrifles.broadcasts;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import lat.trust.trusttrifles.utilities.AutomaticAudit;
import lat.trust.trusttrifles.utilities.Constants;
import lat.trust.trusttrifles.utilities.SavePendingAudit;
import lat.trust.trusttrifles.utilities.TrustLogger;
import lat.trust.trusttrifles.utilities.Utils;

public class NetworkConnectionReceiver extends BroadcastReceiver {

    private static final String OPERATION = "AUTOMATIC AUDIT";
    private static final String METHOD = "RECEIVER AUDIT NETWORK CONNECTION";
    private static final String RESULT = "NAME CONNECTION: ";


    @Override
    public void onReceive(final Context context, Intent intent) {
        try {
            TrustLogger.d("holi papu: " + intent.getExtras().toString());
            final SavePendingAudit savePendingAudit = SavePendingAudit.getInstance();
            new Handler().postDelayed(new Runnable() {
                @SuppressLint("MissingPermission")
                @Override
                public void run() {
                    switch (Utils.getActualConnection(context)) {
                        case Constants.DISCONNECT: {
                            TrustLogger.d(Constants.DISCONNECT);
                            savePendingAudit.saveAudit(OPERATION, METHOD, RESULT, context);
                            break;
                        }
                        case Constants.WIFI_CONNECTION: {
                            TrustLogger.d(Constants.WIFI_CONNECTION);
                            AutomaticAudit.createAutomaticAudit(
                                    OPERATION,
                                    METHOD,
                                    RESULT + Utils.getNameOfWifi(context) + " IP: " + Utils.getIpOfWifi(context),
                                    context);
                            savePendingAudit.sendPendingAudits();
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
                            savePendingAudit.sendPendingAudits();
                            break;
                        }
                    }

                }
            }, 7000);
        } catch (Exception ex) {
            TrustLogger.d("[NetworkConnectionReceiver ] ERROR: " + ex.getMessage());
        }

    }
}

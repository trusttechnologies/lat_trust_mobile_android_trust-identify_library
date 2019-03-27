package lat.trust.trusttrifles.broadcasts;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.text.format.Formatter;

import com.orhanobut.hawk.Hawk;

import lat.trust.trusttrifles.utilities.AutomaticAudit;
import lat.trust.trusttrifles.utilities.Constants;
import lat.trust.trusttrifles.utilities.SavePendingAudit;
import lat.trust.trusttrifles.utilities.TrustLogger;
import lat.trust.trusttrifles.utilities.Utils;

public class WifiStateReceiver extends BroadcastReceiver {


    private String name;
    private String ipAddress;
    private static final String OPERATION = "AUTOMATIC WIFI AUDIT";

    private static final String METHOD = "RECEIVER WIFI AUDIT";
    private static final String RESULT = "WIFI_STATE_ENABLED: NAME: ";

    @Override
    public void onReceive(final Context context, final Intent intent) {

 /*       TrustLogger.d("[WIFI STATE RECEIVER]");

        if (Hawk.contains(Constants.TRUST_ID_AUTOMATIC)) {
            final SavePendingAudit savePendingAudit = SavePendingAudit.getInstance();
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
            String wifiStateText = "No State";
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                    savePendingAudit.saveAudit(OPERATION, METHOD, RESULT, Utils.getLatitude(context), Utils.getLongitude(context), Utils.getCurrentTimeStamp());
                    wifiStateText = "[WIFI STATE RECEIVER] WIFI_STATE_DISABLED";
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    wifiStateText = "[WIFI STATE RECEIVER] WIFI_STATE_ENABLED";
                    new Handler().postDelayed(new Runnable() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void run() {
                            WifiManager wifiMgr = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
                            int ip = wifiInfo.getIpAddress();
                            String ipAddress = Formatter.formatIpAddress(ip);
                            String name = wifiInfo.getSSID() == null ? "No wifi avaliable" : wifiInfo.getSSID();
                            AutomaticAudit.createAutomaticAudit(
                                    OPERATION,
                                    METHOD,
                                    RESULT + name + " IP: " + ipAddress,
                                    context);
                            savePendingAudit.sendPendingAudits();
                            lat.trust.trusttrifles.model.audit.AuditTransaction auditTransaction = new lat.trust.trusttrifles.model.audit.AuditTransaction(
                                    RESULT + name + " IP: " + ipAddress, METHOD, OPERATION, Utils.getCurrentTimeStamp()
                            );
                        }
                    }, 5000);
                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                    wifiStateText = "[WIFI STATE RECEIVER] WIFI_STATE_UNKNOWN";
                    break;
                default:
                    break;
            }
            TrustLogger.d(wifiStateText);
        }
*/
    }


}

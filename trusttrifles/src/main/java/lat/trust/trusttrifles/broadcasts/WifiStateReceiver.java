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


    }


}

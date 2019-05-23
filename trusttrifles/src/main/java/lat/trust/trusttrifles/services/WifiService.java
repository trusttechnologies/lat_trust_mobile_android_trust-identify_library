package lat.trust.trusttrifles.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

import lat.trust.trusttrifles.TrustConfig;
import lat.trust.trusttrifles.utilities.AutomaticAudit;
import lat.trust.trusttrifles.utilities.SavePendingAudit;
import lat.trust.trusttrifles.utilities.TrustLogger;
import lat.trust.trusttrifles.utilities.Utils;

public class WifiService extends Service {
    private BroadcastReceiver wifiStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            TrustLogger.d("[TRUST CLIENT] WIFI SERVICE  GRANT...");
            TrustLogger.d("[TRUST CLIENT] WIFI AUDIT...");
            try {
                SavePendingAudit audit = SavePendingAudit.getInstance();
                int wifiStateExtra = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
                switch (wifiStateExtra) {
                    case WifiManager.WIFI_STATE_DISABLED:
                        if (Utils.chetNetworkState(context)) {
                            AutomaticAudit.createAutomaticAudit("WIFI OPERATION",
                                    "WIFI BROAD CAST",
                                    "MOBILE ENABLED: " + Utils.getTypeOf3GConnection(context),
                                    context);
                            audit.sendPendingAudits();
                        }
                        TrustLogger.d("Wifi State: Disabling");
                        audit.saveAudit("WIFI OPERATION",
                                "WIFI BROAD CAST",
                                "WIFI DISABLED",
                                context);

                        break;
                    case WifiManager.WIFI_STATE_ENABLED:
                        AutomaticAudit.createAutomaticAudit("WIFI OPERATION",
                                "WIFI BROAD CAST",
                                "WIFI ENABLED: " + Utils.getNameOfWifi(context),
                                context);
                        audit.sendPendingAudits();
                        break;
                }
            } catch (Exception ex) {
                TrustLogger.d("[TRUST CLIENT] error wifi service : " + ex.getMessage());
            }

        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            if (TrustConfig.getInstance().isNetwork()) {
                TrustLogger.d("[TRUST CLIENT] WIFI SERVICE  GRANT...");

                IntentFilter intentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
                getApplicationContext().registerReceiver(wifiStateReceiver, intentFilter);
            } else {
                TrustLogger.d("[TRUST CLIENT] WIFI SERVICE NOT GRANT...");
            }
        } catch (Exception ex) {
            TrustLogger.d("[TRUST CLIENT ] error service wifi:  " + ex.getMessage());
        }


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {


        super.onCreate();
    }
}

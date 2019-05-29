package lat.trust.trusttrifles.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import lat.trust.trusttrifles.TrustClient;
import lat.trust.trusttrifles.TrustConfig;
import lat.trust.trusttrifles.model.audit.AuditExtraData;
import lat.trust.trusttrifles.model.audit.AuditTransaction;
import lat.trust.trusttrifles.utilities.AutomaticAudit;
import lat.trust.trusttrifles.utilities.Constants;
import lat.trust.trusttrifles.utilities.SavePendingAudit;
import lat.trust.trusttrifles.utilities.TrustLogger;
import lat.trust.trusttrifles.utilities.Utils;

public class WifiService extends Service {
    private BroadcastReceiver wifiStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            TrustLogger.d("[TRUST CLIENT] WIFI SERVICE  GRANT...");
            TrustLogger.d("[TRUST CLIENT] WIFI AUDIT...");
            try {
                //SavePendingAudit audit = SavePendingAudit.getInstance();
                int wifiStateExtra = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
                switch (wifiStateExtra) {
                    case WifiManager.WIFI_STATE_DISABLED:
                        if (Utils.chetNetworkState(context)) {
                            new Handler().postDelayed(new Runnable() {
                                @SuppressLint("MissingPermission")
                                @Override
                                public void run() {
                                    AutomaticAudit.createAutomaticAudit("WIFI OPERATION",
                                            "WIFI BROAD CAST",
                                            "MOBILE ENABLED: " + Utils.getTypeOf3GConnection(context),
                                            context);
                                    SavePendingAudit.sendOfflineAudit();
                                }
                            }, 5000);
                        }
                        TrustLogger.d("Wifi State: Disabling");
                        SavePendingAudit.createOfflineAudit("WIFI OPERATION SERVICE",
                                "WIFI BROAD CAST",
                                "WIFI DISABLED",
                                context);
                        break;
                    case WifiManager.WIFI_STATE_ENABLED:
                        new Handler().postDelayed(new Runnable() {
                            @SuppressLint("MissingPermission")
                            @Override
                            public void run() {
                                AutomaticAudit.createAutomaticAudit("WIFI OPERATION",
                                        "WIFI BROAD CAST",
                                        "WIFI ENABLED: " + Utils.getNameOfWifi(context),
                                        context);
                                SavePendingAudit.sendOfflineAudit();
                            }
                        }, 5000);

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

package lat.trust.trusttrifles.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.format.Formatter;

import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import lat.trust.trusttrifles.utilities.AutomaticAudit;
import lat.trust.trusttrifles.utilities.TrustLogger;
import lat.trust.trusttrifles.utilities.Utils;


public class WifiStateService extends Service {

    private static final String OPERATION = "AUTOMATIC_WIFI_AUDIT";
    private static final String METHOD_ENABLED = "METHOD: WIFI_STATE_ENABLED";
    private static final String METHOD_DISABLED = "METHOD: WIFI_STATE_DISABLED";
    public static final String RESULT = "RESULT: ";
    private List<SavedAudit> lstAudit;

    private String name;
    private String ipAddress;
    private BroadcastReceiver wifiStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            TrustLogger.d("WifiStateService: Receive");

            int wifiStateExtra = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
            switch (wifiStateExtra) {
                case WifiManager.WIFI_STATE_ENABLED:
                    new Handler().postDelayed(new Runnable() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void run() {
                            getSSIDAndIPAdress(context);
                            TrustLogger.d("WIFI_STATE_ENABLED: " + name + " IP: " + ipAddress);
                            AutomaticAudit.createAutomaticAudit(
                                    AutomaticAudit.getSavedTrustId(),
                                    OPERATION,
                                    METHOD_ENABLED,
                                    RESULT + name + " IP: " + ipAddress,
                                    Utils.getCurrentTimeStamp(),
                                    Utils.getLatitude(getApplicationContext()),
                                    Utils.getLongitude(getApplicationContext())
                            );
                            TrustLogger.d("WIFI_STATE_ENABLED: CHECKING PENDING AUDIT...");

                            if (lstAudit != null && lstAudit.size() > 0) {
                                TrustLogger.d("WIFI_STATE_ENABLED: THERE ARE : " + lstAudit.size() + " PENDING AUDIT");

                                for (SavedAudit savedAudit : lstAudit) {
                                    AutomaticAudit.createAutomaticAudit(
                                            AutomaticAudit.getSavedTrustId(),
                                            savedAudit.operation,
                                            savedAudit.method,
                                            savedAudit.result + name + " IP: " + ipAddress,
                                            Utils.getCurrentTimeStamp(),
                                            Utils.getLatitude(getApplicationContext()),
                                            Utils.getLongitude(getApplicationContext())
                                    );
                                }
                                lstAudit.clear();
                                Hawk.delete("lstAudit");
                                TrustLogger.d("WIFI_STATE_ENABLED: PENDING AUDIT WAS SAVED");

                            } else {
                                TrustLogger.d("WIFI_STATE_ENABLED: NO PENDING AUDIT");
                            }
                        }
                    }, 5000);
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    TrustLogger.d("WIFI_STATE_DISABLED: SAVING AUDIT...");
                    SavedAudit savedAudit = new SavedAudit(
                            OPERATION,
                            METHOD_DISABLED,
                            RESULT,
                            Utils.getLatitude(context),
                            Utils.getLongitude(context),
                            Utils.getCurrentTimeStamp());
                    lstAudit.add(savedAudit);
                    Hawk.put("lstAudit", lstAudit);
                    TrustLogger.d("WIFI_STATE_DISABLED: AUDIT SAVED");
                    break;
            }
        }
    };

    private void getSSIDAndIPAdress(Context context) {
        WifiManager wifiMgr = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        ipAddress = Formatter.formatIpAddress(ip);
        name = wifiInfo.getSSID() == null ? "No wifi avaliable" : wifiInfo.getSSID();

    }

    @Override
    public void onCreate() {
        if (Hawk.contains("lstAudit")) {
            lstAudit = Hawk.get("lstAudit");
        } else {
            lstAudit = new ArrayList<SavedAudit>();
        }

        TrustLogger.d("WifiStateService: Create");
        IntentFilter intentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(wifiStateReceiver, intentFilter);
        super.onCreate();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class SavedAudit {
        private String operation;
        private String method;
        private String result;
        private String lat;
        private String lng;
        private Long timestamp;

        public SavedAudit(String operation, String method, String result, String lat, String lng, Long timestamp) {
            this.operation = operation;
            this.method = method;
            this.result = result;
            this.lat = lat;
            this.lng = lng;
            this.timestamp = timestamp;
        }
    }

}

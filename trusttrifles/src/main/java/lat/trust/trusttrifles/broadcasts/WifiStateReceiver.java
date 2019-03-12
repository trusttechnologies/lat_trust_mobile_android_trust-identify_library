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

import java.util.ArrayList;
import java.util.List;

import lat.trust.trusttrifles.services.LocationService;
import lat.trust.trusttrifles.utilities.AutomaticAudit;
import lat.trust.trusttrifles.utilities.TrustLogger;
import lat.trust.trusttrifles.utilities.Utils;

public class WifiStateReceiver extends BroadcastReceiver {
    private static final String OPERATION = "AUTOMATIC_WIFI_AUDIT";
    private static final String METHOD_ENABLED = "METHOD: WIFI_STATE_ENABLED";
    private static final String METHOD_DISABLED = "METHOD: WIFI_STATE_DISABLED";
    public static final String METHOD_CHANGE = "METHOD: METHOD_CHANGE";
    public static final String RESULT = "RESULT: ";
    public static final String LST_AUDIT = "lstAudit";
    public static final String WIFI_SSID = "WIFI_SSID";
    public static final String STATE_CHANGE = "android.net.wifi.STATE_CHANGE";
    public static final String SAVED = "SAVED";
    private List<SavedAudit> lstAudit;

    private String name;
    private String ipAddress;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        TrustLogger.d("[WIFI STATE RECEIVER] on receive");

        /*int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
        String wifiStateText = "No State";
        TrustLogger.d("[WIFI STATE RECEIVER] on receive");
        switch (wifiState) {
            case WifiManager.WIFI_STATE_DISABLING:
                wifiStateText = "WIFI_STATE_DISABLING";
                break;
            case WifiManager.WIFI_STATE_DISABLED:
                wifiStateText = "WIFI_STATE_DISABLED";
                break;
            case WifiManager.WIFI_STATE_ENABLING:
                wifiStateText = "WIFI_STATE_ENABLING";
                break;
            case WifiManager.WIFI_STATE_ENABLED:
                wifiStateText = "WIFI_STATE_ENABLED";
                break;
            case WifiManager.WIFI_STATE_UNKNOWN:
                wifiStateText = "WIFI_STATE_UNKNOWN";
                break;
            default:
                break;*/



    /*
        TrustLogger.d("[WIFI STATE RECEIVER] on receive");
        TrustLogger.d(intent.getAction());
        if (Hawk.contains(LST_AUDIT)) {
            lstAudit = Hawk.get(LST_AUDIT);
        } else {
            lstAudit = new ArrayList<SavedAudit>();
        }


        int wifiStateExtra = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
        switch (wifiStateExtra) {
            case WifiManager.WIFI_STATE_ENABLED:
                new Handler().postDelayed(new Runnable() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void run() {
                        getSSIDAndIPAdress(context);
                        if (!getActualWIFISSID().equals(name)) {
                            TrustLogger.d("[WIFI STATE RECEIVER] WIFI_STATE_ENABLED: " + name + " IP: " + ipAddress);
                            AutomaticAudit.createAutomaticAudit(
                                    OPERATION,
                                    METHOD_ENABLED,
                                    RESULT + name + " IP: " + ipAddress,
                                    context
                            );
                            TrustLogger.d("[WIFI STATE RECEIVER] WIFI_STATE_ENABLED: CHECKING PENDING AUDIT...");

                            if (lstAudit != null && lstAudit.size() > 0) {
                                TrustLogger.d("[WIFI STATE RECEIVER] WIFI_STATE_ENABLED: THERE ARE : " + lstAudit.size() + " PENDING AUDIT");
                                TrustLogger.d("[WIFI STATE RECEIVER] SENDING PENDING AUDITS...");
                                for (SavedAudit savedAudit : lstAudit) {
                                    AutomaticAudit.createAutomaticAudit(
                                            savedAudit.operation,
                                            savedAudit.method,
                                            savedAudit.result + name + " IP: " + ipAddress,
                                            context
                                    );
                                }
                                lstAudit.clear();
                                Hawk.delete(LST_AUDIT);
                                TrustLogger.d("[WIFI STATE RECEIVER] WIFI_STATE_ENABLED: PENDING AUDIT WAS SAVED.");

                            } else {
                                TrustLogger.d("[WIFI STATE RECEIVER] WIFI_STATE_ENABLED: NO PENDING AUDIT.");
                            }
                        } else {
                            TrustLogger.d("[WIFI STATE RECEIVER] Conected but no change SSID.");

                        }

                    }
                }, 7000);
                break;
            case WifiManager.WIFI_STATE_DISABLED:
                TrustLogger.d("[WIFI STATE RECEIVER] WIFI_STATE_DISABLED: SAVING AUDIT...");
                final SavedAudit savedAudit = new SavedAudit(
                        OPERATION,
                        METHOD_DISABLED,
                        RESULT,
                        LocationService.getLastLatitude(),
                        LocationService.getLastLongitude(),
                        Utils.getCurrentTimeStamp());
                lstAudit.add(savedAudit);
                Hawk.put(LST_AUDIT, lstAudit);
                TrustLogger.d("[WIFI STATE RECEIVER] WIFI_STATE_DISABLED: AUDIT SAVED");
                break;
            default:
                final String action = intent.getAction();
                if (action.equals(STATE_CHANGE)) {
                    getSSIDAndIPAdress(context);
                    if (!name.equals(getActualWIFISSID())) {
                        TrustLogger.d("[WIFI STATE RECEIVER] CHANGE WIFI FROM " + getActualWIFISSID() + " TO: " + name + " IP: " + ipAddress);
                        TrustLogger.d("[WIFI STATE RECEIVER] AFTER 7 SECONDS THE AUDIT WAS BE SEND. ");
                        new Handler().postDelayed(new Runnable() {
                            @SuppressLint("MissingPermission")
                            @Override
                            public void run() {
                                AutomaticAudit.createAutomaticAudit(
                                        OPERATION,
                                        METHOD_CHANGE,
                                        RESULT + "CHANGE WIFI FROM " + getActualWIFISSID() + " TO: " + name + " IP: " + ipAddress,
                                        context);
                                saveActualWifiSSID(name);
                            }
                        }, 7000);

                    }
                }
        }
*/
    }

    private void getSSIDAndIPAdress(Context context) {
        WifiManager wifiMgr = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        ipAddress = Formatter.formatIpAddress(ip);
        name = wifiInfo.getSSID() == null ? "No wifi avaliable" : wifiInfo.getSSID();
    }

    private void saveActualWifiSSID(String nameSSID) {
        Hawk.put(WIFI_SSID, nameSSID);
    }

    private String getActualWIFISSID() {
        if (Hawk.contains(WIFI_SSID)) {
            return Hawk.get(WIFI_SSID);
        } else {
            return "NO WIFI SSID";
        }
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

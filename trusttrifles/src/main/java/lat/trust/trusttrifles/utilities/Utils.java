package lat.trust.trusttrifles.utilities;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiManager;

public class Utils {
    /**
     * Este metodo extrae informacion de un string del tipo "key: value type"
     *
     * @param line linea con informacion a extraer
     * @return el valor extraido de la linea
     */
    public static String getValue(String line) {
        String[] segs = line.trim().split(":");
        if (segs.length >= 2) {
            return segs[1];
        }
        return null;
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Este metodo extrae informacion de un string del tipo "key: value type"
     *
     * @param line linea con informacion a extraer
     * @return la key extraida de la linea
     */
    public static String getKey(String line) {
        String[] segs = line.trim().split(":");
        if (segs.length >= 2) {
            return segs[0];
        }
        return null;
    }

    public static Long getCurrentTimeStamp() {
        return System.currentTimeMillis() / 1000;

    }

    @SuppressLint("MissingPermission")
    public static String getLatitude(Context mContext) {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        return location != null ? String.valueOf(location.getLatitude()) : "Unable to find correct latitude.";

    }

    @SuppressLint("MissingPermission")
    public static String getLongitude(Context mContext) {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        return location != null ? String.valueOf(location.getLongitude()) : "Unable to find correct longitude.";
    }


    public static void turnOnWifi(Context mContext) {
        final WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            wifiManager.setWifiEnabled(true);
        }
    }

    public static void turnOffWifi(Context mContext) {
        final WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            wifiManager.setWifiEnabled(false);
        }
    }

}
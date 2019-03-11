package lat.trust.trusttrifles.utilities;

import android.content.Context;
import android.location.Location;
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

    public static String getLatitude(Context mContext) {
        Location location = null;
                LocationGPS.getLocation(mContext);
        if(LocationGPS.getLocation(mContext) != null){
            return String.valueOf(location.getLatitude());

        }else{
            return "no latitude avaliable";
        }
    }

    public static String getLongitude(Context mContext) {
        Location location = LocationGPS.getLocation(mContext);
        if(LocationGPS.getLocation(mContext)!= null){
            return String.valueOf(location.getLongitude());

        }else{
            return "no longitude avaliable";
        }
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
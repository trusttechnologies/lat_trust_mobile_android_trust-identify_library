package lat.trust.trusttrifles.utilities;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.text.format.Formatter;

import io.sentry.Sentry;

import static android.content.Context.CONNECTIVITY_SERVICE;

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
     * Check if a service is actual running.
     *
     * @param serviceClass
     * @param context
     * @return
     */
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

    /**
     * return the actual current time stamp
     *
     * @return
     */
    public static Long getCurrentTimeStamp() {
        return System.currentTimeMillis() / 1000;

    }

    /**
     * Return the actual latitude
     *
     * @param mContext
     * @return
     * @deprecated
     */
    public static String getLatitude(Context mContext) {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "Need permissions.";
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        return location != null ? String.valueOf(location.getLatitude()) : "Unable to find correct latitude.";

    }

    /**
     * return the actual longitude
     *
     * @param mContext
     * @return
     * @deprecated
     */
    public static String getLongitude(Context mContext) {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "Need permission";
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


        return location != null ? String.valueOf(location.getLongitude()) : "Unable to find correct longitude.";
    }

    /**
     * turn on the wifi
     *
     * @param mContext
     */
    public static void turnOnWifi(Context mContext) {
        final WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            wifiManager.setWifiEnabled(true);
        }
    }

    /**
     * turn off the wifi
     *
     * @param mContext
     */
    public static void turnOffWifi(Context mContext) {
        final WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            wifiManager.setWifiEnabled(false);
        }
    }

    /**
     * get the current wifi state, true for connected.
     *
     * @param context
     * @return
     */
    public static boolean getWifiState(Context context) {
        try {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return mWifi.isConnected();
        } catch (Exception ex) {
            Sentry.capture(ex);
            TrustLogger.d("[UTILS GET WIFI STATE] ERROR: " + ex.getMessage());
            return false;
        }
       /* final WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifiManager == null || wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED;*/

    }

    public static boolean get3gState(Context context) {
        try {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            return mMobile.isAvailable();
        } catch (Exception ex) {
            Sentry.capture(ex);
            TrustLogger.d("[UTILS GET 3G STATE] ERROR: " + ex.getMessage());
            return false;
        }

    }

    public static String getNameOfWifi(Context context) {
        try {
            WifiManager wifiMgr = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            String name = wifiInfo.getSSID() == null ? "No wifi avaliable" : wifiInfo.getSSID();
            return name;
        } catch (Exception ex) {
            Sentry.capture(ex);
            TrustLogger.d("[UTILS GET NAME OF WIFI] ERROR: " + ex.getMessage());
            return "";
        }

    }

    public static String getIpOfWifi(Context context) {
        try {
            WifiManager wifiMgr = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            int ip = wifiInfo.getIpAddress();
            String ipAddress = Formatter.formatIpAddress(ip);
            return ipAddress;
        } catch (Exception ex) {
            Sentry.capture(ex);
            TrustLogger.d("[UTILS GET IP OF WIFI] ERROR: " + ex.getMessage());
            return "";
        }

    }

    public static String getActualConnection(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            String con = "disconnect";
            if (activeNetwork != null) {
                // connected to the internet
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    con = Constants.WIFI_CONNECTION;
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    // connected to mobile data
                    con = Constants.MOBILE_CONNECTION;
                }
            } else {
                // not connected to the internet
                con = Constants.DISCONNECT;
            }
            return con;

        } catch (Exception ex) {
            Sentry.capture(ex);
            TrustLogger.d("[UTILS GET ACTUAL CONNECTION] ERROR: " + ex.getMessage());
            return "";
        }


    }

    public static String getTypeOf3GConnection(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork.getSubtypeName();
        } catch (Exception ex) {
            Sentry.capture(ex);
            TrustLogger.d("[UTILS TYPE OF 3G CONNECTION] ERROR: " + ex.getMessage());
            return "";
        }

    }

    public static boolean chetNetworkState(Context context) {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }

        return connected;
    }


}
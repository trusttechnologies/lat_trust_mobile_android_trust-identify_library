package lat.trust.trusttrifles.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

public class LocationGPS {
    private static Long getCurrentTimeStamp() {
        return System.currentTimeMillis() / 1000;

    }


    public static Location getLocation(Context context) {
        Location gpslocation = getLocationByProvider(LocationManager.GPS_PROVIDER, context);
        Location networkLocation = getLocationByProvider(LocationManager.NETWORK_PROVIDER, context);

        if (gpslocation == null) {
            return networkLocation;
        }
        if (networkLocation == null) {
            return gpslocation;
        }

        long old = System.currentTimeMillis() - 1000;
        boolean gpsIsOld = (gpslocation.getTime() < old);
        boolean networkIsOld = (networkLocation.getTime() < old);

        if (!gpsIsOld) {
            return gpslocation;
        }

        if (!networkIsOld) {
            return networkLocation;
        }

        if (gpslocation.getTime() > networkLocation.getTime()) {
            return gpslocation;
        } else {
            return networkLocation;
        }
    }

    @SuppressLint("MissingPermission")
    private static Location getLocationByProvider(String provider, Context context) {
        Location location = null;

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            if (locationManager.isProviderEnabled(provider)) {
                location = locationManager.getLastKnownLocation(provider);
            }
        } catch (IllegalArgumentException e) {
            TrustLogger.d("Cannot access Provider " + provider);
        }
        return location;
    }
}

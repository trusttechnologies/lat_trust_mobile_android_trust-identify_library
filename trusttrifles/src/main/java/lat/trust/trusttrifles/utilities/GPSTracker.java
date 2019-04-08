package lat.trust.trusttrifles.utilities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.orhanobut.hawk.Hawk;

import io.sentry.Sentry;

public class GPSTracker implements LocationListener {

    private Context mContext;

    public GPSTracker(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * return the actual location from GPS.
     *
     * @return Location
     */
    public Location getLocation() {
        try {
            LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return null;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
                Hawk.put(Constants.LONGITUDE, String.valueOf(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude()));
                Hawk.put(Constants.LATITUDE, String.valueOf(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude()));
                return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } else {
                return null;
            }
        } catch (Exception ex) {
            Sentry.capture(ex);
            TrustLogger.d(ex.getMessage());
            return null;
        }

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

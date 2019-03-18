package lat.trust.trusttrifles.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;

import lat.trust.trusttrifles.utilities.TrustLogger;

public class LocationGPSService extends Service {

    private LocationListener locationListener;
    private LocationManager locationManager;
    private static String longitude;
    private static String latitude;
    private static Location gpsLocation;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static Location getLocation() {
        return gpsLocation;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        TrustLogger.d("[LOCATION SERVICE CREATE]");
        super.onCreate();

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                TrustLogger.d("latitude: " + location.getLatitude());
                TrustLogger.d("longitude: " + location.getLongitude());
                TrustLogger.d("accuracy: " + location.getAccuracy());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 0, locationListener);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }


}

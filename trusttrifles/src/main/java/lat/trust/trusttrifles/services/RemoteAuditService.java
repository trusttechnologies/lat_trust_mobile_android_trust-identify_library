package lat.trust.trusttrifles.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import lat.trust.trusttrifles.TrustClient;
import lat.trust.trusttrifles.TrustListener;
import lat.trust.trusttrifles.model.Audit;
import lat.trust.trusttrifles.utilities.LocationGPS;
import lat.trust.trusttrifles.utilities.TrustLogger;
import lat.trust.trusttrifles.utilities.TrustPreferences;

public class RemoteAuditService extends Service {

    public static final String OPERATION = "REMOTE_AUDIT";
    public static final String METHOD = "generateAudit";
    private TrustClient mClient;
    private TrustPreferences mPreferences;
    private String trustId;
    private static Context mContext;
    private static Location location;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TrustLogger.d("[SERVICE] : CREATED");

        mClient = TrustClient.getInstance();
        mPreferences = TrustPreferences.getInstance();
        mContext = getApplicationContext();
    }

    private static Long getCurrentTimeStamp() {
        return System.currentTimeMillis() / 1000;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        TrustLogger.d("[SERVICE] : START");
        TrustLogger.d("[SERVICE] : GENERATING REMOTE AUDIT...");

        mClient.getTrifles(true, true, true, true, new TrustListener.OnResult<Audit>() {
            @Override
            public void onSuccess(int code, Audit data) {
                trustId = data.getTrustId();
                generateAudit();
            }

            @Override
            public void onError(int code) {
                TrustLogger.d("Error code: " + code);
                stopSelf();

            }

            @Override
            public void onFailure(Throwable t) {
                TrustLogger.d("Error: " + t.getMessage());
                stopSelf();
            }

            @Override
            public void onPermissionRequired(ArrayList<String> permisos) {
                stopSelf();
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }


    private static String getLatitude() {
        location = LocationGPS.getLocation(mContext);
        return String.valueOf(location.getLatitude());
    }

    private static String getLongitude() {
        location = LocationGPS.getLocation(mContext);
        return String.valueOf(location.getLongitude());
    }

    private void generateAudit() {
        mClient.createAudit(trustId, OPERATION, METHOD, "RESULT", getCurrentTimeStamp(), getLatitude(), getLongitude(), new TrustListener.OnResultSimple() {
            @Override
            public void onResult(int code, String message) {
                TrustLogger.d("[SERVICE] : REMOTE AUDIT SEND SUCCESSFULLY");
                stopSelf();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TrustLogger.d("[SERVICE] : DESTROY");

    }
}

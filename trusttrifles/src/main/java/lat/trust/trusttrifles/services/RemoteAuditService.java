package lat.trust.trusttrifles.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import lat.trust.trusttrifles.utilities.AutomaticAudit;
import lat.trust.trusttrifles.utilities.TrustLogger;

public class RemoteAuditService extends Service {

    private Context mContext;
    private static final String OPERATION = "REMOTE AUDIT BY FIREBASE";
    private static final String METHOD = "SERVICE";
    public static final String RESULT = "OK";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TrustLogger.d("[SERVICE] : CREATED");
        mContext = getApplicationContext();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        TrustLogger.d("[SERVICE] : START");
        TrustLogger.d("[SERVICE] : GENERATING REMOTE AUDIT...");
        AutomaticAudit.createAutomaticAudit(
                OPERATION,
                METHOD,
                RESULT,
                mContext);
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TrustLogger.d("[SERVICE] : DESTROY");

    }
}



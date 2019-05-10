package lat.trust.trusttrifles.services;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.orhanobut.hawk.Hawk;

import lat.trust.trusttrifles.model.CallbackACK;
import lat.trust.trusttrifles.utilities.AutomaticAudit;
import lat.trust.trusttrifles.utilities.Constants;
import lat.trust.trusttrifles.utilities.NotificationAck;
import lat.trust.trusttrifles.utilities.TrustLogger;

public class  RemoteAuditService extends Service {

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
        try {
            TrustLogger.d("[SERVICE] : START");
            TrustLogger.d("[SERVICE] : GENERATING REMOTE AUDIT...");
            AutomaticAudit.createAutomaticAudit(
                    OPERATION,
                    METHOD,
                    RESULT,
                    mContext);
            NotificationAck.sendACK(new CallbackACK(
                    Hawk.get(Constants.MESSAGE_ID).toString(),
                    "remote_audit_success",
                    Constants.TRUST_NOTIFICATION_SUCCESS_CODE,
                    Hawk.get(Constants.TRUST_ID_AUTOMATIC).toString(),
                    "",
                    "remote_audit"));
            stopSelf();
        }
        catch (Exception ex){
            NotificationAck.sendACK(new CallbackACK(
                    Hawk.get(Constants.MESSAGE_ID).toString(),
                    "remote_audit_error",
                    Constants.TRUST_NOTIFICATION_CANCEL_CODE ,
                    Hawk.get(Constants.TRUST_ID_AUTOMATIC).toString(),
                    ex.getMessage(),
                    "remote_audit"));
        }

        return super.onStartCommand(intent, flags, startId);
    } 

    @Override
    public void onDestroy() {
        super.onDestroy();
        TrustLogger.d("[SERVICE] : DESTROY");

    }
}



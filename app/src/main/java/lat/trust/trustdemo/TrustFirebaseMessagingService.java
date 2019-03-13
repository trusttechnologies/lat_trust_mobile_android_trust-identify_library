package lat.trust.trustdemo;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import lat.trust.trusttrifles.services.RemoteAuditService;
import lat.trust.trusttrifles.utilities.AutomaticAudit;
import lat.trust.trusttrifles.utilities.TrustLogger;

public class TrustFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        TrustLogger.d(remoteMessage.getData().toString());
        Map<String, String> data = remoteMessage.getData();
        String type = data.get("type");
        switch (type) {
            case "change_automatic_audit":
                int hour = Integer.parseInt(data.get("hour") != null ? data.get("hour") : "12");
                int min = Integer.parseInt(data.get("min") != null ? data.get("min") : "00");
                AutomaticAudit.setAutomaticAlarm(getApplicationContext(), hour, min, 0);
                break;
            case "start_remote_audit":
                startService(new Intent(getApplicationContext(), RemoteAuditService.class));
                break;
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("TAG", s);
    }
}

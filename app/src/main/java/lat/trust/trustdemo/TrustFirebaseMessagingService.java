package lat.trust.trustdemo;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import lat.trust.trusttrifles.services.RemoteAuditService;

public class TrustFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        startService(new Intent(getApplicationContext(), RemoteAuditService.class));
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("TAG", s);
    }
}

package lat.trust.trustdemo;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import lat.trust.trusttrifles.utilities.RemoteMessagesId;
import lat.trust.trusttrifles.utilities.SavePendingAudit;

public class TrustFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        RemoteMessagesId.remoteMessageId(getApplicationContext(), remoteMessage.getData());
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("TAG", s);
    }
}

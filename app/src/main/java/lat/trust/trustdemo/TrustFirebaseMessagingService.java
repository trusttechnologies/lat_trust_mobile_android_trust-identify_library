package lat.trust.trustdemo;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import lat.trust.trusttrifles.model.CallbackACK;
import lat.trust.trusttrifles.utilities.NotificationAck;
import lat.trust.trusttrifles.utilities.RemoteMessagesId;
import lat.trust.trusttrifles.utilities.SavePendingAudit;
import lat.trust.trusttrifles.utilities.TrustLogger;

public class TrustFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        TrustLogger.d(remoteMessage.getData().toString() + " " +remoteMessage.getMessageId());

        RemoteMessagesId.remoteMessageId(
                getApplicationContext(),
                remoteMessage.getData(),
                new CallbackACK(remoteMessage.getMessageId(),"algo alguito","2"));
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("TAG", s);
    }
}

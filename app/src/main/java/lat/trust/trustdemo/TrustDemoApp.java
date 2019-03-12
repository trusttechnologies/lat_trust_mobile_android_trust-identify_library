package lat.trust.trustdemo;

import android.app.Application;
import android.content.IntentFilter;
import android.provider.Telephony;

import com.crashlytics.android.Crashlytics;
import com.orhanobut.hawk.Hawk;

import io.fabric.sdk.android.Fabric;
import lat.trust.trusttrifles.TrustClient;
import lat.trust.trusttrifles.broadcasts.SmsBroadcastReceiver;
import lat.trust.trusttrifles.utilities.TrustPreferences;

public class TrustDemoApp extends Application {
    private SmsBroadcastReceiver smsBroadcastReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        Hawk.init(this).build();
        TrustClient.init(this);
        TrustPreferences.init(this);
        smsBroadcastReceiver = new SmsBroadcastReceiver("123", "123");
        registerReceiver(smsBroadcastReceiver, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));
    }
}

package lat.trust.trustdemo;

import android.app.Application;

import com.orhanobut.hawk.Hawk;

import lat.trust.trusttrifles.TrustClient;
import lat.trust.trusttrifles.TrustConfig;
import lat.trust.trusttrifles.utilities.TrustPreferences;

public class TrustDemoApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Hawk.init(this).build();
        TrustClient.init(this);

    }
}

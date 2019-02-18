package lat.trust.trustdemo;

import android.app.Application;

import com.google.firebase.iid.FirebaseInstanceId;
import com.orhanobut.hawk.Hawk;


import lat.trust.trusttrifles.TrustClient;
import lat.trust.trusttrifles.utilities.TrustPreferences;

public class TrustDemoApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TrustClient.init(this);
        Hawk.init(this).build();
        TrustPreferences.init(this);

    }
}

package lat.trust.trustdemo;

import android.app.Application;

import com.orhanobut.hawk.Hawk;

import lat.trust.trusttrifles.TrustClient;
import lat.trust.trusttrifles.TrustClientLite;

public class TrustDemoApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Hawk.init(this).build();
        TrustClientLite.init(this);

    }

}

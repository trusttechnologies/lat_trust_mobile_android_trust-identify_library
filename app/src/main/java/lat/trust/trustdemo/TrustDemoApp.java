package lat.trust.trustdemo;

import android.app.Application;

import com.orhanobut.hawk.Hawk;

import lat.trust.trusttrifles.TrustClient;
import lat.trust.trusttrifles.TrustClientLite;
import lat.trust.trusttrifles.TrustClientZero;
import lat.trust.trusttrifles.TrustIdentifyConfigurationService;

public class TrustDemoApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Hawk.init(this).build();
        TrustClientZero.init(this);
        TrustIdentifyConfigurationService.setEnvironment(TrustIdentifyConfigurationService.ENVIRONMENT_MONKEY, this);
    }
}

package lat.trust.trustdemo;

import android.app.Application;

import com.orhanobut.hawk.Hawk;

import lat.trust.trusttrifles.Trust;
import lat.trust.trusttrifles.TrustClientZero;
import lat.trust.trusttrifles.TrustIdentifyConfigurationService;

public class TrustDemoApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Hawk.init(this).build();
        Trust.init(this);
        TrustIdentifyConfigurationService.setEnvironment(TrustIdentifyConfigurationService.ENVIRONMENT_PRODUCTION, this);
    }
}

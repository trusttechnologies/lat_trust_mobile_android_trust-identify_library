package lat.trust.trustdemo;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import com.orhanobut.hawk.Hawk;

import lat.trust.trusttrifles.Trust;
import lat.trust.trusttrifles.TrustIdentifyConfigurationService;

public class TrustDemoApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Hawk.init(this).build();
        Trust.init(this,
                getResources().getString(R.string.client_id),
                getResources().getString(R.string.client_secret));
        TrustIdentifyConfigurationService.setEnvironment(TrustIdentifyConfigurationService.ENVIRONMENT_MONKEY, this);
        Log.d("getResources", getResources().getString(R.string.client_id));
        Log.d("getResources", getResources().getString(R.string.client_secret));


    }
}

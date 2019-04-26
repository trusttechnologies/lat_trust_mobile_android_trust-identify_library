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
        String[] audits = {
                TrustConfig.AUDIT_BOOT,
                TrustConfig.AUDIT_NETWORK,
                TrustConfig.AUDIT_ALARM,
                TrustConfig.AUDIT_CALL,
                TrustConfig.AUDIT_SIM,
                TrustConfig.AUDIT_SMS
        };
        TrustClient.getInstance().setAudits(audits);
        TrustClient.getInstance().setAllAudit();
    }
}

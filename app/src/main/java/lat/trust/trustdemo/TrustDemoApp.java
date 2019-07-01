package lat.trust.trustdemo;

import android.app.Application;

import com.orhanobut.hawk.Hawk;
import com.trust.audit.audit.Audit;

import lat.trust.trusttrifles.TrustClient;

public class TrustDemoApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Hawk.init(this).build();
        TrustClient.init(this);


        Audit.init(this);
      /*  AuditConfiguration.setAudits(new String[]{
                AuditConfiguration.AUDIT_SIM,
                AuditConfiguration.AUDIT_SMS,
                AuditConfiguration.AUDIT_CALL,
                AuditConfiguration.AUDIT_NETWORK
        });*/

    }

}

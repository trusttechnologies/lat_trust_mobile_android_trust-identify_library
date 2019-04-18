package lat.trust.trustdemo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

import lat.trust.trusttrifles.TrustClient;
import lat.trust.trusttrifles.TrustConfig;
import lat.trust.trusttrifles.TrustListener;
import lat.trust.trusttrifles.model.Audit;
import lat.trust.trusttrifles.utilities.AutomaticAudit;
import lat.trust.trusttrifles.utilities.Constants;
import lat.trust.trusttrifles.utilities.Permissions;
import lat.trust.trusttrifles.utilities.TrustLogger;

public class SplashActivity extends AppCompatActivity implements TrustListener.Permissions {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Permissions.checkPermissions(SplashActivity.this, this);
//        TrustLogger.d(FirebaseInstanceId.getInstance().getToken());

        String[] audits = {
                TrustConfig.AUDIT_SIM,
                TrustConfig.AUDIT_SMS,
                TrustConfig.AUDIT_NETWORK,
                TrustConfig.AUDIT_CALL,
                TrustConfig.AUDIT_BOOT,
                TrustConfig.AUDIT_ALARM

        };
        TrustConfig.getInstance().setAudits(audits);

    }

    @Override
    public void onPermissionSuccess() {
        TrustClient mclient = TrustClient.getInstance();
            mclient.getTrifles(true, new TrustListener.OnResult<Audit>() {
                @Override
                public void onSuccess(int code, Audit data) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        finishAffinity();
                    } else {
                        finish();
                    }
                }

                @Override
                public void onError(int code) {
                    TrustLogger.d("[TRUST CLIENT] error code: " + code);
                }

                @Override
                public void onFailure(Throwable t) {
                    TrustLogger.d("[TRUST CLIENT] onFailure code: " + t.getMessage());

                }

                @Override
                public void onPermissionRequired(ArrayList<String> permisos) {

                }
            });






    }

    @Override
    public void onPermissionRevoke() {
        Toast.makeText(this, "Need permissions", Toast.LENGTH_SHORT).show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        } else {
            finish();

        }

    }
}

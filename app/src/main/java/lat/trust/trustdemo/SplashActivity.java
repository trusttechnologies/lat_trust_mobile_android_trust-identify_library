package lat.trust.trustdemo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;

import lat.trust.trusttrifles.TrustClient;
import lat.trust.trusttrifles.TrustListener;
import lat.trust.trusttrifles.model.Audit;
import lat.trust.trusttrifles.utilities.AutomaticAudit;
import lat.trust.trusttrifles.utilities.Permissions;

public class SplashActivity extends AppCompatActivity implements TrustListener.Permissions {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Permissions.checkPermissions(SplashActivity.this, this);
//        TrustLogger.d(FirebaseInstanceId.getInstance().getToken());
    }

    @Override
    public void onPermissionSuccess() {
        TrustClient mClient = TrustClient.getInstance();

        mClient.getTrifles(true, new TrustListener.OnResult<Audit>() {
            @Override
            public void onSuccess(int code, Audit data) {
                AutomaticAudit.createAutomaticAudit(
                        "APP START",
                        "ON PERMISSION SUCCESS",
                        "APP WAS STARTED",
                        SplashActivity.this);
            }

            @Override
            public void onError(int code) {

            }

            @Override
            public void onFailure(Throwable t) {

            }

            @Override
            public void onPermissionRequired(ArrayList<String> permisos) {

            }
        });
        startActivity(new Intent(this, MainActivity.class));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        } else {
            finish();
        }
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

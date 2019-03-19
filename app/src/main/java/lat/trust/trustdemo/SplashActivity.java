package lat.trust.trustdemo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import lat.trust.trusttrifles.TrustClient;
import lat.trust.trusttrifles.TrustListener;
import lat.trust.trusttrifles.utilities.Permissions;
import lat.trust.trusttrifles.utilities.TrustLogger;

public class SplashActivity extends AppCompatActivity implements TrustListener.Permissions {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Permissions.checkPermissions(SplashActivity.this, this);
    }

    @Override
    public void onPermissionSuccess() {
        TrustClient.start();
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

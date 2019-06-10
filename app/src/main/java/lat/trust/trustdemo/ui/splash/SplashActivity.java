package lat.trust.trustdemo.ui.splash;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import lat.trust.trustdemo.R;
import lat.trust.trustdemo.ui.home.MainActivity;
import lat.trust.trusttrifles.ui.DialogInformation;
import lat.trust.trusttrifles.ui.DialogPermission;
import lat.trust.trusttrifles.utilities.TrustLogger;

public class SplashActivity extends AppCompatActivity implements DialogPermission.DialogPermissionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TrustLogger.logo();
        DialogInformation dialogInformation = new DialogInformation(this);
        dialogInformation.configuration(
                this,
                getSupportFragmentManager(),
                R.drawable.ic_ico_sim_tarjeta_historial,
                "Trust Technologies",
                true);
        dialogInformation.show();

    }

    @Override
    public void applyPermission(boolean status) {
        TrustLogger.d(String.valueOf(status));
        if (status) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            // Permissions.checkPermissions(SplashActivity.this, this);
        } else {
            finish();
        }
    }
}

package lat.trust.trustdemo.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;

import lat.trust.trustdemo.R;
import lat.trust.trustdemo.ui.home.MainActivity;
import lat.trust.trusttrifles.TrustClient;
import lat.trust.trusttrifles.TrustClientLite;
import lat.trust.trusttrifles.TrustListener;
import lat.trust.trusttrifles.model.Audit;
import lat.trust.trusttrifles.ui.DialogInformation;
import lat.trust.trusttrifles.ui.DialogPermission;
import lat.trust.trusttrifles.utilities.TrustLogger;

import static android.Manifest.permission.BLUETOOTH;
import static android.Manifest.permission.READ_PHONE_STATE;

public class SplashActivity extends AppCompatActivity implements DialogPermission.DialogPermissionListener {
    private TextView trustid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TrustLogger.logo();
        trustid = findViewById(R.id.trustid);

        Dexter.withActivity(this).withPermissions(READ_PHONE_STATE, BLUETOOTH).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                getTrustId();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }
        }).check();

        DialogInformation dialogInformation = new DialogInformation(this);
       /* dialogInformation.configuration(
                this,
                getSupportFragmentManager(),
                R.drawable.ic_ico_sim_tarjeta_historial,
                "Trust Technologies",
                true);
        dialogInformation.show();*/

    }


    private void getTrustId() {
        TrustClientLite.getTrustIDLite(SplashActivity.this, new TrustListener.OnResult<Audit>() {
            @Override
            public void onSuccess(int code, Audit data) {
                TrustLogger.d(data.getTrustid());
                trustid.setText(data.getTrustid());
            }

            @Override
            public void onError(int code) {
                TrustLogger.d(code + "");

            }

            @Override
            public void onFailure(Throwable t) {
                TrustLogger.d(t.getMessage());

            }

            @Override
            public void onPermissionRequired(ArrayList<String> permisos) {

            }
        });
    }

    @Override
    public void applyPermission(boolean status) {
        TrustLogger.d(String.valueOf(status));
        if (status) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        } else {
            finish();
        }
    }
}

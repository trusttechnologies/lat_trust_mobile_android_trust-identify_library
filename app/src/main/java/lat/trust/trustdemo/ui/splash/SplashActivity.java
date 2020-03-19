package lat.trust.trustdemo.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

import lat.trust.trustdemo.R;
import lat.trust.trusttrifles.TrustClient;
import lat.trust.trusttrifles.TrustClientLite;
import lat.trust.trusttrifles.TrustClientZero;
import lat.trust.trusttrifles.TrustListener;
import lat.trust.trusttrifles.model.Audit;
import lat.trust.trusttrifles.model.Identity;
import lat.trust.trusttrifles.ui.DialogPermission;
import lat.trust.trusttrifles.utilities.TrustLogger;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SplashActivity extends AppCompatActivity implements DialogPermission.DialogPermissionListener {
    private TextView trustid;
    Button btnZero, btnNormal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        trustid = findViewById(R.id.trustid);
        btnZero = findViewById(R.id.btn_zero);
        btnNormal = findViewById(R.id.btn_normal);

        btnZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTrustIdZer(SplashActivity.this);
            }
        });
        btnNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTrustIdNormal(SplashActivity.this);
            }
        });
        /*  */

    }

    private void getTrustIdNormal(SplashActivity splashActivity) {
        Dexter.withActivity(this).withPermissions(
                READ_PHONE_STATE,
                READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                getTrustId();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }
        }).check();
    }

    private void getTrustIdZer(Context context) {
        TrustClientZero.getTrustIdZero(context, new TrustListener.OnResult<Audit>() {
            @Override
            public void onSuccess(int code, Audit data) {
                trustid.setText(data.getTrustid());
            }

            @Override
            public void onError(int code) {
                trustid.setText(code + "");
            }

            @Override
            public void onFailure(Throwable t) {
                trustid.setText(t.getMessage());

            }

            @Override
            public void onPermissionRequired(ArrayList<String> permisos) {

            }
        });
    }


    private void getTrustId() {
        TrustClientLite.getTrustIDLite(SplashActivity.this, new TrustListener.OnResult<Audit>() {
            @Override
            public void onSuccess(int code, Audit data) {
                TrustLogger.d(data.getTrustid());
                trustid.setText(data.getTrustid());

                identify();
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

    private void identify() {
        TrustLogger.d("SENDING IDENTIFY");
        Identity identity = new Identity();
        identity.setDni("18236924-1");
        identity.setEmail("fcaro@trust.lat");
        identity.setName("felipe");
        identity.setLastname("caro");
        identity.setPhone("+56982110950");
        TrustClientLite.sendIdentify(identity, this, new TrustListener.OnResult<Audit>() {
            @Override
            public void onSuccess(int code, Audit data) {

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
    }

    @Override
    public void applyPermission(boolean status) {

    }
}

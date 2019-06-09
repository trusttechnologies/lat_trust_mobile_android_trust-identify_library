package lat.trust.trustdemo.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import lat.trust.trustdemo.R;
import lat.trust.trustdemo.ui.home.MainActivity;
import lat.trust.trusttrifles.TrustListener;
import lat.trust.trusttrifles.ui.DialogPermission;
import lat.trust.trusttrifles.utilities.AutomaticAudit;
import lat.trust.trusttrifles.utilities.Permissions;
import lat.trust.trusttrifles.utilities.TrustLogger;

public class SplashActivity extends AppCompatActivity implements TrustListener.Permissions, DialogPermission.DialogPermissionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TrustLogger.logo();
        DialogPermission dialogPermission = new DialogPermission();

        int identifier = getResources().getIdentifier("ic_ico_sim_tarjeta_historial", "drawable", "lat.trust.trustdemo");
        dialogPermission.setConfiguration(identifier, "Trust", this, true, true);
        dialogPermission.show(getSupportFragmentManager(), "example dialog");
    }

    public static void checkOverlayPermission(Context mContex) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(mContex)) {
                Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                myIntent.setData(Uri.parse("package:" + mContex.getPackageName()));
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContex.startActivity(myIntent);
            }
        }
    }

    @Override
    public void onPermissionSuccess() {

        AutomaticAudit.createAutomaticAudit("inicio sesion", "en el splashy", "permisos concedidos", SplashActivity.this, new TrustListener.OnResultAudit() {
            @Override
            public void onSuccess(String idAudit) {
                TrustLogger.d("=============>" + idAudit);
                startActivity(new Intent(SplashActivity.this, MainActivity.class));

            }

            @Override
            public void onError(String error) {

            }
        });
        /*TrustClient mclient = TrustClient.getInstance();
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

*/


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

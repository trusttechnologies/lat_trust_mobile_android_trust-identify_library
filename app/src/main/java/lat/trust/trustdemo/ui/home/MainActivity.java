package lat.trust.trustdemo.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;
import lat.trust.trustdemo.R;
import lat.trust.trustdemo.ui.audit.AuditActivity;
import lat.trust.trustdemo.ui.trustid.TrustIdActivity;
import lat.trust.trusttrifles.TrustListener;
import lat.trust.trusttrifles.model.Audit;
import lat.trust.trusttrifles.model.audit.AuditTransaction;
import lat.trust.trusttrifles.services.Notifications;
import lat.trust.trusttrifles.utilities.AutomaticAudit;
import lat.trust.trusttrifles.utilities.Constants;
import lat.trust.trusttrifles.utilities.SavePendingAudit;
import lat.trust.trusttrifles.utilities.TrustLogger;
import lat.trust.trusttrifles.utilities.TrustPreferences;
import lat.trust.trusttrifles.utilities.Utils;

public class MainActivity extends AppCompatActivity {


    private MaterialDialog loadingDialog;
    private TrustPreferences mPreferences;
    private LocationManager mLocationManager;
    private BroadcastReceiver broadcastReceiver;
    private MaterialButton materialButton;
    private Context mContext;
    private EditText et;
    private Button btn_audit;
    private Button btn_trust_id;
    private Button btn_session;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //checkPermissions();
        mContext = this;
        TrustLogger.d(FirebaseInstanceId.getInstance().getToken() != null ? FirebaseInstanceId.getInstance().getToken() : "no firebase token id");
        Fabric.with(this, new Crashlytics());
       /* materialButton = findViewById(R.id.button);
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrustLogger.d(Utils.getActualConnection(MainActivity.this));
                Hawk.put(Constants.DNI_USER, "18236924-1");
                Hawk.put(Constants.EMAIL_USER, "fcaro@trust.lat");
                Hawk.put(Constants.LASTNAME_USER, "Caro");
                Hawk.put(Constants.NAME_USER, "FELIPE");
                Hawk.put(Constants.PHONE_USER, "+56982110950");
                //AutomaticAudit.createAutomaticAudit("test1","test2","result test",MainActivity.this);
                TrustLogger.d(FirebaseInstanceId.getInstance().getToken());
                Notifications.registerDevice(FirebaseInstanceId.getInstance().getToken(), MainActivity.this);

            }
        });*/
        bind();
        btn_audit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAudit();
            }
        });
        btn_trust_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTrustId();
            }
        });

        btn_session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Audit asd = new Audit();
                asd.setMessage("el mensajito ");
                asd.setStatus(true);
                asd.setTrustid("el trust id" + getLocalClassName() + "1323123");

                if (Utils.getWifiState(MainActivity.this)) {
                    AutomaticAudit.createAutomaticAudit("operacion", "metodo de prueba mayo", "resultado de prueba uuid nuevo sin conexion: ", asd, MainActivity.this, new TrustListener.OnResultAudit() {
                        @Override
                        public void onSuccess(String idAudit) {
                            Toast.makeText(mContext, idAudit, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
                } else {
                    SavePendingAudit.createOfflineAudit("Prueba auditoria sin conexion", "testing method", "Testing auditoria sin conexion exitoso", asd, MainActivity.this, new TrustListener.OnResultAudit() {
                        @Override
                        public void onSuccess(String idAudit) {
                            TrustLogger.d(idAudit);
                        }

                        @Override
                        public void onError(String error) {
                            TrustLogger.d(error);
                        }
                    });
                }
            }
        });

    }

    private void goTrustId() {
        startActivity(new Intent(MainActivity.this, TrustIdActivity.class));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        } else {
            finish();
        }
    }

    private void goAudit() {
        startActivity(new Intent(MainActivity.this, AuditActivity.class));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        } else {
            finish();
        }

    }

    private void bind() {
        btn_audit = findViewById(R.id.btn_audit_home);
        btn_trust_id = findViewById(R.id.btn_trust_id_home);

        et = findViewById(R.id.et);
        btn_session = findViewById(R.id.btn_session);
    }

    private void showLoading() {
        loadingDialog = new MaterialDialog.Builder(this)
                .theme(Theme.LIGHT)
                .typeface(ResourcesCompat.getFont(this, R.font.fira_sans_extra_condensed_medium), ResourcesCompat.getFont(this, R.font.fira_sans_extra_condensed))
                .progressIndeterminateStyle(true)
                .progress(true, 100)
                .content("Creando Trust ID...")
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .show();
    }

    private void dismissLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) loadingDialog.dismiss();
    }


}

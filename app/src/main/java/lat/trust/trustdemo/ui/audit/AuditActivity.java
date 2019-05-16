package lat.trust.trustdemo.ui.audit;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.JsonObject;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;

import lat.trust.trustdemo.R;
import lat.trust.trustdemo.ui.home.MainActivity;
import lat.trust.trusttrifles.TrustListener;
import lat.trust.trusttrifles.utilities.AutomaticAudit;
import lat.trust.trusttrifles.utilities.Constants;
import lat.trust.trusttrifles.utilities.SavePendingAudit;
import lat.trust.trusttrifles.utilities.TrustLogger;

public class AuditActivity extends AppCompatActivity {

    private Button btn_audit_test, btn_audit;
    private EditText et_operacion, et_metodo, et_resultado;
    private ImageView iv_backaudit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit);
        bind();
        btn_audit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateAudit()) {
                    sendAudit();
                }
            }
        });
        btn_audit_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBtnAuditTest();
            }
        });
        iv_backaudit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volver();
            }
        });
    }

    private boolean validateAudit() {
        boolean ok = true;
        if (TextUtils.isEmpty(et_operacion.getText().toString())) {
            et_operacion.setError("No puede ser vacio");
            ok = false;
        }
        if (TextUtils.isEmpty(et_metodo.getText().toString())) {
            et_metodo.setError("No puede ser vacio");
            ok = false;
        }
        if (TextUtils.isEmpty(et_resultado.getText().toString())) {
            et_resultado.setError("No puede ser vacio");
            ok = false;
        }

        return ok;
    }

    private void bind() {
        btn_audit = findViewById(R.id.btn_audit);
        btn_audit_test = findViewById(R.id.btn_audit_test);
        et_metodo = findViewById(R.id.et_metodo);
        et_operacion = findViewById(R.id.et_operacion);
        et_resultado = findViewById(R.id.et_resultado);
        iv_backaudit = findViewById(R.id.iv_backAudit);
    }

    private void sendAudit() {
        /*AutomaticAudit.createAutomaticAudit(
                et_operacion.getText().toString(),
                et_metodo.getText().toString(),
                et_resultado.getText().toString(),
                AuditActivity.this);*/


        AutomaticAudit.createAutomaticAudit(et_operacion.getText().toString(), et_metodo.getText().toString(), et_resultado.getText().toString(), this, new TrustListener.OnResultAudit() {
            @Override
            public void onSuccess(String idAudit) {
                TrustLogger.d("--------------------> " + idAudit);
            }

            @Override
            public void onError(String error) {
                TrustLogger.d("--------------------> " + error);

            }
        });

        AutomaticAudit.createAutomaticAudit(et_operacion.getText().toString(), et_metodo.getText().toString(), et_resultado.getText().toString(), this, new TrustListener.OnResultAudit() {
            @Override
            public void onSuccess(String idAudit) {
                TrustLogger.d("--------------------> " + idAudit);
            }

            @Override
            public void onError(String error) {
                TrustLogger.d("--------------------> " + error);

            }
        });
        AutomaticAudit.createAutomaticAudit(et_operacion.getText().toString(), et_metodo.getText().toString(), et_resultado.getText().toString(), this, new TrustListener.OnResultAudit() {
            @Override
            public void onSuccess(String idAudit) {
                TrustLogger.d("--------------------> " + idAudit);
            }

            @Override
            public void onError(String error) {
                TrustLogger.d("--------------------> " + error);

            }
        });


    }

    private void setBtnAuditTest() {
        if (btn_audit_test.getText().toString().equals("valores de prueba")) {
            et_resultado.setText("Resultado de prueba exitoso");
            et_operacion.setText("Operacion de prueba");
            et_metodo.setText("Metodo de prueba");
            btn_audit_test.setText("Limpiar valores");
            List<SavePendingAudit.SavedAudit> lstAudit2 = Hawk.get(Constants.LST_AUDIT);
            for (SavePendingAudit.SavedAudit lista : lstAudit2
            ) {
                TrustLogger.d("lista : " + lista.getOperation());
            }
            TrustLogger.d("lista : " + lstAudit2.size());

        } else {
            List<SavePendingAudit.SavedAudit> lstAudit3 = Hawk.get(Constants.LST_AUDIT);
            for (SavePendingAudit.SavedAudit lista : lstAudit3
            ) {
                TrustLogger.d("lista : " + lista.getOperation());
            }
            TrustLogger.d("lista : " + lstAudit3.size());

            et_resultado.setText("");
            et_operacion.setText("");
            et_metodo.setText("");
            btn_audit_test.setText("valores de prueba");

        }
    }

    private void volver() {
        startActivity(new Intent(this, MainActivity.class));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        } else {
            finish();
        }
    }
}

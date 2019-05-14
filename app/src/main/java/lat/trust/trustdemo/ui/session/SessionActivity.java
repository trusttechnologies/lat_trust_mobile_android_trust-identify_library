package lat.trust.trustdemo.ui.session;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import lat.trust.trustdemo.R;
import lat.trust.trustdemo.ui.home.MainActivity;

public class SessionActivity extends AppCompatActivity {

    private TextView et_rut, et_nombre, et_apellido, et_correo, et_telefono;
    private Button btn_enviar, btn_limpiar;
    private ImageView volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        bind();
    }

    private void bind() {
        et_apellido = findViewById(R.id.et_apellido);
        et_correo = findViewById(R.id.et_correo);
        et_nombre = findViewById(R.id.et_nombre);
        et_rut = findViewById(R.id.et_rut);
        et_telefono = findViewById(R.id.et_telefono);
        btn_enviar = findViewById(R.id.btn_session);
        btn_limpiar = findViewById(R.id.btn_session_test);
        volver = findViewById(R.id.iv_backSession);
    }

    private void volver() {
        startActivity(new Intent(SessionActivity.this, MainActivity.class));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        } else {
            finish();
        }
    }

    private void setSession() {

    }
}

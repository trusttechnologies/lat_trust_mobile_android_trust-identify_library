package lat.trust.trustdemo.ui.trustid;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import lat.trust.trustdemo.R;
import lat.trust.trustdemo.ui.home.MainActivity;
import lat.trust.trusttrifles.TrustClient;
import lat.trust.trusttrifles.TrustListener;
import lat.trust.trusttrifles.model.Audit;

public class TrustIdActivity extends AppCompatActivity {

    private ImageView iv_backtrustid;
    private TextView tv_trustId;
    private Button btn_trust_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_trust_id);
        bind();
        btn_trust_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_trustId.setText("Obteniendo trust id...");
                generateTrustId();
            }
        });
        iv_backtrustid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volver();
            }
        });
    }

    private void volver() {
        startActivity(new Intent(TrustIdActivity.this, MainActivity.class));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        } else {
            finish();
        }
    }

    private void bind() {
        iv_backtrustid = findViewById(R.id.iv_backTrustid);
        tv_trustId = findViewById(R.id.tv_trust_id_id);
        btn_trust_id = findViewById(R.id.btn_trust_id_id);

    }

    private void generateTrustId() {
        TrustClient.getInstance().getTrifles(true, new TrustListener.OnResult<Audit>() {
            @Override
            public void onSuccess(int code, Audit data) {
                tv_trustId.setText(data.getTrustid());
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
}

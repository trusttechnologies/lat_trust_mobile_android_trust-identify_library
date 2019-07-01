package lat.trust.trustdemo.ui.home;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.iid.FirebaseInstanceId;

import io.fabric.sdk.android.Fabric;
import lat.trust.trustdemo.R;
import lat.trust.trusttrifles.utilities.TrustLogger;

public class MainActivity extends AppCompatActivity implements HomeContract.View {

    private HomeContract.Presenter mPresenter;
    private TextView txt_trust_id, txt_audit, txt_notification, txt_company, txt_login;
    private ImageView img_trust_id, img_audit, img_notification, img_company, img_login;
    private Button btn_audit, btn_login;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TrustLogger.d(FirebaseInstanceId.getInstance().getToken() != null ? FirebaseInstanceId.getInstance().getToken() : "no firebase token id");
        Fabric.with(this, new Crashlytics());
        mPresenter = new HomePresenter(this);
        mPresenter.onCreate();
        btn_audit = findViewById(R.id.btn_send_audit);
        btn_login = findViewById(R.id.btn_login);


        btn_audit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.audit();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.login();
            }
        });
    }


    @Override
    public void showTrustId(String trustId) {
        txt_trust_id = findViewById(R.id.txt_trust_id);
        img_trust_id = findViewById(R.id.img_trust_id);
        txt_trust_id.setText(trustId);
        img_trust_id.setImageResource(R.drawable.ic_green_24dp);

    }

    @Override
    public void showNotification() {
        txt_notification = findViewById(R.id.txt_notification);
        img_notification = findViewById(R.id.img_notification);

        txt_notification.setText("Notification was succes!");
        img_notification.setImageResource(R.drawable.ic_green_24dp);
    }

    @Override
    public void showCompany() {
        txt_company = findViewById(R.id.txt_company);
        img_company = findViewById(R.id.img_company);

        txt_company.setText("Company was sended!");
        img_company.setImageResource(R.drawable.ic_green_24dp);
    }

    @Override
    public void showAudit() {
        txt_audit = findViewById(R.id.txt_audit);
        img_audit = findViewById(R.id.img_audit);

       /* Audit.createAudit("operacion", "metodo", "result", this, new AuditListener.OnResult<String>() {
            @Override
            public void onSuccess(int code, String data) {
                txt_audit.setText(data);
                img_audit.setImageResource(R.drawable.ic_green_24dp);
            }

            @Override
            public void onError(int code) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });*/

    }

    @Override
    public void showLogin() {

    }
}

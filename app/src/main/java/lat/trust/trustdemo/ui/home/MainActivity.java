package lat.trust.trustdemo.ui.home;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import lat.trust.trustdemo.R;

public class MainActivity extends AppCompatActivity  {

    private TextView txt_trust_id, txt_audit, txt_notification, txt_company, txt_login;
    private ImageView img_trust_id, img_audit, img_notification, img_company, img_login;
    private Button btn_audit, btn_login;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fabric.with(this, new Crashlytics());
        btn_audit = findViewById(R.id.btn_send_audit);
        btn_login = findViewById(R.id.btn_login);


    }



}

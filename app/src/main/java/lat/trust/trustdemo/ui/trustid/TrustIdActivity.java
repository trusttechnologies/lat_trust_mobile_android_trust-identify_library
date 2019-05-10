package lat.trust.trustdemo.ui.trustid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import lat.trust.trustdemo.R;
import lat.trust.trusttrifles.TrustClient;
import lat.trust.trusttrifles.TrustListener;
import lat.trust.trusttrifles.model.Audit;

public class TrustIdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_trust_id);
    }

    private void generateTrustId(){
        TrustClient.getInstance().getTrifles(true, new TrustListener.OnResult<Audit>() {
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
}

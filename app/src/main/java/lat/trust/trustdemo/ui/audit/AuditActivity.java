package lat.trust.trustdemo.ui.audit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import lat.trust.trustdemo.R;
import lat.trust.trusttrifles.utilities.AutomaticAudit;

public class AuditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit);
    }

    private void sendAudit(){
        AutomaticAudit.createAutomaticAudit("OPERACION","METODO" , "RESULT",AuditActivity.this);
    }
}

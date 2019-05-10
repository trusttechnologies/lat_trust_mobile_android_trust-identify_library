package lat.trust.trustdemo.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.orhanobut.hawk.Hawk;

import io.fabric.sdk.android.Fabric;
import lat.trust.trustdemo.R;
import lat.trust.trusttrifles.services.Notifications;
import lat.trust.trusttrifles.utilities.Constants;
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

package lat.trust.trustdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.location.Location;
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

import io.fabric.sdk.android.Fabric;

import lat.trust.trusttrifles.utilities.GPSTracker;
import lat.trust.trusttrifles.utilities.TrustLogger;
import lat.trust.trusttrifles.utilities.TrustPreferences;

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
        materialButton = findViewById(R.id.button);
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPSTracker gpsTracker = new GPSTracker(mContext);
                Location location = gpsTracker.getLocation();

            }
        });
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

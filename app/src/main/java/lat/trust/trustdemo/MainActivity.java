package lat.trust.trustdemo;

import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.iid.FirebaseInstanceId;

import io.fabric.sdk.android.Fabric;
import lat.trust.trusttrifles.TrustListener;
import lat.trust.trusttrifles.utilities.Permissions;
import lat.trust.trusttrifles.utilities.TrustLogger;
import lat.trust.trusttrifles.utilities.TrustPreferences;

public class MainActivity extends AppCompatActivity  {


    private MaterialDialog loadingDialog;
    private TrustPreferences mPreferences;
    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //checkPermissions();
        TrustLogger.d(FirebaseInstanceId.getInstance().getToken() != null ? FirebaseInstanceId.getInstance().getToken() : "no firebase token id");
        Fabric.with(this, new Crashlytics());
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

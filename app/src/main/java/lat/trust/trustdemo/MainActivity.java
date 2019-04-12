package lat.trust.trustdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
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
import lat.trust.trusttrifles.TrustConfig;
import lat.trust.trusttrifles.services.TestLocationService;
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

        materialButton = findViewById(R.id.button);
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //turnGPSOn();

                TrustLogger.d(Utils.getActualConnection(MainActivity.this));
                Hawk.put(Constants.DNI_USER, "18236924-1");
                Hawk.put(Constants.EMAIL_USER, "fcaro@trust.lat");
                Hawk.put(Constants.LASTNAME_USER, "Caro");
                Hawk.put(Constants.NAME_USER, "FELIPE");
                Hawk.put(Constants.PHONE_USER, "+56982110950");


                Intent gps = new Intent(MainActivity.this, TestLocationService.class);
                gps.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startService(gps);
                // startActivity(new Intent(MainActivity.this,TestLocationActivity.class));
            }
        });
    }

    private void turnGPSOn() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

    private class Trust {
        private String email;
        private String dni;
        private String phoneNumber;
        private String name;
        private String lastName;

        public Trust() {
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getDni() {
            return dni;
        }

        public void setDni(String dni) {
            this.dni = dni;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
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

package lat.trust.trustdemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.orhanobut.hawk.Hawk;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lat.trust.trustdemo.Utils.Utils;
import lat.trust.trusttrifles.TrustClient;
import lat.trust.trusttrifles.TrustListener;
import lat.trust.trusttrifles.model.Audit;
import lat.trust.trusttrifles.services.LocationService;
import lat.trust.trusttrifles.utilities.TrustPreferences;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
import static lat.trust.trustdemo.SIMData.TYPE_IMEI;
import static lat.trust.trustdemo.SIMData.TYPE_SIM;
import static lat.trust.trusttrifles.utilities.Constants.BUILD_PROP;
import static lat.trust.trusttrifles.utilities.Constants.SIM_RECEIVER_TAG;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TrustClient mClient;
    private boolean permissionsAccepted = false;
    private LinearLayout notificationLayout;
    private TextView notificationTitle;
    private TextView notificationData;
    private TextView notificationTime;
    private TextView simCarrierInput;
    private RecyclerView recyclerView;
    private TextInputEditText trustInput;
    private HistoryAdapter adapter;
    Location mLocation = null;
    private String mImei = null;
    private String carrier = null;


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                String state = extras.getString("ss", "UNKNOWN");
                onSIMChangeDetected(state);
            }
        }
    };

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
            mLocation = location;
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private MaterialDialog loadingDialog;
    private TrustPreferences mPreferences;
    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this,LocationService.class));
//        Log.d("TAG",FirebaseInstanceId.getInstance().getToken());

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mClient = TrustClient.getInstance();
        TrustPreferences.init(this);
        mPreferences = TrustPreferences.getInstance();

        notificationLayout = findViewById(R.id.actual_sim_layout);
        notificationLayout.setVisibility(View.GONE);
        notificationData = findViewById(R.id.actual_sim_id);
        notificationTime = findViewById(R.id.actual_sim_date);
        notificationTitle = findViewById(R.id.actual_sim_title);
        simCarrierInput = findViewById(R.id.sim_carrier);
        trustInput = findViewById(R.id.trustid_input);
        recyclerView = findViewById(R.id.history_list);

        adapter = new HistoryAdapter();
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
        recyclerView.invalidate();

        Button button = findViewById(R.id.button);
        //button.setOnClickListener(feaListener);
        button.setOnClickListener(this);
        Button event = findViewById(R.id.event);
        event.setOnClickListener(this);
        String tid;
        if (Hawk.contains("TRUST_DEMO_ID")) {
            tid = Hawk.get("TRUST_DEMO_ID");
        } else {
            tid = "No Registrado";
        }

        trustInput.setText(tid);

        //TEST
        Log.d("TRUSTCLIENT", "START");
        //exec();

        Log.d("TRUSTCLIENT", "END");
    }

    private void exec() {
        try {

            String comando = "cat " + BUILD_PROP;
            Process P = Runtime.getRuntime().exec(comando);
            Log.d("TRUSTCLIENT", "Comando: " + "'" + comando + "'");
            P.waitFor();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(P.getInputStream()));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.startsWith("#") && !line.startsWith("\n") && !line.isEmpty()) {
                    if (line.contains("=")) {
                        String[] split = line.split("=");
                        if (split.length == 2)
                            stringBuilder.append(split[0]).append(" = ").append(split[1]).append("\n");
                        else
                            stringBuilder.append(line).append("\n");
                    } else
                        stringBuilder.append(line).append("\n");
                }
            }
            bufferedReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        showLog(getBatteryInfo(this));
    }

    public void showLog(String string) {
        int sizeLog = 4000;
        int paquetes = string.length() / sizeLog;
        int extras = string.length() % sizeLog;
        for (int i = 0; i < paquetes; i++) {
            Log.d("TRUSTCLIENT", String.valueOf(string.substring(i * sizeLog, (i + 1) * sizeLog)));
        }
        Log.d("TRUSTCLIENT", String.valueOf(string.substring(paquetes * sizeLog, paquetes * sizeLog + extras)));

    }

    public String getBatteryInfo(Context ctx) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent intent = registerReceiver(null, ifilter);
        String technology = null;
        Boolean present = null;
        String capacidad;
        if (intent != null) {
            present = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);
            if (intent.getExtras() != null) {
                technology = intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
            }

        }
        String bateria;
        if (present != null && present)
            bateria = "Presente ";
        else
            bateria = "Ausente ";
        if (technology == null)
            technology = "NOT FOUND";
        int capacity = (int) getBatteryCapacity(ctx);

        if (capacity == 0)
            capacidad = "NOT FOUND";
        else
            capacidad = String.valueOf(capacity) + " mAh";

        return "Battery: " + bateria + "\n" +
                "Capacity: " + capacidad + "\n" +
                "Technology: " + technology;
    }

    @SuppressLint("PrivateApi")
    public double getBatteryCapacity(Context context) {
        Object mPowerProfile;
        double batteryCapacity = 0;
        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class)
                    .newInstance(context);

            batteryCapacity = (double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getBatteryCapacity")
                    .invoke(mPowerProfile);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return batteryCapacity;

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermissions();
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

    private void checkPermissions() {

        Dexter.withActivity(this)
                .withPermissions(READ_PHONE_STATE, ACCESS_COARSE_LOCATION, CAMERA,ACCESS_FINE_LOCATION,READ_SMS)
                .withListener(new MultiplePermissionsListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        /*
                        mImei = mClient.getImei();
                        permissionsAccepted = report.areAllPermissionsGranted();
                        setActualSIM(mClient.getSIMState());
                        carrier = mClient.getSIMCarrier();
                        updateCarrier();

                        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000,
                                100, mLocationListener);*/
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        //token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver),
                new IntentFilter(SIM_RECEIVER_TAG)
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    private View.OnClickListener feaListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent test = new Intent();
            test.setAction("acepta.action.SIGN_TRANSACTION");
            test.putExtra("TRANSACTION_ID", "1");
            test.putExtra("DARK_MODE", true);
            test.putExtra("COLOR_PRIMARY", "#555555");
            test.putExtra("COLOR_PRIMARY_DARK", "#555555");
            Drawable drawableCompat = ContextCompat.getDrawable(MainActivity.this, R.drawable.logo_acepta_min);
            Bitmap bitmap = ((BitmapDrawable) drawableCompat).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();
            test.putExtra("BRAND_LOGO", bitmapdata);
            bitmap.recycle();
            startActivityForResult(test, 0);
        }
    };


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.event) {
            new MaterialDialog.Builder(this)
                    .title("Modifique su Imei")
                    .inputType(InputType.TYPE_CLASS_NUMBER)
                    .input("Nueva IMEI", mImei, false, new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        }
                    })
                    .positiveText("Aceptar")
                    .negativeText("Cancelar")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            if (!dialog.getInputEditText().getText().toString().equals(mImei)) {

                                if (Hawk.contains("TRUST_DEMO_ID")) {
                                    String tid = Hawk.get("TRUST_DEMO_ID");
                                    double lat = (mLocation != null) ? mLocation.getLatitude() : 0;
                                    double lng = (mLocation != null) ? mLocation.getLongitude() : 0;

                                    List<SIMData> history = new ArrayList<>();
                                    if (Hawk.contains("HISTORY")) {
                                        history = Hawk.get("HISTORY");
                                    }
                                    Date date = new Date();
                                    history.add(0, new SIMData(dialog.getInputEditText().getText().toString(), date.getTime(), TYPE_IMEI));
                                    Hawk.put("HISTORY", history);
                                    updateHistory();

                                    mClient.remoteEvent2(tid, "device", "imei", dialog.getInputEditText().getText().toString(), lat, lng, null);
                                }
                            }
                        }
                    })
                    .show();
        } else {
            /*
            if (ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, CAMERA) != PackageManager.PERMISSION_GRANTED) {
                checkPermissions();
                return;
            }*/
            showLoading();
            final boolean existTrustid = Hawk.contains("TRUST_DEMO_ID");
            mClient.getTrifles(true, false, true, false, new TrustListener.OnResult<Audit>() {
                @Override
                public void onSuccess(int code, Audit audit) {
                    dismissLoading();
                    Hawk.put("TRUST_DEMO_ID", audit.getTrustId());
                    trustInput.setText(audit.getTrustId());
                    if (!existTrustid && code == 200)
                        Toast.makeText(MainActivity.this, "Trust id Creado!", Toast.LENGTH_LONG).show();
                    else if (existTrustid && code == 200)
                        Toast.makeText(MainActivity.this, "Reporte sin cambios", Toast.LENGTH_LONG).show();
                    else if (code == 201)
                        Toast.makeText(MainActivity.this, "Actualizado", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(int code) {
                    dismissLoading();
                    Toast.makeText(MainActivity.this, "Error " + code, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Throwable t) {
                    dismissLoading();
                    Toast.makeText(MainActivity.this, "Fail: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onPermissionRequired(ArrayList<String> permisos) {
                    if (permisos != null && permisos.size() > 0) {
                        for (String permiso : permisos) {
                            Log.d("TRUSTCLIENT", "Permiso requerido: " + permiso);
                        }
                    } else
                        Log.d("TRUSTCLIENT", "Permisos requerido, pero arreglo vacío");
                }
            });
        }


    }

    public void setActualSIM(String state) {
        if (notificationLayout.getVisibility() == View.GONE)
            notificationLayout.setVisibility(View.VISIBLE);
        List<SIMData> history = null;
        if (Hawk.contains("HISTORY")) {
            history = Hawk.get("HISTORY");
        }

        String title = "No se han detectado cambios";
        String message;
        Date date = new Date();

        switch (state) {
            case "ABSENT":
                message = "SIM Extraida";
                carrier = null;
                updateCarrier();
                break;
            case "LOADED":
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                message = mClient.getSIMSerialID();
                this.carrier = mClient.getSIMCarrier();
                break;
            default:
                message = "Sin Información";
                carrier = null;
                updateCarrier();
                break;
        }

        if (history != null && history.size() > 0) {
            SIMData data = history.get(0);
            if (!message.equals(data.getId())) {
                title = "Se detectó un cambio en la SIM";
                updateSIMChanges(new SIMData(message, date.getTime(), TYPE_SIM));
            }

        }
        configSIMLayout(title, message, Utils.formatDate(new Date()));

    }

    private void updateCarrier() {
        simCarrierInput.setText((carrier == null) ? "SIM ACTUAL" : "SIM ACTUAL: " + carrier);
    }

    public void onSIMChangeDetected(String state) {
        if (permissionsAccepted) {
            if (notificationLayout.getVisibility() == View.GONE)
                notificationLayout.setVisibility(View.VISIBLE);
            String title = "Se detectó un cambio en la SIM";
            String message;
            Date now = new Date();
            switch (state) {
                case "ABSENT":
                    message = "SIM Extraida";
                    carrier = null;
                    updateSIMChanges(new SIMData(message, now.getTime(), TYPE_SIM));
                    break;
                case "LOADED":
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    message = mClient.getSIMSerialID();

                    this.carrier = mClient.getSIMCarrier();
                    updateSIMChanges(new SIMData(message, now.getTime(), TYPE_SIM));
                    break;
                case "READY":
                    carrier = null;
                    message = "Cargando...";
                    break;
                case "IMSI":
                    carrier = null;
                    message = "Cargando...";
                    break;
                default:
                    carrier = null;
                    message = "No se pudo cargar la información";
            }

            updateCarrier();
            configSIMLayout(title, message, Utils.formatDate(now));
        }

    }

    private void updateSIMChanges(SIMData simData) {
        List<SIMData> list = new ArrayList<>();
        if (Hawk.contains("HISTORY")) {
            list = Hawk.get("HISTORY");
        }
        list.add(0, simData);
        Hawk.put("HISTORY", list);

        updateHistory();
        if (Hawk.contains("TRUST_DEMO_ID")) {
            String tid = Hawk.get("TRUST_DEMO_ID");
            double lat = (mLocation != null) ? mLocation.getLatitude() : 0;
            double lng = (mLocation != null) ? mLocation.getLongitude() : 0;

            mClient.remoteEvent(tid, "sim", "iccid", simData.getId(), lat, lng, null);
        } else {
            Toast.makeText(this, "No exite Trust ID, presione Iniciar para crearlo", Toast.LENGTH_SHORT).show();
        }
    }
    private void updateHistory() {
        adapter.update();
        recyclerView.invalidate();
    }

    private void configSIMLayout(String title, String message, String date) {
        notificationTitle.setText(title);
        notificationTime.setText(date);
        notificationData.setText(message);
    }
}

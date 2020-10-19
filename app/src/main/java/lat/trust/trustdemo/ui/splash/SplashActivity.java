package lat.trust.trustdemo.ui.splash;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

import lat.trust.trustdemo.R;
import lat.trust.trustdemo.ui.splash.adapter.ItemClickListener;
import lat.trust.trustdemo.ui.splash.adapter.SplashAdapter;
import lat.trust.trusttrifles.Trust;
import lat.trust.trusttrifles.model.StringsModel;
import lat.trust.trusttrifles.model.TrustResponse;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SplashActivity extends AppCompatActivity implements SplashContract.View {
    private SplashContract.Presenter mPresenter;

    MaterialButton btnNormal, btnZero, btnLitle, btnCreateToken, btnRemoveToken, btnClearlog, btnOverwrite;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mPresenter = new SplashPresenter(this);

        btnCreateToken = findViewById(R.id.setToken);
        btnLitle = findViewById(R.id.trustIdLitle);
        btnNormal = findViewById(R.id.trustIdNormal);
        btnRemoveToken = findViewById(R.id.removeToken);
        btnOverwrite = findViewById(R.id.btnOverWrite);
        btnZero = findViewById(R.id.trustIdZero);
        btnClearlog = findViewById(R.id.clearRecycler);
        recyclerView = findViewById(R.id.recyclerLog);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mPresenter.onCreate();

        btnOverwrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Trust.overWriteTrust(
                        "fd6462f6-f53f-4d1d-bd04-28187189494e",
                      "e6dbf005-cd77-4d74-a546-27f459a14295",
                        SplashActivity.this);
            }
        });

        btnClearlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.clearLog();
            }
        });
        btnZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.requestTrustIdZero(SplashActivity.this);
            }
        });

        btnLitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.requestTrustIdLite(SplashActivity.this);
            }
        });

        btnNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.requestTrustIdNormal(SplashActivity.this);
            }
        });
        btnCreateToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.requestCustomToken("Bearer 9r23ukvUgg7mb-XCbRChKcdSpH5Rcc3idkOREUwt1Dw");
            }
        });
        btnRemoveToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.requestDeleteToken();
            }
        });
    }


    @Override
    public void requestPermissions() {
        Dexter.withActivity(this).withPermissions(
                READ_PHONE_STATE,
                READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                mPresenter.initView();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }
        }).check();
    }

    @Override
    public void reloadData(ArrayList<StringsModel> lst) {
        mAdapter = new SplashAdapter(lst);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void setDataRecyclerView(ArrayList<StringsModel> lst) {
        mAdapter = new SplashAdapter(lst);
        recyclerView.setAdapter(mAdapter);
    }


}

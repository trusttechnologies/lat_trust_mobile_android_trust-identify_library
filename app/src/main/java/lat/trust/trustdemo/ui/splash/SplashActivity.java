package lat.trust.trustdemo.ui.splash;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import lat.trust.trustdemo.R;
import lat.trust.trustdemo.ui.splash.adapter.SplashAdapter;
import lat.trust.trusttrifles.Trust;
import lat.trust.trusttrifles.model.StringsModel;
import lat.trust.trusttrifles.network.res.AuthTokenResponseFlavor;

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
                AuthTokenResponseFlavor authTokenResponse = new AuthTokenResponseFlavor();
                authTokenResponse.setClientId("1f647aab37f4a7d7a0da408015437e7a963daca43da06a7789608c319c2930bd");
                authTokenResponse.setClientSecret("adcc11078bee4ba2d7880a48c4bed02758a5f5328276b08fa14493306f1e9efb");

                authTokenResponse.setRefreshToken("nle0nT41eDUKDDB46FMCDd860_w4eMwlq4GwvA5WPXM");
                authTokenResponse.setGrantType("refresh_token");
                authTokenResponse.setTokenType("Bearer");
                authTokenResponse.setAccessToken("Bearer nle0nT41eDUKDDB46FMCDd860_w4eMwlq4GwvA5WPXM");
                mPresenter.requestCustomToken(authTokenResponse);
            }
        });
        btnRemoveToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.requestDeleteToken();
            }
        });
        createFile();
        saveFile();
        readFile();
    }

    private void createFile() {
        String folder_main = "Trust";
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
        File f = new File(path, folder_main);
        if (!f.exists()) {
            f.mkdirs();
            Log.d("createFile", "folder created");
        } else {
            Log.d("createFile", "!folder created");
        }
    }

    private void readFile() {
        String filename = "android_system";
        String folder_main = "Trust";
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + File.separator + folder_main + File.separator + filename;
        try {
            File myFile = new File(path);
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));
            String aDataRow = "";
            String aBuffer = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow + "\n";
            }
            Log.d("createFile", "read: " + aBuffer);

            myReader.close();

        } catch (Exception e) {
            Log.d("createFile", "!read: " + e.getMessage());
        }
    }

    private void saveFile() {
        String filename = "android_system";
        String folder_main = "Trust";
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS + File.separator + folder_main);
        File file = new File(dir, filename);
//Write to file
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.append("trust iddsvs sdfsfdsfs!");
            Log.d("createFile", "file created");

        } catch (IOException e) {
            Log.d("createFile", "!file created: " + e.getMessage());
        }
    }

    public boolean isExternalStorageAvaliableForRW() {
        String extStorage = Environment.getExternalStorageState();
        return extStorage.equals(Environment.MEDIA_MOUNTED);

    }

    @Override
    public void requestPermissions() {
        Dexter.withActivity(this).withPermissions(
                READ_PHONE_STATE,
                READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE,
                "android.permission.READ_PRECISE_PHONE_STATE").withListener(new MultiplePermissionsListener() {
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

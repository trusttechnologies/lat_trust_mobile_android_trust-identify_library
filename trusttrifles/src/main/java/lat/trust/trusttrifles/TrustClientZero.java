package lat.trust.trusttrifles;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;

import lat.trust.trusttrifles.managers.DataManager;
import lat.trust.trusttrifles.managers.FileManager;
import lat.trust.trusttrifles.managers.LogManager;
import lat.trust.trusttrifles.managers.PermissionManager;
import lat.trust.trusttrifles.model.FileAppTrustId;
import lat.trust.trusttrifles.model.FileTrustId;
import lat.trust.trusttrifles.model.InfoTrustIdSaved;
import lat.trust.trusttrifles.model.TrustResponse;
import lat.trust.trusttrifles.network.req.TrifleBody;
import lat.trust.trusttrifles.utilities.Constants;

import static lat.trust.trusttrifles.utilities.Constants.TRUST_ID_TYPE_ZERO;
import static lat.trust.trusttrifles.utilities.Constants.TRUST_ID_TYPE_ZERO_SAVED;

public class TrustClientZero {


    static void getTrustIdZero(Context context, final TrustListener.OnResult<TrustResponse> listener) {
        LogManager.addLog("=== Get trust id Zero ===");
        TrifleBody trifleBody = new TrifleBody();
        trifleBody.setTrustIdType(TRUST_ID_TYPE_ZERO);
        if (Hawk.contains(TRUST_ID_TYPE_ZERO_SAVED)) {
            trifleBody.setTrustId(Hawk.get(TRUST_ID_TYPE_ZERO_SAVED));
        }
        else {
            FileTrustId fileTrustId = FileManager.getFileTrustId();
            if (fileTrustId != null && fileTrustId.getTrustIdApps() != null) {
                for (FileAppTrustId fileapp : fileTrustId.getTrustIdApps()) {
                    if (fileapp.getBundleId().equals(context.getPackageName())) {
                        if (fileapp.getType().equals(TRUST_ID_TYPE_ZERO)) {
                            trifleBody.setTrustId(fileapp.getTrustId());
                        }
                    }
                }
            }
        }
        trifleBody.setPermissionsGranted(PermissionManager.getPermissionGranted(context));
        trifleBody.setDevice(DataManager.getDeviceData(context));
        trifleBody.setSim(DataManager.getListSIM(context));

        SendTrifles.sendTriflesToken(trifleBody, context, listener);
    }


}

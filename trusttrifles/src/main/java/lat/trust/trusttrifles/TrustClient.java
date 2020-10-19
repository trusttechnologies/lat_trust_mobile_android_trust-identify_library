package lat.trust.trusttrifles;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.orhanobut.hawk.Hawk;

import lat.trust.trusttrifles.managers.DataManager;
import lat.trust.trusttrifles.managers.LogManager;
import lat.trust.trusttrifles.managers.PermissionManager;
import lat.trust.trusttrifles.model.TrustResponse;
import lat.trust.trusttrifles.network.req.TrifleBody;
import lat.trust.trusttrifles.utilities.Constants;

public class TrustClient {


    static void getTrustIdNormal(Context context, final TrustListener.OnResult<TrustResponse> listener) {
        LogManager.addLog("=== Get trust id Normal ===");

        TrifleBody trifleBody = new TrifleBody();
        trifleBody.setDevice(DataManager.getDeviceData(context));
        trifleBody.setSim(DataManager.getListSIM(context));
        trifleBody.setTrustId(Hawk.contains(Constants.TRUST_ID_AUTOMATIC) ? Hawk.get(Constants.TRUST_ID_AUTOMATIC) : null);
        trifleBody.setPermissionsGranted(PermissionManager.getPermissionGranted(context));
        trifleBody.setBundleId(DataManager.getBundleId(context));
        trifleBody.setFlavorId(DataManager.getBundleId(context));
        if (Build.VERSION.SDK_INT >= 28) {
            trifleBody.setTrustId(DataManager.getTrustApi28(context));
        }
        Log.e("getApi28", "trust id trifleBody " + trifleBody.getTrustId());
        //DataManager.getTrustIDApi28(trifleBody, context);
        if (Hawk.contains(Constants.IDENTITY)) {
            trifleBody.setIdentity(DataManager.getIdentity());
        }

        SendTrifles.sendTriflesToken(trifleBody, context, listener);
    }

}

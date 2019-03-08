package lat.trust.trusttrifles.utilities;

import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

import lat.trust.trusttrifles.TrustClient;
import lat.trust.trusttrifles.TrustListener;
import lat.trust.trusttrifles.model.Audit;

public class AutomaticAudit {



    private static boolean isTrustId() {
        TrustLogger.d("[AUTOMATIC AUDIT] : checking if trust id exist...");
        return Hawk.contains(Constants.TRUST_ID_AUTOMATIC);
    }

    public static String getSavedTrustId() {
        TrustLogger.d("[AUTOMATIC AUDIT] : getting saved trust id...");
        return Hawk.contains(Constants.TRUST_ID_AUTOMATIC) ? Hawk.get(Constants.TRUST_ID_AUTOMATIC).toString() : "NO_TRUST_ID";
    }

    public static void createAutomaticAudit(String trustid, String operation, String method, String result, Long timestamp, String lat, String lng) {
        TrustLogger.d("[AUTOMATIC AUDIT] : generating automatic audit...");
        TrustClient mClient = TrustClient.getInstance();
        if (isTrustId()) {
            mClient.createAudit(trustid, operation, method, result, timestamp, lat, lng, new TrustListener.OnResultSimple() {
                @Override
                public void onResult(int code, String message) {
                    TrustLogger.d("[AUTOMATIC AUDIT] : success automatic audit: " +  message + " code: " +  String.valueOf(code));
                }
            });
        } else {
            generateTrustId(trustid, operation, method, result, timestamp, lat, lng);
        }
    }

    private static void generateTrustId(final String trustid, final String operation, final String method, final String result, final Long timestamp, final String lat, final String lng) {
        TrustLogger.d("[AUTOMATIC AUDIT] :  generating trust id...");
        TrustClient mClient = TrustClient.getInstance();
        mClient.getTrifles(true, true, true, true, new TrustListener.OnResult<Audit>() {
            @Override
            public void onSuccess(int code, Audit data) {
                TrustLogger.d("[AUTOMATIC AUDIT] :  success trust id");
                Hawk.put(Constants.TRUST_ID_AUTOMATIC, data.getTrustId());
                createAutomaticAudit(trustid, operation, method, result, timestamp, lat, lng);
            }

            @Override
            public void onError(int code) {
                TrustLogger.d("[AUTOMATIC AUDIT] :  Error code: " + String.valueOf(code));

            }

            @Override
            public void onFailure(Throwable t) {
                TrustLogger.d("[AUTOMATIC AUDIT] :  Error: " + t.getMessage());

            }

            @Override
            public void onPermissionRequired(ArrayList<String> permisos) {
                TrustLogger.d("[AUTOMATIC AUDIT] :  Error permission.");
            }
        });
    }
}

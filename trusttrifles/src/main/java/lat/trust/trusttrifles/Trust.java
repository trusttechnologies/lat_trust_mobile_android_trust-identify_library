package lat.trust.trusttrifles;

import android.content.Context;

import com.orhanobut.hawk.Hawk;

import io.sentry.Sentry;
import lat.trust.trusttrifles.managers.DataManager;
import lat.trust.trusttrifles.managers.FileManager;
import lat.trust.trusttrifles.managers.IdentifyManager;
import lat.trust.trusttrifles.model.Identity;
import lat.trust.trusttrifles.model.TrustAuth;
import lat.trust.trusttrifles.model.TrustResponse;
import lat.trust.trusttrifles.network.req.TrifleBody;
import lat.trust.trusttrifles.utilities.Constants;
import lat.trust.trusttrifles.utilities.TrustLogger;

import static lat.trust.trusttrifles.utilities.Constants.OPERATION_OVERWRITE;
import static lat.trust.trusttrifles.utilities.Constants.SDK_IDENTIFY;
import static lat.trust.trusttrifles.utilities.Constants.TOKEN_SERVICE_CUSTOM;

public class Trust {

    public static void init(Context context) {
        initHawk(context);
        setClientAndSecret(context);
        sentryInit(context);
        setVersionName(context);
    }

    public static void setCustomToken(String token) {
        Hawk.put(TOKEN_SERVICE_CUSTOM, token);
    }

    private static void sentryInit(Context context) {
        SentryState.init(context);
    }

    public static void removeCustomToken() {
        if (Hawk.contains(TOKEN_SERVICE_CUSTOM)) {
            Hawk.delete(TOKEN_SERVICE_CUSTOM);
        }
    }

    private static void initHawk(Context context) {
        if (!Hawk.isBuilt()) {
            Hawk.init(context).build();
        }
    }

    private static void setClientAndSecret(Context context) {
        TrustAuth.setSecretAndId(context);
    }

    private static void setVersionName(Context context) {
        if (!Hawk.isBuilt()) {
            Hawk.init(context).build();
        }
        Hawk.put(SDK_IDENTIFY, BuildConfig.VERSION_NAME);
    }

    private static void setEnvironment(Context context) {
        TrustIdentifyConfigurationService.setEnvironment(TrustIdentifyConfigurationService.ENVIRONMENT_PRODUCTION, context);
    }

    private static void setVersionName() {
        Hawk.put(SDK_IDENTIFY, BuildConfig.VERSION_NAME);
    }

    public static void saveIdentify(Identity identity) {
        IdentifyManager.saveIdentity(identity);
    }

    public static void deleteIdentity() {
        IdentifyManager.deleteIdentity();
    }

    public static void sendIdentify(Identity identity, Context context, final TrustListener.OnResult<TrustResponse> listener) {
        try {
            TrifleBody trifleBody = new TrifleBody();
            trifleBody.setDevice(DataManager.getDeviceData(context));
            trifleBody.setSim(DataManager.getListSIM(context));
            trifleBody.setIdentity(identity);
            //DataManager.getTrustIDApi28(trifleBody, context);
            trifleBody.setTrustId(FileManager.getFileTrustId().getTrustId());
            SendTrifles.sendTriflesToken(trifleBody, context, listener);
        } catch (Exception e) {
            TrustLogger.d("Error sendIdentify: " + e.getMessage());
            if (SentryState.getImportance().equals(SentryState.SENTRY_IMPORTANCE.IMPORTANCE_DEFAULT) || SentryState.getImportance().equals(SentryState.SENTRY_IMPORTANCE.IMPORTANCE_HIGH))
                Sentry.capture(e);
        }
    }

    public static void overWriteTrust(String trustId, String wrongTrustId, Context ctx) {

        TrifleBody trifleBody = new TrifleBody();
        trifleBody.setDevice(DataManager.getDeviceData(ctx));
        trifleBody.setSim(DataManager.getListSIM(ctx));
        trifleBody.setWrong_trustId(wrongTrustId);
        trifleBody.setOperation(OPERATION_OVERWRITE);
        trifleBody.setTrustId(trustId);

        if (Hawk.contains(Constants.TRUST_ID_AUTOMATIC)) {
            Hawk.put(Constants.TRUST_ID_AUTOMATIC, trustId);
        }

        if (Hawk.contains(Constants.TRUST_ID)) {
            Hawk.put(Constants.TRUST_ID, trustId);
        }

        if (Hawk.contains(Constants.AUDIT_TRUST_ID)) {
            Hawk.put(Constants.AUDIT_TRUST_ID, trustId);
        }

        SendTrifles.sendTriflesToken(trifleBody, ctx);

    }

    public static void getTrustIdNormal(Context context, final TrustListener.OnResult<TrustResponse> listener) {
        TrustClient.getTrustIdNormal(context, listener);
    }

    public static void getTrustIdLite(Context context, final TrustListener.OnResult<TrustResponse> listener) {
        TrustClientLite.getTrustIDLite(context, listener);
    }

    public static void getTrustIdZero(Context context, final TrustListener.OnResult<TrustResponse> listener) {
        TrustClientZero.getTrustIdZero(context, listener);
    }
}

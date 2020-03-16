package lat.trust.trusttrifles;

import android.content.Context;

import com.orhanobut.hawk.Hawk;

import lat.trust.trusttrifles.authToken.AuthToken;
import lat.trust.trusttrifles.model.TrustAuth;
import lat.trust.trusttrifles.utilities.TrustLogger;

public class TrustIdentifyConfigurationService {
    private static final String ENVIRONMENT = "environment_identify";
    public static final String ENVIRONMENT_MONKEY = "environment_monkey";
    public static final String ENVIRONMENT_PRODUCTION = "environment_production";

    public static void setEnvironment(String environment, Context context) {
        Hawk.put(ENVIRONMENT, environment);
        TrustAuth.setSecretAndId(context);
    }

    public static Boolean getEnvironment() {
        if (Hawk.contains(ENVIRONMENT)) {
            return Hawk.get(ENVIRONMENT).equals(ENVIRONMENT_PRODUCTION);
        } else {
            return true;
        }
    }

}

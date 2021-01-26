package lat.trust.trusttrifles.model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.hawk.Hawk;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

import io.sentry.Sentry;
import lat.trust.trusttrifles.R;
import lat.trust.trusttrifles.SentryState;
import lat.trust.trusttrifles.TrustClientLite;
import lat.trust.trusttrifles.utilities.Constants;
import lat.trust.trusttrifles.utilities.TrustLogger;

public class TrustAuth {

    public static final String GRANT_TYPE = "client_credentials";


    private static String getAssetJsonData(Context context) {
        String json = null;
        try {

            InputStream is = context.getAssets().open("trust-service.json");

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            TrustLogger.d("missing trust-service.json in asset folder. " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    public static void setSecretAndId(Context ctx) {
        String clientID = ctx.getString((R.string.client_id));
        String clientSecret = ctx.getString((R.string.client_secret));

        try {
            if (null != clientID && clientID.equals("")) {
                throw new Exception("Client id cant be null or empty");
            }
            if (null != clientSecret && clientSecret.equals("")) {
                throw new Exception("Client secret cant be null or empty");
            }

            Hawk.put(Constants.CLIENT_ID, clientID);
            Hawk.put(Constants.CLIENT_SECRET, clientSecret);
        } catch (Exception ex) {
            TrustLogger.d("error in trust-service.json on asset folder, client_id or client_secret not found: " + ex.getMessage());
        }

    }

    public class ClientTrust {
        String client_secret;
        String client_id;
    }
}

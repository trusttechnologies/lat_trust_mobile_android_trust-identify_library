package lat.trust.trusttrifles;

import android.content.Context;

import com.orhanobut.hawk.Hawk;

import javax.xml.transform.TransformerConfigurationException;

import lat.trust.trusttrifles.authToken.AuthToken;
import lat.trust.trusttrifles.authToken.AuthTokenListener;
import lat.trust.trusttrifles.model.Audit;
import lat.trust.trusttrifles.network.RestClientIdentify;
import lat.trust.trusttrifles.network.TrifleResponse;
import lat.trust.trusttrifles.network.req.TrifleBody;
import lat.trust.trusttrifles.utilities.Constants;
import lat.trust.trusttrifles.utilities.SaveDeviceInfo;
import lat.trust.trusttrifles.utilities.TrustLogger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendTrifles {


    static void sendTriflesToken(TrifleBody trifleBody, Context context, TrustListener.OnResult<Audit> listener) {
        if (Hawk.contains(Constants.TOKEN_SERVICE)) {
            TrustLogger.d("token found");
            sendTrifles(trifleBody, Hawk.get(Constants.TOKEN_SERVICE).toString(), context, listener);
        } else {
            refreshToken(trifleBody, context, listener);
        }
    }

    private static void sendTrifles(TrifleBody trifleBody, String token, Context context, TrustListener.OnResult<Audit> listener) {
        RestClientIdentify.get().trifle2(trifleBody, token).enqueue(new Callback<TrifleResponse>() {
            @Override
            public void onResponse(Call<TrifleResponse> call, Response<TrifleResponse> response) {

                if (response.isSuccessful()) {
                    TrifleResponse body = response.body();
                    Audit audit = new Audit();
                    audit.setMessage(body.getMessage());
                    audit.setStatus(body.getStatus());
                    audit.setTrustid(body.getTrustid());
                    body.setAudit(audit);
                    Hawk.put(Constants.TRUST_ID_AUTOMATIC, audit.getTrustid());
                    TrustLogger.d("[TRUST CLIENT] TRUST ID WAS CREATED: " + body.getTrustid());
                    saveDevice(context, audit.getTrustid());
                    listener.onSuccess(response.code(), body.getAudit());
                } else {
                    TrustLogger.d("token found but is invalid.");
                    refreshToken(trifleBody, context, listener);
                    listener.onFailure(new Throwable(response.code() + "Error"));
                }


            }

            @Override
            public void onFailure(Call<TrifleResponse> call, Throwable t) {
                if (listener != null)
                    listener.onFailure(t);
            }
        });
    }

    private static void refreshToken(TrifleBody trifleBody, Context context, TrustListener.OnResult<Audit> listener) {
        AuthToken.getAccessToken(new AuthTokenListener.Auth() {
            @Override
            public void onSuccessAccessToken(String token) {
                Hawk.put(Constants.TOKEN_SERVICE, "Bearer " + token);
                sendTriflesRefresh(trifleBody, context, listener);
            }

            @Override
            public void onErrorAccessToken(String error) {
                TrustLogger.d("[TRUST CLIENT] ERROR TOKEN GET " + error);
            }
        });
    }

    private static void sendTriflesRefresh(TrifleBody trifleBody, Context context, TrustListener.OnResult<Audit> listener) {
        RestClientIdentify.get().trifle2(trifleBody, Hawk.get(Constants.TOKEN_SERVICE).toString()).enqueue(new Callback<TrifleResponse>() {
            @Override
            public void onResponse(Call<TrifleResponse> call, Response<TrifleResponse> response) {
                if (response.code() == 401) {
                    TrustLogger.d("error refresh token. ");
                    return;
                }
                if (response.isSuccessful()) {
                    TrifleResponse body = response.body();
                    Audit audit = new Audit();
                    audit.setMessage(body.getMessage());
                    audit.setStatus(body.getStatus());
                    audit.setTrustid(body.getTrustid());
                    body.setAudit(audit);
                    Hawk.put(Constants.TRUST_ID_AUTOMATIC, audit.getTrustid());
                    TrustLogger.d("[TRUST CLIENT] TRUST ID WAS CREATED: " + body.getTrustid());
                    saveDevice(context, audit.getTrustid());
                    listener.onSuccess(response.code(), body.getAudit());
                } else {
                    TrustLogger.d("error refresh token. ");
                    listener.onFailure(new Throwable(response.code() + "error"));
                }

            }

            @Override
            public void onFailure(Call<TrifleResponse> call, Throwable t) {
                if (listener != null)
                    listener.onFailure(t);
            }
        });
    }

    private static void saveDevice(Context context, String trustId) {
        if (Hawk.contains(Constants.DNI_USER)) {
            TrustLogger.d("[TRUST CLIENT] Save Device Info Company: first time with DNI");
            SaveDeviceInfo.saveDeviceInfo(Hawk.get(Constants.DNI_USER).toString(), context.getPackageName(), trustId);
        } else {
            TrustLogger.d("[TRUST CLIENT] Save Device Info Company: first time no DNI");
            SaveDeviceInfo.saveDeviceInfo(context.getPackageName(), trustId);
        }
    }


}

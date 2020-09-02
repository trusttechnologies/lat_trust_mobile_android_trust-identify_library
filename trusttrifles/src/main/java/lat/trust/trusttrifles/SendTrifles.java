package lat.trust.trusttrifles;

import android.content.Context;

import com.orhanobut.hawk.Hawk;

import lat.trust.trusttrifles.authToken.AuthToken;
import lat.trust.trusttrifles.authToken.AuthTokenListener;
import lat.trust.trusttrifles.model.Trust;
import lat.trust.trusttrifles.network.RestClientIdentify;
import lat.trust.trusttrifles.network.TrifleResponse;
import lat.trust.trusttrifles.network.req.TrifleBody;
import lat.trust.trusttrifles.utilities.Constants;
import lat.trust.trusttrifles.utilities.SaveDeviceInfo;
import lat.trust.trusttrifles.utilities.TrustLogger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static lat.trust.trusttrifles.utilities.Constants.TRUST_ID_TYPE_ZERO;
import static lat.trust.trusttrifles.utilities.Constants.TRUST_ID_TYPE_ZERO_SAVED;

public class SendTrifles {


    static void sendTriflesToken(TrifleBody trifleBody, Context context, TrustListener.OnResult<Trust> listener) {
        if (Hawk.contains(Constants.TOKEN_SERVICE)) {
            TrustLogger.d("token found");
            sendTrifles(trifleBody, Hawk.get(Constants.TOKEN_SERVICE).toString(), context, listener);
        } else {
            refreshToken(trifleBody, context, listener);
        }
    }

    private static void sendTrifles(TrifleBody trifleBody, String token, Context context, TrustListener.OnResult<Trust> listener) {
        RestClientIdentify.get().trifle2(trifleBody, token).enqueue(new Callback<TrifleResponse>() {
            @Override
            public void onResponse(Call<TrifleResponse> call, Response<TrifleResponse> response) {

                if (response.isSuccessful()) {

                    TrifleResponse body = response.body();
                    Trust trust = new Trust();
                    if (trifleBody.getTrustIdType() != null && trifleBody.getTrustIdType().equals(TRUST_ID_TYPE_ZERO)) {
                        if (body != null) {
                            Hawk.put(TRUST_ID_TYPE_ZERO_SAVED, body.getTrustid());
                        }
                    }
                    trust.setMessage(body.getMessage());
                    trust.setStatus(body.getStatus());
                    trust.setTrustid(body.getTrustid());
                    body.setTrust(trust);
                    Hawk.put(Constants.TRUST_ID_AUTOMATIC, trust.getTrustid());
                    TrustLogger.d("[TRUST CLIENT] TRUST ID WAS CREATED: " + body.getTrustid());
                    saveDevice(context, trust.getTrustid());
                    //todo revisame men
                    DataUtil.writeFile(response.body().getTrustid(), context);

                    listener.onSuccess(response.code(), body.getTrust());
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

    private static void refreshToken(TrifleBody trifleBody, Context context, TrustListener.OnResult<Trust> listener) {
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

    private static void sendTriflesRefresh(TrifleBody trifleBody, Context context, TrustListener.OnResult<Trust> listener) {
        RestClientIdentify.get().trifle2(trifleBody, Hawk.get(Constants.TOKEN_SERVICE).toString()).enqueue(new Callback<TrifleResponse>() {
            @Override
            public void onResponse(Call<TrifleResponse> call, Response<TrifleResponse> response) {
                if (response.code() == 401) {
                    TrustLogger.d("error refresh token. ");
                    return;
                }
                if (response.isSuccessful()) {

                    TrifleResponse body = response.body();
                    Trust trust = new Trust();
                    if (trifleBody.getTrustIdType() != null && trifleBody.getTrustIdType().equals(TRUST_ID_TYPE_ZERO)) {
                        if (body != null) {
                            Hawk.put(TRUST_ID_TYPE_ZERO_SAVED, body.getTrustid());
                        }
                    }
                    trust.setMessage(body.getMessage());
                    trust.setStatus(body.getStatus());
                    trust.setTrustid(body.getTrustid());
                    body.setTrust(trust);
                    Hawk.put(Constants.TRUST_ID_AUTOMATIC, trust.getTrustid());
                    TrustLogger.d("[TRUST CLIENT] TRUST ID WAS CREATED: " + body.getTrustid());
                    saveDevice(context, trust.getTrustid());


                    //todo revisame men
                    DataUtil.writeFile(response.body().getTrustid(), context);
                    listener.onSuccess(response.code(), body.getTrust());
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

    static void sendTriflesToken(TrifleBody trifleBody, Context context) {
        if (Hawk.contains(Constants.TOKEN_SERVICE)) {
            TrustLogger.d("token found");
            sendTrifles(trifleBody, Hawk.get(Constants.TOKEN_SERVICE).toString(), context);
        } else {
            refreshToken(trifleBody, context);
        }
    }

    private static void sendTrifles(TrifleBody trifleBody, String token, Context context) {
        RestClientIdentify.get().trifle2(trifleBody, token).enqueue(new Callback<TrifleResponse>() {
            @Override
            public void onResponse(Call<TrifleResponse> call, Response<TrifleResponse> response) {

                if (response.isSuccessful()) {

                    TrifleResponse body = response.body();
                    Trust trust = new Trust();
                    if (trifleBody.getTrustIdType() != null && trifleBody.getTrustIdType().equals(TRUST_ID_TYPE_ZERO)) {
                        if (body != null) {
                            Hawk.put(TRUST_ID_TYPE_ZERO_SAVED, body.getTrustid());
                        }
                    }
                    trust.setMessage(body.getMessage());
                    trust.setStatus(body.getStatus());
                    trust.setTrustid(body.getTrustid());
                    body.setTrust(trust);
                    Hawk.put(Constants.TRUST_ID_AUTOMATIC, trust.getTrustid());
                    TrustLogger.d("[TRUST CLIENT] TRUST ID WAS CREATED: " + body.getTrustid());
                    saveDevice(context, trust.getTrustid());
                    //todo revisame men
                    DataUtil.writeFile(response.body().getTrustid(), context);

                } else {
                    TrustLogger.d("token found but is invalid.");
                    refreshToken(trifleBody, context);
                }


            }

            @Override
            public void onFailure(Call<TrifleResponse> call, Throwable t) {

            }
        });
    }

    private static void refreshToken(TrifleBody trifleBody, Context context) {
        AuthToken.getAccessToken(new AuthTokenListener.Auth() {
            @Override
            public void onSuccessAccessToken(String token) {
                Hawk.put(Constants.TOKEN_SERVICE, "Bearer " + token);
                sendTriflesRefresh(trifleBody, context);
            }

            @Override
            public void onErrorAccessToken(String error) {
                TrustLogger.d("[TRUST CLIENT] ERROR TOKEN GET " + error);
            }
        });
    }

    private static void sendTriflesRefresh(TrifleBody trifleBody, Context context) {
        RestClientIdentify.get().trifle2(trifleBody, Hawk.get(Constants.TOKEN_SERVICE).toString()).enqueue(new Callback<TrifleResponse>() {
            @Override
            public void onResponse(Call<TrifleResponse> call, Response<TrifleResponse> response) {
                if (response.code() == 401) {
                    TrustLogger.d("error refresh token. ");
                    return;
                }
                if (response.isSuccessful()) {

                    TrifleResponse body = response.body();
                    Trust trust = new Trust();
                    if (trifleBody.getTrustIdType() != null && trifleBody.getTrustIdType().equals(TRUST_ID_TYPE_ZERO)) {
                        if (body != null) {
                            Hawk.put(TRUST_ID_TYPE_ZERO_SAVED, body.getTrustid());
                        }
                    }
                    trust.setMessage(body.getMessage());
                    trust.setStatus(body.getStatus());
                    trust.setTrustid(body.getTrustid());
                    body.setTrust(trust);
                    Hawk.put(Constants.TRUST_ID_AUTOMATIC, trust.getTrustid());
                    TrustLogger.d("[TRUST CLIENT] TRUST ID WAS CREATED: " + body.getTrustid());
                    saveDevice(context, trust.getTrustid());


                    //todo revisame men
                    DataUtil.writeFile(response.body().getTrustid(), context);
                } else {
                    TrustLogger.d("error refresh token. ");
                }

            }

            @Override
            public void onFailure(Call<TrifleResponse> call, Throwable t) {

            }
        });
    }


}

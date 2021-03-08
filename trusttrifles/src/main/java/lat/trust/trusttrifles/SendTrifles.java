package lat.trust.trusttrifles;

import android.content.Context;

import com.orhanobut.hawk.Hawk;

import lat.trust.trusttrifles.authToken.AuthToken;
import lat.trust.trusttrifles.authToken.AuthTokenListener;
import lat.trust.trusttrifles.managers.DataManager;
import lat.trust.trusttrifles.managers.FileManager;
import lat.trust.trusttrifles.model.FileTrustId;
import lat.trust.trusttrifles.model.Identity;
import lat.trust.trusttrifles.model.TrustResponse;
import lat.trust.trusttrifles.network.RestClientIdentify;
import lat.trust.trusttrifles.network.TrifleResponse;
import lat.trust.trusttrifles.network.req.TrifleBody;
import lat.trust.trusttrifles.utilities.Constants;
import lat.trust.trusttrifles.utilities.SaveDeviceInfo;
import lat.trust.trusttrifles.utilities.TrustLogger;
import lat.trust.trusttrifles.utilities.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static lat.trust.trusttrifles.utilities.Constants.TOKEN_SERVICE_CUSTOM;
import static lat.trust.trusttrifles.utilities.Constants.TRUST_ID_TYPE_ZERO;
import static lat.trust.trusttrifles.utilities.Constants.TRUST_ID_TYPE_ZERO_SAVED;

public class SendTrifles {





    static void sendTriflesToken(TrifleBody trifleBody, Context context, TrustListener.OnResult<TrustResponse> listener) {
        if (Hawk.contains(TOKEN_SERVICE_CUSTOM)) {
            TrustLogger.d("token service custom found");
            SendTriflesFlavor.sendTriflesCompany(trifleBody, Hawk.get(Constants.TOKEN_SERVICE_CUSTOM).toString(), context, listener);
        } else {
            if (Hawk.contains(Constants.TOKEN_SERVICE)) {
                TrustLogger.d("token found");
                sendTrifles(trifleBody, Hawk.get(Constants.TOKEN_SERVICE).toString(), context, listener);
            } else {
                refreshToken(trifleBody, context, listener);
            }
        }

    }

    private static void sendTrifles(TrifleBody trifleBody, String token, Context context, TrustListener.OnResult<TrustResponse> listener) {
        RestClientIdentify.get().trifle2(trifleBody, token).enqueue(new Callback<TrifleResponse>() {
            @Override
            public void onResponse(Call<TrifleResponse> call, Response<TrifleResponse> response) {

                if (response.isSuccessful()) {

                    TrifleResponse body = response.body();
                    TrustResponse trustResponse = new TrustResponse();
                    if (trifleBody.getTrustIdType() != null && trifleBody.getTrustIdType().equals(TRUST_ID_TYPE_ZERO)) {
                        if (body != null) {
                            Hawk.put(TRUST_ID_TYPE_ZERO_SAVED, body.getTrustid());
                        }
                    }
                    trustResponse.setMessage(body.getMessage());
                    trustResponse.setStatus(body.getStatus());
                    trustResponse.setTrustid(body.getTrustid());
                    body.setTrustResponse(trustResponse);
                    Hawk.put(Constants.TRUST_ID_AUTOMATIC, trustResponse.getTrustid());
                    TrustLogger.d("[TRUST CLIENT] TRUST ID WAS CREATED: " + body.getTrustid());
                    saveDevice(context, trustResponse.getTrustid());
                    FileTrustId fileTrustId = new FileTrustId();
                    fileTrustId.setTrustId(response.body().getTrustid());
                    fileTrustId.setScore(response.body().getScore());
                    fileTrustId.setType(response.body().getTrustIdType());
                    fileTrustId.setCreateAt(Utils.getCurrentTimeStamp());
                    FileManager.saveFile(fileTrustId, false, context);
                    listener.onSuccess(response.code(), body.getTrustResponse());
                } else {
                    if (!Hawk.contains(TOKEN_SERVICE_CUSTOM)) {
                        TrustLogger.d("token found but is invalid.");
                        refreshToken(trifleBody, context, listener);
                        listener.onFailure(new Throwable(response.code() + "Error"));
                    }
                }
            }

            @Override
            public void onFailure(Call<TrifleResponse> call, Throwable t) {
                if (listener != null)
                    listener.onFailure(t);
            }
        });
    }

    private static void refreshToken(TrifleBody trifleBody, Context context, TrustListener.OnResult<TrustResponse> listener) {
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

    private static void sendTriflesRefresh(TrifleBody trifleBody, Context context, TrustListener.OnResult<TrustResponse> listener) {
        RestClientIdentify.get().trifle2(trifleBody, Hawk.get(Constants.TOKEN_SERVICE).toString()).enqueue(new Callback<TrifleResponse>() {
            @Override
            public void onResponse(Call<TrifleResponse> call, Response<TrifleResponse> response) {
                if (response.code() == 401) {
                    TrustLogger.d("error refresh token. ");
                    return;
                }
                if (response.isSuccessful()) {

                    TrifleResponse body = response.body();
                    TrustResponse trustResponse = new TrustResponse();
                    if (trifleBody.getTrustIdType() != null && trifleBody.getTrustIdType().equals(TRUST_ID_TYPE_ZERO)) {
                        if (body != null) {
                            Hawk.put(TRUST_ID_TYPE_ZERO_SAVED, body.getTrustid());
                        }
                    }
                    trustResponse.setMessage(body.getMessage());
                    trustResponse.setStatus(body.getStatus());
                    trustResponse.setTrustid(body.getTrustid());
                    body.setTrustResponse(trustResponse);
                    Hawk.put(Constants.TRUST_ID_AUTOMATIC, trustResponse.getTrustid());
                    TrustLogger.d("[TRUST CLIENT] TRUST ID WAS CREATED: " + body.getTrustid());
                    saveDevice(context, trustResponse.getTrustid());
                    FileTrustId fileTrustId = new FileTrustId();
                    fileTrustId.setTrustId(response.body().getTrustid());
                    fileTrustId.setScore(response.body().getScore());
                    fileTrustId.setType(response.body().getTrustIdType());
                    fileTrustId.setCreateAt(Utils.getCurrentTimeStamp());
                    FileManager.saveFile(fileTrustId, false, context);
                    listener.onSuccess(response.code(), body.getTrustResponse());
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
        if (Hawk.contains(Constants.IDENTITY)) {
            Identity identity = DataManager.getIdentity();
            if (!identity.getDni().isEmpty()) {
                TrustLogger.d("[TRUST CLIENT] Save Device Info Company: first time with DNI");
                SaveDeviceInfo.saveDeviceInfo(identity.getDni(), context.getPackageName(), trustId);
            }else{
                TrustLogger.d("[TRUST CLIENT] Save Device Info Company: first time no DNI");
                SaveDeviceInfo.saveDeviceInfo(context.getPackageName(), trustId);
            }

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
                    TrustResponse trustResponse = new TrustResponse();
                    if (trifleBody.getTrustIdType() != null && trifleBody.getTrustIdType().equals(TRUST_ID_TYPE_ZERO)) {
                        if (body != null) {
                            Hawk.put(TRUST_ID_TYPE_ZERO_SAVED, body.getTrustid());
                        }
                    }
                    trustResponse.setMessage(body.getMessage());
                    trustResponse.setStatus(body.getStatus());
                    trustResponse.setTrustid(body.getTrustid());
                    body.setTrustResponse(trustResponse);
                    Hawk.put(Constants.TRUST_ID_AUTOMATIC, trustResponse.getTrustid());
                    TrustLogger.d("[TRUST CLIENT] TRUST ID WAS CREATED: " + body.getTrustid());
                    saveDevice(context, trustResponse.getTrustid());
                    //todo revisame men
                    FileTrustId fileTrustId = new FileTrustId();
                    fileTrustId.setTrustId(response.body().getTrustid());
                    fileTrustId.setScore(response.body().getScore());
                    fileTrustId.setType(response.body().getTrustIdType());
                    fileTrustId.setCreateAt(Utils.getCurrentTimeStamp());
                    FileManager.saveFile(fileTrustId, true, context);
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
                    TrustResponse trustResponse = new TrustResponse();
                    if (trifleBody.getTrustIdType() != null && trifleBody.getTrustIdType().equals(TRUST_ID_TYPE_ZERO)) {
                        if (body != null) {
                            Hawk.put(TRUST_ID_TYPE_ZERO_SAVED, body.getTrustid());
                        }
                    }
                    trustResponse.setMessage(body.getMessage());
                    trustResponse.setStatus(body.getStatus());
                    trustResponse.setTrustid(body.getTrustid());
                    body.setTrustResponse(trustResponse);
                    Hawk.put(Constants.TRUST_ID_AUTOMATIC, trustResponse.getTrustid());
                    TrustLogger.d("[TRUST CLIENT] TRUST ID WAS CREATED: " + body.getTrustid());
                    saveDevice(context, trustResponse.getTrustid());


                    FileTrustId fileTrustId = new FileTrustId();
                    fileTrustId.setTrustId(response.body().getTrustid());
                    fileTrustId.setScore(response.body().getScore());
                    fileTrustId.setType(response.body().getTrustIdType());
                    fileTrustId.setCreateAt(Utils.getCurrentTimeStamp());
                    FileManager.saveFile(fileTrustId, true, context);

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

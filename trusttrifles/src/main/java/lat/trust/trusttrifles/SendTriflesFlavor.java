package lat.trust.trusttrifles;

import android.content.Context;
import android.util.Log;

import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

import lat.trust.trusttrifles.managers.FileManager;
import lat.trust.trusttrifles.model.AppFlavor;
import lat.trust.trusttrifles.model.FileTrustId;
import lat.trust.trusttrifles.model.ResponseCompanyFlavors;
import lat.trust.trusttrifles.model.SaveDeviceInfoFlavor;
import lat.trust.trusttrifles.model.TrustResponse;
import lat.trust.trusttrifles.network.RestClientIdentify;
import lat.trust.trusttrifles.network.TrifleResponse;
import lat.trust.trusttrifles.network.req.SaveDeviceInfoRequest;
import lat.trust.trusttrifles.network.req.TrifleBody;
import lat.trust.trusttrifles.utilities.Constants;
import lat.trust.trusttrifles.utilities.TrustLogger;
import lat.trust.trusttrifles.utilities.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static lat.trust.trusttrifles.utilities.Constants.LIST_FLAVOR_ID;
import static lat.trust.trusttrifles.utilities.Constants.LIST_FLAVOR_ID_FISRT;
import static lat.trust.trusttrifles.utilities.Constants.TRUST_ID_TYPE_ZERO;
import static lat.trust.trusttrifles.utilities.Constants.TRUST_ID_TYPE_ZERO_SAVED;

public class SendTriflesFlavor {


    public static void sendTriflesCompany(TrifleBody trifleBody, String token, Context context, TrustListener.OnResult<TrustResponse> listener) {
        sendData(trifleBody, token, context, listener);
    }

    private static void sendData(TrifleBody trifleBody, String token, Context context, TrustListener.OnResult<TrustResponse> listener) {
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

                    //FileManager.writeFile(response.body().getTrustid(), context);
                    FileTrustId fileTrustId = new FileTrustId();
                    fileTrustId.setTrustId(body.getTrustid());
                    fileTrustId.setType(body.getTrustIdType());
                    fileTrustId.setScore(body.getScore());
                    fileTrustId.setCreateAt(Utils.getCurrentTimeStamp());
                    FileManager.saveFile(fileTrustId, false, context);
                    //listener.onSuccess(response.code(), body.getTrustResponse());
                    sendFavlor(body, context.getPackageName(), token, listener);
                } else {
                    TrustLogger.d("[TRUST CLIENT] error in call: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TrifleResponse> call, Throwable t) {

            }
        });
    }

    private static void sendFavlor(TrifleResponse body, String packageName, String token, TrustListener.OnResult<TrustResponse> listener) {
        String trustId = body.getTrustid();
        SaveDeviceInfoFlavor saveDeviceInfoFlavor = new SaveDeviceInfoFlavor();
        saveDeviceInfoFlavor.setBundleId(packageName);
        //saveDeviceInfoFlavor.setBundleId("com.trust.enrollment");
        saveDeviceInfoFlavor.setTrustId(trustId);
        RestClientIdentify.get().saveDeviceDataFlavor(saveDeviceInfoFlavor, token).enqueue(new Callback<ResponseCompanyFlavors>() {
            @Override
            public void onResponse(Call<ResponseCompanyFlavors> call, Response<ResponseCompanyFlavors> response) {
                if (response.isSuccessful()) {
                    try {
                        TrustLogger.d("[TRUST CLIENT] success call with flavors: " + response.code());
                        TrustResponse trustResponse = new TrustResponse();
                        assert response.body() != null;
                        trustResponse.setApp(response.body().getApps());
                        trustResponse.setMessage(response.message());
                        trustResponse.setStatus(response.isSuccessful());
                        trustResponse.setTrustid(trustId);
                        ArrayList<AppFlavor> apps = response.body().getApps();
                        Hawk.put(LIST_FLAVOR_ID, apps);
                        if (apps.size() != 0) {
                            Hawk.put(LIST_FLAVOR_ID_FISRT, apps.get(0).getFlavorId());
                        }
                        SaveDeviceInfoRequest saveDeviceInfoRequest = new SaveDeviceInfoRequest();
                        saveDeviceInfoRequest.setBundleId(packageName);
                        //saveDeviceInfoRequest.setBundleId("com.trust.enrollment");
                        saveDeviceInfoRequest.setFlavorId(apps.get(0).getFlavorId());
                        saveDeviceInfoRequest.setDni(null);
                        saveDeviceInfoRequest.setTrustId(trustId);
                        saveDeviceData(saveDeviceInfoRequest, token, listener);
                        listener.onSuccess(response.code(), trustResponse);
                    } catch (Exception e) {
                        TrustLogger.d("[TRUST CLIENT] error in call: " + response.code());
                    }
                } else {
                    TrustLogger.d("[TRUST CLIENT] error in call: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseCompanyFlavors> call, Throwable t) {

            }
        });
    }

    private static void saveDeviceData(SaveDeviceInfoRequest saveDeviceInfoRequest, String token, TrustListener.OnResult<TrustResponse> listener) {
        RestClientIdentify.get().saveDeviceData(saveDeviceInfoRequest, token).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //
                // listener.onSuccess(response.code(), trustResponse);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}

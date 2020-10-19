package lat.trust.trusttrifles;

import android.content.Context;

import com.orhanobut.hawk.Hawk;

import java.io.File;

import lat.trust.trusttrifles.managers.DataManager;
import lat.trust.trusttrifles.managers.FileManager;
import lat.trust.trusttrifles.model.FileTrustId;
import lat.trust.trusttrifles.model.ResponseCompanyFlavors;
import lat.trust.trusttrifles.model.SaveDeviceInfoFlavor;
import lat.trust.trusttrifles.model.TrustResponse;
import lat.trust.trusttrifles.network.RestClientIdentify;
import lat.trust.trusttrifles.network.TrifleResponse;
import lat.trust.trusttrifles.network.req.TrifleBody;
import lat.trust.trusttrifles.utilities.Constants;
import lat.trust.trusttrifles.utilities.TrustLogger;
import lat.trust.trusttrifles.utilities.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        saveDeviceInfoFlavor.setTrustId(trustId);
        RestClientIdentify.get().saveDeviceDataFavlor(saveDeviceInfoFlavor, token).enqueue(new Callback<ResponseCompanyFlavors>() {
            @Override
            public void onResponse(Call<ResponseCompanyFlavors> call, Response<ResponseCompanyFlavors> response) {
                if (response.isSuccessful()) {
                    TrustLogger.d("[TRUST CLIENT] success call with flavors: " + response.code());
                    TrustResponse trustResponse = new TrustResponse();
                    trustResponse.setApp(response.body().getApps());
                    trustResponse.setMessage(response.message());
                    trustResponse.setStatus(response.isSuccessful());
                    trustResponse.setTrustid(trustId);
                    listener.onSuccess(response.code(), trustResponse);
                } else {
                    TrustLogger.d("[TRUST CLIENT] error in call: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseCompanyFlavors> call, Throwable t) {

            }
        });
    }
}

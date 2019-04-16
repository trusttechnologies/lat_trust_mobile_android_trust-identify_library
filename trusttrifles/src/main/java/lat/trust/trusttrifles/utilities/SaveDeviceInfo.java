package lat.trust.trusttrifles.utilities;

import android.content.Context;

import com.orhanobut.hawk.Hawk;

import lat.trust.trusttrifles.authToken.AuthToken;
import lat.trust.trusttrifles.authToken.AuthTokenListener;
import lat.trust.trusttrifles.network.RestClientCompany;
import lat.trust.trusttrifles.network.req.SaveDeviceInfoRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaveDeviceInfo {

    public static void saveDeviceInfo(final String dni, final String bundleId, final String trustId) {
        TrustLogger.d("[TRUST CLIENT] SAVING DEVICE...");
        final SaveDeviceInfoRequest saveDeviceInfo = new SaveDeviceInfoRequest();
        saveDeviceInfo.setBundle_id(bundleId);
        saveDeviceInfo.setDni(dni);
        saveDeviceInfo.setTrust_id(trustId);
        saveDevice(saveDeviceInfo);
    }
    public static void saveDeviceInfo( final String bundleId, final String trustId) {
        TrustLogger.d("[TRUST CLIENT] SAVING DEVICE...");
        final SaveDeviceInfoRequest saveDeviceInfo = new SaveDeviceInfoRequest();
        saveDeviceInfo.setBundle_id(bundleId);
        saveDeviceInfo.setTrust_id(trustId);
        saveDevice(saveDeviceInfo);
    }
    private static void saveDevice(final SaveDeviceInfoRequest saveDeviceInfo) {
        RestClientCompany.setup().saveDeviceData(saveDeviceInfo, Hawk.get(Constants.TOKEN_SERVICE).toString()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 401) {
                    TrustLogger.d("[TRUST CLIENT] UNAUTHORIZED SAVE DEVIDE: " + response.code() + " MESSAGE: " + response.message());
                    refreshSaveDevice(saveDeviceInfo);
                    return;
                }
                if (response.isSuccessful()) {
                    TrustLogger.d("[TRUST CLIENT] DEVICE WAS SAVED CODE: "
                            + response.code() + " DNI: "
                            + saveDeviceInfo.getDni() + " BUNDLE ID: "
                            + saveDeviceInfo.getBundle_id() + " TRUST ID: "
                            + saveDeviceInfo.getTrust_id());
                } else {
                    TrustLogger.d("[TRUST CLIENT] SAvING DEVICE CODE: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                TrustLogger.d("[TRUST CLIENT] SAvING DEVICE ERROR: " + t.getMessage());
            }
        });
    }
    private static void refreshSaveDevice(final SaveDeviceInfoRequest saveDeviceInfo) {
        AuthToken.getAccessToken(new AuthTokenListener.Auth() {
            @Override
            public void onSuccessAccessToken(String token) {
                Hawk.put(Constants.TOKEN_SERVICE,"Bearer " + token);
                RestClientCompany.setup().saveDeviceData(saveDeviceInfo, Hawk.get(Constants.TOKEN_SERVICE).toString()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 401) {
                            TrustLogger.d("[TRUST CLIENT] ERROR TOKEN REFRESH SAVE DEVICXE INFO" + response.code() + " MESSAGE: " + response.message());
                            return;
                        }
                        if (response.isSuccessful()) {
                            TrustLogger.d("[TRUST CLIENT] DEVICE WAS SAVED CODE: "
                                    + response.code() + " DNI: "
                                    + saveDeviceInfo.getDni() + " BUNDLE ID: "
                                    + saveDeviceInfo.getBundle_id() + " TRUST ID: "
                                    + saveDeviceInfo.getTrust_id());
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onErrorAccessToken(String error) {

            }
        });
    }

    public static void saveDeviceInfo(final String dni, Context context) {
        TrustLogger.d("[TRUST CLIENT] SAVING DEVICE...");
        final SaveDeviceInfoRequest saveDeviceInfo = new SaveDeviceInfoRequest();
        saveDeviceInfo.setBundle_id(context.getPackageName());
        saveDeviceInfo.setDni(dni);
        saveDeviceInfo.setTrust_id(Hawk.get(Constants.TRUST_ID_AUTOMATIC).toString());
        RestClientCompany.setup().saveDeviceData(saveDeviceInfo, Hawk.get(Constants.TOKEN_SERVICE).toString()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 401) {
                    TrustLogger.d("[TRUST CLIENT] UNAUTHORIZED SAVE DEVIDE: " + response.code() + " MESSAGE: " + response.message());
                    refreshSaveDevice(saveDeviceInfo);
                    return;
                }
                if (response.isSuccessful() || response.code() == 200) {
                    TrustLogger.d("[TRUST CLIENT] DEVICE WAS SAVED CODE: " + response.code() + ", DNI: " + dni + " , BUNDLE ID: " + saveDeviceInfo.getBundle_id() + " , TRUST ID: " + saveDeviceInfo.getTrust_id());

                } else {
                    TrustLogger.d("[TRUST CLIENT] SAVING DEVICE CODE: " + response.code());

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                TrustLogger.d("[TRUST CLIENT] SAVING DEVICE ERROR: " + t.getMessage());
            }
        });
    }


}

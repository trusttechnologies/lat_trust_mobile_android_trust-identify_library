package lat.trust.trusttrifles.utilities;

import android.content.Context;

import com.orhanobut.hawk.Hawk;

import lat.trust.trusttrifles.authToken.AuthToken;
import lat.trust.trusttrifles.authToken.AuthTokenListener;
import lat.trust.trusttrifles.managers.DataManager;
import lat.trust.trusttrifles.network.RestClientIdentify;
import lat.trust.trusttrifles.network.req.SaveDeviceInfoRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaveDeviceInfo {
    /**
     * Overload 1
     * Save a DNI of user session, bundle id and trust id
     *
     * @param dni      of the user session logged in.
     * @param bundleId of the application.
     * @param trustId  of the device.
     */
    public static void saveDeviceInfo(final String dni, final String bundleId, final String trustId) {
        TrustLogger.d("[TRUST CLIENT] SAVING DEVICE...");
        final SaveDeviceInfoRequest saveDeviceInfo = new SaveDeviceInfoRequest();
        saveDeviceInfo.setBundleId(bundleId);
        saveDeviceInfo.setDni(dni);
        saveDeviceInfo.setTrustId(trustId);
        saveDeviceInfo.setPerson(DataManager.getIdentity());
        saveDevice(saveDeviceInfo);
    }

    /**
     * Save a bundle id and trust id without a DNI of a user session.
     *
     * @param bundleId of the application.
     * @param trustId  of the device.
     */
    public static void saveDeviceInfo(final String bundleId, final String trustId) {
        TrustLogger.d("[TRUST CLIENT] SAVING DEVICE...");
        final SaveDeviceInfoRequest saveDeviceInfo = new SaveDeviceInfoRequest();
        saveDeviceInfo.setBundleId(bundleId);
        saveDeviceInfo.setTrustId(trustId);
        saveDevice(saveDeviceInfo);
    }

    /**
     * Call the api for save the data of the device
     *
     * @param saveDeviceInfo
     */
    private static void saveDevice(final SaveDeviceInfoRequest saveDeviceInfo) {
        try {
            RestClientIdentify.get().saveDeviceData(saveDeviceInfo, Hawk.get(Constants.TOKEN_SERVICE).toString()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 401) {
                        TrustLogger.d("[TRUST CLIENT] UNAUTHORIZED SAVE DEVICE: " + response.code() + " MESSAGE: " + response.message());
                        refreshSaveDevice(saveDeviceInfo);
                        return;
                    }
                    if (response.isSuccessful()) {
                        TrustLogger.d("[TRUST CLIENT] DEVICE WAS SAVED CODE: "
                                + response.code() + " DNI: "
                                + saveDeviceInfo.getDni() + " BUNDLE ID: "
                                + saveDeviceInfo.getBundleId() + " TRUST ID: "
                                + saveDeviceInfo.getTrustId());
                    } else {
                        TrustLogger.d("[TRUST CLIENT] SAVING DEVICE CODE: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    TrustLogger.d("[TRUST CLIENT] SAvING DEVICE ERROR: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            TrustLogger.d("[TRUST CLIENT] ERROR SAVE DEVICE: " + e.getMessage());
        }

    }

    private static void refreshSaveDevice(final SaveDeviceInfoRequest saveDeviceInfo) {
        AuthToken.getAccessToken(new AuthTokenListener.Auth() {
            @Override
            public void onSuccessAccessToken(String token) {
                Hawk.put(Constants.TOKEN_SERVICE, "Bearer " + token);
                RestClientIdentify.get().saveDeviceData(saveDeviceInfo, Hawk.get(Constants.TOKEN_SERVICE).toString()).enqueue(new Callback<Void>() {
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
                                    + saveDeviceInfo.getBundleId() + " TRUST ID: "
                                    + saveDeviceInfo.getTrustId());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        TrustLogger.d("[TRUST CLIENT] ERROR SAVE DEVICE REFRESH TOKEN: " + t.getMessage());

                    }
                });
            }

            @Override
            public void onErrorAccessToken(String error) {
                TrustLogger.d("[TRUST CLIENT] ERROR SAVE DEVICE REFRESH TOKEN: " + error);

            }
        });
    }

    /**
     * Save a DNI of user session, bundle id and trust id
     *
     * @param dni     of the user session logged in
     * @param context of the current appor activity.
     */
    public static void saveDeviceInfo(final String dni, Context context) {
        try {
            TrustLogger.d("[TRUST CLIENT] SAVING DEVICE...");
            final SaveDeviceInfoRequest saveDeviceInfo = new SaveDeviceInfoRequest();
            saveDeviceInfo.setBundleId(context.getPackageName());
            saveDeviceInfo.setDni(dni);
            saveDeviceInfo.setTrustId(Hawk.get(Constants.TRUST_ID_AUTOMATIC).toString());
            saveDeviceInfo.setPerson(DataManager.getIdentity());

            RestClientIdentify.get().saveDeviceData(saveDeviceInfo, Hawk.get(Constants.TOKEN_SERVICE).toString()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 401) {
                        TrustLogger.d("[TRUST CLIENT] UNAUTHORIZED SAVE DEVIDE: " + response.code() + " MESSAGE: " + response.message());
                        refreshSaveDevice(saveDeviceInfo);
                        return;
                    }
                    if (response.isSuccessful() || response.code() == 200) {
                        TrustLogger.d("[TRUST CLIENT] DEVICE WAS SAVED CODE: " + response.code() + ", DNI: " + dni + " , BUNDLE ID: " + saveDeviceInfo.getBundleId() + " , TRUST ID: " + saveDeviceInfo.getTrustId());

                    } else {
                        TrustLogger.d("[TRUST CLIENT] SAVING DEVICE CODE: " + response.code());

                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    TrustLogger.d("[TRUST CLIENT] SAVING DEVICE ERROR: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            TrustLogger.d("[TRUST CLIENT] SAVING DEVICE ERROR: " + e.getMessage());

        }

    }

}

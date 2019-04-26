package lat.trust.trusttrifles.services;

import android.content.Context;

import com.orhanobut.hawk.Hawk;

import lat.trust.trusttrifles.authToken.AuthToken;
import lat.trust.trusttrifles.authToken.AuthTokenListener;
import lat.trust.trusttrifles.network.RestClientNotification;
import lat.trust.trusttrifles.network.req.SaveDeviceNotificationRequest;
import lat.trust.trusttrifles.utilities.Constants;
import lat.trust.trusttrifles.utilities.TrustLogger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Notifications {

    public static void registerDevice(final String firebaseToken, final Context context) {
        TrustLogger.d("[TRUST CLIENT] getting token for register device...");
        final SaveDeviceNotificationRequest device = new SaveDeviceNotificationRequest();
        device.setBundle_id(context.getApplicationContext().getPackageName());
        device.setFirebase_token(firebaseToken);
        device.setPlatform("Android");
        device.setTrust_id(Hawk.get(Constants.TRUST_ID_AUTOMATIC).toString());
        sendDevice(device);

    }

    private static void sendDevice(final SaveDeviceNotificationRequest device) {
        if (Hawk.contains(Constants.TOKEN_SERVICE)) {
            RestClientNotification.setup().registerDeviceNofitication(device, Hawk.get(Constants.TOKEN_SERVICE).toString()).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.code() == 401) {
                        TrustLogger.d("[TRUST CLIENT] TOKEN EXPIRED FOR SAVE DEVICE NOTIFICATION...");
                        refreshTokenSaveDevice(device);
                        return;
                    }
                    if (response.isSuccessful()) {
                        TrustLogger.d("[TRUST CLIENT] DEVICE WAS SAVED");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    TrustLogger.d("[TRUST CLIENT] FAIL FOR SAVE DEVICE NOTIFICATION: " + t.getMessage());

                }
            });
        } else {
            TrustLogger.d("[TRUST CLIENT] NO TOKEN FOR SAVE DEVICE NOTIFICATION...NOW GETTING...");
            AuthToken.getAccessToken(new AuthTokenListener.Auth() {
                @Override
                public void onSuccessAccessToken(String token) {
                    TrustLogger.d("[TRUST CLIENT] TOKEN GET: " + token);
                    Hawk.put(Constants.TOKEN_SERVICE, token);
                    sendDevice(device);
                }

                @Override
                public void onErrorAccessToken(String error) {
                    TrustLogger.d("[TRUST CLIENT] ERROR GETTING TOKEN : " + error);
                }
            });
        }

    }

    private static void refreshTokenSaveDevice(final SaveDeviceNotificationRequest device) {
        TrustLogger.d("[TRUST CLIENT] REFRESHING TOKEN FOR SAVE DEVICE NOTIFICATION...");
        AuthToken.getAccessToken(new AuthTokenListener.Auth() {
            @Override
            public void onSuccessAccessToken(final String token) {
                Hawk.put(Constants.TOKEN_SERVICE, token);
                TrustLogger.d("[TRUST CLIENT] REFRESHING TOKEN REFRESH: " + token);
                RestClientNotification.setup().registerDeviceNofitication(device, token).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.code() == 401) {
                            TrustLogger.d("[TRUST CLIENT] CANT REFRESH TOKEN FOR SAVE DEVICE NOTIFICATION");
                            return;
                        }
                        if (response.isSuccessful()) {
                            TrustLogger.d("[TRUST CLIENT] DEVICE WAS SAVED");

                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        TrustLogger.d("[TRUST CLIENT] FAIL  REFRESH TOKEN FOR SAVE DEVICE NOTIFICATION: " + t.getMessage());

                    }
                });
            }

            @Override
            public void onErrorAccessToken(String error) {
                TrustLogger.d("[TRUST CLIENT] FAIL  REFRESH TOKEN FOR SAVE DEVICE NOTIFICATION: " + error);

            }
        });
    }


}

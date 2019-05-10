package lat.trust.trusttrifles.utilities;

import com.orhanobut.hawk.Hawk;

import lat.trust.trusttrifles.authToken.AuthToken;
import lat.trust.trusttrifles.authToken.AuthTokenListener;
import lat.trust.trusttrifles.model.CallbackACK;
import lat.trust.trusttrifles.network.RestClientACK;
import lat.trust.trusttrifles.network.RestClientNotification;
import lat.trust.trusttrifles.network.req.CallbackACKRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationAck {

    public static void sendACK(CallbackACK callbackACK){
        ack(new CallbackACKRequest(callbackACK.getMessageId(),callbackACK.getAction(),callbackACK.getStatus(),callbackACK.getTrustId()));
    }
    private static void ack(final CallbackACKRequest callbackACKRequest){
        if(Hawk.contains(Constants.TOKEN_SERVICE)){
            RestClientNotification.setup().sendACK(callbackACKRequest,Hawk.get(Constants.TOKEN_SERVICE).toString()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.code() == 401){
                        TrustLogger.d("[TRUST CLIENT] TOKEN EXPIRED FOR ACK NOTIFICATION: " + response.code());
                        refreshAck(callbackACKRequest);
                     return;
                    }
                    if(response.isSuccessful()){
                        TrustLogger.d("[TRUST CLIENT] SUCCESS SEND ACK NOTIFICATION CODE:" + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    TrustLogger.d("[TRUST CLIENT] FAIL SEND ACK NOTIFICATION: " +t.getMessage());
                }
            });
        }else{
            refreshAck(callbackACKRequest);
        }
    }

    private static void refreshAck(final CallbackACKRequest callbackACKRequest){
        TrustLogger.d("[TRUST CLIENT] REFRESHING TOKEN FOR ACK NOTIFICATION...");
        AuthToken.getAccessToken(new AuthTokenListener.Auth() {
            @Override
            public void onSuccessAccessToken(String token) {
                TrustLogger.d("[TRUST CLIENT] TOKEN WAS REFRESH SUCCESSFULLY: " +token);
                Hawk.put(Constants.TOKEN_SERVICE,token);
                RestClientNotification.setup().sendACK(callbackACKRequest,token).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code() == 401){
                            TrustLogger.d("[TRUST CLIENT] TOKEN EXPIRED FOR ACK NOTIFICATION: " + response.code());
                            return;
                        }
                        if(response.isSuccessful()){
                            TrustLogger.d("[TRUST CLIENT] SUCCESS SEND ACK NOTIFICATION CODE:" + response.code());
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        TrustLogger.d("[TRUST CLIENT] FAIL SEND ACK NOTIFICATION: " +t.getMessage());
                    }
                });
            }
            @Override
            public void onErrorAccessToken(String error) {
                TrustLogger.d("[TRUST CLIENT] ERROR REFRESHING TOKEN FOR ACK NOTIFICATION: " + error);

            }
        });
    }

}

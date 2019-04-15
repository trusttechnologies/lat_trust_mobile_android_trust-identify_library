package lat.trust.trusttrifles.authToken;

import com.orhanobut.hawk.Hawk;

import lat.trust.trusttrifles.model.TrustAuth;
import lat.trust.trusttrifles.network.RestClientAccessToken;
import lat.trust.trusttrifles.network.req.AuthTokenRequest;
import lat.trust.trusttrifles.network.res.AuthTokenResponse;
import lat.trust.trusttrifles.utilities.TrustLogger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthToken   {

    private static  String token;

    public static void getAccessToken(final AuthTokenListener.Auth authListener) {
        AuthTokenRequest authTokenRequest = new AuthTokenRequest();
        authTokenRequest.setClient_id(TrustAuth.CLIENT_ID);
        authTokenRequest.setClient_secret(TrustAuth.CLIENT_SECRET);
        authTokenRequest.setGrant_type(TrustAuth.GRANT_TYPE);
        RestClientAccessToken.get().getAccessToken(authTokenRequest).enqueue(new Callback<AuthTokenResponse>() {
            @Override
            public void onResponse(Call<AuthTokenResponse> call, Response<AuthTokenResponse> response) {
                if (response.isSuccessful()) {
                    TrustLogger.d(response.body().toString());
                    authListener.onSuccessAccessToken(response.body().getAccess_token());
                }
            }

            @Override
            public void onFailure(Call<AuthTokenResponse> call, Throwable t) {
                    authListener.onErrorAccessToken(t.getMessage());
            }
        });
    }

    public static void refreshToken(){

    }

    public static String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    /*todo aqui va el refresh*/

}

package lat.trust.trusttrifles.network;

import lat.trust.trusttrifles.model.audit.AuditTest;
import lat.trust.trusttrifles.network.req.AuthTokenRequest;
import lat.trust.trusttrifles.network.req.TrifleBody;
import lat.trust.trusttrifles.network.res.AuthTokenResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by felipe on 15-03-18.
 * <p>
 * This class contains the api methods definition
 */

public interface API {
    //Insert methods here

    //TODO: add create audit call to report transactions

    @POST("trifle")
    Call<TrifleResponse> trifle(@Body TrifleBody body);

/*
    @POST("event")
    Call<Void> sendEvent(@Body EventBody body);

    @POST("trifle/remote")
    Call<TrifleResponse> remote(@Body RemoteEventBody2 body);

    @POST("trifle/remote")
    Call<TrifleResponse> remote2(@Body RemoteEventBody body);
*/


    @POST("api/v1/device")
    Call<TrifleResponse> trifle2(@Body TrifleBody body);

    @POST("api/v1/audit")
    Call<Void> createAuditTest(@Body AuditTest body);


    @POST("oauth/token")
    Call<AuthTokenResponse> getAccessToken(@Body AuthTokenRequest tokenRequest);
}

package lat.trust.trusttrifles.network;

import lat.trust.trusttrifles.authToken.AuthToken;
import lat.trust.trusttrifles.model.audit.AuditTest;
import lat.trust.trusttrifles.network.req.AuthTokenRequest;
import lat.trust.trusttrifles.network.req.SaveDeviceInfoRequest;
import lat.trust.trusttrifles.network.req.TrifleBody;
import lat.trust.trusttrifles.network.res.AuthTokenResponse;
import lat.trust.trusttrifles.utilities.Constants;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.Headers;
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
    Call<TrifleResponse> trifle(@Body TrifleBody body,@Header("Authorization") String token);


    @POST("api/v1/device")
    Call<TrifleResponse> trifle2(@Body TrifleBody body,@Header("Authorization") String token);


  /*  @Headers("Authorization: Bearer" + Constants.TOKEN_SERVICE)
    @POST("api/v1/audit")
    Call<Void> createAuditTest(@Body AuditTest body);
*/


    @POST("api/v1/audit")
    Call<Void> createAuditTest(@Body AuditTest body, @Header("Authorization") String token);



    @POST("oauth/token")
    Call<AuthTokenResponse> getAccessToken(@Body AuthTokenRequest tokenRequest);

    @POST("api/v1/app/state")
    Call<Void> saveDeviceData(@Body SaveDeviceInfoRequest saveDeviceInfoRequest,@Header("Authorization") String token);
}

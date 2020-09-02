package lat.trust.trusttrifles.network;

import lat.trust.trusttrifles.model.gateway.DeviceBody;
import lat.trust.trusttrifles.network.req.AuthTokenRequest;
import lat.trust.trusttrifles.network.req.SaveDeviceInfoRequest;
import lat.trust.trusttrifles.network.req.SaveDeviceNotificationRequest;
import lat.trust.trusttrifles.network.req.TrifleBody;
import lat.trust.trusttrifles.network.res.AuthTokenResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by felipe on 15-03-18.
 * <p>
 * This class contains the api methods definition
 */

public interface API {

    @POST("identification/api/v1/device")
    Call<TrifleResponse> trifle2(@Body TrifleBody body, @Header("Authorization") String token);


    @Deprecated
    @POST("notifications/device/register")
    Call<String> registerDeviceNofitication(@Body SaveDeviceNotificationRequest saveDevice, @Header("Authorization") String token);

    @POST("oauth/token")
    Call<AuthTokenResponse> getAccessToken(@Body AuthTokenRequest tokenRequest);

    @POST("company/api/v1/app/state")
    Call<Void> saveDeviceData(@Body SaveDeviceInfoRequest saveDeviceInfoRequest, @Header("Authorization") String token);




    @POST("v2/device")
    Call<TrifleResponse> getTrustId(@Body DeviceBody body, @Header("Authorization") String token);

    @POST("v2/device/{trustid}/sim")
    Call<Void> sendSIM( @Header("Authorization") String token);

}

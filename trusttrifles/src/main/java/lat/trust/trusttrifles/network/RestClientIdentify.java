package lat.trust.trusttrifles.network;

import java.util.concurrent.TimeUnit;

import lat.trust.trusttrifles.TrustIdentifyConfigurationService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RestClientIdentify {
    private static final String URL_PROD = "https://api.trust.lat/";
    private static final String URL_DEV = "https://api-tst.trust.lat/";
    private static final String URL_GATEWAY = "https://api-tst.trust.lat/sdk-gateway/";

    private static final int CONNECT_TIMEOUT = 30 * 4;
    private static final int WRITE_TIMEOUT = 30 * 4;
    private static final int READ_TIMEOUT = 30 * 4;
    private static API api;

    static {
        setup();
    }


    /**
     * Obtain the access of all API methods
     *
     * @return API reference
     */
    public static API get() {
        return api;
    }

    /**
     * Default setup for OkHttp and Retrofit
     */
    private static void setup() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TrustIdentifyConfigurationService.getEnvironment() ? URL_PROD : URL_DEV) //true:prod ; false:dev
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        api = retrofit.create(API.class);
    }

}

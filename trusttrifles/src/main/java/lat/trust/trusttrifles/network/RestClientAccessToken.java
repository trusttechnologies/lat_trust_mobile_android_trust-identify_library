package lat.trust.trusttrifles.network;

import java.util.concurrent.TimeUnit;

import lat.trust.trusttrifles.TrustIdentifyConfigurationService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClientAccessToken {
    private static final String BASE_URL_PROD = "https://atenea.trust.lat/";
    private static final String BASE_URL_TEMP = "https://atenea-tst.trust.lat/";
    private static final int CONNECT_TIMEOUT = 30 * 4;
    private static final int WRITE_TIMEOUT = 30 * 4;
    private static final int READ_TIMEOUT = 30 * 4;
    private static API api;

    static {
        setup();
    }

    /**
     * Class constructor
     */
    private RestClientAccessToken() {
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
                .baseUrl(TrustIdentifyConfigurationService.getEnvironment() ? BASE_URL_PROD : BASE_URL_TEMP)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        api = retrofit.create(API.class);
    }

}

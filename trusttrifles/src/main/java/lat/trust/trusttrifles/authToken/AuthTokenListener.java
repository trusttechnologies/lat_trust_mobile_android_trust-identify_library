package lat.trust.trusttrifles.authToken;

public interface AuthTokenListener {

    interface Auth {

        void  onSuccessAccessToken(String token);

        void onErrorAccessToken(String error);
        }
}

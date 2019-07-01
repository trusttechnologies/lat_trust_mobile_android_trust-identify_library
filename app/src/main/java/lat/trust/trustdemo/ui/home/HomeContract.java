package lat.trust.trustdemo.ui.home;

import android.content.Context;

public interface HomeContract {

    interface View {
        void showTrustId(String trustId);

        void showNotification();

        void showCompany();

        void showAudit();

        void showLogin();

    }

    interface Presenter {

        void onCreate();

        void login();

        void audit();
    }

    interface Interactor {

        void trustId();

        void company(Context mContext);

        void notification(Context mContext);
    }

    interface InteractorOutput {

        void onSuccessTrustId(String trustId);

        void onErrorTrustId();

        void onSuccessCompany();

        void onErrorCompany();

        void onSuccessNotification();

        void onErrorNotification();
    }

    interface Router {

    }
}

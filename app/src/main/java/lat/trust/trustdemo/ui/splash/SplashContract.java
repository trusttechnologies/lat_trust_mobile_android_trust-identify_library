package lat.trust.trustdemo.ui.splash;

import android.content.Context;

import java.util.ArrayList;

import lat.trust.trusttrifles.model.StringsModel;
import lat.trust.trusttrifles.model.TrustResponse;

public interface SplashContract {

    interface View {

        void requestPermissions();

        void reloadData(ArrayList<StringsModel> list);

        void setDataRecyclerView(ArrayList<StringsModel> lst);
    }

    interface Presenter {
        void onCreate();

        void initView();

        void permissions();

        void clearLog();

        void requestTrustIdNormal(Context context);

        void requestTrustIdLite(Context context);

        void requestTrustIdZero(Context context);

        void requestSendIdentify(Context context);

        void requestCustomToken(String customToken);

        void requestDeleteToken();
    }

    interface Interactor {

        void clearLog();

        void setCustomToken(String customToken);

        void deleteToken();

        void getDataButtons();

        void getListButtons();

        void getTrustIdNormal(Context context);

        void getTrustIdLite(Context context);

        void getTrustIdZero(Context context);

        void getSendIdentify(Context context);

    }

    interface InteractorOutput {
        void onSuccessGetDataButtons(ArrayList<StringsModel> list);

        void onSuccessGestTrustId(ArrayList<StringsModel> list);
    }

    interface Router {

    }
}

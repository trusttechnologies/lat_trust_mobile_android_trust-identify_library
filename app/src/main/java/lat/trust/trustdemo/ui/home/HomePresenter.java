package lat.trust.trustdemo.ui.home;

import android.app.Activity;
import android.content.Context;


public class HomePresenter implements HomeContract.InteractorOutput, HomeContract.Presenter {

    private HomeContract.View mView;
    private HomeContract.Interactor mInteractor;
    private HomeContract.Router mRouter;
    private Context mContext;

    public HomePresenter(Activity activity) {
        this.mView = (HomeContract.View) activity;
        this.mRouter = new HomeRouter(activity);
        this.mInteractor = new HomeInteractor(this);
        this.mContext = activity;
    }

    @Override
    public void onCreate() {
        mInteractor.trustId();
    }

    @Override
    public void login() {

    }

    @Override
    public void audit() {
        mView.showAudit();
    }


    @Override
    public void onSuccessTrustId(String trustId) {
        mView.showTrustId(trustId);
        mInteractor.notification(mContext);
    }

    @Override
    public void onErrorTrustId() {

    }

    @Override
    public void onSuccessCompany() {
        mView.showCompany();
    }

    @Override
    public void onErrorCompany() {

    }

    @Override
    public void onSuccessNotification() {
        mView.showNotification();
        mInteractor.company(mContext);
    }

    @Override
    public void onErrorNotification() {

    }


}

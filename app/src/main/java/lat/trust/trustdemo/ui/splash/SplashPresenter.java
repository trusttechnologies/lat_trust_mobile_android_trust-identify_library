package lat.trust.trustdemo.ui.splash;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;

import lat.trust.trusttrifles.model.StringsModel;
import lat.trust.trusttrifles.model.TrustResponse;
import lat.trust.trusttrifles.network.res.AuthTokenResponseFlavor;

public class SplashPresenter implements SplashContract.Presenter, SplashContract.InteractorOutput {

    private SplashContract.View mView;
    private SplashContract.Interactor mInteractor;

    public SplashPresenter(Activity activity) {
        this.mView = (SplashContract.View) activity;
        this.mInteractor = new SplashInteractor(this);
    }

    @Override
    public void onCreate() {
        mView.requestPermissions();
    }

    @Override
    public void initView() {
        mInteractor.getDataButtons();
    }

    @Override
    public void permissions() {
        mView.requestPermissions();
    }

    @Override
    public void clearLog() {
        mInteractor.clearLog();
    }

    @Override
    public void requestTrustIdNormal(Context context) {
        mInteractor.getTrustIdNormal(context);

    }

    @Override
    public void requestTrustIdLite(Context context) {
        mInteractor.getTrustIdLite(context);
    }

    @Override
    public void requestTrustIdZero(Context context) {
        mInteractor.getTrustIdZero(context);
    }

    @Override
    public void requestSendIdentify(Context context) {
        mInteractor.getSendIdentify(context);
    }

    @Override
    public void requestCustomToken(AuthTokenResponseFlavor authTokenResponseFlavor) {
        mInteractor.setCustomToken(authTokenResponseFlavor);
    }


    @Override
    public void requestDeleteToken() {
        mInteractor.deleteToken();
    }


    @Override
    public void onSuccessGetDataButtons(ArrayList<StringsModel> list) {
        mView.setDataRecyclerView(list);
    }

    @Override
    public void onSuccessGestTrustId(ArrayList<StringsModel> list) {
        mView.reloadData(list);
    }

}


package lat.trust.trustdemo.ui.home;

import android.content.Context;

import com.google.firebase.iid.FirebaseInstanceId;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

import lat.trust.trusttrifles.TrustClient;

public class HomeInteractor implements HomeContract.Interactor {
    private HomeContract.InteractorOutput mOutPuts;


    public HomeInteractor(HomeContract.InteractorOutput output) {
        this.mOutPuts = output;

    }

    @Override
    public void trustId() {

    }

    @Override
    public void company(Context mContext) {
        // SaveDeviceInfo.saveDeviceInfo(mContext.getPackageName(), Hawk.get(Constants.TRUST_ID_AUTOMATIC).toString());
        mOutPuts.onSuccessCompany();
    }

    @Override
    public void notification(Context mContext) {
        // Notifications.registerDevice(FirebaseInstanceId.getInstance().getToken(), mContext);
        mOutPuts.onSuccessNotification();
    }
}

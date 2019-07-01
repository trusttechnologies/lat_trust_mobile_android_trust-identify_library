package lat.trust.trustdemo.ui.home;

import android.app.Activity;

public class HomeRouter implements HomeContract.Router {

    private Activity mActivity;

    public HomeRouter(Activity mActivity) {
        this.mActivity = mActivity;
    }
}

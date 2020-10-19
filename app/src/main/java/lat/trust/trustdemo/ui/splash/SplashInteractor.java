package lat.trust.trustdemo.ui.splash;

import android.content.Context;
import android.util.Log;

import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

import lat.trust.trusttrifles.Trust;
import lat.trust.trusttrifles.TrustListener;
import lat.trust.trusttrifles.managers.LogManager;
import lat.trust.trusttrifles.model.Identity;
import lat.trust.trusttrifles.model.StringsModel;
import lat.trust.trusttrifles.model.TrustResponse;
import lat.trust.trusttrifles.utilities.TrustLogger;

import static lat.trust.trustdemo.Utils.ConstrantsTrustDemo.TYPE_TRUST_ID_NORMAL;
import static lat.trust.trusttrifles.utilities.Constants.LIST_LOG;
import static lat.trust.trusttrifles.utilities.Constants.TOKEN_SERVICE_CUSTOM;

public class SplashInteractor implements SplashContract.Interactor {

    private SplashContract.InteractorOutput mOutput;

    public SplashInteractor(SplashContract.InteractorOutput output) {
        this.mOutput = output;
    }

    @Override
    public void clearLog() {
        LogManager.clearLog();
        ArrayList<StringsModel> list = new ArrayList<>();
        if (Hawk.contains(LIST_LOG)) {
            list = Hawk.get(LIST_LOG);
        }
        mOutput.onSuccessGestTrustId(list);
    }

    @Override
    public void setCustomToken(String customToken) {
        Trust.setCustomToken(customToken);
    }

    @Override
    public void deleteToken() {
        if (Hawk.contains(TOKEN_SERVICE_CUSTOM)) {
            Hawk.delete(TOKEN_SERVICE_CUSTOM);
        }
    }

    @Override
    public void getDataButtons() {
        ArrayList<StringsModel> list = new ArrayList<>();
        if (Hawk.contains(LIST_LOG)) {
            list = Hawk.get(LIST_LOG);
        }
        mOutput.onSuccessGetDataButtons(list);
    }

    @Override
    public void getListButtons() {

    }

    @Override
    public void getTrustIdNormal(Context context) {
        Trust.getTrustIdNormal(context, new TrustListener.OnResult<TrustResponse>() {
            @Override
            public void onSuccess(int code, TrustResponse data) {
                ArrayList<StringsModel> stringsModels = new ArrayList<>();
                if (Hawk.contains(LIST_LOG)) {
                    stringsModels = Hawk.get(LIST_LOG);
                }
                mOutput.onSuccessGestTrustId(stringsModels);
            }

            @Override
            public void onError(int code) {

            }

            @Override
            public void onFailure(Throwable t) {

            }

            @Override
            public void onPermissionRequired(ArrayList<String> permisos) {

            }
        });
    }

    @Override
    public void getTrustIdLite(Context context) {
        Trust.getTrustIdLite(context, new TrustListener.OnResult<TrustResponse>() {
            @Override
            public void onSuccess(int code, TrustResponse data) {
                ArrayList<StringsModel> stringsModels = new ArrayList<>();
                if (Hawk.contains(LIST_LOG)) {
                    stringsModels = Hawk.get(LIST_LOG);
                }
                mOutput.onSuccessGestTrustId(stringsModels);
            }

            @Override
            public void onError(int code) {

            }

            @Override
            public void onFailure(Throwable t) {

            }

            @Override
            public void onPermissionRequired(ArrayList<String> permisos) {

            }
        });
    }

    @Override
    public void getTrustIdZero(Context context) {
        Trust.getTrustIdZero(context, new TrustListener.OnResult<TrustResponse>() {
            @Override
            public void onSuccess(int code, TrustResponse data) {
                ArrayList<StringsModel> stringsModels = new ArrayList<>();
                if (Hawk.contains(LIST_LOG)) {
                    stringsModels = Hawk.get(LIST_LOG);
                }
                mOutput.onSuccessGestTrustId(stringsModels);
            }

            @Override
            public void onError(int code) {

            }

            @Override
            public void onFailure(Throwable t) {

            }

            @Override
            public void onPermissionRequired(ArrayList<String> permisos) {

            }
        });
    }

    @Override
    public void getSendIdentify(Context context) {
        TrustLogger.d("SENDING IDENTIFY");
        Identity identity = new Identity();
        identity.setDni("18236924-1");
        identity.setEmail("fcaro@trust.lat");
        identity.setName("felipe");
        identity.setLastname("caro");
        identity.setPhone("+56982110950");
        Trust.sendIdentify(identity, context, new TrustListener.OnResult<TrustResponse>() {
            @Override
            public void onSuccess(int code, TrustResponse data) {

            }

            @Override
            public void onError(int code) {

            }

            @Override
            public void onFailure(Throwable t) {

            }

            @Override
            public void onPermissionRequired(ArrayList<String> permisos) {

            }
        });
    }
}

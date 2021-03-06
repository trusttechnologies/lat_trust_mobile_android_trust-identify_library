package lat.trust.trusttrifles;


import java.util.ArrayList;

public interface TrustListener {
    interface OnResult<T> {
        void onSuccess(int code, T data);

        void onError(int code);

        void onFailure(Throwable t);

        void onPermissionRequired(ArrayList<String> permisos);


    }

    interface Permissions {
        void onPermissionSuccess();

        void onPermissionRevoke();
    }


    interface OnResultSimple {

        void onResult(int code, String message);

    }

    interface OnResultAudit {

        void onSuccess(String idAudit);

        void onError(String error);
    }
}

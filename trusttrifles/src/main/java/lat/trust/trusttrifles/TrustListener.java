package lat.trust.trusttrifles;


import java.util.ArrayList;

public interface TrustListener {
    interface OnResult<T> {
        void onSuccess(int code, T data);

        void onError(int code);

        void onFailure(Throwable t);

        void onPermissionRequired(ArrayList<String> permisos);
    }

    interface OnResultSimple {
        void onResult(int code, String message);
    }
}

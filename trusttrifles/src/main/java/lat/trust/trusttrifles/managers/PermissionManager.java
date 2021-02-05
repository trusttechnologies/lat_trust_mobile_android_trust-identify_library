package lat.trust.trusttrifles.managers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.orhanobut.hawk.Hawk;

import lat.trust.trusttrifles.model.PermissionsGranted;
import lat.trust.trusttrifles.utilities.Constants;
import lat.trust.trusttrifles.utilities.TrustLogger;

public class PermissionManager {

    public static boolean isWriteStorageGranted() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static boolean isReadStorageGranted(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int result = context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                return result == PackageManager.PERMISSION_GRANTED;
            }
            return false;
        } catch (Exception ex) {
            TrustLogger.d("error in isReadStorageGranted: " + ex.getMessage());
            return false;
        }

    }

    public static boolean isReadPhoneStateGranted(Context context) {
        try {
            return context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;

        } catch (Exception ex) {
            TrustLogger.d("error in isReadPhoneStateGranted: " + ex.getMessage());
            return false;
        }
    }

    public static boolean isAccessCoarseLocationGranted(Context context) {
        try {
            return context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        } catch (Exception ex) {
            TrustLogger.d("error in isAccessCoarseLocationGranted: " + ex.getMessage());
            return false;
        }
    }

    public static boolean isAccessFineLocationGranted(Context context) {
        try {
            return context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        } catch (Exception ex) {
            TrustLogger.d("error in isAccessCoarseLocationGranted: " + ex.getMessage());
            return false;
        }
    }

    public static PermissionsGranted getPermissionGranted(Context context) {
        PermissionsGranted p = new PermissionsGranted();
        p.setAccessCoarseLocation(isAccessCoarseLocationGranted(context));
        p.setAccessFineLocation(isAccessFineLocationGranted(context));
        p.setReadExternalStorage(isReadStorageGranted(context));
        p.setWriteExternalStorage(isWriteStorageGranted());
        p.setReadPhoneState(isReadPhoneStateGranted(context));
        return p;
    }

}

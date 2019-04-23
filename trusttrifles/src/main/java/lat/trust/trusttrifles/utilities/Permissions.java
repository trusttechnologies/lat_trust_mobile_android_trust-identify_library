package lat.trust.trusttrifles.utilities;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import lat.trust.trusttrifles.TrustListener;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CALL_LOG;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.WRITE_CALL_LOG;
import static android.Manifest.permission.WRITE_CONTACTS;

public class Permissions {

    /**
     * ask for the necessary permission.
     * @param activity
     * @param trustListener
     */
    public static void checkPermissions(Activity activity, final TrustListener.Permissions trustListener) {
        Dexter.withActivity(activity)
                .withPermissions(READ_PHONE_STATE,
                        ACCESS_COARSE_LOCATION,
                        CAMERA,
                        ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        List<PermissionGrantedResponse> lst_permissionGrantedResponse = report.getGrantedPermissionResponses();
                        List<PermissionDeniedResponse> lst_permissionDeniedResponse = report.getDeniedPermissionResponses();
                        if (lst_permissionDeniedResponse.size() == 0) {
                            TrustLogger.d("all accepted");
                            trustListener.onPermissionSuccess();

                        } else {
                            TrustLogger.d("not all accepted");
                            for (PermissionGrantedResponse p : lst_permissionGrantedResponse) {
                                TrustLogger.d("permission not acepted: " + p.getPermissionName());
                            }
                            trustListener.onPermissionRevoke();

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                        trustListener.onPermissionRevoke();

                    }
                }).check();
    }
}

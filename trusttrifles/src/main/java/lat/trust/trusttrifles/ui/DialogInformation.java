package lat.trust.trusttrifles.ui;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.annotation.DrawableRes;
import androidx.fragment.app.FragmentManager;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;

public class DialogInformation {

    private FragmentManager fragmentManager;
    private DialogPermission dialogPermission;
    private DialogPermission.DialogPermissionListener dialogPermissionListener;
    private Context context;
    private boolean SMSPermission;
    private static final String TAG = DialogInformation.class.getSimpleName();

    public DialogInformation(Context context) {
        this.context = context;
        dialogPermission = new DialogPermission();
        dialogPermissionListener = (DialogPermission.DialogPermissionListener) context;

    }

    public void configuration(Activity activity, FragmentManager fragmentManager, @DrawableRes int idImage, String brand) {
        this.fragmentManager = fragmentManager;
        this.SMSPermission = false;
        dialogPermission.setConfiguration(idImage, brand, activity);

    }

    public void configuration(Activity activity, FragmentManager fragmentManager, @DrawableRes int idImage, String brand, boolean SMSPermission) {
        this.fragmentManager = fragmentManager;
        this.SMSPermission = SMSPermission;
        dialogPermission.setConfiguration(idImage, brand, activity, SMSPermission);
    }

    public void show() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!SMSPermission) {
                if ((context.checkSelfPermission(READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) ||
                        (context.checkSelfPermission(ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) ||
                        (context.checkSelfPermission(CAMERA) == PackageManager.PERMISSION_DENIED)) {
                    dialogPermission.show(fragmentManager, TAG);
                } else {
                    dialogPermissionListener.applyPermission(true);
                }
            } else {
                if ((context.checkSelfPermission(READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) ||
                        (context.checkSelfPermission(ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) ||
                        (context.checkSelfPermission(CAMERA) == PackageManager.PERMISSION_DENIED) ||
                        (context.checkSelfPermission(READ_SMS) == PackageManager.PERMISSION_DENIED) ||
                        (context.checkSelfPermission(RECEIVE_SMS) == PackageManager.PERMISSION_DENIED)) {
                    dialogPermission.show(fragmentManager, TAG);
                } else {
                    dialogPermissionListener.applyPermission(true);
                }
            }
        } else {
            dialogPermissionListener.applyPermission(true);
        }
    }


}

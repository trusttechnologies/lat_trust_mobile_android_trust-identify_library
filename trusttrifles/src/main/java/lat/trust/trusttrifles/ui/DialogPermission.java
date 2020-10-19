package lat.trust.trusttrifles.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import androidx.lifecycle.LifecycleOwner;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.orhanobut.hawk.Hawk;

import java.util.List;


import lat.trust.trusttrifles.utilities.Constants;
import lat.trust.trusttrifles.utilities.TrustLogger;
import lat.trust.trusttrifles.R;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.PROCESS_OUTGOING_CALLS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;

public  class DialogPermission extends AppCompatDialogFragment {

    private String brand;
    private DialogPermissionListener dialogPermissionListener;
    private Activity mActivity;
    private Drawable drawable;

    private boolean sms;
    private boolean call;

    public void setConfiguration(@DrawableRes int id, String brand, Activity activity) {
        this.drawable = activity.getResources().getDrawable(id);
        this.brand = brand;
        this.sms = false;
        this.call = true;
        this.mActivity = activity;

    }

    public void setConfiguration(@DrawableRes int id, String brand, Activity activity, boolean smsPermission) {
        this.drawable = activity.getResources().getDrawable(id);
        this.brand = brand;
        this.sms = smsPermission;
        this.call = true;
        this.mActivity = activity;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dialogPermissionListener = (DialogPermissionListener) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_dialog_permission, null);
        ImageView ima = view.findViewById(R.id.img_brand_dialog);
        ima.setImageDrawable(drawable);
        TextView textView = view.findViewById(R.id.txt_brand_dialg);
        textView.setText(brand + textView.getText().toString());

        LinearLayout lay_sms = view.findViewById(R.id.layout_sms);

        if (!sms) {
            lay_sms.setVisibility(View.GONE);
        }
        builder.setView(view)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogPermissionListener.applyPermission(false);
                    }
                })
                .setPositiveButton("Entiendo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (sms) {
                            checkPermissionsAll(mActivity);
                        } else {
                            checkPermissionDefault(mActivity);
                        }

                    }
                })
                .setCancelable(false);

        builder.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                // getAction to make sure this doesn't double fire
                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    // Your code here
                    return true; // Capture onKey
                }
                return false; // Don't capture
            }
        });
        return builder.create();


    }

    public interface DialogPermissionListener {
        void applyPermission(boolean status);
    }

    private void checkPermissionDefault(Activity activity) {
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
                            Hawk.put(Constants.PERMISSIONS, "1");
                            dialogPermissionListener.applyPermission(true);
                        } else {
                            TrustLogger.d("not all accepted");
                            for (PermissionGrantedResponse p : lst_permissionGrantedResponse) {
                                TrustLogger.d("permission not acepted: " + p.getPermissionName());
                            }
                            Hawk.put(Constants.PERMISSIONS, "0");
                            dialogPermissionListener.applyPermission(false);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                        Hawk.put(Constants.PERMISSIONS, "0");
                        dialogPermissionListener.applyPermission(false);

                    }
                }).check();
    }

    private void checkPermissionsAll(Activity activity) {
        Dexter.withActivity(activity)
                .withPermissions(READ_PHONE_STATE,
                        ACCESS_COARSE_LOCATION,
                        CAMERA,
                        ACCESS_FINE_LOCATION,
                        READ_SMS,
                        RECEIVE_SMS, PROCESS_OUTGOING_CALLS)
                .withListener(new MultiplePermissionsListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        List<PermissionGrantedResponse> lst_permissionGrantedResponse = report.getGrantedPermissionResponses();
                        List<PermissionDeniedResponse> lst_permissionDeniedResponse = report.getDeniedPermissionResponses();
                        if (lst_permissionDeniedResponse.size() == 0) {
                            TrustLogger.d("all accepted");
                            Hawk.put(Constants.PERMISSIONS, "1");
                            dialogPermissionListener.applyPermission(true);
                        } else {
                            TrustLogger.d("not all accepted");
                            for (PermissionGrantedResponse p : lst_permissionGrantedResponse) {
                                TrustLogger.d("permission not acepted: " + p.getPermissionName());
                            }
                            Hawk.put(Constants.PERMISSIONS, "0");

                            dialogPermissionListener.applyPermission(false);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                        Hawk.put(Constants.PERMISSIONS, "0");
                        dialogPermissionListener.applyPermission(false);
                    }
                }).check();
    }

    private void checkPermissionsWithSMS(Activity activity) {
        Dexter.withActivity(activity)
                .withPermissions(READ_PHONE_STATE,
                        ACCESS_COARSE_LOCATION,
                        CAMERA,
                        ACCESS_FINE_LOCATION,
                        READ_SMS,
                        RECEIVE_SMS)
                .withListener(new MultiplePermissionsListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        List<PermissionGrantedResponse> lst_permissionGrantedResponse = report.getGrantedPermissionResponses();
                        List<PermissionDeniedResponse> lst_permissionDeniedResponse = report.getDeniedPermissionResponses();
                        if (lst_permissionDeniedResponse.size() == 0) {
                            Hawk.put(Constants.PERMISSIONS, "1");
                            dialogPermissionListener.applyPermission(true);
                        } else {
                            TrustLogger.d("not all accepted");
                            for (PermissionGrantedResponse p : lst_permissionGrantedResponse) {
                                TrustLogger.d("permission not acepted: " + p.getPermissionName());
                            }
                            Hawk.put(Constants.PERMISSIONS, "0");
                            dialogPermissionListener.applyPermission(false);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                        Hawk.put(Constants.PERMISSIONS, "0");
                        dialogPermissionListener.applyPermission(false);

                    }
                }).check();
    }

    private void checkPermissionsWithCall(Activity activity) {
        Dexter.withActivity(activity)
                .withPermissions(READ_PHONE_STATE,
                        ACCESS_COARSE_LOCATION,
                        CAMERA,
                        ACCESS_FINE_LOCATION,
                        PROCESS_OUTGOING_CALLS)
                .withListener(new MultiplePermissionsListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        List<PermissionGrantedResponse> lst_permissionGrantedResponse = report.getGrantedPermissionResponses();
                        List<PermissionDeniedResponse> lst_permissionDeniedResponse = report.getDeniedPermissionResponses();
                        if (lst_permissionDeniedResponse.size() == 0) {
                            TrustLogger.d("all accepted");
                            Hawk.put(Constants.PERMISSIONS, "1");
                            dialogPermissionListener.applyPermission(true);
                        } else {
                            TrustLogger.d("not all accepted");
                            for (PermissionGrantedResponse p : lst_permissionGrantedResponse) {
                                TrustLogger.d("permission not acepted: " + p.getPermissionName());
                            }
                            Hawk.put(Constants.PERMISSIONS, "0");
                            dialogPermissionListener.applyPermission(false);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                        Hawk.put(Constants.PERMISSIONS, "0");
                        dialogPermissionListener.applyPermission(false);
                    }
                }).check();
    }
}

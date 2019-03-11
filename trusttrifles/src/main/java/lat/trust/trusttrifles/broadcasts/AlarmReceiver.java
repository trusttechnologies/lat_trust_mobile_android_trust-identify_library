package lat.trust.trusttrifles.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import java.util.ArrayList;

import lat.trust.trusttrifles.TrustClient;
import lat.trust.trusttrifles.TrustListener;
import lat.trust.trusttrifles.model.Audit;
import lat.trust.trusttrifles.utilities.AutomaticAudit;
import lat.trust.trusttrifles.utilities.LocationGPS;
import lat.trust.trusttrifles.utilities.TrustLogger;
import lat.trust.trusttrifles.utilities.Utils;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String OPERATION = "AUTOMATIC AUDIT ALARM";
    public static final String METHOD = "DIARY ALARM";
    public static final String RESULT="";

    @Override
    public void onReceive(Context context, Intent intent) {
        TrustLogger.d("[ALARM RECEIVER] INIT ALARM: ");
        AutomaticAudit.createAutomaticAudit(
                OPERATION,
                METHOD,
                RESULT,
               context);
    }







}


package lat.trust.trusttrifles.utilities;

import android.content.Context;
import android.content.Intent;

import com.google.gson.annotations.SerializedName;
import com.orhanobut.hawk.Hawk;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.sentry.Sentry;
import lat.trust.trusttrifles.model.CallbackACK;
import lat.trust.trusttrifles.model.NotificationFirebase;
import lat.trust.trusttrifles.services.RemoteAuditService;

public class RemoteMessagesId {

    /**
     * get the message from firebase for automatic audit or change the time of the diary audomatic audit.
     * type: change_automatic_audit for change the time of the automatic audit
     * type: remote_automatic_audit for start a automatic audit.
     *
     * @param context
     * @param data
     */
    public static void remoteMessageId(Context context, Map<String, String> data) {
        try {
            String type = data.get("type");
            switch (type == null ? "" : type) {
                case "change_automatic_audit":
                    int hour = Integer.parseInt(data.get("hour") != null ? data.get("hour") : "12");
                    int min = Integer.parseInt(data.get("min") != null ? data.get("min") : "00");
                    AutomaticAudit.setAutomaticAlarm(context, hour, min, 0);
                    break;
                case "remote_automatic_audit":
                    context.startService(new Intent(context, RemoteAuditService.class));
                    break;
                default:
                    TrustLogger.d("[RemoteMessages id] Invalid request");
            }
        } catch (Exception e) {
            Sentry.capture(e);
            TrustLogger.d("[RemoteMessages id] error : " + e.getMessage());
        }
    }

    /**
     * get the message from firebase for automatic audit or change the time of the diary audomatic audit and send a callback to service for ack.
     * type: change_automatic_audit for change the time of the automatic audit
     * type: remote_automatic_audit for start a automatic audit.
     *
     * @param context
     * @param data
     * @param callbackACK
     */
    public static void remoteMessageId(Context context, Map<String, String> data, CallbackACK callbackACK) {
        try {
            NotificationAck.sendACK(callbackACK);
        } catch (Exception ex) {
            TrustLogger.d("[TRUST CLIENT] ERROR SEND NOTIFICATION ACK: " + ex.getMessage());
            Sentry.capture(ex);
        } finally {
            remoteMessageId(context, data);
        }
    }

    public static void remoteMessageId(NotificationFirebase notificationFirebase) {
        try {
            Hawk.put(Constants.MESSAGE_ID, notificationFirebase.getMessageId());
            String type = notificationFirebase.getData().get("type");

            switch (type == null ? "" : type) {
                case "change_automatic_audit":
                    int hour = 0;
                    int min = 0;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        hour = Integer.parseInt(Objects.requireNonNull(notificationFirebase.getData().get("hour") != null ? notificationFirebase.getData().get("hour") : "12"));
                        min = Integer.parseInt(Objects.requireNonNull(notificationFirebase.getData().get("min") != null ? notificationFirebase.getData().get("min") : "00"));
                    } else {
                        hour = Integer.parseInt(notificationFirebase.getData().get("hour") != null ? notificationFirebase.getData().get("hour") : "12");
                        min = Integer.parseInt(notificationFirebase.getData().get("min") != null ? notificationFirebase.getData().get("min") : "00");

                    }
                    AutomaticAudit.setAutomaticAlarm(notificationFirebase.getContext(), hour, min, 0);
                    break;
                case "remote_automatic_audit":
                    notificationFirebase.getContext().startService(new Intent(notificationFirebase.getContext(), RemoteAuditService.class));
                    break;
                default:
                    TrustLogger.d("[RemoteMessages id] Invalid request");
            }
        } catch (Exception ex) {
            Sentry.capture(ex);
            TrustLogger.d("[RemoteMessages id] error : " + ex.getMessage());
        }
    }
}

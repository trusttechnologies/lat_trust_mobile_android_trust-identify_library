package lat.trust.trusttrifles.utilities;

import android.content.Context;
import android.content.Intent;

import java.util.Map;

import lat.trust.trusttrifles.services.RemoteAuditService;

public class RemoteMessagesId {

    public static void remoteMessageId(Context context, Map<String, String> data) {
        try {
            String type = data.get("type");
            switch (type == null ? "":type) {
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
            TrustLogger.d("[RemoteMessages id] error : " + e.getMessage());
        }
    }
}

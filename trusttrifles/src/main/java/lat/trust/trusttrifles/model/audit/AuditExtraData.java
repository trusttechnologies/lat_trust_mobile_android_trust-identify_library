package lat.trust.trusttrifles.model.audit;

import com.google.gson.Gson;

public class AuditExtraData {

    private String object_json;

    private static String getObjectJson(Object myObj) {
        Gson gson = new Gson();
        return gson.toJson(myObj);
    }
}

package lat.trust.trusttrifles.managers;

import com.orhanobut.hawk.Hawk;

import java.lang.reflect.Array;
import java.util.ArrayList;

import lat.trust.trusttrifles.model.StringsModel;

import static lat.trust.trusttrifles.utilities.Constants.LIST_LOG;

public class LogManager {


    public static void addLog(String msg) {
        ArrayList<StringsModel> list = getList();
        list.add(new StringsModel(msg, "green"));
        Hawk.put(LIST_LOG, list);
    }

    public static void addLogError(String msg) {
        ArrayList<StringsModel> list = getList();
        list.add(new StringsModel(msg, "red"));
        Hawk.put(LIST_LOG, list);
    }

    public static void clearLog() {
        if (Hawk.contains(LIST_LOG)) {
            Hawk.delete(LIST_LOG);
        }
    }

    public static ArrayList<StringsModel> getList() {
        ArrayList<StringsModel> list = new ArrayList<>();
        if (Hawk.contains(LIST_LOG)) {
            list = Hawk.get(LIST_LOG);
        }
        return list;
    }
}

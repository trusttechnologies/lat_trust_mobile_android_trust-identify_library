package lat.trust.trusttrifles.managers;

import com.orhanobut.hawk.Hawk;

import lat.trust.trusttrifles.model.Identity;
import lat.trust.trusttrifles.utilities.Constants;

public class IdentifyManager {

    public static void saveIdentity(Identity identity) {
        Hawk.put(Constants.IDENTITY, identity);
    }

    public static void deleteIdentity() {
        if (Hawk.contains(Constants.IDENTITY)) {
            Hawk.delete(Constants.IDENTITY);
        }
    }
}

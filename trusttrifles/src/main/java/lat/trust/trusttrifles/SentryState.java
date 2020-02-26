package lat.trust.trusttrifles;

import android.content.Context;

import com.orhanobut.hawk.Hawk;

import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;
import lat.trust.trusttrifles.utilities.Constants;

import static lat.trust.trusttrifles.utilities.Constants.SENTRY_STATE;

public class SentryState {
    public enum SENTRY_IMPORTANCE {
        IMPORTANCE_DEFAULT,
        IMPORTANCE_NONE,
        IMPORTANCE_HIGH
    }

    static void init(Context context) {
        Sentry.init(Constants.SENTRY_DSN, new AndroidSentryClientFactory(context));
        setImportance(SENTRY_IMPORTANCE.IMPORTANCE_DEFAULT);
    }

    public static void setImportance(SENTRY_IMPORTANCE importance) {
        switch (importance) {
            case IMPORTANCE_HIGH: {
                Hawk.put(SENTRY_STATE, SENTRY_IMPORTANCE.IMPORTANCE_HIGH);
                break;
            }
            case IMPORTANCE_NONE: {
                Hawk.put(SENTRY_STATE, SENTRY_IMPORTANCE.IMPORTANCE_NONE);
                break;
            }
            case IMPORTANCE_DEFAULT: {
                Hawk.put(SENTRY_STATE, SENTRY_IMPORTANCE.IMPORTANCE_DEFAULT);
                break;
            }
            default: {
                Hawk.put(SENTRY_STATE, SENTRY_IMPORTANCE.IMPORTANCE_DEFAULT);
            }
        }
    }

    public static SENTRY_IMPORTANCE getImportance() {
        if (Hawk.contains(SENTRY_STATE) && Hawk.get(SENTRY_STATE) != null) {
            return Hawk.get(SENTRY_STATE);
        }
        return SENTRY_IMPORTANCE.IMPORTANCE_DEFAULT;
    }

    public static boolean isImportantHigh() {
        return Hawk.contains(SENTRY_STATE) && Hawk.get(SENTRY_STATE).equals(SENTRY_IMPORTANCE.IMPORTANCE_HIGH);
    }

    public static boolean isImportantDefault() {
        return Hawk.contains(SENTRY_STATE) && Hawk.get(SENTRY_STATE).equals(SENTRY_IMPORTANCE.IMPORTANCE_DEFAULT);
    }

    public static boolean isImportantNone() {
        return Hawk.contains(SENTRY_STATE) && Hawk.get(SENTRY_STATE).equals(SENTRY_IMPORTANCE.IMPORTANCE_NONE);
    }

    public static boolean isImportantDefaultOrHigh() {
        return isImportantHigh() || isImportantDefault();
    }

}

package lat.trust.trusttrifles.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

@SuppressLint("StaticFieldLeak")
public class TrustPreferences {
    public static final String TRUST_PREFERENCES = "Trust_Preferences";
    private static Context mContext;
    private static TrustPreferences mPreferences;
    private SharedPreferences prefs;
    private SharedPreferences.Editor mEditor;


    public TrustPreferences() {
        prefs = mContext.getSharedPreferences(TRUST_PREFERENCES,
                Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        mContext = context;
        mPreferences = new TrustPreferences();
    }

    public static TrustPreferences getInstance() {
        return mPreferences;
    }

    public void put(String name, String value) {
        if (mEditor == null) mEditor = prefs.edit();
        mEditor.putString(name, value);
        mEditor.apply();
    }

    public void put(String name, int value) {
        if (mEditor == null) mEditor = prefs.edit();
        mEditor.putInt(name, value);
        mEditor.apply();
    }

    public void put(String name, Set<String> value) {
        if (mEditor == null) mEditor = prefs.edit();
        mEditor.putStringSet(name, value);
        mEditor.apply();
    }

    public void put(String name, boolean value) {
        if (mEditor == null) mEditor = prefs.edit();
        mEditor.putBoolean(name, value);
        mEditor.apply();
    }

    public void put(String name, float value) {
        if (mEditor == null) mEditor = prefs.edit();
        mEditor.putFloat(name, value);
        mEditor.apply();
    }

    public void put(String name, long value) {
        if (mEditor == null) mEditor = prefs.edit();
        mEditor.putLong(name, value);
        mEditor.apply();
    }

    public String getString(String name) {
        return prefs.getString(name, null);
    }

    public int getInt(String name) {
        return prefs.getInt(name, -1);
    }

    public float getFloat(String name) {
        return prefs.getFloat(name, -1);
    }

    public Set<String> getStringSet(String name) {
        return prefs.getStringSet(name, null);
    }

    public long getLong(String name) {
        return prefs.getLong(name, -1);
    }

    public boolean getBoolean(String name) {
        return prefs.getBoolean(name, false);
    }


}

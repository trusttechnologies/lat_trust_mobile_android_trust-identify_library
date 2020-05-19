package lat.trust.trusttrifles.utilities;

import android.util.Base64;

public class CryptUtil {


    public static String encrypt(String dataIn) {
        String re = "";
        if (dataIn == null || dataIn.isEmpty())
            return null;
        else
            return Base64.encodeToString(dataIn.getBytes(), Base64.DEFAULT);
    }

    public static String decrypt(String dataOut) {
        String re = "";
        if (dataOut == null || dataOut.isEmpty())
            return null;
        else
            return new String(Base64.decode(dataOut, Base64.DEFAULT));
    }


}
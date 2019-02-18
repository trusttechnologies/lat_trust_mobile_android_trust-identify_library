package lat.trust.trustdemo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy - HH:mm:ss 'hrs'", Locale.getDefault());
        return formatter.format(date);
    }
}

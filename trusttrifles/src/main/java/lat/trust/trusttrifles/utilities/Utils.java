package lat.trust.trusttrifles.utilities;

public class Utils {
    /**
     * Este metodo extrae informacion de un string del tipo "key: value type"
     *
     * @param line linea con informacion a extraer
     * @return el valor extraido de la linea
     */
    public static String getValue(String line) {
        String[] segs = line.trim().split(":");
        if (segs.length >= 2) {
            return segs[1];
        }
        return null;
    }

    /**
     * Este metodo extrae informacion de un string del tipo "key: value type"
     *
     * @param line linea con informacion a extraer
     * @return la key extraida de la linea
     */
    public static String getKey(String line) {
        String[] segs = line.trim().split(":");
        if (segs.length >= 2) {
            return segs[0];
        }
        return null;
    }
}

package lat.trust.trusttrifles.utilities;

public class Constants {

    public final static String MEM_FILE = "/proc/meminfo";
    public final static String CPU_FILE = "/proc/cpuinfo";

    //Comandos cortos (dependientes de la compilacion, con flash cambian frecuentemente)
    public final static String KERNEL_INFO = "/proc/sys/kernel/version";
    public final static String OS_RELEASE = "/proc/sys/kernel/osrelease";
    public final static String BOOT_ID = "/proc/sys/kernel/random/boot_id";

    //Archivos completos
    //Editable con root
    public final static String BUILD_PROP = "/system/build.prop";
    //No siempre se permite la lectura
    public final static String WLAN = "/system/etc/firmware/wlan/prima/WCNSS_qcom_cfg.ini";
    public final static String PARTITIONS = "/proc/partitions";


    public final static String SIM_RECEIVER_TAG = "SIMRECEIVER";
    public final static String IMEI_RECEIVER_TAG = "IMEIRECEIVER";

    public final static String TRUST_TRIFLES = "TRUSTLIB_TRUST_TRIFLES";
    public final static String TRUST_ID = "TRUSTLIB_TRUST_ID";


    public static final String TRUST_ID_AUTOMATIC = "TRUST_ID_AUTOMATIC";

    public static final String WIFI_CONNECTION = "WIFI";
    public static final String MOBILE_CONNECTION = "MOBILE";

    public static final String DISCONNECT = "DISCONNECT";


    public static final String DNI_USER = "dni";
    public static final String NAME_USER = "name";
    public static final String LASTNAME_USER = "lastname";
    public static final String EMAIL_USER = "email";
    public static final String PHONE_USER = "phone";

    public static final String LATITUDE ="lat";
    public static final String LONGITUDE ="lng";

}

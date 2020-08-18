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

    public static final String LATITUDE = "lat";
    public static final String LONGITUDE = "lng";

    public static final String SENTRY_DSN = "https://c591083c8bb54bea810e97db7ae38f51@sentry.io/1433867";

    public static final String BUNDLE_ID = "bundle";

    public static final String TOKEN_SERVICE = "token_service";


    public static final String CLIENT_ID = "client_id";
    public static final String CLIENT_SECRET = "client_secret";
    public static final String MESSAGE_ID = "message_id";

    public static final String TRUST_NOTIFICATION_SUCCESS_CODE = "2";
    public static final String TRUST_NOTIFICATION_CANCEL_CODE = "3";

    public static final String LST_AUDIT = "lst_audit";

    public static final String SIM_IMSI = "sim_imsi";

    public static final String LST_UUID = "lst_uuid";
    public static final String UUID = "uuid";

    public static final String PERMISSIONS = "permission";

    public static final String WIFI_STATUS = "wifi_status";
    public static final String BLUETOOTH_STATUS = "bluetooth_status";

    public static final String IDENTITY = "com.trust.identity";

    public static final String BUNDLE_ID_IDENTIFY = "bundle.id.identify";

    public static final String SENTRY_STATE = "com.trust.identify.sentry_state";
    public static final String TRUST_ID_TYPE_ZERO = "trust_id_zero";
    public static final String TRUST_ID_TYPE_ZERO_SAVED = "trust_id_zero_saved";
    public static final String AUDIT_TRUST_ID = "audit_trust_id";


    public static final String SDK_IDENTIFY = "sdk_identify";
    public static final String OPERATION  = "overwrite";
}

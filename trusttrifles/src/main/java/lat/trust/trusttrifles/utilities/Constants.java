package lat.trust.trusttrifles.utilities;

public class Constants {

    public final static String MEM_FILE = "/proc/meminfo";
    public final static String CPU_FILE = "/proc/cpuinfo";

    //Comandos cortos (dependientes de la compilación, con flash cambian frecuentemente)
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
}

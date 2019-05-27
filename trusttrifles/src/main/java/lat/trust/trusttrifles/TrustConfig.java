package lat.trust.trusttrifles;

import com.orhanobut.hawk.Hawk;

import lat.trust.trusttrifles.utilities.TrustLogger;

public class TrustConfig {


    public static final String AUDIT_BOOT = "audit.boot";
    public static final String AUDIT_CALL = "audit.call";
    public static final String AUDIT_SMS = "audit.sms";
    public static final String AUDIT_SIM = "audit.sim";
    public static final String AUDIT_ALARM = "audit.alarm";
    public static final String AUDIT_NETWORK = "audit.network";

    private static TrustConfig trustConfig;


    private TrustConfig() {

    }

    public void setAllAudits() {
        Hawk.put(AUDIT_NETWORK, "1");
        Hawk.put(AUDIT_ALARM, "1");
        Hawk.put(AUDIT_SIM, "1");
        Hawk.put(AUDIT_SMS, "1");
        Hawk.put(AUDIT_CALL, "1");
        Hawk.put(AUDIT_BOOT, "1");
    }

    public void setNoneAudits() {
        Hawk.put(AUDIT_NETWORK, "0");
        Hawk.put(AUDIT_ALARM, "0");
        Hawk.put(AUDIT_SIM, "0");
        Hawk.put(AUDIT_SMS, "0");
        Hawk.put(AUDIT_CALL, "0");
        Hawk.put(AUDIT_BOOT, "0");
    }

    public void setAudits(String[] audits) {
        setDefaultConfig();
        for (String audit : audits) {
            switch (audit) {
                case AUDIT_BOOT: {
                    Hawk.put(AUDIT_BOOT, "1");

                    break;
                }
                case AUDIT_CALL: {
                    Hawk.put(AUDIT_CALL, "1");

                    break;
                }
                case AUDIT_SMS: {
                    Hawk.put(AUDIT_SMS, "1");

                    break;
                }
                case AUDIT_SIM: {
                    Hawk.put(AUDIT_SIM, "1");


                    break;
                }

                case AUDIT_ALARM: {
                    Hawk.put(AUDIT_ALARM, "1");

                    break;
                }
                case AUDIT_NETWORK: {
                    Hawk.put(AUDIT_NETWORK, "1");

                    break;
                }
                default: {
                    TrustLogger.d("no supported");
                }
            }

        }
    }

    private void setDefaultConfig() {

        Hawk.put(AUDIT_BOOT, "0");
        Hawk.put(AUDIT_ALARM, "0");
        Hawk.put(AUDIT_CALL, "0");
        Hawk.put(AUDIT_NETWORK, "0");
        Hawk.put(AUDIT_SIM, "0");
        Hawk.put(AUDIT_SMS, "0");

    }

    public static void init() {
        trustConfig = new TrustConfig();

    }

    public boolean isBoot() {
        if (Hawk.contains(AUDIT_BOOT)) {
            return Hawk.get(AUDIT_BOOT).equals("1");
        }
        return false;
    }

    public boolean isCall() {
        if (Hawk.contains(AUDIT_CALL)) {
            return Hawk.get(AUDIT_CALL).equals("1");
        }
        return false;
    }

    public boolean isSms() {
        if (Hawk.contains(AUDIT_SMS)) {
            return Hawk.get(AUDIT_SMS).equals("1");
        }
        return false;
    }

    public static boolean isSim() {
        if (Hawk.contains(AUDIT_SIM)) {
            return Hawk.get(AUDIT_SIM).equals("1");
        }
        return false;
    }

    public boolean isAlarm() {
        if (Hawk.contains(AUDIT_ALARM)) {
            return Hawk.get(AUDIT_ALARM).equals("1");
        }
        return false;
    }

    public boolean isNetwork() {
        if (Hawk.contains(AUDIT_NETWORK)) {
            return Hawk.get(AUDIT_NETWORK).equals("1");
        }
        return false;
    }


    public static TrustConfig getInstance() {
        return trustConfig == null ? new TrustConfig() : trustConfig;
    }

    public void setTrustConfig(TrustConfig trustConfig) {
        this.trustConfig = trustConfig;
    }


}

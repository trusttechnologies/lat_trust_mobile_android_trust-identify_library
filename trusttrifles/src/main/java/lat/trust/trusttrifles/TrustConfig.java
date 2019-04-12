package lat.trust.trusttrifles;

import lat.trust.trusttrifles.utilities.TrustLogger;

public class TrustConfig {


    public static final String AUDIT_BOOT = "audit.boot";
    public static final String AUDIT_CALL = "audit.call";
    public static final String AUDIT_SMS = "audit.sms";
    public static final String AUDIT_SIM = "audit.sim";
    public static final String AUDIT_ALARM = "audit.alarm";
    public static final String AUDIT_NETWORK = "audit.network";

    private static TrustConfig trustConfig;

    private boolean boot;
    private boolean call;
    private boolean sms;
    private boolean sim;
    private boolean alarm;
    private boolean network;

    private TrustConfig() {

    }

    public void setAudits(String[] audits) {
        setDefaultConfig();
        for (String audit : audits) {
            switch (audit) {
                case AUDIT_BOOT: {
                    setBoot(true);
                    break;
                }
                case AUDIT_CALL: {
                    setCall(true);
                    break;
                }
                case AUDIT_SMS: {
                    setSms(true);
                    break;
                }
                case AUDIT_SIM: {
                    setSim(true);
                    break;
                }

                case AUDIT_ALARM: {
                    setAlarm(true);
                    break;
                }
                case AUDIT_NETWORK: {
                    setNetwork(true);
                    break;
                }
                default:{
                    TrustLogger.d("no supported");
                }
            }

        }
    }

    private void setDefaultConfig() {
        setNetwork(false);
        setAlarm(false);
        setSim(false);
        setSms(false);
        setCall(false);
        setBoot(false);
    }

    public static void init() {
        trustConfig = new TrustConfig();

    }

    public boolean isBoot() {
        return boot;
    }

    public boolean isCall() {
        return call;
    }

    public boolean isSms() {
        return sms;
    }

    public boolean isSim() {
        return sim;
    }

    public boolean isAlarm() {
        return alarm;
    }

    public boolean isNetwork() {
        return network;
    }


    public static TrustConfig getInstance() {
        return trustConfig == null ? new TrustConfig() : trustConfig;
    }

    public void setTrustConfig(TrustConfig trustConfig) {
        this.trustConfig = trustConfig;
    }

    private void setBoot(boolean boot) {
        this.boot = boot;
    }

    private void setCall(boolean call) {
        this.call = call;
    }

    private void setSms(boolean sms) {
        this.sms = sms;
    }

    private void setSim(boolean sim) {
        this.sim = sim;
    }

    private void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }

    private void setNetwork(boolean network) {
        this.network = network;
    }
}

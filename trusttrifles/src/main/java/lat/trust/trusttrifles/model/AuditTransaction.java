package lat.trust.trusttrifles.model;

public class AuditTransaction {
    private String operation;
    private String method;
    private String result;
    private long timestamp;

    public AuditTransaction(String operation, String method, String result, long timestamp) {
        this.operation = operation;
        this.method = method;
        this.timestamp = timestamp;
        this.result = result;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

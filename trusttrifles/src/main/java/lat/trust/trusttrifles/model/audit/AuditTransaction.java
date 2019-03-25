package lat.trust.trusttrifles.model.audit;

public class AuditTransaction {
    private String result;
    private String method;
    private String operation;
    private long timestamp;

    public AuditTransaction() {
    }

    public AuditTransaction(String result, String method, String operation, long timestamp) {
        this.result = result;
        this.method = method;
        this.operation = operation;
        this.timestamp = timestamp;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    /*"result": "RECEIVER WIFI AUDIT",
		"timestamp":1553362693,
		"method": "RECEIVER WIFI AUDIT",
		"operation": "AUTOMATIC WIFI AUDIT"*/
}

package points.transforming.app.server.exceptions.report;

public class ViolationReport {
    private String cause;
    private String message;
    private String field;

    public ViolationReport() { }

    public ViolationReport(String value, String message, String currentField) {
        this.cause = value;
        this.message = message;
        this.field = currentField;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}

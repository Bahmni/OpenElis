package us.mn.state.health.lims.common.exception;

public class LIMSValidationException extends LIMSRuntimeException {
    protected Exception exception = null;
    protected String message = "";

    public LIMSValidationException(String message, Exception exception) {
        super(message, exception);
        this.message = message;
        this.exception = exception;
    }

    public LIMSValidationException(Exception pException) {
        this("Validation failed: ", pException);
    }

    public String getMessage() {
        return (exception != null) ?  message + exception.getMessage() : message;
    }
}

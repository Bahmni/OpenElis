package us.mn.state.health.lims.common.exception;

public class LIMSInvalidSTNumberException extends LIMSRuntimeException {
    public LIMSInvalidSTNumberException(String pMessage, Exception pException) {
        super(pMessage, pException);
    }
}

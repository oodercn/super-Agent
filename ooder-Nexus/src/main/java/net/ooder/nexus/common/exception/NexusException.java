package net.ooder.nexus.common.exception;

/**
 * 基础业务异常
 */
public class NexusException extends RuntimeException {

    private final String errorCode;
    private final int httpStatus;

    public NexusException(String message) {
        super(message);
        this.errorCode = "NEXUS_ERROR";
        this.httpStatus = 500;
    }

    public NexusException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = 500;
    }

    public NexusException(String errorCode, String message, int httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public NexusException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = 500;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}

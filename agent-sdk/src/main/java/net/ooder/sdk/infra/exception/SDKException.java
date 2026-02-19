
package net.ooder.sdk.infra.exception;

public class SDKException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    private final String errorCode;
    private final String errorDetails;
    
    public SDKException(String message) {
        super(message);
        this.errorCode = "SDK_ERROR";
        this.errorDetails = null;
    }
    
    public SDKException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.errorDetails = null;
    }
    
    public SDKException(String errorCode, String message, String errorDetails) {
        super(message);
        this.errorCode = errorCode;
        this.errorDetails = errorDetails;
    }
    
    public SDKException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "SDK_ERROR";
        this.errorDetails = null;
    }
    
    public SDKException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorDetails = null;
    }
    
    public SDKException(String errorCode, String message, String errorDetails, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorDetails = errorDetails;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public String getErrorDetails() {
        return errorDetails;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SDKException{");
        sb.append("errorCode='").append(errorCode).append('\'');
        sb.append(", message='").append(getMessage()).append('\'');
        if (errorDetails != null) {
            sb.append(", details='").append(errorDetails).append('\'');
        }
        if (getCause() != null) {
            sb.append(", cause='").append(getCause().getMessage()).append('\'');
        }
        sb.append('}');
        return sb.toString();
    }
}

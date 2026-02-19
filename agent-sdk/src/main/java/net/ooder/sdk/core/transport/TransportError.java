package net.ooder.sdk.core.transport;

public class TransportError {
    
    private String messageId;
    private String errorCode;
    private String errorMessage;
    private Throwable cause;
    
    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }
    
    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    
    public Throwable getCause() { return cause; }
    public void setCause(Throwable cause) { this.cause = cause; }
}

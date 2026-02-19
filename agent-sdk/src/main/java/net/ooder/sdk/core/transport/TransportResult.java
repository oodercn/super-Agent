package net.ooder.sdk.core.transport;

public class TransportResult {
    
    private String messageId;
    private boolean success;
    private String errorCode;
    private String errorMessage;
    private long processingTime;
    private byte[] response;
    
    public static TransportResult success(String messageId) {
        TransportResult result = new TransportResult();
        result.setMessageId(messageId);
        result.setSuccess(true);
        return result;
    }
    
    public static TransportResult failure(String messageId, String errorCode, String errorMessage) {
        TransportResult result = new TransportResult();
        result.setMessageId(messageId);
        result.setSuccess(false);
        result.setErrorCode(errorCode);
        result.setErrorMessage(errorMessage);
        return result;
    }
    
    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }
    
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    
    public long getProcessingTime() { return processingTime; }
    public void setProcessingTime(long processingTime) { this.processingTime = processingTime; }
    
    public byte[] getResponse() { return response; }
    public void setResponse(byte[] response) { this.response = response; }
}

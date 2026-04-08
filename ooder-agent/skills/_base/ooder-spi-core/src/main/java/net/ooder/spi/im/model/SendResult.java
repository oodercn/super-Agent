package net.ooder.spi.im.model;

public class SendResult {

    private boolean success;
    private String messageId;
    private String errorCode;
    private String errorMessage;
    private long timestamp;

    public SendResult() {}

    public SendResult(boolean success, String messageId, String errorCode, String errorMessage) {
        this.success = success;
        this.messageId = messageId;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.timestamp = System.currentTimeMillis();
    }

    public static SendResult success(String messageId) {
        return new SendResult(true, messageId, null, null);
    }

    public static SendResult failure(String errorCode, String errorMessage) {
        return new SendResult(false, null, errorCode, errorMessage);
    }

    public static SendResult failure(String error) {
        return new SendResult(false, null, "ERROR", error);
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }
    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}

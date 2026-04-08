package net.ooder.skill.common.spi.im;

public class SendResult {
    
    private boolean success;
    private String messageId;
    private String error;
    private long timestamp;
    
    public SendResult() {}
    
    public SendResult(boolean success, String messageId, String error) {
        this.success = success;
        this.messageId = messageId;
        this.error = error;
        this.timestamp = System.currentTimeMillis();
    }
    
    public static SendResult success(String messageId) {
        SendResult r = new SendResult();
        r.success = true;
        r.messageId = messageId;
        r.timestamp = System.currentTimeMillis();
        return r;
    }
    
    public static SendResult failure(String error) {
        SendResult r = new SendResult();
        r.success = false;
        r.error = error;
        r.timestamp = System.currentTimeMillis();
        return r;
    }
    
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}

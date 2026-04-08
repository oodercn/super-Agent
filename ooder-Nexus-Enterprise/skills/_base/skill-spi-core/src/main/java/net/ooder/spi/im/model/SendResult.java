package net.ooder.spi.im.model;

import lombok.Data;

@Data
public class SendResult {
    
    private boolean success;
    
    private String messageId;
    
    private String errorCode;
    
    private String errorMessage;
    
    private Long timestamp;
    
    public static SendResult success(String messageId) {
        SendResult result = new SendResult();
        result.setSuccess(true);
        result.setMessageId(messageId);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }
    
    public static SendResult failure(String errorCode, String errorMessage) {
        SendResult result = new SendResult();
        result.setSuccess(false);
        result.setErrorCode(errorCode);
        result.setErrorMessage(errorMessage);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }
}

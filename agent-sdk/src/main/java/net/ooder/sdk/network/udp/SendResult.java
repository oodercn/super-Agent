package net.ooder.sdk.network.udp;

/**
 * UDP发送结果
 */
public class SendResult {
    private boolean success;
    private int bytesSent;
    private String errorMessage;
    private long sendTime;
    
    /**
     * 构建成功结果
     * @param bytesSent 发送的字节数
     * @return 发送结果
     */
    public static SendResult success(int bytesSent) {
        return new SendResult(true, bytesSent, null);
    }
    
    /**
     * 构建失败结果
     * @param errorMessage 错误信息
     * @return 发送结果
     */
    public static SendResult failure(String errorMessage) {
        return new SendResult(false, 0, errorMessage);
    }
    
    private SendResult(boolean success, int bytesSent, String errorMessage) {
        this.success = success;
        this.bytesSent = bytesSent;
        this.errorMessage = errorMessage;
        this.sendTime = System.currentTimeMillis();
    }
    
    /**
     * 是否发送成功
     * @return 发送结果
     */
    public boolean isSuccess() {
        return success;
    }
    
    /**
     * 获取发送的字节数
     * @return 字节数
     */
    public int getBytesSent() {
        return bytesSent;
    }
    
    /**
     * 获取错误信息
     * @return 错误信息
     */
    public String getErrorMessage() {
        return errorMessage;
    }
    
    /**
     * 获取消息（兼容旧版本）
     * @return 消息
     */
    public String getMessage() {
        return success ? "Success" : errorMessage;
    }
    
    /**
     * 获取发送时间
     * @return 发送时间
     */
    public long getSendTime() {
        return sendTime;
    }
    
    @Override
    public String toString() {
        if (success) {
            return "SendResult{success=true, bytesSent=" + bytesSent + ", sendTime=" + sendTime + "}";
        } else {
            return "SendResult{success=false, errorMessage='" + errorMessage + "', sendTime=" + sendTime + "}";
        }
    }
}

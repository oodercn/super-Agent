package net.ooder.sdk.core.transport;

public enum AckStatus {
    DELIVERED(1, "已送达"),
    READ(2, "已读"),
    FAILED(3, "发送失败"),
    PENDING(4, "待确认"),
    TIMEOUT(5, "确认超时");
    
    private final int code;
    private final String description;
    
    AckStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public int getCode() { return code; }
    
    public String getDescription() { return description; }
    
    public static AckStatus fromCode(int code) {
        for (AckStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return PENDING;
    }
}

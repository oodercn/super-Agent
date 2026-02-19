package net.ooder.sdk.core.collaboration;

public enum MsgStatus {
    NORMAL(0, "普通"),
    URGENT(1, "紧急"),
    READ(2, "已读"),
    UNREAD(3, "未读"),
    DELIVERED(4, "已送达"),
    FAILED(5, "发送失败"),
    PENDING(6, "待发送"),
    SENDING(7, "发送中");
    
    private final int code;
    private final String description;
    
    MsgStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public int getCode() { return code; }
    
    public String getDescription() { return description; }
    
    public static MsgStatus fromCode(int code) {
        for (MsgStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return NORMAL;
    }
}

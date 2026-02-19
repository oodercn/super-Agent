package net.ooder.sdk.api.protocol;

public enum CommandStatus {
    PENDING(1, "待执行"),
    RUNNING(2, "执行中"),
    SUCCESS(3, "执行成功"),
    FAILED(4, "执行失败"),
    TIMEOUT(5, "执行超时"),
    CANCELLED(6, "已取消"),
    ROLLBACK(7, "已回滚"),
    RETRYING(8, "重试中");
    
    private final int code;
    private final String description;
    
    CommandStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public int getCode() { return code; }
    
    public String getDescription() { return description; }
    
    public static CommandStatus fromCode(int code) {
        for (CommandStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return PENDING;
    }
    
    public boolean isTerminal() {
        return this == SUCCESS || this == FAILED || this == TIMEOUT || this == CANCELLED || this == ROLLBACK;
    }
}

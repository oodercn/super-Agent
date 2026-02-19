package net.ooder.sdk.api.protocol;

public enum CommandDirection {
    NORTHBOUND(1, "北向命令"),
    SOUTHBOUND(2, "南向命令"),
    INTERNAL(3, "内部命令");
    
    private final int code;
    private final String description;
    
    CommandDirection(int code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public int getCode() { return code; }
    
    public String getDescription() { return description; }
    
    public static CommandDirection fromCode(int code) {
        for (CommandDirection direction : values()) {
            if (direction.code == code) {
                return direction;
            }
        }
        return INTERNAL;
    }
}

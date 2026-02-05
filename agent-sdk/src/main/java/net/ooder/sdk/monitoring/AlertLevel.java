package net.ooder.sdk.monitoring;

public enum AlertLevel {
    INFO("info", "信息", 1),
    WARNING("warning", "警告", 2),
    ERROR("error", "错误", 3),
    CRITICAL("critical", "严重", 4),
    EMERGENCY("emergency", "紧急", 5);
    
    private final String value;
    private final String description;
    private final int severity;
    
    AlertLevel(String value, String description, int severity) {
        this.value = value;
        this.description = description;
        this.severity = severity;
    }
    
    public String getValue() {
        return value;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getSeverity() {
        return severity;
    }
    
    @Override
    public String toString() {
        return value;
    }
    
    public static AlertLevel fromValue(String value) {
        for (AlertLevel level : AlertLevel.values()) {
            if (level.value.equals(value)) {
                return level;
            }
        }
        return INFO;
    }
}

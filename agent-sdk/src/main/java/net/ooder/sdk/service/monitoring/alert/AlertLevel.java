
package net.ooder.sdk.service.monitoring.alert;

public enum AlertLevel {
    
    INFO("info", 0),
    WARNING("warning", 1),
    ERROR("error", 2),
    CRITICAL("critical", 3);
    
    private final String code;
    private final int severity;
    
    AlertLevel(String code, int severity) {
        this.code = code;
        this.severity = severity;
    }
    
    public String getCode() {
        return code;
    }
    
    public int getSeverity() {
        return severity;
    }
    
    public boolean isAtLeast(AlertLevel level) {
        return this.severity >= level.severity;
    }
}

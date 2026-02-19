
package net.ooder.sdk.service.monitoring.health;

public enum HealthStatus {
    
    HEALTHY("healthy", "System is operating normally"),
    DEGRADED("degraded", "System is operating with reduced functionality"),
    UNHEALTHY("unhealthy", "System is not operating correctly"),
    UNKNOWN("unknown", "System health status cannot be determined");
    
    private final String code;
    private final String description;
    
    HealthStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isHealthy() {
        return this == HEALTHY;
    }
    
    public boolean needsAttention() {
        return this == DEGRADED || this == UNHEALTHY;
    }
}

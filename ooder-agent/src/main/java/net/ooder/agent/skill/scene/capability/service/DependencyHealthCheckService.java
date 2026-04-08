package net.ooder.mvp.skill.scene.capability.service;

import net.ooder.mvp.skill.scene.capability.model.Capability;

import java.util.List;
import java.util.Map;

public interface DependencyHealthCheckService {
    
    HealthCheckResult checkDependency(String capabilityId);
    
    Map<String, HealthCheckResult> checkAllDependencies(List<String> capabilityIds);
    
    boolean isHealthy(String capabilityId);
    
    boolean isAllHealthy(List<String> capabilityIds);
    
    void registerHealthCheck(String capabilityId, HealthChecker checker);
    
    void unregisterHealthCheck(String capabilityId);
    
    public static class HealthCheckResult {
        private String capabilityId;
        private String capabilityName;
        private HealthStatus status;
        private String message;
        private long checkTime;
        private Map<String, Object> details;
        private long responseTimeMs;
        
        public enum HealthStatus {
            HEALTHY,
            UNHEALTHY,
            DEGRADED,
            UNKNOWN,
            CHECKING
        }
        
        public HealthCheckResult() {
            this.checkTime = System.currentTimeMillis();
            this.status = HealthStatus.UNKNOWN;
        }
        
        public static HealthCheckResult healthy(String capabilityId, String name) {
            HealthCheckResult result = new HealthCheckResult();
            result.setCapabilityId(capabilityId);
            result.setCapabilityName(name);
            result.setStatus(HealthStatus.HEALTHY);
            result.setMessage("服务正常");
            return result;
        }
        
        public static HealthCheckResult unhealthy(String capabilityId, String name, String message) {
            HealthCheckResult result = new HealthCheckResult();
            result.setCapabilityId(capabilityId);
            result.setCapabilityName(name);
            result.setStatus(HealthStatus.UNHEALTHY);
            result.setMessage(message);
            return result;
        }
        
        public static HealthCheckResult degraded(String capabilityId, String name, String message) {
            HealthCheckResult result = new HealthCheckResult();
            result.setCapabilityId(capabilityId);
            result.setCapabilityName(name);
            result.setStatus(HealthStatus.DEGRADED);
            result.setMessage(message);
            return result;
        }
        
        public static HealthCheckResult checking(String capabilityId, String name) {
            HealthCheckResult result = new HealthCheckResult();
            result.setCapabilityId(capabilityId);
            result.setCapabilityName(name);
            result.setStatus(HealthStatus.CHECKING);
            result.setMessage("正在检查...");
            return result;
        }
        
        public static HealthCheckResult unknown(String capabilityId, String name) {
            HealthCheckResult result = new HealthCheckResult();
            result.setCapabilityId(capabilityId);
            result.setCapabilityName(name);
            result.setStatus(HealthStatus.UNKNOWN);
            result.setMessage("无法检查");
            return result;
        }
        
        public String getCapabilityId() { return capabilityId; }
        public void setCapabilityId(String capabilityId) { this.capabilityId = capabilityId; }
        public String getCapabilityName() { return capabilityName; }
        public void setCapabilityName(String capabilityName) { this.capabilityName = capabilityName; }
        public HealthStatus getStatus() { return status; }
        public void setStatus(HealthStatus status) { this.status = status; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public long getCheckTime() { return checkTime; }
        public void setCheckTime(long checkTime) { this.checkTime = checkTime; }
        public Map<String, Object> getDetails() { return details; }
        public void setDetails(Map<String, Object> details) { this.details = details; }
        public long getResponseTimeMs() { return responseTimeMs; }
        public void setResponseTimeMs(long responseTimeMs) { this.responseTimeMs = responseTimeMs; }
        
        public boolean isHealthy() {
            return status == HealthStatus.HEALTHY || status == HealthStatus.DEGRADED;
        }
    }
    
    public interface HealthChecker {
        HealthCheckResult check(Capability capability);
        String getType();
    }
}

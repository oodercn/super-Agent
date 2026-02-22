package net.ooder.skillcenter.runtime.model;

import java.util.Date;

/**
 * 运行时状态 - 符合v0.7.0协议规范
 */
public class RuntimeStatus {
    
    private String runtimeId;
    private String skillId;
    private String language;
    private String version;
    private RuntimeState state;
    private Date startedAt;
    private Date lastHeartbeat;
    private long memoryUsed;
    private long memoryLimit;
    private double cpuUsage;
    private String errorMessage;
    
    public enum RuntimeState {
        INITIALIZING,
        STARTING,
        RUNNING,
        STOPPING,
        STOPPED,
        ERROR,
        UNKNOWN
    }
    
    public RuntimeStatus() {
        this.state = RuntimeState.UNKNOWN;
    }
    
    public static RuntimeStatus create(String runtimeId, String skillId, String language, String version) {
        RuntimeStatus status = new RuntimeStatus();
        status.setRuntimeId(runtimeId);
        status.setSkillId(skillId);
        status.setLanguage(language);
        status.setVersion(version);
        status.setState(RuntimeState.INITIALIZING);
        status.setStartedAt(new Date());
        return status;
    }
    
    public boolean isHealthy() {
        return state == RuntimeState.RUNNING;
    }
    
    public String getRuntimeId() { return runtimeId; }
    public void setRuntimeId(String runtimeId) { this.runtimeId = runtimeId; }
    
    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public RuntimeState getState() { return state; }
    public void setState(RuntimeState state) { this.state = state; }
    
    public Date getStartedAt() { return startedAt; }
    public void setStartedAt(Date startedAt) { this.startedAt = startedAt; }
    
    public Date getLastHeartbeat() { return lastHeartbeat; }
    public void setLastHeartbeat(Date lastHeartbeat) { this.lastHeartbeat = lastHeartbeat; }
    
    public long getMemoryUsed() { return memoryUsed; }
    public void setMemoryUsed(long memoryUsed) { this.memoryUsed = memoryUsed; }
    
    public long getMemoryLimit() { return memoryLimit; }
    public void setMemoryLimit(long memoryLimit) { this.memoryLimit = memoryLimit; }
    
    public double getCpuUsage() { return cpuUsage; }
    public void setCpuUsage(double cpuUsage) { this.cpuUsage = cpuUsage; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}

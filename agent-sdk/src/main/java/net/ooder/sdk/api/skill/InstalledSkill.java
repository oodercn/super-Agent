package net.ooder.sdk.api.skill;

import java.util.List;
import java.util.Map;

public class InstalledSkill {
    
    private String skillId;
    private String name;
    private String version;
    private String sceneId;
    private String installPath;
    private String status;
    private long installTime;
    private long lastInvokeTime;
    private int invokeCount;
    
    private String previousStatus;
    private long lastStateChangeTime;
    private int restartCount;
    private long totalRunTime;
    private long lastStartTime;
    private long lastStopTime;
    private String errorMessage;
    private Map<String, Object> runtimeState;
    private List<String> dependencies;
    private Map<String, String> config;
    private int priority;
    private boolean autoStart;
    private long healthCheckInterval;
    private long lastHealthCheckTime;
    
    public String getSkillId() {
        return skillId;
    }
    
    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getSceneId() {
        return sceneId;
    }
    
    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }
    
    public String getInstallPath() {
        return installPath;
    }
    
    public void setInstallPath(String installPath) {
        this.installPath = installPath;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public long getInstallTime() {
        return installTime;
    }
    
    public void setInstallTime(long installTime) {
        this.installTime = installTime;
    }
    
    public long getLastInvokeTime() {
        return lastInvokeTime;
    }
    
    public void setLastInvokeTime(long lastInvokeTime) {
        this.lastInvokeTime = lastInvokeTime;
    }
    
    public int getInvokeCount() {
        return invokeCount;
    }
    
    public void setInvokeCount(int invokeCount) {
        this.invokeCount = invokeCount;
    }
    
    public String getPreviousStatus() {
        return previousStatus;
    }
    
    public void setPreviousStatus(String previousStatus) {
        this.previousStatus = previousStatus;
    }
    
    public long getLastStateChangeTime() {
        return lastStateChangeTime;
    }
    
    public void setLastStateChangeTime(long lastStateChangeTime) {
        this.lastStateChangeTime = lastStateChangeTime;
    }
    
    public int getRestartCount() {
        return restartCount;
    }
    
    public void setRestartCount(int restartCount) {
        this.restartCount = restartCount;
    }
    
    public long getTotalRunTime() {
        return totalRunTime;
    }
    
    public void setTotalRunTime(long totalRunTime) {
        this.totalRunTime = totalRunTime;
    }
    
    public long getLastStartTime() {
        return lastStartTime;
    }
    
    public void setLastStartTime(long lastStartTime) {
        this.lastStartTime = lastStartTime;
    }
    
    public long getLastStopTime() {
        return lastStopTime;
    }
    
    public void setLastStopTime(long lastStopTime) {
        this.lastStopTime = lastStopTime;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public Map<String, Object> getRuntimeState() {
        return runtimeState;
    }
    
    public void setRuntimeState(Map<String, Object> runtimeState) {
        this.runtimeState = runtimeState;
    }
    
    public List<String> getDependencies() {
        return dependencies;
    }
    
    public void setDependencies(List<String> dependencies) {
        this.dependencies = dependencies;
    }
    
    public Map<String, String> getConfig() {
        return config;
    }
    
    public void setConfig(Map<String, String> config) {
        this.config = config;
    }
    
    public int getPriority() {
        return priority;
    }
    
    public void setPriority(int priority) {
        this.priority = priority;
    }
    
    public boolean isAutoStart() {
        return autoStart;
    }
    
    public void setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
    }
    
    public long getHealthCheckInterval() {
        return healthCheckInterval;
    }
    
    public void setHealthCheckInterval(long healthCheckInterval) {
        this.healthCheckInterval = healthCheckInterval;
    }
    
    public long getLastHealthCheckTime() {
        return lastHealthCheckTime;
    }
    
    public void setLastHealthCheckTime(long lastHealthCheckTime) {
        this.lastHealthCheckTime = lastHealthCheckTime;
    }
}

package net.ooder.mvp.skill.scene.capability.service;

import java.util.List;
import java.util.Map;

public interface InstallLogService {
    
    InstallLogEntry createLog(CreateLogRequest request);
    
    InstallLogEntry appendLog(String installId, String level, String action, String message);
    
    InstallLogEntry appendLog(String installId, String level, String action, String message, Map<String, Object> details);
    
    List<InstallLogEntry> getLogsByInstall(String installId);
    
    List<InstallLogEntry> getLogsByInstall(String installId, int limit);
    
    List<InstallLogEntry> getLogsByUser(String userId);
    
    List<InstallLogEntry> getLogsByCapability(String capabilityId);
    
    List<InstallLogEntry> searchLogs(LogSearchRequest request);
    
    InstallLogSummary getSummary(String installId);
    
    void clearLogs(String installId);
    
    void clearOldLogs(long beforeTime);
    
    public static class CreateLogRequest {
        private String installId;
        private String capabilityId;
        private String userId;
        private String sceneGroupId;
        
        public String getInstallId() { return installId; }
        public void setInstallId(String installId) { this.installId = installId; }
        public String getCapabilityId() { return capabilityId; }
        public void setCapabilityId(String capabilityId) { this.capabilityId = capabilityId; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getSceneGroupId() { return sceneGroupId; }
        public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
    }
    
    public static class LogSearchRequest {
        private String installId;
        private String capabilityId;
        private String userId;
        private String level;
        private String action;
        private Long startTime;
        private Long endTime;
        private int offset;
        private int limit = 100;
        
        public String getInstallId() { return installId; }
        public void setInstallId(String installId) { this.installId = installId; }
        public String getCapabilityId() { return capabilityId; }
        public void setCapabilityId(String capabilityId) { this.capabilityId = capabilityId; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getLevel() { return level; }
        public void setLevel(String level) { this.level = level; }
        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }
        public Long getStartTime() { return startTime; }
        public void setStartTime(Long startTime) { this.startTime = startTime; }
        public Long getEndTime() { return endTime; }
        public void setEndTime(Long endTime) { this.endTime = endTime; }
        public int getOffset() { return offset; }
        public void setOffset(int offset) { this.offset = offset; }
        public int getLimit() { return limit; }
        public void setLimit(int limit) { this.limit = limit; }
    }
    
    public static class InstallLogEntry {
        private String logId;
        private String installId;
        private String capabilityId;
        private String userId;
        private String sceneGroupId;
        private String level;
        private String action;
        private String message;
        private Map<String, Object> details;
        private long timestamp;
        
        public enum Level {
            DEBUG,
            INFO,
            WARN,
            ERROR
        }
        
        public String getLogId() { return logId; }
        public void setLogId(String logId) { this.logId = logId; }
        public String getInstallId() { return installId; }
        public void setInstallId(String installId) { this.installId = installId; }
        public String getCapabilityId() { return capabilityId; }
        public void setCapabilityId(String capabilityId) { this.capabilityId = capabilityId; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getSceneGroupId() { return sceneGroupId; }
        public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
        public String getLevel() { return level; }
        public void setLevel(String level) { this.level = level; }
        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public Map<String, Object> getDetails() { return details; }
        public void setDetails(Map<String, Object> details) { this.details = details; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }
    
    public static class InstallLogSummary {
        private String installId;
        private int totalLogs;
        private int errorCount;
        private int warnCount;
        private int infoCount;
        private int debugCount;
        private long startTime;
        private long endTime;
        private String status;
        private List<String> installedCapabilities;
        private List<String> failedCapabilities;
        
        public String getInstallId() { return installId; }
        public void setInstallId(String installId) { this.installId = installId; }
        public int getTotalLogs() { return totalLogs; }
        public void setTotalLogs(int totalLogs) { this.totalLogs = totalLogs; }
        public int getErrorCount() { return errorCount; }
        public void setErrorCount(int errorCount) { this.errorCount = errorCount; }
        public int getWarnCount() { return warnCount; }
        public void setWarnCount(int warnCount) { this.warnCount = warnCount; }
        public int getInfoCount() { return infoCount; }
        public void setInfoCount(int infoCount) { this.infoCount = infoCount; }
        public int getDebugCount() { return debugCount; }
        public void setDebugCount(int debugCount) { this.debugCount = debugCount; }
        public long getStartTime() { return startTime; }
        public void setStartTime(long startTime) { this.startTime = startTime; }
        public long getEndTime() { return endTime; }
        public void setEndTime(long endTime) { this.endTime = endTime; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public List<String> getInstalledCapabilities() { return installedCapabilities; }
        public void setInstalledCapabilities(List<String> installedCapabilities) { this.installedCapabilities = installedCapabilities; }
        public List<String> getFailedCapabilities() { return failedCapabilities; }
        public void setFailedCapabilities(List<String> failedCapabilities) { this.failedCapabilities = failedCapabilities; }
    }
}

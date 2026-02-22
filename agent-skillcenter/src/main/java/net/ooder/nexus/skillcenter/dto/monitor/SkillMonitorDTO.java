package net.ooder.nexus.skillcenter.dto.monitor;

import java.util.List;
import java.util.Map;

public class SkillMonitorDTO {
    private String skillId;
    private String instanceId;
    private String name;
    private String status;
    private long uptime;
    private long startTime;
    
    private ResourceMetrics resources;
    private ExecutionMetrics execution;
    private HealthStatus health;
    private List<LogEntry> recentLogs;
    private Map<String, Object> metadata;

    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    public String getInstanceId() { return instanceId; }
    public void setInstanceId(String instanceId) { this.instanceId = instanceId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public long getUptime() { return uptime; }
    public void setUptime(long uptime) { this.uptime = uptime; }
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    public ResourceMetrics getResources() { return resources; }
    public void setResources(ResourceMetrics resources) { this.resources = resources; }
    public ExecutionMetrics getExecution() { return execution; }
    public void setExecution(ExecutionMetrics execution) { this.execution = execution; }
    public HealthStatus getHealth() { return health; }
    public void setHealth(HealthStatus health) { this.health = health; }
    public List<LogEntry> getRecentLogs() { return recentLogs; }
    public void setRecentLogs(List<LogEntry> recentLogs) { this.recentLogs = recentLogs; }
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    public static class ResourceMetrics {
        private double cpuUsage;
        private long memoryUsed;
        private long memoryLimit;
        private double memoryUsagePercent;
        private long networkIn;
        private long networkOut;
        private long diskRead;
        private long diskWrite;
        private int fileDescriptors;

        public double getCpuUsage() { return cpuUsage; }
        public void setCpuUsage(double cpuUsage) { this.cpuUsage = cpuUsage; }
        public long getMemoryUsed() { return memoryUsed; }
        public void setMemoryUsed(long memoryUsed) { this.memoryUsed = memoryUsed; }
        public long getMemoryLimit() { return memoryLimit; }
        public void setMemoryLimit(long memoryLimit) { this.memoryLimit = memoryLimit; }
        public double getMemoryUsagePercent() { return memoryUsagePercent; }
        public void setMemoryUsagePercent(double memoryUsagePercent) { this.memoryUsagePercent = memoryUsagePercent; }
        public long getNetworkIn() { return networkIn; }
        public void setNetworkIn(long networkIn) { this.networkIn = networkIn; }
        public long getNetworkOut() { return networkOut; }
        public void setNetworkOut(long networkOut) { this.networkOut = networkOut; }
        public long getDiskRead() { return diskRead; }
        public void setDiskRead(long diskRead) { this.diskRead = diskRead; }
        public long getDiskWrite() { return diskWrite; }
        public void setDiskWrite(long diskWrite) { this.diskWrite = diskWrite; }
        public int getFileDescriptors() { return fileDescriptors; }
        public void setFileDescriptors(int fileDescriptors) { this.fileDescriptors = fileDescriptors; }
    }

    public static class ExecutionMetrics {
        private long totalExecutions;
        private long successCount;
        private long failureCount;
        private double successRate;
        private double avgExecutionTime;
        private long maxExecutionTime;
        private long minExecutionTime;
        private int currentConcurrency;
        private int queueSize;
        private double throughput;

        public long getTotalExecutions() { return totalExecutions; }
        public void setTotalExecutions(long totalExecutions) { this.totalExecutions = totalExecutions; }
        public long getSuccessCount() { return successCount; }
        public void setSuccessCount(long successCount) { this.successCount = successCount; }
        public long getFailureCount() { return failureCount; }
        public void setFailureCount(long failureCount) { this.failureCount = failureCount; }
        public double getSuccessRate() { return successRate; }
        public void setSuccessRate(double successRate) { this.successRate = successRate; }
        public double getAvgExecutionTime() { return avgExecutionTime; }
        public void setAvgExecutionTime(double avgExecutionTime) { this.avgExecutionTime = avgExecutionTime; }
        public long getMaxExecutionTime() { return maxExecutionTime; }
        public void setMaxExecutionTime(long maxExecutionTime) { this.maxExecutionTime = maxExecutionTime; }
        public long getMinExecutionTime() { return minExecutionTime; }
        public void setMinExecutionTime(long minExecutionTime) { this.minExecutionTime = minExecutionTime; }
        public int getCurrentConcurrency() { return currentConcurrency; }
        public void setCurrentConcurrency(int currentConcurrency) { this.currentConcurrency = currentConcurrency; }
        public int getQueueSize() { return queueSize; }
        public void setQueueSize(int queueSize) { this.queueSize = queueSize; }
        public double getThroughput() { return throughput; }
        public void setThroughput(double throughput) { this.throughput = throughput; }
    }

    public static class HealthStatus {
        private String status;
        private String message;
        private long lastCheckTime;
        private int consecutiveFailures;
        private long lastHealthyTime;

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public long getLastCheckTime() { return lastCheckTime; }
        public void setLastCheckTime(long lastCheckTime) { this.lastCheckTime = lastCheckTime; }
        public int getConsecutiveFailures() { return consecutiveFailures; }
        public void setConsecutiveFailures(int consecutiveFailures) { this.consecutiveFailures = consecutiveFailures; }
        public long getLastHealthyTime() { return lastHealthyTime; }
        public void setLastHealthyTime(long lastHealthyTime) { this.lastHealthyTime = lastHealthyTime; }
    }

    public static class LogEntry {
        private long timestamp;
        private String level;
        private String message;
        private String source;
        private Map<String, Object> context;

        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
        public String getLevel() { return level; }
        public void setLevel(String level) { this.level = level; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getSource() { return source; }
        public void setSource(String source) { this.source = source; }
        public Map<String, Object> getContext() { return context; }
        public void setContext(Map<String, Object> context) { this.context = context; }
    }
}

package net.ooder.skillcenter.monitoring;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Capability Monitoring Service Interface
 *
 * @author ooder Team
 * @since 2.0
 */
public interface CapabilityMonitoringService {

    CompletableFuture<RuntimeStatus> getRuntimeStatus(String skillId);

    CompletableFuture<List<RuntimeStatus>> getAllRuntimeStatus();

    CompletableFuture<PerformanceStats> getPerformanceStats(String skillId);

    CompletableFuture<PerformanceStats> getOverallPerformanceStats();

    CompletableFuture<List<ExecutionRecord>> getExecutionRecords(String skillId, int limit);

    CompletableFuture<HealthReport> getHealthReport();

    CompletableFuture<Void> startMonitoring(String skillId);

    CompletableFuture<Void> stopMonitoring(String skillId);

    CompletableFuture<List<AlertInfo>> getActiveAlerts();

    CompletableFuture<Void> acknowledgeAlert(String alertId);
}

class RuntimeStatus {
    private String skillId;
    private String skillName;
    private String status;
    private long uptime;
    private int activeExecutions;
    private double cpuUsage;
    private long memoryUsage;
    private long lastExecutionTime;

    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public long getUptime() { return uptime; }
    public void setUptime(long uptime) { this.uptime = uptime; }
    public int getActiveExecutions() { return activeExecutions; }
    public void setActiveExecutions(int activeExecutions) { this.activeExecutions = activeExecutions; }
    public double getCpuUsage() { return cpuUsage; }
    public void setCpuUsage(double cpuUsage) { this.cpuUsage = cpuUsage; }
    public long getMemoryUsage() { return memoryUsage; }
    public void setMemoryUsage(long memoryUsage) { this.memoryUsage = memoryUsage; }
    public long getLastExecutionTime() { return lastExecutionTime; }
    public void setLastExecutionTime(long lastExecutionTime) { this.lastExecutionTime = lastExecutionTime; }
}

class PerformanceStats {
    private String skillId;
    private long reportTime;
    private long totalExecutions;
    private long successfulExecutions;
    private long failedExecutions;
    private double averageExecutionTime;
    private double minExecutionTime;
    private double maxExecutionTime;
    private double throughput;
    private double errorRate;

    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    public long getReportTime() { return reportTime; }
    public void setReportTime(long reportTime) { this.reportTime = reportTime; }
    public long getTotalExecutions() { return totalExecutions; }
    public void setTotalExecutions(long totalExecutions) { this.totalExecutions = totalExecutions; }
    public long getSuccessfulExecutions() { return successfulExecutions; }
    public void setSuccessfulExecutions(long successfulExecutions) { this.successfulExecutions = successfulExecutions; }
    public long getFailedExecutions() { return failedExecutions; }
    public void setFailedExecutions(long failedExecutions) { this.failedExecutions = failedExecutions; }
    public double getAverageExecutionTime() { return averageExecutionTime; }
    public void setAverageExecutionTime(double averageExecutionTime) { this.averageExecutionTime = averageExecutionTime; }
    public double getMinExecutionTime() { return minExecutionTime; }
    public void setMinExecutionTime(double minExecutionTime) { this.minExecutionTime = minExecutionTime; }
    public double getMaxExecutionTime() { return maxExecutionTime; }
    public void setMaxExecutionTime(double maxExecutionTime) { this.maxExecutionTime = maxExecutionTime; }
    public double getThroughput() { return throughput; }
    public void setThroughput(double throughput) { this.throughput = throughput; }
    public double getErrorRate() { return errorRate; }
    public void setErrorRate(double errorRate) { this.errorRate = errorRate; }
}

class ExecutionRecord {
    private String recordId;
    private String skillId;
    private String executionId;
    private String status;
    private long startTime;
    private long endTime;
    private long executionTime;
    private Map<String, Object> metadata;

    public String getRecordId() { return recordId; }
    public void setRecordId(String recordId) { this.recordId = recordId; }
    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    public String getExecutionId() { return executionId; }
    public void setExecutionId(String executionId) { this.executionId = executionId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }
    public long getExecutionTime() { return executionTime; }
    public void setExecutionTime(long executionTime) { this.executionTime = executionTime; }
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
}

class HealthReport {
    private long reportTime;
    private int totalSkills;
    private int healthySkills;
    private int unhealthySkills;
    private int warningSkills;
    private List<String> unhealthyList;
    private List<String> warningList;

    public long getReportTime() { return reportTime; }
    public void setReportTime(long reportTime) { this.reportTime = reportTime; }
    public int getTotalSkills() { return totalSkills; }
    public void setTotalSkills(int totalSkills) { this.totalSkills = totalSkills; }
    public int getHealthySkills() { return healthySkills; }
    public void setHealthySkills(int healthySkills) { this.healthySkills = healthySkills; }
    public int getUnhealthySkills() { return unhealthySkills; }
    public void setUnhealthySkills(int unhealthySkills) { this.unhealthySkills = unhealthySkills; }
    public int getWarningSkills() { return warningSkills; }
    public void setWarningSkills(int warningSkills) { this.warningSkills = warningSkills; }
    public List<String> getUnhealthyList() { return unhealthyList; }
    public void setUnhealthyList(List<String> unhealthyList) { this.unhealthyList = unhealthyList; }
    public List<String> getWarningList() { return warningList; }
    public void setWarningList(List<String> warningList) { this.warningList = warningList; }
}

class AlertInfo {
    private String alertId;
    private String skillId;
    private String type;
    private String severity;
    private String message;
    private long triggeredTime;
    private boolean acknowledged;

    public String getAlertId() { return alertId; }
    public void setAlertId(String alertId) { this.alertId = alertId; }
    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public long getTriggeredTime() { return triggeredTime; }
    public void setTriggeredTime(long triggeredTime) { this.triggeredTime = triggeredTime; }
    public boolean isAcknowledged() { return acknowledged; }
    public void setAcknowledged(boolean acknowledged) { this.acknowledged = acknowledged; }
}

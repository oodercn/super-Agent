package net.ooder.skillcenter.distribution;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Capability Distribution Service Interface
 *
 * @author ooder Team
 * @since 2.0
 */
public interface CapabilityDistributionService {

    CompletableFuture<DistributionResult> distributeSkill(String skillId, List<String> targetNodes);

    CompletableFuture<DistributionResult> distributeSkill(String skillId, String targetNode);

    CompletableFuture<List<DistributionStatus>> getDistributionStatus(String skillId);

    CompletableFuture<List<DistributionStatus>> getAllDistributionStatus();

    CompletableFuture<Void> cancelDistribution(String distributionId);

    CompletableFuture<Void> retryDistribution(String distributionId);

    CompletableFuture<DistributionReport> getDistributionReport();

    CompletableFuture<List<DistributionHistory>> getDistributionHistory(String skillId, int limit);
}

class DistributionResult {
    private String distributionId;
    private String skillId;
    private boolean success;
    private int totalTargets;
    private int successCount;
    private int failedCount;
    private long distributionTime;

    public String getDistributionId() { return distributionId; }
    public void setDistributionId(String distributionId) { this.distributionId = distributionId; }
    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public int getTotalTargets() { return totalTargets; }
    public void setTotalTargets(int totalTargets) { this.totalTargets = totalTargets; }
    public int getSuccessCount() { return successCount; }
    public void setSuccessCount(int successCount) { this.successCount = successCount; }
    public int getFailedCount() { return failedCount; }
    public void setFailedCount(int failedCount) { this.failedCount = failedCount; }
    public long getDistributionTime() { return distributionTime; }
    public void setDistributionTime(long distributionTime) { this.distributionTime = distributionTime; }
}

class DistributionStatus {
    private String distributionId;
    private String skillId;
    private String targetNode;
    private String status;
    private String message;
    private long startTime;
    private long endTime;
    private int progress;

    public String getDistributionId() { return distributionId; }
    public void setDistributionId(String distributionId) { this.distributionId = distributionId; }
    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    public String getTargetNode() { return targetNode; }
    public void setTargetNode(String targetNode) { this.targetNode = targetNode; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }
    public int getProgress() { return progress; }
    public void setProgress(int progress) { this.progress = progress; }
}

class DistributionReport {
    private long reportTime;
    private int totalDistributions;
    private int activeDistributions;
    private int completedDistributions;
    private int failedDistributions;
    private Map<String, Integer> bySkillType;

    public long getReportTime() { return reportTime; }
    public void setReportTime(long reportTime) { this.reportTime = reportTime; }
    public int getTotalDistributions() { return totalDistributions; }
    public void setTotalDistributions(int totalDistributions) { this.totalDistributions = totalDistributions; }
    public int getActiveDistributions() { return activeDistributions; }
    public void setActiveDistributions(int activeDistributions) { this.activeDistributions = activeDistributions; }
    public int getCompletedDistributions() { return completedDistributions; }
    public void setCompletedDistributions(int completedDistributions) { this.completedDistributions = completedDistributions; }
    public int getFailedDistributions() { return failedDistributions; }
    public void setFailedDistributions(int failedDistributions) { this.failedDistributions = failedDistributions; }
    public Map<String, Integer> getBySkillType() { return bySkillType; }
    public void setBySkillType(Map<String, Integer> bySkillType) { this.bySkillType = bySkillType; }
}

class DistributionHistory {
    private String historyId;
    private String skillId;
    private String targetNode;
    private String status;
    private long distributionTime;
    private long completionTime;

    public String getHistoryId() { return historyId; }
    public void setHistoryId(String historyId) { this.historyId = historyId; }
    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    public String getTargetNode() { return targetNode; }
    public void setTargetNode(String targetNode) { this.targetNode = targetNode; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public long getDistributionTime() { return distributionTime; }
    public void setDistributionTime(long distributionTime) { this.distributionTime = distributionTime; }
    public long getCompletionTime() { return completionTime; }
    public void setCompletionTime(long completionTime) { this.completionTime = completionTime; }
}

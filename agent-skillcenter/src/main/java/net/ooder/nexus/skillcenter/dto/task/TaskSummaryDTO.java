package net.ooder.nexus.skillcenter.dto.task;

import java.util.List;

public class TaskSummaryDTO {
    
    private String groupId;
    private int totalTasks;
    private int pendingTasks;
    private int runningTasks;
    private int completedTasks;
    private int failedTasks;
    private int cancelledTasks;
    private double completionRate;
    private double successRate;
    private long totalDuration;
    private double averageDuration;
    private List<TaskDTO> recentTasks;
    private List<TaskResultDTO> recentResults;

    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }

    public int getTotalTasks() { return totalTasks; }
    public void setTotalTasks(int totalTasks) { this.totalTasks = totalTasks; }

    public int getPendingTasks() { return pendingTasks; }
    public void setPendingTasks(int pendingTasks) { this.pendingTasks = pendingTasks; }

    public int getRunningTasks() { return runningTasks; }
    public void setRunningTasks(int runningTasks) { this.runningTasks = runningTasks; }

    public int getCompletedTasks() { return completedTasks; }
    public void setCompletedTasks(int completedTasks) { this.completedTasks = completedTasks; }

    public int getFailedTasks() { return failedTasks; }
    public void setFailedTasks(int failedTasks) { this.failedTasks = failedTasks; }

    public int getCancelledTasks() { return cancelledTasks; }
    public void setCancelledTasks(int cancelledTasks) { this.cancelledTasks = cancelledTasks; }

    public double getCompletionRate() { return completionRate; }
    public void setCompletionRate(double completionRate) { this.completionRate = completionRate; }

    public double getSuccessRate() { return successRate; }
    public void setSuccessRate(double successRate) { this.successRate = successRate; }

    public long getTotalDuration() { return totalDuration; }
    public void setTotalDuration(long totalDuration) { this.totalDuration = totalDuration; }

    public double getAverageDuration() { return averageDuration; }
    public void setAverageDuration(double averageDuration) { this.averageDuration = averageDuration; }

    public List<TaskDTO> getRecentTasks() { return recentTasks; }
    public void setRecentTasks(List<TaskDTO> recentTasks) { this.recentTasks = recentTasks; }

    public List<TaskResultDTO> getRecentResults() { return recentResults; }
    public void setRecentResults(List<TaskResultDTO> recentResults) { this.recentResults = recentResults; }
}

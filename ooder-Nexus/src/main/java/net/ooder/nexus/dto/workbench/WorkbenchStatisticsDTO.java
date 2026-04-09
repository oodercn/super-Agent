package net.ooder.nexus.dto.workbench;

public class WorkbenchStatisticsDTO {
    private int activeSceneCount;
    private int pendingTodoCount;
    private int highPriorityTodoCount;
    private int dueTodayCount;
    private int completedTodoCount;
    private int pendingApprovalCount;

    public int getActiveSceneCount() { return activeSceneCount; }
    public void setActiveSceneCount(int activeSceneCount) { this.activeSceneCount = activeSceneCount; }
    public int getPendingTodoCount() { return pendingTodoCount; }
    public void setPendingTodoCount(int pendingTodoCount) { this.pendingTodoCount = pendingTodoCount; }
    public int getHighPriorityTodoCount() { return highPriorityTodoCount; }
    public void setHighPriorityTodoCount(int highPriorityTodoCount) { this.highPriorityTodoCount = highPriorityTodoCount; }
    public int getDueTodayCount() { return dueTodayCount; }
    public void setDueTodayCount(int dueTodayCount) { this.dueTodayCount = dueTodayCount; }
    public int getCompletedTodoCount() { return completedTodoCount; }
    public void setCompletedTodoCount(int completedTodoCount) { this.completedTodoCount = completedTodoCount; }
    public int getPendingApprovalCount() { return pendingApprovalCount; }
    public void setPendingApprovalCount(int pendingApprovalCount) { this.pendingApprovalCount = pendingApprovalCount; }
}

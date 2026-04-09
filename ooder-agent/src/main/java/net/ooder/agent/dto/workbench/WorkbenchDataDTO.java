package net.ooder.agent.dto.workbench;

import java.util.List;

public class WorkbenchDataDTO {
    private WorkbenchStatisticsDTO statistics;
    private List<SceneTodoGroupDTO> sceneTodoGroups;
    private List<QuickActionDTO> quickActions;
    private List<GlobalTodoDTO> globalTodos;
    private List<RecentActivityDTO> recentActivity;
    private Long timestamp;

    public WorkbenchStatisticsDTO getStatistics() { return statistics; }
    public void setStatistics(WorkbenchStatisticsDTO statistics) { this.statistics = statistics; }
    public List<SceneTodoGroupDTO> getSceneTodoGroups() { return sceneTodoGroups; }
    public void setSceneTodoGroups(List<SceneTodoGroupDTO> sceneTodoGroups) { this.sceneTodoGroups = sceneTodoGroups; }
    public List<QuickActionDTO> getQuickActions() { return quickActions; }
    public void setQuickActions(List<QuickActionDTO> quickActions) { this.quickActions = quickActions; }
    public List<GlobalTodoDTO> getGlobalTodos() { return globalTodos; }
    public void setGlobalTodos(List<GlobalTodoDTO> globalTodos) { this.globalTodos = globalTodos; }
    public List<RecentActivityDTO> getRecentActivity() { return recentActivity; }
    public void setRecentActivity(List<RecentActivityDTO> recentActivity) { this.recentActivity = recentActivity; }
    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
}

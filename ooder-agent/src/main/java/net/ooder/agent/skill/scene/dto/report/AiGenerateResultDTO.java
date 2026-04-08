package net.ooder.mvp.skill.scene.dto.report;

import java.util.List;

public class AiGenerateResultDTO {
    private String userId;
    private List<String> workItems;
    private List<String> planItems;
    private String issuesSuggestion;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public List<String> getWorkItems() { return workItems; }
    public void setWorkItems(List<String> workItems) { this.workItems = workItems; }
    public List<String> getPlanItems() { return planItems; }
    public void setPlanItems(List<String> planItems) { this.planItems = planItems; }
    public String getIssuesSuggestion() { return issuesSuggestion; }
    public void setIssuesSuggestion(String issuesSuggestion) { this.issuesSuggestion = issuesSuggestion; }
}

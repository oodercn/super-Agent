package net.ooder.mvp.skill.scene.dto.report;

import java.util.List;

public class ReportAggregateResultDTO {
    private String sceneGroupId;
    private Integer totalCount;
    private Integer submittedCount;
    private List<String> allWorkItems;
    private String summary;

    public String getSceneGroupId() { return sceneGroupId; }
    public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
    public Integer getTotalCount() { return totalCount; }
    public void setTotalCount(Integer totalCount) { this.totalCount = totalCount; }
    public Integer getSubmittedCount() { return submittedCount; }
    public void setSubmittedCount(Integer submittedCount) { this.submittedCount = submittedCount; }
    public List<String> getAllWorkItems() { return allWorkItems; }
    public void setAllWorkItems(List<String> allWorkItems) { this.allWorkItems = allWorkItems; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
}

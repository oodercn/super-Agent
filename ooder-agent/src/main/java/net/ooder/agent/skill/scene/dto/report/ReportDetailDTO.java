package net.ooder.mvp.skill.scene.dto.report;

import java.util.List;

public class ReportDetailDTO {
    private String reportId;
    private String sceneGroupId;
    private String userId;
    private String userName;
    private String date;
    private List<String> workItems;
    private List<String> planItems;
    private String issues;
    private List<ReportSubmitRequestDTO.AttachmentDTO> attachments;
    private Long submitTime;
    private String status;

    public String getReportId() { return reportId; }
    public void setReportId(String reportId) { this.reportId = reportId; }
    public String getSceneGroupId() { return sceneGroupId; }
    public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public List<String> getWorkItems() { return workItems; }
    public void setWorkItems(List<String> workItems) { this.workItems = workItems; }
    public List<String> getPlanItems() { return planItems; }
    public void setPlanItems(List<String> planItems) { this.planItems = planItems; }
    public String getIssues() { return issues; }
    public void setIssues(String issues) { this.issues = issues; }
    public List<ReportSubmitRequestDTO.AttachmentDTO> getAttachments() { return attachments; }
    public void setAttachments(List<ReportSubmitRequestDTO.AttachmentDTO> attachments) { this.attachments = attachments; }
    public Long getSubmitTime() { return submitTime; }
    public void setSubmitTime(Long submitTime) { this.submitTime = submitTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

package net.ooder.mvp.skill.scene.dto.report;

import java.util.List;

public class ReportSubmitRequestDTO {
    private String sceneGroupId;
    private String userId;
    private String userName;
    private List<String> workItems;
    private List<String> planItems;
    private String issues;
    private List<AttachmentDTO> attachments;

    public String getSceneGroupId() { return sceneGroupId; }
    public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public List<String> getWorkItems() { return workItems; }
    public void setWorkItems(List<String> workItems) { this.workItems = workItems; }
    public List<String> getPlanItems() { return planItems; }
    public void setPlanItems(List<String> planItems) { this.planItems = planItems; }
    public String getIssues() { return issues; }
    public void setIssues(String issues) { this.issues = issues; }
    public List<AttachmentDTO> getAttachments() { return attachments; }
    public void setAttachments(List<AttachmentDTO> attachments) { this.attachments = attachments; }

    public static class AttachmentDTO {
        private String name;
        private Long size;
        private String type;
        private String url;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Long getSize() { return size; }
        public void setSize(Long size) { this.size = size; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
    }
}

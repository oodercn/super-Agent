package net.ooder.nexus.dto.workbench;

public class TodoItemDTO {
    private String id;
    private String title;
    private String description;
    private String priority;
    private String status;
    private String dueTime;
    private String sceneGroupId;
    private String sceneGroupName;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDueTime() { return dueTime; }
    public void setDueTime(String dueTime) { this.dueTime = dueTime; }
    public String getSceneGroupId() { return sceneGroupId; }
    public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
    public String getSceneGroupName() { return sceneGroupName; }
    public void setSceneGroupName(String sceneGroupName) { this.sceneGroupName = sceneGroupName; }
}

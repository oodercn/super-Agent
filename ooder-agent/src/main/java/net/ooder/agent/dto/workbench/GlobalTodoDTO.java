package net.ooder.agent.dto.workbench;

public class GlobalTodoDTO {
    private String id;
    private String title;
    private String sceneName;
    private String sceneGroupName;
    private String priority;
    private String status;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSceneName() { return sceneName; }
    public void setSceneName(String sceneName) { this.sceneName = sceneName; }
    public String getSceneGroupName() { return sceneGroupName; }
    public void setSceneGroupName(String sceneGroupName) { this.sceneGroupName = sceneGroupName; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

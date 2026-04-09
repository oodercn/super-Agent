package net.ooder.agent.dto.workbench;

public class QuickActionDTO {
    private String id;
    private String title;
    private String name;
    private String url;
    private String icon;
    private String sceneGroupId;
    private int priority;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public String getSceneGroupId() { return sceneGroupId; }
    public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
}

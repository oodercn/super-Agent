package net.ooder.mvp.skill.scene.dto.scene;

import java.util.List;
import java.util.Map;

public class UserSceneGroupDTO {
    
    private String sceneGroupId;
    private String userId;
    private String role;
    private long joinTime;
    private long lastActiveTime;
    private String status;
    private List<String> notifications;
    private Map<String, Object> personalContext;
    private SceneGroupDTO sceneGroup;
    
    public String getSceneGroupId() { return sceneGroupId; }
    public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public long getJoinTime() { return joinTime; }
    public void setJoinTime(long joinTime) { this.joinTime = joinTime; }
    public long getLastActiveTime() { return lastActiveTime; }
    public void setLastActiveTime(long lastActiveTime) { this.lastActiveTime = lastActiveTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<String> getNotifications() { return notifications; }
    public void setNotifications(List<String> notifications) { this.notifications = notifications; }
    public Map<String, Object> getPersonalContext() { return personalContext; }
    public void setPersonalContext(Map<String, Object> personalContext) { this.personalContext = personalContext; }
    public SceneGroupDTO getSceneGroup() { return sceneGroup; }
    public void setSceneGroup(SceneGroupDTO sceneGroup) { this.sceneGroup = sceneGroup; }
}

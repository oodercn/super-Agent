package net.ooder.sdk.capability.model;

import java.util.List;
import java.util.Map;

public class SceneGroupDef {
    
    private String sceneGroupId;
    private String name;
    private String sceneId;
    private String primaryId;
    private List<String> memberIds;
    private int memberCount;
    private SceneGroupStatus status;
    private long createdTime;
    private Map<String, Object> properties;
    
    public String getSceneGroupId() { return sceneGroupId; }
    public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getSceneId() { return sceneId; }
    public void setSceneId(String sceneId) { this.sceneId = sceneId; }
    
    public String getPrimaryId() { return primaryId; }
    public void setPrimaryId(String primaryId) { this.primaryId = primaryId; }
    
    public List<String> getMemberIds() { return memberIds; }
    public void setMemberIds(List<String> memberIds) { this.memberIds = memberIds; }
    
    public int getMemberCount() { return memberCount; }
    public void setMemberCount(int memberCount) { this.memberCount = memberCount; }
    
    public SceneGroupStatus getStatus() { return status; }
    public void setStatus(SceneGroupStatus status) { this.status = status; }
    
    public long getCreatedTime() { return createdTime; }
    public void setCreatedTime(long createdTime) { this.createdTime = createdTime; }
    
    public Map<String, Object> getProperties() { return properties; }
    public void setProperties(Map<String, Object> properties) { this.properties = properties; }
}

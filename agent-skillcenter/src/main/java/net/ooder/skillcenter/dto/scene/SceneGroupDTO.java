package net.ooder.skillcenter.dto.scene;

public class SceneGroupDTO {
    private String sceneGroupId;
    private String sceneId;
    private String name;
    private String status;
    private int memberCount;
    private String primaryAgentId;
    private long createTime;
    private long lastUpdateTime;

    public String getSceneGroupId() { return sceneGroupId; }
    public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
    public String getSceneId() { return sceneId; }
    public void setSceneId(String sceneId) { this.sceneId = sceneId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getMemberCount() { return memberCount; }
    public void setMemberCount(int memberCount) { this.memberCount = memberCount; }
    public String getPrimaryAgentId() { return primaryAgentId; }
    public void setPrimaryAgentId(String primaryAgentId) { this.primaryAgentId = primaryAgentId; }
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
    public long getLastUpdateTime() { return lastUpdateTime; }
    public void setLastUpdateTime(long lastUpdateTime) { this.lastUpdateTime = lastUpdateTime; }
}

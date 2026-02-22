package net.ooder.skillcenter.southbound;

import java.util.List;

public class SceneGroup {
    private String groupId;
    private String groupName;
    private String sceneId;
    private String primaryId;
    private List<String> memberIds;
    private String status;
    private long createdAt;

    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public String getSceneId() { return sceneId; }
    public void setSceneId(String sceneId) { this.sceneId = sceneId; }

    public String getPrimaryId() { return primaryId; }
    public void setPrimaryId(String primaryId) { this.primaryId = primaryId; }

    public List<String> getMemberIds() { return memberIds; }
    public void setMemberIds(List<String> memberIds) { this.memberIds = memberIds; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}

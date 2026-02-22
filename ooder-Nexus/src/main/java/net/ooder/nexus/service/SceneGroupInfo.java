package net.ooder.nexus.service;

import java.util.List;

/**
 * 场景组信息
 */
public class SceneGroupInfo {
    
    private String groupId;
    private String groupName;
    private String description;
    private String coordinatorId;
    private int memberCount;
    private List<String> members;
    private String status;
    private long createdTime;

    public SceneGroupInfo() {}

    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }
    
    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getCoordinatorId() { return coordinatorId; }
    public void setCoordinatorId(String coordinatorId) { this.coordinatorId = coordinatorId; }
    
    public int getMemberCount() { return memberCount; }
    public void setMemberCount(int memberCount) { this.memberCount = memberCount; }
    
    public List<String> getMembers() { return members; }
    public void setMembers(List<String> members) { this.members = members; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public long getCreatedTime() { return createdTime; }
    public void setCreatedTime(long createdTime) { this.createdTime = createdTime; }
}

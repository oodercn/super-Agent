package net.ooder.nexus.skillcenter.model;

/**
 * 群组成员模型类
 */
public class GroupMember {
    private String id;
    private String groupId;
    private String userId;
    private String username;
    private String role; // admin, member
    private String joinedAt;
    private String status; // active, inactive
    
    public GroupMember() {
    }
    
    public GroupMember(String id, String groupId, String userId, String username, String role, String joinedAt) {
        this.id = id;
        this.groupId = groupId;
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.joinedAt = joinedAt;
        this.status = "active";
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getGroupId() {
        return groupId;
    }
    
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getJoinedAt() {
        return joinedAt;
    }
    
    public void setJoinedAt(String joinedAt) {
        this.joinedAt = joinedAt;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "GroupMember{" +
                "id='" + id + '\'' +
                ", groupId='" + groupId + '\'' +
                ", userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

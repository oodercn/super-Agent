package net.ooder.skillcenter.dto;

import java.util.Date;

/**
 * 群组成员数据传输对象
 */
public class GroupMemberDTO {

    private String id;
    private String groupId;
    private String userId;
    private String username;
    private String role;
    private Date joinedAt;
    private String status;

    public GroupMemberDTO() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Date getJoinedAt() { return joinedAt; }
    public void setJoinedAt(Date joinedAt) { this.joinedAt = joinedAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

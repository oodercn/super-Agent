package net.ooder.nexus.skillcenter.model;

import java.time.LocalDateTime;

/**
 * 群组模型类 - 面向个人的群组共享功能
 */
public class Group {
    private String id;
    private String name;
    private String description;
    private int memberCount;
    private String createdAt;
    private String role;
    private String ownerId;
    private String status;
    
    public Group() {
    }
    
    public Group(String id, String name, String description, int memberCount, String createdAt, String role) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.memberCount = memberCount;
        this.createdAt = createdAt;
        this.role = role;
        this.status = "active";
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getMemberCount() {
        return memberCount;
    }
    
    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getOwnerId() {
        return ownerId;
    }
    
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "Group{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", memberCount=" + memberCount +
                ", createdAt='" + createdAt + '\'' +
                ", role='" + role + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

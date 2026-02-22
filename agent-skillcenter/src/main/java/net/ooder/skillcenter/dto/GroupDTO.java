package net.ooder.skillcenter.dto;

import java.util.Date;
import java.util.List;

/**
 * 群组数据传输对象
 */
public class GroupDTO {
    
    private String id;
    private String name;
    private String description;
    private int memberCount;
    private List<String> members;
    private List<String> skills;
    private Date createdAt;
    private Date updatedAt;
    private String owner;
    private String status;
    
    public GroupDTO() {}
    
    public GroupDTO(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public int getMemberCount() { return memberCount; }
    public void setMemberCount(int memberCount) { this.memberCount = memberCount; }
    
    public List<String> getMembers() { return members; }
    public void setMembers(List<String> members) { this.members = members; }
    
    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

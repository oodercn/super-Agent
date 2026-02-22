package net.ooder.skillcenter.model;

/**
 * 群组技能模型类
 */
public class GroupSkill {
    private String id;
    private String groupId;
    private String groupName;
    private String skillId;
    private String skillName;
    private String sharedBy;
    private String sharedAt;
    private String description;
    private String status;
    
    public GroupSkill() {
    }
    
    public GroupSkill(String id, String groupId, String groupName, String skillId, 
                      String skillName, String sharedBy, String sharedAt, String description) {
        this.id = id;
        this.groupId = groupId;
        this.groupName = groupName;
        this.skillId = skillId;
        this.skillName = skillName;
        this.sharedBy = sharedBy;
        this.sharedAt = sharedAt;
        this.description = description;
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
    
    public String getGroupName() {
        return groupName;
    }
    
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
    public String getSkillId() {
        return skillId;
    }
    
    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }
    
    public String getSkillName() {
        return skillName;
    }
    
    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }
    
    public String getSharedBy() {
        return sharedBy;
    }
    
    public void setSharedBy(String sharedBy) {
        this.sharedBy = sharedBy;
    }
    
    public String getSharedAt() {
        return sharedAt;
    }
    
    public void setSharedAt(String sharedAt) {
        this.sharedAt = sharedAt;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "GroupSkill{" +
                "id='" + id + '\'' +
                ", groupId='" + groupId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", skillId='" + skillId + '\'' +
                ", skillName='" + skillName + '\'' +
                ", sharedBy='" + sharedBy + '\'' +
                ", sharedAt='" + sharedAt + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

package net.ooder.skillcenter.dto;

import java.util.Date;

/**
 * 群组技能数据传输对象
 */
public class GroupSkillDTO {

    private String id;
    private String groupId;
    private String skillId;
    private String skillName;
    private String addedBy;
    private Date addedAt;
    private String status;

    public GroupSkillDTO() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }

    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public String getAddedBy() { return addedBy; }
    public void setAddedBy(String addedBy) { this.addedBy = addedBy; }

    public Date getAddedAt() { return addedAt; }
    public void setAddedAt(Date addedAt) { this.addedAt = addedAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

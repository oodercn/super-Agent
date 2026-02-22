package net.ooder.nexus.skillcenter.dto;

import java.util.Date;

/**
 * 技能分享DTO
 */
public class SkillShareDTO {
    private String id;
    private String skillId;
    private String skillName;
    private String groupId;
    private String groupName;
    private String message;
    private Date sharedAt;
    private String status;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Date getSharedAt() { return sharedAt; }
    public void setSharedAt(Date sharedAt) { this.sharedAt = sharedAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

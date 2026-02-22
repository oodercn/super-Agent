package net.ooder.nexus.skillcenter.dto.share;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class SkillShareDTO extends BaseDTO {

    private String id;
    private String skillId;
    private String groupId;
    private String message;
    private String sharedAt;
    private String status;

    public SkillShareDTO() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSharedAt() {
        return sharedAt;
    }

    public void setSharedAt(String sharedAt) {
        this.sharedAt = sharedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

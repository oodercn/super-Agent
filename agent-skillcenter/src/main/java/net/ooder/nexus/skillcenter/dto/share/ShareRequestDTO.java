package net.ooder.nexus.skillcenter.dto.share;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class ShareRequestDTO extends BaseDTO {

    private String skillId;
    private String groupId;
    private String message;

    public ShareRequestDTO() {}

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
}

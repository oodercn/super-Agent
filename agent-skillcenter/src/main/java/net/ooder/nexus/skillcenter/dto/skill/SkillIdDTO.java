package net.ooder.nexus.skillcenter.dto.skill;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class SkillIdDTO extends BaseDTO {

    private String skillId;

    public SkillIdDTO() {}

    public SkillIdDTO(String skillId) {
        this.skillId = skillId;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }
}

package net.ooder.nexus.dto.skill;

import java.io.Serializable;

public class SkillTestRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String skillId;

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }
}

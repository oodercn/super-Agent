package net.ooder.nexus.dto.skill;

import java.io.Serializable;

public class SkillInstallTestRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String skillId;
    private String version;

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}

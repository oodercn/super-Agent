package net.ooder.nexus.dto.skill;

import java.io.Serializable;

public class SkillSceneTestRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String skillId;
    private String sceneId;

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }
}

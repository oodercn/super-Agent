package net.ooder.nexus.skillcenter.dto.skill;

import net.ooder.nexus.skillcenter.dto.BaseDTO;
import java.util.Map;

public class SkillExecutionDTO extends BaseDTO {

    private String skillId;
    private Map<String, Object> parameters;

    public SkillExecutionDTO() {}

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
}

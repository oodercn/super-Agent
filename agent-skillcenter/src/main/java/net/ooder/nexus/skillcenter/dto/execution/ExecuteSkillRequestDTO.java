package net.ooder.nexus.skillcenter.dto.execution;

import net.ooder.nexus.skillcenter.dto.BaseDTO;
import java.util.Map;

public class ExecuteSkillRequestDTO extends BaseDTO {

    private Map<String, Object> parameters;
    private Map<String, Object> attributes;

    public ExecuteSkillRequestDTO() {}

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}

package net.ooder.nexus.dto.skill;

import java.io.Serializable;
import java.util.Map;

public class SkillConnectionTestRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Object> config;

    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }
}

package net.ooder.nexus.dto.skill;

import java.io.Serializable;
import java.util.Map;

public class SkillConfigUpdateRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Object> config;
    private Boolean testConnection;

    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }

    public Boolean getTestConnection() {
        return testConnection;
    }

    public void setTestConnection(Boolean testConnection) {
        this.testConnection = testConnection;
    }
}

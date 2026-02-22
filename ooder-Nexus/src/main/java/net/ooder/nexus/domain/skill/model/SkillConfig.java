package net.ooder.nexus.domain.skill.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class SkillConfig {
    private String skillId;
    private String name;
    private String type;
    private String version;
    private String description;
    private String status;
    private ConfigSchema configSchema;
    private Map<String, Object> currentConfig;
    private ConnectionInfo connectionInfo;
    private Date lastUpdated;

    public SkillConfig() {}

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ConfigSchema getConfigSchema() {
        return configSchema;
    }

    public void setConfigSchema(ConfigSchema configSchema) {
        this.configSchema = configSchema;
    }

    public Map<String, Object> getCurrentConfig() {
        return currentConfig;
    }

    public void setCurrentConfig(Map<String, Object> currentConfig) {
        this.currentConfig = currentConfig;
    }

    public ConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }

    public void setConnectionInfo(ConnectionInfo connectionInfo) {
        this.connectionInfo = connectionInfo;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}

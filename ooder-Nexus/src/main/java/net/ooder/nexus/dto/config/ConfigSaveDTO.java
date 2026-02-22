package net.ooder.nexus.dto.config;

import java.io.Serializable;
import java.util.Map;

/**
 * Config save DTO
 * Used for saving configuration data
 */
public class ConfigSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Configuration type: system, network, terminal, service
     */
    private String configType;

    /**
     * Configuration data
     */
    private Map<String, Object> configData;

    /**
     * Configuration description
     */
    private String description;

    /**
     * Whether to validate before saving
     */
    private Boolean validate;

    /**
     * Whether to backup previous configuration
     */
    private Boolean backup;

    public ConfigSaveDTO() {
        this.validate = true;
        this.backup = true;
    }

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    public Map<String, Object> getConfigData() {
        return configData;
    }

    public void setConfigData(Map<String, Object> configData) {
        this.configData = configData;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getValidate() {
        return validate;
    }

    public void setValidate(Boolean validate) {
        this.validate = validate;
    }

    public Boolean getBackup() {
        return backup;
    }

    public void setBackup(Boolean backup) {
        this.backup = backup;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ConfigSaveDTO dto = new ConfigSaveDTO();

        public Builder configType(String configType) {
            dto.setConfigType(configType);
            return this;
        }

        public Builder configData(Map<String, Object> configData) {
            dto.setConfigData(configData);
            return this;
        }

        public Builder description(String description) {
            dto.setDescription(description);
            return this;
        }

        public Builder validate(Boolean validate) {
            dto.setValidate(validate);
            return this;
        }

        public Builder backup(Boolean backup) {
            dto.setBackup(backup);
            return this;
        }

        public ConfigSaveDTO build() {
            return dto;
        }
    }
}

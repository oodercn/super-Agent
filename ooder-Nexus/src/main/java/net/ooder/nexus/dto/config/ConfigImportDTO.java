package net.ooder.nexus.dto.config;

import java.io.Serializable;
import java.util.Map;

/**
 * Config import request DTO
 */
public class ConfigImportDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String configType;
    private Map<String, Object> config;
    private Boolean overwrite;

    public String getConfigType() { return configType; }
    public void setConfigType(String configType) { this.configType = configType; }
    public Map<String, Object> getConfig() { return config; }
    public void setConfig(Map<String, Object> config) { this.config = config; }
    public Boolean getOverwrite() { return overwrite; }
    public void setOverwrite(Boolean overwrite) { this.overwrite = overwrite; }
}

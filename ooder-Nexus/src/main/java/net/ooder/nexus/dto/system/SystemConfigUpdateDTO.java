package net.ooder.nexus.dto.system;

import java.io.Serializable;
import java.util.Map;

/**
 * System config update request DTO
 */
public class SystemConfigUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Object> configData;

    public Map<String, Object> getConfigData() { return configData; }
    public void setConfigData(Map<String, Object> configData) { this.configData = configData; }
}

package net.ooder.nexus.dto.network;

import java.io.Serializable;

/**
 * Network setting update request DTO
 */
public class NetworkSettingUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String value;
    private String description;
    private Boolean enabled;

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
}

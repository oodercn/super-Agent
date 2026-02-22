package net.ooder.nexus.dto.config;

import java.io.Serializable;

/**
 * Config export request DTO
 */
public class ConfigExportDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String configType;
    private String format;
    private Boolean includeHistory;

    public String getConfigType() { return configType; }
    public void setConfigType(String configType) { this.configType = configType; }
    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }
    public Boolean getIncludeHistory() { return includeHistory; }
    public void setIncludeHistory(Boolean includeHistory) { this.includeHistory = includeHistory; }
}

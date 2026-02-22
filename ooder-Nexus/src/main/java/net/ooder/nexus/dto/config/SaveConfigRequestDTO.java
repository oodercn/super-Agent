package net.ooder.nexus.dto.config;

import java.io.Serializable;
import java.util.Map;

/**
 * Save config request DTO
 */
public class SaveConfigRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Config key
     */
    private String key;

    /**
     * Config value
     */
    private String value;

    /**
     * Config category
     */
    private String category;

    /**
     * Config description
     */
    private String description;

    /**
     * Additional config properties
     */
    private Map<String, Object> properties;

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Map<String, Object> getProperties() { return properties; }
    public void setProperties(Map<String, Object> properties) { this.properties = properties; }
}

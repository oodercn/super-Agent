package net.ooder.skill.discovery.dto.discovery;

import java.util.List;

public class DiscoveryMethodDTO {
    
    private String id;
    private String name;
    private String icon;
    private String description;
    private String color;
    private boolean requiresAuth;
    private List<ConfigFieldDTO> configFields;

    public DiscoveryMethodDTO() {}

    public DiscoveryMethodDTO(String id, String name, String icon, String description, String color, boolean requiresAuth) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.description = description;
        this.color = color;
        this.requiresAuth = requiresAuth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isRequiresAuth() {
        return requiresAuth;
    }

    public void setRequiresAuth(boolean requiresAuth) {
        this.requiresAuth = requiresAuth;
    }

    public List<ConfigFieldDTO> getConfigFields() {
        return configFields;
    }

    public void setConfigFields(List<ConfigFieldDTO> configFields) {
        this.configFields = configFields;
    }
}

package net.ooder.skill.discovery.dto.discovery;

public class ConfigFieldDTO {
    
    private String name;
    private String label;
    private String type;
    private String placeholder;
    private String defaultValue;
    private boolean required;

    public ConfigFieldDTO() {}

    public ConfigFieldDTO(String name, String label, String type, String placeholder) {
        this.name = name;
        this.label = label;
        this.type = type;
        this.placeholder = placeholder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}

package net.ooder.mvp.skill.scene.dto.scene;

public class ParameterDefDTO {
    private String name;
    private String type;
    private boolean required;
    private Object defaultValue;
    private String description;
    private String[] enumValues;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }
    public Object getDefaultValue() { return defaultValue; }
    public void setDefaultValue(Object defaultValue) { this.defaultValue = defaultValue; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String[] getEnumValues() { return enumValues; }
    public void setEnumValues(String[] enumValues) { this.enumValues = enumValues; }
}

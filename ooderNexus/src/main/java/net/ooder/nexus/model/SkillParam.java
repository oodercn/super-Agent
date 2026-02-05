package net.ooder.nexus.model;

public class SkillParam {
    private String name;
    private String description;
    private Class<?> type;
    private boolean required;
    private Object defaultValue;

    public SkillParam(String name, String description, Class<?> type, boolean required) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.required = required;
    }

    public SkillParam(String name, String description, Class<?> type, boolean required, Object defaultValue) {
        this(name, description, type, required);
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }
}

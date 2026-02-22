package net.ooder.skillcenter.config.model;

/**
 * 配置项 - 符合v0.7.0协议规范
 */
public class ConfigItem {
    
    private String name;
    private String description;
    private ConfigType type;
    private boolean required;
    private boolean secret;
    private Object defaultValue;
    private Object value;
    private ValidationRule validation;
    private String group;
    private int order;

    public ConfigItem() {
        this.required = false;
        this.secret = false;
        this.type = ConfigType.STRING;
        this.order = 100;
    }

    public static ConfigItem required(String name, ConfigType type, String description) {
        ConfigItem item = new ConfigItem();
        item.setName(name);
        item.setType(type);
        item.setDescription(description);
        item.setRequired(true);
        return item;
    }

    public static ConfigItem optional(String name, ConfigType type, Object defaultValue, String description) {
        ConfigItem item = new ConfigItem();
        item.setName(name);
        item.setType(type);
        item.setDescription(description);
        item.setRequired(false);
        item.setDefaultValue(defaultValue);
        return item;
    }

    public static ConfigItem secret(String name, String description) {
        ConfigItem item = new ConfigItem();
        item.setName(name);
        item.setType(ConfigType.SECRET);
        item.setDescription(description);
        item.setRequired(true);
        item.setSecret(true);
        return item;
    }

    public boolean validate(Object value) {
        if (value == null) {
            return !required;
        }
        
        if (validation != null) {
            return validation.validate(value);
        }
        
        return true;
    }

    public Object getEffectiveValue() {
        return value != null ? value : defaultValue;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public ConfigType getType() { return type; }
    public void setType(ConfigType type) { this.type = type; }

    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }

    public boolean isSecret() { return secret; }
    public void setSecret(boolean secret) { this.secret = secret; }

    public Object getDefaultValue() { return defaultValue; }
    public void setDefaultValue(Object defaultValue) { this.defaultValue = defaultValue; }

    public Object getValue() { return value; }
    public void setValue(Object value) { this.value = value; }

    public ValidationRule getValidation() { return validation; }
    public void setValidation(ValidationRule validation) { this.validation = validation; }

    public String getGroup() { return group; }
    public void setGroup(String group) { this.group = group; }

    public int getOrder() { return order; }
    public void setOrder(int order) { this.order = order; }
}

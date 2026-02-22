package net.ooder.skillcenter.capability.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 能力参数 - 符合v0.7.0协议规范
 */
public class CapabilityParameter {
    
    private String name;
    private String type;
    private boolean required;
    private Object defaultValue;
    private String description;
    private List<String> enumValues;
    private Double minValue;
    private Double maxValue;
    private String pattern;

    public CapabilityParameter() {
        this.required = false;
        this.enumValues = new ArrayList<>();
    }

    public static CapabilityParameter required(String name, String type, String description) {
        CapabilityParameter param = new CapabilityParameter();
        param.setName(name);
        param.setType(type);
        param.setRequired(true);
        param.setDescription(description);
        return param;
    }

    public static CapabilityParameter optional(String name, String type, Object defaultValue, String description) {
        CapabilityParameter param = new CapabilityParameter();
        param.setName(name);
        param.setType(type);
        param.setRequired(false);
        param.setDefaultValue(defaultValue);
        param.setDescription(description);
        return param;
    }

    public boolean validate(Object value) {
        if (value == null) {
            return !required;
        }

        if (!enumValues.isEmpty() && !enumValues.contains(value.toString())) {
            return false;
        }

        if ("number".equals(type) && value instanceof Number) {
            double numValue = ((Number) value).doubleValue();
            if (minValue != null && numValue < minValue) return false;
            if (maxValue != null && numValue > maxValue) return false;
        }

        if ("string".equals(type) && pattern != null) {
            return value.toString().matches(pattern);
        }

        return true;
    }

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

    public List<String> getEnumValues() { return enumValues; }
    public void setEnumValues(List<String> enumValues) { this.enumValues = enumValues; }

    public Double getMinValue() { return minValue; }
    public void setMinValue(Double minValue) { this.minValue = minValue; }

    public Double getMaxValue() { return maxValue; }
    public void setMaxValue(Double maxValue) { this.maxValue = maxValue; }

    public String getPattern() { return pattern; }
    public void setPattern(String pattern) { this.pattern = pattern; }
}

package net.ooder.skillcenter.config.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 验证规则 - 符合v0.7.0协议规范
 */
public class ValidationRule {
    
    private String pattern;
    private Double minValue;
    private Double maxValue;
    private Integer minLength;
    private Integer maxLength;
    private List<String> enumValues;
    private String customValidator;

    public ValidationRule() {
        this.enumValues = new ArrayList<>();
    }

    public static ValidationRule pattern(String regex) {
        ValidationRule rule = new ValidationRule();
        rule.setPattern(regex);
        return rule;
    }

    public static ValidationRule range(double min, double max) {
        ValidationRule rule = new ValidationRule();
        rule.setMinValue(min);
        rule.setMaxValue(max);
        return rule;
    }

    public static ValidationRule length(int min, int max) {
        ValidationRule rule = new ValidationRule();
        rule.setMinLength(min);
        rule.setMaxLength(max);
        return rule;
    }

    public static ValidationRule enumValues(String... values) {
        ValidationRule rule = new ValidationRule();
        for (String value : values) {
            rule.enumValues.add(value);
        }
        return rule;
    }

    public boolean validate(Object value) {
        if (value == null) {
            return true;
        }

        if (pattern != null) {
            try {
                if (!Pattern.matches(pattern, value.toString())) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }

        if (value instanceof Number) {
            double numValue = ((Number) value).doubleValue();
            if (minValue != null && numValue < minValue) return false;
            if (maxValue != null && numValue > maxValue) return false;
        }

        if (value instanceof String) {
            int len = ((String) value).length();
            if (minLength != null && len < minLength) return false;
            if (maxLength != null && len > maxLength) return false;
        }

        if (!enumValues.isEmpty()) {
            if (!enumValues.contains(value.toString())) {
                return false;
            }
        }

        return true;
    }

    public String getPattern() { return pattern; }
    public void setPattern(String pattern) { this.pattern = pattern; }

    public Double getMinValue() { return minValue; }
    public void setMinValue(Double minValue) { this.minValue = minValue; }

    public Double getMaxValue() { return maxValue; }
    public void setMaxValue(Double maxValue) { this.maxValue = maxValue; }

    public Integer getMinLength() { return minLength; }
    public void setMinLength(Integer minLength) { this.minLength = minLength; }

    public Integer getMaxLength() { return maxLength; }
    public void setMaxLength(Integer maxLength) { this.maxLength = maxLength; }

    public List<String> getEnumValues() { return enumValues; }
    public void setEnumValues(List<String> enumValues) { this.enumValues = enumValues; }

    public String getCustomValidator() { return customValidator; }
    public void setCustomValidator(String customValidator) { this.customValidator = customValidator; }
}

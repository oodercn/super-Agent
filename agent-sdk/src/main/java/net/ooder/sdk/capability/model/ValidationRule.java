package net.ooder.sdk.capability.model;

import java.util.List;

public class ValidationRule {
    
    private String pattern;
    private Object min;
    private Object max;
    private List<Object> allowedValues;
    
    public String getPattern() { return pattern; }
    public void setPattern(String pattern) { this.pattern = pattern; }
    
    public Object getMin() { return min; }
    public void setMin(Object min) { this.min = min; }
    
    public Object getMax() { return max; }
    public void setMax(Object max) { this.max = max; }
    
    public List<Object> getAllowedValues() { return allowedValues; }
    public void setAllowedValues(List<Object> allowedValues) { this.allowedValues = allowedValues; }
}

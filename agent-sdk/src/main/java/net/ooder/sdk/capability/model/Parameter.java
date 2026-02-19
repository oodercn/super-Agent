package net.ooder.sdk.capability.model;

public class Parameter {
    
    private String name;
    private ParameterType type;
    private boolean required;
    private Object defaultValue;
    private String description;
    private ValidationRule validation;
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public ParameterType getType() { return type; }
    public void setType(ParameterType type) { this.type = type; }
    
    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }
    
    public Object getDefaultValue() { return defaultValue; }
    public void setDefaultValue(Object defaultValue) { this.defaultValue = defaultValue; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public ValidationRule getValidation() { return validation; }
    public void setValidation(ValidationRule validation) { this.validation = validation; }
}

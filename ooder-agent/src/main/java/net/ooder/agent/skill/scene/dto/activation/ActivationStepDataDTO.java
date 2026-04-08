package net.ooder.mvp.skill.scene.dto.activation;

import java.util.List;
import java.util.Map;

public class ActivationStepDataDTO {
    
    private String stepId;
    private String name;
    private String description;
    private Boolean required;
    private Boolean skippable;
    private Boolean autoExecute;
    private List<String> privateCapabilities;
    private Map<String, Object> config;
    
    public ActivationStepDataDTO() {
    }
    
    public String getStepId() { return stepId; }
    public void setStepId(String stepId) { this.stepId = stepId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Boolean getRequired() { return required; }
    public void setRequired(Boolean required) { this.required = required; }
    
    public Boolean getSkippable() { return skippable; }
    public void setSkippable(Boolean skippable) { this.skippable = skippable; }
    
    public Boolean getAutoExecute() { return autoExecute; }
    public void setAutoExecute(Boolean autoExecute) { this.autoExecute = autoExecute; }
    
    public List<String> getPrivateCapabilities() { return privateCapabilities; }
    public void setPrivateCapabilities(List<String> privateCapabilities) { this.privateCapabilities = privateCapabilities; }
    
    public Map<String, Object> getConfig() { return config; }
    public void setConfig(Map<String, Object> config) { this.config = config; }
}

package net.ooder.skill.config.dto;

import java.util.List;

public class CreatePolicyRequest {
    
    private String name;
    private String type;
    private boolean enabled;
    private List<String> rules;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public List<String> getRules() { return rules; }
    public void setRules(List<String> rules) { this.rules = rules; }
}

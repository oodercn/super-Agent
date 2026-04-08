package net.ooder.skill.config.dto;

import java.util.List;

public class CliActionDTO {
    
    private String action;
    private String name;
    private String description;
    private List<String> params;

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<String> getParams() { return params; }
    public void setParams(List<String> params) { this.params = params; }
}

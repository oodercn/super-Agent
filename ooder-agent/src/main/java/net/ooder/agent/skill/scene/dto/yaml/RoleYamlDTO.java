package net.ooder.mvp.skill.scene.dto.yaml;

import java.util.List;

public class RoleYamlDTO {
    
    private String name;
    private String description;
    private Boolean required;
    private Integer minCount;
    private Integer maxCount;
    private List<String> permissions;
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Boolean getRequired() { return required; }
    public void setRequired(Boolean required) { this.required = required; }
    
    public Integer getMinCount() { return minCount; }
    public void setMinCount(Integer minCount) { this.minCount = minCount; }
    
    public Integer getMaxCount() { return maxCount; }
    public void setMaxCount(Integer maxCount) { this.maxCount = maxCount; }
    
    public List<String> getPermissions() { return permissions; }
    public void setPermissions(List<String> permissions) { this.permissions = permissions; }
}

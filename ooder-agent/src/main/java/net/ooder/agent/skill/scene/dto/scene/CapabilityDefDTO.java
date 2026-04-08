package net.ooder.mvp.skill.scene.dto.scene;

import java.util.List;
import java.util.Map;

public class CapabilityDefDTO {
    private String capId;
    private String name;
    private String description;
    private String category;
    private List<Map<String, Object>> parameters;
    private Map<String, Object> returns;
    private List<String> permissions;

    public String getCapId() { return capId; }
    public void setCapId(String capId) { this.capId = capId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public List<Map<String, Object>> getParameters() { return parameters; }
    public void setParameters(List<Map<String, Object>> parameters) { this.parameters = parameters; }
    public Map<String, Object> getReturns() { return returns; }
    public void setReturns(Map<String, Object> returns) { this.returns = returns; }
    public List<String> getPermissions() { return permissions; }
    public void setPermissions(List<String> permissions) { this.permissions = permissions; }
}

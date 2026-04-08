package net.ooder.mvp.skill.scene.dto.scene;

import net.ooder.mvp.skill.scene.dto.menu.MenuConfigDTO;
import java.util.List;
import java.util.Map;

public class SceneTemplateDTO {
    private String templateId;
    private String name;
    private String description;
    private String version;
    private String category;
    private String type;
    private String status;
    private String participantMode;
    private boolean active;
    private long createTime;
    private long updateTime;
    private List<CapabilityDefDTO> capabilities;
    private List<RoleDefinitionDTO> roles;
    private Map<String, List<MenuConfigDTO>> menus;
    private WorkflowDefinitionDTO workflow;
    private Map<String, Object> metadata;

    public String getTemplateId() { return templateId; }
    public void setTemplateId(String templateId) { this.templateId = templateId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getParticipantMode() { return participantMode; }
    public void setParticipantMode(String participantMode) { this.participantMode = participantMode; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
    public long getUpdateTime() { return updateTime; }
    public void setUpdateTime(long updateTime) { this.updateTime = updateTime; }
    public List<CapabilityDefDTO> getCapabilities() { return capabilities; }
    public void setCapabilities(List<CapabilityDefDTO> capabilities) { this.capabilities = capabilities; }
    public List<RoleDefinitionDTO> getRoles() { return roles; }
    public void setRoles(List<RoleDefinitionDTO> roles) { this.roles = roles; }
    public Map<String, List<MenuConfigDTO>> getMenus() { return menus; }
    public void setMenus(Map<String, List<MenuConfigDTO>> menus) { this.menus = menus; }
    public List<MenuConfigDTO> getMenus(String role) {
        if (menus == null) return null;
        return menus.get(role);
    }
    public WorkflowDefinitionDTO getWorkflow() { return workflow; }
    public void setWorkflow(WorkflowDefinitionDTO workflow) { this.workflow = workflow; }
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
}

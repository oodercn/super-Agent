package net.ooder.mvp.skill.scene.dto.menu;

import java.util.List;

public class MenuGenerateRequestDTO {
    private String userId;
    private String sceneGroupId;
    private String sceneName;
    private String role;
    private String templateId;
    private List<MenuItemDTO> customItems;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getSceneGroupId() { return sceneGroupId; }
    public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
    public String getSceneName() { return sceneName; }
    public void setSceneName(String sceneName) { this.sceneName = sceneName; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getTemplateId() { return templateId; }
    public void setTemplateId(String templateId) { this.templateId = templateId; }
    public List<MenuItemDTO> getCustomItems() { return customItems; }
    public void setCustomItems(List<MenuItemDTO> customItems) { this.customItems = customItems; }
}

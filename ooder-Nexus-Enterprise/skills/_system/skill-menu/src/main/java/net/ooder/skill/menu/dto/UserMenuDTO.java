package net.ooder.skill.menu.dto;

import java.util.List;

public class UserMenuDTO {
    
    private String userId;
    private String menuId;
    private String sceneGroupId;
    private String sceneName;
    private String role;
    private List<MenuItemDTO> items;
    private Long createTime;
    private Long updateTime;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getMenuId() { return menuId; }
    public void setMenuId(String menuId) { this.menuId = menuId; }
    public String getSceneGroupId() { return sceneGroupId; }
    public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
    public String getSceneName() { return sceneName; }
    public void setSceneName(String sceneName) { this.sceneName = sceneName; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public List<MenuItemDTO> getItems() { return items; }
    public void setItems(List<MenuItemDTO> items) { this.items = items; }
    public Long getCreateTime() { return createTime; }
    public void setCreateTime(Long createTime) { this.createTime = createTime; }
    public Long getUpdateTime() { return updateTime; }
    public void setUpdateTime(Long updateTime) { this.updateTime = updateTime; }
}

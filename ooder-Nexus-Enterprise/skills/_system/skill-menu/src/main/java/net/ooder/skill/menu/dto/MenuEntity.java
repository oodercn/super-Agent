package net.ooder.skill.menu.dto;

import java.util.List;

public class MenuEntity {
    
    private Long id;
    private String menuId;
    private String userId;
    private String sceneGroupId;
    private String sceneName;
    private String role;
    private String menuData;
    private Long createTime;
    private Long updateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMenuId() { return menuId; }
    public void setMenuId(String menuId) { this.menuId = menuId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getSceneGroupId() { return sceneGroupId; }
    public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
    public String getSceneName() { return sceneName; }
    public void setSceneName(String sceneName) { this.sceneName = sceneName; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getMenuData() { return menuData; }
    public void setMenuData(String menuData) { this.menuData = menuData; }
    public Long getCreateTime() { return createTime; }
    public void setCreateTime(Long createTime) { this.createTime = createTime; }
    public Long getUpdateTime() { return updateTime; }
    public void setUpdateTime(Long updateTime) { this.updateTime = updateTime; }
}

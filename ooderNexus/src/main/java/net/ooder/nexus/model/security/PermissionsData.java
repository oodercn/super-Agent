package net.ooder.nexus.model.security;

import java.util.List;

/**
 * 权限数据实体Bean
 * 
 * 用于表示系统的权限配置信息
 * 
 * @version 1.0.0
 */
public class PermissionsData {
    
    /**
     * 权限ID
     */
    private String id;
    
    /**
     * 权限名称
     */
    private String name;
    
    /**
     * 权限描述
     */
    private String description;
    
    /**
     * 权限类型
     */
    private String type;
    
    /**
     * 关联角色
     */
    private List<String> roles;
    
    /**
     * 关联资源
     */
    private List<String> resources;
    
    /**
     * 操作权限
     */
    private List<String> actions;
    
    /**
     * 是否启用
     */
    private boolean enabled;
    
    /**
     * 创建时间戳
     */
    private long createdAt;
    
    /**
     * 最后更新时间戳
     */
    private long lastUpdated;
    
    /**
     * 构造方法
     */
    public PermissionsData() {
    }
    
    /**
     * 构造方法
     * 
     * @param id 权限ID
     * @param name 权限名称
     * @param description 权限描述
     * @param type 权限类型
     * @param roles 关联角色
     * @param resources 关联资源
     * @param actions 操作权限
     * @param enabled 是否启用
     * @param createdAt 创建时间戳
     * @param lastUpdated 最后更新时间戳
     */
    public PermissionsData(String id, String name, String description, String type, 
                         List<String> roles, List<String> resources, List<String> actions, 
                         boolean enabled, long createdAt, long lastUpdated) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.roles = roles;
        this.resources = resources;
        this.actions = actions;
        this.enabled = enabled;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
    }
    
    // Getters and Setters
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public List<String> getRoles() {
        return roles;
    }
    
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
    
    public List<String> getResources() {
        return resources;
    }
    
    public void setResources(List<String> resources) {
        this.resources = resources;
    }
    
    public List<String> getActions() {
        return actions;
    }
    
    public void setActions(List<String> actions) {
        this.actions = actions;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public long getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
    
    public long getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
package net.ooder.nexus.model.security;

/**
 * 用户信息实体Bean
 * 
 * 用于表示系统用户的详细信息
 * 
 * @version 1.0.0
 */
public class UserInfo {
    
    /**
     * 用户ID
     */
    private String id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 显示名称
     */
    private String displayName;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 电话
     */
    private String phone;
    
    /**
     * 用户角色
     */
    private String role;
    
    /**
     * 用户状态
     */
    private String status;
    
    /**
     * 是否启用
     */
    private boolean enabled;
    
    /**
     * 最后登录时间戳
     */
    private long lastLogin;
    
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
    public UserInfo() {
    }
    
    /**
     * 构造方法
     * 
     * @param id 用户ID
     * @param username 用户名
     * @param password 密码
     * @param displayName 显示名称
     * @param email 邮箱
     * @param phone 电话
     * @param role 用户角色
     * @param status 用户状态
     * @param enabled 是否启用
     * @param lastLogin 最后登录时间戳
     * @param createdAt 创建时间戳
     * @param lastUpdated 最后更新时间戳
     */
    public UserInfo(String id, String username, String password, String displayName, 
                   String email, String phone, String role, String status, 
                   boolean enabled, long lastLogin, long createdAt, long lastUpdated) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.status = status;
        this.enabled = enabled;
        this.lastLogin = lastLogin;
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
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public long getLastLogin() {
        return lastLogin;
    }
    
    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
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
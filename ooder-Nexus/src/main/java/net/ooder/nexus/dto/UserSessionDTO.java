package net.ooder.nexus.dto;

import java.util.List;

public class UserSessionDTO {
    private String userId;
    private String username;
    private String name;
    private String email;
    private String role;
    private String roleType;
    private String departmentId;
    private String departmentName;
    private String title;
    private Long loginTime;
    private String token;
    private List<String> permissions;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getRoleType() { return roleType; }
    public void setRoleType(String roleType) { this.roleType = roleType; }
    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Long getLoginTime() { return loginTime; }
    public void setLoginTime(Long loginTime) { this.loginTime = loginTime; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public List<String> getPermissions() { return permissions; }
    public void setPermissions(List<String> permissions) { this.permissions = permissions; }
}

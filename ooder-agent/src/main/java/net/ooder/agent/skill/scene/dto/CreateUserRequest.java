package net.ooder.mvp.skill.scene.dto;

public class CreateUserRequest {
    private String name;
    private String email;
    private String orgRole;
    private String departmentId;
    private String password;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getOrgRole() { return orgRole; }
    public void setOrgRole(String orgRole) { this.orgRole = orgRole; }
    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

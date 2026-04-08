package net.ooder.mvp.skill.scene.dto.selector;

public class UserNodeDTO {
    
    private String id;
    private String name;
    private String role;
    private String dept;
    
    public UserNodeDTO() {
    }
    
    public UserNodeDTO(String id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }
    
    public UserNodeDTO(String id, String name, String role, String dept) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.dept = dept;
    }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public String getDept() { return dept; }
    public void setDept(String dept) { this.dept = dept; }
}

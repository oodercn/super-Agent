package net.ooder.mvp.skill.scene.dto.selector;

public class OrgNodeDTO {
    
    private String id;
    private String name;
    private String type;
    private String role;
    private java.util.List<OrgNodeDTO> children;
    
    public OrgNodeDTO() {
    }
    
    public OrgNodeDTO(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public java.util.List<OrgNodeDTO> getChildren() { return children; }
    public void setChildren(java.util.List<OrgNodeDTO> children) { this.children = children; }
}

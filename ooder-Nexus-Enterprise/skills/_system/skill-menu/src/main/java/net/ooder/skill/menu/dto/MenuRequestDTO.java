package net.ooder.skill.menu.dto;

public class MenuRequestDTO {
    
    private String role;
    private String context;

    public MenuRequestDTO() {
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}

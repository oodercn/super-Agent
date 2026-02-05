package net.ooder.sdk.agent.scene;

import java.util.List;

public class MemberRole {
    private String roleId;
    private String name;
    private List<String> capabilities;
    private boolean required;

    public MemberRole() {
    }

    public MemberRole(String roleId, String name, List<String> capabilities, boolean required) {
        this.roleId = roleId;
        this.name = name;
        this.capabilities = capabilities;
        this.required = required;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<String> capabilities) {
        this.capabilities = capabilities;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
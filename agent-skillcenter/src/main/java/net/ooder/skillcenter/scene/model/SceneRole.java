package net.ooder.skillcenter.scene.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 场景角色 - 符合v0.7.0协议规范
 */
public class SceneRole {
    
    private String roleId;
    private String name;
    private boolean required;
    private List<String> capabilities;
    private String description;

    public SceneRole() {
        this.required = false;
        this.capabilities = new ArrayList<>();
    }

    public static SceneRole of(String roleId, String name, boolean required) {
        SceneRole role = new SceneRole();
        role.setRoleId(roleId);
        role.setName(name);
        role.setRequired(required);
        return role;
    }

    public static SceneRole required(String roleId, String name) {
        return of(roleId, name, true);
    }

    public static SceneRole optional(String roleId, String name) {
        return of(roleId, name, false);
    }

    public void addCapability(String capabilityId) {
        if (!capabilities.contains(capabilityId)) {
            capabilities.add(capabilityId);
        }
    }

    public String getRoleId() { return roleId; }
    public void setRoleId(String roleId) { this.roleId = roleId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }

    public List<String> getCapabilities() { return capabilities; }
    public void setCapabilities(List<String> capabilities) { this.capabilities = capabilities; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

package net.ooder.skillcenter.scene.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 场景定义 - 符合v0.7.0协议规范
 */
public class SceneDefinition {
    
    private String name;
    private String description;
    private List<String> capabilities;
    private List<SceneRole> roles;
    private CommunicationProtocol communicationProtocol;
    private SecurityPolicy securityPolicy;
    private boolean allowParallel;
    private int maxParticipants;
    private long timeout;

    public SceneDefinition() {
        this.capabilities = new ArrayList<>();
        this.roles = new ArrayList<>();
        this.communicationProtocol = CommunicationProtocol.HTTP;
        this.securityPolicy = SecurityPolicy.API_KEY;
        this.allowParallel = true;
        this.maxParticipants = 100;
        this.timeout = 30000;
    }

    public static SceneDefinition of(String name, String description) {
        SceneDefinition scene = new SceneDefinition();
        scene.setName(name);
        scene.setDescription(description);
        return scene;
    }

    public void addCapability(String capabilityId) {
        if (!capabilities.contains(capabilityId)) {
            capabilities.add(capabilityId);
        }
    }

    public void addRole(SceneRole role) {
        roles.add(role);
    }

    public List<SceneRole> getRequiredRoles() {
        List<SceneRole> required = new ArrayList<>();
        for (SceneRole role : roles) {
            if (role.isRequired()) {
                required.add(role);
            }
        }
        return required;
    }

    public boolean hasCapability(String capabilityId) {
        return capabilities.contains(capabilityId);
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getCapabilities() { return capabilities; }
    public void setCapabilities(List<String> capabilities) { this.capabilities = capabilities; }

    public List<SceneRole> getRoles() { return roles; }
    public void setRoles(List<SceneRole> roles) { this.roles = roles; }

    public CommunicationProtocol getCommunicationProtocol() { return communicationProtocol; }
    public void setCommunicationProtocol(CommunicationProtocol communicationProtocol) { this.communicationProtocol = communicationProtocol; }

    public SecurityPolicy getSecurityPolicy() { return securityPolicy; }
    public void setSecurityPolicy(SecurityPolicy securityPolicy) { this.securityPolicy = securityPolicy; }

    public boolean isAllowParallel() { return allowParallel; }
    public void setAllowParallel(boolean allowParallel) { this.allowParallel = allowParallel; }

    public int getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(int maxParticipants) { this.maxParticipants = maxParticipants; }

    public long getTimeout() { return timeout; }
    public void setTimeout(long timeout) { this.timeout = timeout; }
}

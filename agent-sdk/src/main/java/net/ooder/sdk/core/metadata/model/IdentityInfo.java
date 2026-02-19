
package net.ooder.sdk.core.metadata.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class IdentityInfo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String agentId;
    private String agentName;
    private String agentType;
    private String ownerId;
    private String organizationId;
    private String sceneId;
    private String sceneGroupId;
    private String skillId;
    private String role;
    private Map<String, String> attributes;
    
    public IdentityInfo() {
        this.attributes = new HashMap<>();
    }
    
    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }
    
    public String getAgentName() { return agentName; }
    public void setAgentName(String agentName) { this.agentName = agentName; }
    
    public String getAgentType() { return agentType; }
    public void setAgentType(String agentType) { this.agentType = agentType; }
    
    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
    
    public String getOrganizationId() { return organizationId; }
    public void setOrganizationId(String organizationId) { this.organizationId = organizationId; }
    
    public String getSceneId() { return sceneId; }
    public void setSceneId(String sceneId) { this.sceneId = sceneId; }
    
    public String getSceneGroupId() { return sceneGroupId; }
    public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
    
    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public Map<String, String> getAttributes() { return attributes; }
    public void setAttributes(Map<String, String> attributes) { 
        this.attributes = attributes != null ? attributes : new HashMap<>(); 
    }
    
    public void setAttribute(String key, String value) { attributes.put(key, value); }
    public String getAttribute(String key) { return attributes.get(key); }
    
    public boolean isMcpAgent() { return "MCP".equalsIgnoreCase(agentType); }
    public boolean isRouteAgent() { return "ROUTE".equalsIgnoreCase(agentType); }
    public boolean isEndAgent() { return "END".equalsIgnoreCase(agentType); }
    
    public boolean hasScene() { return sceneId != null && !sceneId.isEmpty(); }
    public boolean hasSceneGroup() { return sceneGroupId != null && !sceneGroupId.isEmpty(); }
    public boolean hasSkill() { return skillId != null && !skillId.isEmpty(); }
    
    public String getIdentityKey() {
        return agentId != null ? agentId : "unknown";
    }
    
    public String getDisplayName() {
        if (agentName != null && !agentName.isEmpty()) {
            return agentName;
        }
        return agentId != null ? agentId : "unnamed";
    }
    
    public IdentitySnapshot snapshot() {
        return new IdentitySnapshot(this);
    }
    
    public static IdentityInfo of(String agentId, String agentName, String agentType) {
        IdentityInfo identity = new IdentityInfo();
        identity.setAgentId(agentId);
        identity.setAgentName(agentName);
        identity.setAgentType(agentType);
        return identity;
    }
    
    public static IdentityInfo ofAgentId(String agentId) {
        IdentityInfo identity = new IdentityInfo();
        identity.setAgentId(agentId);
        return identity;
    }
}

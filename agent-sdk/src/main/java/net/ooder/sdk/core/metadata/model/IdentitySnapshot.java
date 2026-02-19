
package net.ooder.sdk.core.metadata.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class IdentitySnapshot implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final String agentId;
    private final String agentName;
    private final String agentType;
    private final String ownerId;
    private final String organizationId;
    private final String sceneId;
    private final String sceneGroupId;
    private final String skillId;
    private final String role;
    private final Map<String, String> attributes;
    private final long snapshotTime;
    
    public IdentitySnapshot(IdentityInfo identity) {
        this.agentId = identity.getAgentId();
        this.agentName = identity.getAgentName();
        this.agentType = identity.getAgentType();
        this.ownerId = identity.getOwnerId();
        this.organizationId = identity.getOrganizationId();
        this.sceneId = identity.getSceneId();
        this.sceneGroupId = identity.getSceneGroupId();
        this.skillId = identity.getSkillId();
        this.role = identity.getRole();
        this.attributes = new HashMap<>(identity.getAttributes());
        this.snapshotTime = System.currentTimeMillis();
    }
    
    public String getAgentId() { return agentId; }
    public String getAgentName() { return agentName; }
    public String getAgentType() { return agentType; }
    public String getOwnerId() { return ownerId; }
    public String getOrganizationId() { return organizationId; }
    public String getSceneId() { return sceneId; }
    public String getSceneGroupId() { return sceneGroupId; }
    public String getSkillId() { return skillId; }
    public String getRole() { return role; }
    public Map<String, String> getAttributes() { return new HashMap<>(attributes); }
    public long getSnapshotTime() { return snapshotTime; }
    
    public boolean hasScene() { return sceneId != null && !sceneId.isEmpty(); }
    public boolean hasSceneGroup() { return sceneGroupId != null && !sceneGroupId.isEmpty(); }
    public boolean hasSkill() { return skillId != null && !skillId.isEmpty(); }
    
    public String getIdentityKey() {
        return agentId != null ? agentId : "unknown";
    }
}

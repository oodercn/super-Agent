
package net.ooder.sdk.southbound.adapter.model;

import java.util.Map;

public class CollaborationMember {
    
    private String agentId;
    private String agentName;
    private String role;
    private String status;
    private long joinedAt;
    private long lastActiveAt;
    private Map<String, Object> metadata;
    
    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }
    
    public String getAgentName() { return agentName; }
    public void setAgentName(String agentName) { this.agentName = agentName; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public long getJoinedAt() { return joinedAt; }
    public void setJoinedAt(long joinedAt) { this.joinedAt = joinedAt; }
    
    public long getLastActiveAt() { return lastActiveAt; }
    public void setLastActiveAt(long lastActiveAt) { this.lastActiveAt = lastActiveAt; }
    
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    
    public boolean isActive() {
        return "active".equals(status);
    }
    
    public boolean isPrimary() {
        return "primary".equals(role);
    }
}

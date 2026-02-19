package net.ooder.sdk.api.scene.store;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AgentRegistration implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String agentId;
    private String agentName;
    private String sceneId;
    private String groupId;
    private String endpoint;
    private String role;
    private String status;
    private Map<String, Object> composition;
    private Map<String, Object> capabilities;
    private long registerTime;
    private long lastHeartbeat;
    private Map<String, Object> metadata;
    
    public AgentRegistration() {
        this.composition = new ConcurrentHashMap<>();
        this.capabilities = new ConcurrentHashMap<>();
        this.metadata = new ConcurrentHashMap<>();
        this.registerTime = System.currentTimeMillis();
        this.lastHeartbeat = System.currentTimeMillis();
        this.status = "registered";
        this.role = "member";
    }
    
    public AgentRegistration(String agentId, String sceneId, String groupId) {
        this();
        this.agentId = agentId;
        this.sceneId = sceneId;
        this.groupId = groupId;
    }
    
    public String getAgentId() {
        return agentId;
    }
    
    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }
    
    public String getAgentName() {
        return agentName;
    }
    
    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }
    
    public String getSceneId() {
        return sceneId;
    }
    
    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }
    
    public String getGroupId() {
        return groupId;
    }
    
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    
    public String getEndpoint() {
        return endpoint;
    }
    
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Map<String, Object> getComposition() {
        return composition;
    }
    
    public void setComposition(Map<String, Object> composition) {
        this.composition = composition != null ? composition : new ConcurrentHashMap<>();
    }
    
    public void addCompositionItem(String key, Object value) {
        this.composition.put(key, value);
    }
    
    public Map<String, Object> getCapabilities() {
        return capabilities;
    }
    
    public void setCapabilities(Map<String, Object> capabilities) {
        this.capabilities = capabilities != null ? capabilities : new ConcurrentHashMap<>();
    }
    
    public void addCapability(String name, Object capability) {
        this.capabilities.put(name, capability);
    }
    
    public long getRegisterTime() {
        return registerTime;
    }
    
    public void setRegisterTime(long registerTime) {
        this.registerTime = registerTime;
    }
    
    public long getLastHeartbeat() {
        return lastHeartbeat;
    }
    
    public void setLastHeartbeat(long lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }
    
    public void updateHeartbeat() {
        this.lastHeartbeat = System.currentTimeMillis();
    }
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata != null ? metadata : new ConcurrentHashMap<>();
    }
    
    public boolean isAlive(long timeoutMs) {
        return (System.currentTimeMillis() - lastHeartbeat) < timeoutMs;
    }
    
    public boolean isPrimary() {
        return "primary".equals(role);
    }
    
    public boolean isBackup() {
        return "backup".equals(role);
    }
    
    @Override
    public String toString() {
        return "AgentRegistration{" +
                "agentId='" + agentId + '\'' +
                ", agentName='" + agentName + '\'' +
                ", sceneId='" + sceneId + '\'' +
                ", groupId='" + groupId + '\'' +
                ", role='" + role + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

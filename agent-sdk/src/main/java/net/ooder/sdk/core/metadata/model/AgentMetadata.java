
package net.ooder.sdk.core.metadata.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgentMetadata implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final IdentityInfo identity;
    private final LocationInfo location;
    private final ResourceInfo resource;
    private final Timeline history;
    
    private SceneGroupInstance currentInstance;
    private Map<String, Object> extendedInfo;
    
    public AgentMetadata() {
        this.identity = new IdentityInfo();
        this.location = new LocationInfo();
        this.resource = new ResourceInfo();
        this.history = new Timeline();
        this.extendedInfo = new HashMap<>();
    }
    
    public AgentMetadata(String agentId) {
        this();
        this.identity.setAgentId(agentId);
    }
    
    public AgentMetadata(IdentityInfo identity, LocationInfo location, ResourceInfo resource) {
        this.identity = identity != null ? identity : new IdentityInfo();
        this.location = location != null ? location : new LocationInfo();
        this.resource = resource != null ? resource : new ResourceInfo();
        this.history = new Timeline();
        this.extendedInfo = new HashMap<>();
    }
    
    public IdentityInfo identity() { return identity; }
    public LocationInfo location() { return location; }
    public ResourceInfo resource() { return resource; }
    public Timeline history() { return history; }
    
    public SceneGroupInstance currentInstance() { return currentInstance; }
    
    public Map<String, Object> extendedInfo() { return extendedInfo; }
    
    public void setCurrentInstance(SceneGroupInstance instance) {
        this.currentInstance = instance;
    }
    
    public void startInstance(String sceneGroupId, String sceneId) {
        this.currentInstance = new SceneGroupInstance(sceneGroupId, sceneId);
        this.currentInstance.start();
        this.identity.setSceneGroupId(sceneGroupId);
        this.identity.setSceneId(sceneId);
        
        recordEvent("instance_started", sceneGroupId);
    }
    
    public void completeInstance(boolean success, String message) {
        if (currentInstance != null) {
            currentInstance.complete(success, message);
            recordEvent(success ? "instance_completed" : "instance_failed", currentInstance.getSceneGroupId());
        }
    }
    
    public void terminateInstance(String reason) {
        if (currentInstance != null) {
            currentInstance.terminate(reason);
            recordEvent("instance_terminated", currentInstance.getSceneGroupId());
        }
    }
    
    public String getAgentId() { return identity.getAgentId(); }
    public String getAgentName() { return identity.getAgentName(); }
    public String getAgentType() { return identity.getAgentType(); }
    
    public String getAddress() { return location.getAddress(); }
    public String getHost() { return location.getHost(); }
    public int getPort() { return location.getPort(); }
    
    public boolean isOnline() { return history.isOnline(); }
    public long getUptime() { return history.getUptime(); }
    
    public String getStatus() { return resource.getStatus(); }
    public boolean hasCapability(String capability) { return resource.hasCapability(capability); }
    
    public void recordEvent(String event) {
        history.record(event, identity, location, resource);
    }
    
    public void recordEvent(String event, String detail) {
        history.record(event + ": " + detail, identity, location, resource);
    }
    
    public void updateContext() {
        history.updateContext(identity, location, resource);
    }
    
    public Context getCurrentContext() {
        return history.getCurrentContext();
    }
    
    public Context getContextAt(long timestamp) {
        return history.getContextAt(timestamp);
    }
    
    public String getSceneId() { return identity.getSceneId(); }
    public String getSceneGroupId() { return identity.getSceneGroupId(); }
    public String getSkillId() { return identity.getSkillId(); }
    
    public boolean isInScene() { return identity.hasScene(); }
    public boolean isInSceneGroup() { return identity.hasSceneGroup(); }
    public boolean hasSkill() { return identity.hasSkill(); }
    
    public String getSummary() {
        return String.format("Agent[id=%s, name=%s, type=%s, addr=%s, status=%s]",
            identity.getAgentId(), 
            identity.getDisplayName(), 
            identity.getAgentType(),
            location.getAddress() != null ? location.getAddress() : "unknown",
            resource.getStatus());
    }
    
    public String getFullReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════════════════════════╗\n");
        sb.append("║              AGENT METADATA REPORT                       ║\n");
        sb.append("╠══════════════════════════════════════════════════════════╣\n");
        
        sb.append("║ [基本信息 - Identity]\n");
        sb.append("║   Agent ID: ").append(identity.getAgentId()).append("\n");
        sb.append("║   Name: ").append(identity.getDisplayName()).append("\n");
        sb.append("║   Type: ").append(identity.getAgentType()).append("\n");
        sb.append("║   Role: ").append(identity.getRole()).append("\n");
        sb.append("║   Scene ID: ").append(identity.getSceneId()).append("\n");
        sb.append("║   Scene Group: ").append(identity.getSceneGroupId()).append("\n");
        
        sb.append("╠══════════════════════════════════════════════════════════╣\n");
        sb.append("║ [基本信息 - Location]\n");
        sb.append("║   Address: ").append(location.getAddress()).append("\n");
        sb.append("║   Region: ").append(location.getRegion()).append("\n");
        sb.append("║   Zone: ").append(location.getZone()).append("\n");
        
        sb.append("╠══════════════════════════════════════════════════════════╣\n");
        sb.append("║ [基本信息 - Resource]\n");
        sb.append("║   Status: ").append(resource.getStatus()).append("\n");
        sb.append("║   Capabilities: ").append(resource.getCapabilities()).append("\n");
        
        sb.append("╠══════════════════════════════════════════════════════════╣\n");
        sb.append("║ [历史信息 - Timeline]\n");
        sb.append("║   ").append(history.getStatusSummary()).append("\n");
        
        if (currentInstance != null) {
            sb.append("╠══════════════════════════════════════════════════════════╣\n");
            sb.append("║ [实例信息 - SceneGroup]\n");
            sb.append("║   Group ID: ").append(currentInstance.getSceneGroupId()).append("\n");
            sb.append("║   State: ").append(currentInstance.getState()).append("\n");
            sb.append("║   Duration: ").append(currentInstance.getDurationFormatted()).append("\n");
        }
        
        sb.append("╚══════════════════════════════════════════════════════════╝\n");
        return sb.toString();
    }
    
    public static Builder builder() { return new Builder(); }
    
    public static class Builder {
        private final AgentMetadata metadata = new AgentMetadata();
        
        public Builder agentId(String agentId) { 
            metadata.identity().setAgentId(agentId); 
            return this; 
        }
        
        public Builder agentName(String agentName) { 
            metadata.identity().setAgentName(agentName); 
            return this; 
        }
        
        public Builder agentType(String agentType) { 
            metadata.identity().setAgentType(agentType); 
            return this; 
        }
        
        public Builder host(String host) { 
            metadata.location().setHost(host); 
            return this; 
        }
        
        public Builder port(int port) { 
            metadata.location().setPort(port); 
            return this; 
        }
        
        public Builder region(String region) { 
            metadata.location().setRegion(region); 
            return this; 
        }
        
        public Builder status(String status) { 
            metadata.resource().setStatus(status); 
            return this; 
        }
        
        public Builder capability(String capability) { 
            metadata.resource().addCapability(capability); 
            return this; 
        }
        
        public Builder sceneId(String sceneId) { 
            metadata.identity().setSceneId(sceneId); 
            return this; 
        }
        
        public Builder sceneGroupId(String sceneGroupId) { 
            metadata.identity().setSceneGroupId(sceneGroupId); 
            return this; 
        }
        
        public AgentMetadata build() {
            metadata.updateContext();
            return metadata;
        }
    }
    
    public static AgentMetadata of(String agentId) {
        return new AgentMetadata(agentId);
    }
    
    public static AgentMetadata of(String agentId, String agentName, String agentType) {
        AgentMetadata metadata = new AgentMetadata(agentId);
        metadata.identity().setAgentName(agentName);
        metadata.identity().setAgentType(agentType);
        return metadata;
    }
}

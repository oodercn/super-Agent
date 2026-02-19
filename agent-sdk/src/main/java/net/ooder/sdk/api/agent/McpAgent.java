
package net.ooder.sdk.api.agent;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import net.ooder.sdk.api.skill.SkillPackage;
import net.ooder.sdk.api.scene.SceneGroup;

public interface McpAgent {
    
    String getAgentId();
    
    String getAgentName();
    
    String getEndpoint();
    
    void start();
    
    void stop();
    
    boolean isHealthy();
    
    CompletableFuture<Void> register();
    
    CompletableFuture<Void> deregister();
    
    CompletableFuture<Void> heartbeat();
    
    CompletableFuture<List<RouteAgentInfo>> listRouteAgents();
    
    CompletableFuture<List<EndAgentInfo>> listEndAgents();
    
    CompletableFuture<RouteAgentInfo> getRouteAgent(String routeAgentId);
    
    CompletableFuture<EndAgentInfo> getEndAgent(String endAgentId);
    
    CompletableFuture<Void> registerRouteAgent(RouteAgentInfo routeAgent);
    
    CompletableFuture<Void> deregisterRouteAgent(String routeAgentId);
    
    CompletableFuture<Void> updateRouteTable(String routeAgentId, List<RouteEntry> routes);
    
    CompletableFuture<List<RouteEntry>> queryRouteTable(String routeAgentId);
    
    CompletableFuture<SkillPackage> deploySkill(String skillId, String targetAgentId);
    
    CompletableFuture<Void> invokeSkill(String skillId, Map<String, Object> params);
    
    CompletableFuture<SceneGroup> createSceneGroup(String sceneId, SceneGroupConfig config);
    
    CompletableFuture<Void> joinSceneGroup(String sceneGroupId);
    
    CompletableFuture<Void> leaveSceneGroup(String sceneGroupId);
    
    CompletableFuture<List<SceneGroup>> listSceneGroups();
    
    class RouteAgentInfo {
        private String agentId;
        private String agentName;
        private String endpoint;
        private Map<String, Object> capabilities;
        private List<String> skills;
        private long registerTime;
        private long lastHeartbeat;
        
        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }
        public String getAgentName() { return agentName; }
        public void setAgentName(String agentName) { this.agentName = agentName; }
        public String getEndpoint() { return endpoint; }
        public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
        public Map<String, Object> getCapabilities() { return capabilities; }
        public void setCapabilities(Map<String, Object> capabilities) { this.capabilities = capabilities; }
        public List<String> getSkills() { return skills; }
        public void setSkills(List<String> skills) { this.skills = skills; }
        public long getRegisterTime() { return registerTime; }
        public void setRegisterTime(long registerTime) { this.registerTime = registerTime; }
        public long getLastHeartbeat() { return lastHeartbeat; }
        public void setLastHeartbeat(long lastHeartbeat) { this.lastHeartbeat = lastHeartbeat; }
    }
    
    class EndAgentInfo {
        private String agentId;
        private String agentName;
        private String endpoint;
        private String routeAgentId;
        private List<String> skills;
        private long registerTime;
        private long lastHeartbeat;
        
        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }
        public String getAgentName() { return agentName; }
        public void setAgentName(String agentName) { this.agentName = agentName; }
        public String getEndpoint() { return endpoint; }
        public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
        public String getRouteAgentId() { return routeAgentId; }
        public void setRouteAgentId(String routeAgentId) { this.routeAgentId = routeAgentId; }
        public List<String> getSkills() { return skills; }
        public void setSkills(List<String> skills) { this.skills = skills; }
        public long getRegisterTime() { return registerTime; }
        public void setRegisterTime(long registerTime) { this.registerTime = registerTime; }
        public long getLastHeartbeat() { return lastHeartbeat; }
        public void setLastHeartbeat(long lastHeartbeat) { this.lastHeartbeat = lastHeartbeat; }
    }
    
    class RouteEntry {
        private String routeId;
        private String destination;
        private Map<String, Object> metadata;
        
        public String getRouteId() { return routeId; }
        public void setRouteId(String routeId) { this.routeId = routeId; }
        public String getDestination() { return destination; }
        public void setDestination(String destination) { this.destination = destination; }
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    }
    
    class SceneGroupConfig {
        private String sceneId;
        private int minMembers;
        private int maxMembers;
        private Map<String, Object> properties;
        
        public String getSceneId() { return sceneId; }
        public void setSceneId(String sceneId) { this.sceneId = sceneId; }
        public int getMinMembers() { return minMembers; }
        public void setMinMembers(int minMembers) { this.minMembers = minMembers; }
        public int getMaxMembers() { return maxMembers; }
        public void setMaxMembers(int maxMembers) { this.maxMembers = maxMembers; }
        public Map<String, Object> getProperties() { return properties; }
        public void setProperties(Map<String, Object> properties) { this.properties = properties; }
    }
}

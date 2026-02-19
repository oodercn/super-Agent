
package net.ooder.sdk.api.agent;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import net.ooder.sdk.api.skill.SkillPackage;

public interface RouteAgent {
    
    String getAgentId();
    
    String getAgentName();
    
    String getEndpoint();
    
    String getMcpAgentId();
    
    void start();
    
    void stop();
    
    boolean isHealthy();
    
    CompletableFuture<Void> register(String mcpAgentId);
    
    CompletableFuture<Void> deregister();
    
    CompletableFuture<Void> heartbeat();
    
    CompletableFuture<Void> registerEndAgent(EndAgentRegistration registration);
    
    CompletableFuture<Void> deregisterEndAgent(String endAgentId);
    
    CompletableFuture<List<EndAgentInfo>> listEndAgents();
    
    CompletableFuture<EndAgentInfo> getEndAgent(String endAgentId);
    
    CompletableFuture<Void> forwardTask(TaskPacket task, String endAgentId);
    
    CompletableFuture<TaskResult> receiveTaskResult(String taskId);
    
    CompletableFuture<Void> updateRouteToMcp(List<RouteUpdate> updates);
    
    CompletableFuture<Void> deploySkill(SkillPackage skillPackage, String endAgentId);
    
    CompletableFuture<Map<String, Object>> invokeSkill(String skillId, Map<String, Object> params, String endAgentId);
    
    class EndAgentRegistration {
        private String agentId;
        private String agentName;
        private String endpoint;
        private List<String> skills;
        private Map<String, Object> metadata;
        
        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }
        public String getAgentName() { return agentName; }
        public void setAgentName(String agentName) { this.agentName = agentName; }
        public String getEndpoint() { return endpoint; }
        public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
        public List<String> getSkills() { return skills; }
        public void setSkills(List<String> skills) { this.skills = skills; }
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    }
    
    class EndAgentInfo {
        private String agentId;
        private String agentName;
        private String endpoint;
        private List<String> skills;
        private String status;
        private long registerTime;
        private long lastHeartbeat;
        
        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }
        public String getAgentName() { return agentName; }
        public void setAgentName(String agentName) { this.agentName = agentName; }
        public String getEndpoint() { return endpoint; }
        public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
        public List<String> getSkills() { return skills; }
        public void setSkills(List<String> skills) { this.skills = skills; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public long getRegisterTime() { return registerTime; }
        public void setRegisterTime(long registerTime) { this.registerTime = registerTime; }
        public long getLastHeartbeat() { return lastHeartbeat; }
        public void setLastHeartbeat(long lastHeartbeat) { this.lastHeartbeat = lastHeartbeat; }
    }
    
    class TaskPacket {
        private String taskId;
        private String skillId;
        private Map<String, Object> params;
        private String senderId;
        private String receiverId;
        private long timestamp;
        
        public String getTaskId() { return taskId; }
        public void setTaskId(String taskId) { this.taskId = taskId; }
        public String getSkillId() { return skillId; }
        public void setSkillId(String skillId) { this.skillId = skillId; }
        public Map<String, Object> getParams() { return params; }
        public void setParams(Map<String, Object> params) { this.params = params; }
        public String getSenderId() { return senderId; }
        public void setSenderId(String senderId) { this.senderId = senderId; }
        public String getReceiverId() { return receiverId; }
        public void setReceiverId(String receiverId) { this.receiverId = receiverId; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }
    
    class TaskResult {
        private String taskId;
        private boolean success;
        private Map<String, Object> result;
        private String error;
        private long timestamp;
        
        public String getTaskId() { return taskId; }
        public void setTaskId(String taskId) { this.taskId = taskId; }
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public Map<String, Object> getResult() { return result; }
        public void setResult(Map<String, Object> result) { this.result = result; }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }
    
    class RouteUpdate {
        private String endAgentId;
        private String sourceId;
        private String destinationId;
        private String action;
        private Map<String, Object> metadata;
        
        public String getEndAgentId() { return endAgentId; }
        public void setEndAgentId(String endAgentId) { this.endAgentId = endAgentId; }
        public String getSourceId() { return sourceId; }
        public void setSourceId(String sourceId) { this.sourceId = sourceId; }
        public String getDestinationId() { return destinationId; }
        public void setDestinationId(String destinationId) { this.destinationId = destinationId; }
        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    }
}

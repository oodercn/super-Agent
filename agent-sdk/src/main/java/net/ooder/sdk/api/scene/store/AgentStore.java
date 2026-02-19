package net.ooder.sdk.api.scene.store;

import java.util.List;

public interface AgentStore {
    
    void saveAgent(AgentRegistration registration);
    
    AgentRegistration loadAgent(String agentId);
    
    void deleteAgent(String agentId);
    
    List<AgentRegistration> listAgents(String sceneId);
    
    List<AgentRegistration> listAgentsByGroup(String sceneId, String groupId);
    
    List<AgentRegistration> listAgentsByRole(String sceneId, String role);
    
    boolean agentExists(String agentId);
    
    void updateAgentHeartbeat(String agentId, long timestamp);
    
    Long getAgentLastHeartbeat(String agentId);
    
    void updateAgentStatus(String agentId, String status);
    
    void updateAgentRole(String agentId, String role);
}

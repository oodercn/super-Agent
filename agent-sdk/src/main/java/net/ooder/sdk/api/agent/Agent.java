
package net.ooder.sdk.api.agent;

import net.ooder.sdk.common.enums.AgentType;

public interface Agent {
    
    String getAgentId();
    
    String getAgentName();
    
    AgentType getAgentType();
    
    String getEndpoint();
    
    void start();
    
    void stop();
    
    boolean isHealthy();
    
    AgentState getState();
    
    enum AgentState {
        CREATED,
        INITIALIZING,
        INITIALIZED,
        STARTING,
        RUNNING,
        STOPPING,
        STOPPED,
        ERROR
    }
}

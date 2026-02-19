
package net.ooder.sdk.api.agent;

import net.ooder.sdk.common.enums.AgentType;
import net.ooder.sdk.infra.config.SDKConfiguration;

public interface AgentFactory {
    
    McpAgent createMcpAgent(SDKConfiguration config);
    
    RouteAgent createRouteAgent(SDKConfiguration config);
    
    EndAgent createEndAgent(SDKConfiguration config);
    
    Agent createAgent(AgentType type, SDKConfiguration config);
    
    void destroyAgent(String agentId);
    
    Agent getAgent(String agentId);
    
    boolean hasAgent(String agentId);
}

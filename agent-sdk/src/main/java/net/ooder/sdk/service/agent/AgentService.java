
package net.ooder.sdk.service.agent;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.api.agent.Agent;
import net.ooder.sdk.api.agent.AgentFactory;
import net.ooder.sdk.api.agent.EndAgent;
import net.ooder.sdk.api.agent.McpAgent;
import net.ooder.sdk.api.agent.RouteAgent;
import net.ooder.sdk.common.enums.AgentType;
import net.ooder.sdk.infra.config.SDKConfiguration;
import net.ooder.sdk.infra.exception.SDKException;

public class AgentService {
    
    private static final Logger log = LoggerFactory.getLogger(AgentService.class);
    
    private final AgentFactory agentFactory;
    private final SDKConfiguration configuration;
    
    public AgentService(AgentFactory agentFactory, SDKConfiguration configuration) {
        this.agentFactory = agentFactory;
        this.configuration = configuration;
    }
    
    public McpAgent createMcpAgent() {
        log.info("Creating MCP Agent");
        return agentFactory.createMcpAgent(configuration);
    }
    
    public RouteAgent createRouteAgent() {
        log.info("Creating Route Agent");
        return agentFactory.createRouteAgent(configuration);
    }
    
    public EndAgent createEndAgent() {
        log.info("Creating End Agent");
        return agentFactory.createEndAgent(configuration);
    }
    
    public Agent createAgent(AgentType type) {
        log.info("Creating Agent of type: {}", type);
        return agentFactory.createAgent(type, configuration);
    }
    
    public void destroyAgent(String agentId) {
        log.info("Destroying Agent: {}", agentId);
        agentFactory.destroyAgent(agentId);
    }
    
    public Agent getAgent(String agentId) {
        return agentFactory.getAgent(agentId);
    }
    
    public boolean hasAgent(String agentId) {
        return agentFactory.hasAgent(agentId);
    }
    
    public void startAgent(String agentId) {
        Agent agent = agentFactory.getAgent(agentId);
        if (agent == null) {
            throw new SDKException("AGENT_NOT_FOUND", "Agent not found: " + agentId);
        }
        agent.start();
        log.info("Agent started: {}", agentId);
    }
    
    public void stopAgent(String agentId) {
        Agent agent = agentFactory.getAgent(agentId);
        if (agent == null) {
            return;
        }
        agent.stop();
        log.info("Agent stopped: {}", agentId);
    }
    
    public boolean isAgentHealthy(String agentId) {
        Agent agent = agentFactory.getAgent(agentId);
        if (agent == null) {
            return false;
        }
        return agent.isHealthy();
    }
    
    public Agent.AgentState getAgentState(String agentId) {
        Agent agent = agentFactory.getAgent(agentId);
        if (agent == null) {
            return null;
        }
        return agent.getState();
    }
    
    public String getAgentEndpoint(String agentId) {
        Agent agent = agentFactory.getAgent(agentId);
        if (agent == null) {
            return null;
        }
        return agent.getEndpoint();
    }
    
    public AgentType getAgentType(String agentId) {
        Agent agent = agentFactory.getAgent(agentId);
        if (agent == null) {
            return null;
        }
        return agent.getAgentType();
    }
    
    public CompletableFuture<Void> heartbeat(String agentId) {
        Agent agent = agentFactory.getAgent(agentId);
        if (agent == null) {
            return CompletableFuture.completedFuture(null);
        }
        
        if (agent instanceof McpAgent) {
            return ((McpAgent) agent).heartbeat();
        } else if (agent instanceof RouteAgent) {
            return ((RouteAgent) agent).heartbeat();
        } else if (agent instanceof EndAgent) {
            return ((EndAgent) agent).heartbeat();
        }
        
        return CompletableFuture.completedFuture(null);
    }
}

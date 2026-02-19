
package net.ooder.sdk.core.agent.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.api.agent.Agent;
import net.ooder.sdk.api.agent.AgentFactory;
import net.ooder.sdk.api.agent.EndAgent;
import net.ooder.sdk.api.agent.McpAgent;
import net.ooder.sdk.api.agent.RouteAgent;
import net.ooder.sdk.common.enums.AgentType;
import net.ooder.sdk.core.agent.impl.EndAgentImpl;
import net.ooder.sdk.core.agent.impl.McpAgentImpl;
import net.ooder.sdk.core.agent.impl.RouteAgentImpl;
import net.ooder.sdk.core.agent.model.AgentConfig;
import net.ooder.sdk.infra.config.SDKConfiguration;

public class AgentFactoryImpl implements AgentFactory {
    
    private static final Logger log = LoggerFactory.getLogger(AgentFactoryImpl.class);
    
    private final Map<String, Agent> agents = new ConcurrentHashMap<>();
    
    @Override
    public McpAgent createMcpAgent(SDKConfiguration config) {
        AgentConfig agentConfig = convertConfig(config, AgentType.MCP);
        McpAgentImpl agent = new McpAgentImpl(agentConfig);
        agents.put(agent.getAgentId(), agent);
        log.info("Created MCP Agent: {}", agent.getAgentId());
        return agent;
    }
    
    @Override
    public RouteAgent createRouteAgent(SDKConfiguration config) {
        AgentConfig agentConfig = convertConfig(config, AgentType.ROUTE);
        RouteAgentImpl agent = new RouteAgentImpl(agentConfig);
        agents.put(agent.getAgentId(), agent);
        log.info("Created Route Agent: {}", agent.getAgentId());
        return agent;
    }
    
    @Override
    public EndAgent createEndAgent(SDKConfiguration config) {
        AgentConfig agentConfig = convertConfig(config, AgentType.END);
        EndAgentImpl agent = new EndAgentImpl(agentConfig);
        agents.put(agent.getAgentId(), agent);
        log.info("Created End Agent: {}", agent.getAgentId());
        return agent;
    }
    
    @Override
    public Agent createAgent(AgentType type, SDKConfiguration config) {
        switch (type) {
            case MCP:
                return (Agent) createMcpAgent(config);
            case ROUTE:
                return (Agent) createRouteAgent(config);
            case END:
                return (Agent) createEndAgent(config);
            default:
                throw new IllegalArgumentException("Unknown agent type: " + type);
        }
    }
    
    @Override
    public void destroyAgent(String agentId) {
        Agent agent = agents.remove(agentId);
        if (agent != null) {
            if (agent instanceof McpAgent) {
                ((McpAgent) agent).stop();
            } else if (agent instanceof RouteAgent) {
                ((RouteAgent) agent).stop();
            } else if (agent instanceof EndAgent) {
                ((EndAgent) agent).stop();
            }
            log.info("Destroyed Agent: {}", agentId);
        }
    }
    
    @Override
    public Agent getAgent(String agentId) {
        return agents.get(agentId);
    }
    
    @Override
    public boolean hasAgent(String agentId) {
        return agents.containsKey(agentId);
    }
    
    private AgentConfig convertConfig(SDKConfiguration sdkConfig, AgentType type) {
        AgentConfig.AgentConfigBuilder builder = AgentConfig.builder()
            .agentId(sdkConfig.getAgentId())
            .agentName(sdkConfig.getAgentName())
            .agentType(type)
            .endpoint(sdkConfig.getEndpoint())
            .udpPort(sdkConfig.getUdpPort())
            .heartbeatInterval(sdkConfig.getHeartbeatInterval())
            .heartbeatTimeout(sdkConfig.getHeartbeatTimeout())
            .strictMode(sdkConfig.isStrictMode());
        
        return builder.build();
    }
}

package net.ooder.sdk.agent.factory;

import net.ooder.sdk.agent.EndAgent;
import net.ooder.sdk.agent.RouteAgent;
import net.ooder.sdk.agent.McpAgent;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class AgentFactoryTest {
    
    private final String agentId = "test-agent-123";
    private final String agentName = "Test Agent";
    private final Map<String, Object> capabilities = new java.util.HashMap<>();
    
    public AgentFactoryTest() {
        capabilities.put("skill", "test");
        capabilities.put("version", "1.0.0");
    }
    
    @Test
    public void testCreateEndAgent() {
        EndAgent endAgent = AgentFactory.createEndAgent(agentId, agentName, capabilities);
        assertNotNull(endAgent);
        assertEquals(agentId, endAgent.getAgentId());
        assertEquals(agentName, endAgent.getAgentName());
        assertEquals("EndAgent", endAgent.getAgentType());
        assertEquals(capabilities, endAgent.getCapabilities());
    }
    
    @Test
    public void testCreateRouteAgent() {
        RouteAgent routeAgent = AgentFactory.createRouteAgent(agentId, agentName, capabilities);
        assertNotNull(routeAgent);
        assertEquals(agentId, routeAgent.getAgentId());
        assertEquals(agentName, routeAgent.getAgentName());
        assertEquals("RouteAgent", routeAgent.getAgentType());
        assertEquals(capabilities, routeAgent.getCapabilities());
    }
    
    @Test
    public void testCreateMcpAgent() {
        McpAgent mcpAgent = AgentFactory.createMcpAgent(agentId, agentName, capabilities);
        assertNotNull(mcpAgent);
        assertEquals(agentId, mcpAgent.getAgentId());
        assertEquals(agentName, mcpAgent.getAgentName());
        assertEquals("RouteAgent", mcpAgent.getAgentType()); // McpAgent 继承自 RouteAgent
        assertEquals(capabilities, mcpAgent.getCapabilities());
    }
    
    @Test
    public void testCreateAgent() {
        // 测试创建 EndAgent
        Object endAgent = AgentFactory.createAgent("endagent", agentId, agentName, capabilities);
        assertNotNull(endAgent);
        assertTrue(endAgent instanceof EndAgent);
        
        // 测试创建 RouteAgent
        Object routeAgent = AgentFactory.createAgent("routeagent", agentId, agentName, capabilities);
        assertNotNull(routeAgent);
        assertTrue(routeAgent instanceof RouteAgent);
        
        // 测试创建 McpAgent
        Object mcpAgent = AgentFactory.createAgent("mcpagent", agentId, agentName, capabilities);
        assertNotNull(mcpAgent);
        assertTrue(mcpAgent instanceof McpAgent);
    }
    
    @Test
    public void testCreateAgentInvalidType() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            AgentFactory.createAgent("invalid", agentId, agentName, capabilities);
        });
        assertEquals("Invalid agent type: invalid", exception.getMessage());
    }
}

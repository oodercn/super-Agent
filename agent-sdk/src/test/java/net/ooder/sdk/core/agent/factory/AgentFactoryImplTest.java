package net.ooder.sdk.core.agent.factory;

import net.ooder.sdk.api.agent.Agent;
import net.ooder.sdk.api.agent.McpAgent;
import net.ooder.sdk.api.agent.RouteAgent;
import net.ooder.sdk.api.agent.EndAgent;
import net.ooder.sdk.common.enums.AgentType;
import net.ooder.sdk.infra.config.SDKConfiguration;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

public class AgentFactoryImplTest {
    
    private AgentFactoryImpl factory;
    
    @Before
    public void setUp() {
        factory = new AgentFactoryImpl();
    }
    
    @After
    public void tearDown() {
    }
    
    private SDKConfiguration createTestConfig(String agentId, String agentName) {
        SDKConfiguration config = new SDKConfiguration();
        config.setAgentId(agentId);
        config.setAgentName(agentName);
        config.setEndpoint("http://localhost:8080");
        return config;
    }
    
    @Test
    public void testCreateMcpAgent() {
        SDKConfiguration config = createTestConfig("mcp-001", "Test MCP Agent");
        
        McpAgent agent = factory.createMcpAgent(config);
        
        assertNotNull(agent);
        assertEquals("mcp-001", agent.getAgentId());
    }
    
    @Test
    public void testCreateRouteAgent() {
        SDKConfiguration config = createTestConfig("route-001", "Test Route Agent");
        
        RouteAgent agent = factory.createRouteAgent(config);
        
        assertNotNull(agent);
        assertEquals("route-001", agent.getAgentId());
    }
    
    @Test
    public void testCreateEndAgent() {
        SDKConfiguration config = createTestConfig("end-001", "Test End Agent");
        
        EndAgent agent = factory.createEndAgent(config);
        
        assertNotNull(agent);
        assertEquals("end-001", agent.getAgentId());
    }
    
    @Test
    public void testCreateAgentMcp() {
        SDKConfiguration config = createTestConfig("mcp-002", "Test MCP Agent 2");
        
        Agent agent = factory.createAgent(AgentType.MCP, config);
        
        assertNotNull(agent);
        assertTrue(agent instanceof McpAgent);
    }
    
    @Test
    public void testCreateAgentRoute() {
        SDKConfiguration config = createTestConfig("route-002", "Test Route Agent 2");
        
        Agent agent = factory.createAgent(AgentType.ROUTE, config);
        
        assertNotNull(agent);
        assertTrue(agent instanceof RouteAgent);
    }
    
    @Test
    public void testCreateAgentEnd() {
        SDKConfiguration config = createTestConfig("end-002", "Test End Agent 2");
        
        Agent agent = factory.createAgent(AgentType.END, config);
        
        assertNotNull(agent);
        assertTrue(agent instanceof EndAgent);
    }
    
    @Test
    public void testGetAgent() {
        SDKConfiguration config = createTestConfig("mcp-003", "Test MCP Agent 3");
        factory.createMcpAgent(config);
        
        Agent retrieved = factory.getAgent("mcp-003");
        
        assertNotNull(retrieved);
    }
    
    @Test
    public void testGetAgentNotExists() {
        Agent agent = factory.getAgent("non-existent");
        
        assertNull(agent);
    }
    
    @Test
    public void testHasAgent() {
        SDKConfiguration config = createTestConfig("mcp-004", "Test MCP Agent 4");
        factory.createMcpAgent(config);
        
        assertTrue(factory.hasAgent("mcp-004"));
        assertFalse(factory.hasAgent("non-existent"));
    }
    
    @Test
    public void testDestroyAgent() {
        SDKConfiguration config = createTestConfig("mcp-005", "Test MCP Agent 5");
        factory.createMcpAgent(config);
        
        assertTrue(factory.hasAgent("mcp-005"));
        
        factory.destroyAgent("mcp-005");
        
        assertFalse(factory.hasAgent("mcp-005"));
    }
    
    @Test
    public void testDestroyAgentNotExists() {
        factory.destroyAgent("non-existent");
    }
}

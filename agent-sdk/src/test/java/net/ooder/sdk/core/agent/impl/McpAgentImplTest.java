package net.ooder.sdk.core.agent.impl;

import net.ooder.sdk.common.enums.AgentType;
import net.ooder.sdk.core.agent.model.AgentConfig;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

public class McpAgentImplTest {
    
    private McpAgentImpl mcpAgent;
    private AgentConfig config;
    
    @Before
    public void setUp() {
        config = new AgentConfig();
        config.setAgentId("test-mcp-001");
        config.setAgentName("Test MCP Agent");
        config.setEndpoint("http://localhost:8080");
        
        mcpAgent = new McpAgentImpl(config);
    }
    
    @After
    public void tearDown() {
        if (mcpAgent != null) {
            mcpAgent.stop();
        }
    }
    
    @Test
    public void testInitialization() {
        assertEquals("test-mcp-001", mcpAgent.getAgentId());
        assertEquals("Test MCP Agent", mcpAgent.getAgentName());
        assertEquals(AgentType.MCP, mcpAgent.getAgentType());
    }
    
    @Test
    public void testStart() {
        mcpAgent.start();
    }
    
    @Test
    public void testStop() {
        mcpAgent.start();
        mcpAgent.stop();
    }
}

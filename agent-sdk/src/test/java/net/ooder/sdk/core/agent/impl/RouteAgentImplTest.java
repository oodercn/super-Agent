package net.ooder.sdk.core.agent.impl;

import net.ooder.sdk.common.enums.AgentType;
import net.ooder.sdk.core.agent.model.AgentConfig;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

public class RouteAgentImplTest {
    
    private RouteAgentImpl routeAgent;
    private AgentConfig config;
    
    @Before
    public void setUp() {
        config = new AgentConfig();
        config.setAgentId("test-route-001");
        config.setAgentName("Test Route Agent");
        config.setEndpoint("http://localhost:8080");
        
        routeAgent = new RouteAgentImpl(config);
    }
    
    @After
    public void tearDown() {
        if (routeAgent != null) {
            routeAgent.stop();
        }
    }
    
    @Test
    public void testInitialization() {
        assertEquals("test-route-001", routeAgent.getAgentId());
        assertEquals("Test Route Agent", routeAgent.getAgentName());
        assertEquals(AgentType.ROUTE, routeAgent.getAgentType());
    }
    
    @Test
    public void testStart() {
        routeAgent.start();
    }
    
    @Test
    public void testStop() {
        routeAgent.start();
        routeAgent.stop();
    }
}

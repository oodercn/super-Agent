package net.ooder.sdk.southbound.protocol.impl;

import net.ooder.sdk.southbound.protocol.*;
import net.ooder.sdk.southbound.protocol.model.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

public class RoleProtocolImplTest {
    
    private RoleProtocolImpl roleProtocol;
    
    @Before
    public void setUp() {
        roleProtocol = new RoleProtocolImpl();
    }
    
    @After
    public void tearDown() {
        roleProtocol.shutdown();
    }
    
    @Test
    public void testDecideRole() throws Exception {
        RoleContext context = new RoleContext();
        context.setAgentId("agent-001");
        
        RoleDecision decision = roleProtocol.decideRole(context).get(10, TimeUnit.SECONDS);
        
        assertNotNull(decision);
        assertNotNull(decision.getDecidedRole());
    }
    
    @Test
    public void testRegisterRole() throws Exception {
        RoleRegistration registration = new RoleRegistration();
        registration.setAgentId("agent-001");
        registration.setRoleType(RoleType.MCP_AGENT);
        
        roleProtocol.registerRole(registration).get(10, TimeUnit.SECONDS);
        
        RoleInfo info = roleProtocol.getRoleInfo("agent-001").get(10, TimeUnit.SECONDS);
        
        assertNotNull(info);
        assertEquals("agent-001", info.getAgentId());
        assertEquals(RoleType.MCP_AGENT, info.getRoleType());
    }
    
    @Test
    public void testGetRoleInfo() throws Exception {
        RoleRegistration registration = new RoleRegistration();
        registration.setAgentId("agent-001");
        registration.setRoleType(RoleType.ROUTE_AGENT);
        
        roleProtocol.registerRole(registration).get(10, TimeUnit.SECONDS);
        
        RoleInfo info = roleProtocol.getRoleInfo("agent-001").get(10, TimeUnit.SECONDS);
        
        assertNotNull(info);
        assertEquals(RoleType.ROUTE_AGENT, info.getRoleType());
    }
    
    @Test
    public void testUnregisterRole() throws Exception {
        RoleRegistration registration = new RoleRegistration();
        registration.setAgentId("agent-001");
        registration.setRoleType(RoleType.END_AGENT);
        
        roleProtocol.registerRole(registration).get(10, TimeUnit.SECONDS);
        roleProtocol.unregisterRole("agent-001").get(10, TimeUnit.SECONDS);
        
        RoleInfo info = roleProtocol.getRoleInfo("agent-001").get(10, TimeUnit.SECONDS);
        assertNull(info);
    }
}

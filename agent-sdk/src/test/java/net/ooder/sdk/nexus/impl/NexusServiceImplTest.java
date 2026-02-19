package net.ooder.sdk.nexus.impl;

import net.ooder.sdk.nexus.*;
import net.ooder.sdk.nexus.model.*;
import net.ooder.sdk.southbound.protocol.model.LoginRequest;
import net.ooder.sdk.southbound.protocol.model.PeerInfo;
import net.ooder.sdk.southbound.protocol.model.RoleDecision;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

public class NexusServiceImplTest {
    
    private NexusService nexusService;
    
    @Before
    public void setUp() {
        nexusService = NexusServiceFactory.create();
    }
    
    @After
    public void tearDown() {
        nexusService.shutdown();
    }
    
    @Test
    public void testStart() throws Exception {
        NexusConfig config = new NexusConfig();
        config.setNodeId("nexus-001");
        config.setNodeName("Test Nexus");
        config.setAutoStart(true);
        
        NexusStatus status = nexusService.start(config).get(10, TimeUnit.SECONDS);
        
        assertNotNull(status);
        assertEquals("nexus-001", status.getNodeId());
        assertEquals("Test Nexus", status.getNodeName());
        assertEquals(NexusState.RUNNING, status.getState());
        assertTrue(status.isOnline());
    }
    
    @Test
    public void testStop() throws Exception {
        NexusConfig config = new NexusConfig();
        config.setNodeId("nexus-001");
        config.setNodeName("Test Nexus");
        
        nexusService.start(config).get(10, TimeUnit.SECONDS);
        nexusService.stop().get(10, TimeUnit.SECONDS);
        
        NexusStatus status = nexusService.getStatus().get(10, TimeUnit.SECONDS);
        assertEquals(NexusState.STOPPED, status.getState());
        assertFalse(status.isOnline());
    }
    
    @Test
    public void testLogin() throws Exception {
        NexusConfig config = new NexusConfig();
        config.setNodeId("nexus-001");
        config.setNodeName("Test Nexus");
        
        nexusService.start(config).get(10, TimeUnit.SECONDS);
        
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("testpass");
        
        nexusService.login(request).get(10, TimeUnit.SECONDS);
        
        UserSession session = nexusService.getCurrentSession().get(10, TimeUnit.SECONDS);
        assertNotNull(session);
        assertEquals("testuser", session.getUserName());
    }
    
    @Test
    public void testLogout() throws Exception {
        NexusConfig config = new NexusConfig();
        config.setNodeId("nexus-001");
        config.setNodeName("Test Nexus");
        
        nexusService.start(config).get(10, TimeUnit.SECONDS);
        
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("testpass");
        
        nexusService.login(request).get(10, TimeUnit.SECONDS);
        nexusService.logout().get(10, TimeUnit.SECONDS);
        
        UserSession session = nexusService.getCurrentSession().get(10, TimeUnit.SECONDS);
        assertNull(session);
    }
    
    @Test
    public void testDiscoverPeers() throws Exception {
        NexusConfig config = new NexusConfig();
        config.setNodeId("nexus-001");
        config.setNodeName("Test Nexus");
        
        nexusService.start(config).get(10, TimeUnit.SECONDS);
        
        java.util.List<PeerInfo> peers = nexusService.discoverPeers().get(10, TimeUnit.SECONDS);
        assertNotNull(peers);
    }
    
    @Test
    public void testGetCurrentRole() throws Exception {
        NexusConfig config = new NexusConfig();
        config.setNodeId("nexus-001");
        config.setNodeName("Test Nexus");
        
        nexusService.start(config).get(10, TimeUnit.SECONDS);
        
        RoleDecision role = nexusService.getCurrentRole().get(10, TimeUnit.SECONDS);
        assertNotNull(role);
        assertNotNull(role.getDecidedRole());
    }
}

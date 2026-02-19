package net.ooder.sdk.core.metadata.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class IdentityInfoTest {
    
    @Test
    public void testCreateIdentity() {
        IdentityInfo identity = new IdentityInfo();
        
        identity.setAgentId("agent-001");
        identity.setAgentName("TestAgent");
        identity.setAgentType("MCP");
        identity.setSceneId("scene-001");
        identity.setSkillId("skill-001");
        
        assertEquals("agent-001", identity.getAgentId());
        assertEquals("TestAgent", identity.getAgentName());
        assertEquals("MCP", identity.getAgentType());
        assertEquals("scene-001", identity.getSceneId());
        assertEquals("skill-001", identity.getSkillId());
    }
    
    @Test
    public void testSnapshot() {
        IdentityInfo identity = new IdentityInfo();
        identity.setAgentId("agent-001");
        identity.setAgentName("TestAgent");
        identity.setAgentType("END");
        identity.setSceneId("scene-001");
        
        IdentitySnapshot snapshot = identity.snapshot();
        
        assertEquals("agent-001", snapshot.getAgentId());
        assertEquals("TestAgent", snapshot.getAgentName());
        assertEquals("END", snapshot.getAgentType());
        assertEquals("scene-001", snapshot.getSceneId());
        assertTrue(snapshot.getSnapshotTime() > 0);
    }
    
    @Test
    public void testHasScene() {
        IdentityInfo identity = new IdentityInfo();
        
        assertFalse(identity.hasScene());
        
        identity.setSceneId("scene-001");
        assertTrue(identity.hasScene());
        
        identity.setSceneId("");
        assertFalse(identity.hasScene());
    }
    
    @Test
    public void testAttributes() {
        IdentityInfo identity = new IdentityInfo();
        
        identity.setAttribute("key1", "value1");
        identity.setAttribute("key2", "value2");
        
        assertEquals("value1", identity.getAttribute("key1"));
        assertEquals("value2", identity.getAttribute("key2"));
        assertNull(identity.getAttribute("nonexistent"));
    }
    
    @Test
    public void testFactoryMethods() {
        IdentityInfo identity = IdentityInfo.of("agent-001", "TestAgent", "ROUTE");
        
        assertEquals("agent-001", identity.getAgentId());
        assertEquals("TestAgent", identity.getAgentName());
        assertEquals("ROUTE", identity.getAgentType());
        
        IdentityInfo identity2 = IdentityInfo.ofAgentId("agent-002");
        assertEquals("agent-002", identity2.getAgentId());
    }
    
    @Test
    public void testDisplayName() {
        IdentityInfo identity = new IdentityInfo();
        
        assertEquals("unnamed", identity.getDisplayName());
        
        identity.setAgentName("NamedAgent");
        assertEquals("NamedAgent", identity.getDisplayName());
        
        identity.setAgentName(null);
        identity.setAgentId("agent-001");
        assertEquals("agent-001", identity.getDisplayName());
    }
}

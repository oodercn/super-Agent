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

    @Test
    public void testOwnerId() {
        IdentityInfo identity = new IdentityInfo();
        identity.setOwnerId("owner-001");

        assertEquals("owner-001", identity.getOwnerId());
    }

    @Test
    public void testOrganizationId() {
        IdentityInfo identity = new IdentityInfo();
        identity.setOrganizationId("org-001");

        assertEquals("org-001", identity.getOrganizationId());
    }

    @Test
    public void testSceneGroupId() {
        IdentityInfo identity = new IdentityInfo();
        identity.setSceneGroupId("group-001");

        assertEquals("group-001", identity.getSceneGroupId());
    }

    @Test
    public void testRole() {
        IdentityInfo identity = new IdentityInfo();
        identity.setRole("admin");

        assertEquals("admin", identity.getRole());
    }

    @Test
    public void testIsMcpAgent() {
        IdentityInfo identity = new IdentityInfo();

        identity.setAgentType("MCP");
        assertTrue(identity.isMcpAgent());

        identity.setAgentType("mcp");
        assertTrue(identity.isMcpAgent());

        identity.setAgentType("ROUTE");
        assertFalse(identity.isMcpAgent());
    }

    @Test
    public void testIsRouteAgent() {
        IdentityInfo identity = new IdentityInfo();

        identity.setAgentType("ROUTE");
        assertTrue(identity.isRouteAgent());

        identity.setAgentType("route");
        assertTrue(identity.isRouteAgent());

        identity.setAgentType("MCP");
        assertFalse(identity.isRouteAgent());
    }

    @Test
    public void testIsEndAgent() {
        IdentityInfo identity = new IdentityInfo();

        identity.setAgentType("END");
        assertTrue(identity.isEndAgent());

        identity.setAgentType("end");
        assertTrue(identity.isEndAgent());

        identity.setAgentType("MCP");
        assertFalse(identity.isEndAgent());
    }

    @Test
    public void testHasSceneGroup() {
        IdentityInfo identity = new IdentityInfo();

        assertFalse(identity.hasSceneGroup());

        identity.setSceneGroupId("group-001");
        assertTrue(identity.hasSceneGroup());

        identity.setSceneGroupId("");
        assertFalse(identity.hasSceneGroup());

        identity.setSceneGroupId(null);
        assertFalse(identity.hasSceneGroup());
    }

    @Test
    public void testHasSkill() {
        IdentityInfo identity = new IdentityInfo();

        assertFalse(identity.hasSkill());

        identity.setSkillId("skill-001");
        assertTrue(identity.hasSkill());

        identity.setSkillId("");
        assertFalse(identity.hasSkill());

        identity.setSkillId(null);
        assertFalse(identity.hasSkill());
    }

    @Test
    public void testGetIdentityKey() {
        IdentityInfo identity = new IdentityInfo();
        identity.setAgentId("agent-001");

        assertEquals("agent-001", identity.getIdentityKey());

        IdentityInfo emptyIdentity = new IdentityInfo();
        assertEquals("unknown", emptyIdentity.getIdentityKey());
    }
}

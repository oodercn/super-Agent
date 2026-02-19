package net.ooder.sdk.core.metadata.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class AgentMetadataTest {

    @Test
    public void testCreateMetadata() {
        AgentMetadata metadata = new AgentMetadata();

        assertNotNull(metadata.identity());
        assertNotNull(metadata.location());
        assertNotNull(metadata.resource());
        assertNotNull(metadata.history());
        assertNotNull(metadata.extendedInfo());
    }

    @Test
    public void testCreateMetadataWithAgentId() {
        AgentMetadata metadata = new AgentMetadata("agent-001");

        assertEquals("agent-001", metadata.getAgentId());
    }

    @Test
    public void testCreateMetadataWithComponents() {
        IdentityInfo identity = new IdentityInfo();
        identity.setAgentId("agent-001");

        LocationInfo location = new LocationInfo();
        location.setHost("192.168.1.1");

        ResourceInfo resource = new ResourceInfo();
        resource.setStatus("ACTIVE");

        AgentMetadata metadata = new AgentMetadata(identity, location, resource);

        assertEquals("agent-001", metadata.identity().getAgentId());
        assertEquals("192.168.1.1", metadata.location().getHost());
        assertEquals("ACTIVE", metadata.resource().getStatus());
    }

    @Test
    public void testCreateMetadataWithNullComponents() {
        AgentMetadata metadata = new AgentMetadata(null, null, null);

        assertNotNull(metadata.identity());
        assertNotNull(metadata.location());
        assertNotNull(metadata.resource());
    }

    @Test
    public void testGetAgentId() {
        AgentMetadata metadata = new AgentMetadata("agent-001");

        assertEquals("agent-001", metadata.getAgentId());
    }

    @Test
    public void testGetAgentName() {
        AgentMetadata metadata = new AgentMetadata("agent-001");
        metadata.identity().setAgentName("TestAgent");

        assertEquals("TestAgent", metadata.getAgentName());
    }

    @Test
    public void testGetAgentType() {
        AgentMetadata metadata = new AgentMetadata("agent-001");
        metadata.identity().setAgentType("MCP");

        assertEquals("MCP", metadata.getAgentType());
    }

    @Test
    public void testGetAddress() {
        AgentMetadata metadata = new AgentMetadata("agent-001");
        metadata.location().setHost("192.168.1.1");
        metadata.location().setPort(8080);

        assertEquals("192.168.1.1:8080", metadata.getAddress());
    }

    @Test
    public void testGetHost() {
        AgentMetadata metadata = new AgentMetadata("agent-001");
        metadata.location().setHost("192.168.1.1");

        assertEquals("192.168.1.1", metadata.getHost());
    }

    @Test
    public void testGetPort() {
        AgentMetadata metadata = new AgentMetadata("agent-001");
        metadata.location().setPort(8080);

        assertEquals(8080, metadata.getPort());
    }

    @Test
    public void testGetStatus() {
        AgentMetadata metadata = new AgentMetadata("agent-001");
        metadata.resource().setStatus("ACTIVE");

        assertEquals("ACTIVE", metadata.getStatus());
    }

    @Test
    public void testHasCapability() {
        AgentMetadata metadata = new AgentMetadata("agent-001");
        metadata.resource().addCapability("compute");

        assertTrue(metadata.hasCapability("compute"));
        assertFalse(metadata.hasCapability("storage"));
    }

    @Test
    public void testIsOnline() {
        AgentMetadata metadata = new AgentMetadata("agent-001");
        metadata.history().record("startup");

        assertTrue(metadata.isOnline());
    }

    @Test
    public void testGetUptime() {
        AgentMetadata metadata = new AgentMetadata("agent-001");

        assertTrue(metadata.getUptime() >= 0);
    }

    @Test
    public void testRecordEvent() {
        AgentMetadata metadata = new AgentMetadata("agent-001");

        metadata.recordEvent("test_event");

        assertEquals(1, metadata.history().getEventCount());
    }

    @Test
    public void testRecordEventWithDetail() {
        AgentMetadata metadata = new AgentMetadata("agent-001");

        metadata.recordEvent("test_event", "detail_info");

        assertEquals(1, metadata.history().getEventCount());
    }

    @Test
    public void testUpdateContext() {
        AgentMetadata metadata = new AgentMetadata("agent-001");
        metadata.identity().setAgentName("TestAgent");
        metadata.location().setHost("192.168.1.1");

        metadata.updateContext();

        Context context = metadata.getCurrentContext();
        assertNotNull(context);
    }

    @Test
    public void testGetCurrentContext() {
        AgentMetadata metadata = new AgentMetadata("agent-001");
        metadata.updateContext();

        Context context = metadata.getCurrentContext();
        assertNotNull(context);
    }

    @Test
    public void testGetContextAt() {
        AgentMetadata metadata = new AgentMetadata("agent-001");
        metadata.recordEvent("event1");

        Context context = metadata.getContextAt(System.currentTimeMillis());
        assertNotNull(context);
    }

    @Test
    public void testGetSceneId() {
        AgentMetadata metadata = new AgentMetadata("agent-001");
        metadata.identity().setSceneId("scene-001");

        assertEquals("scene-001", metadata.getSceneId());
    }

    @Test
    public void testGetSceneGroupId() {
        AgentMetadata metadata = new AgentMetadata("agent-001");
        metadata.identity().setSceneGroupId("group-001");

        assertEquals("group-001", metadata.getSceneGroupId());
    }

    @Test
    public void testGetSkillId() {
        AgentMetadata metadata = new AgentMetadata("agent-001");
        metadata.identity().setSkillId("skill-001");

        assertEquals("skill-001", metadata.getSkillId());
    }

    @Test
    public void testIsInScene() {
        AgentMetadata metadata = new AgentMetadata("agent-001");

        assertFalse(metadata.isInScene());

        metadata.identity().setSceneId("scene-001");
        assertTrue(metadata.isInScene());
    }

    @Test
    public void testIsInSceneGroup() {
        AgentMetadata metadata = new AgentMetadata("agent-001");

        assertFalse(metadata.isInSceneGroup());

        metadata.identity().setSceneGroupId("group-001");
        assertTrue(metadata.isInSceneGroup());
    }

    @Test
    public void testHasSkill() {
        AgentMetadata metadata = new AgentMetadata("agent-001");

        assertFalse(metadata.hasSkill());

        metadata.identity().setSkillId("skill-001");
        assertTrue(metadata.hasSkill());
    }

    @Test
    public void testGetSummary() {
        AgentMetadata metadata = new AgentMetadata("agent-001");
        metadata.identity().setAgentName("TestAgent");
        metadata.identity().setAgentType("MCP");
        metadata.location().setHost("192.168.1.1");
        metadata.resource().setStatus("ACTIVE");

        String summary = metadata.getSummary();
        assertNotNull(summary);
        assertTrue(summary.contains("agent-001"));
        assertTrue(summary.contains("TestAgent"));
        assertTrue(summary.contains("MCP"));
    }

    @Test
    public void testGetFullReport() {
        AgentMetadata metadata = new AgentMetadata("agent-001");
        metadata.identity().setAgentName("TestAgent");
        metadata.identity().setAgentType("MCP");
        metadata.location().setHost("192.168.1.1");
        metadata.resource().setStatus("ACTIVE");

        String report = metadata.getFullReport();
        assertNotNull(report);
        assertTrue(report.contains("agent-001"));
        assertTrue(report.contains("TestAgent"));
    }

    @Test
    public void testStartInstance() {
        AgentMetadata metadata = new AgentMetadata("agent-001");

        metadata.startInstance("group-001", "scene-001");

        assertNotNull(metadata.currentInstance());
        assertEquals("group-001", metadata.currentInstance().getSceneGroupId());
        assertEquals("scene-001", metadata.currentInstance().getSceneId());
        assertTrue(metadata.currentInstance().isRunning());
    }

    @Test
    public void testCompleteInstanceSuccess() {
        AgentMetadata metadata = new AgentMetadata("agent-001");
        metadata.startInstance("group-001", "scene-001");

        metadata.completeInstance(true, "Completed successfully");

        assertTrue(metadata.currentInstance().isCompleted());
        assertTrue(metadata.currentInstance().isSuccessful());
    }

    @Test
    public void testCompleteInstanceFailure() {
        AgentMetadata metadata = new AgentMetadata("agent-001");
        metadata.startInstance("group-001", "scene-001");

        metadata.completeInstance(false, "Failed with error");

        assertTrue(metadata.currentInstance().isCompleted());
        assertFalse(metadata.currentInstance().isSuccessful());
    }

    @Test
    public void testTerminateInstance() {
        AgentMetadata metadata = new AgentMetadata("agent-001");
        metadata.startInstance("group-001", "scene-001");

        metadata.terminateInstance("User requested");

        assertTrue(metadata.currentInstance().isCompleted());
        assertFalse(metadata.currentInstance().isSuccessful());
    }

    @Test
    public void testSetCurrentInstance() {
        AgentMetadata metadata = new AgentMetadata("agent-001");
        SceneGroupInstance instance = new SceneGroupInstance("group-001", "scene-001");

        metadata.setCurrentInstance(instance);

        assertEquals(instance, metadata.currentInstance());
    }

    @Test
    public void testExtendedInfo() {
        AgentMetadata metadata = new AgentMetadata("agent-001");

        metadata.extendedInfo().put("customKey", "customValue");

        assertEquals("customValue", metadata.extendedInfo().get("customKey"));
    }

    @Test
    public void testBuilder() {
        AgentMetadata metadata = AgentMetadata.builder()
            .agentId("agent-001")
            .agentName("TestAgent")
            .agentType("MCP")
            .host("192.168.1.1")
            .port(8080)
            .region("us-east")
            .status("ACTIVE")
            .capability("compute")
            .sceneId("scene-001")
            .sceneGroupId("group-001")
            .build();

        assertEquals("agent-001", metadata.getAgentId());
        assertEquals("TestAgent", metadata.getAgentName());
        assertEquals("MCP", metadata.getAgentType());
        assertEquals("192.168.1.1", metadata.getHost());
        assertEquals(8080, metadata.getPort());
        assertEquals("us-east", metadata.location().getRegion());
        assertEquals("ACTIVE", metadata.getStatus());
        assertTrue(metadata.hasCapability("compute"));
        assertEquals("scene-001", metadata.getSceneId());
        assertEquals("group-001", metadata.getSceneGroupId());
    }

    @Test
    public void testFactoryOfOne() {
        AgentMetadata metadata = AgentMetadata.of("agent-001");

        assertEquals("agent-001", metadata.getAgentId());
    }

    @Test
    public void testFactoryOfThree() {
        AgentMetadata metadata = AgentMetadata.of("agent-001", "TestAgent", "MCP");

        assertEquals("agent-001", metadata.getAgentId());
        assertEquals("TestAgent", metadata.getAgentName());
        assertEquals("MCP", metadata.getAgentType());
    }
}

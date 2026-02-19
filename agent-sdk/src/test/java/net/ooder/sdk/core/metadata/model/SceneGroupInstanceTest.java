package net.ooder.sdk.core.metadata.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class SceneGroupInstanceTest {

    @Test
    public void testCreateInstance() {
        SceneGroupInstance instance = new SceneGroupInstance();

        assertEquals(SceneGroupInstance.InstanceState.INITIALIZING, instance.getState());
        assertNotNull(instance.getRecoveryPoints());
        assertTrue(instance.getRecoveryPoints().isEmpty());
        assertTrue(instance.getCreateTime() > 0);
    }

    @Test
    public void testCreateInstanceWithIds() {
        SceneGroupInstance instance = new SceneGroupInstance("group-001", "scene-001");

        assertEquals("group-001", instance.getSceneGroupId());
        assertEquals("scene-001", instance.getSceneId());
        assertEquals(SceneGroupInstance.InstanceState.INITIALIZING, instance.getState());
    }

    @Test
    public void testSetSceneGroupId() {
        SceneGroupInstance instance = new SceneGroupInstance();
        instance.setSceneGroupId("group-001");

        assertEquals("group-001", instance.getSceneGroupId());
    }

    @Test
    public void testSetSceneId() {
        SceneGroupInstance instance = new SceneGroupInstance();
        instance.setSceneId("scene-001");

        assertEquals("scene-001", instance.getSceneId());
    }

    @Test
    public void testSetState() {
        SceneGroupInstance instance = new SceneGroupInstance();
        instance.setState(SceneGroupInstance.InstanceState.RUNNING);

        assertEquals(SceneGroupInstance.InstanceState.RUNNING, instance.getState());
    }

    @Test
    public void testSetCreateTime() {
        SceneGroupInstance instance = new SceneGroupInstance();
        long time = System.currentTimeMillis();
        instance.setCreateTime(time);

        assertEquals(time, instance.getCreateTime());
    }

    @Test
    public void testSetStartTime() {
        SceneGroupInstance instance = new SceneGroupInstance();
        long time = System.currentTimeMillis();
        instance.setStartTime(time);

        assertEquals(time, instance.getStartTime());
    }

    @Test
    public void testSetEndTime() {
        SceneGroupInstance instance = new SceneGroupInstance();
        long time = System.currentTimeMillis();
        instance.setEndTime(time);

        assertEquals(time, instance.getEndTime());
    }

    @Test
    public void testIsRunning() {
        SceneGroupInstance instance = new SceneGroupInstance();

        assertTrue(instance.isRunning());

        instance.setState(SceneGroupInstance.InstanceState.RUNNING);
        assertTrue(instance.isRunning());

        instance.setState(SceneGroupInstance.InstanceState.COMPLETED);
        assertFalse(instance.isRunning());
    }

    @Test
    public void testIsCompleted() {
        SceneGroupInstance instance = new SceneGroupInstance();

        assertFalse(instance.isCompleted());

        instance.setState(SceneGroupInstance.InstanceState.COMPLETED);
        assertTrue(instance.isCompleted());

        instance.setState(SceneGroupInstance.InstanceState.FAILED);
        assertTrue(instance.isCompleted());

        instance.setState(SceneGroupInstance.InstanceState.TERMINATED);
        assertTrue(instance.isCompleted());
    }

    @Test
    public void testIsSuccessful() {
        SceneGroupInstance instance = new SceneGroupInstance();
        instance.setState(SceneGroupInstance.InstanceState.COMPLETED);

        assertTrue(instance.isSuccessful());

        instance.complete(false, "Failed");
        assertFalse(instance.isSuccessful());
    }

    @Test
    public void testStart() {
        SceneGroupInstance instance = new SceneGroupInstance();
        instance.start();

        assertEquals(SceneGroupInstance.InstanceState.RUNNING, instance.getState());
        assertTrue(instance.getStartTime() > 0);
    }

    @Test
    public void testCompleteSuccess() {
        SceneGroupInstance instance = new SceneGroupInstance();
        instance.start();
        instance.complete(true, "Success");

        assertEquals(SceneGroupInstance.InstanceState.COMPLETED, instance.getState());
        assertTrue(instance.getEndTime() > 0);
        assertNotNull(instance.getResult());
        assertTrue(instance.getResult().isSuccess());
        assertEquals("Success", instance.getResult().getMessage());
    }

    @Test
    public void testCompleteFailure() {
        SceneGroupInstance instance = new SceneGroupInstance();
        instance.start();
        instance.complete(false, "Error occurred");

        assertEquals(SceneGroupInstance.InstanceState.FAILED, instance.getState());
        assertNotNull(instance.getResult());
        assertFalse(instance.getResult().isSuccess());
        assertEquals("Error occurred", instance.getResult().getMessage());
    }

    @Test
    public void testTerminate() {
        SceneGroupInstance instance = new SceneGroupInstance();
        instance.start();
        instance.terminate("User cancelled");

        assertEquals(SceneGroupInstance.InstanceState.TERMINATED, instance.getState());
        assertNotNull(instance.getResult());
        assertFalse(instance.getResult().isSuccess());
        assertEquals("User cancelled", instance.getResult().getMessage());
    }

    @Test
    public void testCreateRecoveryPoint() {
        SceneGroupInstance instance = new SceneGroupInstance();
        instance.start();
        instance.createRecoveryPoint("checkpoint1");

        assertEquals(1, instance.getRecoveryPoints().size());
        assertEquals("checkpoint1", instance.getRecoveryPoints().get(0).getName());
    }

    @Test
    public void testCreateRecoveryPointNotRunning() {
        SceneGroupInstance instance = new SceneGroupInstance();
        instance.setState(SceneGroupInstance.InstanceState.COMPLETED);
        instance.createRecoveryPoint("checkpoint1");

        assertTrue(instance.getRecoveryPoints().isEmpty());
    }

    @Test
    public void testGetLatestRecoveryPoint() {
        SceneGroupInstance instance = new SceneGroupInstance();
        instance.start();
        instance.createRecoveryPoint("checkpoint1");
        instance.createRecoveryPoint("checkpoint2");

        SceneGroupInstance.RecoveryPoint latest = instance.getLatestRecoveryPoint();
        assertNotNull(latest);
        assertEquals("checkpoint2", latest.getName());
    }

    @Test
    public void testGetLatestRecoveryPointEmpty() {
        SceneGroupInstance instance = new SceneGroupInstance();

        assertNull(instance.getLatestRecoveryPoint());
    }

    @Test
    public void testGetDuration() {
        SceneGroupInstance instance = new SceneGroupInstance();
        instance.start();

        assertTrue(instance.getDuration() >= 0);
    }

    @Test
    public void testGetDurationNotStarted() {
        SceneGroupInstance instance = new SceneGroupInstance();

        assertEquals(0, instance.getDuration());
    }

    @Test
    public void testGetDurationCompleted() {
        SceneGroupInstance instance = new SceneGroupInstance();
        instance.start();
        instance.complete(true, "Done");

        assertTrue(instance.getDuration() >= 0);
    }

    @Test
    public void testGetDurationFormatted() {
        SceneGroupInstance instance = new SceneGroupInstance();
        instance.start();

        String formatted = instance.getDurationFormatted();
        assertNotNull(formatted);
    }

    @Test
    public void testSetResult() {
        SceneGroupInstance instance = new SceneGroupInstance();
        SceneGroupInstance.InstanceResult result = new SceneGroupInstance.InstanceResult();
        result.setSuccess(true);
        result.setMessage("Test result");

        instance.setResult(result);

        assertEquals(result, instance.getResult());
        assertTrue(instance.getResult().isSuccess());
    }

    @Test
    public void testSetContext() {
        SceneGroupInstance instance = new SceneGroupInstance();
        SceneGroupInstance.InstanceContext context = new SceneGroupInstance.InstanceContext();
        context.setPrimaryAgentId("agent-001");

        instance.setContext(context);

        assertEquals(context, instance.getContext());
        assertEquals("agent-001", instance.getContext().getPrimaryAgentId());
    }

    @Test
    public void testSetRecoveryPoints() {
        SceneGroupInstance instance = new SceneGroupInstance();
        java.util.List<SceneGroupInstance.RecoveryPoint> points = new java.util.ArrayList<>();
        SceneGroupInstance.RecoveryPoint point = new SceneGroupInstance.RecoveryPoint();
        point.setName("checkpoint1");
        points.add(point);

        instance.setRecoveryPoints(points);

        assertEquals(1, instance.getRecoveryPoints().size());
    }

    @Test
    public void testInstanceResult() {
        SceneGroupInstance.InstanceResult result = new SceneGroupInstance.InstanceResult();

        result.setSuccess(true);
        result.setMessage("Test message");
        result.setErrorCode("ERR001");
        result.setDuration(1000L);
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("key", "value");
        result.setData(data);

        assertTrue(result.isSuccess());
        assertEquals("Test message", result.getMessage());
        assertEquals("ERR001", result.getErrorCode());
        assertEquals(1000L, result.getDuration());
        assertEquals("value", result.getData().get("key"));
    }

    @Test
    public void testInstanceContext() {
        SceneGroupInstance.InstanceContext context = new SceneGroupInstance.InstanceContext();

        context.setPrimaryAgentId("agent-001");
        context.addMember("agent-002");
        context.addMember("agent-003");
        context.setAgentRole("agent-002", "worker");
        context.getConfiguration().put("key", "value");

        assertEquals("agent-001", context.getPrimaryAgentId());
        assertEquals(2, context.getMemberAgentIds().size());
        assertEquals("worker", context.getAgentRole("agent-002"));
        assertEquals("value", context.getConfiguration().get("key"));
    }

    @Test
    public void testInstanceContextRemoveMember() {
        SceneGroupInstance.InstanceContext context = new SceneGroupInstance.InstanceContext();
        context.addMember("agent-001");
        context.addMember("agent-002");

        assertEquals(2, context.getMemberAgentIds().size());

        context.removeMember("agent-001");
        assertEquals(1, context.getMemberAgentIds().size());
    }

    @Test
    public void testInstanceContextRecordEvent() {
        SceneGroupInstance.InstanceContext context = new SceneGroupInstance.InstanceContext();

        context.recordEvent("event1");

        assertEquals(1, context.getExecutionTimeline().getEventCount());
    }

    @Test
    public void testInstanceContextSnapshot() {
        SceneGroupInstance.InstanceContext context = new SceneGroupInstance.InstanceContext();
        context.setPrimaryAgentId("agent-001");
        context.addMember("agent-002");

        SceneGroupInstance.InstanceContextSnapshot snapshot = context.snapshot();

        assertEquals("agent-001", snapshot.getPrimaryAgentId());
        assertEquals(1, snapshot.getMemberAgentIds().size());
        assertTrue(snapshot.getSnapshotTime() > 0);
    }

    @Test
    public void testRecoveryPoint() {
        SceneGroupInstance.RecoveryPoint point = new SceneGroupInstance.RecoveryPoint();

        point.setName("checkpoint1");
        point.setTimestamp(System.currentTimeMillis());

        assertEquals("checkpoint1", point.getName());
        assertTrue(point.getTimestamp() > 0);
    }

    @Test
    public void testCompleteClearsContext() {
        SceneGroupInstance instance = new SceneGroupInstance();
        instance.start();
        instance.setContext(new SceneGroupInstance.InstanceContext());
        instance.createRecoveryPoint("checkpoint1");

        instance.complete(true, "Done");

        assertNull(instance.getContext());
        assertTrue(instance.getRecoveryPoints().isEmpty());
    }
}

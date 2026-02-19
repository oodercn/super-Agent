package net.ooder.sdk.core.metadata.model;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.HashSet;

public class ResourceInfoTest {

    @Test
    public void testCreateResource() {
        ResourceInfo resource = new ResourceInfo();

        resource.setResourceType("compute");
        resource.setResourceId("res-001");
        resource.setResourceName("ComputeNode1");
        resource.setDescription("Main compute node");
        resource.setVersion("1.0.0");
        resource.setStatus("ACTIVE");

        assertEquals("compute", resource.getResourceType());
        assertEquals("res-001", resource.getResourceId());
        assertEquals("ComputeNode1", resource.getResourceName());
        assertEquals("Main compute node", resource.getDescription());
        assertEquals("1.0.0", resource.getVersion());
        assertEquals("ACTIVE", resource.getStatus());
    }

    @Test
    public void testDefaultStatus() {
        ResourceInfo resource = new ResourceInfo();
        assertEquals("INITIALIZED", resource.getStatus());
    }

    @Test
    public void testCapabilities() {
        ResourceInfo resource = new ResourceInfo();

        resource.addCapability("compute");
        resource.addCapability("storage");
        resource.addCapability("compute");

        assertEquals(2, resource.getCapabilities().size());
        assertTrue(resource.hasCapability("compute"));
        assertTrue(resource.hasCapability("storage"));
        assertFalse(resource.hasCapability("network"));
    }

    @Test
    public void testSetCapabilities() {
        ResourceInfo resource = new ResourceInfo();
        resource.setCapabilities(Arrays.asList("cap1", "cap2", "cap3"));

        assertEquals(3, resource.getCapabilities().size());
        assertTrue(resource.hasCapability("cap1"));
    }

    @Test
    public void testDependencies() {
        ResourceInfo resource = new ResourceInfo();

        resource.addDependency("dep1");
        resource.addDependency("dep2");
        resource.addDependency("dep1");

        assertEquals(2, resource.getDependencies().size());
        assertTrue(resource.getDependencies().contains("dep1"));
        assertTrue(resource.getDependencies().contains("dep2"));
    }

    @Test
    public void testTags() {
        ResourceInfo resource = new ResourceInfo();

        resource.addTag("production");
        resource.addTag("critical");
        resource.addTag("production");

        assertEquals(2, resource.getTags().size());
        assertTrue(resource.getTags().contains("production"));
        assertTrue(resource.getTags().contains("critical"));
    }

    @Test
    public void testSetTags() {
        ResourceInfo resource = new ResourceInfo();
        resource.setTags(new HashSet<>(Arrays.asList("tag1", "tag2")));

        assertEquals(2, resource.getTags().size());
    }

    @Test
    public void testProperties() {
        ResourceInfo resource = new ResourceInfo();

        resource.setProperty("key1", "value1");
        resource.setProperty("key2", 123);
        resource.setProperty("key3", true);

        assertEquals("value1", resource.getProperty("key1"));
        assertEquals(123, resource.getProperty("key2"));
        assertEquals(true, resource.getProperty("key3"));
        assertNull(resource.getProperty("nonexistent"));
    }

    @Test
    public void testGetPropertyAsString() {
        ResourceInfo resource = new ResourceInfo();
        resource.setProperty("strKey", "stringValue");
        resource.setProperty("numKey", 123);

        assertEquals("stringValue", resource.getPropertyAsString("strKey"));
        assertEquals("123", resource.getPropertyAsString("numKey"));
        assertNull(resource.getPropertyAsString("nonexistent"));
    }

    @Test
    public void testGetPropertyAsInt() {
        ResourceInfo resource = new ResourceInfo();
        resource.setProperty("intKey", 42);
        resource.setProperty("longKey", 100L);
        resource.setProperty("strKey", "not a number");

        assertEquals(42, resource.getPropertyAsInt("intKey", 0));
        assertEquals(100, resource.getPropertyAsInt("longKey", 0));
        assertEquals(0, resource.getPropertyAsInt("strKey", 0));
        assertEquals(99, resource.getPropertyAsInt("nonexistent", 99));
    }

    @Test
    public void testGetPropertyAsLong() {
        ResourceInfo resource = new ResourceInfo();
        resource.setProperty("longKey", 1000L);
        resource.setProperty("intKey", 500);

        assertEquals(1000L, resource.getPropertyAsLong("longKey", 0));
        assertEquals(500L, resource.getPropertyAsLong("intKey", 0));
        assertEquals(999L, resource.getPropertyAsLong("nonexistent", 999L));
    }

    @Test
    public void testGetPropertyAsBoolean() {
        ResourceInfo resource = new ResourceInfo();
        resource.setProperty("trueKey", true);
        resource.setProperty("falseKey", false);
        resource.setProperty("strKey", "not boolean");

        assertTrue(resource.getPropertyAsBoolean("trueKey", false));
        assertFalse(resource.getPropertyAsBoolean("falseKey", true));
        assertTrue(resource.getPropertyAsBoolean("strKey", true));
        assertTrue(resource.getPropertyAsBoolean("nonexistent", true));
    }

    @Test
    public void testGetPropertyWithDefault() {
        ResourceInfo resource = new ResourceInfo();
        resource.setProperty("key1", "value1");

        assertEquals("value1", resource.getProperty("key1", "default"));
        assertEquals("default", resource.getProperty("nonexistent", "default"));
    }

    @Test
    public void testMetrics() {
        ResourceInfo resource = new ResourceInfo();

        resource.setMetric("cpu_usage", 75.5);
        resource.setMetric("memory_usage", 1024L);

        assertEquals(75.5, resource.getMetric("cpu_usage"));
        assertEquals(1024L, resource.getMetric("memory_usage"));
        assertNull(resource.getMetric("nonexistent"));
    }

    @Test
    public void testGetMetricAsDouble() {
        ResourceInfo resource = new ResourceInfo();
        resource.setMetric("cpu", 85.5);
        resource.setMetric("count", 100);

        assertEquals(85.5, resource.getMetricAsDouble("cpu", 0.0), 0.001);
        assertEquals(100.0, resource.getMetricAsDouble("count", 0.0), 0.001);
        assertEquals(50.0, resource.getMetricAsDouble("nonexistent", 50.0), 0.001);
    }

    @Test
    public void testIsActive() {
        ResourceInfo resource = new ResourceInfo();

        resource.setStatus("ACTIVE");
        assertTrue(resource.isActive());

        resource.setStatus("RUNNING");
        assertTrue(resource.isActive());

        resource.setStatus("active");
        assertTrue(resource.isActive());

        resource.setStatus("IDLE");
        assertFalse(resource.isActive());
    }

    @Test
    public void testIsIdle() {
        ResourceInfo resource = new ResourceInfo();

        resource.setStatus("IDLE");
        assertTrue(resource.isIdle());

        resource.setStatus("INITIALIZED");
        assertTrue(resource.isIdle());

        resource.setStatus("ACTIVE");
        assertFalse(resource.isIdle());
    }

    @Test
    public void testIsError() {
        ResourceInfo resource = new ResourceInfo();

        resource.setStatus("ERROR");
        assertTrue(resource.isError());

        resource.setStatus("FAILED");
        assertTrue(resource.isError());

        resource.setStatus("ACTIVE");
        assertFalse(resource.isError());
    }

    @Test
    public void testIsBusy() {
        ResourceInfo resource = new ResourceInfo();

        resource.setStatus("BUSY");
        assertTrue(resource.isBusy());

        resource.setStatus("ACTIVE");
        assertFalse(resource.isBusy());
    }

    @Test
    public void testMarkMethods() {
        ResourceInfo resource = new ResourceInfo();

        resource.markActive();
        assertEquals("ACTIVE", resource.getStatus());
        assertTrue(resource.isActive());

        resource.markIdle();
        assertEquals("IDLE", resource.getStatus());
        assertTrue(resource.isIdle());

        resource.markError();
        assertEquals("ERROR", resource.getStatus());
        assertTrue(resource.isError());

        resource.markBusy();
        assertEquals("BUSY", resource.getStatus());
        assertTrue(resource.isBusy());
    }

    @Test
    public void testGetResourceKey() {
        ResourceInfo resource = new ResourceInfo();
        resource.setResourceId("res-001");

        assertEquals("res-001", resource.getResourceKey());

        ResourceInfo emptyResource = new ResourceInfo();
        assertEquals("unknown", emptyResource.getResourceKey());
    }

    @Test
    public void testSnapshot() {
        ResourceInfo resource = new ResourceInfo();
        resource.setResourceId("res-001");
        resource.setResourceType("compute");
        resource.setStatus("ACTIVE");
        resource.addCapability("cap1");

        ResourceSnapshot snapshot = resource.snapshot();

        assertNotNull(snapshot);
        assertEquals("res-001", snapshot.getResourceId());
        assertEquals("compute", snapshot.getResourceType());
        assertEquals("ACTIVE", snapshot.getStatus());
    }

    @Test
    public void testFactoryOfTwo() {
        ResourceInfo resource = ResourceInfo.of("compute", "res-001");

        assertEquals("compute", resource.getResourceType());
        assertEquals("res-001", resource.getResourceId());
        assertEquals("INITIALIZED", resource.getStatus());
    }

    @Test
    public void testFactoryOfThree() {
        ResourceInfo resource = ResourceInfo.of("compute", "res-001", "ACTIVE");

        assertEquals("compute", resource.getResourceType());
        assertEquals("res-001", resource.getResourceId());
        assertEquals("ACTIVE", resource.getStatus());
    }

    @Test
    public void testAction() {
        ResourceInfo resource = new ResourceInfo();
        resource.setAction("START");

        assertEquals("START", resource.getAction());
    }
}

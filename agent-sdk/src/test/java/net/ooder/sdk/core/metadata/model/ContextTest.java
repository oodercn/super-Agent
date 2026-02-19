package net.ooder.sdk.core.metadata.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class ContextTest {

    @Test
    public void testCreateContextWithSnapshots() {
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setAgentId("agent-001");
        identityInfo.setAgentName("TestAgent");
        IdentitySnapshot identity = identityInfo.snapshot();

        LocationInfo locationInfo = new LocationInfo();
        locationInfo.setHost("192.168.1.1");
        locationInfo.setPort(8080);
        LocationSnapshot location = locationInfo.snapshot();

        ResourceInfo resourceInfo = new ResourceInfo();
        resourceInfo.setStatus("ACTIVE");
        ResourceSnapshot resource = resourceInfo.snapshot();

        Context context = new Context(identity, location, resource);

        assertEquals("agent-001", context.getIdentity().getAgentId());
        assertEquals("192.168.1.1", context.getLocation().getHost());
        assertEquals("ACTIVE", context.getResource().getStatus());
        assertTrue(context.getTimestamp() > 0);
    }

    @Test
    public void testCreateContextWithInfo() {
        IdentityInfo identity = new IdentityInfo();
        identity.setAgentId("agent-001");
        identity.setAgentName("TestAgent");

        LocationInfo location = new LocationInfo();
        location.setHost("192.168.1.1");
        location.setPort(8080);

        ResourceInfo resource = new ResourceInfo();
        resource.setStatus("ACTIVE");
        resource.addCapability("compute");

        Context context = new Context(identity, location, resource);

        assertEquals("agent-001", context.getIdentity().getAgentId());
        assertEquals("192.168.1.1", context.getLocation().getHost());
        assertEquals("ACTIVE", context.getResource().getStatus());
    }

    @Test
    public void testCreateContextWithNullInfo() {
        Context context = new Context((IdentityInfo) null, null, null);

        assertNull(context.getIdentity());
        assertNull(context.getLocation());
        assertNull(context.getResource());
    }

    @Test
    public void testGetAgentId() {
        IdentityInfo identity = new IdentityInfo();
        identity.setAgentId("agent-001");

        Context context = new Context(identity, new LocationInfo(), new ResourceInfo());

        assertEquals("agent-001", context.getAgentId());
    }

    @Test
    public void testGetAgentIdNull() {
        Context context = new Context((IdentityInfo) null, null, null);

        assertNull(context.getAgentId());
    }

    @Test
    public void testGetAddress() {
        LocationInfo location = new LocationInfo();
        location.setHost("192.168.1.1");
        location.setPort(8080);

        Context context = new Context(new IdentityInfo(), location, new ResourceInfo());

        assertEquals("192.168.1.1:8080", context.getAddress());
    }

    @Test
    public void testGetAddressNull() {
        Context context = new Context(new IdentityInfo(), null, new ResourceInfo());

        assertNull(context.getAddress());
    }

    @Test
    public void testGetStatus() {
        ResourceInfo resource = new ResourceInfo();
        resource.setStatus("ACTIVE");

        Context context = new Context(new IdentityInfo(), new LocationInfo(), resource);

        assertEquals("ACTIVE", context.getStatus());
    }

    @Test
    public void testGetStatusNull() {
        Context context = new Context(new IdentityInfo(), new LocationInfo(), null);

        assertNull(context.getStatus());
    }

    @Test
    public void testHasCapability() {
        ResourceInfo resource = new ResourceInfo();
        resource.addCapability("compute");
        resource.addCapability("storage");

        Context context = new Context(new IdentityInfo(), new LocationInfo(), resource);

        assertTrue(context.hasCapability("compute"));
        assertTrue(context.hasCapability("storage"));
        assertFalse(context.hasCapability("network"));
    }

    @Test
    public void testHasCapabilityNullResource() {
        Context context = new Context(new IdentityInfo(), new LocationInfo(), null);

        assertFalse(context.hasCapability("compute"));
    }

    @Test
    public void testIsValid() {
        IdentityInfo identity = new IdentityInfo();
        identity.setAgentId("agent-001");

        Context context = new Context(identity, new LocationInfo(), new ResourceInfo());

        assertTrue(context.isValid());
    }

    @Test
    public void testIsValidNullIdentity() {
        Context context = new Context((IdentityInfo) null, new LocationInfo(), new ResourceInfo());

        assertFalse(context.isValid());
    }

    @Test
    public void testIsValidNullAgentId() {
        IdentityInfo identity = new IdentityInfo();
        identity.setAgentId(null);

        Context context = new Context(identity, new LocationInfo(), new ResourceInfo());

        assertFalse(context.isValid());
    }

    @Test
    public void testGetSummary() {
        IdentityInfo identity = new IdentityInfo();
        identity.setAgentId("agent-001");

        LocationInfo location = new LocationInfo();
        location.setHost("192.168.1.1");
        location.setPort(8080);

        ResourceInfo resource = new ResourceInfo();
        resource.setStatus("ACTIVE");

        Context context = new Context(identity, location, resource);

        String summary = context.getSummary();
        assertNotNull(summary);
        assertTrue(summary.contains("agent=agent-001"));
        assertTrue(summary.contains("addr=192.168.1.1:8080"));
        assertTrue(summary.contains("status=ACTIVE"));
    }

    @Test
    public void testGetSummaryWithNulls() {
        Context context = new Context((IdentityInfo) null, null, null);

        String summary = context.getSummary();
        assertNotNull(summary);
        assertTrue(summary.contains("agent=unknown"));
        assertTrue(summary.contains("addr=unknown"));
        assertTrue(summary.contains("status=unknown"));
    }

    @Test
    public void testEmptyFactory() {
        Context context = Context.empty();

        assertNotNull(context);
        assertNull(context.getIdentity());
        assertNull(context.getLocation());
        assertNull(context.getResource());
    }

    @Test
    public void testTimestamp() throws InterruptedException {
        long before = System.currentTimeMillis();
        Context context = new Context(new IdentityInfo(), new LocationInfo(), new ResourceInfo());
        long after = System.currentTimeMillis();

        assertTrue(context.getTimestamp() >= before);
        assertTrue(context.getTimestamp() <= after);
    }

    @Test
    public void testGetIdentity() {
        IdentityInfo identity = new IdentityInfo();
        identity.setAgentId("agent-001");

        Context context = new Context(identity, new LocationInfo(), new ResourceInfo());

        assertNotNull(context.getIdentity());
        assertEquals("agent-001", context.getIdentity().getAgentId());
    }

    @Test
    public void testGetLocation() {
        LocationInfo location = new LocationInfo();
        location.setHost("192.168.1.1");

        Context context = new Context(new IdentityInfo(), location, new ResourceInfo());

        assertNotNull(context.getLocation());
        assertEquals("192.168.1.1", context.getLocation().getHost());
    }

    @Test
    public void testGetResource() {
        ResourceInfo resource = new ResourceInfo();
        resource.setStatus("ACTIVE");

        Context context = new Context(new IdentityInfo(), new LocationInfo(), resource);

        assertNotNull(context.getResource());
        assertEquals("ACTIVE", context.getResource().getStatus());
    }
}

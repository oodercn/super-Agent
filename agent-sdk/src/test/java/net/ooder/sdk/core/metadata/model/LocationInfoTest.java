package net.ooder.sdk.core.metadata.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class LocationInfoTest {

    @Test
    public void testCreateLocation() {
        LocationInfo location = new LocationInfo();

        location.setNetworkId("net-001");
        location.setHost("192.168.1.1");
        location.setPort(8080);
        location.setEndpoint("tcp://192.168.1.1:8080");
        location.setRegion("us-east");
        location.setZone("zone-a");
        location.setDataCenter("dc1");
        location.setNetworkType("TCP");
        location.setLatitude(40.7128);
        location.setLongitude(-74.0060);

        assertEquals("net-001", location.getNetworkId());
        assertEquals("192.168.1.1", location.getHost());
        assertEquals(8080, location.getPort());
        assertEquals("tcp://192.168.1.1:8080", location.getEndpoint());
        assertEquals("us-east", location.getRegion());
        assertEquals("zone-a", location.getZone());
        assertEquals("dc1", location.getDataCenter());
        assertEquals("TCP", location.getNetworkType());
        assertEquals(40.7128, location.getLatitude(), 0.0001);
        assertEquals(-74.0060, location.getLongitude(), 0.0001);
    }

    @Test
    public void testGetAddress() {
        LocationInfo location = new LocationInfo();
        location.setHost("192.168.1.1");
        location.setPort(8080);

        assertEquals("192.168.1.1:8080", location.getAddress());
    }

    @Test
    public void testGetAddressWithEndpoint() {
        LocationInfo location = new LocationInfo();
        location.setEndpoint("tcp://192.168.1.1:8080");

        assertEquals("tcp://192.168.1.1:8080", location.getAddress());
    }

    @Test
    public void testGetAddressHostOnly() {
        LocationInfo location = new LocationInfo();
        location.setHost("192.168.1.1");

        assertEquals("192.168.1.1", location.getAddress());
    }

    @Test
    public void testGetAddressNull() {
        LocationInfo location = new LocationInfo();

        assertNull(location.getAddress());
    }

    @Test
    public void testGetFullAddress() {
        LocationInfo location = new LocationInfo();
        location.setNetworkType("TCP");
        location.setHost("192.168.1.1");
        location.setPort(8080);

        assertEquals("tcp://192.168.1.1:8080", location.getFullAddress());
    }

    @Test
    public void testHasValidAddress() {
        LocationInfo location = new LocationInfo();
        location.setHost("192.168.1.1");
        location.setPort(8080);

        assertTrue(location.hasValidAddress());
    }

    @Test
    public void testHasValidAddressNoPort() {
        LocationInfo location = new LocationInfo();
        location.setHost("192.168.1.1");

        assertFalse(location.hasValidAddress());
    }

    @Test
    public void testHasValidAddressNoHost() {
        LocationInfo location = new LocationInfo();
        location.setPort(8080);

        assertFalse(location.hasValidAddress());
    }

    @Test
    public void testIsLocal() {
        LocationInfo location = new LocationInfo();
        location.setHost("localhost");

        assertTrue(location.isLocal());
    }

    @Test
    public void testIsLocalLoopback() {
        LocationInfo location = new LocationInfo();
        location.setHost("127.0.0.1");

        assertTrue(location.isLocal());
    }

    @Test
    public void testIsInSameRegion() {
        LocationInfo loc1 = new LocationInfo();
        loc1.setRegion("us-east");

        LocationInfo loc2 = new LocationInfo();
        loc2.setRegion("us-east");

        assertTrue(loc1.isInSameRegion(loc2));
    }

    @Test
    public void testIsInSameRegionDifferent() {
        LocationInfo loc1 = new LocationInfo();
        loc1.setRegion("us-east");

        LocationInfo loc2 = new LocationInfo();
        loc2.setRegion("us-west");

        assertFalse(loc1.isInSameRegion(loc2));
    }

    @Test
    public void testIsInSameRegionNull() {
        LocationInfo loc1 = new LocationInfo();
        loc1.setRegion("us-east");

        assertFalse(loc1.isInSameRegion(null));
    }

    @Test
    public void testIsInSameZone() {
        LocationInfo loc1 = new LocationInfo();
        loc1.setZone("zone-a");

        LocationInfo loc2 = new LocationInfo();
        loc2.setZone("zone-a");

        assertTrue(loc1.isInSameZone(loc2));
    }

    @Test
    public void testDistanceTo() {
        LocationInfo loc1 = new LocationInfo();
        loc1.setLatitude(40.7128);
        loc1.setLongitude(-74.0060);

        LocationInfo loc2 = new LocationInfo();
        loc2.setLatitude(34.0522);
        loc2.setLongitude(-118.2437);

        double distance = loc1.distanceTo(loc2);
        assertTrue(distance > 3900 && distance < 4000);
    }

    @Test
    public void testDistanceToNull() {
        LocationInfo loc1 = new LocationInfo();
        loc1.setLatitude(40.7128);
        loc1.setLongitude(-74.0060);

        assertEquals(-1, loc1.distanceTo(null), 0);
    }

    @Test
    public void testSnapshot() {
        LocationInfo location = new LocationInfo();
        location.setNetworkId("net-001");
        location.setHost("192.168.1.1");
        location.setPort(8080);
        location.setRegion("us-east");

        LocationSnapshot snapshot = location.snapshot();

        assertEquals("net-001", snapshot.getNetworkId());
        assertEquals("192.168.1.1", snapshot.getHost());
        assertEquals(8080, snapshot.getPort());
        assertEquals("us-east", snapshot.getRegion());
        assertTrue(snapshot.getSnapshotTime() > 0);
    }

    @Test
    public void testFactoryOf() {
        LocationInfo location = LocationInfo.of("192.168.1.1", 8080);

        assertEquals("192.168.1.1", location.getHost());
        assertEquals(8080, location.getPort());
    }

    @Test
    public void testFactoryOfEndpoint() {
        LocationInfo location = LocationInfo.ofEndpoint("tcp://192.168.1.1:8080");

        assertEquals("tcp://192.168.1.1:8080", location.getEndpoint());
    }

    @Test
    public void testFactoryLocal() {
        LocationInfo location = LocationInfo.local(8080);

        assertEquals("0.0.0.0", location.getHost());
        assertEquals(8080, location.getPort());
        assertEquals("local", location.getRegion());
    }

    @Test
    public void testAttributes() {
        LocationInfo location = new LocationInfo();

        location.setNetworkAttribute("key1", "value1");
        location.setNetworkAttribute("key2", "value2");

        assertEquals("value1", location.getNetworkAttribute("key1"));
        assertEquals("value2", location.getNetworkAttribute("key2"));
        assertNull(location.getNetworkAttribute("nonexistent"));
    }

    @Test
    public void testGetLocationKey() {
        LocationInfo location = new LocationInfo();
        location.setHost("192.168.1.1");
        location.setPort(8080);

        assertEquals("192.168.1.1:8080", location.getLocationKey());
    }

    @Test
    public void testGetLocationKeyNull() {
        LocationInfo location = new LocationInfo();

        assertEquals("unknown", location.getLocationKey());
    }
}

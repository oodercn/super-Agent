package net.ooder.sdk.core.metadata.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class TimelineTest {

    private Timeline timeline;

    @Before
    public void setUp() {
        timeline = new Timeline();
    }

    @Test
    public void testCreateTimeline() {
        assertNotNull(timeline);
        assertNotNull(timeline.getHistory());
        assertTrue(timeline.getHistory().isEmpty());
        assertNotNull(timeline.getCurrentContext());
    }

    @Test
    public void testCreateWithMaxHistory() {
        Timeline customTimeline = new Timeline(100);
        assertEquals(100, customTimeline.getMaxHistory());
    }

    @Test
    public void testDefaultMaxHistory() {
        assertEquals(1000, timeline.getMaxHistory());
    }

    @Test
    public void testRecordEvent() {
        timeline.record("event1");
        timeline.record("event2");

        assertEquals(2, timeline.getEventCount());
    }

    @Test
    public void testRecordEventWithContext() {
        IdentityInfo identity = new IdentityInfo();
        identity.setAgentId("agent-001");

        LocationInfo location = new LocationInfo();
        location.setHost("192.168.1.1");

        ResourceInfo resource = new ResourceInfo();
        resource.setStatus("ACTIVE");

        timeline.record("test_event", identity, location, resource);

        assertEquals(1, timeline.getEventCount());
        assertNotNull(timeline.getCurrentContext());
    }

    @Test
    public void testGetLatest() {
        timeline.record("event1");
        timeline.record("event2");
        timeline.record("event3");

        TimelinePoint latest = timeline.getLatest();
        assertNotNull(latest);
        assertEquals("event3", latest.getEvent());
    }

    @Test
    public void testGetLatestEmpty() {
        TimelinePoint latest = timeline.getLatest();
        assertNull(latest);
    }

    @Test
    public void testGetFirst() {
        timeline.record("event1");
        timeline.record("event2");
        timeline.record("event3");

        TimelinePoint first = timeline.getFirst();
        assertNotNull(first);
        assertEquals("event1", first.getEvent());
    }

    @Test
    public void testGetFirstEmpty() {
        TimelinePoint first = timeline.getFirst();
        assertNull(first);
    }

    @Test
    public void testGetByEventType() {
        timeline.record("start process");
        timeline.record("action performed");
        timeline.record("start task");
        timeline.record("stop process");

        List<TimelinePoint> startEvents = timeline.getByEventType("START");
        assertEquals(2, startEvents.size());

        List<TimelinePoint> stopEvents = timeline.getByEventType("STOP");
        assertEquals(1, stopEvents.size());

        List<TimelinePoint> nonexistentEvents = timeline.getByEventType("nonexistent");
        assertTrue(nonexistentEvents.isEmpty());
    }

    @Test
    public void testGetByTimeRange() throws InterruptedException {
        long start = System.currentTimeMillis();
        timeline.record("event1");
        Thread.sleep(10);
        timeline.record("event2");
        Thread.sleep(10);
        long mid = System.currentTimeMillis();
        timeline.record("event3");
        Thread.sleep(10);
        long end = System.currentTimeMillis();

        List<TimelinePoint> allEvents = timeline.getByTimeRange(start, end);
        assertEquals(3, allEvents.size());

        List<TimelinePoint> partialEvents = timeline.getByTimeRange(start, mid);
        assertTrue(partialEvents.size() >= 2);
    }

    @Test
    public void testFindNearest() throws InterruptedException {
        timeline.record("event1");
        Thread.sleep(50);
        long targetTime = System.currentTimeMillis();
        Thread.sleep(50);
        timeline.record("event2");
        Thread.sleep(50);
        timeline.record("event3");

        TimelinePoint nearest = timeline.findNearest(targetTime);
        assertNotNull(nearest);
    }

    @Test
    public void testFindNearestEmpty() {
        TimelinePoint nearest = timeline.findNearest(System.currentTimeMillis());
        assertNull(nearest);
    }

    @Test
    public void testGetContextAt() {
        IdentityInfo identity = new IdentityInfo();
        identity.setAgentId("agent-001");
        timeline.record("event1", identity, new LocationInfo(), new ResourceInfo());

        Context context = timeline.getContextAt(System.currentTimeMillis());
        assertNotNull(context);
    }

    @Test
    public void testGetContextAtEmpty() {
        Context context = timeline.getContextAt(System.currentTimeMillis());
        assertNull(context);
    }

    @Test
    public void testIsOnline() {
        timeline.record("event1");
        assertTrue(timeline.isOnline());
    }

    @Test
    public void testIsOnlineEmpty() {
        assertFalse(timeline.isOnline());
    }

    @Test
    public void testGetAge() throws InterruptedException {
        long beforeCreate = System.currentTimeMillis();
        Timeline newTimeline = new Timeline();
        Thread.sleep(10);

        assertTrue(newTimeline.getAge() >= 10);
        assertTrue(newTimeline.getAge() < 1000);
    }

    @Test
    public void testGetUptime() {
        timeline.record("event1");
        assertTrue(timeline.getUptime() >= 0);
    }

    @Test
    public void testGetIdleTime() throws InterruptedException {
        timeline.record("event1");
        Thread.sleep(10);

        assertTrue(timeline.getIdleTime() >= 10);
    }

    @Test
    public void testGetIdleTimeEmpty() {
        long idleTime = timeline.getIdleTime();
        assertTrue(idleTime >= 0);
    }

    @Test
    public void testGetTimeSinceLastEvent() throws InterruptedException {
        timeline.record("event1");
        Thread.sleep(10);

        assertTrue(timeline.getTimeSinceLastEvent() >= 10);
    }

    @Test
    public void testGetEventCount() {
        assertEquals(0, timeline.getEventCount());

        timeline.record("event1");
        assertEquals(1, timeline.getEventCount());

        timeline.record("event2");
        assertEquals(2, timeline.getEventCount());
    }

    @Test
    public void testClear() {
        timeline.record("event1");
        timeline.record("event2");
        assertEquals(2, timeline.getEventCount());

        timeline.clear();
        assertEquals(0, timeline.getEventCount());
    }

    @Test
    public void testGetAgeFormatted() {
        String formatted = timeline.getAgeFormatted();
        assertNotNull(formatted);
        assertTrue(formatted.contains("s") || formatted.contains("m") || formatted.contains("h") || formatted.contains("d"));
    }

    @Test
    public void testGetIdleTimeFormatted() {
        timeline.record("event1");
        String formatted = timeline.getIdleTimeFormatted();
        assertNotNull(formatted);
    }

    @Test
    public void testGetStatusSummary() {
        timeline.record("event1");
        String summary = timeline.getStatusSummary();

        assertNotNull(summary);
        assertTrue(summary.contains("online"));
        assertTrue(summary.contains("age="));
        assertTrue(summary.contains("idle="));
        assertTrue(summary.contains("events="));
    }

    @Test
    public void testGetStatusSummaryOffline() {
        String summary = timeline.getStatusSummary();
        assertTrue(summary.contains("offline"));
    }

    @Test
    public void testUpdateContext() {
        IdentityInfo identity = new IdentityInfo();
        identity.setAgentId("agent-001");

        LocationInfo location = new LocationInfo();
        location.setHost("192.168.1.1");

        ResourceInfo resource = new ResourceInfo();
        resource.setStatus("ACTIVE");

        timeline.updateContext(identity, location, resource);

        Context context = timeline.getCurrentContext();
        assertNotNull(context);
        assertEquals("agent-001", context.getAgentId());
    }

    @Test
    public void testSetCurrentContext() {
        Context context = Context.empty();
        timeline.setCurrentContext(context);

        assertEquals(context, timeline.getCurrentContext());
    }

    @Test
    public void testSetMaxHistory() {
        timeline.setMaxHistory(500);
        assertEquals(500, timeline.getMaxHistory());
    }

    @Test
    public void testSetOnlineTimeout() {
        timeline.setOnlineTimeout(60000);
        assertEquals(60000, timeline.getOnlineTimeout());
    }

    @Test
    public void testMaxHistoryLimit() {
        Timeline smallTimeline = new Timeline(3);

        smallTimeline.record("event1");
        smallTimeline.record("event2");
        smallTimeline.record("event3");
        smallTimeline.record("event4");

        assertEquals(3, smallTimeline.getEventCount());
        assertEquals("event2", smallTimeline.getFirst().getEvent());
        assertEquals("event4", smallTimeline.getLatest().getEvent());
    }

    @Test
    public void testFactoryCreate() {
        Timeline newTimeline = Timeline.create();
        assertNotNull(newTimeline);
    }

    @Test
    public void testFactoryWithMaxHistory() {
        Timeline newTimeline = Timeline.withMaxHistory(100);
        assertNotNull(newTimeline);
        assertEquals(100, newTimeline.getMaxHistory());
    }

    @Test
    public void testGetCreateTime() {
        long beforeCreate = System.currentTimeMillis();
        Timeline newTimeline = new Timeline();
        long afterCreate = System.currentTimeMillis();

        assertTrue(newTimeline.getCreateTime() >= beforeCreate);
        assertTrue(newTimeline.getCreateTime() <= afterCreate);
    }

    @Test
    public void testIsExpired() {
        assertFalse(timeline.isExpired());
    }
}

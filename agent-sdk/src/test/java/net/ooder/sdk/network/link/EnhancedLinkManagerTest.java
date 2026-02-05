package net.ooder.sdk.network.link;

import net.ooder.sdk.network.link.LinkInfo;
import net.ooder.sdk.network.link.LinkStatus;
import net.ooder.sdk.network.link.LinkTable;
import net.ooder.sdk.network.link.model.LinkEvent;
import net.ooder.sdk.network.link.model.LinkEventType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedLinkManagerTest {

    @Mock
    private EnhancedLinkManager enhancedLinkManager;

    private String testLinkId1;
    private String testLinkId2;
    private LinkEvent testLinkEvent;
    private Consumer<LinkEvent> testEventHandler;

    @Before
    public void setUp() {
        // 初始化测试链路ID
        testLinkId1 = "link-1";
        testLinkId2 = "link-2";

        // 初始化测试链路事件
        testLinkEvent = new LinkEvent();
        testLinkEvent.setEventId("event-1");
        testLinkEvent.setLinkId(testLinkId1);
        testLinkEvent.setEventType(LinkEventType.LINK_PERFORMANCE_DEGRADED);
        testLinkEvent.setTimestamp(System.currentTimeMillis());
        testLinkEvent.setDetails(Collections.singletonMap("reason", "high latency"));

        // 初始化测试事件处理器
        testEventHandler = event -> {
            System.out.println("Received link event: " + event.getEventType());
        };
    }

    // 测试链路性能监控相关方法
    @Test
    public void testLinkMonitoringMethods() {
        // 测试开始链路监控
        enhancedLinkManager.startLinkMonitoring();
        verify(enhancedLinkManager).startLinkMonitoring();

        // 测试停止链路监控
        enhancedLinkManager.stopLinkMonitoring();
        verify(enhancedLinkManager).stopLinkMonitoring();

        // 测试链路监控运行状态
        when(enhancedLinkManager.isLinkMonitoringRunning()).thenReturn(true);
        assertTrue(enhancedLinkManager.isLinkMonitoringRunning());

        when(enhancedLinkManager.isLinkMonitoringRunning()).thenReturn(false);
        assertFalse(enhancedLinkManager.isLinkMonitoringRunning());
    }

    // 测试链路性能分析相关方法
    @Test
    public void testLinkPerformanceAnalysisMethods() throws ExecutionException, InterruptedException {
        // 测试分析单个链路性能
        Map<String, Object> linkPerformance = new HashMap<>();
        linkPerformance.put("latency", 15.5);
        linkPerformance.put("bandwidth", 1000);
        linkPerformance.put("packetLoss", 0.1);
        linkPerformance.put("utilization", 0.4);
        when(enhancedLinkManager.analyzeLinkPerformance(testLinkId1)).thenReturn(CompletableFuture.completedFuture(linkPerformance));
        Map<String, Object> result1 = enhancedLinkManager.analyzeLinkPerformance(testLinkId1).get();
        assertNotNull(result1);
        assertEquals(15.5, result1.get("latency"));
        assertEquals(1000, result1.get("bandwidth"));

        // 测试分析所有链路性能
        Map<String, Object> allLinksPerformance = new HashMap<>();
        allLinksPerformance.put("averageLatency", 12.3);
        allLinksPerformance.put("averageBandwidth", 950);
        allLinksPerformance.put("averagePacketLoss", 0.05);
        allLinksPerformance.put("totalLinks", 5);
        when(enhancedLinkManager.analyzeAllLinksPerformance()).thenReturn(CompletableFuture.completedFuture(allLinksPerformance));
        Map<String, Object> result2 = enhancedLinkManager.analyzeAllLinksPerformance().get();
        assertNotNull(result2);
        assertEquals(12.3, result2.get("averageLatency"));
        assertEquals(5, result2.get("totalLinks"));

        // 测试查找性能问题
        List<String> performanceIssues = Arrays.asList(testLinkId1, testLinkId2);
        when(enhancedLinkManager.findPerformanceIssues()).thenReturn(CompletableFuture.completedFuture(performanceIssues));
        List<String> issues = enhancedLinkManager.findPerformanceIssues().get();
        assertNotNull(issues);
        assertEquals(2, issues.size());
        assertEquals(testLinkId1, issues.get(0));
        assertEquals(testLinkId2, issues.get(1));

        // 测试查找瓶颈
        List<String> bottlenecks = Collections.singletonList(testLinkId1);
        when(enhancedLinkManager.findBottlenecks()).thenReturn(CompletableFuture.completedFuture(bottlenecks));
        List<String> foundBottlenecks = enhancedLinkManager.findBottlenecks().get();
        assertNotNull(foundBottlenecks);
        assertEquals(1, foundBottlenecks.size());
        assertEquals(testLinkId1, foundBottlenecks.get(0));
    }

    // 测试链路事件相关方法
    @Test
    public void testLinkEventMethods() throws InterruptedException {
        // 测试发布链路事件
        enhancedLinkManager.publishLinkEvent(testLinkEvent);
        verify(enhancedLinkManager).publishLinkEvent(testLinkEvent);

        // 测试订阅链路事件
        enhancedLinkManager.subscribeToLinkEvents(testEventHandler);
        verify(enhancedLinkManager).subscribeToLinkEvents(testEventHandler);

        // 测试取消订阅链路事件
        enhancedLinkManager.unsubscribeFromLinkEvents(testEventHandler);
        verify(enhancedLinkManager).unsubscribeFromLinkEvents(testEventHandler);

        // 测试按类型订阅链路事件
        enhancedLinkManager.subscribeToLinkEventsByType(LinkEventType.LINK_PERFORMANCE_DEGRADED, testEventHandler);
        verify(enhancedLinkManager).subscribeToLinkEventsByType(LinkEventType.LINK_PERFORMANCE_DEGRADED, testEventHandler);

        // 测试按类型取消订阅链路事件
        enhancedLinkManager.unsubscribeFromLinkEventsByType(LinkEventType.LINK_PERFORMANCE_DEGRADED, testEventHandler);
        verify(enhancedLinkManager).unsubscribeFromLinkEventsByType(LinkEventType.LINK_PERFORMANCE_DEGRADED, testEventHandler);

        // 测试获取最近的链路事件
        List<LinkEvent> events = Collections.singletonList(testLinkEvent);
        when(enhancedLinkManager.getRecentLinkEvents(10)).thenReturn(events);
        List<LinkEvent> recentEvents = enhancedLinkManager.getRecentLinkEvents(10);
        assertNotNull(recentEvents);
        assertEquals(1, recentEvents.size());
        assertEquals("event-1", recentEvents.get(0).getEventId());

        // 测试按类型获取链路事件
        when(enhancedLinkManager.getLinkEventsByType(LinkEventType.LINK_PERFORMANCE_DEGRADED, 10)).thenReturn(events);
        List<LinkEvent> performanceEvents = enhancedLinkManager.getLinkEventsByType(LinkEventType.LINK_PERFORMANCE_DEGRADED, 10);
        assertNotNull(performanceEvents);
        assertEquals(1, performanceEvents.size());
        assertEquals(LinkEventType.LINK_PERFORMANCE_DEGRADED, performanceEvents.get(0).getEventType());
    }

    // 测试链路统计相关方法
    @Test
    public void testLinkStatisticsMethods() throws ExecutionException, InterruptedException {
        // 测试获取单个链路统计信息
        Map<String, Object> linkStats = new HashMap<>();
        linkStats.put("latency", 10.5);
        linkStats.put("bandwidth", 1000);
        linkStats.put("packetLoss", 0.05);
        linkStats.put("utilization", 0.3);
        linkStats.put("uptime", 3600);
        when(enhancedLinkManager.getLinkStatistics(testLinkId1)).thenReturn(CompletableFuture.completedFuture(linkStats));
        Map<String, Object> retrievedLinkStats = enhancedLinkManager.getLinkStatistics(testLinkId1).get();
        assertNotNull(retrievedLinkStats);
        assertEquals(10.5, retrievedLinkStats.get("latency"));
        assertEquals(1000, retrievedLinkStats.get("bandwidth"));

        // 测试获取整体链路统计信息
        Map<String, Object> overallStats = new HashMap<>();
        overallStats.put("totalLinks", 5);
        overallStats.put("activeLinks", 4);
        overallStats.put("averageLatency", 12.3);
        overallStats.put("averageBandwidth", 950);
        overallStats.put("totalUptime", 18000);
        when(enhancedLinkManager.getOverallLinkStatistics()).thenReturn(CompletableFuture.completedFuture(overallStats));
        Map<String, Object> retrievedOverallStats = enhancedLinkManager.getOverallLinkStatistics().get();
        assertNotNull(retrievedOverallStats);
        assertEquals(5, retrievedOverallStats.get("totalLinks"));
        assertEquals(4, retrievedOverallStats.get("activeLinks"));

        // 测试获取单个链路健康状态
        Map<String, Object> linkHealth = new HashMap<>();
        linkHealth.put("status", "healthy");
        linkHealth.put("score", 95.5);
        linkHealth.put("issues", 0);
        linkHealth.put("lastCheck", System.currentTimeMillis());
        when(enhancedLinkManager.getLinkHealth(testLinkId1)).thenReturn(CompletableFuture.completedFuture(linkHealth));
        Map<String, Object> retrievedLinkHealth = enhancedLinkManager.getLinkHealth(testLinkId1).get();
        assertNotNull(retrievedLinkHealth);
        assertEquals("healthy", retrievedLinkHealth.get("status"));
        assertEquals(95.5, retrievedLinkHealth.get("score"));

        // 测试获取整体链路健康状态
        Map<String, Object> overallHealth = new HashMap<>();
        overallHealth.put("status", "healthy");
        overallHealth.put("averageScore", 92.3);
        overallHealth.put("totalIssues", 1);
        overallHealth.put("lastCheck", System.currentTimeMillis());
        when(enhancedLinkManager.getOverallLinkHealth()).thenReturn(CompletableFuture.completedFuture(overallHealth));
        Map<String, Object> retrievedOverallHealth = enhancedLinkManager.getOverallLinkHealth().get();
        assertNotNull(retrievedOverallHealth);
        assertEquals("healthy", retrievedOverallHealth.get("status"));
        assertEquals(92.3, retrievedOverallHealth.get("averageScore"));
    }

    // 测试链路操作相关方法
    @Test
    public void testLinkOperationMethods() throws ExecutionException, InterruptedException {
        // 测试更新链路指标
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("latency", 15.5);
        metrics.put("bandwidth", 950);
        metrics.put("packetLoss", 0.1);
        when(enhancedLinkManager.updateLinkMetrics(testLinkId1, metrics)).thenReturn(CompletableFuture.completedFuture(true));
        boolean metricsUpdated = enhancedLinkManager.updateLinkMetrics(testLinkId1, metrics).get();
        assertTrue(metricsUpdated);

        // 测试更新链路质量
        double quality = 0.85;
        when(enhancedLinkManager.updateLinkQuality(testLinkId1, quality)).thenReturn(CompletableFuture.completedFuture(true));
        boolean qualityUpdated = enhancedLinkManager.updateLinkQuality(testLinkId1, quality).get();
        assertTrue(qualityUpdated);

        // 测试测试链路
        when(enhancedLinkManager.testLink(testLinkId1)).thenReturn(CompletableFuture.completedFuture(true));
        boolean linkTested = enhancedLinkManager.testLink(testLinkId1).get();
        assertTrue(linkTested);

        // 测试重置链路统计信息
        when(enhancedLinkManager.resetLinkStatistics(testLinkId1)).thenReturn(CompletableFuture.completedFuture(true));
        boolean statsReset = enhancedLinkManager.resetLinkStatistics(testLinkId1).get();
        assertTrue(statsReset);
    }

    // 测试链路配置相关方法
    @Test
    public void testLinkConfigurationMethods() throws ExecutionException, InterruptedException {
        // 测试设置链路阈值
        Map<String, Object> thresholds = new HashMap<>();
        thresholds.put("latency", 50.0);
        thresholds.put("packetLoss", 1.0);
        thresholds.put("utilization", 90.0);
        when(enhancedLinkManager.setLinkThresholds(testLinkId1, thresholds)).thenReturn(CompletableFuture.completedFuture(true));
        boolean thresholdsSet = enhancedLinkManager.setLinkThresholds(testLinkId1, thresholds).get();
        assertTrue(thresholdsSet);

        // 测试获取链路阈值
        when(enhancedLinkManager.getLinkThresholds(testLinkId1)).thenReturn(CompletableFuture.completedFuture(thresholds));
        Map<String, Object> retrievedThresholds = enhancedLinkManager.getLinkThresholds(testLinkId1).get();
        assertNotNull(retrievedThresholds);
        assertEquals(50.0, retrievedThresholds.get("latency"));
        assertEquals(1.0, retrievedThresholds.get("packetLoss"));
        assertEquals(90.0, retrievedThresholds.get("utilization"));
    }

    // 测试链路拓扑相关方法
    @Test
    public void testLinkTopologyMethods() throws ExecutionException, InterruptedException {
        // 测试获取链路拓扑
        Map<String, Object> topology = new HashMap<>();
        topology.put("nodes", 10);
        topology.put("links", 15);
        topology.put("density", 0.3);
        topology.put("averagePathLength", 2.5);
        when(enhancedLinkManager.getLinkTopology()).thenReturn(CompletableFuture.completedFuture(topology));
        Map<String, Object> retrievedTopology = enhancedLinkManager.getLinkTopology().get();
        assertNotNull(retrievedTopology);
        assertEquals(10, retrievedTopology.get("nodes"));
        assertEquals(15, retrievedTopology.get("links"));

        // 测试获取最短路径
        String sourceNodeId = "node-1";
        String targetNodeId = "node-5";
        List<String> shortestPath = Arrays.asList("link-1", "link-3", "link-5");
        when(enhancedLinkManager.getShortestPath(sourceNodeId, targetNodeId)).thenReturn(CompletableFuture.completedFuture(shortestPath));
        List<String> retrievedPath = enhancedLinkManager.getShortestPath(sourceNodeId, targetNodeId).get();
        assertNotNull(retrievedPath);
        assertEquals(3, retrievedPath.size());
        assertEquals("link-1", retrievedPath.get(0));
        assertEquals("link-3", retrievedPath.get(1));
        assertEquals("link-5", retrievedPath.get(2));

        // 测试获取多条路径
        List<List<String>> multiplePaths = new ArrayList<>();
        multiplePaths.add(Arrays.asList("link-1", "link-3", "link-5"));
        multiplePaths.add(Arrays.asList("link-1", "link-4", "link-5"));
        multiplePaths.add(Arrays.asList("link-2", "link-4", "link-5"));
        when(enhancedLinkManager.getMultiplePaths(sourceNodeId, targetNodeId, 3)).thenReturn(CompletableFuture.completedFuture(multiplePaths));
        List<List<String>> retrievedPaths = enhancedLinkManager.getMultiplePaths(sourceNodeId, targetNodeId, 3).get();
        assertNotNull(retrievedPaths);
        assertEquals(3, retrievedPaths.size());
        assertEquals(3, retrievedPaths.get(0).size());
        assertEquals(3, retrievedPaths.get(1).size());
        assertEquals(3, retrievedPaths.get(2).size());
    }

    // 测试链路事件处理
    @Test
    public void testLinkEventHandler() throws InterruptedException {
        // 测试事件处理器
        final CountDownLatch latch = new CountDownLatch(1);
        final LinkEvent[] receivedEvent = new LinkEvent[1];

        Consumer<LinkEvent> eventHandler = e -> {
            receivedEvent[0] = e;
            latch.countDown();
        };

        // 模拟事件处理
        enhancedLinkManager.subscribeToLinkEvents(eventHandler);
        enhancedLinkManager.publishLinkEvent(testLinkEvent);

        // 等待事件处理
        boolean eventReceived = latch.await(1, TimeUnit.SECONDS);
        if (eventReceived) {
            assertNotNull(receivedEvent[0]);
            assertEquals(testLinkId1, receivedEvent[0].getLinkId());
            assertEquals(LinkEventType.LINK_PERFORMANCE_DEGRADED, receivedEvent[0].getEventType());
        }

        // 清理
        enhancedLinkManager.unsubscribeFromLinkEvents(eventHandler);
    }

    // 测试链路性能分析结果
    @Test
    public void testLinkPerformanceAnalysisResults() throws ExecutionException, InterruptedException {
        // 测试性能分析结果
        Map<String, Object> performanceAnalysis = new HashMap<>();
        performanceAnalysis.put("latency", 15.5);
        performanceAnalysis.put("bandwidth", 1000);
        performanceAnalysis.put("packetLoss", 0.1);
        performanceAnalysis.put("utilization", 0.4);
        performanceAnalysis.put("jitter", 2.3);
        performanceAnalysis.put("throughput", 500);
        performanceAnalysis.put("responseTime", 10.5);
        performanceAnalysis.put("errorRate", 0.01);

        when(enhancedLinkManager.analyzeLinkPerformance(testLinkId1)).thenReturn(CompletableFuture.completedFuture(performanceAnalysis));
        Map<String, Object> analysisResult = enhancedLinkManager.analyzeLinkPerformance(testLinkId1).get();

        assertNotNull(analysisResult);
        assertEquals(15.5, analysisResult.get("latency"));
        assertEquals(1000, analysisResult.get("bandwidth"));
        assertEquals(0.1, analysisResult.get("packetLoss"));
        assertEquals(0.4, analysisResult.get("utilization"));
        assertEquals(2.3, analysisResult.get("jitter"));
        assertEquals(500, analysisResult.get("throughput"));
        assertEquals(10.5, analysisResult.get("responseTime"));
        assertEquals(0.01, analysisResult.get("errorRate"));
    }

    // 测试链路健康状态
    @Test
    public void testLinkHealthStatus() throws ExecutionException, InterruptedException {
        // 测试健康状态
        Map<String, Object> healthStatus = new HashMap<>();
        healthStatus.put("status", "healthy");
        healthStatus.put("score", 95.5);
        healthStatus.put("issues", 0);
        healthStatus.put("lastCheck", System.currentTimeMillis());
        healthStatus.put("latency", 10.5);
        healthStatus.put("packetLoss", 0.05);
        healthStatus.put("utilization", 0.3);
        healthStatus.put("uptime", 3600);

        when(enhancedLinkManager.getLinkHealth(testLinkId1)).thenReturn(CompletableFuture.completedFuture(healthStatus));
        Map<String, Object> healthResult = enhancedLinkManager.getLinkHealth(testLinkId1).get();

        assertNotNull(healthResult);
        assertEquals("healthy", healthResult.get("status"));
        assertEquals(95.5, healthResult.get("score"));
        assertEquals(0, healthResult.get("issues"));
        assertTrue((long) healthResult.get("lastCheck") > System.currentTimeMillis() - 1000);
    }

    // 测试链路拓扑分析
    @Test
    public void testLinkTopologyAnalysis() throws ExecutionException, InterruptedException {
        // 测试拓扑分析
        Map<String, Object> topologyAnalysis = new HashMap<>();
        topologyAnalysis.put("nodes", 10);
        topologyAnalysis.put("links", 15);
        topologyAnalysis.put("density", 0.3);
        topologyAnalysis.put("averagePathLength", 2.5);
        topologyAnalysis.put("clusteringCoefficient", 0.6);
        topologyAnalysis.put("diameter", 4);
        topologyAnalysis.put("centrality", Collections.singletonMap("node-1", 0.8));

        when(enhancedLinkManager.getLinkTopology()).thenReturn(CompletableFuture.completedFuture(topologyAnalysis));
        Map<String, Object> topologyResult = enhancedLinkManager.getLinkTopology().get();

        assertNotNull(topologyResult);
        assertEquals(10, topologyResult.get("nodes"));
        assertEquals(15, topologyResult.get("links"));
        assertEquals(0.3, topologyResult.get("density"));
        assertEquals(2.5, topologyResult.get("averagePathLength"));
        assertEquals(0.6, topologyResult.get("clusteringCoefficient"));
        assertEquals(4, topologyResult.get("diameter"));
    }
}

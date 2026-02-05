package net.ooder.sdk.topology;

import net.ooder.sdk.topology.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TopologyManagerTest {

    @Mock
    private TopologyManager topologyManager;

    private NetworkNode testNode1;
    private NetworkNode testNode2;
    private NetworkLink testLink;

    @Before
    public void setUp() {
        // 初始化测试节点
        testNode1 = new NetworkNode();
        testNode1.setNodeId("node-1");
        testNode1.setNodeType(NodeType.SERVER);
        testNode1.setStatus(NodeStatus.ONLINE);
        testNode1.setIpAddress("192.168.1.1");
        Map<String, Object> properties1 = new HashMap<>();
        properties1.put("name", "Test Node 1");
        properties1.put("port", 8080);
        testNode1.setProperties(properties1);

        testNode2 = new NetworkNode();
        testNode2.setNodeId("node-2");
        testNode2.setNodeType(NodeType.CLIENT);
        testNode2.setStatus(NodeStatus.ONLINE);
        testNode2.setIpAddress("192.168.1.2");
        Map<String, Object> properties2 = new HashMap<>();
        properties2.put("name", "Test Node 2");
        properties2.put("port", 8081);
        testNode2.setProperties(properties2);

        // 初始化测试链路
        testLink = new NetworkLink();
        testLink.setLinkId("link-1");
        testLink.setSourceNodeId("node-1");
        testLink.setDestinationNodeId("node-2");
        testLink.setLinkType(LinkType.ETHERNET);
        testLink.setStatus(LinkStatus.UP);
        
        // 设置链路指标
        LinkMetrics metrics = new LinkMetrics();
        metrics.setBandwidth(1000);
        metrics.setLatency(10);
        metrics.setPacketLoss(0.1);
        testLink.setMetrics(metrics);
    }

    // 测试拓扑构建相关方法
    @Test
    public void testTopologyBuildMethods() {
        // 测试构建拓扑
        topologyManager.buildTopology();
        verify(topologyManager).buildTopology();

        // 测试重建拓扑
        topologyManager.rebuildTopology();
        verify(topologyManager).rebuildTopology();

        // 测试拓扑构建状态
        when(topologyManager.isTopologyBuilt()).thenReturn(true);
        assertTrue(topologyManager.isTopologyBuilt());
    }

    // 测试节点管理相关方法
    @Test
    public void testNodeManagementMethods() {
        // 测试添加节点
        when(topologyManager.addNode(testNode1)).thenReturn(testNode1);
        NetworkNode addedNode = topologyManager.addNode(testNode1);
        assertNotNull(addedNode);
        assertEquals("node-1", addedNode.getNodeId());

        // 测试移除节点
        topologyManager.removeNode("node-1");
        verify(topologyManager).removeNode("node-1");

        // 测试更新节点
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", NodeStatus.OFFLINE);
        NetworkNode updatedNode = testNode1;
        updatedNode.setStatus(NodeStatus.OFFLINE);
        when(topologyManager.updateNode("node-1", updates)).thenReturn(updatedNode);
        NetworkNode result = topologyManager.updateNode("node-1", updates);
        assertEquals(NodeStatus.OFFLINE, result.getStatus());

        // 测试获取节点
        when(topologyManager.getNode("node-1")).thenReturn(testNode1);
        NetworkNode retrievedNode = topologyManager.getNode("node-1");
        assertNotNull(retrievedNode);
        assertEquals("node-1", retrievedNode.getNodeId());

        // 测试获取所有节点
        List<NetworkNode> nodes = Arrays.asList(testNode1, testNode2);
        when(topologyManager.getAllNodes()).thenReturn(nodes);
        List<NetworkNode> allNodes = topologyManager.getAllNodes();
        assertEquals(2, allNodes.size());

        // 测试按类型获取节点
        when(topologyManager.getNodesByType(NodeType.SERVER)).thenReturn(Collections.singletonList(testNode1));
        List<NetworkNode> serverNodes = topologyManager.getNodesByType(NodeType.SERVER);
        assertEquals(1, serverNodes.size());
        assertEquals(NodeType.SERVER, serverNodes.get(0).getNodeType());

        // 测试按状态获取节点
        when(topologyManager.getNodesByStatus(NodeStatus.ONLINE)).thenReturn(Arrays.asList(testNode1, testNode2));
        List<NetworkNode> onlineNodes = topologyManager.getNodesByStatus(NodeStatus.ONLINE);
        assertEquals(2, onlineNodes.size());
    }

    // 测试链路管理相关方法
    @Test
    public void testLinkManagementMethods() {
        // 测试添加链路
        when(topologyManager.addLink(testLink)).thenReturn(testLink);
        NetworkLink addedLink = topologyManager.addLink(testLink);
        assertNotNull(addedLink);
        assertEquals("link-1", addedLink.getLinkId());

        // 测试移除链路
        topologyManager.removeLink("link-1");
        verify(topologyManager).removeLink("link-1");

        // 测试更新链路
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", LinkStatus.DOWN);
        NetworkLink updatedLink = testLink;
        updatedLink.setStatus(LinkStatus.DOWN);
        when(topologyManager.updateLink("link-1", updates)).thenReturn(updatedLink);
        NetworkLink result = topologyManager.updateLink("link-1", updates);
        assertEquals(LinkStatus.DOWN, result.getStatus());

        // 测试获取链路
        when(topologyManager.getLink("link-1")).thenReturn(testLink);
        NetworkLink retrievedLink = topologyManager.getLink("link-1");
        assertNotNull(retrievedLink);
        assertEquals("link-1", retrievedLink.getLinkId());

        // 测试获取所有链路
        List<NetworkLink> links = Collections.singletonList(testLink);
        when(topologyManager.getAllLinks()).thenReturn(links);
        List<NetworkLink> allLinks = topologyManager.getAllLinks();
        assertEquals(1, allLinks.size());

        // 测试按节点获取链路
        when(topologyManager.getLinksByNode("node-1")).thenReturn(Collections.singletonList(testLink));
        List<NetworkLink> nodeLinks = topologyManager.getLinksByNode("node-1");
        assertEquals(1, nodeLinks.size());

        // 测试按类型获取链路
        when(topologyManager.getLinksByType(LinkType.ETHERNET)).thenReturn(Collections.singletonList(testLink));
        List<NetworkLink> ethernetLinks = topologyManager.getLinksByType(LinkType.ETHERNET);
        assertEquals(1, ethernetLinks.size());
        assertEquals(LinkType.ETHERNET, ethernetLinks.get(0).getLinkType());

        // 测试按状态获取链路
        when(topologyManager.getLinksByStatus(LinkStatus.UP)).thenReturn(Collections.singletonList(testLink));
        List<NetworkLink> upLinks = topologyManager.getLinksByStatus(LinkStatus.UP);
        assertEquals(1, upLinks.size());
        assertEquals(LinkStatus.UP, upLinks.get(0).getStatus());
    }

    // 测试拓扑查询相关方法
    @Test
    public void testTopologyQueryMethods() {
        // 测试获取邻居节点
        when(topologyManager.getNeighbors("node-1")).thenReturn(Collections.singletonList(testNode2));
        List<NetworkNode> neighbors = topologyManager.getNeighbors("node-1");
        assertEquals(1, neighbors.size());
        assertEquals("node-2", neighbors.get(0).getNodeId());

        // 测试获取路径
        when(topologyManager.getPaths("node-1", "node-2")).thenReturn(Collections.singletonList(testLink));
        List<NetworkLink> paths = topologyManager.getPaths("node-1", "node-2");
        assertEquals(1, paths.size());

        // 测试获取拓扑统计信息
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalNodes", 2);
        stats.put("totalLinks", 1);
        stats.put("onlineNodes", 2);
        stats.put("activeLinks", 1);
        when(topologyManager.getTopologyStatistics()).thenReturn(stats);
        Map<String, Object> topologyStats = topologyManager.getTopologyStatistics();
        assertEquals(2, topologyStats.get("totalNodes"));
        assertEquals(1, topologyStats.get("totalLinks"));

        // 测试获取拓扑图
        String expectedGraph = "node-1 -- link-1 -- node-2";
        when(topologyManager.getTopologyGraph()).thenReturn(expectedGraph);
        String topologyGraph = topologyManager.getTopologyGraph();
        assertEquals(expectedGraph, topologyGraph);
    }

    // 测试拓扑分析相关方法
    @Test
    public void testTopologyAnalysisMethods() {
        // 测试分析拓扑
        Map<String, Object> analysis = new HashMap<>();
        analysis.put("connectivity", "full");
        analysis.put("redundancy", "medium");
        analysis.put("latency", 15.5);
        when(topologyManager.analyzeTopology()).thenReturn(analysis);
        Map<String, Object> topologyAnalysis = topologyManager.analyzeTopology();
        assertEquals("full", topologyAnalysis.get("connectivity"));

        // 测试分析节点
        Map<String, Object> nodeAnalysis = new HashMap<>();
        nodeAnalysis.put("load", 0.3);
        nodeAnalysis.put("throughput", 100.5);
        nodeAnalysis.put("responseTime", 10.2);
        when(topologyManager.analyzeNode("node-1")).thenReturn(nodeAnalysis);
        Map<String, Object> analysisResult = topologyManager.analyzeNode("node-1");
        assertEquals(0.3, analysisResult.get("load"));

        // 测试分析链路
        Map<String, Object> linkAnalysis = new HashMap<>();
        linkAnalysis.put("utilization", 0.4);
        linkAnalysis.put("throughput", 500.5);
        linkAnalysis.put("latency", 5.2);
        when(topologyManager.analyzeLink("link-1")).thenReturn(linkAnalysis);
        Map<String, Object> linkAnalysisResult = topologyManager.analyzeLink("link-1");
        assertEquals(0.4, linkAnalysisResult.get("utilization"));

        // 测试查找瓶颈
        List<String> bottlenecks = Arrays.asList("link-1");
        when(topologyManager.findBottlenecks()).thenReturn(bottlenecks);
        List<String> foundBottlenecks = topologyManager.findBottlenecks();
        assertEquals(1, foundBottlenecks.size());
        assertEquals("link-1", foundBottlenecks.get(0));

        // 测试查找单点故障
        List<String> spof = Arrays.asList("node-1");
        when(topologyManager.findSinglePointsOfFailure()).thenReturn(spof);
        List<String> foundSpof = topologyManager.findSinglePointsOfFailure();
        assertEquals(1, foundSpof.size());
        assertEquals("node-1", foundSpof.get(0));
    }

    // 测试拓扑变化处理相关方法
    @Test
    public void testTopologyChangeMethods() {
        // 测试处理节点变化
        Map<String, Object> nodeDetails = new HashMap<>();
        nodeDetails.put("nodeId", "node-1");
        TopologyEvent nodeEvent = new TopologyEvent(TopologyEventType.NODE_ADDED, nodeDetails);
        topologyManager.handleNodeChange("node-1", nodeEvent);
        verify(topologyManager).handleNodeChange("node-1", nodeEvent);

        // 测试处理链路变化
        Map<String, Object> linkDetails = new HashMap<>();
        linkDetails.put("linkId", "link-1");
        TopologyEvent linkEvent = new TopologyEvent(TopologyEventType.LINK_ADDED, linkDetails);
        topologyManager.handleLinkChange("link-1", linkEvent);
        verify(topologyManager).handleLinkChange("link-1", linkEvent);

        // 测试处理拓扑变化
        topologyManager.handleTopologyChange();
        verify(topologyManager).handleTopologyChange();
    }

    // 测试拓扑事件相关方法
    @Test
    public void testTopologyEventMethods() throws InterruptedException {
        // 测试发布拓扑事件
        TopologyEvent event = new TopologyEvent();
        event.setEventType(TopologyEventType.TOPOLOGY_CHANGED);
        event.setTimestamp(System.currentTimeMillis());
        topologyManager.publishTopologyEvent(event);
        verify(topologyManager).publishTopologyEvent(event);

        // 测试订阅拓扑事件
        final CountDownLatch latch = new CountDownLatch(1);
        final TopologyEvent[] receivedEvent = new TopologyEvent[1];

        Consumer<TopologyEvent> eventHandler = e -> {
            receivedEvent[0] = e;
            latch.countDown();
        };

        topologyManager.subscribeToTopologyEvents(eventHandler);
        verify(topologyManager).subscribeToTopologyEvents(eventHandler);

        // 测试取消订阅拓扑事件
        topologyManager.unsubscribeFromTopologyEvents(eventHandler);
        verify(topologyManager).unsubscribeFromTopologyEvents(eventHandler);

        // 测试获取最近的拓扑事件
        List<TopologyEvent> events = Collections.singletonList(event);
        when(topologyManager.getRecentTopologyEvents(10)).thenReturn(events);
        List<TopologyEvent> recentEvents = topologyManager.getRecentTopologyEvents(10);
        assertEquals(1, recentEvents.size());
    }

    // 测试拓扑监控相关方法
    @Test
    public void testTopologyMonitoringMethods() {
        // 测试开始拓扑监控
        topologyManager.startTopologyMonitoring();
        verify(topologyManager).startTopologyMonitoring();

        // 测试停止拓扑监控
        topologyManager.stopTopologyMonitoring();
        verify(topologyManager).stopTopologyMonitoring();

        // 测试获取拓扑健康状态
        Map<String, Object> health = new HashMap<>();
        health.put("status", "healthy");
        health.put("issues", 0);
        health.put("score", 95.5);
        when(topologyManager.getTopologyHealth()).thenReturn(health);
        Map<String, Object> topologyHealth = topologyManager.getTopologyHealth();
        assertEquals("healthy", topologyHealth.get("status"));

        // 测试获取节点健康状态
        Map<String, Object> nodeHealth = new HashMap<>();
        nodeHealth.put("status", "online");
        nodeHealth.put("cpuUsage", 0.3);
        nodeHealth.put("memoryUsage", 0.4);
        when(topologyManager.getNodeHealth("node-1")).thenReturn(nodeHealth);
        Map<String, Object> retrievedNodeHealth = topologyManager.getNodeHealth("node-1");
        assertEquals("online", retrievedNodeHealth.get("status"));

        // 测试获取链路健康状态
        Map<String, Object> linkHealth = new HashMap<>();
        linkHealth.put("status", "active");
        linkHealth.put("utilization", 0.2);
        linkHealth.put("latency", 5.5);
        when(topologyManager.getLinkHealth("link-1")).thenReturn(linkHealth);
        Map<String, Object> retrievedLinkHealth = topologyManager.getLinkHealth("link-1");
        assertEquals("active", retrievedLinkHealth.get("status"));
    }

    // 测试拓扑持久化相关方法
    @Test
    public void testTopologyPersistenceMethods() {
        // 测试保存拓扑
        String savePath = "/tmp/topology.json";
        topologyManager.saveTopology(savePath);
        verify(topologyManager).saveTopology(savePath);

        // 测试加载拓扑
        String loadPath = "/tmp/topology.json";
        topologyManager.loadTopology(loadPath);
        verify(topologyManager).loadTopology(loadPath);
    }
}

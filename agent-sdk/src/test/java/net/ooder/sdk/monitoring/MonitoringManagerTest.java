package net.ooder.sdk.monitoring;

import net.ooder.sdk.monitoring.Alert;
import net.ooder.sdk.monitoring.AlertLevel;
import net.ooder.sdk.monitoring.AlertRule;
import net.ooder.sdk.monitoring.HealthStatus;
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
public class MonitoringManagerTest {

    @Mock
    private MonitoringManager monitoringManager;

    private String testTerminalId;
    private String testLinkId;
    private AlertRule testAlertRule;
    private Alert testAlert;
    private Consumer<Alert> testAlertHandler;

    @Before
    public void setUp() {
        // 初始化测试ID
        testTerminalId = "terminal-1";
        testLinkId = "link-1";

        // 初始化测试告警规则
        testAlertRule = new AlertRule();
        testAlertRule.setRuleId("rule-1");
        testAlertRule.setName("High CPU Usage");
        testAlertRule.setDescription("Alert when CPU usage exceeds 80%");
        testAlertRule.setMetric("cpuUsage");
        testAlertRule.setCondition(">=");
        testAlertRule.setThreshold(80.0);
        testAlertRule.setLevel(AlertLevel.WARNING);
        testAlertRule.setEnabled(true);

        // 初始化测试告警
        testAlert = new Alert();
        testAlert.setAlertId("alert-1");
        testAlert.setRuleId("rule-1");
        testAlert.setLevel(AlertLevel.WARNING);
        testAlert.setMessage("CPU usage exceeded 80%");
        testAlert.setType("cpuUsage");
        testAlert.setTimestamp(System.currentTimeMillis());
        testAlert.setDetails(Collections.singletonMap("host", "test-server"));

        // 初始化测试告警处理器
        testAlertHandler = alert -> {
            System.out.println("Received alert: " + alert.getMessage());
        };
    }

    // 测试监控启动和停止方法
    @Test
    public void testMonitoringStartStopMethods() throws ExecutionException, InterruptedException {
        // 测试开始监控
        when(monitoringManager.startMonitoring()).thenReturn(CompletableFuture.completedFuture(true));
        boolean started = monitoringManager.startMonitoring().get();
        assertTrue(started);

        // 测试停止监控
        when(monitoringManager.stopMonitoring()).thenReturn(CompletableFuture.completedFuture(true));
        boolean stopped = monitoringManager.stopMonitoring().get();
        assertTrue(stopped);

        // 测试监控运行状态
        when(monitoringManager.isMonitoringRunning()).thenReturn(true);
        assertTrue(monitoringManager.isMonitoringRunning());

        when(monitoringManager.isMonitoringRunning()).thenReturn(false);
        assertFalse(monitoringManager.isMonitoringRunning());
    }

    // 测试系统指标采集相关方法
    @Test
    public void testSystemMetricsCollectionMethods() throws ExecutionException, InterruptedException {
        // 测试采集系统指标
        Map<String, Object> systemMetrics = new HashMap<>();
        systemMetrics.put("cpuUsage", 35.5);
        systemMetrics.put("memoryUsage", 45.0);
        systemMetrics.put("diskUsage", 60.0);
        systemMetrics.put("uptime", 86400);
        systemMetrics.put("loadAverage", 1.2);
        when(monitoringManager.collectSystemMetrics()).thenReturn(CompletableFuture.completedFuture(systemMetrics));
        Map<String, Object> collectedSystemMetrics = monitoringManager.collectSystemMetrics().get();
        assertNotNull(collectedSystemMetrics);
        assertEquals(35.5, collectedSystemMetrics.get("cpuUsage"));
        assertEquals(45.0, collectedSystemMetrics.get("memoryUsage"));

        // 测试采集JVM指标
        Map<String, Object> jvmMetrics = new HashMap<>();
        jvmMetrics.put("heapUsed", 256);
        jvmMetrics.put("heapMax", 512);
        jvmMetrics.put("nonHeapUsed", 64);
        jvmMetrics.put("threadCount", 50);
        jvmMetrics.put("gcCount", 10);
        when(monitoringManager.collectJvmMetrics()).thenReturn(CompletableFuture.completedFuture(jvmMetrics));
        Map<String, Object> collectedJvmMetrics = monitoringManager.collectJvmMetrics().get();
        assertNotNull(collectedJvmMetrics);
        assertEquals(256, collectedJvmMetrics.get("heapUsed"));
        assertEquals(512, collectedJvmMetrics.get("heapMax"));

        // 测试采集进程指标
        Map<String, Object> processMetrics = new HashMap<>();
        processMetrics.put("pid", 12345);
        processMetrics.put("cpuUsage", 20.5);
        processMetrics.put("memoryUsage", 128);
        processMetrics.put("threadCount", 25);
        processMetrics.put("openFiles", 100);
        when(monitoringManager.collectProcessMetrics()).thenReturn(CompletableFuture.completedFuture(processMetrics));
        Map<String, Object> collectedProcessMetrics = monitoringManager.collectProcessMetrics().get();
        assertNotNull(collectedProcessMetrics);
        assertEquals(12345, collectedProcessMetrics.get("pid"));
        assertEquals(20.5, collectedProcessMetrics.get("cpuUsage"));

        // 测试采集所有系统指标
        Map<String, Object> allSystemMetrics = new HashMap<>();
        allSystemMetrics.put("system", systemMetrics);
        allSystemMetrics.put("jvm", jvmMetrics);
        allSystemMetrics.put("process", processMetrics);
        when(monitoringManager.collectAllSystemMetrics()).thenReturn(CompletableFuture.completedFuture(allSystemMetrics));
        Map<String, Object> collectedAllSystemMetrics = monitoringManager.collectAllSystemMetrics().get();
        assertNotNull(collectedAllSystemMetrics);
        assertNotNull(collectedAllSystemMetrics.get("system"));
        assertNotNull(collectedAllSystemMetrics.get("jvm"));
        assertNotNull(collectedAllSystemMetrics.get("process"));
    }

    // 测试网络状态监控相关方法
    @Test
    public void testNetworkStatusMonitoringMethods() throws ExecutionException, InterruptedException {
        // 测试采集网络指标
        Map<String, Object> networkMetrics = new HashMap<>();
        networkMetrics.put("bytesSent", 1024);
        networkMetrics.put("bytesReceived", 2048);
        networkMetrics.put("packetsSent", 100);
        networkMetrics.put("packetsReceived", 150);
        networkMetrics.put("errorsIn", 0);
        networkMetrics.put("errorsOut", 0);
        when(monitoringManager.collectNetworkMetrics()).thenReturn(CompletableFuture.completedFuture(networkMetrics));
        Map<String, Object> collectedNetworkMetrics = monitoringManager.collectNetworkMetrics().get();
        assertNotNull(collectedNetworkMetrics);
        assertEquals(1024, collectedNetworkMetrics.get("bytesSent"));
        assertEquals(2048, collectedNetworkMetrics.get("bytesReceived"));

        // 测试采集链路指标
        Map<String, Object> linkMetrics = new HashMap<>();
        linkMetrics.put("latency", 10.5);
        linkMetrics.put("bandwidth", 1000);
        linkMetrics.put("packetLoss", 0.1);
        linkMetrics.put("utilization", 35.0);
        when(monitoringManager.collectLinkMetrics(testLinkId)).thenReturn(CompletableFuture.completedFuture(linkMetrics));
        Map<String, Object> collectedLinkMetrics = monitoringManager.collectLinkMetrics(testLinkId).get();
        assertNotNull(collectedLinkMetrics);
        assertEquals(10.5, collectedLinkMetrics.get("latency"));
        assertEquals(1000, collectedLinkMetrics.get("bandwidth"));

        // 测试采集所有链路指标
        Map<String, Object> allLinkMetrics = new HashMap<>();
        allLinkMetrics.put(testLinkId, linkMetrics);
        when(monitoringManager.collectAllLinkMetrics()).thenReturn(CompletableFuture.completedFuture(allLinkMetrics));
        Map<String, Object> collectedAllLinkMetrics = monitoringManager.collectAllLinkMetrics().get();
        assertNotNull(collectedAllLinkMetrics);
        assertNotNull(collectedAllLinkMetrics.get(testLinkId));

        // 测试采集终端指标
        Map<String, Object> terminalMetrics = new HashMap<>();
        terminalMetrics.put("cpuUsage", 45.5);
        terminalMetrics.put("memoryUsage", 60.0);
        terminalMetrics.put("diskUsage", 70.0);
        terminalMetrics.put("networkIn", 512);
        terminalMetrics.put("networkOut", 256);
        when(monitoringManager.collectTerminalMetrics(testTerminalId)).thenReturn(CompletableFuture.completedFuture(terminalMetrics));
        Map<String, Object> collectedTerminalMetrics = monitoringManager.collectTerminalMetrics(testTerminalId).get();
        assertNotNull(collectedTerminalMetrics);
        assertEquals(45.5, collectedTerminalMetrics.get("cpuUsage"));
        assertEquals(60.0, collectedTerminalMetrics.get("memoryUsage"));

        // 测试采集所有终端指标
        Map<String, Object> allTerminalMetrics = new HashMap<>();
        allTerminalMetrics.put(testTerminalId, terminalMetrics);
        when(monitoringManager.collectAllTerminalMetrics()).thenReturn(CompletableFuture.completedFuture(allTerminalMetrics));
        Map<String, Object> collectedAllTerminalMetrics = monitoringManager.collectAllTerminalMetrics().get();
        assertNotNull(collectedAllTerminalMetrics);
        assertNotNull(collectedAllTerminalMetrics.get(testTerminalId));
    }

    // 测试性能分析相关方法
    @Test
    public void testPerformanceAnalysisMethods() throws ExecutionException, InterruptedException {
        // 测试分析系统性能
        Map<String, Object> systemPerformance = new HashMap<>();
        systemPerformance.put("cpuUsage", 45.5);
        systemPerformance.put("memoryUsage", 60.0);
        systemPerformance.put("diskUsage", 70.0);
        systemPerformance.put("networkUsage", 35.0);
        systemPerformance.put("loadAverage", 1.2);
        systemPerformance.put("uptime", 86400);
        when(monitoringManager.analyzeSystemPerformance()).thenReturn(CompletableFuture.completedFuture(systemPerformance));
        Map<String, Object> analyzedSystemPerformance = monitoringManager.analyzeSystemPerformance().get();
        assertNotNull(analyzedSystemPerformance);
        assertEquals(45.5, analyzedSystemPerformance.get("cpuUsage"));
        assertEquals(60.0, analyzedSystemPerformance.get("memoryUsage"));

        // 测试分析网络性能
        Map<String, Object> networkPerformance = new HashMap<>();
        networkPerformance.put("averageLatency", 15.5);
        networkPerformance.put("averageBandwidth", 950);
        networkPerformance.put("averagePacketLoss", 0.05);
        networkPerformance.put("totalBytesSent", 1024000);
        networkPerformance.put("totalBytesReceived", 2048000);
        when(monitoringManager.analyzeNetworkPerformance()).thenReturn(CompletableFuture.completedFuture(networkPerformance));
        Map<String, Object> analyzedNetworkPerformance = monitoringManager.analyzeNetworkPerformance().get();
        assertNotNull(analyzedNetworkPerformance);
        assertEquals(15.5, analyzedNetworkPerformance.get("averageLatency"));
        assertEquals(950, analyzedNetworkPerformance.get("averageBandwidth"));

        // 测试分析终端性能
        Map<String, Object> terminalPerformance = new HashMap<>();
        terminalPerformance.put("cpuUsage", 45.5);
        terminalPerformance.put("memoryUsage", 60.0);
        terminalPerformance.put("diskUsage", 70.0);
        terminalPerformance.put("networkIn", 512);
        terminalPerformance.put("networkOut", 256);
        terminalPerformance.put("uptime", 3600);
        when(monitoringManager.analyzeTerminalPerformance(testTerminalId)).thenReturn(CompletableFuture.completedFuture(terminalPerformance));
        Map<String, Object> analyzedTerminalPerformance = monitoringManager.analyzeTerminalPerformance(testTerminalId).get();
        assertNotNull(analyzedTerminalPerformance);
        assertEquals(45.5, analyzedTerminalPerformance.get("cpuUsage"));
        assertEquals(60.0, analyzedTerminalPerformance.get("memoryUsage"));

        // 测试分析所有终端性能
        Map<String, Object> allTerminalPerformance = new HashMap<>();
        allTerminalPerformance.put(testTerminalId, terminalPerformance);
        when(monitoringManager.analyzeAllTerminalPerformance()).thenReturn(CompletableFuture.completedFuture(allTerminalPerformance));
        Map<String, Object> analyzedAllTerminalPerformance = monitoringManager.analyzeAllTerminalPerformance().get();
        assertNotNull(analyzedAllTerminalPerformance);
        assertNotNull(analyzedAllTerminalPerformance.get(testTerminalId));

        // 测试查找性能瓶颈
        List<String> bottlenecks = Arrays.asList("high disk usage", "network congestion");
        when(monitoringManager.findPerformanceBottlenecks()).thenReturn(CompletableFuture.completedFuture(bottlenecks));
        List<String> foundBottlenecks = monitoringManager.findPerformanceBottlenecks().get();
        assertNotNull(foundBottlenecks);
        assertEquals(2, foundBottlenecks.size());
        assertEquals("high disk usage", foundBottlenecks.get(0));
        assertEquals("network congestion", foundBottlenecks.get(1));
    }

    // 测试告警管理相关方法
    @Test
    public void testAlertManagementMethods() throws ExecutionException, InterruptedException {
        // 测试添加告警规则
        when(monitoringManager.addAlertRule(testAlertRule)).thenReturn(CompletableFuture.completedFuture(true));
        boolean ruleAdded = monitoringManager.addAlertRule(testAlertRule).get();
        assertTrue(ruleAdded);

        // 测试移除告警规则
        when(monitoringManager.removeAlertRule("rule-1")).thenReturn(CompletableFuture.completedFuture(true));
        boolean ruleRemoved = monitoringManager.removeAlertRule("rule-1").get();
        assertTrue(ruleRemoved);

        // 测试获取告警规则列表
        List<AlertRule> rules = Collections.singletonList(testAlertRule);
        when(monitoringManager.getAlertRules()).thenReturn(CompletableFuture.completedFuture(rules));
        List<AlertRule> retrievedRules = monitoringManager.getAlertRules().get();
        assertNotNull(retrievedRules);
        assertEquals(1, retrievedRules.size());
        assertEquals("rule-1", retrievedRules.get(0).getRuleId());

        // 测试获取单个告警规则
        when(monitoringManager.getAlertRule("rule-1")).thenReturn(CompletableFuture.completedFuture(testAlertRule));
        AlertRule retrievedRule = monitoringManager.getAlertRule("rule-1").get();
        assertNotNull(retrievedRule);
        assertEquals("rule-1", retrievedRule.getRuleId());
        assertEquals("High CPU Usage", retrievedRule.getName());

        // 测试更新告警规则
        AlertRule updatedRule = testAlertRule;
        updatedRule.setDescription("Updated alert rule");
        updatedRule.setThreshold(85.0);
        when(monitoringManager.updateAlertRule("rule-1", updatedRule)).thenReturn(CompletableFuture.completedFuture(true));
        boolean ruleUpdated = monitoringManager.updateAlertRule("rule-1", updatedRule).get();
        assertTrue(ruleUpdated);
    }

    // 测试告警通知相关方法
    @Test
    public void testAlertNotificationMethods() throws ExecutionException, InterruptedException {
        // 测试发送告警
        when(monitoringManager.sendAlert(testAlert)).thenReturn(CompletableFuture.completedFuture(true));
        boolean alertSent = monitoringManager.sendAlert(testAlert).get();
        assertTrue(alertSent);

        // 测试订阅告警
        monitoringManager.subscribeToAlerts(testAlertHandler);
        verify(monitoringManager).subscribeToAlerts(testAlertHandler);

        // 测试取消订阅告警
        monitoringManager.unsubscribeFromAlerts(testAlertHandler);
        verify(monitoringManager).unsubscribeFromAlerts(testAlertHandler);

        // 测试按级别订阅告警
        monitoringManager.subscribeToAlertsByLevel(AlertLevel.WARNING, testAlertHandler);
        verify(monitoringManager).subscribeToAlertsByLevel(AlertLevel.WARNING, testAlertHandler);

        // 测试按级别取消订阅告警
        monitoringManager.unsubscribeFromAlertsByLevel(AlertLevel.WARNING, testAlertHandler);
        verify(monitoringManager).unsubscribeFromAlertsByLevel(AlertLevel.WARNING, testAlertHandler);
    }

    // 测试告警历史相关方法
    @Test
    public void testAlertHistoryMethods() throws ExecutionException, InterruptedException {
        // 测试获取告警历史
        List<Alert> alerts = Collections.singletonList(testAlert);
        when(monitoringManager.getAlertHistory(10)).thenReturn(CompletableFuture.completedFuture(alerts));
        List<Alert> retrievedAlerts = monitoringManager.getAlertHistory(10).get();
        assertNotNull(retrievedAlerts);
        assertEquals(1, retrievedAlerts.size());
        assertEquals("alert-1", retrievedAlerts.get(0).getAlertId());

        // 测试按级别获取告警
        when(monitoringManager.getAlertsByLevel(AlertLevel.WARNING, 10)).thenReturn(CompletableFuture.completedFuture(alerts));
        List<Alert> warningAlerts = monitoringManager.getAlertsByLevel(AlertLevel.WARNING, 10).get();
        assertNotNull(warningAlerts);
        assertEquals(1, warningAlerts.size());
        assertEquals(AlertLevel.WARNING, warningAlerts.get(0).getLevel());

        // 测试按类型获取告警
        when(monitoringManager.getAlertsByType("cpuUsage", 10)).thenReturn(CompletableFuture.completedFuture(alerts));
        List<Alert> cpuAlerts = monitoringManager.getAlertsByType("cpuUsage", 10).get();
        assertNotNull(cpuAlerts);
        assertEquals(1, cpuAlerts.size());
        assertEquals("cpuUsage", cpuAlerts.get(0).getType());

        // 测试获取按级别统计的告警数
        Map<String, Long> levelCounts = new HashMap<>();
        levelCounts.put("INFO", 10L);
        levelCounts.put("WARNING", 5L);
        levelCounts.put("ERROR", 2L);
        levelCounts.put("CRITICAL", 1L);
        when(monitoringManager.getAlertCountsByLevel()).thenReturn(CompletableFuture.completedFuture(levelCounts));
        Map<String, Long> retrievedLevelCounts = monitoringManager.getAlertCountsByLevel().get();
        assertNotNull(retrievedLevelCounts);
        assertEquals(Long.valueOf(10), retrievedLevelCounts.get("INFO"));
        assertEquals(Long.valueOf(5), retrievedLevelCounts.get("WARNING"));

        // 测试获取按类型统计的告警数
        Map<String, Long> typeCounts = new HashMap<>();
        typeCounts.put("cpuUsage", 5L);
        typeCounts.put("memoryUsage", 3L);
        typeCounts.put("diskUsage", 2L);
        typeCounts.put("networkUsage", 1L);
        when(monitoringManager.getAlertCountsByType()).thenReturn(CompletableFuture.completedFuture(typeCounts));
        Map<String, Long> retrievedTypeCounts = monitoringManager.getAlertCountsByType().get();
        assertNotNull(retrievedTypeCounts);
        assertEquals(Long.valueOf(5), retrievedTypeCounts.get("cpuUsage"));
        assertEquals(Long.valueOf(3), retrievedTypeCounts.get("memoryUsage"));
    }

    // 测试监控配置相关方法
    @Test
    public void testMonitoringConfigurationMethods() throws ExecutionException, InterruptedException {
        // 测试设置监控间隔
        long interval = 60;
        TimeUnit unit = TimeUnit.SECONDS;
        when(monitoringManager.setMonitoringInterval(interval, unit)).thenReturn(CompletableFuture.completedFuture(true));
        boolean intervalSet = monitoringManager.setMonitoringInterval(interval, unit).get();
        assertTrue(intervalSet);

        // 测试获取监控间隔
        when(monitoringManager.getMonitoringInterval()).thenReturn(CompletableFuture.completedFuture(interval));
        long retrievedInterval = monitoringManager.getMonitoringInterval().get();
        assertEquals(interval, retrievedInterval);

        // 测试设置指标保留时间
        int retentionDays = 7;
        when(monitoringManager.setMetricsRetention(retentionDays)).thenReturn(CompletableFuture.completedFuture(true));
        boolean retentionSet = monitoringManager.setMetricsRetention(retentionDays).get();
        assertTrue(retentionSet);

        // 测试获取指标保留时间
        when(monitoringManager.getMetricsRetention()).thenReturn(CompletableFuture.completedFuture(retentionDays));
        int retrievedRetention = monitoringManager.getMetricsRetention().get();
        assertEquals(retentionDays, retrievedRetention);
    }

    // 测试监控数据存储和查询相关方法
    @Test
    public void testMonitoringDataStorageMethods() throws ExecutionException, InterruptedException {
        // 测试存储指标
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("cpuUsage", 45.5);
        metrics.put("memoryUsage", 60.0);
        metrics.put("diskUsage", 70.0);
        metrics.put("timestamp", System.currentTimeMillis());
        when(monitoringManager.storeMetrics(metrics)).thenReturn(CompletableFuture.completedFuture(true));
        boolean metricsStored = monitoringManager.storeMetrics(metrics).get();
        assertTrue(metricsStored);

        // 测试查询指标
        long startTime = System.currentTimeMillis() - 3600000;
        long endTime = System.currentTimeMillis();
        List<Map<String, Object>> metricsList = Collections.singletonList(metrics);
        when(monitoringManager.queryMetrics("cpuUsage", startTime, endTime)).thenReturn(CompletableFuture.completedFuture(metricsList));
        List<Map<String, Object>> queriedMetrics = monitoringManager.queryMetrics("cpuUsage", startTime, endTime).get();
        assertNotNull(queriedMetrics);
        assertEquals(1, queriedMetrics.size());
        assertEquals(45.5, queriedMetrics.get(0).get("cpuUsage"));

        // 测试获取指标统计信息
        Map<String, Object> metricStats = new HashMap<>();
        metricStats.put("min", 30.0);
        metricStats.put("max", 60.0);
        metricStats.put("avg", 45.0);
        metricStats.put("sum", 450.0);
        metricStats.put("count", 10L);
        when(monitoringManager.getMetricStatistics("cpuUsage", startTime, endTime)).thenReturn(CompletableFuture.completedFuture(metricStats));
        Map<String, Object> retrievedMetricStats = monitoringManager.getMetricStatistics("cpuUsage", startTime, endTime).get();
        assertNotNull(retrievedMetricStats);
        assertEquals(30.0, retrievedMetricStats.get("min"));
        assertEquals(60.0, retrievedMetricStats.get("max"));
        assertEquals(45.0, retrievedMetricStats.get("avg"));
    }

    // 测试健康检查相关方法
    @Test
    public void testHealthCheckMethods() throws ExecutionException, InterruptedException {
        // 测试检查系统健康状态
        HealthStatus systemHealth = new HealthStatus();
        systemHealth.setStatus(HealthStatus.Status.HEALTHY);
        systemHealth.setLevel(HealthStatus.HealthLevel.EXCELLENT);
        systemHealth.setTimestamp(System.currentTimeMillis());
        systemHealth.setDetails(Collections.singletonMap("components", Arrays.asList("cpu", "memory", "disk", "network")));
        when(monitoringManager.checkSystemHealth()).thenReturn(CompletableFuture.completedFuture(systemHealth));
        HealthStatus systemHealthStatus = monitoringManager.checkSystemHealth().get();
        assertNotNull(systemHealthStatus);
        assertEquals(HealthStatus.Status.HEALTHY, systemHealthStatus.getStatus());

        // 测试检查网络健康状态
        HealthStatus networkHealth = new HealthStatus();
        networkHealth.setStatus(HealthStatus.Status.HEALTHY);
        networkHealth.setLevel(HealthStatus.HealthLevel.GOOD);
        networkHealth.setTimestamp(System.currentTimeMillis());
        networkHealth.setDetails(Collections.singletonMap("links", Arrays.asList(testLinkId)));
        when(monitoringManager.checkNetworkHealth()).thenReturn(CompletableFuture.completedFuture(networkHealth));
        HealthStatus networkHealthStatus = monitoringManager.checkNetworkHealth().get();
        assertNotNull(networkHealthStatus);
        assertEquals(HealthStatus.Status.HEALTHY, networkHealthStatus.getStatus());

        // 测试检查终端健康状态
        HealthStatus terminalHealth = new HealthStatus();
        terminalHealth.setStatus(HealthStatus.Status.HEALTHY);
        terminalHealth.setLevel(HealthStatus.HealthLevel.GOOD);
        terminalHealth.setTimestamp(System.currentTimeMillis());
        terminalHealth.setDetails(Collections.singletonMap("terminalId", testTerminalId));
        when(monitoringManager.checkTerminalHealth(testTerminalId)).thenReturn(CompletableFuture.completedFuture(terminalHealth));
        HealthStatus terminalHealthStatus = monitoringManager.checkTerminalHealth(testTerminalId).get();
        assertNotNull(terminalHealthStatus);
        assertEquals(HealthStatus.Status.HEALTHY, terminalHealthStatus.getStatus());

        // 测试检查所有终端健康状态
        Map<String, HealthStatus> allTerminalHealth = new HashMap<>();
        allTerminalHealth.put(testTerminalId, terminalHealth);
        when(monitoringManager.checkAllTerminalHealth()).thenReturn(CompletableFuture.completedFuture(allTerminalHealth));
        Map<String, HealthStatus> allTerminalHealthStatus = monitoringManager.checkAllTerminalHealth().get();
        assertNotNull(allTerminalHealthStatus);
        assertNotNull(allTerminalHealthStatus.get(testTerminalId));
        assertEquals("HEALTHY", allTerminalHealthStatus.get(testTerminalId).getStatus());
    }

    // 测试告警处理器
    @Test
    public void testAlertHandler() throws InterruptedException {
        // 测试告警处理器
        final CountDownLatch latch = new CountDownLatch(1);
        final Alert[] receivedAlert = new Alert[1];

        Consumer<Alert> alertHandler = alert -> {
            receivedAlert[0] = alert;
            latch.countDown();
        };

        // 模拟告警处理
        monitoringManager.subscribeToAlerts(alertHandler);
        monitoringManager.sendAlert(testAlert);

        // 等待告警处理
        boolean alertReceived = latch.await(1, TimeUnit.SECONDS);
        if (alertReceived) {
            assertNotNull(receivedAlert[0]);
            assertEquals("alert-1", receivedAlert[0].getAlertId());
            assertEquals(AlertLevel.WARNING, receivedAlert[0].getLevel());
        }

        // 清理
        monitoringManager.unsubscribeFromAlerts(alertHandler);
    }

    // 测试告警规则启用/禁用
    @Test
    public void testAlertRuleEnableDisable() {
        // 测试启用规则
        testAlertRule.setEnabled(true);
        assertTrue(testAlertRule.isEnabled());

        // 测试禁用规则
        testAlertRule.setEnabled(false);
        assertFalse(testAlertRule.isEnabled());
    }

    // 测试健康状态评估
    @Test
    public void testHealthStatusEvaluation() {
        // 测试健康状态
        HealthStatus healthStatus = new HealthStatus();
        healthStatus.setStatus(HealthStatus.Status.HEALTHY);
        healthStatus.setLevel(HealthStatus.HealthLevel.EXCELLENT);
        healthStatus.setTimestamp(System.currentTimeMillis());

        assertEquals(HealthStatus.Status.HEALTHY, healthStatus.getStatus());
        assertEquals(HealthStatus.HealthLevel.EXCELLENT, healthStatus.getLevel());
        assertTrue(healthStatus.getTimestamp() > System.currentTimeMillis() - 1000);

        // 测试降级状态
        healthStatus.setStatus(HealthStatus.Status.DEGRADED);
        healthStatus.setLevel(HealthStatus.HealthLevel.FAIR);
        assertEquals(HealthStatus.Status.DEGRADED, healthStatus.getStatus());
        assertEquals(HealthStatus.HealthLevel.FAIR, healthStatus.getLevel());

        // 测试不健康状态
        healthStatus.setStatus(HealthStatus.Status.UNHEALTHY);
        healthStatus.setLevel(HealthStatus.HealthLevel.POOR);
        assertEquals(HealthStatus.Status.UNHEALTHY, healthStatus.getStatus());
        assertEquals(HealthStatus.HealthLevel.POOR, healthStatus.getLevel());

        // 测试严重状态
        healthStatus.setStatus(HealthStatus.Status.CRITICAL);
        healthStatus.setLevel(HealthStatus.HealthLevel.CRITICAL);
        assertEquals(HealthStatus.Status.CRITICAL, healthStatus.getStatus());
        assertEquals(HealthStatus.HealthLevel.CRITICAL, healthStatus.getLevel());
    }
}

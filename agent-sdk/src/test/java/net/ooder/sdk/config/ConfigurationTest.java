package net.ooder.sdk.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
@ActiveProfiles("test")
public class ConfigurationTest {

    @Autowired
    private NetworkProperties networkProperties;

    @Autowired
    private RetryProperties retryProperties;

    @Autowired
    private PortProperties portProperties;

    @Autowired
    private MonitoringProperties monitoringProperties;

    @Autowired
    private PerformanceProperties performanceProperties;

    @Autowired
    private AgentProperties agentProperties;

    @Autowired
    private TerminalDiscoveryProperties terminalDiscoveryProperties;

    @Autowired
    private AgentConfigProperties agentConfigProperties;

    @Test
    public void testNetworkProperties() {
        assertNotNull(networkProperties);
        assertEquals("255.255.255.255", networkProperties.getBroadcastAddress());
        assertEquals(0, networkProperties.getDefaultPort());
        assertEquals(8192, networkProperties.getBufferSize());
        assertEquals(65536, networkProperties.getMaxPacketSize());
        assertEquals(30000, networkProperties.getTimeout());
        assertEquals(5000, networkProperties.getAckTimeout());
        assertTrue(networkProperties.isSocketReuse());
        assertTrue(networkProperties.isSocketBroadcast());
    }

    @Test
    public void testRetryProperties() {
        assertNotNull(retryProperties);
        assertEquals(3, retryProperties.getMaxRetries());
        assertEquals(1000, retryProperties.getDelayBase());
        assertEquals(RetryProperties.RetryStrategy.EXPONENTIAL, retryProperties.getStrategy());
        assertTrue(retryProperties.isJitterEnabled());
    }

    @Test
    public void testPortProperties() {
        assertNotNull(portProperties);
        assertEquals(PortProperties.AllocationStrategy.DYNAMIC, portProperties.getAllocationStrategy());
        assertEquals(8080, portProperties.getLocalStart());
        assertEquals(8192, portProperties.getLocalEnd());
        assertEquals(9000, portProperties.getLanStart());
        assertEquals(9100, portProperties.getLanEnd());
        assertEquals(10000, portProperties.getIntranetStart());
        assertEquals(10100, portProperties.getIntranetEnd());
        assertEquals(1024, portProperties.getGlobalStart());
        assertEquals(65535, portProperties.getGlobalEnd());
        assertTrue(portProperties.isSmartAllocationEnabled());
        assertEquals(1000, portProperties.getHistorySize());
        assertEquals(3600000, portProperties.getCleanupIntervalMs());
    }

    @Test
    public void testMonitoringProperties() {
        assertNotNull(monitoringProperties);
        assertTrue(monitoringProperties.isEnabled());
        assertTrue(monitoringProperties.isMetricsCollectionEnabled());
        assertEquals(5000, monitoringProperties.getMetricsCollectionIntervalMs());
        assertTrue(monitoringProperties.isAlertEnabled());
        assertEquals(10, monitoringProperties.getErrorThreshold());
        assertEquals(5000, monitoringProperties.getLatencyThresholdMs());
        assertEquals(1000000, monitoringProperties.getThroughputThresholdBytes());
        assertTrue(monitoringProperties.isReportingEnabled());
        assertEquals(60000, monitoringProperties.getReportingIntervalMs());
        assertFalse(monitoringProperties.isIntelligentMonitoringEnabled());
        assertEquals(3.0, monitoringProperties.getAnomalyThreshold());
        assertEquals(10, monitoringProperties.getPredictionHorizon());
    }

    @Test
    public void testPerformanceProperties() {
        assertNotNull(performanceProperties);
        assertTrue(performanceProperties.isOptimizerEnabled());
        assertTrue(performanceProperties.isCompressionEnabled());
        assertEquals(1024, performanceProperties.getCompressionThreshold());
        assertTrue(performanceProperties.isAdaptiveBuffer());
        assertTrue(performanceProperties.isAdaptiveTimeout());
        assertTrue(performanceProperties.isConnectionPoolEnabled());
        assertEquals(10, performanceProperties.getConnectionPoolSize());
        assertEquals(8, performanceProperties.getThreadPoolSize());
        assertTrue(performanceProperties.isUseNio());
    }

    @Test
    public void testAgentProperties() {
        assertNotNull(agentProperties);
        assertEquals(9000, agentProperties.getEndagentDefaultPort());
        assertEquals(8080, agentProperties.getRouteagentDefaultPort());
        assertEquals(7070, agentProperties.getMcpagentDefaultPort());
    }

    @Test
    public void testTerminalDiscoveryProperties() {
        assertNotNull(terminalDiscoveryProperties);
        assertEquals(30000, terminalDiscoveryProperties.getScanInterval());
    }

    @Test
    public void testAgentConfigProperties() {
        assertNotNull(agentConfigProperties);
        assertEquals(9001, agentConfigProperties.getUdpPort());
        assertEquals(65535, agentConfigProperties.getUdpBufferSize());
        assertEquals(5000, agentConfigProperties.getUdpTimeout());
        assertEquals(65507, agentConfigProperties.getUdpMaxPacketSize());
        assertEquals(30000, agentConfigProperties.getHeartbeatInterval());
        assertEquals(90000, agentConfigProperties.getHeartbeatTimeout());
        assertEquals(3, agentConfigProperties.getHeartbeatLossThreshold());
        assertEquals(5, agentConfigProperties.getRetryMaxRetries());
        assertEquals(1000, agentConfigProperties.getRetryInitialInterval());
        assertEquals(30000, agentConfigProperties.getRetryMaxInterval());
        assertEquals(2.0, agentConfigProperties.getRetryBackoffFactor());
    }

    @Test
    public void testAllPropertiesInjected() {
        // 验证所有配置类都能正确注入
        assertNotNull(networkProperties);
        assertNotNull(retryProperties);
        assertNotNull(portProperties);
        assertNotNull(monitoringProperties);
        assertNotNull(performanceProperties);
        assertNotNull(agentProperties);
        assertNotNull(terminalDiscoveryProperties);
        assertNotNull(agentConfigProperties);
    }
}

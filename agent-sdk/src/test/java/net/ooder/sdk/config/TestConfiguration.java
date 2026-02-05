package net.ooder.sdk.config;

import net.ooder.sdk.async.AsyncExecutorService;
import net.ooder.sdk.async.AsyncConfiguration;
import net.ooder.sdk.network.udp.PortManager;
import net.ooder.sdk.network.udp.monitoring.UDPMetricsCollector;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Profile("test")
@ComponentScan(
    basePackages = "net.ooder.sdk",
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {
            NetworkProperties.class,
            RetryProperties.class,
            PerformanceProperties.class,
            MonitoringProperties.class,
            AgentProperties.class,
            PortProperties.class,
            TerminalDiscoveryProperties.class,
            AgentConfigProperties.class,
            net.ooder.sdk.network.udp.UDPSDK.class,
            net.ooder.sdk.async.AsyncExecutorService.class,
            net.ooder.sdk.network.udp.monitoring.UDPMetricsCollector.class
        }
    )
)
public class TestConfiguration {
    
    @Bean
    public NetworkProperties networkProperties() {
        NetworkProperties properties = new NetworkProperties();
        properties.setBufferSize(8192);
        properties.setMaxPacketSize(65536);
        properties.setTimeout(30000);
        properties.setAckTimeout(5000);
        properties.setSocketReuse(true);
        properties.setSocketBroadcast(true);
        properties.setBroadcastAddress("255.255.255.255");
        properties.setDefaultPort(0);
        return properties;
    }
    
    @Bean
    public RetryProperties retryProperties() {
        RetryProperties properties = new RetryProperties();
        properties.setMaxRetries(3);
        properties.setDelayBase(1000);
        properties.setStrategy(RetryProperties.RetryStrategy.EXPONENTIAL);
        properties.setJitterEnabled(true);
        return properties;
    }
    
    @Bean
    public PerformanceProperties performanceProperties() {
        PerformanceProperties properties = new PerformanceProperties();
        properties.setOptimizerEnabled(true);
        properties.setCompressionEnabled(true);
        properties.setCompressionThreshold(1024);
        properties.setAdaptiveBuffer(true);
        properties.setAdaptiveTimeout(true);
        properties.setConnectionPoolEnabled(true);
        properties.setConnectionPoolSize(10);
        properties.setThreadPoolSize(8);
        properties.setUseNio(true);
        return properties;
    }
    
    @Bean
    public MonitoringProperties monitoringProperties() {
        MonitoringProperties properties = new MonitoringProperties();
        properties.setEnabled(true);
        properties.setMetricsCollectionEnabled(true);
        properties.setMetricsCollectionIntervalMs(5000);
        properties.setAlertEnabled(true);
        properties.setErrorThreshold(10);
        properties.setLatencyThresholdMs(5000);
        properties.setThroughputThresholdBytes(1000000);
        properties.setReportingEnabled(true);
        properties.setReportingIntervalMs(60000);
        properties.setIntelligentMonitoringEnabled(false);
        properties.setAnomalyThreshold(3.0);
        properties.setPredictionHorizon(10);
        return properties;
    }
    
    @Bean
    public AgentProperties agentProperties() {
        AgentProperties properties = new AgentProperties();
        properties.setEndagentDefaultPort(9000);
        properties.setRouteagentDefaultPort(8080);
        properties.setMcpagentDefaultPort(7070);
        return properties;
    }
    
    @Bean
    public PortProperties portProperties() {
        PortProperties properties = new PortProperties();
        properties.setLocalStart(8000);
        properties.setLocalEnd(8100);
        properties.setLanStart(9000);
        properties.setLanEnd(9100);
        properties.setIntranetStart(10000);
        properties.setIntranetEnd(10100);
        properties.setGlobalStart(1024);
        properties.setGlobalEnd(65535);
        properties.setSmartAllocationEnabled(true);
        properties.setHistorySize(1000);
        properties.setCleanupIntervalMs(3600000);
        properties.setAllocationStrategy(PortProperties.AllocationStrategy.DYNAMIC);
        return properties;
    }
    
    @Bean
    public PortManager portManager() {
        return new PortManager(portProperties());
    }
    
    @Bean
    public AsyncExecutorService asyncExecutorService() {
        return new AsyncExecutorService();
    }
    
    @Bean
    public UDPMetricsCollector udpMetricsCollector() {
        return new UDPMetricsCollector();
    }
    
    @Bean
    public net.ooder.sdk.network.udp.UDPSDK udpSDK() throws Exception {
        NetworkProperties networkProps = networkProperties();
        networkProps.setDefaultPort(0);
        
        return new net.ooder.sdk.network.udp.UDPSDK(
            networkProps,
            retryProperties(),
            performanceProperties(),
            udpMetricsCollector(),
            asyncExecutorService(),
            portManager()
        );
    }
    
    @Bean
    public TerminalDiscoveryProperties terminalDiscoveryProperties() {
        TerminalDiscoveryProperties properties = new TerminalDiscoveryProperties();
        properties.setScanInterval(30000);
        return properties;
    }
    
    @Bean
    public AgentConfigProperties agentConfigProperties() {
        AgentConfigProperties properties = new AgentConfigProperties();
        properties.setUdpPort(9001);
        properties.setUdpBufferSize(65535);
        properties.setUdpTimeout(5000);
        properties.setUdpMaxPacketSize(65507);
        properties.setHeartbeatInterval(30000);
        properties.setHeartbeatTimeout(90000);
        properties.setHeartbeatLossThreshold(3);
        properties.setRetryMaxRetries(5);
        properties.setRetryInitialInterval(1000);
        properties.setRetryMaxInterval(30000);
        properties.setRetryBackoffFactor(2.0);
        return properties;
    }
}

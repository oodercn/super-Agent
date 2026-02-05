package net.ooder.sdk.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
    NetworkProperties.class,
    RetryProperties.class,
    PortProperties.class,
    MonitoringProperties.class,
    PerformanceProperties.class,
    AgentProperties.class,
    TerminalDiscoveryProperties.class,
    AgentConfigProperties.class
})
public class SDKConfiguration {
}

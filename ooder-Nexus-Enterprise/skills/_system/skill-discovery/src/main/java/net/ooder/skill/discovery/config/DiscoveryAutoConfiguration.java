package net.ooder.skill.discovery.config;

import net.ooder.skill.discovery.service.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "net.ooder.skill.discovery")
@ConditionalOnProperty(name = "skill.discovery.enabled", havingValue = "true", matchIfMissing = true)
public class DiscoveryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(MvpSkillIndexLoader.class)
    public MvpSkillIndexLoader mvpSkillIndexLoader() {
        return new MvpSkillIndexLoader();
    }

    @Bean
    @ConditionalOnMissingBean(RestApiDiscoveryService.class)
    public RestApiDiscoveryService restApiDiscoveryService() {
        return new RestApiDiscoveryService();
    }

    @Bean
    @ConditionalOnMissingBean(UdpBroadcastDiscoveryService.class)
    public UdpBroadcastDiscoveryService udpBroadcastDiscoveryService() {
        return new UdpBroadcastDiscoveryService();
    }

    @Bean
    @ConditionalOnMissingBean(MdnsDiscoveryService.class)
    public MdnsDiscoveryService mdnsDiscoveryService() {
        return new MdnsDiscoveryService();
    }
}

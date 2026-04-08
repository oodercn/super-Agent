package net.ooder.agent.config;

import net.ooder.skill.hotplug.ui.UiRouteRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UiConfigAutoConfiguration {

    @Bean
    public UiRouteRegistry uiRouteRegistry() {
        return new UiRouteRegistry();
    }
}

package net.ooder.examples.skillsa2ui.config;

import net.ooder.sdk.AgentSDK;
import net.ooder.sdk.agent.model.AgentConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AgentSdkConfig {
    @Bean
    public AgentConfig agentConfig() {
        return AgentConfig.builder()
                .agentId("skills-a2ui-001")
                .agentName("SkillsA2UI")
                .agentType("end")
                .endpoint("http://localhost:9013")
                .udpPort(9006)
                .heartbeatInterval(30000)
                .build();
    }

    @Bean
    public AgentSDK agentSDK(AgentConfig agentConfig) {
        try {
            return new AgentSDK(agentConfig);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize AgentSDK", e);
        }
    }
}
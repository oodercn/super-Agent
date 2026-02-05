package net.ooder.nexus.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import net.ooder.sdk.agent.model.AgentConfig;
import net.ooder.sdk.AgentSDK;
import net.ooder.nexus.skill.NexusSkill;
import net.ooder.nexus.skill.impl.NexusSkillImpl;
import net.ooder.nexus.management.NexusManager;
import net.ooder.nexus.management.impl.NexusManagerImpl;

/**
 * 应用配置类
 */
@Configuration
public class AppConfig {
    
    @Value("${ooder.agent.id}")
    private String agentId;
    
    @Value("${ooder.agent.name}")
    private String agentName;
    
    @Value("${ooder.agent.type}")
    private String agentType;
    
    @Value("${ooder.udp.port}")
    private int udpPort;
    
    @Value("${ooder.heartbeat.interval}")
    private long heartbeatInterval;
    
    @Value("${ooder.heartbeat.timeout}")
    private long heartbeatTimeout;
    
    /**
     * 配置AgentSDK
     */
    @Bean
    public AgentSDK agentSDK() throws Exception {
        // 创建Agent配置
        AgentConfig config = AgentConfig.builder()
                .agentId(agentId)
                .agentName(agentName)
                .agentType(agentType)
                .endpoint("localhost:" + udpPort)
                .udpPort(udpPort)
                .heartbeatInterval(heartbeatInterval)
                .build();
        
        // 初始化AgentSDK
        AgentSDK sdk = new AgentSDK(config);
        
        return sdk;
    }
    
    /**
     * 配置NexusSkill
     */
    @Bean
    public NexusSkill nexusSkill(AgentSDK agentSDK) throws Exception {
        NexusSkill skill = new NexusSkillImpl();
        skill.initialize(agentSDK);
        return skill;
    }
    
    /**
     * 配置NexusManager
     */
    @Bean
    public NexusManager nexusManager(AgentSDK agentSDK) throws Exception {
        NexusManager manager = new NexusManagerImpl();
        manager.initialize(agentSDK);
        return manager;
    }
}
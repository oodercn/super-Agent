package net.ooder.skillvfs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.ooder.sdk.agent.model.AgentConfig;
import net.ooder.sdk.AgentSDK;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * Main application class for Ooder Skill VFS service
 */
@SpringBootApplication(
        scanBasePackages = "net.ooder.skillvfs",
        exclude = {
                DataSourceAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class
        }
)
public class SkillVfsApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(SkillVfsApplication.class);

    private AgentSDK agentSDK;
    private AgentConfig agentConfig;
    private VfsService vfsService;

    public static void main(String[] args) {
        SpringApplication.run(SkillVfsApplication.class, args);
    }

    @Bean
    @ConfigurationProperties(prefix = "agent")
    public AgentConfig agentConfig() {
        return AgentConfig.builder()
                .agentType("routeAgent") // Declare as route agent
                .build();
    }
    
    // Remove VfsConfig and VfsService bean definitions to avoid circular dependency

    @Autowired
    public void setAgentConfig(AgentConfig agentConfig) {
        this.agentConfig = agentConfig;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting Ooder Skill VFS Service...");
        
        try {
            // Initialize Agent SDK
            agentSDK = AgentSDK.builder().config(agentConfig).build();
            agentSDK.start();
            
            // Initialize VFS configuration
            VfsConfig vfsConfig = VfsConfig.builder()
                    .vfsServerUrl("http://localhost:8080/vfs")
                    .groupName("default-group")
                    .build();
            
            // Create and start VFS service
            vfsService = new VfsService(vfsConfig);
            vfsService.start();
            
            log.info("Ooder Skill VFS Service started successfully");
            log.info("Agent ID: {}", agentConfig.getAgentId());
            log.info("Agent Type: {}", agentConfig.getAgentType());
            log.info("Endpoint: {}", agentConfig.getEndpoint());
            log.info("UDP Port: {}", agentConfig.getUdpPort());
            
        } catch (Exception e) {
            log.error("Failed to start Ooder Skill VFS Service: {}", e.getMessage(), e);
            System.exit(1);
        }
    }
    
    public void shutdown() {
        if (vfsService != null) {
            vfsService.stop();
        }
        
        if (agentSDK != null) {
            agentSDK.stop();
            log.info("Ooder Skill VFS Service stopped");
        }
    }
}


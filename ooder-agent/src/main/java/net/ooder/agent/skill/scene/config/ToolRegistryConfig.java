package net.ooder.mvp.skill.scene.config;

import net.ooder.scene.skill.tool.Tool;
import net.ooder.scene.skill.tool.ToolOrchestrator;
import net.ooder.scene.skill.tool.ToolRegistry;
import net.ooder.scene.skill.tool.impl.ToolOrchestratorImpl;
import net.ooder.scene.skill.tool.impl.ToolRegistryImpl;
import net.ooder.mvp.skill.scene.tool.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class ToolRegistryConfig {
    
    private static final Logger log = LoggerFactory.getLogger(ToolRegistryConfig.class);
    
    @Bean
    public ToolRegistry toolRegistry() {
        ToolRegistry registry = new ToolRegistryImpl();
        
        registry.register(new StartScanTool());
        registry.register(new FilterCapabilitiesTool());
        registry.register(new SelectCapabilityTool());
        registry.register(new InstallCapabilityTool());
        registry.register(new NavigateToPageTool());
        
        log.info("[ToolRegistryConfig] Registered {} tools", registry.listAll().size());
        
        return registry;
    }
    
    @Bean
    public ToolOrchestrator toolOrchestrator(ToolRegistry toolRegistry) {
        ToolOrchestrator orchestrator = new ToolOrchestratorImpl(toolRegistry);
        log.info("[ToolRegistryConfig] ToolOrchestrator created");
        return orchestrator;
    }
}

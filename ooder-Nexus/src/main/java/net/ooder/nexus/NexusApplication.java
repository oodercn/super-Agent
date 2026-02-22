package net.ooder.nexus;

import net.ooder.nexus.core.skill.NexusSkill;
import net.ooder.nexus.core.skill.impl.NexusSkillImpl;
import net.ooder.sdk.api.OoderSDK;
import net.ooder.sdk.infra.config.SDKConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NexusApplication {
    
    private static final Logger log = LoggerFactory.getLogger(NexusApplication.class);
    
    public static void main(String[] args) {
        log.info("Starting Independent Nexus...");
        
        try {
            SDKConfiguration config = new SDKConfiguration();
            config.setAgentId("nexus-001");
            config.setAgentName("Independent Nexus");
            config.setSkillRootPath("./skills");
            
            OoderSDK sdk = OoderSDK.builder()
                .configuration(config)
                .build();
            
            sdk.initialize();
            
            NexusSkill nexusSkill = new NexusSkillImpl();
            nexusSkill.initialize(sdk);
            
            sdk.start();
            
            log.info("Independent Nexus started successfully!");
            
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log.info("Shutting down Independent Nexus...");
                nexusSkill.stop();
                sdk.stop();
                log.info("Independent Nexus stopped successfully!");
            }));
            
            synchronized (NexusApplication.class) {
                NexusApplication.class.wait();
            }
            
        } catch (Exception e) {
            log.error("Failed to start Independent Nexus: {}", e.getMessage(), e);
            System.exit(1);
        }
    }
}

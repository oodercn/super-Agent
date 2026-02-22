package net.ooder.nexus.config;

import net.ooder.nexus.core.skill.NexusSkill;
import net.ooder.nexus.core.skill.impl.NexusSkillImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import net.ooder.sdk.api.OoderSDK;
import net.ooder.nexus.infrastructure.management.NexusManager;
import net.ooder.nexus.infrastructure.management.NexusManagerImpl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class AppConfig {

    private static final Logger log = LoggerFactory.getLogger(AppConfig.class);

    @Value("${ooder.agent.id:nexus-001}")
    private String agentId;

    @Value("${ooder.agent.name:Independent Nexus}")
    private String agentName;

    @Value("${ooder.udp.port:9876}")
    private int udpPort;

    @Value("${ooder.heartbeat.interval:30000}")
    private long heartbeatInterval;

    @Bean
    public NexusSkill nexusSkill(OoderSDK ooderSDK) {
        try {
            NexusSkill skill = new NexusSkillImpl();
            if (ooderSDK != null) {
                skill.initialize(ooderSDK);
            }
            return skill;
        } catch (Exception e) {
            log.error("Failed to initialize NexusSkill: {}", e.getMessage(), e);
            return null;
        }
    }

    @Bean
    public NexusManager nexusManager(OoderSDK ooderSDK) {
        try {
            NexusManager manager = new NexusManagerImpl();
            if (ooderSDK != null) {
                manager.initialize(ooderSDK);
            }
            return manager;
        } catch (Exception e) {
            log.error("Failed to initialize NexusManager: {}", e.getMessage(), e);
            return null;
        }
    }

    @Bean
    public net.ooder.nexus.infrastructure.management.NexusSkillManager nexusSkillManager(OoderSDK ooderSDK) {
        try {
            net.ooder.nexus.infrastructure.management.NexusSkillManager manager = new net.ooder.nexus.infrastructure.management.NexusSkillManager();
            if (ooderSDK != null) {
                manager.initialize(ooderSDK);
            }
            return manager;
        } catch (Exception e) {
            log.error("Failed to initialize NexusSkillManager: {}", e.getMessage(), e);
            return null;
        }
    }

    private boolean isOpenWrt() {
        try {
            return Files.exists(Paths.get("/etc/openwrt_release")) ||
                   new File("/etc/openwrt_release").exists();
        } catch (Exception e) {
            return false;
        }
    }
}

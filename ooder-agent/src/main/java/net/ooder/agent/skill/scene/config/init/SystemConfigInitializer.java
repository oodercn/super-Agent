package net.ooder.mvp.skill.scene.config.init;

import net.ooder.mvp.skill.scene.config.sdk.ConfigNode;
import net.ooder.mvp.skill.scene.config.sdk.SdkConfigStorage;
import net.ooder.mvp.skill.scene.config.service.ConfigLoaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Order(1)
public class SystemConfigInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(SystemConfigInitializer.class);

    private final ConfigLoaderService configLoader;
    private final SdkConfigStorage sdkStorage;

    @Value("${ooder.profile:micro}")
    private String profile;

    @Value("${ooder.config.root:./config}")
    private String configRoot;

    @Autowired
    public SystemConfigInitializer(ConfigLoaderService configLoader, SdkConfigStorage sdkStorage) {
        this.configLoader = configLoader;
        this.sdkStorage = sdkStorage;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("[SystemConfig] Initializing system configuration with profile: {}", profile);

        if (!sdkStorage.exists("system", "system")) {
            log.info("[SystemConfig] No system config found, initializing from profile: {}", profile);
            initializeFromProfile(profile);
        }

        ConfigNode systemConfig = configLoader.loadSystemConfig();
        log.info("[SystemConfig] System configuration loaded successfully");

        initializeCapabilities(systemConfig);
    }

    private void initializeFromProfile(String profileName) {
        try {
            ConfigNode profileConfig = sdkStorage.loadProfile(profileName);
            sdkStorage.saveSystemConfig(profileConfig);
            log.info("[SystemConfig] System config initialized from profile: {}", profileName);
        } catch (Exception e) {
            log.warn("[SystemConfig] Profile not found: {}, creating default config", profileName);
            ConfigNode defaultConfig = createDefaultConfig();
            sdkStorage.saveSystemConfig(defaultConfig);
        }
    }

    private ConfigNode createDefaultConfig() {
        Map<String, Object> config = new LinkedHashMap<>();
        config.put("apiVersion", "skills.ooder.io/v1");
        config.put("kind", "SystemConfig");

        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("name", "ooder-skills-system");
        metadata.put("version", "1.0.0");
        metadata.put("profile", profile);
        metadata.put("createdAt", Instant.now().toString());
        metadata.put("updatedAt", Instant.now().toString());
        config.put("metadata", metadata);

        Map<String, Object> spec = new LinkedHashMap<>();
        spec.put("capabilities", createDefaultCapabilities());
        config.put("spec", spec);

        return new ConfigNode(config);
    }

    private Map<String, Object> createDefaultCapabilities() {
        Map<String, Object> capabilities = new LinkedHashMap<>();

        capabilities.put("llm", createCapability(true, "skill-llm-aliyun-bailian", null));
        capabilities.put("db", createCapability(true, "skill-db-mysql", null));
        capabilities.put("vfs", createCapability(true, "skill-vfs-local", null));
        capabilities.put("org", createCapability(true, "skill-org-local", null));
        capabilities.put("know", createCapability(true, "skill-know-rag", null));
        capabilities.put("comm", createCapability(true, "skill-comm-notify", null));
        capabilities.put("auth", createCapability(true, "skill-auth-user", null));
        capabilities.put("mon", createCapability(true, "skill-mon-health", null));
        capabilities.put("payment", createCapability(false, null, null));
        capabilities.put("media", createCapability(false, null, null));
        capabilities.put("search", createCapability(true, "skill-search-es", null));
        capabilities.put("sched", createCapability(true, "skill-sched-quartz", null));
        capabilities.put("sec", createCapability(true, "skill-sec-access", null));
        capabilities.put("iot", createCapability(false, null, null));
        capabilities.put("net", createCapability(true, "skill-net-proxy", null));
        capabilities.put("sys", createCapability(true, "skill-sys-registry", null));
        capabilities.put("util", createCapability(true, "skill-util-report", null));

        return capabilities;
    }

    private Map<String, Object> createCapability(boolean enabled, String defaultDriver, String fallbackDriver) {
        Map<String, Object> capability = new LinkedHashMap<>();
        capability.put("enabled", enabled);
        if (defaultDriver != null) {
            capability.put("default", defaultDriver);
        }
        if (fallbackDriver != null) {
            capability.put("fallback", fallbackDriver);
        }
        capability.put("config", new LinkedHashMap<>());
        return capability;
    }

    private void initializeCapabilities(ConfigNode systemConfig) {
        Map<String, Object> capabilities = systemConfig.getNested("spec.capabilities");
        if (capabilities == null) {
            capabilities = new LinkedHashMap<>();
        }

        String[] requiredCapabilities = {
            "llm", "db", "vfs", "org", "know", "comm", 
            "auth", "mon", "search", "sched", "sec", "net", "sys", "util"
        };

        boolean updated = false;
        for (String capability : requiredCapabilities) {
            if (!capabilities.containsKey(capability)) {
                capabilities.put(capability, createCapability(false, null, null));
                updated = true;
            }
        }

        if (updated) {
            systemConfig.putNested("spec.capabilities", capabilities);
            sdkStorage.saveSystemConfig(systemConfig);
            log.info("[SystemConfig] Capabilities initialized");
        }
    }
}

package net.ooder.mvp.skill.scene.config;

import net.ooder.sdk.OoderSdk;
import net.ooder.sdk.OoderSdkBuilder;
import net.ooder.sdk.a2a.capability.CapabilityRegistry;
import net.ooder.sdk.service.skill.SkillService;
import net.ooder.skills.core.discovery.LocalDiscoverer;
import net.ooder.skills.core.impl.SkillRegistryImpl;
import net.ooder.skills.core.installer.SkillInstallerImpl;
import net.ooder.skills.core.impl.SkillPackageManagerImpl;
import net.ooder.skills.api.SkillDiscoverer;
import net.ooder.skills.api.SkillRegistry;
import net.ooder.skills.api.SkillInstaller;
import net.ooder.skills.api.SkillPackageManager;
import net.ooder.scene.discovery.UnifiedDiscoveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import jakarta.annotation.PreDestroy;

@Configuration
public class SdkConfiguration {

    private static final Logger log = LoggerFactory.getLogger(SdkConfiguration.class);

    @Value("${ooder.sdk.enabled:true}")
    private boolean sdkEnabled;

    @Value("${ooder.sdk.node-id:skill-scene}")
    private String nodeId;

    @Value("${ooder.skills.path:./skills}")
    private String skillRootPath;

    @Value("${scene.engine.discovery.gitee.token:}")
    private String giteeToken;

    @Value("${scene.engine.discovery.gitee.default-owner:ooderCN}")
    private String giteeOwner;

    @Value("${scene.engine.discovery.gitee.default-repo:skills}")
    private String giteeSkillsRepo;

    @Value("${scene.engine.discovery.github.token:}")
    private String githubToken;

    @Value("${scene.engine.discovery.github.default-owner:oodercn}")
    private String githubOwner;

    @Value("${scene.engine.discovery.github.default-repo:skills}")
    private String githubSkillsRepo;

    @Value("${ooder.llm.provider:mock}")
    private String llmProvider;

    @Value("${ooder.llm.model:default}")
    private String llmModel;

    @Value("${ooder.llm.baidu.api-key:}")
    private String baiduApiKey;

    @Value("${ooder.llm.baidu.secret-key:}")
    private String baiduSecretKey;

    @Value("${ooder.llm.aliyun-bailian.api-key:}")
    private String aliyunBailianApiKey;

    @Value("${ooder.llm.aliyun-bailian.model:qwen-plus}")
    private String aliyunBailianModel;

    @Autowired(required = false)
    private GiteeDiscoveryProperties giteeDiscoveryProperties;

    private OoderSdk sdk;

    @Bean
    @ConditionalOnProperty(name = "ooder.sdk.enabled", havingValue = "true")
    public OoderSdk ooderSDK() {
        log.info("[ooderSDK] Initializing OoderSDK with agentId: {}", nodeId);

        try {
            sdk = OoderSdkBuilder.create()
                .sdkId(nodeId)
                .autoDiscoverDrivers(true)
                .autoStartScenes(true)
                .property("skillRootPath", skillRootPath)
                .property("llm.provider", llmProvider)
                .property("llm.model", llmModel)
                .property("llm.baidu.apiKey", baiduApiKey)
                .property("llm.baidu.secretKey", baiduSecretKey)
                .property("llm.aliyun-bailian.apiKey", aliyunBailianApiKey)
                .property("llm.aliyun-bailian.model", aliyunBailianModel)
                .property("discovery.gitee.token", giteeToken)
                .property("discovery.gitee.owner", giteeOwner)
                .property("discovery.gitee.repo", giteeSkillsRepo)
                .property("discovery.github.token", githubToken)
                .property("discovery.github.owner", githubOwner)
                .property("discovery.github.repo", githubSkillsRepo)
                .build();

            log.info("[ooderSDK] OoderSDK initialized successfully");
            return sdk;
        } catch (Exception e) {
            log.error("[ooderSDK] Failed to initialize OoderSDK: {}", e.getMessage(), e);
            return null;
        }
    }

    @Bean
    @ConditionalOnProperty(name = "scene.engine.discovery.enabled", havingValue = "true", matchIfMissing = true)
    @org.springframework.boot.context.properties.ConfigurationProperties(prefix = "scene.engine.discovery")
    public net.ooder.scene.discovery.config.DiscoveryProperties discoveryProperties() {
        log.info("[discoveryProperties] Creating DiscoveryProperties with config binding");
        net.ooder.scene.discovery.config.DiscoveryProperties props = new net.ooder.scene.discovery.config.DiscoveryProperties();
        return props;
    }

    @Bean
    @ConditionalOnProperty(name = "scene.engine.discovery.enabled", havingValue = "true", matchIfMissing = true)
    public net.ooder.scene.discovery.UnifiedDiscoveryService unifiedDiscoveryService() {
        log.info("[unifiedDiscoveryService] Creating UnifiedDiscoveryService");
        
        String token = null;
        String owner = "ooderCN";
        String repo = "skills";
        String branch = "main";
        String skillsPath = "";
        
        if (giteeDiscoveryProperties != null) {
            token = giteeDiscoveryProperties.getToken();
            owner = giteeDiscoveryProperties.getDefaultOwner();
            repo = giteeDiscoveryProperties.getDefaultRepo();
            branch = giteeDiscoveryProperties.getDefaultBranch();
            skillsPath = giteeDiscoveryProperties.getSkillsPath();
            log.info("[unifiedDiscoveryService] GiteeDiscoveryProperties loaded: token={}, owner={}, repo={}", 
                token != null ? (token.isEmpty() ? "empty" : "set(length=" + token.length() + ")") : "null", 
                owner, repo);
        } else {
            log.warn("[unifiedDiscoveryService] GiteeDiscoveryProperties is null, falling back to @Value");
            token = giteeToken;
            owner = giteeOwner;
            repo = giteeSkillsRepo;
            log.info("[unifiedDiscoveryService] @Value fallback: giteeToken={}, giteeOwner={}, giteeSkillsRepo={}", 
                token != null ? (token.isEmpty() ? "empty" : "set") : "null", 
                owner, repo);
        }
        
        try {
            net.ooder.scene.discovery.impl.UnifiedDiscoveryServiceImpl service = 
                new net.ooder.scene.discovery.impl.UnifiedDiscoveryServiceImpl();
            
            if (token != null && !token.isEmpty()) {
                log.info("[unifiedDiscoveryService] Configuring Gitee with owner={}, repo={}, branch={}", owner, repo, branch);
                service.configureGitee(token, owner, repo, branch, skillsPath);
                log.info("[unifiedDiscoveryService] Gitee configured successfully");
            } else {
                log.warn("[unifiedDiscoveryService] Gitee token is not configured, skipping Gitee discovery setup");
            }
            
            if (githubToken != null && !githubToken.isEmpty()) {
                log.info("[unifiedDiscoveryService] Configuring GitHub with owner={}, repo={}", githubOwner, githubSkillsRepo);
                service.configureGithub(githubToken, githubOwner, githubSkillsRepo);
            }
            
            log.info("[unifiedDiscoveryService] UnifiedDiscoveryService created and configured successfully");
            return service;
        } catch (Exception e) {
            log.error("[unifiedDiscoveryService] Failed to create UnifiedDiscoveryService: {}", e.getMessage());
            return null;
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public SkillPackageManager skillPackageManager() {
        log.info("[skillPackageManager] Creating SkillPackageManagerImpl with skillRootPath: {}", skillRootPath);
        net.ooder.skills.core.impl.SkillPackageManagerImpl impl = new net.ooder.skills.core.impl.SkillPackageManagerImpl();
        impl.setSkillRootPath(skillRootPath);
        log.info("[skillPackageManager] Gitee/GitHub discovery requires UnifiedDiscoveryService from SE SDK");
        return impl;
    }

    @Bean
    @ConditionalOnMissingBean
    public SkillInstaller skillInstaller() {
        log.info("[skillInstaller] Creating SkillInstallerImpl");
        return new SkillInstallerImpl();
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public SkillRegistry skillRegistry() {
        log.info("[skillRegistry] Creating SkillRegistryImpl");
        return new SkillRegistryImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public SkillDiscoverer skillDiscoverer() {
        log.info("[skillDiscoverer] Creating LocalDiscoverer with skillsDirectory: {}", skillRootPath);
        LocalDiscoverer discoverer = new LocalDiscoverer(skillRootPath);
        return discoverer;
    }

    @Bean
    @ConditionalOnMissingBean
    public SkillService skillService(SkillPackageManager packageManager) {
        log.info("[skillService] Creating SkillService");
        return new SkillService(packageManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public CapabilityRegistry capabilityRegistry() {
        log.info("[capabilityRegistry] Creating CapabilityRegistry");
        return new CapabilityRegistry();
    }

    @Bean
    @ConditionalOnProperty(name = "ooder.sdk.enabled", havingValue = "true")
    public SkillDiscoverer gitHubDiscoverer() {
        log.info("[gitHubDiscoverer] Git discovery will be handled by SkillPackageManager");
        return null;
    }

    @Bean
    @ConditionalOnProperty(name = "ooder.sdk.enabled", havingValue = "true")
    public SkillDiscoverer giteeDiscoverer() {
        log.info("[giteeDiscoverer] Git discovery will be handled by SkillPackageManager");
        return null;
    }

    @PreDestroy
    public void shutdown() {
        if (sdk != null) {
            log.info("[shutdown] Shutting down OoderSDK");
            try {
                sdk.shutdown();
            } catch (Exception e) {
                log.warn("[shutdown] Failed to shutdown OoderSDK: {}", e.getMessage());
            }
        }
    }

    public boolean isSdkAvailable() {
        return sdk != null && sdk.isInitialized();
    }
}

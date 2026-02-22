package net.ooder.nexus.config;

import net.ooder.sdk.api.OoderSDK;
import net.ooder.sdk.api.scene.CapabilityInvoker;
import net.ooder.sdk.api.scene.SceneGroupManager;
import net.ooder.sdk.api.skill.SkillPackageManager;
import net.ooder.sdk.infra.config.SDKConfiguration;
import net.ooder.sdk.southbound.protocol.DiscoveryProtocol;
import net.ooder.sdk.southbound.protocol.LoginProtocol;
import net.ooder.sdk.southbound.protocol.CollaborationProtocol;
import net.ooder.sdk.southbound.protocol.RoleProtocol;
import net.ooder.sdk.southbound.protocol.impl.DiscoveryProtocolImpl;
import net.ooder.sdk.southbound.protocol.impl.LoginProtocolImpl;
import net.ooder.sdk.southbound.protocol.impl.CollaborationProtocolImpl;
import net.ooder.sdk.southbound.protocol.impl.RoleProtocolImpl;
import net.ooder.sdk.northbound.protocol.ObservationProtocol;
import net.ooder.sdk.northbound.protocol.DomainManagementProtocol;
import net.ooder.sdk.northbound.protocol.impl.ObservationProtocolImpl;
import net.ooder.sdk.northbound.protocol.impl.DomainManagementProtocolImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

@Configuration
public class SdkConfiguration {

    private static final Logger log = LoggerFactory.getLogger(SdkConfiguration.class);

    @Value("${ooder.sdk.node-id:nexus-local}")
    private String nodeId;

    @Value("${ooder.sdk.skill-path:./skills}")
    private String skillPath;

    @Value("${ooder.sdk.enabled:true}")
    private boolean sdkEnabled;

    private OoderSDK sdk;

    @Bean
    public OoderSDK ooderSDK() throws Exception {
        if (!sdkEnabled) {
            log.info("SDK is disabled, skipping initialization");
            return null;
        }

        log.info("Initializing OoderSDK with agentId: {}", nodeId);

        SDKConfiguration configuration = new SDKConfiguration();
        configuration.setAgentId(nodeId);
        configuration.setAgentName("Nexus Node");
        configuration.setAgentType("NEXUS");
        configuration.setSkillRootPath(skillPath);

        sdk = OoderSDK.builder()
            .configuration(configuration)
            .build();

        sdk.initialize();
        sdk.start();

        log.info("OoderSDK initialized and started successfully");
        return sdk;
    }

    @Bean
    public SkillPackageManager skillPackageManager(OoderSDK sdk) {
        if (sdk == null) {
            log.warn("OoderSDK is null, SkillPackageManager not available");
            return null;
        }
        return sdk.getSkillPackageManager();
    }

    @Bean
    public SceneGroupManager sceneGroupManager(OoderSDK sdk) {
        if (sdk == null) {
            log.warn("OoderSDK is null, SceneGroupManager not available");
            return null;
        }
        return sdk.getSceneGroupManager();
    }

    @Bean
    public CapabilityInvoker capabilityInvoker(OoderSDK sdk) {
        if (sdk == null) {
            log.warn("OoderSDK is null, CapabilityInvoker not available");
            return null;
        }
        return sdk.getCapabilityInvoker();
    }

    @Bean
    public DiscoveryProtocol discoveryProtocol() {
        if (!sdkEnabled) {
            log.info("SDK is disabled, DiscoveryProtocol not available");
            return null;
        }
        log.info("DiscoveryProtocol initialized");
        return new DiscoveryProtocolImpl();
    }

    @Bean
    public LoginProtocol loginProtocol() {
        if (!sdkEnabled) {
            log.info("SDK is disabled, LoginProtocol not available");
            return null;
        }
        log.info("LoginProtocol initialized");
        return new LoginProtocolImpl();
    }

    @Bean
    public CollaborationProtocol collaborationProtocol() {
        if (!sdkEnabled) {
            log.info("SDK is disabled, CollaborationProtocol not available");
            return null;
        }
        log.info("CollaborationProtocol initialized");
        return new CollaborationProtocolImpl();
    }

    @Bean
    public RoleProtocol roleProtocol() {
        if (!sdkEnabled) {
            log.info("SDK is disabled, RoleProtocol not available");
            return null;
        }
        log.info("RoleProtocol initialized");
        return new RoleProtocolImpl();
    }

    @Bean
    public ObservationProtocol observationProtocol() {
        if (!sdkEnabled) {
            log.info("SDK is disabled, ObservationProtocol not available");
            return null;
        }
        log.info("ObservationProtocol initialized");
        return new ObservationProtocolImpl();
    }

    @Bean
    public DomainManagementProtocol domainManagementProtocol() {
        if (!sdkEnabled) {
            log.info("SDK is disabled, DomainManagementProtocol not available");
            return null;
        }
        log.info("DomainManagementProtocol initialized");
        return new DomainManagementProtocolImpl();
    }

    @PreDestroy
    public void shutdown() {
        if (sdk != null) {
            log.info("Shutting down OoderSDK");
            sdk.shutdown();
        }
    }
}

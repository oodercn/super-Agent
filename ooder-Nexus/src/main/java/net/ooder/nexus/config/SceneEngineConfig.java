package net.ooder.nexus.config;

import net.ooder.scene.core.SceneEngine;
import net.ooder.scene.core.SceneClient;
import net.ooder.scene.core.AdminClient;
import net.ooder.scene.core.ProviderRegistry;
import net.ooder.scene.core.impl.DefaultProviderRegistry;
import net.ooder.scene.provider.SystemProvider;
import net.ooder.scene.provider.LogProvider;
import net.ooder.scene.provider.ConfigProvider;
import net.ooder.nexus.provider.NexusSystemProvider;
import net.ooder.nexus.provider.NexusLogProvider;
import net.ooder.nexus.provider.NexusConfigProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.util.ServiceLoader;

@Configuration
public class SceneEngineConfig {

    private static final Logger log = LoggerFactory.getLogger(SceneEngineConfig.class);

    @Value("${ooder.scene.engine.enabled:true}")
    private boolean engineEnabled;

    @Value("${ooder.scene.engine.userId:nexus-system}")
    private String userId;

    @Value("${ooder.scene.engine.userName:Nexus System}")
    private String userName;

    @Value("${ooder.scene.engine.token:}")
    private String token;

    @Value("${ooder.scene.engine.password:nexus-default}")
    private String password;

    private SceneEngine sceneEngine;
    private ProviderRegistry providerRegistry;

    @Bean
    public ProviderRegistry providerRegistry() {
        log.info("Creating ProviderRegistry...");
        this.providerRegistry = new DefaultProviderRegistry();
        return this.providerRegistry;
    }

    @Bean
    public SceneEngine sceneEngine(ProviderRegistry providerRegistry,
                                   @Autowired(required = false) NexusSystemProvider systemProvider,
                                   @Autowired(required = false) NexusLogProvider logProvider,
                                   @Autowired(required = false) NexusConfigProvider configProvider) {
        if (!engineEnabled) {
            log.info("SceneEngine is disabled");
            return null;
        }

        log.info("Initializing SceneEngine v0.7.3...");
        
        try {
            ServiceLoader<SceneEngine> loader = ServiceLoader.load(SceneEngine.class);
            SceneEngine engine = loader.iterator().hasNext() ? loader.iterator().next() : null;
            
            if (engine == null) {
                log.warn("No SceneEngine implementation found via ServiceLoader, using fallback mode");
                return null;
            }
            
            if (systemProvider != null) {
                systemProvider.initialize(engine);
                providerRegistry.register(SystemProvider.class, systemProvider);
                log.info("Registered SystemProvider: {}", systemProvider.getProviderName());
            }
            
            if (logProvider != null) {
                logProvider.initialize(engine);
                providerRegistry.register(LogProvider.class, logProvider);
                log.info("Registered LogProvider: {}", logProvider.getProviderName());
            }
            
            if (configProvider != null) {
                configProvider.initialize(engine);
                providerRegistry.register(ConfigProvider.class, configProvider);
                log.info("Registered ConfigProvider: {}", configProvider.getProviderName());
            }
            
            providerRegistry.startAll();
            
            engine.start();
            this.sceneEngine = engine;
            
            log.info("SceneEngine initialized successfully: {} v{}", engine.getName(), engine.getVersion());
            log.info("ProviderRegistry initialized with {} providers", providerRegistry.getProviderCount());
            return engine;
        } catch (Exception e) {
            log.error("Failed to initialize SceneEngine: {}", e.getMessage(), e);
            return null;
        }
    }

    @Bean
    public SceneClient sceneClient() {
        if (sceneEngine == null) {
            log.warn("SceneEngine not available, SceneClient will be null");
            return null;
        }

        try {
            SceneClient client;
            if (token != null && !token.isEmpty()) {
                client = sceneEngine.login(token);
            } else {
                client = sceneEngine.login(userId, password);
            }
            log.info("SceneClient created for user: {}", userId);
            return client;
        } catch (Exception e) {
            log.error("Failed to create SceneClient: {}", e.getMessage(), e);
            return null;
        }
    }

    @Bean
    public AdminClient adminClient() {
        if (sceneEngine == null) {
            log.warn("SceneEngine not available, AdminClient will be null");
            return null;
        }

        try {
            AdminClient admin = sceneEngine.adminLogin(userId, password);
            log.info("AdminClient created");
            return admin;
        } catch (Exception e) {
            log.error("Failed to create AdminClient: {}", e.getMessage(), e);
            return null;
        }
    }

    @PreDestroy
    public void shutdown() {
        if (providerRegistry != null) {
            log.info("Stopping all providers...");
            try {
                providerRegistry.stopAll();
                log.info("All providers stopped");
            } catch (Exception e) {
                log.error("Error stopping providers: {}", e.getMessage(), e);
            }
        }
        
        if (sceneEngine != null) {
            log.info("Shutting down SceneEngine...");
            try {
                sceneEngine.stop();
                log.info("SceneEngine stopped successfully");
            } catch (Exception e) {
                log.error("Error shutting down SceneEngine: {}", e.getMessage(), e);
            }
        }
    }
}

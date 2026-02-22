package net.ooder.nexus.config;

import net.ooder.sdk.api.storage.StorageService;
import net.ooder.sdk.api.llm.LlmService;
import net.ooder.sdk.api.event.EventBus;
import net.ooder.sdk.api.network.NetworkService;
import net.ooder.sdk.api.scheduler.TaskScheduler;
import net.ooder.sdk.api.security.SecurityService;

import net.ooder.sdk.service.storage.StorageServiceImpl;
import net.ooder.sdk.service.llm.LlmServiceImpl;
import net.ooder.sdk.service.event.EventBusImpl;
import net.ooder.sdk.service.network.NetworkServiceImpl;
import net.ooder.sdk.service.scheduler.TaskSchedulerImpl;
import net.ooder.sdk.service.security.SecurityServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.PreDestroy;

@Configuration
public class SDKServiceConfig {

    private static final Logger log = LoggerFactory.getLogger(SDKServiceConfig.class);

    @Value("${sdk.storage.base-path:./data/storage}")
    private String storageBasePath;

    @Value("${sdk.storage.cache-enabled:true}")
    private boolean storageCacheEnabled;

    @Value("${sdk.llm.default-model:gpt-4}")
    private String defaultLlmModel;

    @Value("${sdk.network.quality-monitor-enabled:true}")
    private boolean networkQualityMonitorEnabled;

    @Value("${sdk.network.quality-monitor-interval:30000}")
    private long networkQualityMonitorInterval;

    private StorageService storageService;
    private LlmService llmService;
    private EventBus eventBus;
    private NetworkService networkService;
    private TaskScheduler taskScheduler;
    private SecurityService securityService;

    @Bean
    @Primary
    public StorageService storageService() {
        log.info("Initializing StorageService with base path: {}", storageBasePath);
        StorageServiceImpl storage = new StorageServiceImpl(storageBasePath);
        if (storageCacheEnabled) {
            storage.setUseCache(true);
            log.info("Storage cache enabled");
        }
        this.storageService = storage;
        return storage;
    }

    @Bean
    public LlmService llmService() {
        log.info("Initializing LlmService with default model: {}", defaultLlmModel);
        LlmServiceImpl llm = new LlmServiceImpl();
        llm.setModel(defaultLlmModel);
        this.llmService = llm;
        return llm;
    }

    @Bean
    public EventBus eventBus() {
        log.info("Initializing EventBus");
        EventBusImpl eventBus = new EventBusImpl();
        this.eventBus = eventBus;
        return eventBus;
    }

    @Bean
    public NetworkService networkService() {
        log.info("Initializing NetworkService");
        NetworkServiceImpl network = new NetworkServiceImpl();
        if (networkQualityMonitorEnabled) {
            network.enableQualityMonitor(networkQualityMonitorInterval);
            log.info("Network quality monitor enabled with interval: {}ms", networkQualityMonitorInterval);
        }
        this.networkService = network;
        return network;
    }

    @Bean
    public TaskScheduler taskScheduler() {
        log.info("Initializing TaskScheduler");
        TaskSchedulerImpl scheduler = new TaskSchedulerImpl();
        if (storageService != null) {
            scheduler.enablePersistence(storageService);
            scheduler.recoverTasks();
            log.info("TaskScheduler persistence enabled");
        }
        this.taskScheduler = scheduler;
        return scheduler;
    }

    @Bean
    public SecurityService securityService() {
        log.info("Initializing SecurityService");
        SecurityServiceImpl security = new SecurityServiceImpl();
        this.securityService = security;
        return security;
    }

    @PreDestroy
    public void shutdown() {
        log.info("Shutting down SDK services...");
        
        if (llmService != null) {
            try {
                llmService.shutdown();
                log.info("LlmService shutdown complete");
            } catch (Exception e) {
                log.warn("Error shutting down LlmService", e);
            }
        }
        
        if (networkService != null) {
            try {
                networkService.shutdown();
                log.info("NetworkService shutdown complete");
            } catch (Exception e) {
                log.warn("Error shutting down NetworkService", e);
            }
        }
        
        if (taskScheduler != null) {
            try {
                taskScheduler.shutdown();
                log.info("TaskScheduler shutdown complete");
            } catch (Exception e) {
                log.warn("Error shutting down TaskScheduler", e);
            }
        }
        
        if (securityService != null) {
            try {
                securityService.shutdown();
                log.info("SecurityService shutdown complete");
            } catch (Exception e) {
                log.warn("Error shutting down SecurityService", e);
            }
        }
        
        if (eventBus != null) {
            try {
                eventBus.shutdown();
                log.info("EventBus shutdown complete");
            } catch (Exception e) {
                log.warn("Error shutting down EventBus", e);
            }
        }
        
        log.info("All SDK services shutdown complete");
    }
}

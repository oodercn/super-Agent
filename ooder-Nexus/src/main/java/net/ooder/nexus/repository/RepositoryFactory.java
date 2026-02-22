package net.ooder.nexus.repository;

import net.ooder.nexus.config.RepositoryConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Repository factory
 * Provides dynamic switching between mock and real repository implementations
 */
@Component
public class RepositoryFactory {

    private static final Logger log = LoggerFactory.getLogger(RepositoryFactory.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private RepositoryConfig repositoryConfig;

    private final Map<String, Object> mockRepositories = new HashMap<>();
    private final Map<String, Object> realRepositories = new HashMap<>();

    @PostConstruct
    public void init() {
        log.info("Initializing RepositoryFactory with mode: {}", repositoryConfig.getRepositoryMode());
        initializeRepositories();
    }

    /**
     * Initialize repository mappings
     */
    @SuppressWarnings("unchecked")
    private void initializeRepositories() {
        // LogRepository
        mockRepositories.put("logRepository", applicationContext.getBean("logRepositoryMock", LogRepository.class));
        // Real implementation will be added when created
        // realRepositories.put("logRepository", applicationContext.getBean(LogRepositoryImpl.class));

        // HealthCheckRepository
        // mockRepositories.put("healthCheckRepository", applicationContext.getBean(HealthCheckRepositoryMock.class));
        // realRepositories.put("healthCheckRepository", applicationContext.getBean(HealthCheckRepositoryImpl.class));

        // ConfigRepository
        // mockRepositories.put("configRepository", applicationContext.getBean(ConfigRepositoryMock.class));
        // realRepositories.put("configRepository", applicationContext.getBean(ConfigRepositoryImpl.class));

        // ProtocolRepository
        // mockRepositories.put("protocolRepository", applicationContext.getBean(ProtocolRepositoryMock.class));
        // realRepositories.put("protocolRepository", applicationContext.getBean(ProtocolRepositoryImpl.class));
    }

    /**
     * Get LogRepository based on current mode
     */
    public LogRepository getLogRepository() {
        return (LogRepository) getRepository("logRepository");
    }

    /**
     * Get HealthCheckRepository based on current mode
     */
    public HealthCheckRepository getHealthCheckRepository() {
        return (HealthCheckRepository) getRepository("healthCheckRepository");
    }

    /**
     * Get ConfigRepository based on current mode
     */
    public ConfigRepository getConfigRepository() {
        return (ConfigRepository) getRepository("configRepository");
    }

    /**
     * Get ProtocolRepository based on current mode
     */
    public ProtocolRepository getProtocolRepository() {
        return (ProtocolRepository) getRepository("protocolRepository");
    }

    /**
     * Get repository by name based on current mode
     */
    private Object getRepository(String name) {
        boolean isMockMode = repositoryConfig.isMockMode();
        Map<String, Object> repositoryMap = isMockMode ? mockRepositories : realRepositories;

        Object repository = repositoryMap.get(name);
        if (repository == null) {
            log.warn("Repository not found: {} in {} mode, falling back to mock", name, isMockMode ? "mock" : "real");
            repository = mockRepositories.get(name);
        }

        return repository;
    }

    /**
     * Switch repository mode
     */
    public void switchMode(String mode) {
        String oldMode = repositoryConfig.getRepositoryMode();
        repositoryConfig.setRepositoryMode(mode);
        log.info("Repository mode switched from {} to {}", oldMode, mode);
    }

    /**
     * Get current mode
     */
    public String getCurrentMode() {
        return repositoryConfig.getRepositoryMode();
    }

    /**
     * Check if current mode is mock
     */
    public boolean isMockMode() {
        return repositoryConfig.isMockMode();
    }

    /**
     * Check if current mode is real
     */
    public boolean isRealMode() {
        return repositoryConfig.isRealMode();
    }

    /**
     * Register mock repository
     */
    public void registerMockRepository(String name, Object repository) {
        mockRepositories.put(name, repository);
        log.info("Registered mock repository: {}", name);
    }

    /**
     * Register real repository
     */
    public void registerRealRepository(String name, Object repository) {
        realRepositories.put(name, repository);
        log.info("Registered real repository: {}", name);
    }
}

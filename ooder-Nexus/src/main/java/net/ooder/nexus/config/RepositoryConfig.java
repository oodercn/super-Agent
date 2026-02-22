package net.ooder.nexus.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Repository configuration
 * Controls the repository implementation mode (mock or real)
 */
@Configuration
public class RepositoryConfig {

    @Value("${repository.mode:mock}")
    private String repositoryMode;

    @Value("${repository.mock.enabled:true}")
    private boolean mockEnabled;

    @PostConstruct
    public void init() {
        // Validate configuration
        if (!isValidMode(repositoryMode)) {
            repositoryMode = "mock";
        }
    }

    /**
     * Check if current mode is mock
     */
    public boolean isMockMode() {
        return "mock".equalsIgnoreCase(repositoryMode);
    }

    /**
     * Check if current mode is real
     */
    public boolean isRealMode() {
        return "real".equalsIgnoreCase(repositoryMode);
    }

    /**
     * Get current repository mode
     */
    public String getRepositoryMode() {
        return repositoryMode.toLowerCase();
    }

    /**
     * Set repository mode
     */
    public void setRepositoryMode(String mode) {
        if (isValidMode(mode)) {
            this.repositoryMode = mode.toLowerCase();
        }
    }

    /**
     * Check if mock is enabled
     */
    public boolean isMockEnabled() {
        return mockEnabled;
    }

    /**
     * Validate repository mode
     */
    private boolean isValidMode(String mode) {
        return mode != null && ("mock".equalsIgnoreCase(mode) || "real".equalsIgnoreCase(mode));
    }
}

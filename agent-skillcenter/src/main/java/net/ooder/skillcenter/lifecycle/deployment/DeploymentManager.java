package net.ooder.skillcenter.lifecycle.deployment;

import net.ooder.skillcenter.lifecycle.deployment.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 部署管理器 - 符合v0.7.0协议规范
 */
@Component
public class DeploymentManager {

    private static final Logger log = LoggerFactory.getLogger(DeploymentManager.class);

    @Autowired
    private HealthChecker healthChecker;

    @Autowired
    private StartupManager startupManager;

    private final Map<String, DeploymentConfig> configs = new ConcurrentHashMap<>();
    private final Map<String, DeploymentState> states = new ConcurrentHashMap<>();
    private final Set<String> singletons = ConcurrentHashMap.newKeySet();

    public enum DeploymentState {
        CREATED,
        DEPLOYING,
        DEPLOYED,
        RUNNING,
        STOPPED,
        FAILED,
        UNDEPLOYED
    }

    public boolean deploy(String skillId, DeploymentConfig config) {
        if (config == null) {
            log.error("Null deployment config for skill: {}", skillId);
            return false;
        }

        if (config.isSingleton() && singletons.contains(skillId)) {
            log.error("Singleton skill already deployed: {}", skillId);
            return false;
        }

        configs.put(skillId, config);
        states.put(skillId, DeploymentState.CREATED);

        if (config.isSingleton()) {
            singletons.add(skillId);
        }

        if (config.getHealthCheck() != null) {
            healthChecker.register(skillId, config.getHealthCheck());
        }

        if (config.getStartup() != null) {
            startupManager.register(skillId, config.getStartup());
        }

        log.info("Deployed skill {} with modes: {}", skillId, config.getModes());
        return true;
    }

    public boolean start(String skillId) {
        DeploymentConfig config = configs.get(skillId);
        if (config == null) {
            log.error("No deployment config found for skill: {}", skillId);
            return false;
        }

        if (!startupManager.canStart(skillId)) {
            log.warn("Cannot start skill {}: dependencies not ready", skillId);
            return false;
        }

        states.put(skillId, DeploymentState.DEPLOYING);
        startupManager.markStarting(skillId);

        try {
            Thread.sleep(100);
            
            states.put(skillId, DeploymentState.RUNNING);
            startupManager.markStarted(skillId);
            
            log.info("Started skill: {}", skillId);
            return true;
        } catch (Exception e) {
            states.put(skillId, DeploymentState.FAILED);
            startupManager.markFailed(skillId);
            log.error("Failed to start skill {}: {}", skillId, e.getMessage());
            return false;
        }
    }

    public boolean stop(String skillId) {
        DeploymentState state = states.get(skillId);
        if (state == null || state != DeploymentState.RUNNING) {
            return false;
        }

        states.put(skillId, DeploymentState.STOPPED);
        startupManager.markStopped(skillId);
        
        log.info("Stopped skill: {}", skillId);
        return true;
    }

    public boolean undeploy(String skillId) {
        stop(skillId);
        
        configs.remove(skillId);
        states.remove(skillId);
        singletons.remove(skillId);
        
        healthChecker.unregister(skillId);
        startupManager.unregister(skillId);
        
        log.info("Undeployed skill: {}", skillId);
        return true;
    }

    public DeploymentState getState(String skillId) {
        return states.get(skillId);
    }

    public DeploymentConfig getConfig(String skillId) {
        return configs.get(skillId);
    }

    public boolean isHealthy(String skillId) {
        HealthChecker.HealthStatus status = healthChecker.getStatus(skillId);
        return status == HealthChecker.HealthStatus.HEALTHY;
    }

    public List<String> getStartupOrder() {
        return startupManager.getStartupOrder();
    }

    public Map<String, Object> getDeploymentStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", configs.size());
        stats.put("running", states.values().stream().filter(s -> s == DeploymentState.RUNNING).count());
        stats.put("stopped", states.values().stream().filter(s -> s == DeploymentState.STOPPED).count());
        stats.put("failed", states.values().stream().filter(s -> s == DeploymentState.FAILED).count());
        stats.put("healthy", healthChecker.getHealthyCount());
        stats.put("unhealthy", healthChecker.getUnhealthyCount());
        return stats;
    }

    public List<String> getDeployedSkills() {
        return new ArrayList<>(configs.keySet());
    }

    public boolean supportsMode(String skillId, DeploymentMode mode) {
        DeploymentConfig config = configs.get(skillId);
        return config != null && config.supportsMode(mode);
    }
}

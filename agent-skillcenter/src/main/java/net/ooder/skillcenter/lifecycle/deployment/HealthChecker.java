package net.ooder.skillcenter.lifecycle.deployment;

import net.ooder.skillcenter.lifecycle.deployment.model.HealthCheckConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 健康检查器 - 符合v0.7.0协议规范
 */
@Component
public class HealthChecker {

    private static final Logger log = LoggerFactory.getLogger(HealthChecker.class);

    private final Map<String, HealthStatus> healthStatuses = new ConcurrentHashMap<>();
    private final Map<String, HealthCheckConfig> configs = new ConcurrentHashMap<>();

    public enum HealthStatus {
        HEALTHY,
        UNHEALTHY,
        UNKNOWN,
        CHECKING
    }

    public void register(String skillId, HealthCheckConfig config) {
        configs.put(skillId, config);
        healthStatuses.put(skillId, HealthStatus.UNKNOWN);
        log.info("Registered health check for skill: {}", skillId);
    }

    public HealthStatus check(String skillId) {
        HealthCheckConfig config = configs.get(skillId);
        if (config == null) {
            return HealthStatus.UNKNOWN;
        }

        healthStatuses.put(skillId, HealthStatus.CHECKING);
        
        try {
            boolean healthy = performHealthCheck(skillId, config);
            HealthStatus status = healthy ? HealthStatus.HEALTHY : HealthStatus.UNHEALTHY;
            healthStatuses.put(skillId, status);
            return status;
        } catch (Exception e) {
            healthStatuses.put(skillId, HealthStatus.UNHEALTHY);
            log.error("Health check failed for skill {}: {}", skillId, e.getMessage());
            return HealthStatus.UNHEALTHY;
        }
    }

    private boolean performHealthCheck(String skillId, HealthCheckConfig config) {
        log.debug("Performing health check for skill {} at path {}", skillId, config.getPath());
        return true;
    }

    public HealthStatus getStatus(String skillId) {
        return healthStatuses.getOrDefault(skillId, HealthStatus.UNKNOWN);
    }

    public void unregister(String skillId) {
        configs.remove(skillId);
        healthStatuses.remove(skillId);
        log.info("Unregistered health check for skill: {}", skillId);
    }

    public Map<String, HealthStatus> getAllStatuses() {
        return new ConcurrentHashMap<>(healthStatuses);
    }

    public int getHealthyCount() {
        return (int) healthStatuses.values().stream()
                .filter(s -> s == HealthStatus.HEALTHY)
                .count();
    }

    public int getUnhealthyCount() {
        return (int) healthStatuses.values().stream()
                .filter(s -> s == HealthStatus.UNHEALTHY)
                .count();
    }
}

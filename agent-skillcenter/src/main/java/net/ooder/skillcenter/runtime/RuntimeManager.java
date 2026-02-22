package net.ooder.skillcenter.runtime;

import net.ooder.skillcenter.runtime.model.RuntimeConfig;
import net.ooder.skillcenter.runtime.model.RuntimeStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 运行时管理器 - 符合v0.7.0协议规范
 */
@Component
public class RuntimeManager {

    private static final Logger log = LoggerFactory.getLogger(RuntimeManager.class);

    @Autowired
    private RuntimeFactory runtimeFactory;

    private final Map<String, RuntimeStatus> runtimeStatuses = new ConcurrentHashMap<>();
    private final Map<String, RuntimeConfig> runtimeConfigs = new ConcurrentHashMap<>();
    private final Map<String, String> skillRuntimeMapping = new ConcurrentHashMap<>();

    public RuntimeStatus createRuntime(String skillId, RuntimeConfig config) {
        if (config == null || config.getLanguage() == null) {
            log.error("Invalid runtime config for skill: {}", skillId);
            return null;
        }

        RuntimeExecutor executor = runtimeFactory.getExecutor(config.getLanguage());
        if (executor == null) {
            log.error("Unsupported language: {} for skill: {}", config.getLanguage(), skillId);
            return null;
        }

        if (!executor.isSupported(config.getVersion())) {
            log.error("Unsupported version: {} for language: {}", config.getVersion(), config.getLanguage());
            return null;
        }

        String runtimeId = generateRuntimeId(skillId);
        RuntimeStatus status = executor.initialize(runtimeId, config);
        
        if (status != null) {
            runtimeStatuses.put(runtimeId, status);
            runtimeConfigs.put(runtimeId, config);
            skillRuntimeMapping.put(skillId, runtimeId);
            log.info("Created runtime {} for skill {} with language {}", runtimeId, skillId, config.getLanguage());
        }

        return status;
    }

    public RuntimeStatus startRuntime(String skillId) {
        String runtimeId = skillRuntimeMapping.get(skillId);
        if (runtimeId == null) {
            log.error("No runtime found for skill: {}", skillId);
            return null;
        }

        RuntimeConfig config = runtimeConfigs.get(runtimeId);
        if (config == null) {
            return null;
        }

        RuntimeExecutor executor = runtimeFactory.getExecutor(config.getLanguage());
        if (executor == null) {
            return null;
        }

        RuntimeStatus status = executor.start(runtimeId);
        if (status != null) {
            runtimeStatuses.put(runtimeId, status);
            log.info("Started runtime {} for skill {}", runtimeId, skillId);
        }
        return status;
    }

    public RuntimeStatus stopRuntime(String skillId) {
        String runtimeId = skillRuntimeMapping.get(skillId);
        if (runtimeId == null) {
            return null;
        }

        RuntimeConfig config = runtimeConfigs.get(runtimeId);
        if (config == null) {
            return null;
        }

        RuntimeExecutor executor = runtimeFactory.getExecutor(config.getLanguage());
        if (executor == null) {
            return null;
        }

        RuntimeStatus status = executor.stop(runtimeId);
        if (status != null) {
            runtimeStatuses.put(runtimeId, status);
            log.info("Stopped runtime {} for skill {}", runtimeId, skillId);
        }
        return status;
    }

    public RuntimeStatus getRuntimeStatus(String skillId) {
        String runtimeId = skillRuntimeMapping.get(skillId);
        if (runtimeId == null) {
            return null;
        }
        return runtimeStatuses.get(runtimeId);
    }

    public Object execute(String skillId, String method, Map<String, Object> params) {
        String runtimeId = skillRuntimeMapping.get(skillId);
        if (runtimeId == null) {
            log.error("No runtime found for skill: {}", skillId);
            return null;
        }

        RuntimeConfig config = runtimeConfigs.get(runtimeId);
        if (config == null) {
            return null;
        }

        RuntimeExecutor executor = runtimeFactory.getExecutor(config.getLanguage());
        if (executor == null) {
            return null;
        }

        return executor.execute(runtimeId, method, params);
    }

    public void destroyRuntime(String skillId) {
        String runtimeId = skillRuntimeMapping.get(skillId);
        if (runtimeId == null) {
            return;
        }

        RuntimeConfig config = runtimeConfigs.get(runtimeId);
        if (config != null) {
            RuntimeExecutor executor = runtimeFactory.getExecutor(config.getLanguage());
            if (executor != null) {
                executor.destroy(runtimeId);
            }
        }

        runtimeStatuses.remove(runtimeId);
        runtimeConfigs.remove(runtimeId);
        skillRuntimeMapping.remove(skillId);
        log.info("Destroyed runtime {} for skill {}", runtimeId, skillId);
    }

    public List<RuntimeStatus> getAllRuntimeStatuses() {
        return new ArrayList<>(runtimeStatuses.values());
    }

    public int getActiveRuntimeCount() {
        return (int) runtimeStatuses.values().stream()
                .filter(s -> s.getState() == RuntimeStatus.RuntimeState.RUNNING)
                .count();
    }

    private String generateRuntimeId(String skillId) {
        return "rt-" + skillId + "-" + System.currentTimeMillis();
    }
}

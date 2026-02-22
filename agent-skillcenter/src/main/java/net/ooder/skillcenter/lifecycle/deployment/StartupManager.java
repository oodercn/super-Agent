package net.ooder.skillcenter.lifecycle.deployment;

import net.ooder.skillcenter.lifecycle.deployment.model.StartupConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 启动管理器 - 符合v0.7.0协议规范
 */
@Component
public class StartupManager {

    private static final Logger log = LoggerFactory.getLogger(StartupManager.class);

    private final Map<String, StartupConfig> configs = new ConcurrentHashMap<>();
    private final Map<String, StartupState> states = new ConcurrentHashMap<>();
    private final Map<String, List<String>> dependencies = new ConcurrentHashMap<>();

    public enum StartupState {
        PENDING,
        STARTING,
        STARTED,
        FAILED,
        STOPPED
    }

    public void register(String skillId, StartupConfig config) {
        configs.put(skillId, config);
        states.put(skillId, StartupState.PENDING);
        log.info("Registered startup config for skill: {} with order {}", skillId, config.getOrder());
    }

    public void addDependency(String skillId, String dependsOn) {
        dependencies.computeIfAbsent(skillId, k -> new ArrayList<>()).add(dependsOn);
        log.debug("Added dependency: {} depends on {}", skillId, dependsOn);
    }

    public List<String> getStartupOrder() {
        List<Map.Entry<String, StartupConfig>> sorted = new ArrayList<>(configs.entrySet());
        sorted.sort(Comparator.comparingInt(e -> e.getValue().getOrder()));
        
        List<String> order = new ArrayList<>();
        Set<String> added = new HashSet<>();
        
        for (Map.Entry<String, StartupConfig> entry : sorted) {
            addInOrder(entry.getKey(), order, added);
        }
        
        return order;
    }

    private void addInOrder(String skillId, List<String> order, Set<String> added) {
        if (added.contains(skillId)) {
            return;
        }

        List<String> deps = dependencies.get(skillId);
        if (deps != null) {
            for (String dep : deps) {
                addInOrder(dep, order, added);
            }
        }

        order.add(skillId);
        added.add(skillId);
    }

    public boolean canStart(String skillId) {
        StartupConfig config = configs.get(skillId);
        if (config == null) {
            return true;
        }

        if (!config.isWaitForDependencies()) {
            return true;
        }

        List<String> deps = dependencies.get(skillId);
        if (deps == null || deps.isEmpty()) {
            return true;
        }

        for (String dep : deps) {
            StartupState state = states.get(dep);
            if (state != StartupState.STARTED) {
                log.debug("Cannot start {}: dependency {} not started", skillId, dep);
                return false;
            }
        }

        return true;
    }

    public void markStarting(String skillId) {
        states.put(skillId, StartupState.STARTING);
        log.info("Skill {} is starting", skillId);
    }

    public void markStarted(String skillId) {
        states.put(skillId, StartupState.STARTED);
        log.info("Skill {} started successfully", skillId);
    }

    public void markFailed(String skillId) {
        states.put(skillId, StartupState.FAILED);
        log.error("Skill {} failed to start", skillId);
    }

    public void markStopped(String skillId) {
        states.put(skillId, StartupState.STOPPED);
        log.info("Skill {} stopped", skillId);
    }

    public StartupState getState(String skillId) {
        return states.getOrDefault(skillId, StartupState.PENDING);
    }

    public void unregister(String skillId) {
        configs.remove(skillId);
        states.remove(skillId);
        dependencies.remove(skillId);
        log.info("Unregistered startup config for skill: {}", skillId);
    }

    public Map<String, StartupState> getAllStates() {
        return new ConcurrentHashMap<>(states);
    }
}

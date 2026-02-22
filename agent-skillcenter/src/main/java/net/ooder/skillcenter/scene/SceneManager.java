package net.ooder.skillcenter.scene;

import net.ooder.skillcenter.scene.model.SceneDefinition;
import net.ooder.skillcenter.scene.model.SceneRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 场景管理器 - 符合v0.7.0协议规范
 */
@Component
public class SceneManager {

    private static final Logger log = LoggerFactory.getLogger(SceneManager.class);

    private final Map<String, SceneDefinition> scenes = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> skillScenes = new ConcurrentHashMap<>();
    private final Map<String, SceneSession> activeSessions = new ConcurrentHashMap<>();

    public void registerScene(String sceneName, SceneDefinition scene) {
        if (sceneName == null || scene == null) {
            log.warn("Cannot register null scene");
            return;
        }
        scenes.put(sceneName, scene);
        log.debug("Registered scene: {}", sceneName);
    }

    public void registerSceneForSkill(String skillId, String sceneName) {
        SceneDefinition scene = scenes.get(sceneName);
        if (scene == null) {
            log.warn("Scene not found: {}", sceneName);
            return;
        }
        skillScenes.computeIfAbsent(skillId, k -> ConcurrentHashMap.newKeySet()).add(sceneName);
        log.debug("Registered scene {} for skill {}", sceneName, skillId);
    }

    public SceneDefinition getScene(String sceneName) {
        return scenes.get(sceneName);
    }

    public List<SceneDefinition> getScenesForSkill(String skillId) {
        Set<String> sceneNames = skillScenes.get(skillId);
        if (sceneNames == null || sceneNames.isEmpty()) {
            return Collections.emptyList();
        }
        return sceneNames.stream()
                .map(scenes::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public String startSession(String skillId, String sceneName, Map<String, Object> context) {
        SceneDefinition scene = scenes.get(sceneName);
        if (scene == null) {
            log.error("Scene not found: {}", sceneName);
            return null;
        }

        if (!scene.isAllowParallel()) {
            long activeCount = activeSessions.values().stream()
                    .filter(s -> s.sceneName.equals(sceneName) && s.active)
                    .count();
            if (activeCount > 0) {
                log.warn("Scene {} does not allow parallel execution", sceneName);
                return null;
            }
        }

        String sessionId = UUID.randomUUID().toString();
        SceneSession session = new SceneSession();
        session.sessionId = sessionId;
        session.skillId = skillId;
        session.sceneName = sceneName;
        session.context = context != null ? new HashMap<>(context) : new HashMap<>();
        session.active = true;
        session.startedAt = System.currentTimeMillis();

        activeSessions.put(sessionId, session);
        log.info("Started session {} for scene {}", sessionId, sceneName);
        return sessionId;
    }

    public void endSession(String sessionId) {
        SceneSession session = activeSessions.get(sessionId);
        if (session != null) {
            session.active = false;
            session.endedAt = System.currentTimeMillis();
            log.info("Ended session {} for scene {}", sessionId, session.sceneName);
        }
    }

    public Map<String, Object> getSessionContext(String sessionId) {
        SceneSession session = activeSessions.get(sessionId);
        return session != null ? session.context : null;
    }

    public void updateSessionContext(String sessionId, Map<String, Object> context) {
        SceneSession session = activeSessions.get(sessionId);
        if (session != null) {
            session.context.putAll(context);
        }
    }

    public List<SceneRole> getRequiredRoles(String sceneName) {
        SceneDefinition scene = scenes.get(sceneName);
        return scene != null ? scene.getRequiredRoles() : Collections.emptyList();
    }

    public boolean validateCapabilities(String sceneName, List<String> providedCapabilities) {
        SceneDefinition scene = scenes.get(sceneName);
        if (scene == null) return false;
        
        for (String required : scene.getCapabilities()) {
            if (!providedCapabilities.contains(required)) {
                log.warn("Missing required capability: {} for scene: {}", required, sceneName);
                return false;
            }
        }
        return true;
    }

    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalScenes", scenes.size());
        stats.put("skillsWithScenes", skillScenes.size());
        stats.put("activeSessions", activeSessions.values().stream().filter(s -> s.active).count());
        return stats;
    }

    public void unregisterScenesForSkill(String skillId) {
        skillScenes.remove(skillId);
        log.debug("Unregistered all scenes for skill: {}", skillId);
    }

    private static class SceneSession {
        String sessionId;
        String skillId;
        String sceneName;
        Map<String, Object> context;
        boolean active;
        long startedAt;
        long endedAt;
    }
}

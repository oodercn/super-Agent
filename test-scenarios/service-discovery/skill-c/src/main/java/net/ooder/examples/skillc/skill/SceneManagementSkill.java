package net.ooder.examples.skillc.skill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.ooder.sdk.skill.Skill;
import net.ooder.sdk.skill.SkillResult;
import net.ooder.sdk.skill.SkillStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SceneManagementSkill implements Skill {
    private static final Logger log = LoggerFactory.getLogger(SceneManagementSkill.class);
    private boolean initialized = false;
    private Map<String, Map<String, Object>> scenes = new ConcurrentHashMap<>();

    @Override
    public String getSkillId() {
        return "scene-management-skill";
    }

    @Override
    public String getName() {
        return "SceneManagementSkill";
    }

    @Override
    public String getDescription() {
        return "A skill for managing scenes and agent membership";
    }

    @Override
    public Map<String, String> getParameters() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("action", "The action to perform (create, join, leave, list, remove)");
        parameters.put("sceneId", "The scene ID");
        parameters.put("agentId", "The agent ID");
        return parameters;
    }

    @Override
    public SkillResult execute(Map<String, Object> params) {
        if (!initialized) {
            log.error("Skill not initialized");
            Map<String, Object> metadata = new HashMap<>();
            return SkillResult.failure("Skill not initialized", metadata);
        }

        try {
            String action = (String) params.get("action");
            if (action == null || action.isEmpty()) {
                Map<String, Object> metadata = new HashMap<>();
                return SkillResult.failure("Action parameter is required", metadata);
            }

            switch (action) {
                case "create":
                    return handleCreateScene(params);
                case "join":
                    return handleJoinScene(params);
                case "leave":
                    return handleLeaveScene(params);
                case "list":
                    return handleListScenes();
                case "remove":
                    return handleRemoveScene(params);
                default:
                    Map<String, Object> metadata = new HashMap<>();
                    return SkillResult.failure("Unknown action: " + action, metadata);
            }
        } catch (Exception e) {
            log.error("Error executing skill", e);
            Map<String, Object> metadata = new HashMap<>();
            return SkillResult.failure("Internal error: " + e.getMessage(), metadata);
        }
    }

    private SkillResult handleCreateScene(Map<String, Object> params) {
        String sceneId = (String) params.get("sceneId");
        if (sceneId == null || sceneId.isEmpty()) {
            Map<String, Object> metadata = new HashMap<>();
            return SkillResult.failure("Scene ID parameter is required", metadata);
        }

        if (scenes.containsKey(sceneId)) {
            Map<String, Object> metadata = new HashMap<>();
            return SkillResult.failure("Scene already exists: " + sceneId, metadata);
        }

        Map<String, Object> sceneData = new HashMap<>();
        sceneData.put("sceneId", sceneId);
        sceneData.put("createdAt", System.currentTimeMillis());
        sceneData.put("status", "active");
        sceneData.put("members", new HashMap<String, Object>());

        scenes.put(sceneId, sceneData);
        log.info("Created scene: {}", sceneId);

        Map<String, Object> resultData = new HashMap<>();
        resultData.put("sceneId", sceneId);
        resultData.put("status", "created");
        resultData.put("timestamp", System.currentTimeMillis());

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("message", "Scene created successfully");
        return SkillResult.success(resultData, metadata);
    }

    private SkillResult handleJoinScene(Map<String, Object> params) {
        String sceneId = (String) params.get("sceneId");
        String agentId = (String) params.get("agentId");

        if (sceneId == null || sceneId.isEmpty()) {
            Map<String, Object> metadata = new HashMap<>();
            return SkillResult.failure("Scene ID parameter is required", metadata);
        }

        if (agentId == null || agentId.isEmpty()) {
            Map<String, Object> metadata = new HashMap<>();
            return SkillResult.failure("Agent ID parameter is required", metadata);
        }

        if (!scenes.containsKey(sceneId)) {
            Map<String, Object> metadata = new HashMap<>();
            return SkillResult.failure("Scene not found: " + sceneId, metadata);
        }

        Map<String, Object> sceneData = scenes.get(sceneId);
        Map<String, Object> members = (Map<String, Object>) sceneData.get("members");
        members.put(agentId, System.currentTimeMillis());

        log.info("Agent {} joined scene: {}", agentId, sceneId);

        Map<String, Object> resultData = new HashMap<>();
        resultData.put("sceneId", sceneId);
        resultData.put("agentId", agentId);
        resultData.put("status", "joined");
        resultData.put("memberCount", members.size());

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("message", "Agent joined scene successfully");
        return SkillResult.success(resultData, metadata);
    }

    private SkillResult handleLeaveScene(Map<String, Object> params) {
        String sceneId = (String) params.get("sceneId");
        String agentId = (String) params.get("agentId");

        if (sceneId == null || sceneId.isEmpty()) {
            Map<String, Object> metadata = new HashMap<>();
            return SkillResult.failure("Scene ID parameter is required", metadata);
        }

        if (agentId == null || agentId.isEmpty()) {
            Map<String, Object> metadata = new HashMap<>();
            return SkillResult.failure("Agent ID parameter is required", metadata);
        }

        if (!scenes.containsKey(sceneId)) {
            Map<String, Object> metadata = new HashMap<>();
            return SkillResult.failure("Scene not found: " + sceneId, metadata);
        }

        Map<String, Object> sceneData = scenes.get(sceneId);
        Map<String, Object> members = (Map<String, Object>) sceneData.get("members");
        members.remove(agentId);

        log.info("Agent {} left scene: {}", agentId, sceneId);

        Map<String, Object> resultData = new HashMap<>();
        resultData.put("sceneId", sceneId);
        resultData.put("agentId", agentId);
        resultData.put("status", "left");
        resultData.put("memberCount", members.size());

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("message", "Agent left scene successfully");
        return SkillResult.success(resultData, metadata);
    }

    private SkillResult handleListScenes() {
        Map<String, Object> resultData = new HashMap<>();
        resultData.put("scenes", scenes);
        resultData.put("sceneCount", scenes.size());

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("message", "Scenes listed successfully");
        return SkillResult.success(resultData, metadata);
    }

    private SkillResult handleRemoveScene(Map<String, Object> params) {
        String sceneId = (String) params.get("sceneId");
        if (sceneId == null || sceneId.isEmpty()) {
            Map<String, Object> metadata = new HashMap<>();
            return SkillResult.failure("Scene ID parameter is required", metadata);
        }

        if (!scenes.containsKey(sceneId)) {
            Map<String, Object> metadata = new HashMap<>();
            return SkillResult.failure("Scene not found: " + sceneId, metadata);
        }

        scenes.remove(sceneId);
        log.info("Removed scene: {}", sceneId);

        Map<String, Object> resultData = new HashMap<>();
        resultData.put("sceneId", sceneId);
        resultData.put("status", "removed");
        resultData.put("timestamp", System.currentTimeMillis());

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("message", "Scene removed successfully");
        return SkillResult.success(resultData, metadata);
    }

    @Override
    public void initialize() {
        log.info("Initializing Scene Management Skill");
        // 初始化技能所需的资源
        initialized = true;
        log.info("Scene Management Skill initialized successfully");
    }

    @Override
    public void destroy() {
        log.info("Destroying Scene Management Skill");
        // 释放技能占用的资源
        scenes.clear();
        initialized = false;
        log.info("Scene Management Skill destroyed");
    }

    @Override
    public SkillStatus getStatus() {
        return initialized ? SkillStatus.READY : SkillStatus.UNINITIALIZED;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public int getSceneCount() {
        return scenes.size();
    }
}
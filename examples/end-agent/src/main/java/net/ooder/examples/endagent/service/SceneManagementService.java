package net.ooder.examples.endagent.service;

import net.ooder.examples.endagent.model.AiBridgeMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 场景管理服务
 * 负责场景的创建、加入、离开和查询等操作
 */
@Service
public class SceneManagementService {

    // 场景存储
    private final Map<String, SceneDefinition> scenes = new ConcurrentHashMap<>();
    
    // 场景成员存储
    private final Map<String, List<String>> sceneMembers = new ConcurrentHashMap<>();

    /**
     * 创建场景
     * @param sceneId 场景ID
     * @param sceneName 场景名称
     * @param description 场景描述
     * @param creator 创建者
     * @return 场景定义
     */
    public SceneDefinition createScene(String sceneId, String sceneName, String description, String creator) {
        SceneDefinition scene = new SceneDefinition();
        scene.setSceneId(sceneId);
        scene.setName(sceneName);
        scene.setDescription(description);
        scene.setCreator(creator);
        scene.setCreatedTime(System.currentTimeMillis());
        scene.setStatus("ACTIVE");
        
        scenes.put(sceneId, scene);
        sceneMembers.put(sceneId, new ArrayList<>());
        
        return scene;
    }

    /**
     * 加入场景
     * @param sceneId 场景ID
     * @param memberId 成员ID
     * @param role 角色
     * @return 是否成功
     */
    public boolean joinScene(String sceneId, String memberId, String role) {
        if (!scenes.containsKey(sceneId)) {
            return false;
        }
        
        List<String> members = sceneMembers.get(sceneId);
        if (!members.contains(memberId)) {
            members.add(memberId);
        }
        
        return true;
    }

    /**
     * 离开场景
     * @param sceneId 场景ID
     * @param memberId 成员ID
     * @return 是否成功
     */
    public boolean leaveScene(String sceneId, String memberId) {
        if (!scenes.containsKey(sceneId)) {
            return false;
        }
        
        List<String> members = sceneMembers.get(sceneId);
        return members.remove(memberId);
    }

    /**
     * 获取场景列表
     * @return 场景列表
     */
    public List<SceneDefinition> getScenes() {
        return new ArrayList<>(scenes.values());
    }

    /**
     * 获取场景详情
     * @param sceneId 场景ID
     * @return 场景定义
     */
    public SceneDefinition getScene(String sceneId) {
        return scenes.get(sceneId);
    }

    /**
     * 获取场景成员
     * @param sceneId 场景ID
     * @return 成员列表
     */
    public List<String> getSceneMembers(String sceneId) {
        return sceneMembers.getOrDefault(sceneId, new ArrayList<>());
    }

    /**
     * 删除场景
     * @param sceneId 场景ID
     * @return 是否成功
     */
    public boolean deleteScene(String sceneId) {
        scenes.remove(sceneId);
        sceneMembers.remove(sceneId);
        return true;
    }

    /**
     * 处理场景相关的消息
     * @param message 消息
     * @return 响应
     */
    public AiBridgeMessage handleSceneMessage(AiBridgeMessage message) {
        AiBridgeMessage response = new AiBridgeMessage();
        response.setMessageId(message.getMessageId());
        response.setTimestamp(System.currentTimeMillis());
        response.setResponseTo(message.getMessageId());
        response.setSource(message.getTarget());
        response.setTarget(message.getSource());
        
        String command = message.getCommand();
        Map<String, Object> params = message.getParams();
        
        try {
            switch (command) {
                case "scene.create":
                    handleSceneCreate(message, response, params);
                    break;
                case "scene.join":
                    handleSceneJoin(message, response, params);
                    break;
                case "scene.leave":
                    handleSceneLeave(message, response, params);
                    break;
                case "scene.query":
                    handleSceneQuery(message, response, params);
                    break;
                case "scene.delete":
                    handleSceneDelete(message, response, params);
                    break;
                default:
                    response.setStatus("error");
                    Map<String, Object> error = new HashMap<>();
                    error.put("code", 400);
                    error.put("message", "Unknown scene command");
                    response.setError(error);
            }
        } catch (Exception e) {
            response.setStatus("error");
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", e.getMessage());
            response.setError(error);
        }
        
        return response;
    }

    private void handleSceneCreate(AiBridgeMessage message, AiBridgeMessage response, Map<String, Object> params) {
        String sceneId = (String) params.get("sceneId");
        String sceneName = (String) params.get("sceneName");
        String description = (String) params.get("description");
        String creator = message.getSource();
        
        SceneDefinition scene = createScene(sceneId, sceneName, description, creator);
        
        Map<String, Object> result = new HashMap<>();
        result.put("scene", scene);
        response.setParams(result);
        response.setStatus("success");
    }

    private void handleSceneJoin(AiBridgeMessage message, AiBridgeMessage response, Map<String, Object> params) {
        String sceneId = (String) params.get("sceneId");
        String memberId = message.getSource();
        String role = (String) params.get("role");
        
        boolean success = joinScene(sceneId, memberId, role);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        response.setParams(result);
        response.setStatus(success ? "success" : "error");
    }

    private void handleSceneLeave(AiBridgeMessage message, AiBridgeMessage response, Map<String, Object> params) {
        String sceneId = (String) params.get("sceneId");
        String memberId = message.getSource();
        
        boolean success = leaveScene(sceneId, memberId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        response.setParams(result);
        response.setStatus(success ? "success" : "error");
    }

    private void handleSceneQuery(AiBridgeMessage message, AiBridgeMessage response, Map<String, Object> params) {
        String sceneId = (String) params.get("sceneId");
        
        if (sceneId != null) {
            // 查询单个场景
            SceneDefinition scene = getScene(sceneId);
            if (scene != null) {
                Map<String, Object> result = new HashMap<>();
                result.put("scene", scene);
                result.put("members", getSceneMembers(sceneId));
                response.setParams(result);
                response.setStatus("success");
            } else {
                response.setStatus("error");
                Map<String, Object> error = new HashMap<>();
                error.put("code", 404);
                error.put("message", "Scene not found");
                response.setError(error);
            }
        } else {
            // 查询所有场景
            List<SceneDefinition> sceneList = getScenes();
            Map<String, Object> result = new HashMap<>();
            result.put("scenes", sceneList);
            response.setParams(result);
            response.setStatus("success");
        }
    }

    private void handleSceneDelete(AiBridgeMessage message, AiBridgeMessage response, Map<String, Object> params) {
        String sceneId = (String) params.get("sceneId");
        
        boolean success = deleteScene(sceneId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        response.setParams(result);
        response.setStatus(success ? "success" : "error");
    }

    /**
     * 场景定义
     */
    public static class SceneDefinition {
        private String sceneId;
        private String name;
        private String description;
        private String creator;
        private long createdTime;
        private String status;
        private Map<String, Object> properties;

        // Getters and setters
        public String getSceneId() { return sceneId; }
        public void setSceneId(String sceneId) { this.sceneId = sceneId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getCreator() { return creator; }
        public void setCreator(String creator) { this.creator = creator; }
        public long getCreatedTime() { return createdTime; }
        public void setCreatedTime(long createdTime) { this.createdTime = createdTime; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public Map<String, Object> getProperties() { return properties; }
        public void setProperties(Map<String, Object> properties) { this.properties = properties; }
    }
}

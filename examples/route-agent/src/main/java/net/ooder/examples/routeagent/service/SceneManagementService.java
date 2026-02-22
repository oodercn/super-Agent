package net.ooder.examples.routeagent.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 场景管理服务
 * 负责 Route Agent 中的场景管理功能
 */
@Service
public class SceneManagementService {

    private static final Logger log = LoggerFactory.getLogger(SceneManagementService.class);
    
    // 场景存储
    private final Map<String, SceneDefinition> scenes = new ConcurrentHashMap<>();
    
    // 场景成员存储
    private final Map<String, List<String>> sceneMembers = new ConcurrentHashMap<>();
    
    // 场景与 End Agent 的映射
    private final Map<String, List<String>> sceneEndAgents = new ConcurrentHashMap<>();

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
        sceneEndAgents.put(sceneId, new ArrayList<>());
        
        log.info("Created scene: {} by {}", sceneId, creator);
        return scene;
    }

    /**
     * 加入场景
     * @param sceneId 场景ID
     * @param memberId 成员ID
     * @param memberType 成员类型
     * @param role 角色
     * @return 是否成功
     */
    public boolean joinScene(String sceneId, String memberId, String memberType, String role) {
        if (!scenes.containsKey(sceneId)) {
            log.warn("Scene not found: {}", sceneId);
            return false;
        }
        
        List<String> members = sceneMembers.get(sceneId);
        if (!members.contains(memberId)) {
            members.add(memberId);
            log.info("Member {} joined scene {}", memberId, sceneId);
        }
        
        // 如果是 End Agent，添加到场景终端映射
        if ("END_AGENT".equals(memberType)) {
            List<String> endAgents = sceneEndAgents.get(sceneId);
            if (!endAgents.contains(memberId)) {
                endAgents.add(memberId);
            }
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
        boolean removed = members.remove(memberId);
        
        if (removed) {
            log.info("Member {} left scene {}", memberId, sceneId);
        }
        
        // 从场景终端映射中移除
        List<String> endAgents = sceneEndAgents.get(sceneId);
        endAgents.remove(memberId);
        
        return removed;
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
     * 获取场景的 End Agent 列表
     * @param sceneId 场景ID
     * @return End Agent 列表
     */
    public List<String> getSceneEndAgents(String sceneId) {
        return sceneEndAgents.getOrDefault(sceneId, new ArrayList<>());
    }

    /**
     * 删除场景
     * @param sceneId 场景ID
     * @return 是否成功
     */
    public boolean deleteScene(String sceneId) {
        scenes.remove(sceneId);
        sceneMembers.remove(sceneId);
        sceneEndAgents.remove(sceneId);
        log.info("Deleted scene: {}", sceneId);
        return true;
    }

    /**
     * 更新场景状态
     * @param sceneId 场景ID
     * @param status 状态
     * @return 是否成功
     */
    public boolean updateSceneStatus(String sceneId, String status) {
        SceneDefinition scene = scenes.get(sceneId);
        if (scene != null) {
            scene.setStatus(status);
            log.info("Updated scene {} status to {}", sceneId, status);
            return true;
        }
        return false;
    }

    /**
     * 转发场景命令到 End Agent
     * @param sceneId 场景ID
     * @param command 命令
     * @param params 参数
     * @return 转发结果
     */
    public Map<String, Object> forwardSceneCommand(String sceneId, String command, Map<String, Object> params) {
        List<String> endAgents = getSceneEndAgents(sceneId);
        
        Map<String, Object> result = new ConcurrentHashMap<>();
        result.put("sceneId", sceneId);
        result.put("command", command);
        result.put("targets", endAgents);
        result.put("timestamp", System.currentTimeMillis());
        
        log.info("Forwarding scene command {} to {} end agents in scene {}", command, endAgents.size(), sceneId);
        
        return result;
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

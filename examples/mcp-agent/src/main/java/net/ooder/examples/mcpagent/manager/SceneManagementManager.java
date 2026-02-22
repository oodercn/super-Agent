package net.ooder.examples.mcpagent.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 场景管理管理器
 * 负责 MCP Agent 中的场景管理功能
 */
public class SceneManagementManager {

    private static final Logger log = LoggerFactory.getLogger(SceneManagementManager.class);
    
    // 场景存储
    private final Map<String, SceneDefinition> scenes = new ConcurrentHashMap<>();
    
    // 场景成员存储
    private final Map<String, List<String>> sceneMembers = new ConcurrentHashMap<>();
    
    // 场景与 Route Agent 的映射
    private final Map<String, List<String>> sceneRouteAgents = new ConcurrentHashMap<>();

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
        sceneRouteAgents.put(sceneId, new ArrayList<>());
        
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
        
        // 如果是 Route Agent，添加到场景路由映射
        if ("ROUTE_AGENT".equals(memberType)) {
            List<String> routeAgents = sceneRouteAgents.get(sceneId);
            if (!routeAgents.contains(memberId)) {
                routeAgents.add(memberId);
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
        
        // 从场景路由映射中移除
        List<String> routeAgents = sceneRouteAgents.get(sceneId);
        routeAgents.remove(memberId);
        
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
     * 获取场景的 Route Agent 列表
     * @param sceneId 场景ID
     * @return Route Agent 列表
     */
    public List<String> getSceneRouteAgents(String sceneId) {
        return sceneRouteAgents.getOrDefault(sceneId, new ArrayList<>());
    }

    /**
     * 删除场景
     * @param sceneId 场景ID
     * @return 是否成功
     */
    public boolean deleteScene(String sceneId) {
        scenes.remove(sceneId);
        sceneMembers.remove(sceneId);
        sceneRouteAgents.remove(sceneId);
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

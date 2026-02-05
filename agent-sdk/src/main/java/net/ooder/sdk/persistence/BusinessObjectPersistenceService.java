package net.ooder.sdk.persistence;

import net.ooder.sdk.agent.model.AgentConfig;
import net.ooder.sdk.agent.scene.SceneDefinition;
import net.ooder.sdk.agent.scene.SceneState;
import net.ooder.sdk.skill.SkillStatus;
import net.ooder.sdk.network.packet.LinkInfo;
import net.ooder.sdk.system.config.AgentProperties;
import net.ooder.sdk.system.security.Permission;
import net.ooder.sdk.storage.StorageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * 业务基础对象持久化服务，用于管理系统中核心业务对象的持久化存储
 */
public class BusinessObjectPersistenceService {

    private static final Logger log = LoggerFactory.getLogger(BusinessObjectPersistenceService.class);

    /**
     * 存储键前缀
     */
    private static final String SCENE_PREFIX = "business/scene/";
    private static final String SKILL_PREFIX = "business/skill/";
    private static final String ROUTE_PREFIX = "business/route/";
    private static final String SECURITY_PREFIX = "business/security/";
    private static final String CONFIG_PREFIX = "business/config/";

    /**
     * 单例实例
     */
    private static final BusinessObjectPersistenceService instance = new BusinessObjectPersistenceService();

    /**
     * 私有构造函数
     */
    private BusinessObjectPersistenceService() {
    }

    /**
     * 获取单例实例
     * @return 业务对象持久化服务实例
     */
    public static BusinessObjectPersistenceService getInstance() {
        return instance;
    }

    // ==================== 场景管理相关 ====================

    /**
     * 保存场景定义
     * @param sceneDefinition 场景定义
     * @return 是否保存成功
     */
    public boolean saveSceneDefinition(SceneDefinition sceneDefinition) {
        try {
            String key = SCENE_PREFIX + "definition/" + sceneDefinition.getSceneId();
            return StorageManager.getInstance().getJsonStorage().save(key, sceneDefinition);
        } catch (Exception e) {
            log.error("Failed to save scene definition: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 加载场景定义
     * @param sceneId 场景ID
     * @return 场景定义
     */
    public SceneDefinition loadSceneDefinition(String sceneId) {
        try {
            String key = SCENE_PREFIX + "definition/" + sceneId;
            return StorageManager.getInstance().getJsonStorage().load(key, SceneDefinition.class);
        } catch (Exception e) {
            log.error("Failed to load scene definition: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 保存场景状态
     * @param sceneState 场景状态
     * @return 是否保存成功
     */
    public boolean saveSceneState(SceneState sceneState) {
        try {
            String key = SCENE_PREFIX + "state/" + sceneState.getSceneId();
            return StorageManager.getInstance().getJsonStorage().save(key, sceneState);
        } catch (Exception e) {
            log.error("Failed to save scene state: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 加载场景状态
     * @param sceneId 场景ID
     * @return 场景状态
     */
    public SceneState loadSceneState(String sceneId) {
        try {
            String key = SCENE_PREFIX + "state/" + sceneId;
            return StorageManager.getInstance().getJsonStorage().load(key, SceneState.class);
        } catch (Exception e) {
            log.error("Failed to load scene state: {}", e.getMessage(), e);
            return null;
        }
    }

    // ==================== 技能相关 ====================

    /**
     * 保存技能状态
     * @param skillId 技能ID
     * @param status 技能状态
     * @return 是否保存成功
     */
    public boolean saveSkillStatus(String skillId, SkillStatus status) {
        try {
            String key = SKILL_PREFIX + "status/" + skillId;
            return StorageManager.getInstance().getJsonStorage().save(key, status);
        } catch (Exception e) {
            log.error("Failed to save skill status: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 加载技能状态
     * @param skillId 技能ID
     * @return 技能状态
     */
    public SkillStatus loadSkillStatus(String skillId) {
        try {
            String key = SKILL_PREFIX + "status/" + skillId;
            return StorageManager.getInstance().getJsonStorage().load(key, SkillStatus.class);
        } catch (Exception e) {
            log.error("Failed to load skill status: {}", e.getMessage(), e);
            return null;
        }
    }

    // ==================== 路由相关 ====================

    /**
     * 保存链路信息
     * @param linkInfo 链路信息
     * @return 是否保存成功
     */
    public boolean saveLinkInfo(LinkInfo linkInfo) {
        try {
            String key = ROUTE_PREFIX + "link/" + System.currentTimeMillis();
            return StorageManager.getInstance().getJsonStorage().save(key, linkInfo);
        } catch (Exception e) {
            log.error("Failed to save link info: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 加载链路信息
     * @param linkId 链路ID
     * @return 链路信息
     */
    public LinkInfo loadLinkInfo(String linkId) {
        try {
            String key = ROUTE_PREFIX + "link/" + linkId;
            return StorageManager.getInstance().getJsonStorage().load(key, LinkInfo.class);
        } catch (Exception e) {
            log.error("Failed to load link info: {}", e.getMessage(), e);
            return null;
        }
    }

    // ==================== 安全相关 ====================

    /**
     * 保存权限信息
     * @param permission 权限信息
     * @return 是否保存成功
     */
    public boolean savePermission(Permission permission) {
        try {
            String key = SECURITY_PREFIX + "permission/" + permission.getId();
            return StorageManager.getInstance().getJsonStorage().save(key, permission);
        } catch (Exception e) {
            log.error("Failed to save permission: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 加载权限信息
     * @param permissionId 权限ID
     * @return 权限信息
     */
    public Permission loadPermission(String permissionId) {
        try {
            String key = SECURITY_PREFIX + "permission/" + permissionId;
            return StorageManager.getInstance().getJsonStorage().load(key, Permission.class);
        } catch (Exception e) {
            log.error("Failed to load permission: {}", e.getMessage(), e);
            return null;
        }
    }

    // ==================== 配置相关 ====================

    /**
     * 保存代理属性
     * @param properties 代理属性
     * @return 是否保存成功
     */
    public boolean saveAgentProperties(AgentProperties properties) {
        try {
            String key = CONFIG_PREFIX + "agent/properties";
            return StorageManager.getInstance().getJsonStorage().save(key, properties);
        } catch (Exception e) {
            log.error("Failed to save agent properties: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 加载代理属性
     * @return 代理属性
     */
    public AgentProperties loadAgentProperties() {
        try {
            String key = CONFIG_PREFIX + "agent/properties";
            return StorageManager.getInstance().getJsonStorage().load(key, AgentProperties.class);
        } catch (Exception e) {
            log.error("Failed to load agent properties: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 保存代理配置
     * @param config 代理配置
     * @return 是否保存成功
     */
    public boolean saveAgentConfig(AgentConfig config) {
        try {
            String key = CONFIG_PREFIX + "agent/config";
            return StorageManager.getInstance().getJsonStorage().save(key, config);
        } catch (Exception e) {
            log.error("Failed to save agent config: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 加载代理配置
     * @return 代理配置
     */
    public AgentConfig loadAgentConfig() {
        try {
            String key = CONFIG_PREFIX + "agent/config";
            return StorageManager.getInstance().getJsonStorage().load(key, AgentConfig.class);
        } catch (Exception e) {
            log.error("Failed to load agent config: {}", e.getMessage(), e);
            return null;
        }
    }

    // ==================== 批量操作 ====================

    /**
     * 批量保存业务对象
     * @param objects 业务对象映射，键为存储路径，值为对象
     * @return 是否全部保存成功
     */
    public boolean saveBatch(Map<String, Object> objects) {
        if (objects == null || objects.isEmpty()) {
            return true;
        }

        boolean allSuccess = true;
        for (Map.Entry<String, Object> entry : objects.entrySet()) {
            try {
                if (!StorageManager.getInstance().getJsonStorage().save(entry.getKey(), entry.getValue())) {
                    allSuccess = false;
                }
            } catch (Exception e) {
                log.error("Failed to save object: {}", e.getMessage(), e);
                allSuccess = false;
            }
        }
        return allSuccess;
    }

    /**
     * 异步批量保存业务对象
     * @param objects 业务对象映射
     * @return 保存结果的CompletableFuture
     */
    public CompletableFuture<Boolean> saveBatchAsync(Map<String, Object> objects) {
        return CompletableFuture.supplyAsync(() -> saveBatch(objects));
    }

    // ==================== 清理操作 ====================

    /**
     * 清理过期的业务对象
     * @param maxAge 最大保留时间（毫秒）
     * @return 清理的记录数量
     */
    public int cleanExpiredObjects(long maxAge) {
        try {
            // 这里简化实现，实际应该根据具体对象类型进行清理
            return 0;
        } catch (Exception e) {
            log.error("Failed to clean expired objects: {}", e.getMessage(), e);
            return 0;
        }
    }
}

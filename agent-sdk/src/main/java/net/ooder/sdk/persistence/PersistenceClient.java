package net.ooder.sdk.persistence;

import net.ooder.sdk.agent.model.AgentConfig;
import net.ooder.sdk.network.packet.LinkInfo;
import net.ooder.sdk.agent.scene.SceneDefinition;
import net.ooder.sdk.agent.scene.SceneState;
import net.ooder.sdk.skill.SkillStatus;
import net.ooder.sdk.system.config.AgentProperties;
import net.ooder.sdk.system.security.Permission;
import net.ooder.sdk.storage.JsonStorage;
import net.ooder.sdk.storage.StorageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 统一持久化客户端，所有持久化操作的单一入口点
 */
public class PersistenceClient {
    private static final Logger log = LoggerFactory.getLogger(PersistenceClient.class);
    
    /**
     * 单例实例
     */
    private static final PersistenceClient instance = new PersistenceClient();
    
    /**
     * 存储服务
     */
    private final JsonStorage storage;
    
    /**
     * 业务对象持久化服务
     */
    private final BusinessObjectPersistenceService businessService;
    
    /**
     * 私有构造函数
     */
    private PersistenceClient() {
        this.storage = StorageManager.getInstance().getJsonStorage();
        this.businessService = BusinessObjectPersistenceService.getInstance();
    }
    
    /**
     * 获取单例实例
     * @return 持久化客户端实例
     */
    public static PersistenceClient getInstance() {
        return instance;
    }
    
    /**
     * 获取存储服务
     * @return 存储服务实例
     */
    public JsonStorage getStorage() {
        return storage;
    }
    
    // ==================== 场景管理相关 ====================
    
    /**
     * 保存场景定义
     * @param sceneDefinition 场景定义
     * @return 是否保存成功
     */
    public boolean saveSceneDefinition(SceneDefinition sceneDefinition) {
        return businessService.saveSceneDefinition(sceneDefinition);
    }
    
    /**
     * 加载场景定义
     * @param sceneId 场景ID
     * @return 场景定义
     */
    public SceneDefinition loadSceneDefinition(String sceneId) {
        return businessService.loadSceneDefinition(sceneId);
    }
    
    /**
     * 保存场景状态
     * @param sceneState 场景状态
     * @return 是否保存成功
     */
    public boolean saveSceneState(SceneState sceneState) {
        return businessService.saveSceneState(sceneState);
    }
    
    /**
     * 加载场景状态
     * @param sceneId 场景ID
     * @return 场景状态
     */
    public SceneState loadSceneState(String sceneId) {
        return businessService.loadSceneState(sceneId);
    }
    
    // ==================== 技能相关 ====================
    
    /**
     * 保存技能状态
     * @param skillId 技能ID
     * @param status 技能状态
     * @return 是否保存成功
     */
    public boolean saveSkillStatus(String skillId, SkillStatus status) {
        return businessService.saveSkillStatus(skillId, status);
    }
    
    /**
     * 加载技能状态
     * @param skillId 技能ID
     * @return 技能状态
     */
    public SkillStatus loadSkillStatus(String skillId) {
        return businessService.loadSkillStatus(skillId);
    }
    
    // ==================== 路由相关 ====================
    
    /**
     * 保存链路信息
     * @param linkInfo 链路信息
     * @return 是否保存成功
     */
    public boolean saveLinkInfo(LinkInfo linkInfo) {
        return businessService.saveLinkInfo(linkInfo);
    }
    
    /**
     * 加载链路信息
     * @param linkId 链路ID
     * @return 链路信息
     */
    public LinkInfo loadLinkInfo(String linkId) {
        return businessService.loadLinkInfo(linkId);
    }
    
    // ==================== 安全相关 ====================
    
    /**
     * 保存权限信息
     * @param permission 权限信息
     * @return 是否保存成功
     */
    public boolean savePermission(Permission permission) {
        return businessService.savePermission(permission);
    }
    
    /**
     * 加载权限信息
     * @param permissionId 权限ID
     * @return 权限信息
     */
    public Permission loadPermission(String permissionId) {
        return businessService.loadPermission(permissionId);
    }
    
    // ==================== 配置相关 ====================
    
    /**
     * 保存代理属性
     * @param properties 代理属性
     * @return 是否保存成功
     */
    public boolean saveAgentProperties(AgentProperties properties) {
        return businessService.saveAgentProperties(properties);
    }
    
    /**
     * 加载代理属性
     * @return 代理属性
     */
    public AgentProperties loadAgentProperties() {
        return businessService.loadAgentProperties();
    }
    
    /**
     * 保存代理配置
     * @param config 代理配置
     * @return 是否保存成功
     */
    public boolean saveAgentConfig(AgentConfig config) {
        return businessService.saveAgentConfig(config);
    }
    
    /**
     * 加载代理配置
     * @return 代理配置
     */
    public AgentConfig loadAgentConfig() {
        return businessService.loadAgentConfig();
    }
    
    // ==================== 批量操作 ====================
    
    /**
     * 批量保存业务对象
     * @param objects 业务对象映射，键为存储路径，值为对象
     * @return 是否全部保存成功
     */
    public boolean saveBatch(Map<String, Object> objects) {
        return businessService.saveBatch(objects);
    }
    
    /**
     * 异步批量保存业务对象
     * @param objects 业务对象映射
     * @return 保存结果的CompletableFuture
     */
    public CompletableFuture<Boolean> saveBatchAsync(Map<String, Object> objects) {
        return businessService.saveBatchAsync(objects);
    }
    
    // ==================== 清理操作 ====================
    
    /**
     * 清理过期的业务对象
     * @param maxAge 最大保留时间（毫秒）
     * @return 清理的记录数量
     */
    public int cleanExpiredObjects(long maxAge) {
        return businessService.cleanExpiredObjects(maxAge);
    }
    
    // ==================== 角色转换 ====================
    
    /**
     * 转换为MCP持久化客户端
     * @return MCP持久化客户端
     */
    public McpPersistenceClient asMcpClient() {
        return new McpPersistenceClientImpl(this);
    }
    
    /**
     * 转换为路由持久化客户端
     * @param routeAgentId 路由代理ID
     * @return 路由持久化客户端
     */
    public RoutePersistenceClient asRouteClient(String routeAgentId) {
        return new RoutePersistenceClientImpl(this, routeAgentId);
    }
    
    /**
     * 转换为终端持久化客户端
     * @param endAgentId 终端代理ID
     * @return 终端持久化客户端
     */
    public EndPersistenceClient asEndClient(String endAgentId) {
        return new EndPersistenceClientImpl(this, endAgentId);
    }
}

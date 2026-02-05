package net.ooder.sdk.agent;

import net.ooder.sdk.network.packet.CommandPacket;
import net.ooder.sdk.network.packet.TaskPacket;
import net.ooder.sdk.network.packet.ResponsePacket;
import net.ooder.sdk.network.udp.SendResult;
import net.ooder.sdk.system.enums.EndAgentStatus;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface EndAgent {
    
    /**
     * 获取 Agent ID
     */
    String getAgentId();
    
    /**
     * 获取 Agent 名称
     */
    String getAgentName();
    
    /**
     * 获取 Agent 类型
     */
    String getAgentType();
    
    /**
     * 获取 Agent 状态
     */
    EndAgentStatus getStatus();
    
    /**
     * 获取 Agent 能力
     */
    Map<String, Object> getCapabilities();
    
    /**
     * 注册 CAP
     */
    CompletableFuture<Boolean> registerCAP(String capKey, Map<String, Object> capInfo);
    
    /**
     * 更新 CAP
     */
    CompletableFuture<Boolean> updateCAP(String capKey, Map<String, Object> capInfo);
    
    /**
     * 查询 CAP
     */
    CompletableFuture<Map<String, Object>> getCAP(String capKey);
    
    /**
     * 删除 CAP
     */
    CompletableFuture<Boolean> deleteCAP(String capKey);
    
    /**
     * 获取所有 CAP
     */
    Map<String, Map<String, Object>> getAllCAPs();
    
    /**
     * 注册技能
     */
    CompletableFuture<Boolean> registerSkill(String skillId, Map<String, Object> skillInfo);
    
    /**
     * 更新技能
     */
    CompletableFuture<Boolean> updateSkill(String skillId, Map<String, Object> skillInfo);
    
    /**
     * 查询技能
     */
    CompletableFuture<Map<String, Object>> getSkill(String skillId);
    
    /**
     * 删除技能
     */
    CompletableFuture<Boolean> deleteSkill(String skillId);
    
    /**
     * 获取所有技能
     */
    Map<String, Map<String, Object>> getAllSkills();
    
    /**
     * 注册场景技能
     */
    CompletableFuture<Boolean> registerSceneSkill(String sceneId, String skillId, Map<String, Object> skillInfo);
    
    /**
     * 查询场景技能
     */
    CompletableFuture<Map<String, Map<String, Object>>> getSceneSkills(String sceneId);
    
    /**
     * 验证场景技能
     */
    CompletableFuture<Boolean> validateSceneSkill(String sceneId, String skillId);
    
    /**
     * 执行场景技能
     */
    CompletableFuture<ResponsePacket> executeSceneSkill(String sceneId, String skillId, Map<String, Object> params);
    
    /**
     * 生成新密钥
     */
    CompletableFuture<Map<String, String>> generateNewKeys();
    
    /**
     * 轮换密钥
     */
    CompletableFuture<Boolean> rotateKeys();
    
    /**
     * 获取当前密钥状态
     */
    Map<String, String> getKeyStatus();
    
    /**
     * 设置密钥轮换周期
     */
    void setKeyRotationPeriod(long period, java.util.concurrent.TimeUnit unit);
    
    /**
     * 启动自动密钥轮换
     */
    CompletableFuture<Boolean> startAutoKeyRotation();
    
    /**
     * 停止自动密钥轮换
     */
    void stopAutoKeyRotation();
    
    /**
     * 执行任务
     */
    CompletableFuture<ResponsePacket> executeTask(TaskPacket taskPacket);
    
    /**
     * 执行命令
     */
    CompletableFuture<ResponsePacket> executeCommand(CommandPacket commandPacket);
    
    /**
     * 缓存命令
     */
    void cacheCommand(CommandPacket commandPacket);
    
    /**
     * 获取命令缓存状态
     */
    int getCommandCacheSize();
    
    /**
     * 处理缓存命令
     */
    void processCachedCommands();
    
    /**
     * 同步 Skill 状态
     */
    void syncSkillStatus();
    
    /**
     * 启动 Agent
     */
    CompletableFuture<Boolean> start();
    
    /**
     * 停止 Agent
     */
    CompletableFuture<Boolean> stop();
    
    /**
     * 检查 Agent 是否在线
     */
    boolean isOnline();
    
    /**
     * 更新 Agent 状态
     */
    void updateStatus(EndAgentStatus status);
}

package net.ooder.skill.chat.service;

import java.util.List;
import java.util.Map;

/**
 * Skills Context Service Interface
 * Skills 上下文服务接口
 * 
 * 负责管理 AI 助手的上下文信息，包括：
 * - 用户身份（我是谁）
 * - 当前模块功能（我干什么）
 * - 可用 API 方法（我有哪些方法可以调用）
 */
public interface SkillsContextService {

    /**
     * 初始化用户上下文
     * @param userId 用户ID
     * @param sessionId 会话ID
     */
    void initializeContext(String userId, String sessionId);

    /**
     * 获取用户身份信息
     * @param userId 用户ID
     * @return 身份信息
     */
    Map<String, Object> getUserIdentity(String userId);

    /**
     * 获取模块信息
     * @param skillId 技能ID
     * @return 模块信息列表
     */
    List<Map<String, Object>> getModuleInfo(String skillId);

    /**
     * 获取当前模块上下文
     * @param sessionId 会话ID
     * @return 模块上下文
     */
    Map<String, Object> getCurrentModuleContext(String sessionId);

    /**
     * 注册技能上下文
     * @param skillId 技能ID
     * @param skillInfo 技能信息
     */
    void registerSkillContext(String skillId, Map<String, Object> skillInfo);

    /**
     * 更新页面状态
     * @param sessionId 会话ID
     * @param module 模块名
     * @param state 状态
     */
    void updatePageState(String sessionId, String module, Map<String, Object> state);

    /**
     * 获取页面状态
     * @param sessionId 会话ID
     * @param module 模块名
     * @return 页面状态
     */
    Map<String, Object> getPageState(String sessionId, String module);

    /**
     * 获取知识库上下文
     * @param query 查询内容
     * @param limit 限制数量
     * @return 知识库内容
     */
    List<String> getKnowledgeContext(String query, int limit);

    /**
     * 构建 AI 助手系统提示
     * @param userId 用户ID
     * @param skillId 技能ID
     * @return 系统提示
     */
    String buildSystemPrompt(String userId, String skillId);
}

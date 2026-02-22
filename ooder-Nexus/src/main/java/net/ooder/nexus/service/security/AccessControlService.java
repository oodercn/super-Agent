package net.ooder.nexus.service.security;

import java.util.List;
import java.util.Map;

/**
 * 访问控制服务接口
 * 提供 IP 白名单/黑名单、API 访问规则、角色权限管理功能
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since 0.7.3
 */
public interface AccessControlService {

    /**
     * 获取 IP 白名单列表
     * @return 白名单列表
     */
    List<Map<String, Object>> getWhitelist();

    /**
     * 添加 IP 到白名单
     * @param ipData IP 数据
     * @return 添加结果
     */
    Map<String, Object> addToWhitelist(Map<String, Object> ipData);

    /**
     * 从白名单移除 IP
     * @param id 记录 ID
     * @return 是否成功
     */
    boolean removeFromWhitelist(String id);

    /**
     * 获取 IP 黑名单列表
     * @return 黑名单列表
     */
    List<Map<String, Object>> getBlacklist();

    /**
     * 添加 IP 到黑名单
     * @param ipData IP 数据
     * @return 添加结果
     */
    Map<String, Object> addToBlacklist(Map<String, Object> ipData);

    /**
     * 从黑名单解封 IP
     * @param id 记录 ID
     * @return 是否成功
     */
    boolean unbanFromBlacklist(String id);

    /**
     * 获取 API 访问规则列表
     * @return API 规则列表
     */
    List<Map<String, Object>> getApiRules();

    /**
     * 添加或更新 API 访问规则
     * @param ruleData 规则数据
     * @return 规则数据
     */
    Map<String, Object> saveApiRule(Map<String, Object> ruleData);

    /**
     * 删除 API 访问规则
     * @param id 规则 ID
     * @return 是否成功
     */
    boolean deleteApiRule(String id);

    /**
     * 获取角色列表
     * @return 角色列表
     */
    List<Map<String, Object>> getRoles();

    /**
     * 添加或更新角色
     * @param roleData 角色数据
     * @return 角色数据
     */
    Map<String, Object> saveRole(Map<String, Object> roleData);

    /**
     * 删除角色
     * @param id 角色 ID
     * @return 是否成功
     */
    boolean deleteRole(String id);

    /**
     * 检查 IP 是否被允许访问
     * @param ip IP 地址
     * @return 是否允许
     */
    boolean isIpAllowed(String ip);

    /**
     * 检查用户是否有 API 访问权限
     * @param userId 用户 ID
     * @param apiPath API 路径
     * @return 是否允许
     */
    boolean hasApiAccess(String userId, String apiPath);
}

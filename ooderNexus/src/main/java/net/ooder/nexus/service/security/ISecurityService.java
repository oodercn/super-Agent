package net.ooder.nexus.service.security;

import net.ooder.nexus.model.Result;
import net.ooder.nexus.model.security.SecurityStatus;
import net.ooder.nexus.model.security.UserInfo;
import net.ooder.nexus.model.security.PermissionsData;
import net.ooder.nexus.model.security.SecurityLogsResult;

import java.util.List;
import java.util.Map;

/**
 * 安全管理服务接口
 * 负责用户管理、权限管理、安全监控等功能
 */
public interface ISecurityService {
    
    /**
     * 获取安全状态
     * @return 安全状态
     */
    Result<SecurityStatus> getSecurityStatus();

    /**
     * 获取用户列表
     * @return 用户列表
     */
    Result<List<UserInfo>> getUsers();

    /**
     * 添加用户
     * @param userData 用户数据
     * @return 添加结果
     */
    Result<UserInfo> addUser(Map<String, Object> userData);

    /**
     * 编辑用户
     * @param userId 用户 ID
     * @param userData 用户数据
     * @return 编辑结果
     */
    Result<UserInfo> editUser(String userId, Map<String, Object> userData);

    /**
     * 删除用户
     * @param userId 用户 ID
     * @return 删除结果
     */
    Result<UserInfo> deleteUser(String userId);

    /**
     * 启用用户
     * @param userId 用户 ID
     * @return 启用结果
     */
    Result<UserInfo> enableUser(String userId);

    /**
     * 禁用用户
     * @param userId 用户 ID
     * @return 禁用结果
     */
    Result<UserInfo> disableUser(String userId);

    /**
     * 获取权限列表
     * @return 权限列表
     */
    Result<List<PermissionsData>> getPermissions();

    /**
     * 保存权限设置
     * @param permissions 权限设置
     * @return 保存结果
     */
    Result<PermissionsData> savePermissions(Map<String, Object> permissions);

    /**
     * 获取安全日志
     * @return 安全日志
     */
    Result<SecurityLogsResult> getSecurityLogs();
}

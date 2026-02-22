package net.ooder.nexus.service;

import net.ooder.nexus.model.Result;
import net.ooder.nexus.domain.security.model.SecurityStatus;
import net.ooder.nexus.domain.security.model.UserInfo;
import net.ooder.nexus.domain.security.model.PermissionsData;
import net.ooder.nexus.domain.security.model.SecurityLogsResult;

import java.util.List;
import java.util.Map;

/**
 * 安全管理服务接口
 * 提供安全状态、用户管理、权限管理、安全日志等功能
 */
public interface SecurityService {

    /**
     * 获取安全状态
     */
    Result<SecurityStatus> getSecurityStatus();

    /**
     * 获取用户列表
     */
    Result<List<UserInfo>> getUsers();

    /**
     * 添加用户
     */
    Result<UserInfo> addUser(Map<String, Object> userData);

    /**
     * 编辑用户
     */
    Result<UserInfo> editUser(String userId, Map<String, Object> userData);

    /**
     * 删除用户
     */
    Result<UserInfo> deleteUser(String userId);

    /**
     * 启用用户
     */
    Result<UserInfo> enableUser(String userId);

    /**
     * 禁用用户
     */
    Result<UserInfo> disableUser(String userId);

    /**
     * 获取权限列表
     */
    Result<List<PermissionsData>> getPermissions();

    /**
     * 保存权限设置
     */
    Result<PermissionsData> savePermissions(Map<String, Object> permissions);

    /**
     * 获取安全日志
     */
    Result<SecurityLogsResult> getSecurityLogs();
}

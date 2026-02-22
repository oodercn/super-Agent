package net.ooder.nexus.provider;

import net.ooder.scene.core.Result;
import net.ooder.scene.core.PageResult;
import net.ooder.scene.core.SceneEngine;
import net.ooder.scene.provider.BaseProvider;

import java.util.List;
import java.util.Map;

/**
 * 用户管理Provider接口
 *
 * <p>定义用户管理相关的操作接口</p>
 * <p>注：此接口在 scene-engine 0.7.3 中不存在，由 ooderNexus 自行定义</p>
 * <p>与 SecurityProvider 不同，此接口专注于用户和权限管理</p>
 */
public interface UserProvider extends BaseProvider {

    /**
     * 获取用户状态
     */
    Result<UserStatus> getStatus();

    /**
     * 分页获取用户列表
     */
    Result<PageResult<UserInfo>> listUsers(int page, int size);

    /**
     * 获取用户信息
     */
    Result<UserInfo> getUser(String userId);

    /**
     * 创建用户
     */
    Result<UserInfo> createUser(Map<String, Object> userData);

    /**
     * 更新用户
     */
    Result<UserInfo> updateUser(String userId, Map<String, Object> userData);

    /**
     * 删除用户
     */
    Result<Boolean> deleteUser(String userId);

    /**
     * 启用用户
     */
    Result<UserInfo> enableUser(String userId);

    /**
     * 禁用用户
     */
    Result<UserInfo> disableUser(String userId);

    /**
     * 分页获取权限列表
     */
    Result<PageResult<Permission>> listPermissions(int page, int size);

    /**
     * 保存用户权限
     */
    Result<Boolean> savePermissions(String userId, List<String> permissions);

    /**
     * 获取用户权限
     */
    Result<List<Permission>> getUserPermissions(String userId);

    /**
     * 分页获取安全日志
     */
    Result<PageResult<SecurityLog>> listSecurityLogs(int page, int size);

    /**
     * 用户状态
     */
    class UserStatus {
        private int totalUsers;
        private int activeUsers;
        private int disabledUsers;
        private long lastUpdated;

        public int getTotalUsers() { return totalUsers; }
        public void setTotalUsers(int totalUsers) { this.totalUsers = totalUsers; }
        public int getActiveUsers() { return activeUsers; }
        public void setActiveUsers(int activeUsers) { this.activeUsers = activeUsers; }
        public int getDisabledUsers() { return disabledUsers; }
        public void setDisabledUsers(int disabledUsers) { this.disabledUsers = disabledUsers; }
        public long getLastUpdated() { return lastUpdated; }
        public void setLastUpdated(long lastUpdated) { this.lastUpdated = lastUpdated; }
    }

    /**
     * 用户信息
     */
    class UserInfo {
        private String userId;
        private String username;
        private String email;
        private String status;
        private List<String> roles;
        private long createdAt;
        private long updatedAt;

        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public List<String> getRoles() { return roles; }
        public void setRoles(List<String> roles) { this.roles = roles; }
        public long getCreatedAt() { return createdAt; }
        public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
        public long getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
    }

    /**
     * 权限
     */
    class Permission {
        private String permissionId;
        private String name;
        private String resource;
        private String action;
        private String description;

        public String getPermissionId() { return permissionId; }
        public void setPermissionId(String permissionId) { this.permissionId = permissionId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getResource() { return resource; }
        public void setResource(String resource) { this.resource = resource; }
        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    /**
     * 安全日志
     */
    class SecurityLog {
        private String logId;
        private String userId;
        private String action;
        private String resource;
        private String result;
        private String ipAddress;
        private long timestamp;

        public String getLogId() { return logId; }
        public void setLogId(String logId) { this.logId = logId; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }
        public String getResource() { return resource; }
        public void setResource(String resource) { this.resource = resource; }
        public String getResult() { return result; }
        public void setResult(String result) { this.result = result; }
        public String getIpAddress() { return ipAddress; }
        public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }
}

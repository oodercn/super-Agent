package net.ooder.nexus.service.south;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 登录协议适配器接口
 *
 * <p>提供本地认证能力，支持登录、会话管理。</p>
 *
 * @author ooder Team
 * @version 2.0
 * @since SDK 0.7.2
 */
public interface LoginProtocolAdapter {

    /**
     * 登录
     *
     * @param request 登录请求
     * @return 登录结果
     */
    CompletableFuture<LoginResultDTO> login(LoginRequestDTO request);

    /**
     * 登出
     *
     * @param sessionId 会话 ID
     * @return 登出结果
     */
    CompletableFuture<Void> logout(String sessionId);

    /**
     * 自动登录
     *
     * @param config 自动登录配置
     * @return 登录结果
     */
    CompletableFuture<LoginResultDTO> autoLogin(AutoLoginConfigDTO config);

    /**
     * 获取会话
     *
     * @param sessionId 会话 ID
     * @return 会话信息
     */
    CompletableFuture<SessionDTO> getSession(String sessionId);

    /**
     * 验证会话
     *
     * @param sessionId 会话 ID
     * @return 会话信息
     */
    CompletableFuture<SessionDTO> validateSession(String sessionId);

    /**
     * 刷新会话
     *
     * @param sessionId 会话 ID
     * @return 刷新结果
     */
    CompletableFuture<Void> refreshSession(String sessionId);

    /**
     * 获取域策略
     *
     * @param userId 用户 ID
     * @return 域策略
     */
    CompletableFuture<DomainPolicyDTO> getDomainPolicy(String userId);

    /**
     * 添加登录事件监听器
     *
     * @param listener 监听器
     */
    void addLoginListener(LoginEventListener listener);

    /**
     * 移除登录事件监听器
     *
     * @param listener 监听器
     */
    void removeLoginListener(LoginEventListener listener);

    /**
     * 登录请求 DTO
     */
    class LoginRequestDTO {
        private String username;
        private String password;
        private String domain;
        private boolean rememberMe;
        private java.util.Map<String, Object> extra;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getDomain() { return domain; }
        public void setDomain(String domain) { this.domain = domain; }
        public boolean isRememberMe() { return rememberMe; }
        public void setRememberMe(boolean rememberMe) { this.rememberMe = rememberMe; }
        public java.util.Map<String, Object> getExtra() { return extra; }
        public void setExtra(java.util.Map<String, Object> extra) { this.extra = extra; }
    }

    /**
     * 登录结果 DTO
     */
    class LoginResultDTO {
        private boolean success;
        private String sessionId;
        private String userId;
        private String userName;
        private String token;
        private long expiresAt;
        private List<DomainDTO> domains;
        private DomainPolicyDTO policy;
        private String errorMessage;
        private String errorCode;

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getSessionId() { return sessionId; }
        public void setSessionId(String sessionId) { this.sessionId = sessionId; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getUserName() { return userName; }
        public void setUserName(String userName) { this.userName = userName; }
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        public long getExpiresAt() { return expiresAt; }
        public void setExpiresAt(long expiresAt) { this.expiresAt = expiresAt; }
        public List<DomainDTO> getDomains() { return domains; }
        public void setDomains(List<DomainDTO> domains) { this.domains = domains; }
        public DomainPolicyDTO getPolicy() { return policy; }
        public void setPolicy(DomainPolicyDTO policy) { this.policy = policy; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
        public String getErrorCode() { return errorCode; }
        public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    }

    /**
     * 自动登录配置 DTO
     */
    class AutoLoginConfigDTO {
        private String userId;
        private String token;
        private boolean refreshOnExpiry;

        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        public boolean isRefreshOnExpiry() { return refreshOnExpiry; }
        public void setRefreshOnExpiry(boolean refreshOnExpiry) { this.refreshOnExpiry = refreshOnExpiry; }
    }

    /**
     * 会话 DTO
     */
    class SessionDTO {
        private String sessionId;
        private String userId;
        private String userName;
        private String domainId;
        private long createdAt;
        private long expiresAt;
        private long lastActiveAt;
        private String status;

        public String getSessionId() { return sessionId; }
        public void setSessionId(String sessionId) { this.sessionId = sessionId; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getUserName() { return userName; }
        public void setUserName(String userName) { this.userName = userName; }
        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
        public long getCreatedAt() { return createdAt; }
        public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
        public long getExpiresAt() { return expiresAt; }
        public void setExpiresAt(long expiresAt) { this.expiresAt = expiresAt; }
        public long getLastActiveAt() { return lastActiveAt; }
        public void setLastActiveAt(long lastActiveAt) { this.lastActiveAt = lastActiveAt; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public boolean isExpired() {
            return System.currentTimeMillis() > expiresAt;
        }
    }

    /**
     * 域 DTO
     */
    class DomainDTO {
        private String domainId;
        private String domainName;
        private String domainType;
        private String role;

        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
        public String getDomainName() { return domainName; }
        public void setDomainName(String domainName) { this.domainName = domainName; }
        public String getDomainType() { return domainType; }
        public void setDomainType(String domainType) { this.domainType = domainType; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }

    /**
     * 域策略 DTO
     */
    class DomainPolicyDTO {
        private String domainId;
        private List<String> allowedSkills;
        private List<String> requiredSkills;
        private java.util.Map<String, Object> storageConfig;
        private java.util.Map<String, Object> securityConfig;
        private java.util.Map<String, Object> networkConfig;

        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
        public List<String> getAllowedSkills() { return allowedSkills; }
        public void setAllowedSkills(List<String> allowedSkills) { this.allowedSkills = allowedSkills; }
        public List<String> getRequiredSkills() { return requiredSkills; }
        public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; }
        public java.util.Map<String, Object> getStorageConfig() { return storageConfig; }
        public void setStorageConfig(java.util.Map<String, Object> storageConfig) { this.storageConfig = storageConfig; }
        public java.util.Map<String, Object> getSecurityConfig() { return securityConfig; }
        public void setSecurityConfig(java.util.Map<String, Object> securityConfig) { this.securityConfig = securityConfig; }
        public java.util.Map<String, Object> getNetworkConfig() { return networkConfig; }
        public void setNetworkConfig(java.util.Map<String, Object> networkConfig) { this.networkConfig = networkConfig; }
    }

    /**
     * 登录事件监听器
     */
    interface LoginEventListener {
        void onLoginSuccess(LoginResultDTO result);
        void onLoginFailure(String errorCode, String errorMessage);
        void onLogout(String sessionId);
        void onSessionExpired(String sessionId);
        void onPolicyApplied(String domainId, DomainPolicyDTO policy);
    }
}

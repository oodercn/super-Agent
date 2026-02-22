package net.ooder.nexus.service.south;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 登录协议接口
 *
 * <p>SDK 0.7.2 南向协议，提供登录认证能力。</p>
 *
 * @author ooder Team
 * @version 2.0
 * @since SDK 0.7.2
 */
public interface LoginProtocol {

    CompletableFuture<LoginResult> login(LoginRequest request);

    CompletableFuture<Void> logout(String sessionId);

    CompletableFuture<SessionInfo> getSession(String sessionId);

    CompletableFuture<SessionInfo> validateSession(String sessionId);

    CompletableFuture<Void> refreshSession(String sessionId);

    CompletableFuture<DomainPolicy> getDomainPolicy(String userId);

    void addLoginListener(LoginListener listener);

    void removeLoginListener(LoginListener listener);

    class LoginRequest {
        private String username;
        private String password;
        private String domain;
        private boolean rememberMe;
        private Map<String, Object> extra;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getDomain() { return domain; }
        public void setDomain(String domain) { this.domain = domain; }
        public boolean isRememberMe() { return rememberMe; }
        public void setRememberMe(boolean rememberMe) { this.rememberMe = rememberMe; }
        public Map<String, Object> getExtra() { return extra; }
        public void setExtra(Map<String, Object> extra) { this.extra = extra; }
    }

    class LoginResult {
        private boolean success;
        private String sessionId;
        private String userId;
        private String userName;
        private String token;
        private long expiresAt;
        private List<DomainInfo> domains;
        private DomainPolicy policy;
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
        public List<DomainInfo> getDomains() { return domains; }
        public void setDomains(List<DomainInfo> domains) { this.domains = domains; }
        public DomainPolicy getPolicy() { return policy; }
        public void setPolicy(DomainPolicy policy) { this.policy = policy; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
        public String getErrorCode() { return errorCode; }
        public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    }

    class SessionInfo {
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

    class DomainInfo {
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

    class DomainPolicy {
        private String domainId;
        private List<String> allowedSkills;
        private List<String> requiredSkills;
        private Map<String, Object> storageConfig;
        private Map<String, Object> securityConfig;
        private Map<String, Object> networkConfig;

        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
        public List<String> getAllowedSkills() { return allowedSkills; }
        public void setAllowedSkills(List<String> allowedSkills) { this.allowedSkills = allowedSkills; }
        public List<String> getRequiredSkills() { return requiredSkills; }
        public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; }
        public Map<String, Object> getStorageConfig() { return storageConfig; }
        public void setStorageConfig(Map<String, Object> storageConfig) { this.storageConfig = storageConfig; }
        public Map<String, Object> getSecurityConfig() { return securityConfig; }
        public void setSecurityConfig(Map<String, Object> securityConfig) { this.securityConfig = securityConfig; }
        public Map<String, Object> getNetworkConfig() { return networkConfig; }
        public void setNetworkConfig(Map<String, Object> networkConfig) { this.networkConfig = networkConfig; }
    }

    interface LoginListener {
        void onLoginSuccess(LoginResult result);
        void onLoginFailure(String errorCode, String errorMessage);
        void onLogout(String sessionId);
        void onSessionExpired(String sessionId);
        void onPolicyApplied(String domainId, DomainPolicy policy);
    }
}

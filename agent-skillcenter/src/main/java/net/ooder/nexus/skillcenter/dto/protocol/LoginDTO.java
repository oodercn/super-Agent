package net.ooder.nexus.skillcenter.dto.protocol;

import java.util.List;
import java.util.Map;

public class LoginDTO {

    public static class LoginRequestDTO {
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

    public static class LoginResultDTO {
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

    public static class SessionDTO {
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
    }

    public static class DomainDTO {
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

    public static class DomainPolicyDTO {
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

    public static class AutoLoginConfigDTO {
        private String userId;
        private String credential;
        private String domain;

        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getCredential() { return credential; }
        public void setCredential(String credential) { this.credential = credential; }
        public String getDomain() { return domain; }
        public void setDomain(String domain) { this.domain = domain; }
    }

    public static class CredentialDTO {
        private String userId;
        private String credential;
        private long savedAt;
        private long expiresAt;

        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getCredential() { return credential; }
        public void setCredential(String credential) { this.credential = credential; }
        public long getSavedAt() { return savedAt; }
        public void setSavedAt(long savedAt) { this.savedAt = savedAt; }
        public long getExpiresAt() { return expiresAt; }
        public void setExpiresAt(long expiresAt) { this.expiresAt = expiresAt; }
    }
}

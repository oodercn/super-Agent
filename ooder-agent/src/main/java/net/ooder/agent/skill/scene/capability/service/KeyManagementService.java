package net.ooder.mvp.skill.scene.capability.service;

import java.util.List;
import java.util.Map;

public interface KeyManagementService {
    
    KeyInfo generateKey(KeyGenerateRequest request);
    
    KeyInfo getKey(String keyId);
    
    boolean validateKey(String keyId, String scope);
    
    boolean revokeKey(String keyId);
    
    KeyInfo refreshKey(String keyId);
    
    List<KeyInfo> getKeysByUser(String userId);
    
    List<KeyInfo> getKeysByScene(String sceneGroupId);
    
    KeyAccessResult accessResource(String keyId, String resource, String action);
    
    List<KeyInfo> getAllKeys();
    
    public static class KeyGenerateRequest {
        private String userId;
        private String sceneGroupId;
        private String installId;
        private String scope;
        private long expireTimeMs;
        private Map<String, Object> permissions;
        private String description;
        private String keyName;
        private String keyType;
        private String provider;
        private List<String> allowedUsers;
        private List<String> allowedRoles;
        private List<String> allowedScenes;
        
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getSceneGroupId() { return sceneGroupId; }
        public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
        public String getInstallId() { return installId; }
        public void setInstallId(String installId) { this.installId = installId; }
        public String getScope() { return scope; }
        public void setScope(String scope) { this.scope = scope; }
        public long getExpireTimeMs() { return expireTimeMs; }
        public void setExpireTimeMs(long expireTimeMs) { this.expireTimeMs = expireTimeMs; }
        public Map<String, Object> getPermissions() { return permissions; }
        public void setPermissions(Map<String, Object> permissions) { this.permissions = permissions; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getKeyName() { return keyName; }
        public void setKeyName(String keyName) { this.keyName = keyName; }
        public String getKeyType() { return keyType; }
        public void setKeyType(String keyType) { this.keyType = keyType; }
        public String getProvider() { return provider; }
        public void setProvider(String provider) { this.provider = provider; }
        public List<String> getAllowedUsers() { return allowedUsers; }
        public void setAllowedUsers(List<String> allowedUsers) { this.allowedUsers = allowedUsers; }
        public List<String> getAllowedRoles() { return allowedRoles; }
        public void setAllowedRoles(List<String> allowedRoles) { this.allowedRoles = allowedRoles; }
        public List<String> getAllowedScenes() { return allowedScenes; }
        public void setAllowedScenes(List<String> allowedScenes) { this.allowedScenes = allowedScenes; }
    }
    
    public static class KeyInfo {
        private String keyId;
        private String keyValue;
        private String userId;
        private String sceneGroupId;
        private String installId;
        private String scope;
        private KeyStatus status;
        private long createTime;
        private long expireTime;
        private long lastAccessTime;
        private int accessCount;
        private Map<String, Object> permissions;
        private String description;
        private String keyName;
        private String keyType;
        private String provider;
        private List<String> allowedUsers;
        private List<String> allowedRoles;
        private List<String> allowedScenes;
        private long updatedAt;
        
        public enum KeyStatus {
            ACTIVE,
            INACTIVE,
            EXPIRED,
            REVOKED,
            SUSPENDED
        }
        
        public boolean isExpired() {
            return expireTime > 0 && System.currentTimeMillis() > expireTime;
        }
        
        public boolean isActive() {
            return status == KeyStatus.ACTIVE && !isExpired();
        }
        
        public String getKeyId() { return keyId; }
        public void setKeyId(String keyId) { this.keyId = keyId; }
        public String getKeyValue() { return keyValue; }
        public void setKeyValue(String keyValue) { this.keyValue = keyValue; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getSceneGroupId() { return sceneGroupId; }
        public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
        public String getInstallId() { return installId; }
        public void setInstallId(String installId) { this.installId = installId; }
        public String getScope() { return scope; }
        public void setScope(String scope) { this.scope = scope; }
        public KeyStatus getStatus() { return status; }
        public void setStatus(KeyStatus status) { this.status = status; }
        public long getCreateTime() { return createTime; }
        public void setCreateTime(long createTime) { this.createTime = createTime; }
        public long getExpireTime() { return expireTime; }
        public void setExpireTime(long expireTime) { this.expireTime = expireTime; }
        public long getLastAccessTime() { return lastAccessTime; }
        public void setLastAccessTime(long lastAccessTime) { this.lastAccessTime = lastAccessTime; }
        public int getAccessCount() { return accessCount; }
        public void setAccessCount(int accessCount) { this.accessCount = accessCount; }
        public Map<String, Object> getPermissions() { return permissions; }
        public void setPermissions(Map<String, Object> permissions) { this.permissions = permissions; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getKeyName() { return keyName; }
        public void setKeyName(String keyName) { this.keyName = keyName; }
        public String getKeyType() { return keyType; }
        public void setKeyType(String keyType) { this.keyType = keyType; }
        public String getProvider() { return provider; }
        public void setProvider(String provider) { this.provider = provider; }
        public List<String> getAllowedUsers() { return allowedUsers; }
        public void setAllowedUsers(List<String> allowedUsers) { this.allowedUsers = allowedUsers; }
        public List<String> getAllowedRoles() { return allowedRoles; }
        public void setAllowedRoles(List<String> allowedRoles) { this.allowedRoles = allowedRoles; }
        public List<String> getAllowedScenes() { return allowedScenes; }
        public void setAllowedScenes(List<String> allowedScenes) { this.allowedScenes = allowedScenes; }
        public long getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
    }
    
    public static class KeyAccessResult {
        private boolean allowed;
        private String reason;
        private KeyInfo keyInfo;
        
        public static KeyAccessResult allowed(KeyInfo keyInfo) {
            KeyAccessResult result = new KeyAccessResult();
            result.setAllowed(true);
            result.setKeyInfo(keyInfo);
            result.setReason("Access granted");
            return result;
        }
        
        public static KeyAccessResult denied(String reason) {
            KeyAccessResult result = new KeyAccessResult();
            result.setAllowed(false);
            result.setReason(reason);
            return result;
        }
        
        public boolean isAllowed() { return allowed; }
        public void setAllowed(boolean allowed) { this.allowed = allowed; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
        public KeyInfo getKeyInfo() { return keyInfo; }
        public void setKeyInfo(KeyInfo keyInfo) { this.keyInfo = keyInfo; }
    }
}

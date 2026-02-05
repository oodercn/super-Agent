package net.ooder.skillcenter.security.service;

import java.util.Map;

public interface KeyManagementService {
    
    KeyGenerationResult generateKeyForAgent(String agentId, KeyType keyType, Map<String, Object> options);
    
    KeyDistributionResult distributeKeyToAgent(String keyId, String agentId);
    
    KeyValidationResult validateAgentKey(String agentId, String key);
    
    KeyRotationResult rotateAgentKey(String agentId, String keyId);
    
    KeyRevocationResult revokeAgentKey(String agentId, String keyId);
    
    java.util.List<KeyInfo> listAgentKeys(String agentId);
    
    KeyInfo getKeyInfo(String keyId);
    
    KeyAuditLog getKeyAuditLog(String keyId);
    
    java.util.List<KeyAuditLog> getAgentAuditLogs(String agentId);
    

    
    enum KeyType {
        API_KEY,
        SESSION_KEY,
        ACCESS_TOKEN,
        ENCRYPTION_KEY_PAIR,
        SYMMETRIC_KEY
    }
    
    class KeyGenerationResult {
        private boolean success;
        private String keyId;
        private String keyType;
        private String keyValue;
        private String agentId;
        private long expiresAt;
        private String message;
        
        public boolean isSuccess() {
            return success;
        }
        
        public void setSuccess(boolean success) {
            this.success = success;
        }
        
        public String getKeyId() {
            return keyId;
        }
        
        public void setKeyId(String keyId) {
            this.keyId = keyId;
        }
        
        public String getKeyType() {
            return keyType;
        }
        
        public void setKeyType(String keyType) {
            this.keyType = keyType;
        }
        
        public String getKeyValue() {
            return keyValue;
        }
        
        public void setKeyValue(String keyValue) {
            this.keyValue = keyValue;
        }
        
        public String getAgentId() {
            return agentId;
        }
        
        public void setAgentId(String agentId) {
            this.agentId = agentId;
        }
        
        public long getExpiresAt() {
            return expiresAt;
        }
        
        public void setExpiresAt(long expiresAt) {
            this.expiresAt = expiresAt;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
    }
    
    class KeyDistributionResult {
        private boolean success;
        private String keyId;
        private String agentId;
        private String distributionId;
        private String message;
        
        public boolean isSuccess() {
            return success;
        }
        
        public void setSuccess(boolean success) {
            this.success = success;
        }
        
        public String getKeyId() {
            return keyId;
        }
        
        public void setKeyId(String keyId) {
            this.keyId = keyId;
        }
        
        public String getAgentId() {
            return agentId;
        }
        
        public void setAgentId(String agentId) {
            this.agentId = agentId;
        }
        
        public String getDistributionId() {
            return distributionId;
        }
        
        public void setDistributionId(String distributionId) {
            this.distributionId = distributionId;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
    }
    
    class KeyValidationResult {
        private boolean valid;
        private String keyId;
        private String agentId;
        private String message;
        private long expiresAt;
        private Map<String, Object> claims;
        
        public boolean isValid() {
            return valid;
        }
        
        public void setValid(boolean valid) {
            this.valid = valid;
        }
        
        public String getKeyId() {
            return keyId;
        }
        
        public void setKeyId(String keyId) {
            this.keyId = keyId;
        }
        
        public String getAgentId() {
            return agentId;
        }
        
        public void setAgentId(String agentId) {
            this.agentId = agentId;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public long getExpiresAt() {
            return expiresAt;
        }
        
        public void setExpiresAt(long expiresAt) {
            this.expiresAt = expiresAt;
        }
        
        public Map<String, Object> getClaims() {
            return claims;
        }
        
        public void setClaims(Map<String, Object> claims) {
            this.claims = claims;
        }
    }
    
    class KeyRotationResult {
        private boolean success;
        private String oldKeyId;
        private String newKeyId;
        private String message;
        private long timestamp;
        
        public boolean isSuccess() {
            return success;
        }
        
        public void setSuccess(boolean success) {
            this.success = success;
        }
        
        public String getOldKeyId() {
            return oldKeyId;
        }
        
        public void setOldKeyId(String oldKeyId) {
            this.oldKeyId = oldKeyId;
        }
        
        public String getNewKeyId() {
            return newKeyId;
        }
        
        public void setNewKeyId(String newKeyId) {
            this.newKeyId = newKeyId;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public long getTimestamp() {
            return timestamp;
        }
        
        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }
    
    class KeyRevocationResult {
        private boolean success;
        private String keyId;
        private String agentId;
        private String message;
        private long revokedAt;
        
        public boolean isSuccess() {
            return success;
        }
        
        public void setSuccess(boolean success) {
            this.success = success;
        }
        
        public String getKeyId() {
            return keyId;
        }
        
        public void setKeyId(String keyId) {
            this.keyId = keyId;
        }
        
        public String getAgentId() {
            return agentId;
        }
        
        public void setAgentId(String agentId) {
            this.agentId = agentId;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public long getRevokedAt() {
            return revokedAt;
        }
        
        public void setRevokedAt(long revokedAt) {
            this.revokedAt = revokedAt;
        }
    }
    
    class KeyInfo {
        private String keyId;
        private String keyType;
        private String agentId;
        private String status;
        private long createdAt;
        private long expiresAt;
        private long lastUsedAt;
        private Map<String, Object> metadata;
        
        public String getKeyId() {
            return keyId;
        }
        
        public void setKeyId(String keyId) {
            this.keyId = keyId;
        }
        
        public String getKeyType() {
            return keyType;
        }
        
        public void setKeyType(String keyType) {
            this.keyType = keyType;
        }
        
        public String getAgentId() {
            return agentId;
        }
        
        public void setAgentId(String agentId) {
            this.agentId = agentId;
        }
        
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
        
        public long getCreatedAt() {
            return createdAt;
        }
        
        public void setCreatedAt(long createdAt) {
            this.createdAt = createdAt;
        }
        
        public long getExpiresAt() {
            return expiresAt;
        }
        
        public void setExpiresAt(long expiresAt) {
            this.expiresAt = expiresAt;
        }
        
        public long getLastUsedAt() {
            return lastUsedAt;
        }
        
        public void setLastUsedAt(long lastUsedAt) {
            this.lastUsedAt = lastUsedAt;
        }
        
        public Map<String, Object> getMetadata() {
            return metadata;
        }
        
        public void setMetadata(Map<String, Object> metadata) {
            this.metadata = metadata;
        }
    }
    
    class KeyAuditLog {
        private String logId;
        private String keyId;
        private String agentId;
        private String action;
        private String actor;
        private long timestamp;
        private String details;
        
        public String getLogId() {
            return logId;
        }
        
        public void setLogId(String logId) {
            this.logId = logId;
        }
        
        public String getKeyId() {
            return keyId;
        }
        
        public void setKeyId(String keyId) {
            this.keyId = keyId;
        }
        
        public String getAgentId() {
            return agentId;
        }
        
        public void setAgentId(String agentId) {
            this.agentId = agentId;
        }
        
        public String getAction() {
            return action;
        }
        
        public void setAction(String action) {
            this.action = action;
        }
        
        public String getActor() {
            return actor;
        }
        
        public void setActor(String actor) {
            this.actor = actor;
        }
        
        public long getTimestamp() {
            return timestamp;
        }
        
        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
        
        public String getDetails() {
            return details;
        }
        
        public void setDetails(String details) {
            this.details = details;
        }
    }
}

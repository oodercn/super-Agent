package net.ooder.skillcenter.security.service.impl;

import net.ooder.skillcenter.security.service.KeyManagementService;
import java.util.*;

public class KeyManagementServiceImpl implements KeyManagementService {
    
    private static KeyManagementServiceImpl instance;
    private Map<String, KeyInfo> keys;
    private Map<String, KeyAuditLog> auditLogs;
    
    public KeyManagementServiceImpl() {
        this.keys = new HashMap<>();
        this.auditLogs = new HashMap<>();
        loadSampleData();
    }
    
    public static KeyManagementService getInstance() {
        if (instance == null) {
            instance = new KeyManagementServiceImpl();
        }
        return instance;
    }
    
    @Override
    public KeyGenerationResult generateKeyForAgent(String agentId, KeyType keyType, Map<String, Object> options) {
        KeyGenerationResult result = new KeyGenerationResult();
        String keyId = "key-" + System.currentTimeMillis();
        
        KeyInfo keyInfo = new KeyInfo();
        keyInfo.setKeyId(keyId);
        keyInfo.setKeyType(keyType.name());
        keyInfo.setAgentId(agentId);
        keyInfo.setStatus("active");
        keyInfo.setCreatedAt(System.currentTimeMillis());
        
        Long ttl = options != null ? (Long) options.getOrDefault("ttl", 86400000L) : 86400000L;
        keyInfo.setExpiresAt(System.currentTimeMillis() + ttl);
        
        String keyValue = generateKeyValue(keyType);
        keyInfo.setMetadata(options);
        
        keys.put(keyId, keyInfo);
        
        KeyAuditLog auditLog = new KeyAuditLog();
        auditLog.setLogId("audit-" + System.currentTimeMillis());
        auditLog.setKeyId(keyId);
        auditLog.setAgentId(agentId);
        auditLog.setAction("generate");
        auditLog.setActor(System.getProperty("user.name"));
        auditLog.setTimestamp(System.currentTimeMillis());
        auditLog.setDetails("Generated key type: " + keyType.name());
        
        auditLogs.put(auditLog.getLogId(), auditLog);
        
        result.setSuccess(true);
        result.setKeyId(keyId);
        result.setKeyType(keyType.name());
        result.setKeyValue(keyValue);
        result.setAgentId(agentId);
        result.setExpiresAt(keyInfo.getExpiresAt());
        
        return result;
    }
    
    @Override
    public KeyDistributionResult distributeKeyToAgent(String keyId, String agentId) {
        KeyDistributionResult result = new KeyDistributionResult();
        
        if (!keys.containsKey(keyId)) {
            result.setSuccess(false);
            result.setMessage("Key not found: " + keyId);
            return result;
        }
        
        KeyAuditLog auditLog = new KeyAuditLog();
        auditLog.setLogId("audit-" + System.currentTimeMillis());
        auditLog.setKeyId(keyId);
        auditLog.setAgentId(agentId);
        auditLog.setAction("distribute");
        auditLog.setActor(System.getProperty("user.name"));
        auditLog.setTimestamp(System.currentTimeMillis());
        auditLog.setDetails("Distributed key to agent: " + agentId);
        
        auditLogs.put(auditLog.getLogId(), auditLog);
        
        result.setSuccess(true);
        result.setKeyId(keyId);
        result.setAgentId(agentId);
        result.setDistributionId("dist-" + System.currentTimeMillis());
        
        return result;
    }
    
    @Override
    public KeyValidationResult validateAgentKey(String agentId, String key) {
        KeyValidationResult result = new KeyValidationResult();
        
        for (KeyInfo keyInfo : keys.values()) {
            if (keyInfo.getAgentId().equals(agentId)) {
                if (keyInfo.getStatus().equals("active")) {
                    if (System.currentTimeMillis() < keyInfo.getExpiresAt()) {
                        result.setValid(true);
                        result.setKeyId(keyInfo.getKeyId());
                        result.setAgentId(agentId);
                        result.setExpiresAt(keyInfo.getExpiresAt());
                        result.setClaims(keyInfo.getMetadata());
                        return result;
                    }
                }
            }
        }
        
        result.setValid(false);
        result.setMessage("Invalid or expired key");
        return result;
    }
    
    @Override
    public KeyRotationResult rotateAgentKey(String agentId, String keyId) {
        KeyRotationResult result = new KeyRotationResult();
        
        if (!keys.containsKey(keyId)) {
            result.setSuccess(false);
            result.setMessage("Key not found: " + keyId);
            return result;
        }
        
        KeyInfo oldKey = keys.get(keyId);
        oldKey.setStatus("revoked");
        
        KeyInfo newKey = new KeyInfo();
        String newKeyId = "key-" + System.currentTimeMillis();
        newKey.setKeyId(newKeyId);
        newKey.setKeyType(oldKey.getKeyType());
        newKey.setAgentId(agentId);
        newKey.setStatus("active");
        newKey.setCreatedAt(System.currentTimeMillis());
        newKey.setExpiresAt(System.currentTimeMillis() + 86400000L);
        newKey.setMetadata(oldKey.getMetadata());
        
        keys.put(newKeyId, newKey);
        
        KeyAuditLog auditLog = new KeyAuditLog();
        auditLog.setLogId("audit-" + System.currentTimeMillis());
        auditLog.setKeyId(newKeyId);
        auditLog.setAgentId(agentId);
        auditLog.setAction("rotate");
        auditLog.setActor(System.getProperty("user.name"));
        auditLog.setTimestamp(System.currentTimeMillis());
        auditLog.setDetails("Rotated key: " + keyId + " -> " + newKeyId);
        
        auditLogs.put(auditLog.getLogId(), auditLog);
        
        result.setSuccess(true);
        result.setOldKeyId(keyId);
        result.setNewKeyId(newKeyId);
        result.setTimestamp(System.currentTimeMillis());
        
        return result;
    }
    
    @Override
    public KeyRevocationResult revokeAgentKey(String agentId, String keyId) {
        KeyRevocationResult result = new KeyRevocationResult();
        
        if (!keys.containsKey(keyId)) {
            result.setSuccess(false);
            result.setMessage("Key not found: " + keyId);
            return result;
        }
        
        KeyInfo keyInfo = keys.get(keyId);
        keyInfo.setStatus("revoked");
        
        KeyAuditLog auditLog = new KeyAuditLog();
        auditLog.setLogId("audit-" + System.currentTimeMillis());
        auditLog.setKeyId(keyId);
        auditLog.setAgentId(agentId);
        auditLog.setAction("revoke");
        auditLog.setActor(System.getProperty("user.name"));
        auditLog.setTimestamp(System.currentTimeMillis());
        auditLog.setDetails("Revoked key: " + keyId);
        
        auditLogs.put(auditLog.getLogId(), auditLog);
        
        result.setSuccess(true);
        result.setKeyId(keyId);
        result.setAgentId(agentId);
        result.setRevokedAt(System.currentTimeMillis());
        
        return result;
    }
    
    @Override
    public List<KeyInfo> listAgentKeys(String agentId) {
        List<KeyInfo> result = new ArrayList<>();
        for (KeyInfo keyInfo : keys.values()) {
            if (keyInfo.getAgentId().equals(agentId)) {
                result.add(keyInfo);
            }
        }
        return result;
    }
    
    @Override
    public KeyInfo getKeyInfo(String keyId) {
        return keys.get(keyId);
    }
    
    @Override
    public KeyAuditLog getKeyAuditLog(String keyId) {
        for (KeyAuditLog auditLog : auditLogs.values()) {
            if (auditLog.getKeyId().equals(keyId)) {
                return auditLog;
            }
        }
        return null;
    }
    
    @Override
    public List<KeyAuditLog> getAgentAuditLogs(String agentId) {
        List<KeyAuditLog> result = new ArrayList<>();
        for (KeyAuditLog auditLog : auditLogs.values()) {
            if (auditLog.getAgentId().equals(agentId)) {
                result.add(auditLog);
            }
        }
        return result;
    }
    
    private String generateKeyValue(KeyType keyType) {
        switch (keyType) {
            case API_KEY:
                return "api-" + UUID.randomUUID().toString().substring(0, 8);
            case SESSION_KEY:
                return "session-" + UUID.randomUUID().toString().substring(0, 8);
            case ACCESS_TOKEN:
                return "token-" + UUID.randomUUID().toString().substring(0, 8);
            case ENCRYPTION_KEY_PAIR:
                return "keypair-" + UUID.randomUUID().toString().substring(0, 8);
            case SYMMETRIC_KEY:
                return "symmetric-" + UUID.randomUUID().toString().substring(0, 8);
            default:
                return "default-" + UUID.randomUUID().toString().substring(0, 8);
        }
    }
    
    private void loadSampleData() {
        KeyInfo key1 = new KeyInfo();
        key1.setKeyId("key-sample-001");
        key1.setKeyType("API_KEY");
        key1.setAgentId("agent-001");
        key1.setStatus("active");
        key1.setCreatedAt(System.currentTimeMillis() - 86400000L);
        key1.setExpiresAt(System.currentTimeMillis() + 86400000L);
        keys.put(key1.getKeyId(), key1);
        
        KeyAuditLog audit1 = new KeyAuditLog();
        audit1.setLogId("audit-sample-001");
        audit1.setKeyId(key1.getKeyId());
        audit1.setAgentId(key1.getAgentId());
        audit1.setAction("generate");
        audit1.setActor("admin");
        audit1.setTimestamp(key1.getCreatedAt());
        audit1.setDetails("Sample audit log");
        auditLogs.put(audit1.getLogId(), audit1);
    }
}

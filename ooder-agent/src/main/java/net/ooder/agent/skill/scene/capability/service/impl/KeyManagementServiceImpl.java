package net.ooder.mvp.skill.scene.capability.service.impl;

import net.ooder.mvp.skill.scene.capability.service.KeyManagementService;
import net.ooder.skill.common.storage.JsonStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class KeyManagementServiceImpl implements KeyManagementService {

    private static final Logger log = LoggerFactory.getLogger(KeyManagementServiceImpl.class);

    private static final String KEY_PREFIX = "sk-scene-";
    private static final long DEFAULT_EXPIRE_MS = 7 * 24 * 60 * 60 * 1000L;
    private static final String KEYS_STORAGE_KEY = "key-management-keys";
    private static final String USER_KEYS_STORAGE_KEY = "key-management-user-keys";
    private static final String SCENE_KEYS_STORAGE_KEY = "key-management-scene-keys";
    
    private Map<String, KeyInfo> keyStore = new ConcurrentHashMap<>();
    private Map<String, List<String>> userKeys = new ConcurrentHashMap<>();
    private Map<String, List<String>> sceneKeys = new ConcurrentHashMap<>();
    
    private SecureRandom random = new SecureRandom();
    
    @Autowired(required = false)
    private JsonStorageService jsonStorageService;
    
    @PostConstruct
    public void init() {
        loadFromStorage();
    }
    
    @PreDestroy
    public void shutdown() {
        persistToStorage();
    }
    
    private void loadFromStorage() {
        if (jsonStorageService == null) {
            log.info("[loadFromStorage] JsonStorageService not available, using memory-only mode");
            return;
        }
        
        try {
            List<KeyInfo> storedKeys = jsonStorageService.loadList(KEYS_STORAGE_KEY, KeyInfo.class);
            if (storedKeys != null && !storedKeys.isEmpty()) {
                for (KeyInfo key : storedKeys) {
                    keyStore.put(key.getKeyId(), key);
                    
                    String userId = key.getUserId();
                    if (userId != null) {
                        userKeys.computeIfAbsent(userId, k -> new ArrayList<>()).add(key.getKeyId());
                    }
                    
                    String sceneGroupId = key.getSceneGroupId();
                    if (sceneGroupId != null) {
                        sceneKeys.computeIfAbsent(sceneGroupId, k -> new ArrayList<>()).add(key.getKeyId());
                    }
                }
                log.info("[loadFromStorage] Loaded {} keys from storage", keyStore.size());
            }
        } catch (Exception e) {
            log.error("[loadFromStorage] Failed to load keys: {}", e.getMessage());
        }
    }
    
    private void persistToStorage() {
        if (jsonStorageService == null) {
            return;
        }
        
        try {
            jsonStorageService.saveList(KEYS_STORAGE_KEY, new ArrayList<>(keyStore.values()));
            log.info("[persistToStorage] Saved {} keys to storage", keyStore.size());
        } catch (Exception e) {
            log.error("[persistToStorage] Failed to persist keys: {}", e.getMessage());
        }
    }
    
    private void saveOnChange() {
        persistToStorage();
    }

    @Override
    public KeyInfo generateKey(KeyGenerateRequest request) {
        log.info("[generateKey] Generating key for user: {}, scene: {}", 
            request.getUserId(), request.getSceneGroupId());
        
        String keyId = generateKeyId();
        String keyValue = generateKeyValue();
        
        KeyInfo keyInfo = new KeyInfo();
        keyInfo.setKeyId(keyId);
        keyInfo.setKeyValue(hashKey(keyValue));
        keyInfo.setUserId(request.getUserId());
        keyInfo.setSceneGroupId(request.getSceneGroupId());
        keyInfo.setInstallId(request.getInstallId());
        keyInfo.setScope(request.getScope() != null ? request.getScope() : "default");
        keyInfo.setStatus(KeyInfo.KeyStatus.ACTIVE);
        keyInfo.setCreateTime(System.currentTimeMillis());
        
        long expireTime = request.getExpireTimeMs() > 0 
            ? System.currentTimeMillis() + request.getExpireTimeMs() 
            : System.currentTimeMillis() + DEFAULT_EXPIRE_MS;
        keyInfo.setExpireTime(expireTime);
        keyInfo.setPermissions(request.getPermissions());
        keyInfo.setDescription(request.getDescription());
        keyInfo.setAccessCount(0);
        keyInfo.setKeyName(request.getKeyName());
        keyInfo.setKeyType(request.getKeyType() != null ? request.getKeyType() : request.getScope());
        keyInfo.setProvider(request.getProvider());
        keyInfo.setAllowedUsers(request.getAllowedUsers());
        keyInfo.setAllowedRoles(request.getAllowedRoles());
        keyInfo.setAllowedScenes(request.getAllowedScenes());
        keyInfo.setUpdatedAt(System.currentTimeMillis());
        
        keyStore.put(keyId, keyInfo);
        
        userKeys.computeIfAbsent(request.getUserId(), k -> new ArrayList<>()).add(keyId);
        if (request.getSceneGroupId() != null) {
            sceneKeys.computeIfAbsent(request.getSceneGroupId(), k -> new ArrayList<>()).add(keyId);
        }
        
        saveOnChange();
        
        log.info("[generateKey] Key generated: {} for user: {}", keyId, request.getUserId());
        
        KeyInfo result = new KeyInfo();
        result.setKeyId(keyId);
        result.setKeyValue(keyValue);
        result.setUserId(keyInfo.getUserId());
        result.setSceneGroupId(keyInfo.getSceneGroupId());
        result.setScope(keyInfo.getScope());
        result.setStatus(keyInfo.getStatus());
        result.setCreateTime(keyInfo.getCreateTime());
        result.setExpireTime(keyInfo.getExpireTime());
        result.setDescription(keyInfo.getDescription());
        result.setKeyName(keyInfo.getKeyName());
        result.setKeyType(keyInfo.getKeyType());
        result.setProvider(keyInfo.getProvider());
        result.setAllowedUsers(keyInfo.getAllowedUsers());
        result.setAllowedRoles(keyInfo.getAllowedRoles());
        result.setAllowedScenes(keyInfo.getAllowedScenes());
        
        return result;
    }

    @Override
    public KeyInfo getKey(String keyId) {
        KeyInfo keyInfo = keyStore.get(keyId);
        if (keyInfo == null) {
            return null;
        }
        
        if (keyInfo.isExpired() && keyInfo.getStatus() == KeyInfo.KeyStatus.ACTIVE) {
            keyInfo.setStatus(KeyInfo.KeyStatus.EXPIRED);
            saveOnChange();
            log.info("[getKey] Key expired: {}", keyId);
        }
        
        return keyInfo;
    }

    @Override
    public boolean validateKey(String keyId, String scope) {
        KeyInfo keyInfo = getKey(keyId);
        if (keyInfo == null) {
            log.warn("[validateKey] Key not found: {}", keyId);
            return false;
        }
        
        if (!keyInfo.isActive()) {
            log.warn("[validateKey] Key not active: {} (status={})", keyId, keyInfo.getStatus());
            return false;
        }
        
        if (scope != null && !scope.equals(keyInfo.getScope()) && !"default".equals(keyInfo.getScope())) {
            log.warn("[validateKey] Key scope mismatch: {} (expected={}, actual={})", 
                keyId, scope, keyInfo.getScope());
            return false;
        }
        
        return true;
    }

    @Override
    public boolean revokeKey(String keyId) {
        KeyInfo keyInfo = keyStore.get(keyId);
        if (keyInfo == null) {
            log.warn("[revokeKey] Key not found: {}", keyId);
            return false;
        }
        
        keyInfo.setStatus(KeyInfo.KeyStatus.REVOKED);
        saveOnChange();
        log.info("[revokeKey] Key revoked: {}", keyId);
        return true;
    }

    @Override
    public KeyInfo refreshKey(String keyId) {
        KeyInfo keyInfo = keyStore.get(keyId);
        if (keyInfo == null) {
            log.warn("[refreshKey] Key not found: {}", keyId);
            return null;
        }
        
        if (keyInfo.getStatus() == KeyInfo.KeyStatus.REVOKED) {
            log.warn("[refreshKey] Cannot refresh revoked key: {}", keyId);
            return null;
        }
        
        keyInfo.setExpireTime(System.currentTimeMillis() + DEFAULT_EXPIRE_MS);
        keyInfo.setStatus(KeyInfo.KeyStatus.ACTIVE);
        saveOnChange();
        
        log.info("[refreshKey] Key refreshed: {}, new expire time: {}", keyId, keyInfo.getExpireTime());
        return keyInfo;
    }

    @Override
    public List<KeyInfo> getKeysByUser(String userId) {
        List<KeyInfo> result = new ArrayList<>();
        List<String> keyIds = userKeys.get(userId);
        
        if (keyIds != null) {
            for (String keyId : keyIds) {
                KeyInfo keyInfo = getKey(keyId);
                if (keyInfo != null) {
                    result.add(keyInfo);
                }
            }
        }
        
        return result;
    }

    @Override
    public List<KeyInfo> getKeysByScene(String sceneGroupId) {
        List<KeyInfo> result = new ArrayList<>();
        List<String> keyIds = sceneKeys.get(sceneGroupId);
        
        if (keyIds != null) {
            for (String keyId : keyIds) {
                KeyInfo keyInfo = getKey(keyId);
                if (keyInfo != null) {
                    result.add(keyInfo);
                }
            }
        }
        
        return result;
    }

    @Override
    public List<KeyInfo> getAllKeys() {
        return new ArrayList<>(keyStore.values());
    }

    @Override
    public KeyAccessResult accessResource(String keyId, String resource, String action) {
        KeyInfo keyInfo = getKey(keyId);
        
        if (keyInfo == null) {
            return KeyAccessResult.denied("Key not found");
        }
        
        if (!keyInfo.isActive()) {
            return KeyAccessResult.denied("Key is not active: " + keyInfo.getStatus());
        }
        
        keyInfo.setLastAccessTime(System.currentTimeMillis());
        keyInfo.setAccessCount(keyInfo.getAccessCount() + 1);
        saveOnChange();
        
        Map<String, Object> permissions = keyInfo.getPermissions();
        if (permissions != null && !permissions.isEmpty()) {
            Boolean allowed = checkPermissions(permissions, resource, action);
            if (allowed != null && !allowed) {
                return KeyAccessResult.denied("Permission denied for action: " + action);
            }
        }
        
        return KeyAccessResult.allowed(keyInfo);
    }
    
    private Boolean checkPermissions(Map<String, Object> permissions, String resource, String action) {
        Object resourcePerms = permissions.get(resource);
        if (resourcePerms == null) {
            resourcePerms = permissions.get("*");
        }
        
        if (resourcePerms instanceof List) {
            List<?> actions = (List<?>) resourcePerms;
            return actions.contains(action) || actions.contains("*");
        }
        
        if (resourcePerms instanceof Boolean) {
            return (Boolean) resourcePerms;
        }
        
        return null;
    }
    
    private String generateKeyId() {
        return "key-" + System.currentTimeMillis() + "-" + randomHex(4);
    }
    
    private String generateKeyValue() {
        return KEY_PREFIX + randomHex(32);
    }
    
    private String randomHex(int length) {
        byte[] bytes = new byte[length / 2];
        random.nextBytes(bytes);
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    
    private String hashKey(String keyValue) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(keyValue.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return keyValue;
        }
    }
    
    public void cleanupExpiredKeys() {
        long now = System.currentTimeMillis();
        int cleaned = 0;
        
        for (KeyInfo keyInfo : keyStore.values()) {
            if (keyInfo.isExpired() && keyInfo.getStatus() == KeyInfo.KeyStatus.ACTIVE) {
                keyInfo.setStatus(KeyInfo.KeyStatus.EXPIRED);
                cleaned++;
            }
        }
        
        if (cleaned > 0) {
            saveOnChange();
            log.info("[cleanupExpiredKeys] Cleaned {} expired keys", cleaned);
        }
    }
}

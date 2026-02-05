package net.ooder.skillcenter.identity;

import net.ooder.skillcenter.personalai.IdentityManager;

import java.util.HashMap;
import java.util.Map;

/**
 * 身份存储，用于存储和管理身份相关的信息
 */
public class IdentityStorage {
    // 存储映射
    private Map<String, String> storage;
    
    // 恢复密钥
    private String recoveryKey;
    
    /**
     * 构造方法
     */
    public IdentityStorage() {
        this.storage = new HashMap<>();
    }
    
    /**
     * 存储DID
     * @param did DID
     */
    public void storeDID(String did) {
        storage.put("did", did);
    }
    
    /**
     * 获取DID
     * @return DID
     */
    public String getDID() {
        return storage.get("did");
    }
    
    /**
     * 存储公钥
     * @param publicKey 公钥
     */
    public void storePublicKey(String publicKey) {
        storage.put("publicKey", publicKey);
    }
    
    /**
     * 获取公钥
     * @return 公钥
     */
    public String getPublicKey() {
        return storage.get("publicKey");
    }
    
    /**
     * 存储私钥
     * @param privateKey 私钥
     */
    public void storePrivateKey(String privateKey) {
        storage.put("privateKey", privateKey);
    }
    
    /**
     * 获取私钥
     * @return 私钥
     */
    public String getPrivateKey() {
        return storage.get("privateKey");
    }
    
    /**
     * 存储用户身份信息
     * @param identity 用户身份信息
     */
    public void storeUserIdentity(IdentityManager.UserIdentity identity) {
        storage.put("userId", identity.getUserId());
        storage.put("token", identity.getToken());
        storage.put("username", identity.getUsername());
        storage.put("displayName", identity.getDisplayName());
        storage.put("email", identity.getEmail());
        storage.put("createdAt", String.valueOf(identity.getCreatedAt()));
        storage.put("lastLoginAt", String.valueOf(identity.getLastLoginAt()));
    }
    
    /**
     * 获取用户身份信息
     * @return 用户身份信息
     */
    public IdentityManager.UserIdentity getUserIdentity() {
        String userId = storage.get("userId");
        if (userId != null) {
            IdentityManager.UserIdentity identity = new IdentityManager.UserIdentity(userId);
            identity.setToken(storage.get("token"));
            identity.setUsername(storage.get("username"));
            identity.setDisplayName(storage.get("displayName"));
            identity.setEmail(storage.get("email"));
            
            String createdAtStr = storage.get("createdAt");
            if (createdAtStr != null) {
                identity.setCreatedAt(Long.parseLong(createdAtStr));
            }
            
            String lastLoginAtStr = storage.get("lastLoginAt");
            if (lastLoginAtStr != null) {
                identity.setLastLoginAt(Long.parseLong(lastLoginAtStr));
            }
            
            return identity;
        }
        return null;
    }
    
    /**
     * 设置恢复密钥
     * @param recoveryKey 恢复密钥
     */
    public void setRecoveryKey(String recoveryKey) {
        this.recoveryKey = recoveryKey;
        storage.put("recoveryKey", recoveryKey);
    }
    
    /**
     * 获取恢复密钥
     * @return 恢复密钥
     */
    public String getRecoveryKey() {
        return recoveryKey;
    }
    
    /**
     * 验证恢复密钥
     * @param key 恢复密钥
     * @return 是否验证通过
     */
    public boolean verifyRecoveryKey(String key) {
        return recoveryKey != null && recoveryKey.equals(key);
    }
    
    /**
     * 导出身份信息
     * @return 导出的身份信息
     */
    public String exportIdentity() {
        // 简单实现：将存储的信息序列化为JSON格式
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;
        for (Map.Entry<String, String> entry : storage.entrySet()) {
            if (!first) {
                sb.append(",");
            }
            sb.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\"");
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }
    
    /**
     * 导入身份信息
     * @param exportedIdentity 导出的身份信息
     * @return 是否导入成功
     */
    public boolean importIdentity(String exportedIdentity) {
        // 简单实现：解析JSON格式的身份信息
        try {
            // 移除首尾的大括号
            String content = exportedIdentity.substring(1, exportedIdentity.length() - 1);
            String[] pairs = content.split(",");
            
            for (String pair : pairs) {
                String[] keyValue = pair.split(":");
                if (keyValue.length == 2) {
                    String key = keyValue[0].replaceAll("\"", "");
                    String value = keyValue[1].replaceAll("\"", "");
                    storage.put(key, value);
                }
            }
            
            // 更新恢复密钥
            recoveryKey = storage.get("recoveryKey");
            
            return true;
        } catch (Exception e) {
            System.err.println("Failed to import identity: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 清理存储
     */
    public void clear() {
        storage.clear();
        recoveryKey = null;
    }
    
    /**
     * 获取存储大小
     * @return 存储大小
     */
    public int size() {
        return storage.size();
    }
    
    /**
     * 检查存储是否为空
     * @return 是否为空
     */
    public boolean isEmpty() {
        return storage.isEmpty();
    }
}
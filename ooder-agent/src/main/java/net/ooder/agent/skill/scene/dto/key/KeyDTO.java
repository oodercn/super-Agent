package net.ooder.mvp.skill.scene.dto.key;

import java.util.List;
import java.util.Map;

public class KeyDTO {
    
    private String keyId;
    private String keyName;
    private String keyType;
    private String provider;
    private String status;
    private Long createdAt;
    private Long expiresAt;
    private Long lastAccessAt;
    private Integer useCount;
    private Integer maxUseCount;
    private String userId;
    private String sceneGroupId;
    private String installId;
    private List<String> allowedUsers;
    private List<String> allowedRoles;
    private List<String> allowedScenes;
    private Long updatedAt;
    private String keyValue;
    
    public KeyDTO() {
    }
    
    public String getKeyId() { return keyId; }
    public void setKeyId(String keyId) { this.keyId = keyId; }
    
    public String getKeyName() { return keyName; }
    public void setKeyName(String keyName) { this.keyName = keyName; }
    
    public String getKeyType() { return keyType; }
    public void setKeyType(String keyType) { this.keyType = keyType; }
    
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Long getCreatedAt() { return createdAt; }
    public void setCreatedAt(Long createdAt) { this.createdAt = createdAt; }
    
    public Long getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Long expiresAt) { this.expiresAt = expiresAt; }
    
    public Long getLastAccessAt() { return lastAccessAt; }
    public void setLastAccessAt(Long lastAccessAt) { this.lastAccessAt = lastAccessAt; }
    
    public Integer getUseCount() { return useCount; }
    public void setUseCount(Integer useCount) { this.useCount = useCount; }
    
    public Integer getMaxUseCount() { return maxUseCount; }
    public void setMaxUseCount(Integer maxUseCount) { this.maxUseCount = maxUseCount; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getSceneGroupId() { return sceneGroupId; }
    public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
    
    public String getInstallId() { return installId; }
    public void setInstallId(String installId) { this.installId = installId; }
    
    public List<String> getAllowedUsers() { return allowedUsers; }
    public void setAllowedUsers(List<String> allowedUsers) { this.allowedUsers = allowedUsers; }
    
    public List<String> getAllowedRoles() { return allowedRoles; }
    public void setAllowedRoles(List<String> allowedRoles) { this.allowedRoles = allowedRoles; }
    
    public List<String> getAllowedScenes() { return allowedScenes; }
    public void setAllowedScenes(List<String> allowedScenes) { this.allowedScenes = allowedScenes; }
    
    public Long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Long updatedAt) { this.updatedAt = updatedAt; }
    
    public String getKeyValue() { return keyValue; }
    public void setKeyValue(String keyValue) { this.keyValue = keyValue; }
    
    public static KeyDTO fromKeyInfo(net.ooder.mvp.skill.scene.capability.service.KeyManagementService.KeyInfo key) {
        KeyDTO dto = new KeyDTO();
        dto.setKeyId(key.getKeyId());
        dto.setKeyName(key.getKeyName() != null ? key.getKeyName() : 
            (key.getDescription() != null ? key.getDescription() : key.getKeyId()));
        dto.setKeyType(key.getKeyType() != null ? key.getKeyType() : key.getScope());
        dto.setProvider(key.getProvider());
        dto.setStatus(key.getStatus().name());
        dto.setCreatedAt(key.getCreateTime());
        dto.setExpiresAt(key.getExpireTime());
        dto.setLastAccessAt(key.getLastAccessTime());
        dto.setUseCount(key.getAccessCount());
        dto.setMaxUseCount(key.getPermissions() != null ? 
            (Integer) key.getPermissions().get("maxUseCount") : -1);
        dto.setUserId(key.getUserId());
        dto.setSceneGroupId(key.getSceneGroupId());
        dto.setInstallId(key.getInstallId());
        dto.setAllowedUsers(key.getAllowedUsers());
        dto.setAllowedRoles(key.getAllowedRoles());
        dto.setAllowedScenes(key.getAllowedScenes());
        dto.setUpdatedAt(key.getUpdatedAt());
        return dto;
    }
}

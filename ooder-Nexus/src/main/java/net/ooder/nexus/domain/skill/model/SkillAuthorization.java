package net.ooder.nexus.domain.skill.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 技能授权信息
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
public class SkillAuthorization {
    
    private String authId;
    private String skillId;
    private String skillName;
    private String userId;
    private String authType;
    private List<ResourcePermission> resourcePermissions;
    private List<ScenePermission> scenePermissions;
    private int status;
    private long createdAt;
    private long updatedAt;
    private long expiresAt;
    private String authReason;
    
    public static final int STATUS_PENDING = 0;
    public static final int STATUS_APPROVED = 1;
    public static final int STATUS_REJECTED = 2;
    public static final int STATUS_REVOKED = 3;
    
    public SkillAuthorization() {
        this.resourcePermissions = new ArrayList<>();
        this.scenePermissions = new ArrayList<>();
        this.status = STATUS_PENDING;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }
    
    public String getAuthId() {
        return authId;
    }
    
    public void setAuthId(String authId) {
        this.authId = authId;
    }
    
    public String getSkillId() {
        return skillId;
    }
    
    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }
    
    public String getSkillName() {
        return skillName;
    }
    
    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getAuthType() {
        return authType;
    }
    
    public void setAuthType(String authType) {
        this.authType = authType;
    }
    
    public List<ResourcePermission> getResourcePermissions() {
        return resourcePermissions;
    }
    
    public void setResourcePermissions(List<ResourcePermission> resourcePermissions) {
        this.resourcePermissions = resourcePermissions;
    }
    
    public List<ScenePermission> getScenePermissions() {
        return scenePermissions;
    }
    
    public void setScenePermissions(List<ScenePermission> scenePermissions) {
        this.scenePermissions = scenePermissions;
    }
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public long getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
    
    public long getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public long getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(long expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public String getAuthReason() {
        return authReason;
    }
    
    public void setAuthReason(String authReason) {
        this.authReason = authReason;
    }
    
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("authId", authId);
        map.put("skillId", skillId);
        map.put("skillName", skillName);
        map.put("userId", userId);
        map.put("authType", authType);
        map.put("status", status);
        map.put("statusText", getStatusText());
        map.put("createdAt", createdAt);
        map.put("updatedAt", updatedAt);
        map.put("expiresAt", expiresAt);
        map.put("authReason", authReason);
        
        List<Map<String, Object>> resources = new ArrayList<>();
        for (ResourcePermission rp : resourcePermissions) {
            resources.add(rp.toMap());
        }
        map.put("resourcePermissions", resources);
        
        List<Map<String, Object>> scenes = new ArrayList<>();
        for (ScenePermission sp : scenePermissions) {
            scenes.add(sp.toMap());
        }
        map.put("scenePermissions", scenes);
        
        return map;
    }
    
    public String getStatusText() {
        switch (status) {
            case STATUS_PENDING: return "待授权";
            case STATUS_APPROVED: return "已授权";
            case STATUS_REJECTED: return "已拒绝";
            case STATUS_REVOKED: return "已撤销";
            default: return "未知";
        }
    }
    
    public static class ResourcePermission {
        private String resourceType;
        private String resourceId;
        private String resourceName;
        private List<String> permissions;
        private String accessScope;
        
        public ResourcePermission() {
            this.permissions = new ArrayList<>();
        }
        
        public String getResourceType() {
            return resourceType;
        }
        
        public void setResourceType(String resourceType) {
            this.resourceType = resourceType;
        }
        
        public String getResourceId() {
            return resourceId;
        }
        
        public void setResourceId(String resourceId) {
            this.resourceId = resourceId;
        }
        
        public String getResourceName() {
            return resourceName;
        }
        
        public void setResourceName(String resourceName) {
            this.resourceName = resourceName;
        }
        
        public List<String> getPermissions() {
            return permissions;
        }
        
        public void setPermissions(List<String> permissions) {
            this.permissions = permissions;
        }
        
        public String getAccessScope() {
            return accessScope;
        }
        
        public void setAccessScope(String accessScope) {
            this.accessScope = accessScope;
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("resourceType", resourceType);
            map.put("resourceId", resourceId);
            map.put("resourceName", resourceName);
            map.put("permissions", permissions);
            map.put("accessScope", accessScope);
            return map;
        }
    }
    
    public static class ScenePermission {
        private String sceneId;
        private String sceneName;
        private String sceneType;
        private List<String> capabilities;
        private boolean joinGroup;
        private String groupId;
        
        public ScenePermission() {
            this.capabilities = new ArrayList<>();
        }
        
        public String getSceneId() {
            return sceneId;
        }
        
        public void setSceneId(String sceneId) {
            this.sceneId = sceneId;
        }
        
        public String getSceneName() {
            return sceneName;
        }
        
        public void setSceneName(String sceneName) {
            this.sceneName = sceneName;
        }
        
        public String getSceneType() {
            return sceneType;
        }
        
        public void setSceneType(String sceneType) {
            this.sceneType = sceneType;
        }
        
        public List<String> getCapabilities() {
            return capabilities;
        }
        
        public void setCapabilities(List<String> capabilities) {
            this.capabilities = capabilities;
        }
        
        public boolean isJoinGroup() {
            return joinGroup;
        }
        
        public void setJoinGroup(boolean joinGroup) {
            this.joinGroup = joinGroup;
        }
        
        public String getGroupId() {
            return groupId;
        }
        
        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("sceneId", sceneId);
            map.put("sceneName", sceneName);
            map.put("sceneType", sceneType);
            map.put("capabilities", capabilities);
            map.put("joinGroup", joinGroup);
            map.put("groupId", groupId);
            return map;
        }
    }
}

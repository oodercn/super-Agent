package net.ooder.nexus.domain.skill.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Skill依赖信息
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
public class SkillDependency {
    
    private String skillId;
    private String skillName;
    private String skillType;
    private String version;
    private String description;
    private String source;
    private List<SceneDependency> scenes;
    private List<SkillRefDependency> skills;
    private List<ResourceDependency> resources;
    private List<PermissionRequest> permissions;
    private boolean hasDependencies;
    
    public SkillDependency() {
        this.scenes = new ArrayList<>();
        this.skills = new ArrayList<>();
        this.resources = new ArrayList<>();
        this.permissions = new ArrayList<>();
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
    
    public String getSkillType() {
        return skillType;
    }
    
    public void setSkillType(String skillType) {
        this.skillType = skillType;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getSource() {
        return source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    
    public List<SceneDependency> getScenes() {
        return scenes;
    }
    
    public void setScenes(List<SceneDependency> scenes) {
        this.scenes = scenes;
    }
    
    public List<SkillRefDependency> getSkills() {
        return skills;
    }
    
    public void setSkills(List<SkillRefDependency> skills) {
        this.skills = skills;
    }
    
    public List<ResourceDependency> getResources() {
        return resources;
    }
    
    public void setResources(List<ResourceDependency> resources) {
        this.resources = resources;
    }
    
    public List<PermissionRequest> getPermissions() {
        return permissions;
    }
    
    public void setPermissions(List<PermissionRequest> permissions) {
        this.permissions = permissions;
    }
    
    public boolean isHasDependencies() {
        return hasDependencies;
    }
    
    public void setHasDependencies(boolean hasDependencies) {
        this.hasDependencies = hasDependencies;
    }
    
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("skillId", skillId);
        map.put("skillName", skillName);
        map.put("skillType", skillType);
        map.put("version", version);
        map.put("description", description);
        map.put("source", source);
        map.put("hasDependencies", hasDependencies || !scenes.isEmpty() || !skills.isEmpty());
        
        List<Map<String, Object>> sceneList = new ArrayList<>();
        for (SceneDependency s : scenes) {
            sceneList.add(s.toMap());
        }
        map.put("scenes", sceneList);
        
        List<Map<String, Object>> skillList = new ArrayList<>();
        for (SkillRefDependency s : skills) {
            skillList.add(s.toMap());
        }
        map.put("skills", skillList);
        
        List<Map<String, Object>> resourceList = new ArrayList<>();
        for (ResourceDependency r : resources) {
            resourceList.add(r.toMap());
        }
        map.put("resources", resourceList);
        
        List<Map<String, Object>> permList = new ArrayList<>();
        for (PermissionRequest p : permissions) {
            permList.add(p.toMap());
        }
        map.put("permissions", permList);
        
        return map;
    }
    
    public static class SceneDependency {
        private String sceneId;
        private String sceneName;
        private String sceneType;
        private boolean required;
        private String groupId;
        private String groupName;
        private List<String> capabilities;
        
        public SceneDependency() {
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
        
        public boolean isRequired() {
            return required;
        }
        
        public void setRequired(boolean required) {
            this.required = required;
        }
        
        public String getGroupId() {
            return groupId;
        }
        
        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }
        
        public String getGroupName() {
            return groupName;
        }
        
        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }
        
        public List<String> getCapabilities() {
            return capabilities;
        }
        
        public void setCapabilities(List<String> capabilities) {
            this.capabilities = capabilities;
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("sceneId", sceneId);
            map.put("sceneName", sceneName);
            map.put("sceneType", sceneType);
            map.put("required", required);
            map.put("groupId", groupId);
            map.put("groupName", groupName);
            map.put("capabilities", capabilities);
            return map;
        }
    }
    
    public static class SkillRefDependency {
        private String skillId;
        private String skillName;
        private String version;
        private boolean required;
        private String reason;
        
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
        
        public String getVersion() {
            return version;
        }
        
        public void setVersion(String version) {
            this.version = version;
        }
        
        public boolean isRequired() {
            return required;
        }
        
        public void setRequired(boolean required) {
            this.required = required;
        }
        
        public String getReason() {
            return reason;
        }
        
        public void setReason(String reason) {
            this.reason = reason;
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("skillId", skillId);
            map.put("skillName", skillName);
            map.put("version", version);
            map.put("required", required);
            map.put("reason", reason);
            return map;
        }
    }
    
    public static class ResourceDependency {
        private String resourceType;
        private String resourceScope;
        private List<String> permissions;
        private String description;
        
        public ResourceDependency() {
            this.permissions = new ArrayList<>();
        }
        
        public String getResourceType() {
            return resourceType;
        }
        
        public void setResourceType(String resourceType) {
            this.resourceType = resourceType;
        }
        
        public String getResourceScope() {
            return resourceScope;
        }
        
        public void setResourceScope(String resourceScope) {
            this.resourceScope = resourceScope;
        }
        
        public List<String> getPermissions() {
            return permissions;
        }
        
        public void setPermissions(List<String> permissions) {
            this.permissions = permissions;
        }
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("resourceType", resourceType);
            map.put("resourceScope", resourceScope);
            map.put("permissions", permissions);
            map.put("description", description);
            return map;
        }
    }
    
    public static class PermissionRequest {
        private String permissionId;
        private String permissionType;
        private String resourceName;
        private String permission;
        private String description;
        private boolean required;
        private boolean granted;
        
        public String getPermissionId() {
            return permissionId;
        }
        
        public void setPermissionId(String permissionId) {
            this.permissionId = permissionId;
        }
        
        public String getPermissionType() {
            return permissionType;
        }
        
        public void setPermissionType(String permissionType) {
            this.permissionType = permissionType;
        }
        
        public String getResourceName() {
            return resourceName;
        }
        
        public void setResourceName(String resourceName) {
            this.resourceName = resourceName;
        }
        
        public String getPermission() {
            return permission;
        }
        
        public void setPermission(String permission) {
            this.permission = permission;
        }
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        public boolean isRequired() {
            return required;
        }
        
        public void setRequired(boolean required) {
            this.required = required;
        }
        
        public boolean isGranted() {
            return granted;
        }
        
        public void setGranted(boolean granted) {
            this.granted = granted;
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("permissionId", permissionId);
            map.put("permissionType", permissionType);
            map.put("resourceName", resourceName);
            map.put("permission", permission);
            map.put("description", description);
            map.put("required", required);
            map.put("granted", granted);
            return map;
        }
    }
}

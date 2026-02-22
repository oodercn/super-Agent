package net.ooder.nexus.domain.skill.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Skill安装预览信息
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
public class InstallPreview {
    
    private String previewId;
    private String skillId;
    private String skillName;
    private String skillType;
    private String version;
    private String description;
    private String source;
    private String downloadUrl;
    
    private List<SceneInfo> scenes;
    private List<SkillDepInfo> skillDeps;
    private List<PermissionInfo> permissions;
    private List<GroupInfo> groups;
    
    private boolean canInstall;
    private List<String> warnings;
    private List<String> errors;
    
    public InstallPreview() {
        this.scenes = new ArrayList<>();
        this.skillDeps = new ArrayList<>();
        this.permissions = new ArrayList<>();
        this.groups = new ArrayList<>();
        this.warnings = new ArrayList<>();
        this.errors = new ArrayList<>();
        this.canInstall = true;
    }
    
    public String getPreviewId() {
        return previewId;
    }
    
    public void setPreviewId(String previewId) {
        this.previewId = previewId;
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
    
    public String getDownloadUrl() {
        return downloadUrl;
    }
    
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
    
    public List<SceneInfo> getScenes() {
        return scenes;
    }
    
    public void setScenes(List<SceneInfo> scenes) {
        this.scenes = scenes;
    }
    
    public List<SkillDepInfo> getSkillDeps() {
        return skillDeps;
    }
    
    public void setSkillDeps(List<SkillDepInfo> skillDeps) {
        this.skillDeps = skillDeps;
    }
    
    public List<PermissionInfo> getPermissions() {
        return permissions;
    }
    
    public void setPermissions(List<PermissionInfo> permissions) {
        this.permissions = permissions;
    }
    
    public List<GroupInfo> getGroups() {
        return groups;
    }
    
    public void setGroups(List<GroupInfo> groups) {
        this.groups = groups;
    }
    
    public boolean isCanInstall() {
        return canInstall;
    }
    
    public void setCanInstall(boolean canInstall) {
        this.canInstall = canInstall;
    }
    
    public List<String> getWarnings() {
        return warnings;
    }
    
    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }
    
    public List<String> getErrors() {
        return errors;
    }
    
    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
    
    public void addWarning(String warning) {
        this.warnings.add(warning);
    }
    
    public void addError(String error) {
        this.errors.add(error);
        this.canInstall = false;
    }
    
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("previewId", previewId);
        map.put("skillId", skillId);
        map.put("skillName", skillName);
        map.put("skillType", skillType);
        map.put("version", version);
        map.put("description", description);
        map.put("source", source);
        map.put("downloadUrl", downloadUrl);
        map.put("canInstall", canInstall);
        map.put("warnings", warnings);
        map.put("errors", errors);
        
        List<Map<String, Object>> sceneList = new ArrayList<>();
        for (SceneInfo s : scenes) {
            sceneList.add(s.toMap());
        }
        map.put("scenes", sceneList);
        
        List<Map<String, Object>> skillList = new ArrayList<>();
        for (SkillDepInfo s : skillDeps) {
            skillList.add(s.toMap());
        }
        map.put("skillDeps", skillList);
        
        List<Map<String, Object>> permList = new ArrayList<>();
        for (PermissionInfo p : permissions) {
            permList.add(p.toMap());
        }
        map.put("permissions", permList);
        
        List<Map<String, Object>> groupList = new ArrayList<>();
        for (GroupInfo g : groups) {
            groupList.add(g.toMap());
        }
        map.put("groups", groupList);
        
        return map;
    }
    
    public static class SceneInfo {
        private String sceneId;
        private String sceneName;
        private String sceneType;
        private boolean required;
        private boolean willJoin;
        
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
        
        public boolean isWillJoin() {
            return willJoin;
        }
        
        public void setWillJoin(boolean willJoin) {
            this.willJoin = willJoin;
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("sceneId", sceneId);
            map.put("sceneName", sceneName);
            map.put("sceneType", sceneType);
            map.put("required", required);
            map.put("willJoin", willJoin);
            return map;
        }
    }
    
    public static class SkillDepInfo {
        private String skillId;
        private String skillName;
        private boolean required;
        private boolean installed;
        
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
        
        public boolean isRequired() {
            return required;
        }
        
        public void setRequired(boolean required) {
            this.required = required;
        }
        
        public boolean isInstalled() {
            return installed;
        }
        
        public void setInstalled(boolean installed) {
            this.installed = installed;
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("skillId", skillId);
            map.put("skillName", skillName);
            map.put("required", required);
            map.put("installed", installed);
            return map;
        }
    }
    
    public static class PermissionInfo {
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
    
    public static class GroupInfo {
        private String groupId;
        private String groupName;
        private String sceneId;
        private String sceneName;
        private String groupAddress;
        
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
        
        public String getGroupAddress() {
            return groupAddress;
        }
        
        public void setGroupAddress(String groupAddress) {
            this.groupAddress = groupAddress;
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("groupId", groupId);
            map.put("groupName", groupName);
            map.put("sceneId", sceneId);
            map.put("sceneName", sceneName);
            map.put("groupAddress", groupAddress);
            return map;
        }
    }
}

package net.ooder.nexus.domain.collaboration.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 协作关系模型
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
public class CollaborationRelation {
    
    private String relationId;
    private String userId;
    private List<SceneRelation> scenes;
    private List<GroupRelation> groups;
    private List<SkillRelation> skills;
    private long updatedAt;
    
    public CollaborationRelation() {
        this.scenes = new ArrayList<>();
        this.groups = new ArrayList<>();
        this.skills = new ArrayList<>();
        this.updatedAt = System.currentTimeMillis();
    }
    
    public String getRelationId() {
        return relationId;
    }
    
    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public List<SceneRelation> getScenes() {
        return scenes;
    }
    
    public void setScenes(List<SceneRelation> scenes) {
        this.scenes = scenes;
    }
    
    public List<GroupRelation> getGroups() {
        return groups;
    }
    
    public void setGroups(List<GroupRelation> groups) {
        this.groups = groups;
    }
    
    public List<SkillRelation> getSkills() {
        return skills;
    }
    
    public void setSkills(List<SkillRelation> skills) {
        this.skills = skills;
    }
    
    public long getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("relationId", relationId);
        map.put("userId", userId);
        map.put("updatedAt", updatedAt);
        
        List<Map<String, Object>> sceneList = new ArrayList<>();
        for (SceneRelation s : scenes) {
            sceneList.add(s.toMap());
        }
        map.put("scenes", sceneList);
        
        List<Map<String, Object>> groupList = new ArrayList<>();
        for (GroupRelation g : groups) {
            groupList.add(g.toMap());
        }
        map.put("groups", groupList);
        
        List<Map<String, Object>> skillList = new ArrayList<>();
        for (SkillRelation s : skills) {
            skillList.add(s.toMap());
        }
        map.put("skills", skillList);
        
        return map;
    }
    
    public static class SceneRelation {
        private String sceneId;
        private String sceneName;
        private String sceneType;
        private String description;
        private List<String> capabilities;
        private List<String> groupIds;
        private List<String> skillIds;
        private long joinedAt;
        
        public SceneRelation() {
            this.capabilities = new ArrayList<>();
            this.groupIds = new ArrayList<>();
            this.skillIds = new ArrayList<>();
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
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        public List<String> getCapabilities() {
            return capabilities;
        }
        
        public void setCapabilities(List<String> capabilities) {
            this.capabilities = capabilities;
        }
        
        public List<String> getGroupIds() {
            return groupIds;
        }
        
        public void setGroupIds(List<String> groupIds) {
            this.groupIds = groupIds;
        }
        
        public List<String> getSkillIds() {
            return skillIds;
        }
        
        public void setSkillIds(List<String> skillIds) {
            this.skillIds = skillIds;
        }
        
        public long getJoinedAt() {
            return joinedAt;
        }
        
        public void setJoinedAt(long joinedAt) {
            this.joinedAt = joinedAt;
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("sceneId", sceneId);
            map.put("sceneName", sceneName);
            map.put("sceneType", sceneType);
            map.put("description", description);
            map.put("capabilities", capabilities);
            map.put("groupIds", groupIds);
            map.put("skillIds", skillIds);
            map.put("joinedAt", joinedAt);
            return map;
        }
    }
    
    public static class GroupRelation {
        private String groupId;
        private String groupName;
        private String sceneId;
        private String sceneName;
        private String groupAddress;
        private List<String> memberIds;
        private List<String> skillIds;
        private String role;
        private long joinedAt;
        
        public GroupRelation() {
            this.memberIds = new ArrayList<>();
            this.skillIds = new ArrayList<>();
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
        
        public List<String> getMemberIds() {
            return memberIds;
        }
        
        public void setMemberIds(List<String> memberIds) {
            this.memberIds = memberIds;
        }
        
        public List<String> getSkillIds() {
            return skillIds;
        }
        
        public void setSkillIds(List<String> skillIds) {
            this.skillIds = skillIds;
        }
        
        public String getRole() {
            return role;
        }
        
        public void setRole(String role) {
            this.role = role;
        }
        
        public long getJoinedAt() {
            return joinedAt;
        }
        
        public void setJoinedAt(long joinedAt) {
            this.joinedAt = joinedAt;
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("groupId", groupId);
            map.put("groupName", groupName);
            map.put("sceneId", sceneId);
            map.put("sceneName", sceneName);
            map.put("groupAddress", groupAddress);
            map.put("memberIds", memberIds);
            map.put("skillIds", skillIds);
            map.put("role", role);
            map.put("joinedAt", joinedAt);
            return map;
        }
    }
    
    public static class SkillRelation {
        private String skillId;
        private String skillName;
        private String skillType;
        private String version;
        private List<String> capabilities;
        private List<String> sceneIds;
        private List<String> groupIds;
        private String status;
        private long installedAt;
        
        public SkillRelation() {
            this.capabilities = new ArrayList<>();
            this.sceneIds = new ArrayList<>();
            this.groupIds = new ArrayList<>();
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
        
        public List<String> getCapabilities() {
            return capabilities;
        }
        
        public void setCapabilities(List<String> capabilities) {
            this.capabilities = capabilities;
        }
        
        public List<String> getSceneIds() {
            return sceneIds;
        }
        
        public void setSceneIds(List<String> sceneIds) {
            this.sceneIds = sceneIds;
        }
        
        public List<String> getGroupIds() {
            return groupIds;
        }
        
        public void setGroupIds(List<String> groupIds) {
            this.groupIds = groupIds;
        }
        
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
        
        public long getInstalledAt() {
            return installedAt;
        }
        
        public void setInstalledAt(long installedAt) {
            this.installedAt = installedAt;
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("skillId", skillId);
            map.put("skillName", skillName);
            map.put("skillType", skillType);
            map.put("version", version);
            map.put("capabilities", capabilities);
            map.put("sceneIds", sceneIds);
            map.put("groupIds", groupIds);
            map.put("status", status);
            map.put("installedAt", installedAt);
            return map;
        }
    }
}

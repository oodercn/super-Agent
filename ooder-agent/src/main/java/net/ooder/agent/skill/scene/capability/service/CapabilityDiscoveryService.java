package net.ooder.mvp.skill.scene.capability.service;

import net.ooder.mvp.skill.scene.capability.model.Capability;
import net.ooder.mvp.skill.scene.capability.model.CapabilityType;
import net.ooder.mvp.skill.scene.capability.driver.DriverCondition;
import net.ooder.mvp.skill.scene.dto.discovery.InvokeResultDTO;

import java.util.List;
import java.util.Map;

public interface CapabilityDiscoveryService {
    
    DiscoveryResult discoverCapabilities(DiscoveryRequest request);
    
    CapabilityDetail getCapabilityDetail(String capabilityId);
    
    List<DriverCondition> getDriverConditions(String capabilityId);
    
    InvokeResultDTO invokeCapability(String capabilityId, Map<String, Object> params);
    
    public static class DiscoveryRequest {
        private DiscoveryMethod method;
        private String query;
        private CapabilityType type;
        private String sceneType;
        private int page;
        private int size;
        private String skillForm;
        private Boolean mainFirst;
        private String visibility;
        
        public DiscoveryMethod getMethod() { return method; }
        public void setMethod(DiscoveryMethod method) { this.method = method; }
        public String getQuery() { return query; }
        public void setQuery(String query) { this.query = query; }
        public CapabilityType getType() { return type; }
        public void setType(CapabilityType type) { this.type = type; }
        public String getSceneType() { return sceneType; }
        public void setSceneType(String sceneType) { this.sceneType = sceneType; }
        public int getPage() { return page; }
        public void setPage(int page) { this.page = page; }
        public int getSize() { return size; }
        public void setSize(int size) { this.size = size; }
        public String getSkillForm() { return skillForm; }
        public void setSkillForm(String skillForm) { this.skillForm = skillForm; }
        public Boolean getMainFirst() { return mainFirst; }
        public void setMainFirst(Boolean mainFirst) { this.mainFirst = mainFirst; }
        public String getVisibility() { return visibility; }
        public void setVisibility(String visibility) { this.visibility = visibility; }
    }
    
    public static class DiscoveryResult {
        private List<CapabilityItem> sceneCapabilities;
        private List<CapabilityItem> collaborationCapabilities;
        private int totalScene;
        private int totalCollaboration;
        
        public List<CapabilityItem> getSceneCapabilities() { 
            return sceneCapabilities != null ? sceneCapabilities : java.util.Collections.emptyList(); 
        }
        public void setSceneCapabilities(List<CapabilityItem> sceneCapabilities) { 
            this.sceneCapabilities = sceneCapabilities; 
        }
        public List<CapabilityItem> getCollaborationCapabilities() { 
            return collaborationCapabilities != null ? collaborationCapabilities : java.util.Collections.emptyList(); 
        }
        public void setCollaborationCapabilities(List<CapabilityItem> collaborationCapabilities) { 
            this.collaborationCapabilities = collaborationCapabilities; 
        }
        public int getTotalScene() { return totalScene; }
        public void setTotalScene(int totalScene) { this.totalScene = totalScene; }
        public int getTotalCollaboration() { return totalCollaboration; }
        public void setTotalCollaboration(int totalCollaboration) { this.totalCollaboration = totalCollaboration; }
    }
    
    public static class CapabilityItem {
        private String capabilityId;
        private String name;
        private String description;
        private CapabilityType type;
        private String icon;
        private String version;
        private boolean installed;
        private List<DriverConditionInfo> driverConditions;
        private List<String> supportedSceneTypes;
        private Map<String, Object> metadata;
        private String skillForm;
        private String sceneType;
        private boolean mainFirst;
        private String visibility;
        private List<ParticipantInfo> participants;
        
        public String getCapabilityId() { return capabilityId; }
        public void setCapabilityId(String capabilityId) { this.capabilityId = capabilityId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public CapabilityType getType() { return type; }
        public void setType(CapabilityType type) { this.type = type; }
        public String getIcon() { return icon; }
        public void setIcon(String icon) { this.icon = icon; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public boolean isInstalled() { return installed; }
        public void setInstalled(boolean installed) { this.installed = installed; }
        public List<DriverConditionInfo> getDriverConditions() { return driverConditions; }
        public void setDriverConditions(List<DriverConditionInfo> driverConditions) { this.driverConditions = driverConditions; }
        public List<String> getSupportedSceneTypes() { return supportedSceneTypes; }
        public void setSupportedSceneTypes(List<String> supportedSceneTypes) { this.supportedSceneTypes = supportedSceneTypes; }
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
        public String getSkillForm() { return skillForm; }
        public void setSkillForm(String skillForm) { this.skillForm = skillForm; }
        public String getSceneType() { return sceneType; }
        public void setSceneType(String sceneType) { this.sceneType = sceneType; }
        public boolean isMainFirst() { return mainFirst; }
        public void setMainFirst(boolean mainFirst) { this.mainFirst = mainFirst; }
        public String getVisibility() { return visibility; }
        public void setVisibility(String visibility) { this.visibility = visibility; }
        public List<ParticipantInfo> getParticipants() { return participants; }
        public void setParticipants(List<ParticipantInfo> participants) { this.participants = participants; }
    }
    
    public static class ParticipantInfo {
        private String role;
        private String name;
        private String userId;
        private List<String> permissions;
        
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public List<String> getPermissions() { return permissions; }
        public void setPermissions(List<String> permissions) { this.permissions = permissions; }
    }
    
    public static class DriverConditionInfo {
        private String conditionId;
        private String name;
        private String description;
        private String sceneType;
        
        public String getConditionId() { return conditionId; }
        public void setConditionId(String conditionId) { this.conditionId = conditionId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getSceneType() { return sceneType; }
        public void setSceneType(String sceneType) { this.sceneType = sceneType; }
    }
    
    public static class CapabilityDetail {
        private String capabilityId;
        private String name;
        private String description;
        private CapabilityType type;
        private String typeName;
        private String icon;
        private String version;
        private boolean installed;
        private List<DriverCondition> driverConditions;
        private List<String> dependencies;
        private List<String> optionalCapabilities;
        private Map<String, Object> config;
        private String skillForm;
        private String sceneType;
        private boolean mainFirst;
        private String visibility;
        private List<ParticipantInfo> participants;
        
        public String getCapabilityId() { return capabilityId; }
        public void setCapabilityId(String capabilityId) { this.capabilityId = capabilityId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public CapabilityType getType() { return type; }
        public void setType(CapabilityType type) { 
            this.type = type; 
            this.typeName = type != null ? type.getName() : null;
        }
        public String getTypeName() { return typeName; }
        public String getIcon() { return icon; }
        public void setIcon(String icon) { this.icon = icon; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public boolean isInstalled() { return installed; }
        public void setInstalled(boolean installed) { this.installed = installed; }
        public List<DriverCondition> getDriverConditions() { return driverConditions; }
        public void setDriverConditions(List<DriverCondition> driverConditions) { this.driverConditions = driverConditions; }
        public List<String> getDependencies() { return dependencies; }
        public void setDependencies(List<String> dependencies) { this.dependencies = dependencies; }
        public List<String> getOptionalCapabilities() { return optionalCapabilities; }
        public void setOptionalCapabilities(List<String> optionalCapabilities) { this.optionalCapabilities = optionalCapabilities; }
        public Map<String, Object> getConfig() { return config; }
        public void setConfig(Map<String, Object> config) { this.config = config; }
        public String getSkillForm() { return skillForm; }
        public void setSkillForm(String skillForm) { this.skillForm = skillForm; }
        public String getSceneType() { return sceneType; }
        public void setSceneType(String sceneType) { this.sceneType = sceneType; }
        public boolean isMainFirst() { return mainFirst; }
        public void setMainFirst(boolean mainFirst) { this.mainFirst = mainFirst; }
        public String getVisibility() { return visibility; }
        public void setVisibility(String visibility) { this.visibility = visibility; }
        public List<ParticipantInfo> getParticipants() { return participants; }
        public void setParticipants(List<ParticipantInfo> participants) { this.participants = participants; }
    }
    
    public enum DiscoveryMethod {
        LOCAL_FS,
        GITHUB,
        GITEE,
        SKILL_CENTER,
        UDP_BROADCAST,
        AUTO
    }
}

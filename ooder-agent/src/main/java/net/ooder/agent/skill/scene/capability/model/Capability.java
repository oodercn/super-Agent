package net.ooder.mvp.skill.scene.capability.model;

import net.ooder.mvp.skill.scene.capability.driver.DriverCondition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Capability implements Serializable {
    private static final long serialVersionUID = 1L;

    private String capabilityId;
    private String name;
    private String description;
    private CapabilityType capabilityType;
    private String version;
    private AccessLevel accessLevel;
    private String ownerId;
    private List<String> supportedSceneTypes;
    private ConnectorType connectorType;
    private String endpoint;
    private List<ParameterDef> parameters;
    private ReturnDef returns;
    private CapabilityStatus status;
    private long createTime;
    private long updateTime;
    private String skillId;
    
    private List<String> capabilities;
    private boolean mainFirst;
    private MainFirstConfig mainFirstConfig;
    private List<CollaborativeCapabilityRef> collaborativeCapabilities;
    private DriverType driverType;
    private String icon;
    private Map<String, Object> metadata;
    private List<String> dependencies;
    private List<String> optionalCapabilities;
    
    private SkillForm skillForm;
    private SceneType sceneType;
    private Visibility visibility;
    private CapabilityCategory capabilityCategory;
    
    private String businessCategory;
    private String subCategory;
    private List<String> tags;
    
    private List<CapabilityAddress> requiredAddresses;
    private List<CapabilityAddress> optionalAddresses;
    
    private List<DriverCondition> driverConditions;
    private List<Participant> participants;
    
    private boolean dynamicSceneTypes;
    private String parentSkill;
    private String parentScene;
    
    private boolean installed;
    private Integer businessSemanticsScore;

    public Capability() {
        this.version = "1.0.0";
        this.accessLevel = AccessLevel.SCENE;
        this.supportedSceneTypes = new ArrayList<String>();
        this.parameters = new ArrayList<ParameterDef>();
        this.status = CapabilityStatus.REGISTERED;
        this.createTime = System.currentTimeMillis();
        this.updateTime = this.createTime;
        this.capabilities = new ArrayList<String>();
        this.collaborativeCapabilities = new ArrayList<CollaborativeCapabilityRef>();
        this.skillForm = SkillForm.PROVIDER;
        this.visibility = Visibility.PUBLIC;
        this.tags = new ArrayList<String>();
        this.requiredAddresses = new ArrayList<CapabilityAddress>();
        this.optionalAddresses = new ArrayList<CapabilityAddress>();
        this.driverConditions = new ArrayList<DriverCondition>();
        this.participants = new ArrayList<Participant>();
    }

    public String getCapabilityId() {
        return capabilityId;
    }

    public void setCapabilityId(String capabilityId) {
        this.capabilityId = capabilityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CapabilityType getCapabilityType() {
        return capabilityType;
    }

    public void setCapabilityType(CapabilityType capabilityType) {
        this.capabilityType = capabilityType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public List<String> getSupportedSceneTypes() {
        return supportedSceneTypes;
    }

    public void setSupportedSceneTypes(List<String> supportedSceneTypes) {
        this.supportedSceneTypes = supportedSceneTypes;
    }

    public ConnectorType getConnectorType() {
        return connectorType;
    }

    public void setConnectorType(ConnectorType connectorType) {
        this.connectorType = connectorType;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public List<ParameterDef> getParameters() {
        return parameters;
    }

    public void setParameters(List<ParameterDef> parameters) {
        this.parameters = parameters;
    }

    public ReturnDef getReturns() {
        return returns;
    }

    public void setReturns(ReturnDef returns) {
        this.returns = returns;
    }

    public CapabilityStatus getStatus() {
        return status;
    }

    public void setStatus(CapabilityStatus status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public boolean supportsSceneType(String sceneType) {
        return supportedSceneTypes != null && supportedSceneTypes.contains(sceneType);
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public List<String> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<String> capabilities) {
        this.capabilities = capabilities;
    }

    public boolean isMainFirst() {
        return mainFirst;
    }

    public void setMainFirst(boolean mainFirst) {
        this.mainFirst = mainFirst;
    }

    public MainFirstConfig getMainFirstConfig() {
        return mainFirstConfig;
    }

    public void setMainFirstConfig(MainFirstConfig mainFirstConfig) {
        this.mainFirstConfig = mainFirstConfig;
    }

    public List<CollaborativeCapabilityRef> getCollaborativeCapabilities() {
        return collaborativeCapabilities;
    }

    public void setCollaborativeCapabilities(List<CollaborativeCapabilityRef> collaborativeCapabilities) {
        this.collaborativeCapabilities = collaborativeCapabilities;
    }

    public DriverType getDriverType() {
        return driverType;
    }

    public void setDriverType(DriverType driverType) {
        this.driverType = driverType;
    }

    public boolean isSceneCapability() {
        return "SCENE".equals(skillForm) || capabilityType == CapabilityType.SCENE;
    }
    
    public void setSceneCapability(boolean sceneCapability) {
        this.skillForm = sceneCapability ? SkillForm.SCENE : SkillForm.PROVIDER;
    }

    public boolean isInstalled() {
        return installed;
    }

    public void setInstalled(boolean installed) {
        this.installed = installed;
    }

    public String getCategory() {
        return capabilityCategory != null ? capabilityCategory.getCode() : null;
    }

    public void setCategory(String category) {
        this.capabilityCategory = CapabilityCategory.fromCode(category);
    }

    public Integer getBusinessSemanticsScore() {
        return businessSemanticsScore;
    }

    public void setBusinessSemanticsScore(Integer businessSemanticsScore) {
        this.businessSemanticsScore = businessSemanticsScore;
    }

    public boolean isHasSelfDrive() {
        return mainFirst && hasSelfDriveConfig();
    }
    
    private boolean hasSelfDriveConfig() {
        return mainFirstConfig != null && mainFirstConfig.getSelfDrive() != null;
    }
    
    private boolean hasTriggerConfig() {
        return driverType != null && driverType.isTrigger();
    }

    public boolean isDriverCapability() {
        return capabilityType == CapabilityType.DRIVER;
    }

    public boolean isEnabled() {
        return status == CapabilityStatus.ENABLED || status == CapabilityStatus.REGISTERED;
    }

    public Map<String, Object> getConfig() {
        Map<String, Object> config = new HashMap<String, Object>();
        if (metadata != null) {
            config.putAll(metadata);
        }
        return config;
    }

    public Map<String, Object> getMetadata() {
        Map<String, Object> result = new HashMap<String, Object>();
        if (metadata != null) {
            result.putAll(metadata);
        }
        result.put("version", version);
        result.put("createTime", createTime);
        result.put("updateTime", updateTime);
        result.put("ownerId", ownerId);
        result.put("skillId", skillId);
        result.put("skillForm", skillForm);
        result.put("sceneType", sceneType);
        return result;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public String getIcon() {
        if (icon != null) {
            return icon;
        }
        return capabilityType != null ? capabilityType.getIcon() : "ri-flashlight-line";
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<String> getDependencies() {
        if (dependencies != null) {
            return dependencies;
        }
        return capabilities;
    }

    public void setDependencies(List<String> dependencies) {
        this.dependencies = dependencies;
    }

    public List<String> getOptionalCapabilities() {
        if (optionalCapabilities != null) {
            return optionalCapabilities;
        }
        List<String> optional = new ArrayList<String>();
        if (collaborativeCapabilities != null) {
            for (CollaborativeCapabilityRef ref : collaborativeCapabilities) {
                optional.add(ref.getCapabilityId());
            }
        }
        return optional;
    }

    public void setOptionalCapabilities(List<String> optionalCapabilities) {
        this.optionalCapabilities = optionalCapabilities;
    }

    public SkillForm getSkillForm() {
        return skillForm;
    }

    public void setSkillForm(SkillForm skillForm) {
        this.skillForm = skillForm;
    }
    
    public void setSkillForm(String skillForm) {
        this.skillForm = SkillForm.fromCode(skillForm);
    }

    public SceneType getSceneTypeEnum() {
        return sceneType;
    }
    
    public String getSceneType() {
        return sceneType != null ? sceneType.getCode() : null;
    }

    public void setSceneType(SceneType sceneType) {
        this.sceneType = sceneType;
    }
    
    public void setSceneType(String sceneType) {
        this.sceneType = sceneType != null ? SceneType.fromCode(sceneType) : null;
    }

    public Visibility getVisibilityEnum() {
        return visibility;
    }
    
    public String getVisibility() {
        return visibility != null ? visibility.getCode() : null;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }
    
    public void setVisibility(String visibility) {
        this.visibility = visibility != null ? Visibility.fromCode(visibility) : Visibility.PUBLIC;
    }
    
    public CapabilityCategory getCapabilityCategory() {
        return capabilityCategory;
    }
    
    public void setCapabilityCategory(CapabilityCategory capabilityCategory) {
        this.capabilityCategory = capabilityCategory;
    }
    
    public void setCapabilityCategory(String capabilityCategory) {
        this.capabilityCategory = CapabilityCategory.fromCode(capabilityCategory);
    }
    
    public String getBusinessCategory() {
        return businessCategory;
    }
    
    public void setBusinessCategory(String businessCategory) {
        this.businessCategory = businessCategory;
    }
    
    public String getSubCategory() {
        return subCategory;
    }
    
    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }
    
    public List<String> getTags() {
        return tags;
    }
    
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    
    public List<CapabilityAddress> getRequiredAddresses() {
        return requiredAddresses;
    }
    
    public void setRequiredAddresses(List<CapabilityAddress> requiredAddresses) {
        this.requiredAddresses = requiredAddresses;
    }
    
    public List<CapabilityAddress> getOptionalAddresses() {
        return optionalAddresses;
    }
    
    public void setOptionalAddresses(List<CapabilityAddress> optionalAddresses) {
        this.optionalAddresses = optionalAddresses;
    }

    public List<DriverCondition> getDriverConditions() {
        return driverConditions != null ? driverConditions : new ArrayList<DriverCondition>();
    }

    public void setDriverConditions(List<DriverCondition> driverConditions) {
        this.driverConditions = driverConditions;
    }

    public List<Participant> getParticipants() {
        return participants != null ? participants : new ArrayList<Participant>();
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public boolean isPublicVisible() {
        return visibility == Visibility.PUBLIC || visibility == Visibility.DEVELOPER;
    }

    public boolean isInternalVisible() {
        return visibility == Visibility.INTERNAL;
    }
    
    public boolean isDeveloperVisible() {
        return visibility == Visibility.DEVELOPER;
    }
    
    public boolean isDynamicSceneTypes() {
        return dynamicSceneTypes;
    }
    
    public void setDynamicSceneTypes(boolean dynamicSceneTypes) {
        this.dynamicSceneTypes = dynamicSceneTypes;
    }
    
    public String getParentSkill() {
        return parentSkill;
    }
    
    public void setParentSkill(String parentSkill) {
        this.parentSkill = parentSkill;
    }
    
    public String getParentScene() {
        return parentScene;
    }
    
    public void setParentScene(String parentScene) {
        this.parentScene = parentScene;
    }
    
    public CapabilityOwnership getOwnership() {
        if (parentSkill != null && parentScene != null) {
            return CapabilityOwnership.SCENE_INTERNAL;
        } else if (supportedSceneTypes != null && !supportedSceneTypes.isEmpty()) {
            return CapabilityOwnership.INDEPENDENT;
        } else {
            return CapabilityOwnership.PLATFORM;
        }
    }
    
    public boolean isSceneInternal() {
        return getOwnership() == CapabilityOwnership.SCENE_INTERNAL;
    }
    
    public boolean isIndependent() {
        return getOwnership() == CapabilityOwnership.INDEPENDENT;
    }
    
    public boolean isPlatform() {
        return getOwnership() == CapabilityOwnership.PLATFORM;
    }
    
    public void addSceneType(String sceneType) {
        if (supportedSceneTypes == null) {
            supportedSceneTypes = new ArrayList<String>();
        }
        if (!supportedSceneTypes.contains(sceneType)) {
            supportedSceneTypes.add(sceneType);
            this.updateTime = System.currentTimeMillis();
        }
    }
    
    public void removeSceneType(String sceneType) {
        if (supportedSceneTypes != null) {
            supportedSceneTypes.remove(sceneType);
            this.updateTime = System.currentTimeMillis();
        }
    }

    public static class Participant implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private String role;
        private String name;
        private String userId;
        private List<String> permissions;

        public Participant() {
            this.permissions = new ArrayList<String>();
        }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public List<String> getPermissions() { return permissions; }
        public void setPermissions(List<String> permissions) { this.permissions = permissions; }
    }

    public static class ParameterDef implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private String name;
        private String type;
        private boolean required;
        private Object defaultValue;
        private String description;

        public ParameterDef() {}

        public ParameterDef(String name, String type, boolean required) {
            this.name = name;
            this.type = type;
            this.required = required;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public boolean isRequired() { return required; }
        public void setRequired(boolean required) { this.required = required; }
        public Object getDefaultValue() { return defaultValue; }
        public void setDefaultValue(Object defaultValue) { this.defaultValue = defaultValue; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class ReturnDef implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private String type;
        private Map<String, String> properties;

        public ReturnDef() {
            this.properties = new HashMap<String, String>();
        }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public Map<String, String> getProperties() { return properties; }
        public void setProperties(Map<String, String> properties) { this.properties = properties; }
    }
}

package net.ooder.skill.discovery.dto.discovery;

import net.ooder.skill.discovery.dto.base.FullDTO;
import java.util.List;
import java.util.Map;

public class CapabilityDTO extends FullDTO {
    
    private String capabilityId;
    private String capabilityType;
    private String capabilityCategory;
    private String skillId;
    private String skillForm;
    private String sceneType;
    private String visibility;
    private String version;
    private String source;
    private String icon;
    private String ownership;
    private String category;
    private String businessCategory;
    private String subCategory;
    private List<String> tags;
    private List<String> capabilities;
    private List<String> dependencies;
    private List<String> supportedSceneTypes;
    private List<Map<String, Object>> driverConditions;
    private List<Map<String, Object>> participants;
    private List<Map<String, Object>> parameters;
    private Map<String, Object> returns;
    private Map<String, Object> metadata;
    private Map<String, Object> mainFirstConfig;
    private String driverType;
    private boolean sceneCapability;
    private boolean hasSelfDrive;
    private boolean mainFirst;
    private Boolean installed;
    private Boolean enabled;
    private String installSource;
    
    public CapabilityDTO() {
        super();
    }
    
    public String getCapabilityId() { 
        return capabilityId; 
    }
    
    public void setCapabilityId(String capabilityId) { 
        this.capabilityId = capabilityId; 
        this.id = capabilityId;
    }
    
    public String getCapabilityType() { 
        return capabilityType; 
    }
    
    public void setCapabilityType(String capabilityType) { 
        this.capabilityType = capabilityType; 
    }
    
    public String getCapabilityCategory() { 
        return capabilityCategory; 
    }
    
    public void setCapabilityCategory(String capabilityCategory) { 
        this.capabilityCategory = capabilityCategory; 
    }
    
    public String getSkillId() { 
        return skillId; 
    }
    
    public void setSkillId(String skillId) { 
        this.skillId = skillId; 
    }
    
    public String getSkillForm() { 
        return skillForm; 
    }
    
    public void setSkillForm(String skillForm) { 
        this.skillForm = skillForm; 
    }
    
    public String getSceneType() { 
        return sceneType; 
    }
    
    public void setSceneType(String sceneType) { 
        this.sceneType = sceneType; 
    }
    
    public String getVisibility() { 
        return visibility; 
    }
    
    public void setVisibility(String visibility) { 
        this.visibility = visibility; 
    }
    
    public String getVersion() { 
        return version; 
    }
    
    public void setVersion(String version) { 
        this.version = version; 
    }
    
    public String getSource() { 
        return source; 
    }
    
    public void setSource(String source) { 
        this.source = source; 
    }
    
    public String getIcon() { 
        return icon; 
    }
    
    public void setIcon(String icon) { 
        this.icon = icon; 
    }
    
    public String getOwnership() { 
        return ownership; 
    }
    
    public void setOwnership(String ownership) { 
        this.ownership = ownership; 
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
    
    public List<String> getCapabilities() { 
        return capabilities; 
    }
    
    public void setCapabilities(List<String> capabilities) { 
        this.capabilities = capabilities; 
    }
    
    public List<String> getDependencies() { 
        return dependencies; 
    }
    
    public void setDependencies(List<String> dependencies) { 
        this.dependencies = dependencies; 
    }
    
    public List<Map<String, Object>> getDriverConditions() { 
        return driverConditions; 
    }
    
    public void setDriverConditions(List<Map<String, Object>> driverConditions) { 
        this.driverConditions = driverConditions; 
    }
    
    public List<Map<String, Object>> getParticipants() { 
        return participants; 
    }
    
    public void setParticipants(List<Map<String, Object>> participants) { 
        this.participants = participants; 
    }
    
    public String getCategory() { 
        return category; 
    }
    
    public void setCategory(String category) { 
        this.category = category; 
    }
    
    public String getBusinessCategory() { 
        return businessCategory; 
    }
    
    public void setBusinessCategory(String businessCategory) { 
        this.businessCategory = businessCategory; 
    }
    
    public boolean isSceneCapability() { 
        return sceneCapability; 
    }
    
    public void setSceneCapability(boolean sceneCapability) { 
        this.sceneCapability = sceneCapability; 
    }
    
    public boolean isHasSelfDrive() { 
        return hasSelfDrive; 
    }
    
    public void setHasSelfDrive(boolean hasSelfDrive) { 
        this.hasSelfDrive = hasSelfDrive; 
    }
    
    public boolean isMainFirst() { 
        return mainFirst; 
    }
    
    public void setMainFirst(boolean mainFirst) { 
        this.mainFirst = mainFirst; 
    }
    
    public Boolean getInstalled() { 
        return installed; 
    }
    
    public void setInstalled(Boolean installed) { 
        this.installed = installed; 
    }
    
    public boolean isInstalled() { 
        return installed != null && installed; 
    }
    
    public Boolean getEnabled() { 
        return enabled; 
    }
    
    public void setEnabled(Boolean enabled) { 
        this.enabled = enabled; 
    }
    
    public boolean isEnabled() { 
        return enabled != null && enabled; 
    }
    
    public List<String> getSupportedSceneTypes() { 
        return supportedSceneTypes; 
    }
    
    public void setSupportedSceneTypes(List<String> supportedSceneTypes) { 
        this.supportedSceneTypes = supportedSceneTypes; 
    }
    
    public List<Map<String, Object>> getParameters() { 
        return parameters; 
    }
    
    public void setParameters(List<Map<String, Object>> parameters) { 
        this.parameters = parameters; 
    }
    
    public Map<String, Object> getReturns() { 
        return returns; 
    }
    
    public void setReturns(Map<String, Object> returns) { 
        this.returns = returns; 
    }
    
    public Map<String, Object> getMetadata() { 
        return metadata; 
    }
    
    public void setMetadata(Map<String, Object> metadata) { 
        this.metadata = metadata; 
    }
    
    public Map<String, Object> getMainFirstConfig() { 
        return mainFirstConfig; 
    }
    
    public void setMainFirstConfig(Map<String, Object> mainFirstConfig) { 
        this.mainFirstConfig = mainFirstConfig; 
    }
    
    public String getDriverType() { 
        return driverType; 
    }
    
    public void setDriverType(String driverType) { 
        this.driverType = driverType; 
    }
    
    @Deprecated
    public String getType() { 
        return capabilityType; 
    }
    
    @Deprecated
    public void setType(String type) { 
        this.capabilityType = type; 
    }
    
    @Deprecated
    public void setCapId(String capId) { 
        this.capabilityId = capId; 
    }
    
    @Deprecated
    public String getCapId() { 
        return this.capabilityId; 
    }
    
    public String getInstallSource() {
        return installSource;
    }
    
    public void setInstallSource(String installSource) {
        this.installSource = installSource;
    }
    
    private String installPath;
    
    public String getInstallPath() {
        return installPath;
    }
    
    public void setInstallPath(String installPath) {
        this.installPath = installPath;
    }
}

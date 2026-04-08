package net.ooder.mvp.skill.scene.dto.discovery;

import java.util.List;

public class CapabilityDetailDTO {
    
    private String id;
    private String name;
    private String description;
    private String version;
    private String icon;
    private String skillId;
    private boolean installed;
    private String visibility;
    private String skillForm;
    private String sceneType;
    private boolean sceneCapability;
    private boolean hasSelfDrive;
    private String businessCategory;
    private Integer businessSemanticsScore;
    private boolean mainFirst;
    private String category;
    private String capabilityCategory;
    private List<String> dependencies;
    private List<String> tags;
    private List<String> participants;
    private String type;
    
    public CapabilityDetailDTO() {
    }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    
    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    
    public boolean isInstalled() { return installed; }
    public void setInstalled(boolean installed) { this.installed = installed; }
    
    public String getVisibility() { return visibility; }
    public void setVisibility(String visibility) { this.visibility = visibility; }
    
    public String getSkillForm() { return skillForm; }
    public void setSkillForm(String skillForm) { this.skillForm = skillForm; }
    
    public String getSceneType() { return sceneType; }
    public void setSceneType(String sceneType) { this.sceneType = sceneType; }
    
    public boolean isSceneCapability() { return sceneCapability; }
    public void setSceneCapability(boolean sceneCapability) { this.sceneCapability = sceneCapability; }
    
    public boolean isHasSelfDrive() { return hasSelfDrive; }
    public void setHasSelfDrive(boolean hasSelfDrive) { this.hasSelfDrive = hasSelfDrive; }
    
    public String getBusinessCategory() { return businessCategory; }
    public void setBusinessCategory(String businessCategory) { this.businessCategory = businessCategory; }
    
    public Integer getBusinessSemanticsScore() { return businessSemanticsScore; }
    public void setBusinessSemanticsScore(Integer businessSemanticsScore) { this.businessSemanticsScore = businessSemanticsScore; }
    
    public boolean isMainFirst() { return mainFirst; }
    public void setMainFirst(boolean mainFirst) { this.mainFirst = mainFirst; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getCapabilityCategory() { return capabilityCategory; }
    public void setCapabilityCategory(String capabilityCategory) { this.capabilityCategory = capabilityCategory; }
    
    public List<String> getDependencies() { return dependencies; }
    public void setDependencies(List<String> dependencies) { this.dependencies = dependencies; }
    
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    
    public List<String> getParticipants() { return participants; }
    public void setParticipants(List<String> participants) { this.participants = participants; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}

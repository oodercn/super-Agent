
package net.ooder.sdk.api.skill;

import java.util.List;
import java.util.Map;

public class SkillManifest {
    
    private String skillId;
    private String name;
    private String description;
    private String version;
    private String sceneId;
    private String mainClass;
    private String skillType;
    private List<Capability> capabilities;
    private List<Dependency> dependencies;
    private List<String> collaborativeScenes;
    private List<SceneDependency> collaborativeSceneDependencies;
    private SceneConfig primaryScene;
    private Map<String, Parameter> parameters;
    private Map<String, Object> config;
    private String author;
    private String license;
    private String homepage;
    private String category;
    private String subCategory;
    private List<String> tags;
    
    public String getSkillId() {
        return skillId;
    }
    
    public void setSkillId(String skillId) {
        this.skillId = skillId;
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
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getSceneId() {
        return sceneId;
    }
    
    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }
    
    public String getMainClass() {
        return mainClass;
    }
    
    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }
    
    public String getSkillType() {
        return skillType;
    }
    
    public void setSkillType(String skillType) {
        this.skillType = skillType;
    }
    
    public List<Capability> getCapabilities() {
        return capabilities;
    }
    
    public void setCapabilities(List<Capability> capabilities) {
        this.capabilities = capabilities;
    }
    
    public List<Dependency> getDependencies() {
        return dependencies;
    }
    
    public void setDependencies(List<Dependency> dependencies) {
        this.dependencies = dependencies;
    }
    
    public List<String> getCollaborativeScenes() {
        return collaborativeScenes;
    }
    
    public void setCollaborativeScenes(List<String> collaborativeScenes) {
        this.collaborativeScenes = collaborativeScenes;
    }
    
    public List<SceneDependency> getCollaborativeSceneDependencies() {
        return collaborativeSceneDependencies;
    }
    
    public void setCollaborativeSceneDependencies(List<SceneDependency> collaborativeSceneDependencies) {
        this.collaborativeSceneDependencies = collaborativeSceneDependencies;
    }
    
    public SceneConfig getPrimaryScene() {
        return primaryScene;
    }
    
    public void setPrimaryScene(SceneConfig primaryScene) {
        this.primaryScene = primaryScene;
    }
    
    public List<CapabilityInfo> getProvidedCapabilities() {
        return new java.util.ArrayList<>();
    }
    
    public Map<String, Parameter> getParameters() {
        return parameters;
    }
    
    public void setParameters(Map<String, Parameter> parameters) {
        this.parameters = parameters;
    }
    
    public Map<String, Object> getConfig() {
        return config;
    }
    
    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getLicense() {
        return license;
    }
    
    public void setLicense(String license) {
        this.license = license;
    }
    
    public String getHomepage() {
        return homepage;
    }
    
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
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
    
    public static class Dependency {
        private String skillId;
        private String versionRange;
        private boolean required;
        
        public String getSkillId() { return skillId; }
        public void setSkillId(String skillId) { this.skillId = skillId; }
        public String getVersionRange() { return versionRange; }
        public void setVersionRange(String versionRange) { this.versionRange = versionRange; }
        public boolean isRequired() { return required; }
        public void setRequired(boolean required) { this.required = required; }
    }
}

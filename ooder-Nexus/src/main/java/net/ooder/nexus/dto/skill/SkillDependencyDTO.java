package net.ooder.nexus.dto.skill;

import java.util.List;

/**
 * 技能依赖 DTO
 */
public class SkillDependencyDTO {
    
    private String skillId;
    private String skillName;
    private String version;
    private List<DependencyInfo> dependencies;
    private List<DependencyInfo> missingDependencies;
    private List<DependencyInfo> outdatedDependencies;
    private boolean satisfied;

    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    
    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public List<DependencyInfo> getDependencies() { return dependencies; }
    public void setDependencies(List<DependencyInfo> dependencies) { this.dependencies = dependencies; }
    
    public List<DependencyInfo> getMissingDependencies() { return missingDependencies; }
    public void setMissingDependencies(List<DependencyInfo> missingDependencies) { this.missingDependencies = missingDependencies; }
    
    public List<DependencyInfo> getOutdatedDependencies() { return outdatedDependencies; }
    public void setOutdatedDependencies(List<DependencyInfo> outdatedDependencies) { this.outdatedDependencies = outdatedDependencies; }
    
    public boolean isSatisfied() { return satisfied; }
    public void setSatisfied(boolean satisfied) { this.satisfied = satisfied; }

    public static class DependencyInfo {
        private String dependencyId;
        private String name;
        private String requiredVersion;
        private String installedVersion;
        private String type;
        private String status;
        private String description;

        public String getDependencyId() { return dependencyId; }
        public void setDependencyId(String dependencyId) { this.dependencyId = dependencyId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getRequiredVersion() { return requiredVersion; }
        public void setRequiredVersion(String requiredVersion) { this.requiredVersion = requiredVersion; }
        public String getInstalledVersion() { return installedVersion; }
        public void setInstalledVersion(String installedVersion) { this.installedVersion = installedVersion; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}

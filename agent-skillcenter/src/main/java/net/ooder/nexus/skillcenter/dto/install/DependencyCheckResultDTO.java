package net.ooder.nexus.skillcenter.dto.install;

import java.util.List;

public class DependencyCheckResultDTO {
    
    private String skillId;
    private boolean canInstall;
    private List<DependencyDTO> dependencies;
    private List<String> missingDependencies;
    private List<String> conflictDependencies;
    private String message;

    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }

    public boolean isCanInstall() { return canInstall; }
    public void setCanInstall(boolean canInstall) { this.canInstall = canInstall; }

    public List<DependencyDTO> getDependencies() { return dependencies; }
    public void setDependencies(List<DependencyDTO> dependencies) { this.dependencies = dependencies; }

    public List<String> getMissingDependencies() { return missingDependencies; }
    public void setMissingDependencies(List<String> missingDependencies) { this.missingDependencies = missingDependencies; }

    public List<String> getConflictDependencies() { return conflictDependencies; }
    public void setConflictDependencies(List<String> conflictDependencies) { this.conflictDependencies = conflictDependencies; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}

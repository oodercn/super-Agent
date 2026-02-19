
package net.ooder.sdk.api.skill;

import java.util.List;
import java.util.Map;

import net.ooder.sdk.common.enums.DiscoveryMethod;

public class InstallRequest {
    
    private String skillId;
    private String version;
    private String source;
    private DiscoveryMethod discoveryMethod;
    private String downloadUrl;
    private InstallMode mode;
    private boolean installDependencies;
    private boolean forceOverwrite;
    private List<String> collaborativeScenes;
    private String targetPath;
    private Map<String, String> options;
    private String owner;
    private String repo;
    private String branch;
    
    public String getSkillId() {
        return skillId;
    }
    
    public void setSkillId(String skillId) {
        this.skillId = skillId;
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
    
    public DiscoveryMethod getDiscoveryMethod() {
        return discoveryMethod;
    }
    
    public void setDiscoveryMethod(DiscoveryMethod discoveryMethod) {
        this.discoveryMethod = discoveryMethod;
    }
    
    public String getDownloadUrl() {
        return downloadUrl;
    }
    
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
    
    public InstallMode getMode() {
        return mode;
    }
    
    public void setMode(InstallMode mode) {
        this.mode = mode;
    }
    
    public boolean isInstallDependencies() {
        return installDependencies;
    }
    
    public void setInstallDependencies(boolean installDependencies) {
        this.installDependencies = installDependencies;
    }
    
    public boolean isForceOverwrite() {
        return forceOverwrite;
    }
    
    public void setForceOverwrite(boolean forceOverwrite) {
        this.forceOverwrite = forceOverwrite;
    }
    
    public List<String> getCollaborativeScenes() {
        return collaborativeScenes;
    }
    
    public void setCollaborativeScenes(List<String> collaborativeScenes) {
        this.collaborativeScenes = collaborativeScenes;
    }
    
    public String getTargetPath() {
        return targetPath;
    }
    
    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }
    
    public Map<String, String> getOptions() {
        return options;
    }
    
    public void setOptions(Map<String, String> options) {
        this.options = options;
    }
    
    public String getOwner() {
        return owner;
    }
    
    public void setOwner(String owner) {
        this.owner = owner;
    }
    
    public String getRepo() {
        return repo;
    }
    
    public void setRepo(String repo) {
        this.repo = repo;
    }
    
    public String getBranch() {
        return branch;
    }
    
    public void setBranch(String branch) {
        this.branch = branch;
    }
    
    public enum InstallMode {
        NORMAL,
        FORCE,
        UPGRADE
    }
}

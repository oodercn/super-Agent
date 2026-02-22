package net.ooder.nexus.skillcenter.dto.install;

import java.util.List;

public class InstallRequestDTO {
    
    private String skillId;
    private String source;
    private String version;
    private boolean autoInstallDependencies;
    private boolean forceReinstall;
    private String targetPath;
    private List<String> options;

    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public boolean isAutoInstallDependencies() { return autoInstallDependencies; }
    public void setAutoInstallDependencies(boolean autoInstallDependencies) { this.autoInstallDependencies = autoInstallDependencies; }

    public boolean isForceReinstall() { return forceReinstall; }
    public void setForceReinstall(boolean forceReinstall) { this.forceReinstall = forceReinstall; }

    public String getTargetPath() { return targetPath; }
    public void setTargetPath(String targetPath) { this.targetPath = targetPath; }

    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }
}

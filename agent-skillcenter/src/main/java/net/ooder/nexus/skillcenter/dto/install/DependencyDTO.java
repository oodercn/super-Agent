package net.ooder.nexus.skillcenter.dto.install;

public class DependencyDTO {
    
    private String dependencyId;
    private String name;
    private String version;
    private String requiredVersion;
    private boolean installed;
    private boolean compatible;
    private String description;

    public String getDependencyId() { return dependencyId; }
    public void setDependencyId(String dependencyId) { this.dependencyId = dependencyId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public String getRequiredVersion() { return requiredVersion; }
    public void setRequiredVersion(String requiredVersion) { this.requiredVersion = requiredVersion; }

    public boolean isInstalled() { return installed; }
    public void setInstalled(boolean installed) { this.installed = installed; }

    public boolean isCompatible() { return compatible; }
    public void setCompatible(boolean compatible) { this.compatible = compatible; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

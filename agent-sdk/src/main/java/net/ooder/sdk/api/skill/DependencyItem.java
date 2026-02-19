
package net.ooder.sdk.api.skill;

public class DependencyItem {
    
    private String name;
    private String requiredVersion;
    private String installedVersion;
    private String status;
    private boolean required;
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getRequiredVersion() { return requiredVersion; }
    public void setRequiredVersion(String requiredVersion) { this.requiredVersion = requiredVersion; }
    
    public String getInstalledVersion() { return installedVersion; }
    public void setInstalledVersion(String installedVersion) { this.installedVersion = installedVersion; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }
    
    public boolean isSatisfied() {
        return "satisfied".equals(status);
    }
    
    public boolean isMissing() {
        return "missing".equals(status);
    }
    
    public boolean isOutdated() {
        return "outdated".equals(status);
    }
}

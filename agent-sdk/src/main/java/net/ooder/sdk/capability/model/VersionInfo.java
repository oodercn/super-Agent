package net.ooder.sdk.capability.model;

public class VersionInfo {
    
    private String version;
    private String description;
    private long releaseTime;
    private String changelog;
    private boolean isLatest;
    private boolean isStable;
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public long getReleaseTime() { return releaseTime; }
    public void setReleaseTime(long releaseTime) { this.releaseTime = releaseTime; }
    
    public String getChangelog() { return changelog; }
    public void setChangelog(String changelog) { this.changelog = changelog; }
    
    public boolean isLatest() { return isLatest; }
    public void setLatest(boolean latest) { isLatest = latest; }
    
    public boolean isStable() { return isStable; }
    public void setStable(boolean stable) { isStable = stable; }
}

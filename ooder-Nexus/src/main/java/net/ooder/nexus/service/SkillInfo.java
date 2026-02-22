package net.ooder.nexus.service;

public class SkillInfo {
    
    private String skillId;
    private String name;
    private String version;
    private String description;
    private String status;
    private long installTime;
    private long lastUsedTime;

    public SkillInfo() {}

    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public long getInstallTime() { return installTime; }
    public void setInstallTime(long installTime) { this.installTime = installTime; }
    
    public long getLastUsedTime() { return lastUsedTime; }
    public void setLastUsedTime(long lastUsedTime) { this.lastUsedTime = lastUsedTime; }
}

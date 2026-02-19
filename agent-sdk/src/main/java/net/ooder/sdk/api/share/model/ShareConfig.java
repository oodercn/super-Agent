package net.ooder.sdk.api.share.model;

import java.util.List;
import java.util.Map;

public class ShareConfig {
    
    private String skillId;
    private String skillName;
    private String version;
    private boolean includeDependencies;
    private boolean includeConfig;
    private List<String> excludedFiles;
    private Map<String, Object> options;
    private long expirationTime;
    
    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    
    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public boolean isIncludeDependencies() { return includeDependencies; }
    public void setIncludeDependencies(boolean includeDependencies) { this.includeDependencies = includeDependencies; }
    
    public boolean isIncludeConfig() { return includeConfig; }
    public void setIncludeConfig(boolean includeConfig) { this.includeConfig = includeConfig; }
    
    public List<String> getExcludedFiles() { return excludedFiles; }
    public void setExcludedFiles(List<String> excludedFiles) { this.excludedFiles = excludedFiles; }
    
    public Map<String, Object> getOptions() { return options; }
    public void setOptions(Map<String, Object> options) { this.options = options; }
    
    public long getExpirationTime() { return expirationTime; }
    public void setExpirationTime(long expirationTime) { this.expirationTime = expirationTime; }
}

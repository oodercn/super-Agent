
package net.ooder.sdk.core.skill.installer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ooder.sdk.api.skill.InstallRequest;
import net.ooder.sdk.api.skill.SkillPackage;

public class InstallContext {
    
    private String contextId;
    private InstallRequest request;
    private SkillPackage skillPackage;
    private String installPath;
    private Map<String, Object> properties;
    private List<String> installedFiles;
    private List<String> installedDependencies;
    private InstallStatus status;
    private String errorMessage;
    private long startTime;
    private long endTime;
    private Map<String, Object> rollbackData;
    
    public InstallContext() {
        this.contextId = java.util.UUID.randomUUID().toString();
        this.properties = new HashMap<>();
        this.installedFiles = new ArrayList<>();
        this.installedDependencies = new ArrayList<>();
        this.rollbackData = new HashMap<>();
        this.status = InstallStatus.INITIALIZED;
    }
    
    public String getContextId() { return contextId; }
    
    public InstallRequest getRequest() { return request; }
    public void setRequest(InstallRequest request) { this.request = request; }
    
    public String getSkillId() {
        return request != null ? request.getSkillId() : null;
    }
    public void setSkillId(String skillId) {
        if (request == null) {
            request = new InstallRequest();
        }
        request.setSkillId(skillId);
    }
    
    public SkillPackage getSkillPackage() { return skillPackage; }
    public void setSkillPackage(SkillPackage skillPackage) { this.skillPackage = skillPackage; }
    
    public String getInstallPath() { return installPath; }
    public void setInstallPath(String installPath) { this.installPath = installPath; }
    
    public Map<String, Object> getProperties() { return properties; }
    public void setProperty(String key, Object value) { properties.put(key, value); }
    public Object getProperty(String key) { return properties.get(key); }
    
    public List<String> getInstalledFiles() { return installedFiles; }
    public void addInstalledFile(String file) { installedFiles.add(file); }
    
    public List<String> getInstalledDependencies() { return installedDependencies; }
    public void addInstalledDependency(String dep) { installedDependencies.add(dep); }
    
    public InstallStatus getStatus() { return status; }
    public void setStatus(InstallStatus status) { this.status = status; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }
    
    public long getDuration() {
        if (startTime > 0 && endTime > 0) {
            return endTime - startTime;
        }
        return 0;
    }
    
    public Map<String, Object> getRollbackData() { return rollbackData; }
    public void setRollbackData(String key, Object value) { rollbackData.put(key, value); }
    public Object getRollbackData(String key) { return rollbackData.get(key); }
}

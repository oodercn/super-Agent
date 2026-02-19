package net.ooder.sdk.northbound.protocol.model;

import java.util.List;
import java.util.Map;

public class DomainPolicyConfig {
    
    private String domainId;
    private List<String> allowedSkills;
    private List<String> requiredSkills;
    private Map<String, Object> storageConfig;
    private Map<String, Object> securityConfig;
    private Map<String, Object> networkConfig;
    private Map<String, Object> collaborationConfig;
    
    public String getDomainId() { return domainId; }
    public void setDomainId(String domainId) { this.domainId = domainId; }
    
    public List<String> getAllowedSkills() { return allowedSkills; }
    public void setAllowedSkills(List<String> allowedSkills) { this.allowedSkills = allowedSkills; }
    
    public List<String> getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; }
    
    public Map<String, Object> getStorageConfig() { return storageConfig; }
    public void setStorageConfig(Map<String, Object> storageConfig) { this.storageConfig = storageConfig; }
    
    public Map<String, Object> getSecurityConfig() { return securityConfig; }
    public void setSecurityConfig(Map<String, Object> securityConfig) { this.securityConfig = securityConfig; }
    
    public Map<String, Object> getNetworkConfig() { return networkConfig; }
    public void setNetworkConfig(Map<String, Object> networkConfig) { this.networkConfig = networkConfig; }
    
    public Map<String, Object> getCollaborationConfig() { return collaborationConfig; }
    public void setCollaborationConfig(Map<String, Object> collaborationConfig) { this.collaborationConfig = collaborationConfig; }
}


package net.ooder.sdk.southbound.adapter.model;

import java.util.List;
import java.util.Map;

public class DomainConfig {
    
    private String domainId;
    private String domainName;
    private String domainType;
    private String description;
    private int maxMembers;
    private Map<String, Object> policies;
    private List<String> allowedRoles;
    private String owner;
    
    public String getDomainId() { return domainId; }
    public void setDomainId(String domainId) { this.domainId = domainId; }
    
    public String getDomainName() { return domainName; }
    public void setDomainName(String domainName) { this.domainName = domainName; }
    
    public String getDomainType() { return domainType; }
    public void setDomainType(String domainType) { this.domainType = domainType; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public int getMaxMembers() { return maxMembers; }
    public void setMaxMembers(int maxMembers) { this.maxMembers = maxMembers; }
    
    public Map<String, Object> getPolicies() { return policies; }
    public void setPolicies(Map<String, Object> policies) { this.policies = policies; }
    
    public List<String> getAllowedRoles() { return allowedRoles; }
    public void setAllowedRoles(List<String> allowedRoles) { this.allowedRoles = allowedRoles; }
    
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
}

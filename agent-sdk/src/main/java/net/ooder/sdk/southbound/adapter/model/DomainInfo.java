
package net.ooder.sdk.southbound.adapter.model;

import java.util.List;
import java.util.Map;

public class DomainInfo {
    
    private String domainId;
    private String domainName;
    private String domainType;
    private String status;
    private String owner;
    private int memberCount;
    private int maxMembers;
    private long createdAt;
    private long updatedAt;
    private Map<String, Object> policies;
    private List<String> allowedRoles;
    
    public String getDomainId() { return domainId; }
    public void setDomainId(String domainId) { this.domainId = domainId; }
    
    public String getDomainName() { return domainName; }
    public void setDomainName(String domainName) { this.domainName = domainName; }
    
    public String getDomainType() { return domainType; }
    public void setDomainType(String domainType) { this.domainType = domainType; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    
    public int getMemberCount() { return memberCount; }
    public void setMemberCount(int memberCount) { this.memberCount = memberCount; }
    
    public int getMaxMembers() { return maxMembers; }
    public void setMaxMembers(int maxMembers) { this.maxMembers = maxMembers; }
    
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    
    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
    
    public Map<String, Object> getPolicies() { return policies; }
    public void setPolicies(Map<String, Object> policies) { this.policies = policies; }
    
    public List<String> getAllowedRoles() { return allowedRoles; }
    public void setAllowedRoles(List<String> allowedRoles) { this.allowedRoles = allowedRoles; }
    
    public boolean isActive() {
        return "active".equals(status);
    }
    
    public boolean canAddMember() {
        return memberCount < maxMembers;
    }
}

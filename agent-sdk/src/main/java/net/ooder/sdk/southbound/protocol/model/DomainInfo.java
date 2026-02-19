package net.ooder.sdk.southbound.protocol.model;

public class DomainInfo {
    
    private String domainId;
    private String domainName;
    private String domainType;
    private String role;
    
    public String getDomainId() { return domainId; }
    public void setDomainId(String domainId) { this.domainId = domainId; }
    
    public String getDomainName() { return domainName; }
    public void setDomainName(String domainName) { this.domainName = domainName; }
    
    public String getDomainType() { return domainType; }
    public void setDomainType(String domainType) { this.domainType = domainType; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}

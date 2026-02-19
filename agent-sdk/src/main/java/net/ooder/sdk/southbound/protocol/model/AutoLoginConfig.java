package net.ooder.sdk.southbound.protocol.model;

public class AutoLoginConfig {
    
    private String userId;
    private String credential;
    private String domain;
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getCredential() { return credential; }
    public void setCredential(String credential) { this.credential = credential; }
    
    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
}

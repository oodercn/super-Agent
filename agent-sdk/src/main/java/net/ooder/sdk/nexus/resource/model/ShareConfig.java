package net.ooder.sdk.nexus.resource.model;

import java.util.List;

public class ShareConfig {
    
    private String resourceType;
    private long expirationTime;
    private int maxAccess;
    private String password;
    private List<String> allowedUsers;
    
    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }
    
    public long getExpirationTime() { return expirationTime; }
    public void setExpirationTime(long expirationTime) { this.expirationTime = expirationTime; }
    
    public int getMaxAccess() { return maxAccess; }
    public void setMaxAccess(int maxAccess) { this.maxAccess = maxAccess; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public List<String> getAllowedUsers() { return allowedUsers; }
    public void setAllowedUsers(List<String> allowedUsers) { this.allowedUsers = allowedUsers; }
}

package net.ooder.sdk.nexus.resource.model;

public class ShareLink {
    
    private String shareId;
    private String resourceId;
    private String shareUrl;
    private String accessToken;
    private long createdAt;
    private long expiresAt;
    private int accessCount;
    private int maxAccess;
    
    public String getShareId() { return shareId; }
    public void setShareId(String shareId) { this.shareId = shareId; }
    
    public String getResourceId() { return resourceId; }
    public void setResourceId(String resourceId) { this.resourceId = resourceId; }
    
    public String getShareUrl() { return shareUrl; }
    public void setShareUrl(String shareUrl) { this.shareUrl = shareUrl; }
    
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    
    public long getExpiresAt() { return expiresAt; }
    public void setExpiresAt(long expiresAt) { this.expiresAt = expiresAt; }
    
    public int getAccessCount() { return accessCount; }
    public void setAccessCount(int accessCount) { this.accessCount = accessCount; }
    
    public int getMaxAccess() { return maxAccess; }
    public void setMaxAccess(int maxAccess) { this.maxAccess = maxAccess; }
}

package net.ooder.nexus.service;

import java.util.Date;

/**
 * 分享链接
 */
public class ShareLink {
    
    private String shareId;
    private String resourceId;
    private String shareUrl;
    private String accessCode;
    private Date createdTime;
    private Date expireTime;
    private int maxAccessCount;
    private int currentAccessCount;

    public ShareLink() {}

    public String getShareId() { return shareId; }
    public void setShareId(String shareId) { this.shareId = shareId; }
    
    public String getResourceId() { return resourceId; }
    public void setResourceId(String resourceId) { this.resourceId = resourceId; }
    
    public String getShareUrl() { return shareUrl; }
    public void setShareUrl(String shareUrl) { this.shareUrl = shareUrl; }
    
    public String getAccessCode() { return accessCode; }
    public void setAccessCode(String accessCode) { this.accessCode = accessCode; }
    
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    
    public Date getExpireTime() { return expireTime; }
    public void setExpireTime(Date expireTime) { this.expireTime = expireTime; }
    
    public int getMaxAccessCount() { return maxAccessCount; }
    public void setMaxAccessCount(int maxAccessCount) { this.maxAccessCount = maxAccessCount; }
    
    public int getCurrentAccessCount() { return currentAccessCount; }
    public void setCurrentAccessCount(int currentAccessCount) { this.currentAccessCount = currentAccessCount; }
    
    public boolean isExpired() {
        return expireTime != null && new Date().after(expireTime);
    }
}

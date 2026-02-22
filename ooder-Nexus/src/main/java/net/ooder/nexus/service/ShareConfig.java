package net.ooder.nexus.service;

/**
 * 分享配置
 */
public class ShareConfig {
    
    private long expireDuration;
    private int maxAccessCount;
    private boolean requireAccessCode;
    private String permission;

    public ShareConfig() {}

    public long getExpireDuration() { return expireDuration; }
    public void setExpireDuration(long expireDuration) { this.expireDuration = expireDuration; }
    
    public int getMaxAccessCount() { return maxAccessCount; }
    public void setMaxAccessCount(int maxAccessCount) { this.maxAccessCount = maxAccessCount; }
    
    public boolean isRequireAccessCode() { return requireAccessCode; }
    public void setRequireAccessCode(boolean requireAccessCode) { this.requireAccessCode = requireAccessCode; }
    
    public String getPermission() { return permission; }
    public void setPermission(String permission) { this.permission = permission; }
}

package net.ooder.nexus.service;

/**
 * 离线能力
 */
public class OfflineCapability {
    
    private String capabilityId;
    private String name;
    private String description;
    private boolean available;
    private long cacheSize;
    private long lastSyncTime;

    public OfflineCapability() {}

    public String getCapabilityId() { return capabilityId; }
    public void setCapabilityId(String capabilityId) { this.capabilityId = capabilityId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    
    public long getCacheSize() { return cacheSize; }
    public void setCacheSize(long cacheSize) { this.cacheSize = cacheSize; }
    
    public long getLastSyncTime() { return lastSyncTime; }
    public void setLastSyncTime(long lastSyncTime) { this.lastSyncTime = lastSyncTime; }
}

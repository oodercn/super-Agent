package net.ooder.sdk.nexus.offline.model;

public class OfflineCapability {
    
    private String capabilityId;
    private String capabilityType;
    private String name;
    private boolean available;
    private String description;
    private long lastSyncTime;
    private boolean requiresSync;
    
    public String getCapabilityId() { return capabilityId; }
    public void setCapabilityId(String capabilityId) { this.capabilityId = capabilityId; }
    
    public String getCapabilityType() { return capabilityType; }
    public void setCapabilityType(String capabilityType) { this.capabilityType = capabilityType; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public long getLastSyncTime() { return lastSyncTime; }
    public void setLastSyncTime(long lastSyncTime) { this.lastSyncTime = lastSyncTime; }
    
    public boolean isRequiresSync() { return requiresSync; }
    public void setRequiresSync(boolean requiresSync) { this.requiresSync = requiresSync; }
    
    public enum CapabilityType {
        SKILL,
        DATA,
        STORAGE,
        CONFIG
    }
}

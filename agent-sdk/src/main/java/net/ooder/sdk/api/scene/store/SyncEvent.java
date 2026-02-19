package net.ooder.sdk.api.scene.store;

public class SyncEvent {
    
    private String eventId;
    private String path;
    private SyncType type;
    private SyncDirection direction;
    private long startTime;
    private long endTime;
    private int totalItems;
    private int processedItems;
    private String status;
    
    public SyncEvent() {
        this.startTime = System.currentTimeMillis();
        this.status = "started";
    }
    
    public SyncEvent(String path, SyncType type) {
        this();
        this.path = path;
        this.type = type;
    }
    
    public String getEventId() {
        return eventId;
    }
    
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public SyncType getType() {
        return type;
    }
    
    public void setType(SyncType type) {
        this.type = type;
    }
    
    public SyncDirection getDirection() {
        return direction;
    }
    
    public void setDirection(SyncDirection direction) {
        this.direction = direction;
    }
    
    public long getStartTime() {
        return startTime;
    }
    
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    
    public long getEndTime() {
        return endTime;
    }
    
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
    
    public int getTotalItems() {
        return totalItems;
    }
    
    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }
    
    public int getProcessedItems() {
        return processedItems;
    }
    
    public void setProcessedItems(int processedItems) {
        this.processedItems = processedItems;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public long getDuration() {
        if (endTime > 0) {
            return endTime - startTime;
        }
        return System.currentTimeMillis() - startTime;
    }
    
    public enum SyncType {
        SCENE_CONFIG,
        GROUP_CONFIG,
        SKILL_REGISTRATION,
        LINK_CONFIG,
        AGENT_REGISTRATION,
        FULL_SYNC
    }
    
    public enum SyncDirection {
        LOCAL_TO_REMOTE,
        REMOTE_TO_LOCAL,
        BIDIRECTIONAL
    }
}

package net.ooder.sdk.api.scene.store;

public class StorageStatus {
    
    private boolean localAvailable;
    private boolean remoteAvailable;
    private long lastSyncTime;
    private int pendingChanges;
    private SyncDirection lastSyncDirection;
    private String lastError;
    
    public StorageStatus() {
        this.localAvailable = true;
        this.remoteAvailable = false;
        this.pendingChanges = 0;
    }
    
    public boolean isLocalAvailable() {
        return localAvailable;
    }
    
    public void setLocalAvailable(boolean localAvailable) {
        this.localAvailable = localAvailable;
    }
    
    public boolean isRemoteAvailable() {
        return remoteAvailable;
    }
    
    public void setRemoteAvailable(boolean remoteAvailable) {
        this.remoteAvailable = remoteAvailable;
    }
    
    public long getLastSyncTime() {
        return lastSyncTime;
    }
    
    public void setLastSyncTime(long lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }
    
    public int getPendingChanges() {
        return pendingChanges;
    }
    
    public void setPendingChanges(int pendingChanges) {
        this.pendingChanges = pendingChanges;
    }
    
    public void incrementPendingChanges() {
        this.pendingChanges++;
    }
    
    public void decrementPendingChanges() {
        if (this.pendingChanges > 0) {
            this.pendingChanges--;
        }
    }
    
    public SyncDirection getLastSyncDirection() {
        return lastSyncDirection;
    }
    
    public void setLastSyncDirection(SyncDirection lastSyncDirection) {
        this.lastSyncDirection = lastSyncDirection;
    }
    
    public String getLastError() {
        return lastError;
    }
    
    public void setLastError(String lastError) {
        this.lastError = lastError;
    }
    
    public boolean isFullyAvailable() {
        return localAvailable && remoteAvailable;
    }
    
    public enum SyncDirection {
        LOCAL_TO_REMOTE,
        REMOTE_TO_LOCAL,
        BIDIRECTIONAL
    }
}

package net.ooder.sdk.nexus.offline.model;

public interface SyncStateListener {
    
    void onSyncStarted(String syncId);
    
    void onSyncProgress(String syncId, int progress);
    
    void onSyncCompleted(String syncId, SyncResult result);
    
    void onSyncFailed(String syncId, String errorMessage);
    
    void onPendingSyncAdded(PendingSync sync);
    
    void onPendingSyncRemoved(String syncId);
}

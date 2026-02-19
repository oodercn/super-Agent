package net.ooder.sdk.api.scene.store;

public interface SyncListener {
    
    void onSyncStarted(SyncEvent event);
    
    void onSyncProgress(SyncEvent event, int progress);
    
    void onSyncCompleted(SyncEvent event);
    
    void onSyncFailed(SyncEvent event, Throwable error);
    
    void onConflictDetected(SyncEvent event, ConflictInfo conflict);
}

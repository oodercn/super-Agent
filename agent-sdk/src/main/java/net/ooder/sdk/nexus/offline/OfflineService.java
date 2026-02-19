package net.ooder.sdk.nexus.offline;

import net.ooder.sdk.nexus.offline.model.OfflineCapability;
import net.ooder.sdk.nexus.offline.model.PendingSync;
import net.ooder.sdk.nexus.offline.model.SyncResult;
import net.ooder.sdk.nexus.offline.model.NetworkStateListener;
import net.ooder.sdk.nexus.offline.model.SyncStateListener;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface OfflineService {
    
    CompletableFuture<Void> enableOfflineMode();
    
    CompletableFuture<Void> disableOfflineMode();
    
    boolean isOfflineMode();
    
    boolean isNetworkAvailable();
    
    CompletableFuture<List<OfflineCapability>> getOfflineCapabilities();
    
    CompletableFuture<List<PendingSync>> getPendingSyncItems();
    
    CompletableFuture<SyncResult> syncNow();
    
    CompletableFuture<Void> syncItem(String syncId);
    
    void addNetworkListener(NetworkStateListener listener);
    
    void removeNetworkListener(NetworkStateListener listener);
    
    void addSyncListener(SyncStateListener listener);
    
    void removeSyncListener(SyncStateListener listener);
}

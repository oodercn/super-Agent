package net.ooder.sdk.service.storage.vfs;

import net.ooder.sdk.api.scene.store.SyncEvent;
import net.ooder.sdk.api.scene.store.SyncListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

public class VfsSyncService {
    
    private static final Logger log = LoggerFactory.getLogger(VfsSyncService.class);
    
    private final VfsClient vfsClient;
    private final ScheduledExecutorService scheduler;
    private final Map<String, PendingSyncItem> pendingSyncs;
    private final List<SyncListener> syncListeners;
    
    private volatile boolean running = false;
    private ScheduledFuture<?> syncTask;
    private long syncIntervalMs = 60000;
    
    public VfsSyncService(VfsClient vfsClient) {
        this.vfsClient = vfsClient;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.pendingSyncs = new ConcurrentHashMap<>();
        this.syncListeners = new CopyOnWriteArrayList<>();
    }
    
    public void startSync(long intervalMs) {
        if (running) {
            log.warn("VfsSyncService is already running");
            return;
        }
        
        this.syncIntervalMs = intervalMs;
        this.running = true;
        
        syncTask = scheduler.scheduleAtFixedRate(
            this::syncPendingItems,
            intervalMs,
            intervalMs,
            TimeUnit.MILLISECONDS
        );
        
        log.info("VfsSyncService started with interval: {}ms", intervalMs);
    }
    
    public void stopSync() {
        if (!running) {
            return;
        }
        
        running = false;
        
        if (syncTask != null) {
            syncTask.cancel(false);
        }
        
        log.info("VfsSyncService stopped");
    }
    
    public void addToSyncQueue(String path, byte[] data, SyncType type) {
        String syncId = UUID.randomUUID().toString();
        
        PendingSyncItem item = new PendingSyncItem();
        item.setSyncId(syncId);
        item.setPath(path);
        item.setData(data);
        item.setType(type);
        item.setCreatedTime(System.currentTimeMillis());
        item.setRetryCount(0);
        
        pendingSyncs.put(syncId, item);
        
        log.debug("Added to sync queue: {} -> {}", syncId, path);
        
        notifyPendingAdded(item);
    }
    
    public void syncPendingItems() {
        if (!vfsClient.isConnected()) {
            log.debug("VFS not connected, skip sync");
            return;
        }
        
        if (pendingSyncs.isEmpty()) {
            return;
        }
        
        SyncEvent event = new SyncEvent("pending-sync", SyncEvent.SyncType.FULL_SYNC);
        event.setTotalItems(pendingSyncs.size());
        notifySyncStarted(event);
        
        int processed = 0;
        for (PendingSyncItem item : pendingSyncs.values()) {
            try {
                syncItem(item);
                pendingSyncs.remove(item.getSyncId());
                processed++;
                notifySyncProgress(event, (processed * 100) / event.getTotalItems());
            } catch (Exception e) {
                log.error("Failed to sync item: {}", item.getSyncId(), e);
                item.incrementRetryCount();
                
                if (item.getRetryCount() >= 3) {
                    log.warn("Max retries reached, removing item: {}", item.getSyncId());
                    pendingSyncs.remove(item.getSyncId());
                }
            }
        }
        
        event.setEndTime(System.currentTimeMillis());
        event.setStatus("completed");
        notifySyncCompleted(event);
    }
    
    private void syncItem(PendingSyncItem item) {
        switch (item.getType()) {
            case UPLOAD:
                vfsClient.writeFile(item.getPath(), item.getData()).join();
                break;
            case DOWNLOAD:
                byte[] data = vfsClient.readFile(item.getPath()).join();
                item.setData(data);
                break;
            case DELETE:
                vfsClient.delete(item.getPath()).join();
                break;
            default:
                log.warn("Unknown sync type: {}", item.getType());
        }
    }
    
    public void syncNow() {
        scheduler.execute(this::syncPendingItems);
    }
    
    public int getPendingCount() {
        return pendingSyncs.size();
    }
    
    public boolean isRunning() {
        return running;
    }
    
    public void addSyncListener(SyncListener listener) {
        syncListeners.add(listener);
    }
    
    public void removeSyncListener(SyncListener listener) {
        syncListeners.remove(listener);
    }
    
    private void notifySyncStarted(SyncEvent event) {
        for (SyncListener listener : syncListeners) {
            try {
                listener.onSyncStarted(event);
            } catch (Exception e) {
                log.warn("SyncListener error", e);
            }
        }
    }
    
    private void notifySyncProgress(SyncEvent event, int progress) {
        for (SyncListener listener : syncListeners) {
            try {
                listener.onSyncProgress(event, progress);
            } catch (Exception e) {
                log.warn("SyncListener error", e);
            }
        }
    }
    
    private void notifySyncCompleted(SyncEvent event) {
        for (SyncListener listener : syncListeners) {
            try {
                listener.onSyncCompleted(event);
            } catch (Exception e) {
                log.warn("SyncListener error", e);
            }
        }
    }
    
    private void notifyPendingAdded(PendingSyncItem item) {
        // 可以扩展通知逻辑
    }
    
    public void shutdown() {
        stopSync();
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
        log.info("VfsSyncService shutdown complete");
    }
    
    public enum SyncType {
        UPLOAD,
        DOWNLOAD,
        DELETE
    }
    
    public static class PendingSyncItem {
        private String syncId;
        private String path;
        private byte[] data;
        private SyncType type;
        private long createdTime;
        private int retryCount;
        private String status;
        
        public String getSyncId() {
            return syncId;
        }
        
        public void setSyncId(String syncId) {
            this.syncId = syncId;
        }
        
        public String getPath() {
            return path;
        }
        
        public void setPath(String path) {
            this.path = path;
        }
        
        public byte[] getData() {
            return data;
        }
        
        public void setData(byte[] data) {
            this.data = data;
        }
        
        public SyncType getType() {
            return type;
        }
        
        public void setType(SyncType type) {
            this.type = type;
        }
        
        public long getCreatedTime() {
            return createdTime;
        }
        
        public void setCreatedTime(long createdTime) {
            this.createdTime = createdTime;
        }
        
        public int getRetryCount() {
            return retryCount;
        }
        
        public void setRetryCount(int retryCount) {
            this.retryCount = retryCount;
        }
        
        public void incrementRetryCount() {
            this.retryCount++;
        }
        
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
    }
}

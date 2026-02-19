package net.ooder.sdk.nexus.offline.impl;

import net.ooder.sdk.nexus.offline.OfflineService;
import net.ooder.sdk.nexus.offline.model.OfflineCapability;
import net.ooder.sdk.nexus.offline.model.PendingSync;
import net.ooder.sdk.nexus.offline.model.SyncResult;
import net.ooder.sdk.nexus.offline.model.NetworkState;
import net.ooder.sdk.nexus.offline.model.NetworkStateListener;
import net.ooder.sdk.nexus.offline.model.SyncStateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class OfflineServiceImpl implements OfflineService {
    
    private static final Logger log = LoggerFactory.getLogger(OfflineServiceImpl.class);
    
    private final AtomicBoolean offlineMode;
    private final AtomicBoolean networkAvailable;
    private final AtomicReference<NetworkState> networkState;
    private final Map<String, PendingSync> pendingSyncs;
    private final List<OfflineCapability> capabilities;
    private final List<NetworkStateListener> networkListeners;
    private final List<SyncStateListener> syncListeners;
    private final ExecutorService executor;
    private final ScheduledExecutorService scheduler;
    private ScheduledFuture<?> networkCheckTask;
    
    public OfflineServiceImpl() {
        this.offlineMode = new AtomicBoolean(false);
        this.networkAvailable = new AtomicBoolean(true);
        this.networkState = new AtomicReference<NetworkState>();
        this.pendingSyncs = new ConcurrentHashMap<String, PendingSync>();
        this.capabilities = new CopyOnWriteArrayList<OfflineCapability>();
        this.networkListeners = new CopyOnWriteArrayList<NetworkStateListener>();
        this.syncListeners = new CopyOnWriteArrayList<SyncStateListener>();
        this.executor = Executors.newCachedThreadPool();
        this.scheduler = Executors.newScheduledThreadPool(1);
        
        NetworkState state = new NetworkState();
        state.setAvailable(true);
        state.setConnectionType("UNKNOWN");
        state.setCheckTime(System.currentTimeMillis());
        networkState.set(state);
        
        log.info("OfflineServiceImpl initialized");
    }
    
    @Override
    public CompletableFuture<Void> enableOfflineMode() {
        return CompletableFuture.runAsync(() -> {
            log.info("Enabling offline mode");
            offlineMode.set(true);
            log.info("Offline mode enabled");
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> disableOfflineMode() {
        return CompletableFuture.runAsync(() -> {
            log.info("Disabling offline mode");
            offlineMode.set(false);
            log.info("Offline mode disabled");
        }, executor);
    }
    
    @Override
    public boolean isOfflineMode() {
        return offlineMode.get();
    }
    
    @Override
    public boolean isNetworkAvailable() {
        return networkAvailable.get();
    }
    
    @Override
    public CompletableFuture<List<OfflineCapability>> getOfflineCapabilities() {
        return CompletableFuture.supplyAsync(() -> {
            List<OfflineCapability> result = new ArrayList<OfflineCapability>();
            
            OfflineCapability skillCap = new OfflineCapability();
            skillCap.setCapabilityId("cap-skill");
            skillCap.setCapabilityType(OfflineCapability.CapabilityType.SKILL.name());
            skillCap.setName("Offline Skills");
            skillCap.setAvailable(true);
            skillCap.setDescription("Skills available for offline execution");
            result.add(skillCap);
            
            OfflineCapability dataCap = new OfflineCapability();
            dataCap.setCapabilityId("cap-data");
            dataCap.setCapabilityType(OfflineCapability.CapabilityType.DATA.name());
            dataCap.setName("Offline Data");
            dataCap.setAvailable(true);
            dataCap.setDescription("Data cached for offline access");
            result.add(dataCap);
            
            OfflineCapability storageCap = new OfflineCapability();
            storageCap.setCapabilityId("cap-storage");
            storageCap.setCapabilityType(OfflineCapability.CapabilityType.STORAGE.name());
            storageCap.setName("Offline Storage");
            storageCap.setAvailable(true);
            storageCap.setDescription("Local storage available offline");
            result.add(storageCap);
            
            return result;
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<PendingSync>> getPendingSyncItems() {
        return CompletableFuture.supplyAsync(() -> new ArrayList<PendingSync>(pendingSyncs.values()), executor);
    }
    
    @Override
    public CompletableFuture<SyncResult> syncNow() {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Starting sync");
            
            String syncId = "sync-" + UUID.randomUUID().toString().substring(0, 8);
            notifySyncStarted(syncId);
            
            SyncResult result = new SyncResult();
            result.setSyncId(syncId);
            result.setStartTime(System.currentTimeMillis());
            
            List<String> syncedItems = new ArrayList<String>();
            List<String> failedItems = new ArrayList<String>();
            
            List<PendingSync> toSync = new ArrayList<PendingSync>(pendingSyncs.values());
            int total = toSync.size();
            int progress = 0;
            
            for (PendingSync sync : toSync) {
                try {
                    Thread.sleep(100);
                    pendingSyncs.remove(sync.getSyncId());
                    syncedItems.add(sync.getSyncId());
                    notifyPendingSyncRemoved(sync.getSyncId());
                    progress++;
                    notifySyncProgress(syncId, (int) (progress * 100.0 / total));
                } catch (Exception e) {
                    log.warn("Failed to sync item: {}", sync.getSyncId(), e);
                    failedItems.add(sync.getSyncId());
                }
            }
            
            result.setSuccess(failedItems.isEmpty());
            result.setSyncedCount(syncedItems.size());
            result.setFailedCount(failedItems.size());
            result.setSyncedItems(syncedItems);
            result.setFailedItems(failedItems);
            result.setEndTime(System.currentTimeMillis());
            
            if (result.isSuccess()) {
                notifySyncCompleted(syncId, result);
                log.info("Sync completed: {} items synced", syncedItems.size());
            } else {
                notifySyncFailed(syncId, "Some items failed to sync");
                log.warn("Sync completed with errors: {} items failed", failedItems.size());
            }
            
            return result;
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> syncItem(String syncId) {
        return CompletableFuture.runAsync(() -> {
            log.info("Syncing item: {}", syncId);
            
            PendingSync sync = pendingSyncs.remove(syncId);
            if (sync != null) {
                notifyPendingSyncRemoved(syncId);
                log.info("Item synced: {}", syncId);
            }
        }, executor);
    }
    
    @Override
    public void addNetworkListener(NetworkStateListener listener) {
        networkListeners.add(listener);
    }
    
    @Override
    public void removeNetworkListener(NetworkStateListener listener) {
        networkListeners.remove(listener);
    }
    
    @Override
    public void addSyncListener(SyncStateListener listener) {
        syncListeners.add(listener);
    }
    
    @Override
    public void removeSyncListener(SyncStateListener listener) {
        syncListeners.remove(listener);
    }
    
    public void startNetworkMonitoring(long intervalMs) {
        networkCheckTask = scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                checkNetworkState();
            }
        }, 0, intervalMs, TimeUnit.MILLISECONDS);
        log.info("Network monitoring started");
    }
    
    public void stopNetworkMonitoring() {
        if (networkCheckTask != null) {
            networkCheckTask.cancel(false);
        }
        log.info("Network monitoring stopped");
    }
    
    private void checkNetworkState() {
        boolean wasAvailable = networkAvailable.get();
        boolean nowAvailable = checkNetworkConnectivity();
        
        networkAvailable.set(nowAvailable);
        
        NetworkState state = new NetworkState();
        state.setAvailable(nowAvailable);
        state.setConnectionType(determineConnectionType(nowAvailable));
        state.setLatency(nowAvailable ? measureNetworkLatency() : -1);
        state.setCheckTime(System.currentTimeMillis());
        networkState.set(state);
        
        if (wasAvailable != nowAvailable) {
            if (nowAvailable) {
                notifyNetworkAvailable();
            } else {
                notifyNetworkLost();
            }
            notifyNetworkStateChanged(state);
        }
    }
    
    private boolean checkNetworkConnectivity() {
        String[] testHosts = {"8.8.8.8", "1.1.1.1", "www.baidu.com"};
        
        for (String host : testHosts) {
            try {
                java.net.InetAddress address;
                if (host.matches("\\d+\\.\\d+\\.\\d+\\.\\d+")) {
                    address = java.net.InetAddress.getByName(host);
                } else {
                    address = java.net.InetAddress.getByName(host);
                }
                
                if (address.isReachable(3000)) {
                    log.debug("Network connectivity confirmed via {}", host);
                    return true;
                }
            } catch (Exception e) {
                log.trace("Network check failed for {}: {}", host, e.getMessage());
            }
        }
        
        return false;
    }
    
    private String determineConnectionType(boolean available) {
        if (!available) {
            return "NONE";
        }
        
        try {
            java.net.NetworkInterface networkInterface = null;
            java.util.Enumeration<java.net.NetworkInterface> interfaces = 
                java.net.NetworkInterface.getNetworkInterfaces();
            
            while (interfaces != null && interfaces.hasMoreElements()) {
                java.net.NetworkInterface ni = interfaces.nextElement();
                if (ni.isUp() && !ni.isLoopback()) {
                    String name = ni.getName().toLowerCase();
                    if (name.contains("wlan") || name.contains("wi") || name.contains("wifi")) {
                        return "WIFI";
                    } else if (name.contains("eth") || name.contains("en")) {
                        return "ETHERNET";
                    } else if (name.contains("mobile") || name.contains("cellular")) {
                        return "MOBILE";
                    }
                    networkInterface = ni;
                }
            }
            
            if (networkInterface != null) {
                return "ETHERNET";
            }
        } catch (Exception e) {
            log.trace("Failed to determine connection type: {}", e.getMessage());
        }
        
        return "UNKNOWN";
    }
    
    private int measureNetworkLatency() {
        String[] testHosts = {"8.8.8.8", "1.1.1.1"};
        int minLatency = Integer.MAX_VALUE;
        
        for (String host : testHosts) {
            try {
                java.net.InetAddress address = java.net.InetAddress.getByName(host);
                long startTime = System.currentTimeMillis();
                
                if (address.isReachable(5000)) {
                    long latency = System.currentTimeMillis() - startTime;
                    minLatency = (int) Math.min(minLatency, latency);
                }
            } catch (Exception e) {
                // Ignore
            }
        }
        
        return minLatency == Integer.MAX_VALUE ? -1 : minLatency;
    }
    
    public void addPendingSync(String resourceKey, String resourceName, PendingSync.SyncType type) {
        PendingSync sync = new PendingSync();
        sync.setSyncId("psync-" + UUID.randomUUID().toString().substring(0, 8));
        sync.setType(type);
        sync.setResourceKey(resourceKey);
        sync.setResourceName(resourceName);
        sync.setCreatedTime(System.currentTimeMillis());
        sync.setRetryCount(0);
        sync.setPriority(PendingSync.SyncPriority.NORMAL);
        sync.setStatus("pending");
        
        pendingSyncs.put(sync.getSyncId(), sync);
        notifyPendingSyncAdded(sync);
        log.debug("Pending sync added: {}", sync.getSyncId());
    }
    
    private void notifyNetworkAvailable() {
        for (NetworkStateListener listener : networkListeners) {
            try {
                listener.onNetworkAvailable();
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyNetworkLost() {
        for (NetworkStateListener listener : networkListeners) {
            try {
                listener.onNetworkLost();
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyNetworkStateChanged(NetworkState state) {
        for (NetworkStateListener listener : networkListeners) {
            try {
                listener.onNetworkStateChanged(state);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifySyncStarted(String syncId) {
        for (SyncStateListener listener : syncListeners) {
            try {
                listener.onSyncStarted(syncId);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifySyncProgress(String syncId, int progress) {
        for (SyncStateListener listener : syncListeners) {
            try {
                listener.onSyncProgress(syncId, progress);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifySyncCompleted(String syncId, SyncResult result) {
        for (SyncStateListener listener : syncListeners) {
            try {
                listener.onSyncCompleted(syncId, result);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifySyncFailed(String syncId, String errorMessage) {
        for (SyncStateListener listener : syncListeners) {
            try {
                listener.onSyncFailed(syncId, errorMessage);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyPendingSyncAdded(PendingSync sync) {
        for (SyncStateListener listener : syncListeners) {
            try {
                listener.onPendingSyncAdded(sync);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyPendingSyncRemoved(String syncId) {
        for (SyncStateListener listener : syncListeners) {
            try {
                listener.onPendingSyncRemoved(syncId);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    public void shutdown() {
        log.info("Shutting down OfflineService");
        stopNetworkMonitoring();
        scheduler.shutdown();
        executor.shutdown();
        pendingSyncs.clear();
        capabilities.clear();
        log.info("OfflineService shutdown complete");
    }
}

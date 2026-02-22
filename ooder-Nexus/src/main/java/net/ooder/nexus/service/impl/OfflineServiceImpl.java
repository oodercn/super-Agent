package net.ooder.nexus.service.impl;

import net.ooder.nexus.service.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class OfflineServiceImpl implements OfflineService {

    private static final Logger log = LoggerFactory.getLogger(OfflineServiceImpl.class);

    private boolean offlineMode = false;
    private final List<NetworkStateListener> networkListeners = new CopyOnWriteArrayList<NetworkStateListener>();
    private final List<SyncStateListener> syncListeners = new CopyOnWriteArrayList<SyncStateListener>();
    private final List<PendingSync> pendingSyncs = new ArrayList<PendingSync>();

    public OfflineServiceImpl() {
        log.info("OfflineServiceImpl initialized with SDK 0.7.2");
    }

    @Override
    public CompletableFuture<Void> enableOfflineMode() {
        log.info("Enabling offline mode");
        
        return CompletableFuture.runAsync(() -> {
            offlineMode = true;
            notifyNetworkDisconnected();
            log.info("Offline mode enabled");
        });
    }

    @Override
    public CompletableFuture<Void> disableOfflineMode() {
        log.info("Disabling offline mode");
        
        return CompletableFuture.runAsync(() -> {
            offlineMode = false;
            notifyNetworkConnected();
            log.info("Offline mode disabled");
        });
    }

    @Override
    public boolean isOfflineMode() {
        return offlineMode;
    }

    @Override
    public CompletableFuture<List<OfflineCapability>> getOfflineCapabilities() {
        log.info("Getting offline capabilities");
        
        return CompletableFuture.supplyAsync(() -> {
            List<OfflineCapability> capabilities = new ArrayList<OfflineCapability>();
            
            OfflineCapability cap1 = new OfflineCapability();
            cap1.setCapabilityId("local-storage");
            cap1.setName("本地存储");
            cap1.setDescription("离线时可以访问本地存储的数据");
            cap1.setAvailable(true);
            capabilities.add(cap1);
            
            OfflineCapability cap2 = new OfflineCapability();
            cap2.setCapabilityId("local-skills");
            cap2.setName("本地技能");
            cap2.setDescription("离线时可以执行已安装的本地技能");
            cap2.setAvailable(true);
            capabilities.add(cap2);
            
            OfflineCapability cap3 = new OfflineCapability();
            cap3.setCapabilityId("local-llm");
            cap3.setName("本地 LLM");
            cap3.setDescription("离线时可以使用本地部署的 LLM");
            cap3.setAvailable(false);
            capabilities.add(cap3);
            
            return capabilities;
        });
    }

    @Override
    public CompletableFuture<List<PendingSync>> getPendingSyncItems() {
        log.info("Getting pending sync items");
        return CompletableFuture.completedFuture(new ArrayList<PendingSync>(pendingSyncs));
    }

    @Override
    public CompletableFuture<SyncResult> syncNow() {
        log.info("Starting sync");
        
        return CompletableFuture.supplyAsync(() -> {
            SyncResult result = new SyncResult();
            int total = pendingSyncs.size();
            
            notifySyncStarted(total);
            
            int success = 0;
            int failed = 0;
            
            for (int i = 0; i < pendingSyncs.size(); i++) {
                PendingSync item = pendingSyncs.get(i);
                notifySyncProgress(i + 1, total);
                
                try {
                    Thread.sleep(100);
                    item.setStatus("synced");
                    success++;
                } catch (Exception e) {
                    item.setStatus("failed");
                    item.setRetryCount(item.getRetryCount() + 1);
                    failed++;
                }
            }
            
            pendingSyncs.clear();
            
            result.setTotalItems(total);
            result.setSuccessCount(success);
            result.setFailureCount(failed);
            result.setSyncTime(System.currentTimeMillis());
            result.setMessage("Sync completed");
            
            notifySyncCompleted(result);
            
            return result;
        });
    }

    @Override
    public void addNetworkListener(NetworkStateListener listener) {
        networkListeners.add(listener);
    }

    @Override
    public void addSyncListener(SyncStateListener listener) {
        syncListeners.add(listener);
    }

    private void notifyNetworkConnected() {
        for (NetworkStateListener listener : networkListeners) {
            try {
                listener.onNetworkConnected();
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }

    private void notifyNetworkDisconnected() {
        for (NetworkStateListener listener : networkListeners) {
            try {
                listener.onNetworkDisconnected();
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }

    private void notifySyncStarted(int totalItems) {
        for (SyncStateListener listener : syncListeners) {
            try {
                listener.onSyncStarted(totalItems);
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }

    private void notifySyncProgress(int current, int total) {
        for (SyncStateListener listener : syncListeners) {
            try {
                listener.onSyncProgress(current, total);
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }

    private void notifySyncCompleted(SyncResult result) {
        for (SyncStateListener listener : syncListeners) {
            try {
                listener.onSyncCompleted(result);
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }
}

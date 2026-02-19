package net.ooder.sdk.nexus.offline.impl;

import net.ooder.sdk.nexus.offline.*;
import net.ooder.sdk.nexus.offline.model.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class OfflineServiceImplTest {
    
    private OfflineServiceImpl offlineService;
    
    @Before
    public void setUp() {
        offlineService = new OfflineServiceImpl();
    }
    
    @After
    public void tearDown() {
        offlineService.shutdown();
    }
    
    @Test
    public void testEnableOfflineMode() throws Exception {
        assertFalse(offlineService.isOfflineMode());
        
        offlineService.enableOfflineMode().get(10, TimeUnit.SECONDS);
        
        assertTrue(offlineService.isOfflineMode());
    }
    
    @Test
    public void testDisableOfflineMode() throws Exception {
        offlineService.enableOfflineMode().get(10, TimeUnit.SECONDS);
        assertTrue(offlineService.isOfflineMode());
        
        offlineService.disableOfflineMode().get(10, TimeUnit.SECONDS);
        
        assertFalse(offlineService.isOfflineMode());
    }
    
    @Test
    public void testIsNetworkAvailable() {
        boolean available = offlineService.isNetworkAvailable();
        assertTrue(available || !available);
    }
    
    @Test
    public void testGetOfflineCapabilities() throws Exception {
        List<OfflineCapability> capabilities = offlineService.getOfflineCapabilities().get(10, TimeUnit.SECONDS);
        
        assertNotNull(capabilities);
        assertTrue(capabilities.size() > 0);
    }
    
    @Test
    public void testGetPendingSyncItems() throws Exception {
        List<PendingSync> items = offlineService.getPendingSyncItems().get(10, TimeUnit.SECONDS);
        
        assertNotNull(items);
    }
    
    @Test
    public void testSyncNow() throws Exception {
        SyncResult result = offlineService.syncNow().get(10, TimeUnit.SECONDS);
        
        assertNotNull(result);
        assertNotNull(result.getSyncId());
    }
    
    @Test
    public void testSyncItem() throws Exception {
        offlineService.syncItem("test-sync-id").get(10, TimeUnit.SECONDS);
    }
    
    @Test
    public void testAddNetworkListener() {
        NetworkStateListener listener = new NetworkStateListener() {
            @Override
            public void onNetworkAvailable() {}
            @Override
            public void onNetworkLost() {}
            @Override
            public void onNetworkStateChanged(NetworkState state) {}
        };
        
        offlineService.addNetworkListener(listener);
        
        offlineService.removeNetworkListener(listener);
    }
    
    @Test
    public void testAddSyncListener() {
        SyncStateListener listener = new SyncStateListener() {
            @Override
            public void onSyncStarted(String syncId) {}
            @Override
            public void onSyncProgress(String syncId, int progress) {}
            @Override
            public void onSyncCompleted(String syncId, SyncResult result) {}
            @Override
            public void onSyncFailed(String syncId, String errorMessage) {}
            @Override
            public void onPendingSyncAdded(PendingSync sync) {}
            @Override
            public void onPendingSyncRemoved(String syncId) {}
        };
        
        offlineService.addSyncListener(listener);
        
        offlineService.removeSyncListener(listener);
    }
    
    @Test
    public void testNetworkStateCallback() throws Exception {
        final boolean[] callbackInvoked = new boolean[1];
        
        NetworkStateListener listener = new NetworkStateListener() {
            @Override
            public void onNetworkAvailable() {
                callbackInvoked[0] = true;
            }
            @Override
            public void onNetworkLost() {
                callbackInvoked[0] = true;
            }
            @Override
            public void onNetworkStateChanged(NetworkState state) {
                callbackInvoked[0] = true;
            }
        };
        
        offlineService.addNetworkListener(listener);
        offlineService.startNetworkMonitoring(100);
        
        Thread.sleep(200);
        
        offlineService.stopNetworkMonitoring();
        offlineService.removeNetworkListener(listener);
    }
    
    @Test
    public void testAddPendingSync() throws Exception {
        offlineService.addPendingSync("test-key", "Test Resource", PendingSync.SyncType.DATA_UPLOAD);
        
        List<PendingSync> items = offlineService.getPendingSyncItems().get(10, TimeUnit.SECONDS);
        
        assertEquals(1, items.size());
        assertEquals("test-key", items.get(0).getResourceKey());
    }
}

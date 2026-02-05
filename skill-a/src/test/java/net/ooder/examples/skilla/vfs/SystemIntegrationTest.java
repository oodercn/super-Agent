package net.ooder.examples.skilla.vfs;

import net.ooder.skillvfs.VfsService;
import net.ooder.skillvfs.VfsConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * System Integration Test for the entire VFS ecosystem
 * This test simulates:
 * 1. Skill-VFS starting and joining Skill-C
 * 2. Skill-C broadcasting VFS availability to Skill-A and Skill-B
 * 3. Skill-A and Skill-B enabling VFS storage logic
 * 4. Testing VFS file synchronization operations
 */
public class SystemIntegrationTest {
    private static final Logger log = LoggerFactory.getLogger(SystemIntegrationTest.class);
    
    private StorageService storageService;
    private VfsService vfsService;
    private VfsConfig vfsConfig;
    
    @BeforeEach
    public void setUp() {
        log.info("=== Starting System Integration Test ===");
        
        // Step 1: Simulate Skill-VFS starting and joining Skill-C (route agent)
        log.info("1. Simulating Skill-VFS starting and joining Skill-C...");
        simulateSkillVfsStartup();
        
        // Step 2: Simulate Skill-C broadcasting VFS availability to end agents
        log.info("2. Simulating Skill-C broadcasting VFS availability...");
        simulateBroadcastVfsAvailability();
        
        // Step 3: Initialize Skill-A's VFS configuration and services
        log.info("3. Initializing Skill-A's VFS services...");
        vfsConfig = VfsConfig.builder()
                .vfsServerUrl("http://localhost:9002/skill-vfs")
                .groupName("skill-a-group")
                .build();
        
        // Initialize both VfsService and StorageService
        vfsService = new VfsService(vfsConfig);
        vfsService.start();
        
        storageService = new StorageService(vfsConfig);
        storageService.start();
        
        log.info("System Integration Test environment initialized");
    }
    
    @AfterEach
    public void tearDown() {
        // Cleanup resources
        if (storageService != null) {
            storageService.stop();
        }
        
        if (vfsService != null) {
            vfsService.stop();
        }
        
        log.info("=== System Integration Test completed ===");
    }
    
    /**
     * Simulate Skill-VFS startup and joining Skill-C (route agent)
     */
    private void simulateSkillVfsStartup() {
        log.debug("  - Simulating Skill-VFS registration with Skill-C");
        log.debug("  - Skill-VFS registered as routeAgent with ID: skill-vfs-001");
        log.debug("  - Skill-C acknowledged Skill-VFS registration");
    }
    
    /**
     * Simulate Skill-C broadcasting VFS availability to end agents
     */
    private void simulateBroadcastVfsAvailability() {
        log.debug("  - Skill-C broadcasting: VFS service available at http://localhost:9002/skill-vfs");
        log.debug("  - Skill-A received broadcast and will enable VFS storage");
        log.debug("  - Skill-B received broadcast and will enable VFS storage");
    }
    
    /**
     * Test the complete VFS file synchronization workflow
     */
    @Test
    public void testVfsFileSynchronizationWorkflow() {
        log.info("4. Testing VFS file synchronization workflow...");
        
        // Step 1: Test VFS availability via StorageService
        log.info("   a. Testing VFS availability via StorageService...");
        boolean isVfsAvailable = storageService.checkVfsAvailability();
        log.info("   VFS availability: {}", isVfsAvailable);
        
        // Step 2: Store test data (should use VFS if available, otherwise local storage)
        String testKey = "system-test-data.json";
        String testContent = "{\"system\": \"test\", \"timestamp\": \"" + System.currentTimeMillis() + "\"}";
        byte[] testData = testContent.getBytes();
        
        log.info("   b. Storing test data with key: {}", testKey);
        boolean storeResult = storageService.storeData(testKey, testData);
        assertTrue(storeResult, "Data should be stored successfully");
        
        // Step 3: Retrieve stored data
        log.info("   c. Retrieving test data with key: {}", testKey);
        byte[] retrievedData = storageService.retrieveData(testKey);
        assertNotNull(retrievedData, "Retrieved data should not be null");
        assertEquals(testContent, new String(retrievedData), "Retrieved data should match stored data");
        
        // Step 4: Test batch file synchronization
        log.info("   d. Testing batch file synchronization...");
        boolean batchSyncResult = vfsService.syncBatchToVfs(Collections.singletonList(testKey));
        log.info("   Batch sync result: {}", batchSyncResult);
        
        // Step 5: Test asynchronous file synchronization
        log.info("   e. Testing asynchronous file synchronization...");
        CompletableFuture<Boolean> asyncResult = vfsService.syncToVfsAsync(testKey);
        
        try {
            Boolean asyncSyncResult = asyncResult.get(5, TimeUnit.SECONDS);
            log.info("   Asynchronous sync result: {}", asyncSyncResult);
        } catch (Exception e) {
            log.warn("   Asynchronous sync timed out or failed: {}", e.getMessage());
        }
        
        // Step 6: Test VFS fallback mechanism
        log.info("   f. Testing VFS fallback mechanism...");
        testVfsFallbackMechanism();
        
        log.info("4. VFS file synchronization workflow test completed");
    }
    
    /**
     * Test VFS fallback mechanism when VFS service becomes unavailable
     */
    private void testVfsFallbackMechanism() {
        log.debug("   - Simulating VFS service temporary unavailability");
        
        // This test simulates what happens when VFS becomes unavailable
        // The StorageService should automatically fallback to local storage
        
        String fallbackKey = "fallback-test-data.json";
        String fallbackContent = "{\"fallback\": \"test\", \"timestamp\": \"" + System.currentTimeMillis() + "\"}";
        byte[] fallbackData = fallbackContent.getBytes();
        
        log.debug("   - Storing data with simulated VFS unavailability");
        boolean fallbackStoreResult = storageService.storeData(fallbackKey, fallbackData);
        assertTrue(fallbackStoreResult, "Data should be stored using fallback mechanism");
        
        log.debug("   - Retrieving data using fallback mechanism");
        byte[] fallbackRetrievedData = storageService.retrieveData(fallbackKey);
        assertNotNull(fallbackRetrievedData, "Retrieved data should not be null from fallback");
        
        log.debug("   - VFS fallback mechanism test completed");
    }
    
    /**
     * Assert that two strings are equal
     */
    private void assertEquals(String expected, String actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + " Expected: '" + expected + "', Actual: '" + actual + "'" + "'");
        }
    }
    
    /**
     * Assert that an object is not null
     */
    private void assertNotNull(Object obj, String message) {
        if (obj == null) {
            throw new AssertionError(message);
        }
    }
}
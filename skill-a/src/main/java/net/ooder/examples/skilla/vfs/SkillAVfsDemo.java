package net.ooder.examples.skilla.vfs;

import net.ooder.skillvfs.VfsConfig;
import net.ooder.skillvfs.VfsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

/**
 * Demonstration class showing how Skill A can interact with Kill VFS
 * This class provides a simple example of storage operations using VFS
 */
public class SkillAVfsDemo {
    private static final Logger log = LoggerFactory.getLogger(SkillAVfsDemo.class);
    
    public static void main(String[] args) {
        log.info("Starting Skill A VFS Integration Demonstration");
        
        // Create and configure the VFS service
        VfsConfig vfsConfig = VfsConfig.builder()
                .vfsServerUrl("http://localhost:8080/vfs")
                .groupName("skill-a-group")
                .vfsConnectionTimeout(5000)
                .vfsRetryCount(3)
                .build();
        
        // Initialize the VFS service
        VfsService vfsService = new VfsService(vfsConfig);
        vfsService.start();
        
        try {
            // Demonstrate various VFS operations
            demonstrateVfsOperations(vfsService);
            
        } finally {
            // Cleanup resources
            vfsService.stop();
            log.info("Skill A VFS Integration Demonstration Completed");
        }
    }
    
    private static void demonstrateVfsOperations(VfsService vfsService) {
        log.info("\n=== Demonstrating VFS Operations ===");
        
        // 1. Check VFS availability
        log.info("1. Checking VFS availability...");
        boolean isAvailable = vfsService.checkVfsAvailability();
        log.info("   VFS available: {}", isAvailable);
        
        // 2. Single file sync to VFS
        log.info("\n2. Syncing single file to VFS...");
        String fileKey = "skill-a-data-20260119.json";
        boolean syncResult = vfsService.syncToVfs(fileKey);
        log.info("   Single file sync result: {}", syncResult);
        
        // 3. Batch file sync to VFS
        log.info("\n3. Syncing batch files to VFS...");
        java.util.List<String> fileKeys = Collections.singletonList("skill-a-batch-data.json");
        boolean batchSyncResult = vfsService.syncBatchToVfs(fileKeys);
        log.info("   Batch file sync result: {}", batchSyncResult);
        
        // 4. Get files that need syncing to VFS
        log.info("\n4. Getting files that need sync to VFS...");
        java.util.List<String> diffFilesToVfs = vfsService.getDiffFilesToVfs();
        log.info("   Files needing sync to VFS: {}", diffFilesToVfs);
        
        // 5. Get files that need syncing from VFS
        log.info("\n5. Getting files that need sync from VFS...");
        java.util.List<String> diffFilesFromVfs = vfsService.getDiffFilesFromVfs();
        log.info("   Files needing sync from VFS: {}", diffFilesFromVfs);
        
        // 6. Async file sync
        log.info("\n6. Performing async file sync...");
        CompletableFuture<Boolean> asyncResult = vfsService.syncToVfsAsync("skill-a-async-data.json");
        asyncResult.thenAccept(result -> {
            log.info("   Async file sync completed with result: {}", result);
        });
        
        // Wait for async operation to complete
        try {
            asyncResult.get();
        } catch (Exception e) {
            log.error("   Async file sync failed: {}", e.getMessage());
        }
        
        log.info("\n=== VFS Operations Demonstration Completed ===");
    }
}

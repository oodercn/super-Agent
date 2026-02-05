package net.ooder.examples.skillb.vfs;

import net.ooder.skillvfs.VfsConfig;
import net.ooder.skillvfs.VfsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Storage service that implements automatic switching between VFS and local storage
 * This service provides a unified storage interface that transparently uses VFS when available
 * and falls back to local storage when VFS is unavailable
 */
public class StorageService {
    private static final Logger log = LoggerFactory.getLogger(StorageService.class);
    private static final String LOCAL_STORAGE_DIR = "./skill-b-storage";
    private static final int VFS_CHECK_INTERVAL = 30000; // 30 seconds
    
    private final VfsService vfsService;
    private boolean vfsAvailable = false;
    private final ReentrantLock vfsCheckLock = new ReentrantLock();
    private long lastVfsCheckTime = 0;
    
    /**
     * Create a new StorageService with VFS configuration
     * @param vfsConfig The VFS configuration
     */
    public StorageService(VfsConfig vfsConfig) {
        this.vfsService = new VfsService(vfsConfig);
        initializeLocalStorage();
    }
    
    /**
     * Start the storage service and initialize VFS connection
     */
    public void start() {
        log.info("Starting Storage Service...");
        vfsService.start();
        checkVfsAvailability();
        log.info("Storage Service started successfully");
    }
    
    /**
     * Stop the storage service and clean up resources
     */
    public void stop() {
        log.info("Stopping Storage Service...");
        vfsService.stop();
        log.info("Storage Service stopped successfully");
    }
    
    /**
     * Initialize local storage directory if it doesn't exist
     */
    private void initializeLocalStorage() {
        File storageDir = new File(LOCAL_STORAGE_DIR);
        if (!storageDir.exists()) {
            if (storageDir.mkdirs()) {
                log.info("Created local storage directory: {}", LOCAL_STORAGE_DIR);
            } else {
                log.error("Failed to create local storage directory: {}", LOCAL_STORAGE_DIR);
            }
        } else {
            log.info("Using existing local storage directory: {}", LOCAL_STORAGE_DIR);
        }
    }

    /**
     * Check if VFS is available, with caching to avoid too frequent checks
     * @return true if VFS is available, false otherwise
     */
    public boolean checkVfsAvailability() {
        long currentTime = System.currentTimeMillis();
        
        // Only check VFS availability if the last check was more than VFS_CHECK_INTERVAL ago
        if (currentTime - lastVfsCheckTime > VFS_CHECK_INTERVAL) {
            vfsCheckLock.lock();
            try {
                // Double-check after acquiring lock
                if (currentTime - lastVfsCheckTime > VFS_CHECK_INTERVAL) {
                    boolean previousState = vfsAvailable;
                    vfsAvailable = vfsService.checkVfsAvailability();
                    lastVfsCheckTime = currentTime;
                    
                    if (vfsAvailable != previousState) {
                        log.info("VFS availability changed: {}", vfsAvailable ? "AVAILABLE" : "UNAVAILABLE");
                        if (vfsAvailable) {
                            // When VFS becomes available, sync any local changes to VFS
                            syncAllToVfs();
                        }
                    }
                }
            } finally {
                vfsCheckLock.unlock();
            }
        }
        
        return vfsAvailable;
    }
    
    /**
     * Store data to the appropriate storage (VFS if available, local otherwise)
     * @param key The key/name for the data
     * @param data The data to store
     * @return true if storage was successful, false otherwise
     */
    public boolean storeData(String key, byte[] data) {
        log.debug("Storing data with key: {}", key);
        
        boolean result = false;
        
        try {
            if (checkVfsAvailability()) {
                // Store to VFS if available
                log.debug("Using VFS for data storage: {}", key);
                // First save locally, then sync to VFS
                saveLocally(key, data);
                result = vfsService.syncToVfs(key);
            } else {
                // Fall back to local storage
                log.debug("Using local storage for data: {}", key);
                result = saveLocally(key, data);
            }
        } catch (Exception e) {
            log.error("Failed to store data with key {}: {}", key, e.getMessage(), e);
        }
        
        return result;
    }
    
    /**
     * Save data to local storage
     * @param key The key/name for the data
     * @param data The data to store
     * @return true if storage was successful, false otherwise
     */
    private boolean saveLocally(String key, byte[] data) {
        try {
            Path filePath = Paths.get(LOCAL_STORAGE_DIR, key);
            Files.write(filePath, data);
            log.debug("Successfully saved data locally: {}", key);
            return true;
        } catch (IOException e) {
            log.error("Failed to save data locally with key {}: {}", key, e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Retrieve data from the appropriate storage (VFS if available, local otherwise)
     * @param key The key/name for the data
     * @return The retrieved data, or null if retrieval failed
     */
    public byte[] retrieveData(String key) {
        log.debug("Retrieving data with key: {}", key);
        
        byte[] data = null;
        
        try {
            if (checkVfsAvailability()) {
                // Try to retrieve from VFS first
                log.debug("Using VFS for data retrieval: {}", key);
                // In a real implementation, we would have a method to retrieve from VFS
                // For this demo, we'll just get it from local storage after ensuring it's synced
                if (vfsService.syncFromVfs(key)) {
                    data = loadLocally(key);
                } else {
                    // Fall back to local storage if sync fails
                    data = loadLocally(key);
                }
            } else {
                // Fall back to local storage
                log.debug("Using local storage for data retrieval: {}", key);
                data = loadLocally(key);
            }
        } catch (Exception e) {
            log.error("Failed to retrieve data with key {}: {}", key, e.getMessage(), e);
        }
        
        return data;
    }
    
    /**
     * Load data from local storage
     * @param key The key/name for the data
     * @return The loaded data, or null if loading failed
     */
    private byte[] loadLocally(String key) {
        try {
            Path filePath = Paths.get(LOCAL_STORAGE_DIR, key);
            if (Files.exists(filePath)) {
                byte[] data = Files.readAllBytes(filePath);
                log.debug("Successfully loaded data from local storage: {}", key);
                return data;
            } else {
                log.debug("No local data found for key: {}", key);
                return null;
            }
        } catch (IOException e) {
            log.error("Failed to load data from local storage with key {}: {}", key, e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Delete data from both storage systems if they exist
     * @param key The key/name for the data
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteData(String key) {
        log.debug("Deleting data with key: {}", key);
        
        boolean result = false;
        
        try {
            // Delete from local storage first
            boolean localDeleted = deleteLocally(key);
            
            // Delete from VFS if available
            boolean vfsDeleted = true;
            if (checkVfsAvailability()) {
                log.debug("Deleting data from VFS: {}", key);
                // In a real implementation, we would have a method to delete from VFS
                // For this demo, we'll just mark it as needing sync
                vfsService.syncToVfs(key);
            }
            
            result = localDeleted && vfsDeleted;
        } catch (Exception e) {
            log.error("Failed to delete data with key {}: {}", key, e.getMessage(), e);
        }
        
        return result;
    }
    
    /**
     * Delete data from local storage
     * @param key The key/name for the data
     * @return true if deletion was successful, false otherwise
     */
    private boolean deleteLocally(String key) {
        try {
            Path filePath = Paths.get(LOCAL_STORAGE_DIR, key);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.debug("Successfully deleted data from local storage: {}", key);
                return true;
            } else {
                log.debug("No local data found to delete for key: {}", key);
                return true; // Consider it successful if the file doesn't exist
            }
        } catch (IOException e) {
            log.error("Failed to delete data from local storage with key {}: {}", key, e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * List all keys in the storage system
     * @return List of keys
     */
    public List<String> listKeys() {
        List<String> keys = new ArrayList<>();
        
        try {
            // Get all keys from local storage
            File storageDir = new File(LOCAL_STORAGE_DIR);
            File[] files = storageDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        keys.add(file.getName());
                    }
                }
            }
            
            // If VFS is available, we could merge keys from VFS here
            if (checkVfsAvailability()) {
                // In a real implementation, we would get keys from VFS and merge them
                log.debug("VFS available but not yet implemented listing from VFS");
            }
            
        } catch (Exception e) {
            log.error("Failed to list keys: {}", e.getMessage(), e);
        }
        
        return keys;
    }
    
    /**
     * Sync all local changes to VFS when it becomes available
     */
    public void syncAllToVfs() {
        log.info("Syncing local changes to VFS...");
        
        try {
            List<String> localFiles = listKeys();
            if (!localFiles.isEmpty()) {
                log.info("Found {} local files to sync to VFS", localFiles.size());
                
                // Perform folder sync comparison and sync files
                List<String> filesToSync = getDiffFilesToVfs(localFiles);
                if (!filesToSync.isEmpty()) {
                    log.info("Found {} files that need to be synced to VFS", filesToSync.size());
                    boolean result = vfsService.syncBatchToVfs(filesToSync);
                    log.info("Batch sync to VFS completed: {}", result);
                } else {
                    log.info("All local files are already synced to VFS");
                }
            } else {
                log.info("No local files to sync to VFS");
            }
        } catch (Exception e) {
            log.error("Failed to sync local changes to VFS: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Sync all changes from VFS to local storage
     */
    public void syncAllFromVfs() {
        log.info("Syncing changes from VFS to local storage...");
        
        try {
            // Get list of files from VFS
            List<String> vfsFiles = getVfsFileList();
            if (!vfsFiles.isEmpty()) {
                log.info("Found {} files in VFS", vfsFiles.size());
                
                // Perform folder sync comparison and sync files
                List<String> filesToDownload = getDiffFilesFromVfs(vfsFiles);
                if (!filesToDownload.isEmpty()) {
                    log.info("Found {} files that need to be downloaded from VFS", filesToDownload.size());
                    boolean result = vfsService.syncBatchFromVfs(filesToDownload);
                    log.info("Batch sync from VFS completed: {}", result);
                } else {
                    log.info("All VFS files are already synced to local storage");
                }
            } else {
                log.info("No files found in VFS");
            }
        } catch (Exception e) {
            log.error("Failed to sync changes from VFS: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Get list of files from VFS
     */
    private List<String> getVfsFileList() {
        // In a real implementation, we would get the list of files from VFS
        // For now, we'll return an empty list as a placeholder
        return vfsService.getDiffFilesToVfs();
    }
    
    /**
     * Get diff files that need to be synced to VFS
     */
    private List<String> getDiffFilesToVfs(List<String> localFiles) {
        // In a real implementation, we would compare local files with VFS files
        // For now, we'll return all local files as a placeholder
        return localFiles;
    }
    
    /**
     * Get diff files that need to be downloaded from VFS
     */
    private List<String> getDiffFilesFromVfs(List<String> vfsFiles) {
        // In a real implementation, we would compare VFS files with local files
        // For now, we'll return all VFS files as a placeholder
        return vfsFiles;
    }
    
    /**
     * Switch JSON storage implementation to use VFS or local storage
     */
    public void switchJsonStorageImplementation(boolean useVfs) {
        log.info("Switching JSON storage implementation to {}", useVfs ? "VFS" : "Local storage");
        
        // In a real implementation, we would switch the JSON storage implementation
        // For now, we'll just update the vfsAvailable flag
        this.vfsAvailable = useVfs;
    }
}
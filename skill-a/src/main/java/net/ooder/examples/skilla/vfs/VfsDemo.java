 package net.ooder.examples.skilla.vfs;

import net.ooder.skillvfs.VfsConfig;

/**
 * Demonstration program showing how Skill A uses VFS service for storage and synchronization
 */
public class VfsDemo {
    public static void main(String[] args) {
        // Parse command line arguments or use default values
        String vfsServerUrl = "http://localhost:8080/vfs";
        String groupName = "skill-a-group";
        
        // Check if command line arguments are provided
        if (args.length >= 1) {
            vfsServerUrl = args[0];
        }
        if (args.length >= 2) {
            groupName = args[1];
        }
        
        System.out.println("Using VFS Configuration:");
        System.out.println("  VFS Server URL: " + vfsServerUrl);
        System.out.println("  Group Name: " + groupName);
        
        // Create VFS configuration
        VfsConfig vfsConfig = VfsConfig.builder()
                .vfsServerUrl(vfsServerUrl)
                .groupName(groupName)
                .build();
        
        // Initialize storage service
        StorageService storageService = new StorageService(vfsConfig);
        storageService.start();
        
        try {
            // Test storing data
            String key1 = "test-data-1.json";
            String key2 = "test-data-2.txt";
            
            byte[] data1 = "{\"name\":\"Skill A\",\"version\":\"1.0.0\",\"status\":\"active\"}".getBytes();
            byte[] data2 = "This is a sample text file for VFS synchronization test".getBytes();
            
            System.out.println("Storing data to VFS...");
            boolean result1 = storageService.storeData(key1, data1);
            boolean result2 = storageService.storeData(key2, data2);
            
            System.out.println("Data storage results:");
            System.out.println("  " + key1 + ": " + (result1 ? "SUCCESS" : "FAILED"));
            System.out.println("  " + key2 + ": " + (result2 ? "SUCCESS" : "FAILED"));
            
            // Test retrieving data
            System.out.println("\nRetrieving data from VFS...");
            byte[] retrievedData1 = storageService.retrieveData(key1);
            byte[] retrievedData2 = storageService.retrieveData(key2);
            
            if (retrievedData1 != null) {
                System.out.println("Retrieved data for " + key1 + ":");
                System.out.println("  " + new String(retrievedData1));
            } else {
                System.out.println("Failed to retrieve data for " + key1);
            }
            
            if (retrievedData2 != null) {
                System.out.println("Retrieved data for " + key2 + ":");
                System.out.println("  " + new String(retrievedData2));
            } else {
                System.out.println("Failed to retrieve data for " + key2);
            }
            
            // Test listing keys
            System.out.println("\nListing all keys:");
            storageService.listKeys().forEach(key -> System.out.println("  " + key));
            
            // Test synchronization
            System.out.println("\nPerforming VFS synchronization...");
            storageService.syncAllToVfs();
            storageService.syncAllFromVfs();
            
            // Test switching JSON storage implementation
            System.out.println("\nSwitching JSON storage implementation to VFS...");
            storageService.switchJsonStorageImplementation(true);
            
            System.out.println("\nVFS Demo completed successfully!");
            
        } catch (Exception e) {
            System.err.println("Error during VFS Demo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            storageService.stop();
        }
    }
}
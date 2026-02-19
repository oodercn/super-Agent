package net.ooder.sdk.api.scene.store;

public interface DualStorage {
    
    void save(String path, byte[] data) throws StorageException;
    
    byte[] load(String path) throws StorageException;
    
    void delete(String path) throws StorageException;
    
    boolean exists(String path);
    
    void sync() throws StorageException;
    
    StorageStatus getStatus();
    
    void addSyncListener(SyncListener listener);
    
    void removeSyncListener(SyncListener listener);
}

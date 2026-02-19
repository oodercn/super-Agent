package net.ooder.sdk.api.scene.store;

public class StorageException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    private final String path;
    private final StorageOperation operation;
    
    public StorageException(String message) {
        super(message);
        this.path = null;
        this.operation = null;
    }
    
    public StorageException(String message, Throwable cause) {
        super(message, cause);
        this.path = null;
        this.operation = null;
    }
    
    public StorageException(String message, String path, StorageOperation operation) {
        super(message);
        this.path = path;
        this.operation = operation;
    }
    
    public StorageException(String message, String path, StorageOperation operation, Throwable cause) {
        super(message, cause);
        this.path = path;
        this.operation = operation;
    }
    
    public String getPath() {
        return path;
    }
    
    public StorageOperation getOperation() {
        return operation;
    }
    
    public enum StorageOperation {
        SAVE,
        LOAD,
        DELETE,
        SYNC,
        EXISTS
    }
}

package net.ooder.sdk.api.scene.store;

import java.util.Map;

public class ConflictInfo {
    
    private String path;
    private ConflictType type;
    private Map<String, Object> localData;
    private Map<String, Object> remoteData;
    private long localUpdateTime;
    private long remoteUpdateTime;
    private String resolution;
    
    public ConflictInfo() {
    }
    
    public ConflictInfo(String path, ConflictType type) {
        this.path = path;
        this.type = type;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public ConflictType getType() {
        return type;
    }
    
    public void setType(ConflictType type) {
        this.type = type;
    }
    
    public Map<String, Object> getLocalData() {
        return localData;
    }
    
    public void setLocalData(Map<String, Object> localData) {
        this.localData = localData;
    }
    
    public Map<String, Object> getRemoteData() {
        return remoteData;
    }
    
    public void setRemoteData(Map<String, Object> remoteData) {
        this.remoteData = remoteData;
    }
    
    public long getLocalUpdateTime() {
        return localUpdateTime;
    }
    
    public void setLocalUpdateTime(long localUpdateTime) {
        this.localUpdateTime = localUpdateTime;
    }
    
    public long getRemoteUpdateTime() {
        return remoteUpdateTime;
    }
    
    public void setRemoteUpdateTime(long remoteUpdateTime) {
        this.remoteUpdateTime = remoteUpdateTime;
    }
    
    public String getResolution() {
        return resolution;
    }
    
    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
    
    public boolean isLocalNewer() {
        return localUpdateTime > remoteUpdateTime;
    }
    
    public boolean isRemoteNewer() {
        return remoteUpdateTime > localUpdateTime;
    }
    
    public enum ConflictType {
        DATA_MISMATCH,
        DELETE_UPDATE,
        UPDATE_DELETE,
        BOTH_DELETED,
        BOTH_MODIFIED
    }
}

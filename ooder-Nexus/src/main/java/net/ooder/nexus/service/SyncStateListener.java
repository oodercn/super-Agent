package net.ooder.nexus.service;

/**
 * 同步状态监听器
 */
public interface SyncStateListener {
    
    /**
     * 同步开始事件
     */
    void onSyncStarted(int totalItems);
    
    /**
     * 同步进度事件
     */
    void onSyncProgress(int currentItem, int totalItems);
    
    /**
     * 同步完成事件
     */
    void onSyncCompleted(SyncResult result);
    
    /**
     * 同步错误事件
     */
    void onSyncError(String error);
}

package net.ooder.skillcenter.storage;

/**
 * 存储服务状态枚举
 */
public enum StorageStatus {
    
    /**
     * 未初始化
     */
    UNINITIALIZED,
    
    /**
     * 初始化中
     */
    INITIALIZING,
    
    /**
     * 正常运行
     */
    RUNNING,
    
    /**
     * 出错
     */
    ERROR,
    
    /**
     * 已关闭
     */
    CLOSED
}

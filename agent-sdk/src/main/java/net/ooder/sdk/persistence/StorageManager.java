package net.ooder.sdk.persistence;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface StorageManager {
    // 初始化存储
    CompletableFuture<Boolean> initialize();
    
    // 关闭存储
    CompletableFuture<Boolean> shutdown();
    
    // 存储操作 - 通用方法
    <T> CompletableFuture<Boolean> save(String key, T value);
    <T> CompletableFuture<T> load(String key, Class<T> clazz);
    CompletableFuture<Boolean> delete(String key);
    CompletableFuture<Boolean> exists(String key);
    CompletableFuture<Long> size();
    CompletableFuture<Boolean> clear();
    
    // 存储操作 - 批量方法
    <T> CompletableFuture<Boolean> saveAll(Map<String, T> entries);
    <T> CompletableFuture<Map<String, T>> loadAll(Class<T> clazz);
    CompletableFuture<Boolean> deleteAll(Iterable<String> keys);
    
    // 备份和恢复
    CompletableFuture<Boolean> backup(String backupPath);
    CompletableFuture<Boolean> restore(String backupPath);
    CompletableFuture<Boolean> scheduleBackup(long period, java.util.concurrent.TimeUnit unit);
    CompletableFuture<Boolean> cancelBackupSchedule();
    
    // 存储管理
    CompletableFuture<Map<String, Object>> getStorageInfo();
    CompletableFuture<Boolean> compact();
    CompletableFuture<Boolean> validate();
    CompletableFuture<Boolean> repair();
    
    // 事务支持
    CompletableFuture<Boolean> beginTransaction();
    CompletableFuture<Boolean> commitTransaction();
    CompletableFuture<Boolean> rollbackTransaction();
    boolean isInTransaction();
}

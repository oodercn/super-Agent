package net.ooder.sdk.storage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 统一存储服务接口，提供基本的存储操作功能
 */
public interface StorageService {
    // 单条数据操作
    <T> boolean save(String key, T data);
    <T> T load(String key, Class<T> clazz);
    boolean delete(String key);
    boolean exists(String key);
    
    // 批量操作
    <T> boolean saveBatch(Map<String, T> dataMap);
    <T> Map<String, T> loadBatch(List<String> keys, Class<T> clazz);
    boolean deleteBatch(List<String> keys);
    
    // 异步操作
    <T> CompletableFuture<Boolean> saveAsync(String key, T data);
    <T> CompletableFuture<T> loadAsync(String key, Class<T> clazz);
    
    // 事务操作
    Transaction beginTransaction();
    
    /**
     * 事务接口，提供事务内的存储操作
     */
    interface Transaction {
        <T> boolean save(String key, T data);
        <T> T load(String key, Class<T> clazz);
        boolean delete(String key);
        boolean commit();
        boolean rollback();
    }
}
package net.ooder.sdk.api.storage;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Storage Service Interface
 *
 * <p>Provides type-safe JSON storage capabilities with async support.</p>
 *
 * <h3>Features:</h3>
 * <ul>
 *   <li>Type-safe data access with generics</li>
 *   <li>TypeReference support for complex types</li>
 *   <li>Async API for non-blocking operations</li>
 *   <li>Batch operations for performance</li>
 *   <li>Prefix-based key listing</li>
 * </ul>
 *
 * <h3>Usage Example:</h3>
 * <pre>
 * // Simple save and load
 * storage.save("user:001", user);
 * Optional&lt;User&gt; user = storage.load("user:001", User.class);
 *
 * // Complex type with TypeReference
 * TypeReference&lt;List&lt;User&gt;&gt; typeRef = new TypeReference&lt;List&lt;User&gt;&gt;() {};
 * Optional&lt;List&lt;User&gt;&gt; users = storage.load("users:all", typeRef);
 *
 * // Async operations
 * storage.saveAsync("user:001", user)
 *     .thenAccept(key -> log.info("Saved: {}", key));
 *
 * // Batch operations
 * Map&lt;String, Object&gt; batch = new HashMap&lt;&gt;();
 * batch.put("key1", value1);
 * batch.put("key2", value2);
 * storage.saveBatch(batch);
 * </pre>
 *
 * @author ooder Team
 * @since 0.7.1
 */
public interface StorageService {

    // ==================== Synchronous Operations ====================

    /**
     * Save data with the given key
     *
     * @param key  the storage key
     * @param data the data to save
     * @return the key used for storage
     */
    String save(String key, Object data);

    /**
     * Load data by key and type
     *
     * @param key  the storage key
     * @param type the expected type
     * @param <T>  the type parameter
     * @return Optional containing the data, or empty if not found
     */
    <T> Optional<T> load(String key, Class<T> type);

    /**
     * Load data by key with TypeReference for complex types
     *
     * @param key     the storage key
     * @param typeRef the TypeReference for complex types
     * @param <T>     the type parameter
     * @return Optional containing the data, or empty if not found
     */
    <T> Optional<T> load(String key, TypeReference<T> typeRef);

    /**
     * Delete data by key
     *
     * @param key the storage key
     * @return true if deleted, false if not found
     */
    boolean delete(String key);

    /**
     * Check if key exists
     *
     * @param key the storage key
     * @return true if exists
     */
    boolean exists(String key);

    /**
     * List keys with given prefix
     *
     * @param prefix the key prefix, null or empty for all keys
     * @return list of matching keys
     */
    List<String> listKeys(String prefix);

    // ==================== Asynchronous Operations ====================

    /**
     * Save data asynchronously
     *
     * @param key  the storage key
     * @param data the data to save
     * @return CompletableFuture with the key
     */
    CompletableFuture<String> saveAsync(String key, Object data);

    /**
     * Load data asynchronously
     *
     * @param key  the storage key
     * @param type the expected type
     * @param <T>  the type parameter
     * @return CompletableFuture with Optional data
     */
    <T> CompletableFuture<Optional<T>> loadAsync(String key, Class<T> type);

    /**
     * Load data asynchronously with TypeReference
     *
     * @param key     the storage key
     * @param typeRef the TypeReference for complex types
     * @param <T>     the type parameter
     * @return CompletableFuture with Optional data
     */
    <T> CompletableFuture<Optional<T>> loadAsync(String key, TypeReference<T> typeRef);

    /**
     * Delete data asynchronously
     *
     * @param key the storage key
     * @return CompletableFuture with deletion result
     */
    CompletableFuture<Boolean> deleteAsync(String key);

    // ==================== Batch Operations ====================

    /**
     * Save multiple items in batch
     *
     * @param batch map of key-value pairs to save
     */
    void saveBatch(Map<String, Object> batch);

    /**
     * Save multiple items asynchronously
     *
     * @param batch map of key-value pairs to save
     * @return CompletableFuture with void
     */
    CompletableFuture<Void> saveBatchAsync(Map<String, Object> batch);

    /**
     * Load multiple items by keys
     *
     * @param keys list of keys to load
     * @return map of key-value pairs (only existing keys)
     */
    Map<String, Object> loadBatch(List<String> keys);

    /**
     * Load multiple items asynchronously
     *
     * @param keys list of keys to load
     * @return CompletableFuture with map of key-value pairs
     */
    CompletableFuture<Map<String, Object>> loadBatchAsync(List<String> keys);

    // ==================== Configuration ====================

    /**
     * Get the base path for storage
     *
     * @return the base path
     */
    String getBasePath();

    /**
     * Set the base path for storage
     *
     * @param basePath the new base path
     */
    void setBasePath(String basePath);

    // ==================== Management ====================

    /**
     * Clear all stored data
     */
    void clear();

    /**
     * Get the number of stored items
     *
     * @return the count
     */
    long size();

    /**
     * Shutdown the storage service
     */
    void shutdown();
}

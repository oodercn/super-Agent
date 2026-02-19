package net.ooder.sdk.core.msg.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LocalMsgCache<V> {
    
    private static final Logger log = LoggerFactory.getLogger(LocalMsgCache.class);
    
    private final Map<String, CacheEntry<V>> cache = new ConcurrentHashMap<>();
    private final long maxSize;
    private final long expireTime;
    
    public LocalMsgCache() {
        this(10485760, 86400000);
    }
    
    public LocalMsgCache(long maxSize, long expireTime) {
        this.maxSize = maxSize;
        this.expireTime = expireTime;
    }
    
    public void put(String key, V value) {
        if (key == null || value == null) {
            return;
        }
        
        if (cache.size() >= maxSize) {
            evictOldest();
        }
        
        CacheEntry<V> entry = new CacheEntry<>();
        entry.value = value;
        entry.createTime = System.currentTimeMillis();
        entry.lastAccessTime = entry.createTime;
        
        cache.put(key, entry);
        
        log.debug("Cache put: {}", key);
    }
    
    public V get(String key) {
        if (key == null) {
            return null;
        }
        
        CacheEntry<V> entry = cache.get(key);
        if (entry == null) {
            return null;
        }
        
        if (isExpired(entry)) {
            cache.remove(key);
            return null;
        }
        
        entry.lastAccessTime = System.currentTimeMillis();
        entry.accessCount++;
        
        return entry.value;
    }
    
    public void remove(String key) {
        if (key != null) {
            cache.remove(key);
            log.debug("Cache remove: {}", key);
        }
    }
    
    public boolean contains(String key) {
        if (key == null) {
            return false;
        }
        
        CacheEntry<V> entry = cache.get(key);
        if (entry == null) {
            return false;
        }
        
        if (isExpired(entry)) {
            cache.remove(key);
            return false;
        }
        
        return true;
    }
    
    public void clear() {
        cache.clear();
        log.info("Cache cleared");
    }
    
    public int size() {
        return cache.size();
    }
    
    public Set<String> keys() {
        return new HashSet<>(cache.keySet());
    }
    
    public List<V> values() {
        List<V> result = new ArrayList<>();
        for (CacheEntry<V> entry : cache.values()) {
            if (!isExpired(entry)) {
                result.add(entry.value);
            }
        }
        return result;
    }
    
    public void cleanupExpired() {
        List<String> expiredKeys = new ArrayList<>();
        
        for (Map.Entry<String, CacheEntry<V>> entry : cache.entrySet()) {
            if (isExpired(entry.getValue())) {
                expiredKeys.add(entry.getKey());
            }
        }
        
        for (String key : expiredKeys) {
            cache.remove(key);
        }
        
        if (!expiredKeys.isEmpty()) {
            log.debug("Cleaned up {} expired entries", expiredKeys.size());
        }
    }
    
    private boolean isExpired(CacheEntry<V> entry) {
        return System.currentTimeMillis() - entry.createTime > expireTime;
    }
    
    private void evictOldest() {
        String oldestKey = null;
        long oldestTime = Long.MAX_VALUE;
        
        for (Map.Entry<String, CacheEntry<V>> entry : cache.entrySet()) {
            if (entry.getValue().lastAccessTime < oldestTime) {
                oldestTime = entry.getValue().lastAccessTime;
                oldestKey = entry.getKey();
            }
        }
        
        if (oldestKey != null) {
            cache.remove(oldestKey);
            log.debug("Evicted oldest entry: {}", oldestKey);
        }
    }
    
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("size", cache.size());
        stats.put("maxSize", maxSize);
        stats.put("expireTime", expireTime);
        
        long totalAccess = 0;
        for (CacheEntry<V> entry : cache.values()) {
            totalAccess += entry.accessCount;
        }
        stats.put("totalAccessCount", totalAccess);
        
        return stats;
    }
    
    private static class CacheEntry<V> {
        V value;
        long createTime;
        long lastAccessTime;
        int accessCount;
    }
}

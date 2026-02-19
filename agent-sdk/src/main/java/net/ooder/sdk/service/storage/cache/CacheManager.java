
package net.ooder.sdk.service.storage.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheManager {
    
    private static final Logger log = LoggerFactory.getLogger(CacheManager.class);
    
    private final Map<String, CacheEntry> cache;
    private final ScheduledExecutorService scheduler;
    private CacheStrategy strategy;
    private long defaultTtl = 300000;
    private int maxSize = 10000;
    
    public CacheManager() {
        this.cache = new ConcurrentHashMap<>();
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.strategy = CacheStrategy.LRU;
        
        scheduler.scheduleAtFixedRate(this::evictExpired, 60, 60, TimeUnit.SECONDS);
    }
    
    public void put(String key, Object value) {
        put(key, value, defaultTtl);
    }
    
    public void put(String key, Object value, long ttlMs) {
        if (cache.size() >= maxSize) {
            evict();
        }
        
        CacheEntry entry = new CacheEntry(value, System.currentTimeMillis() + ttlMs);
        cache.put(key, entry);
        
        log.debug("Cached value for key: {}", key);
    }
    
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        CacheEntry entry = cache.get(key);
        
        if (entry == null) {
            return null;
        }
        
        if (entry.isExpired()) {
            cache.remove(key);
            return null;
        }
        
        entry.incrementAccess();
        return (T) entry.getValue();
    }
    
    public void remove(String key) {
        cache.remove(key);
        log.debug("Removed cache entry for key: {}", key);
    }
    
    public boolean contains(String key) {
        CacheEntry entry = cache.get(key);
        return entry != null && !entry.isExpired();
    }
    
    public void clear() {
        cache.clear();
        log.info("Cache cleared");
    }
    
    public int size() {
        return cache.size();
    }
    
    private void evict() {
        if (strategy == CacheStrategy.LRU) {
            evictLRU();
        } else if (strategy == CacheStrategy.LFU) {
            evictLFU();
        }
    }
    
    private void evictLRU() {
        String lruKey = null;
        long oldestAccess = Long.MAX_VALUE;
        
        for (Map.Entry<String, CacheEntry> entry : cache.entrySet()) {
            if (entry.getValue().getLastAccessTime() < oldestAccess) {
                oldestAccess = entry.getValue().getLastAccessTime();
                lruKey = entry.getKey();
            }
        }
        
        if (lruKey != null) {
            cache.remove(lruKey);
            log.debug("Evicted LRU entry: {}", lruKey);
        }
    }
    
    private void evictLFU() {
        String lfuKey = null;
        int lowestCount = Integer.MAX_VALUE;
        
        for (Map.Entry<String, CacheEntry> entry : cache.entrySet()) {
            if (entry.getValue().getAccessCount() < lowestCount) {
                lowestCount = entry.getValue().getAccessCount();
                lfuKey = entry.getKey();
            }
        }
        
        if (lfuKey != null) {
            cache.remove(lfuKey);
            log.debug("Evicted LFU entry: {}", lfuKey);
        }
    }
    
    private void evictExpired() {
        int evicted = 0;
        for (Map.Entry<String, CacheEntry> entry : cache.entrySet()) {
            if (entry.getValue().isExpired()) {
                cache.remove(entry.getKey());
                evicted++;
            }
        }
        
        if (evicted > 0) {
            log.debug("Evicted {} expired entries", evicted);
        }
    }
    
    public void setStrategy(CacheStrategy strategy) {
        this.strategy = strategy;
    }
    
    public void setDefaultTtl(long ttlMs) {
        this.defaultTtl = ttlMs;
    }
    
    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
    
    public void shutdown() {
        scheduler.shutdown();
        cache.clear();
    }
    
    private static class CacheEntry {
        private final Object value;
        private final long expireTime;
        private long lastAccessTime;
        private int accessCount;
        
        public CacheEntry(Object value, long expireTime) {
            this.value = value;
            this.expireTime = expireTime;
            this.lastAccessTime = System.currentTimeMillis();
            this.accessCount = 0;
        }
        
        public Object getValue() { return value; }
        public long getLastAccessTime() { return lastAccessTime; }
        public int getAccessCount() { return accessCount; }
        
        public boolean isExpired() {
            return System.currentTimeMillis() > expireTime;
        }
        
        public void incrementAccess() {
            accessCount++;
            lastAccessTime = System.currentTimeMillis();
        }
    }
}

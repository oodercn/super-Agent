
package net.ooder.sdk.service.storage.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StorageManager {
    
    private static final Logger log = LoggerFactory.getLogger(StorageManager.class);
    
    private final Map<String, JsonStorage> storages;
    private final String basePath;
    private final Map<String, Object> memoryCache;
    private final List<StorageListener> listeners;
    
    private boolean cacheEnabled = true;
    private int maxCacheSize = 10000;
    
    public StorageManager(String basePath) {
        this.basePath = basePath;
        this.storages = new ConcurrentHashMap<>();
        this.memoryCache = new ConcurrentHashMap<>();
        this.listeners = new ArrayList<>();
        
        storages.put("default", new JsonStorage(basePath));
    }
    
    public JsonStorage getStorage(String namespace) {
        return storages.computeIfAbsent(namespace, k -> {
            String path = basePath + File.separator + namespace;
            return new JsonStorage(path);
        });
    }
    
    public void save(String namespace, String key, Map<String, Object> data) throws IOException {
        JsonStorage storage = getStorage(namespace);
        storage.save(key, data);
        
        if (cacheEnabled) {
            String cacheKey = namespace + ":" + key;
            memoryCache.put(cacheKey, data);
            
            if (memoryCache.size() > maxCacheSize) {
                evictOldest();
            }
        }
        
        notifyListeners("save", namespace, key, data);
        log.debug("Saved {}:{} to storage", namespace, key);
    }
    
    public Map<String, Object> load(String namespace, String key) throws IOException {
        if (cacheEnabled) {
            String cacheKey = namespace + ":" + key;
            Object cached = memoryCache.get(cacheKey);
            if (cached != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> result = (Map<String, Object>) cached;
                return result;
            }
        }
        
        JsonStorage storage = getStorage(namespace);
        Map<String, Object> data = storage.load(key);
        
        if (data != null && cacheEnabled) {
            String cacheKey = namespace + ":" + key;
            memoryCache.put(cacheKey, data);
        }
        
        return data;
    }
    
    public void delete(String namespace, String key) {
        JsonStorage storage = getStorage(namespace);
        storage.delete(key);
        
        if (cacheEnabled) {
            String cacheKey = namespace + ":" + key;
            memoryCache.remove(cacheKey);
        }
        
        notifyListeners("delete", namespace, key, null);
        log.debug("Deleted {}:{} from storage", namespace, key);
    }
    
    public boolean exists(String namespace, String key) {
        JsonStorage storage = getStorage(namespace);
        return storage.exists(key);
    }
    
    public List<String> listKeys(String namespace) {
        JsonStorage storage = getStorage(namespace);
        return storage.listKeys();
    }
    
    public void clearNamespace(String namespace) {
        JsonStorage storage = getStorage(namespace);
        List<String> keys = storage.listKeys();
        
        for (String key : keys) {
            storage.delete(key);
        }
        
        if (cacheEnabled) {
            String prefix = namespace + ":";
            memoryCache.keySet().removeIf(k -> k.startsWith(prefix));
        }
        
        log.info("Cleared namespace: {}", namespace);
    }
    
    public void clearCache() {
        memoryCache.clear();
        log.info("Cleared memory cache");
    }
    
    private void evictOldest() {
        int toRemove = memoryCache.size() / 10;
        List<String> keys = new ArrayList<>(memoryCache.keySet());
        for (int i = 0; i < toRemove && i < keys.size(); i++) {
            memoryCache.remove(keys.get(i));
        }
    }
    
    public void addListener(StorageListener listener) {
        listeners.add(listener);
    }
    
    private void notifyListeners(String operation, String namespace, String key, Map<String, Object> data) {
        for (StorageListener listener : listeners) {
            try {
                listener.onStorageEvent(operation, namespace, key, data);
            } catch (Exception e) {
                log.warn("Storage listener error", e);
            }
        }
    }
    
    public void setCacheEnabled(boolean enabled) { this.cacheEnabled = enabled; }
    public void setMaxCacheSize(int size) { this.maxCacheSize = size; }
    
    public interface StorageListener {
        void onStorageEvent(String operation, String namespace, String key, Map<String, Object> data);
    }
}

package net.ooder.examples.endagent.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class CacheService {
    // 缓存项类
    private static class CacheItem<T> {
        private T value;
        private long expireTime;

        public CacheItem(T value, long ttl, TimeUnit timeUnit) {
            this.value = value;
            this.expireTime = System.currentTimeMillis() + timeUnit.toMillis(ttl);
        }

        public T getValue() {
            return isExpired() ? null : value;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expireTime;
        }
    }

    // SKILL元数据缓存 (TTL 30秒)
    private final Map<String, CacheItem<Object>> skillMetadataCache = new ConcurrentHashMap<>();
    // 权限信息缓存 (TTL 10分钟)
    private final Map<String, CacheItem<Object>> permissionCache = new ConcurrentHashMap<>();
    // 资源信息缓存 (TTL 1分钟)
    private final Map<String, CacheItem<Object>> resourceCache = new ConcurrentHashMap<>();
    // Cap信息缓存 (TTL 5分钟)
    private final Map<String, CacheItem<Object>> capCache = new ConcurrentHashMap<>();

    // SKILL元数据缓存操作
    public void putSkillMetadata(String key, Object value) {
        skillMetadataCache.put(key, new CacheItem<>(value, 30, TimeUnit.SECONDS));
    }

    public Object getSkillMetadata(String key) {
        CacheItem<Object> item = skillMetadataCache.get(key);
        if (item == null) {
            return null;
        }
        Object value = item.getValue();
        if (value == null) {
            skillMetadataCache.remove(key);
        }
        return value;
    }

    public void removeSkillMetadata(String key) {
        skillMetadataCache.remove(key);
    }

    // 权限信息缓存操作
    public void putPermission(String key, Object value) {
        permissionCache.put(key, new CacheItem<>(value, 10, TimeUnit.MINUTES));
    }

    public Object getPermission(String key) {
        CacheItem<Object> item = permissionCache.get(key);
        if (item == null) {
            return null;
        }
        Object value = item.getValue();
        if (value == null) {
            permissionCache.remove(key);
        }
        return value;
    }

    public void removePermission(String key) {
        permissionCache.remove(key);
    }

    // 资源信息缓存操作
    public void putResource(String key, Object value) {
        resourceCache.put(key, new CacheItem<>(value, 1, TimeUnit.MINUTES));
    }

    public Object getResource(String key) {
        CacheItem<Object> item = resourceCache.get(key);
        if (item == null) {
            return null;
        }
        Object value = item.getValue();
        if (value == null) {
            resourceCache.remove(key);
        }
        return value;
    }

    public void removeResource(String key) {
        resourceCache.remove(key);
    }

    // Cap信息缓存操作
    public void putCap(String key, Object value) {
        capCache.put(key, new CacheItem<>(value, 5, TimeUnit.MINUTES));
    }

    public Object getCap(String key) {
        CacheItem<Object> item = capCache.get(key);
        if (item == null) {
            return null;
        }
        Object value = item.getValue();
        if (value == null) {
            capCache.remove(key);
        }
        return value;
    }

    public void removeCap(String key) {
        capCache.remove(key);
    }

    // 清除所有过期缓存项
    public void cleanupExpired() {
        skillMetadataCache.entrySet().removeIf(entry -> entry.getValue().isExpired());
        permissionCache.entrySet().removeIf(entry -> entry.getValue().isExpired());
        resourceCache.entrySet().removeIf(entry -> entry.getValue().isExpired());
        capCache.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }

    // 清除所有缓存
    public void clearAll() {
        skillMetadataCache.clear();
        permissionCache.clear();
        resourceCache.clear();
        capCache.clear();
    }
}
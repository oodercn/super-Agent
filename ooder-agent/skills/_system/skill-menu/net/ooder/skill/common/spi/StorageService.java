package net.ooder.skill.common.spi;

import java.util.Map;

public interface StorageService {
    
    void save(String sceneId, String key, Object value);
    
    Object get(String sceneId, String key);
    
    <T> T get(String sceneId, String key, Class<T> type);
    
    void delete(String sceneId, String key);
    
    Map<String, Object> query(String sceneId, String prefix);
    
    void batchSave(String sceneId, Map<String, Object> data);
}

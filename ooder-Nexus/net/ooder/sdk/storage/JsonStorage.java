package net.ooder.sdk.storage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.List;
import java.util.Map;

/**
 * JSON格式专用存储接口，扩展StorageService接口，提供JSON特定的操作功能
 */
public interface JsonStorage extends StorageService {
    // JSON对象操作
    boolean saveJson(String key, JSONObject json);
    JSONObject loadJson(String key);
    
    // 泛型类型支持
    <T> T loadJson(String key, TypeReference<T> typeRef);
    
    // 部分更新
    boolean updateJson(String key, Map<String, Object> updates);
    <T> T getJsonField(String key, String fieldName, Class<T> fieldType);
    
    // 查询支持
    <T> List<T> findByField(String directory, String fieldName, Object fieldValue, Class<T> clazz);
}
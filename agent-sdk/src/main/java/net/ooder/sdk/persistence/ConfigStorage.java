package net.ooder.sdk.persistence;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface ConfigStorage {
    // 存储配置
    CompletableFuture<Boolean> saveConfig(String key, Object value);
    CompletableFuture<Boolean> saveConfigs(Map<String, Object> configs);
    
    // 加载配置
    <T> CompletableFuture<T> loadConfig(String key, Class<T> clazz);
    CompletableFuture<Map<String, Object>> loadAllConfigs();
    <T> CompletableFuture<Map<String, T>> loadConfigsByType(Class<T> clazz);
    
    // 删除配置
    CompletableFuture<Boolean> deleteConfig(String key);
    CompletableFuture<Boolean> deleteConfigs(List<String> keys);
    CompletableFuture<Boolean> deleteAllConfigs();
    
    // 检查配置
    CompletableFuture<Boolean> existsConfig(String key);
    CompletableFuture<Long> countConfigs();
    
    // 配置查询
    CompletableFuture<List<String>> listConfigKeys();
    CompletableFuture<List<String>> listConfigKeysByPrefix(String prefix);
    CompletableFuture<Map<String, Object>> searchConfigs(String pattern);
    
    // 配置更新
    CompletableFuture<Boolean> updateConfig(String key, Object value);
    CompletableFuture<Boolean> updateConfigs(Map<String, Object> configs);
    
    // 配置版本管理
    CompletableFuture<Boolean> saveConfigVersion(String key, Object value, String version);
    <T> CompletableFuture<T> loadConfigVersion(String key, String version, Class<T> clazz);
    CompletableFuture<List<String>> listConfigVersions(String key);
    CompletableFuture<Boolean> deleteConfigVersion(String key, String version);
    
    // 配置导入导出
    CompletableFuture<Boolean> exportConfigs(String exportPath);
    CompletableFuture<Boolean> importConfigs(String importPath);
    
    // 配置验证
    CompletableFuture<Boolean> validateConfig(String key, Object value);
    CompletableFuture<Map<String, Object>> validateAllConfigs();
}

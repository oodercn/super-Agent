package net.ooder.sdk.persistence.impl;

import net.ooder.sdk.persistence.ConfigStorage;
import net.ooder.sdk.persistence.StorageManager;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ConfigStorageImpl implements ConfigStorage {
    private static final String CONFIG_PREFIX = "config_";
    private static final String CONFIG_VERSION_PREFIX = "config_version_";
    private final StorageManager storageManager;
    
    public ConfigStorageImpl(StorageManager storageManager) {
        this.storageManager = storageManager;
    }
    
    @Override
    public CompletableFuture<Boolean> saveConfig(String key, Object value) {
        String storageKey = CONFIG_PREFIX + key;
        return storageManager.save(storageKey, value);
    }
    
    @Override
    public CompletableFuture<Boolean> saveConfigs(Map<String, Object> configs) {
        Map<String, Object> entries = new HashMap<>();
        for (Map.Entry<String, Object> entry : configs.entrySet()) {
            String storageKey = CONFIG_PREFIX + entry.getKey();
            entries.put(storageKey, entry.getValue());
        }
        return storageManager.saveAll(entries);
    }
    
    @Override
    public <T> CompletableFuture<T> loadConfig(String key, Class<T> clazz) {
        String storageKey = CONFIG_PREFIX + key;
        return storageManager.load(storageKey, clazz);
    }
    
    @Override
    public CompletableFuture<Map<String, Object>> loadAllConfigs() {
        return storageManager.loadAll(Object.class)
            .thenApply(map -> {
                Map<String, Object> configs = new HashMap<>();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String key = entry.getKey();
                    if (key.startsWith(CONFIG_PREFIX)) {
                        String configKey = key.substring(CONFIG_PREFIX.length());
                        configs.put(configKey, entry.getValue());
                    }
                }
                return configs;
            });
    }
    
    @Override
    public <T> CompletableFuture<Map<String, T>> loadConfigsByType(Class<T> clazz) {
        return storageManager.loadAll(clazz)
            .thenApply(map -> {
                Map<String, T> configs = new HashMap<>();
                for (Map.Entry<String, T> entry : map.entrySet()) {
                    String key = entry.getKey();
                    if (key.startsWith(CONFIG_PREFIX)) {
                        String configKey = key.substring(CONFIG_PREFIX.length());
                        configs.put(configKey, entry.getValue());
                    }
                }
                return configs;
            });
    }
    
    @Override
    public CompletableFuture<Boolean> deleteConfig(String key) {
        String storageKey = CONFIG_PREFIX + key;
        return storageManager.delete(storageKey);
    }
    
    @Override
    public CompletableFuture<Boolean> deleteConfigs(List<String> keys) {
        List<String> storageKeys = keys.stream()
            .map(key -> CONFIG_PREFIX + key)
            .collect(Collectors.toList());
        return storageManager.deleteAll(storageKeys);
    }
    
    @Override
    public CompletableFuture<Boolean> deleteAllConfigs() {
        return loadAllConfigs()
            .thenCompose(configs -> {
                List<String> storageKeys = configs.keySet().stream()
                    .map(key -> CONFIG_PREFIX + key)
                    .collect(Collectors.toList());
                return storageManager.deleteAll(storageKeys);
            });
    }
    
    @Override
    public CompletableFuture<Boolean> existsConfig(String key) {
        String storageKey = CONFIG_PREFIX + key;
        return storageManager.exists(storageKey);
    }
    
    @Override
    public CompletableFuture<Long> countConfigs() {
        return loadAllConfigs()
            .thenApply(configs -> (long) configs.size());
    }
    
    @Override
    public CompletableFuture<List<String>> listConfigKeys() {
        return loadAllConfigs()
            .thenApply(configs -> new ArrayList<>(configs.keySet()));
    }
    
    @Override
    public CompletableFuture<List<String>> listConfigKeysByPrefix(String prefix) {
        return listConfigKeys()
            .thenApply(keys -> keys.stream()
                .filter(key -> key.startsWith(prefix))
                .collect(Collectors.toList()));
    }
    
    @Override
    public CompletableFuture<Map<String, Object>> searchConfigs(String pattern) {
        return loadAllConfigs()
            .thenApply(configs -> {
                Map<String, Object> result = new HashMap<>();
                for (Map.Entry<String, Object> entry : configs.entrySet()) {
                    String key = entry.getKey();
                    if (key.matches(pattern)) {
                        result.put(key, entry.getValue());
                    }
                }
                return result;
            });
    }
    
    @Override
    public CompletableFuture<Boolean> updateConfig(String key, Object value) {
        return saveConfig(key, value);
    }
    
    @Override
    public CompletableFuture<Boolean> updateConfigs(Map<String, Object> configs) {
        return saveConfigs(configs);
    }
    
    @Override
    public CompletableFuture<Boolean> saveConfigVersion(String key, Object value, String version) {
        String storageKey = CONFIG_VERSION_PREFIX + key + "_" + version;
        return storageManager.save(storageKey, value);
    }
    
    @Override
    public <T> CompletableFuture<T> loadConfigVersion(String key, String version, Class<T> clazz) {
        String storageKey = CONFIG_VERSION_PREFIX + key + "_" + version;
        return storageManager.load(storageKey, clazz);
    }
    
    @Override
    public CompletableFuture<List<String>> listConfigVersions(String key) {
        return storageManager.loadAll(Object.class)
            .thenApply(map -> {
                List<String> versions = new ArrayList<>();
                String prefix = CONFIG_VERSION_PREFIX + key + "_";
                for (String storageKey : map.keySet()) {
                    if (storageKey.startsWith(prefix)) {
                        String version = storageKey.substring(prefix.length());
                        versions.add(version);
                    }
                }
                return versions;
            });
    }
    
    @Override
    public CompletableFuture<Boolean> deleteConfigVersion(String key, String version) {
        String storageKey = CONFIG_VERSION_PREFIX + key + "_" + version;
        return storageManager.delete(storageKey);
    }
    
    @Override
    public CompletableFuture<Boolean> exportConfigs(String exportPath) {
        // 这里可以实现配置导出逻辑
        // 例如将配置导出到指定的文件中
        return CompletableFuture.completedFuture(true);
    }
    
    @Override
    public CompletableFuture<Boolean> importConfigs(String importPath) {
        // 这里可以实现配置导入逻辑
        // 例如从指定的文件中导入配置
        return CompletableFuture.completedFuture(true);
    }
    
    @Override
    public CompletableFuture<Boolean> validateConfig(String key, Object value) {
        // 这里可以实现配置验证逻辑
        // 例如验证配置值是否符合预期格式
        return CompletableFuture.completedFuture(true);
    }
    
    @Override
    public CompletableFuture<Map<String, Object>> validateAllConfigs() {
        // 这里可以实现所有配置的验证逻辑
        // 例如验证所有配置值是否符合预期格式
        return CompletableFuture.completedFuture(new HashMap<>());
    }
}

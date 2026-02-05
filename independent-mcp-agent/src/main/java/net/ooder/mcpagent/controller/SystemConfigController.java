package net.ooder.mcpagent.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/system/config")
public class SystemConfigController {

    private static final Logger log = LoggerFactory.getLogger(SystemConfigController.class);
    
    // 系统配置存储
    private final Map<String, Object> systemConfig = new HashMap<>();
    
    // 配置文件路径
    private static final String CONFIG_FILE = "system-config.properties";
    
    // 初始化配置
    public SystemConfigController() {
        initializeConfig();
        loadConfigFromFile();
    }
    
    private void initializeConfig() {
        // 基础配置
        Map<String, Object> basicConfig = new HashMap<>();
        basicConfig.put("systemName", "Independent MCP Agent");
        basicConfig.put("systemVersion", "0.6.5");
        basicConfig.put("systemDescription", "独立MCP Agent系统，用于网络管理和服务协调");
        basicConfig.put("defaultLanguage", "zh-CN");
        basicConfig.put("timezone", "Asia/Shanghai");
        basicConfig.put("dateFormat", "yyyy-MM-dd HH:mm:ss");
        
        // 网络配置
        Map<String, Object> networkConfig = new HashMap<>();
        networkConfig.put("defaultPort", 9876);
        networkConfig.put("httpPort", 8091);
        networkConfig.put("maxConnections", 1000);
        networkConfig.put("connectionTimeout", 30000);
        networkConfig.put("socketTimeout", 60000);
        networkConfig.put("bufferSize", 8192);
        
        // 服务配置
        Map<String, Object> serviceConfig = new HashMap<>();
        serviceConfig.put("heartbeatInterval", 30000);
        serviceConfig.put("serviceCheckInterval", 60000);
        serviceConfig.put("maxServiceInstances", 10);
        serviceConfig.put("serviceStartupTimeout", 120000);
        serviceConfig.put("serviceShutdownTimeout", 60000);
        
        // 安全配置
        Map<String, Object> securityConfig = new HashMap<>();
        securityConfig.put("authenticationEnabled", false);
        securityConfig.put("encryptionEnabled", false);
        securityConfig.put("sslEnabled", false);
        securityConfig.put("apiKey", "test-api-key");
        securityConfig.put("corsEnabled", true);
        securityConfig.put("allowedOrigins", "*");
        
        // 日志配置
        Map<String, Object> logConfig = new HashMap<>();
        logConfig.put("logLevel", "INFO");
        logConfig.put("logFile", "logs/system.log");
        logConfig.put("maxLogFileSize", "10MB");
        logConfig.put("maxLogFiles", 5);
        logConfig.put("consoleLoggingEnabled", true);
        logConfig.put("fileLoggingEnabled", true);
        
        // 存储配置
        Map<String, Object> storageConfig = new HashMap<>();
        storageConfig.put("dataDirectory", "data");
        storageConfig.put("tempDirectory", "temp");
        storageConfig.put("backupDirectory", "backup");
        storageConfig.put("maxBackupFiles", 10);
        storageConfig.put("backupInterval", 86400000); // 24小时
        
        // 性能配置
        Map<String, Object> performanceConfig = new HashMap<>();
        performanceConfig.put("threadPoolSize", Runtime.getRuntime().availableProcessors() * 2);
        performanceConfig.put("maxThreadPoolSize", 50);
        performanceConfig.put("queueCapacity", 1000);
        performanceConfig.put("keepAliveTime", 60000);
        performanceConfig.put("memoryThreshold", 80); // 80%
        performanceConfig.put("cpuThreshold", 90); // 90%
        
        // 整合所有配置
        systemConfig.put("basic", basicConfig);
        systemConfig.put("network", networkConfig);
        systemConfig.put("service", serviceConfig);
        systemConfig.put("security", securityConfig);
        systemConfig.put("log", logConfig);
        systemConfig.put("storage", storageConfig);
        systemConfig.put("performance", performanceConfig);
    }
    
    /**
     * 获取所有系统配置
     */
    @GetMapping("/all")
    public Map<String, Object> getAllConfig() {
        log.info("Get all system config requested");
        Map<String, Object> response = new HashMap<>();
        
        try {
            response.put("status", "success");
            response.put("message", "System config retrieved successfully");
            response.put("data", systemConfig);
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting system config: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "CONFIG_RETRIEVAL_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 获取指定类型的配置
     */
    @GetMapping("/{configType}")
    public Map<String, Object> getConfigByType(@PathVariable String configType) {
        log.info("Get system config by type requested: {}", configType);
        Map<String, Object> response = new HashMap<>();
        
        try {
            Object config = systemConfig.get(configType);
            if (config == null) {
                response.put("status", "error");
                response.put("message", "Config type not found");
                response.put("code", "CONFIG_TYPE_NOT_FOUND");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }
            
            response.put("status", "success");
            response.put("message", "System config retrieved successfully");
            response.put("configType", configType);
            response.put("data", config);
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting system config by type: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "CONFIG_RETRIEVAL_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 更新指定类型的配置
     */
    @PutMapping("/{configType}")
    public Map<String, Object> updateConfigByType(
            @PathVariable String configType, 
            @RequestBody Map<String, Object> configData) {
        log.info("Update system config by type requested: {}, data: {}", configType, configData);
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (!systemConfig.containsKey(configType)) {
                response.put("status", "error");
                response.put("message", "Config type not found");
                response.put("code", "CONFIG_TYPE_NOT_FOUND");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }
            
            @SuppressWarnings("unchecked")
            Map<String, Object> existingConfig = (Map<String, Object>) systemConfig.get(configType);
            existingConfig.putAll(configData);
            
            saveConfigToFile();
            
            response.put("status", "success");
            response.put("message", "System config updated successfully");
            response.put("configType", configType);
            response.put("data", existingConfig);
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error updating system config: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "CONFIG_UPDATE_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 更新单个配置项
     */
    @PutMapping("/{configType}/{configKey}")
    public Map<String, Object> updateConfigItem(
            @PathVariable String configType, 
            @PathVariable String configKey, 
            @RequestBody Map<String, Object> configData) {
        log.info("Update system config item requested: {}.{}, data: {}", configType, configKey, configData);
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (!systemConfig.containsKey(configType)) {
                response.put("status", "error");
                response.put("message", "Config type not found");
                response.put("code", "CONFIG_TYPE_NOT_FOUND");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }
            
            @SuppressWarnings("unchecked")
            Map<String, Object> existingConfig = (Map<String, Object>) systemConfig.get(configType);
            
            Object value = configData.get("value");
            if (value == null) {
                response.put("status", "error");
                response.put("message", "Config value is required");
                response.put("code", "CONFIG_VALUE_REQUIRED");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }
            
            existingConfig.put(configKey, value);
            
            saveConfigToFile();
            
            response.put("status", "success");
            response.put("message", "System config item updated successfully");
            response.put("configType", configType);
            response.put("configKey", configKey);
            response.put("configValue", value);
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error updating system config item: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "CONFIG_UPDATE_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 重置配置到默认值
     */
    @PostMapping("/reset/{configType}")
    public Map<String, Object> resetConfig(@PathVariable String configType) {
        log.info("Reset system config requested: {}", configType);
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (!systemConfig.containsKey(configType)) {
                response.put("status", "error");
                response.put("message", "Config type not found");
                response.put("code", "CONFIG_TYPE_NOT_FOUND");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }
            
            // 重置指定类型的配置
            resetConfigType(configType);
            
            saveConfigToFile();
            
            response.put("status", "success");
            response.put("message", "System config reset successfully");
            response.put("configType", configType);
            response.put("data", systemConfig.get(configType));
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error resetting system config: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "CONFIG_RESET_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 重置所有配置到默认值
     */
    @PostMapping("/reset-all")
    public Map<String, Object> resetAllConfig() {
        log.info("Reset all system config requested");
        Map<String, Object> response = new HashMap<>();
        
        try {
            systemConfig.clear();
            initializeConfig();
            
            saveConfigToFile();
            
            response.put("status", "success");
            response.put("message", "All system config reset successfully");
            response.put("data", systemConfig);
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error resetting all system config: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "CONFIG_RESET_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 获取配置状态
     */
    @GetMapping("/status")
    public Map<String, Object> getConfigStatus() {
        log.info("Get system config status requested");
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<String, Object> status = new HashMap<>();
            status.put("configCount", systemConfig.size());
            status.put("lastModified", System.currentTimeMillis());
            status.put("configFileExists", new File(CONFIG_FILE).exists());
            status.put("configFileSize", new File(CONFIG_FILE).length());
            
            // 计算各类型配置数量
            Map<String, Integer> configTypeCounts = new HashMap<>();
            for (Map.Entry<String, Object> entry : systemConfig.entrySet()) {
                @SuppressWarnings("unchecked")
                Map<String, Object> configMap = (Map<String, Object>) entry.getValue();
                configTypeCounts.put(entry.getKey(), configMap.size());
            }
            status.put("configTypeCounts", configTypeCounts);
            
            response.put("status", "success");
            response.put("message", "System config status retrieved successfully");
            response.put("data", status);
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting system config status: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "CONFIG_STATUS_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 从文件加载配置
     */
    private void loadConfigFromFile() {
        File configFile = new File(CONFIG_FILE);
        if (!configFile.exists()) {
            log.info("Config file not found, using default config");
            return;
        }
        
        try (FileInputStream fis = new FileInputStream(configFile)) {
            Properties props = new Properties();
            props.load(fis);
            
            // 从属性文件加载配置
            for (String key : props.stringPropertyNames()) {
                String[] parts = key.split("\\.");
                if (parts.length >= 2) {
                    String configType = parts[0];
                    String configKey = parts[1];
                    String value = props.getProperty(key);
                    
                    if (systemConfig.containsKey(configType)) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> configMap = (Map<String, Object>) systemConfig.get(configType);
                        configMap.put(configKey, value);
                    }
                }
            }
            
            log.info("Config loaded from file: {}", CONFIG_FILE);
            
        } catch (IOException e) {
            log.error("Error loading config from file: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 保存配置到文件
     */
    private void saveConfigToFile() {
        File configFile = new File(CONFIG_FILE);
        
        try (FileOutputStream fos = new FileOutputStream(configFile)) {
            Properties props = new Properties();
            
            // 将配置保存到属性文件
            for (Map.Entry<String, Object> entry : systemConfig.entrySet()) {
                String configType = entry.getKey();
                @SuppressWarnings("unchecked")
                Map<String, Object> configMap = (Map<String, Object>) entry.getValue();
                
                for (Map.Entry<String, Object> configEntry : configMap.entrySet()) {
                    String key = configType + "." + configEntry.getKey();
                    String value = String.valueOf(configEntry.getValue());
                    props.setProperty(key, value);
                }
            }
            
            props.store(fos, "System Configuration");
            log.info("Config saved to file: {}", CONFIG_FILE);
            
        } catch (IOException e) {
            log.error("Error saving config to file: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 重置指定类型的配置
     */
    private void resetConfigType(String configType) {
        switch (configType) {
            case "basic":
                Map<String, Object> basicConfig = new HashMap<>();
                basicConfig.put("systemName", "Independent MCP Agent");
                basicConfig.put("systemVersion", "0.6.5");
                basicConfig.put("systemDescription", "独立MCP Agent系统，用于网络管理和服务协调");
                basicConfig.put("defaultLanguage", "zh-CN");
                basicConfig.put("timezone", "Asia/Shanghai");
                basicConfig.put("dateFormat", "yyyy-MM-dd HH:mm:ss");
                systemConfig.put("basic", basicConfig);
                break;
                
            case "network":
                Map<String, Object> networkConfig = new HashMap<>();
                networkConfig.put("defaultPort", 9876);
                networkConfig.put("httpPort", 8091);
                networkConfig.put("maxConnections", 1000);
                networkConfig.put("connectionTimeout", 30000);
                networkConfig.put("socketTimeout", 60000);
                networkConfig.put("bufferSize", 8192);
                systemConfig.put("network", networkConfig);
                break;
                
            case "service":
                Map<String, Object> serviceConfig = new HashMap<>();
                serviceConfig.put("heartbeatInterval", 30000);
                serviceConfig.put("serviceCheckInterval", 60000);
                serviceConfig.put("maxServiceInstances", 10);
                serviceConfig.put("serviceStartupTimeout", 120000);
                serviceConfig.put("serviceShutdownTimeout", 60000);
                systemConfig.put("service", serviceConfig);
                break;
                
            case "security":
                Map<String, Object> securityConfig = new HashMap<>();
                securityConfig.put("authenticationEnabled", false);
                securityConfig.put("encryptionEnabled", false);
                securityConfig.put("sslEnabled", false);
                securityConfig.put("apiKey", "test-api-key");
                securityConfig.put("corsEnabled", true);
                securityConfig.put("allowedOrigins", "*");
                systemConfig.put("security", securityConfig);
                break;
                
            case "log":
                Map<String, Object> logConfig = new HashMap<>();
                logConfig.put("logLevel", "INFO");
                logConfig.put("logFile", "logs/system.log");
                logConfig.put("maxLogFileSize", "10MB");
                logConfig.put("maxLogFiles", 5);
                logConfig.put("consoleLoggingEnabled", true);
                logConfig.put("fileLoggingEnabled", true);
                systemConfig.put("log", logConfig);
                break;
                
            case "storage":
                Map<String, Object> storageConfig = new HashMap<>();
                storageConfig.put("dataDirectory", "data");
                storageConfig.put("tempDirectory", "temp");
                storageConfig.put("backupDirectory", "backup");
                storageConfig.put("maxBackupFiles", 10);
                storageConfig.put("backupInterval", 86400000);
                systemConfig.put("storage", storageConfig);
                break;
                
            case "performance":
                Map<String, Object> performanceConfig = new HashMap<>();
                performanceConfig.put("threadPoolSize", Runtime.getRuntime().availableProcessors() * 2);
                performanceConfig.put("maxThreadPoolSize", 50);
                performanceConfig.put("queueCapacity", 1000);
                performanceConfig.put("keepAliveTime", 60000);
                performanceConfig.put("memoryThreshold", 80);
                performanceConfig.put("cpuThreshold", 90);
                systemConfig.put("performance", performanceConfig);
                break;
        }
    }
}
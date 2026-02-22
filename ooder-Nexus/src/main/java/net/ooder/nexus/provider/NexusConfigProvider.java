package net.ooder.nexus.provider;

import net.ooder.scene.core.Result;
import net.ooder.scene.core.PageResult;
import net.ooder.scene.core.SceneEngine;
import net.ooder.scene.provider.BaseProvider;
import net.ooder.scene.provider.ConfigProvider;
import net.ooder.scene.provider.ConfigHistory;
import net.ooder.scene.provider.ConfigExportResult;
import net.ooder.scene.provider.ConfigGroup;
import net.ooder.scene.provider.ConfigItem;
import net.ooder.scene.provider.model.config.NetworkConfig;
import net.ooder.scene.provider.model.config.SystemConfig;
import net.ooder.scene.provider.model.config.ServiceConfig;
import net.ooder.scene.provider.model.config.TerminalConfig;
import net.ooder.scene.provider.model.config.SecurityConfig;
import net.ooder.scene.provider.model.config.AdvancedConfig;
import net.ooder.scene.provider.model.config.BasicConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class NexusConfigProvider implements ConfigProvider {

    private static final Logger log = LoggerFactory.getLogger(NexusConfigProvider.class);
    private static final String CONFIG_DIR = "./config/";
    private static final String HISTORY_DIR = "./config/history/";

    private SceneEngine sceneEngine;
    private boolean initialized = false;
    private boolean running = false;
    
    private final Map<String, String> configStore = new ConcurrentHashMap<String, String>();
    private final Map<String, List<ConfigHistory>> configHistory = new ConcurrentHashMap<String, List<ConfigHistory>>();
    private final Map<String, ConfigGroup> configGroups = new ConcurrentHashMap<String, ConfigGroup>();
    private final AtomicLong historyIdCounter = new AtomicLong(0);

    @Override
    public String getProviderName() {
        return "NexusConfigProvider";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public void initialize(SceneEngine engine) {
        this.sceneEngine = engine;
        this.initialized = true;
        
        File configDir = new File(CONFIG_DIR);
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
        
        File historyDir = new File(HISTORY_DIR);
        if (!historyDir.exists()) {
            historyDir.mkdirs();
        }
        
        initializeDefaultConfigs();
        
        log.info("NexusConfigProvider initialized");
    }

    @Override
    public void start() {
        this.running = true;
        log.info("NexusConfigProvider started");
    }

    @Override
    public void stop() {
        this.running = false;
        log.info("NexusConfigProvider stopped");
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    private void initializeDefaultConfigs() {
        configStore.put("system.name", "ooderNexus");
        configStore.put("system.version", "2.0");
        configStore.put("system.mode", "production");
        configStore.put("system.timezone", "Asia/Shanghai");
        configStore.put("system.ntpServer", "pool.ntp.org");
        
        configStore.put("network.name", "Home Network");
        configStore.put("network.domain", "home.local");
        configStore.put("network.dns.primary", "8.8.8.8");
        configStore.put("network.dns.secondary", "8.8.4.4");
        
        configStore.put("api.port", "8080");
        configStore.put("api.timeout", "30000");
        configStore.put("api.maxConnections", "1000");
        
        configStore.put("log.level", "INFO");
        configStore.put("log.maxSize", "10000");
        configStore.put("log.retention", "7");
        
        ConfigGroup systemGroup = new ConfigGroup();
        systemGroup.setGroupName("system");
        systemGroup.setDescription("System-level configuration settings");
        systemGroup.setConfigCount(5);
        configGroups.put("system", systemGroup);
        
        ConfigGroup networkGroup = new ConfigGroup();
        networkGroup.setGroupName("network");
        networkGroup.setDescription("Network-related configuration settings");
        networkGroup.setConfigCount(4);
        configGroups.put("network", networkGroup);
        
        ConfigGroup apiGroup = new ConfigGroup();
        apiGroup.setGroupName("api");
        apiGroup.setDescription("API service configuration settings");
        apiGroup.setConfigCount(3);
        configGroups.put("api", apiGroup);
        
        ConfigGroup logGroup = new ConfigGroup();
        logGroup.setGroupName("log");
        logGroup.setDescription("Logging configuration settings");
        logGroup.setConfigCount(3);
        configGroups.put("log", logGroup);
    }

    @Override
    public Result<String> getConfig(String key) {
        log.debug("Getting config: {}", key);
        
        String value = configStore.get(key);
        if (value == null) {
            return Result.error("Config not found: " + key);
        }
        
        return Result.success(value);
    }

    @Override
    public Result<String> getConfig(String key, String defaultValue) {
        log.debug("Getting config: {} with default: {}", key, defaultValue);
        
        String value = configStore.get(key);
        return Result.success(value != null ? value : defaultValue);
    }

    @Override
    public <T> Result<T> getConfig(String key, Class<T> type) {
        log.debug("Getting config: {} as type: {}", key, type.getName());
        
        String value = configStore.get(key);
        if (value == null) {
            return Result.error("Config not found: " + key);
        }
        
        try {
            T converted = convertValue(value, type);
            return Result.success(converted);
        } catch (Exception e) {
            log.error("Failed to convert config value: {}", e.getMessage(), e);
            return Result.error("Failed to convert config value: " + e.getMessage());
        }
    }

    @Override
    public <T> Result<T> getConfig(String key, Class<T> type, T defaultValue) {
        log.debug("Getting config: {} as type: {} with default", key, type.getName());
        
        String value = configStore.get(key);
        if (value == null) {
            return Result.success(defaultValue);
        }
        
        try {
            T converted = convertValue(value, type);
            return Result.success(converted);
        } catch (Exception e) {
            log.error("Failed to convert config value: {}", e.getMessage(), e);
            return Result.success(defaultValue);
        }
    }

    @Override
    public Result<Boolean> setConfig(String key, String value) {
        log.info("Setting config: {} = {}", key, value);
        
        try {
            String oldValue = configStore.put(key, value);
            
            addConfigHistory(key, oldValue, value, "update");
            
            return Result.success(true);
        } catch (Exception e) {
            log.error("Failed to set config: {}", e.getMessage(), e);
            return Result.error("Failed to set config: " + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> setConfig(String key, Object value) {
        log.info("Setting config: {} = {}", key, value);
        
        try {
            String strValue = value != null ? String.valueOf(value) : null;
            String oldValue = configStore.put(key, strValue);
            
            addConfigHistory(key, oldValue, strValue, "update");
            
            return Result.success(true);
        } catch (Exception e) {
            log.error("Failed to set config: {}", e.getMessage(), e);
            return Result.error("Failed to set config: " + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> setConfigs(Map<String, String> configs) {
        log.info("Setting {} configs", configs.size());
        
        try {
            for (Map.Entry<String, String> entry : configs.entrySet()) {
                String oldValue = configStore.put(entry.getKey(), entry.getValue());
                addConfigHistory(entry.getKey(), oldValue, entry.getValue(), "batch-update");
            }
            
            return Result.success(true);
        } catch (Exception e) {
            log.error("Failed to set configs: {}", e.getMessage(), e);
            return Result.error("Failed to set configs: " + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> deleteConfig(String key) {
        log.info("Deleting config: {}", key);
        
        try {
            String oldValue = configStore.remove(key);
            
            if (oldValue != null) {
                addConfigHistory(key, oldValue, null, "delete");
                return Result.success(true);
            } else {
                return Result.error("Config not found: " + key);
            }
        } catch (Exception e) {
            log.error("Failed to delete config: {}", e.getMessage(), e);
            return Result.error("Failed to delete config: " + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> hasConfig(String key) {
        return Result.success(Boolean.valueOf(configStore.containsKey(key)));
    }

    @Override
    public Result<Map<String, String>> getAllConfigs() {
        log.debug("Getting all configs");
        return Result.success(new HashMap<String, String>(configStore));
    }

    @Override
    public Result<Map<String, String>> getConfigsByPrefix(String prefix) {
        log.debug("Getting configs by prefix: {}", prefix);
        
        Map<String, String> result = new HashMap<String, String>();
        
        for (Map.Entry<String, String> entry : configStore.entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        
        return Result.success(result);
    }

    @Override
    public Result<PageResult<ConfigHistory>> getConfigHistory(String key, int page, int size) {
        log.debug("Getting config history: {}, page: {}, size: {}", key, page, size);
        
        List<ConfigHistory> history = configHistory.get(key);
        if (history == null) {
            history = new ArrayList<ConfigHistory>();
        }
        
        List<ConfigHistory> sortedHistory = new ArrayList<ConfigHistory>(history);
        Collections.sort(sortedHistory, new Comparator<ConfigHistory>() {
            @Override
            public int compare(ConfigHistory a, ConfigHistory b) {
                return Long.compare(b.getChangedAt(), a.getChangedAt());
            }
        });
        
        int start = (page - 1) * size;
        int end = Math.min(start + size, sortedHistory.size());
        
        List<ConfigHistory> pageData = new ArrayList<ConfigHistory>();
        if (start < sortedHistory.size()) {
            pageData = sortedHistory.subList(start, end);
        }
        
        PageResult<ConfigHistory> pageResult = new PageResult<ConfigHistory>();
        pageResult.setItems(pageData);
        pageResult.setTotal(sortedHistory.size());
        pageResult.setPageNum(page);
        pageResult.setPageSize(size);
        
        return Result.success(pageResult);
    }

    @Override
    public Result<Boolean> rollbackConfig(String key, String historyId) {
        log.info("Rolling back config: {} to history: {}", key, historyId);
        
        List<ConfigHistory> history = configHistory.get(key);
        if (history == null) {
            return Result.error("No history found for config: " + key);
        }
        
        for (ConfigHistory h : history) {
            if (h.getHistoryId().equals(historyId)) {
                String currentValue = configStore.get(key);
                configStore.put(key, h.getOldValue());
                addConfigHistory(key, currentValue, h.getOldValue(), "rollback");
                return Result.success(true);
            }
        }
        
        return Result.error("History not found: " + historyId);
    }

    @Override
    public Result<ConfigExportResult> exportConfig(String format) {
        log.info("Exporting config with format: {}", format);
        
        try {
            Map<String, String> configs = new HashMap<String, String>(configStore);
            
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String extension = "json".equalsIgnoreCase(format) ? "json" : 
                              "yaml".equalsIgnoreCase(format) ? "yaml" : "properties";
            String fileName = "config_export_" + timestamp + "." + extension;
            String filePath = CONFIG_DIR + fileName;
            
            StringBuilder content = new StringBuilder();
            if ("yaml".equalsIgnoreCase(format)) {
                for (Map.Entry<String, String> entry : configs.entrySet()) {
                    content.append(entry.getKey() + ": \"" + escapeJson(entry.getValue()) + "\"\n");
                }
            } else if ("properties".equalsIgnoreCase(format)) {
                for (Map.Entry<String, String> entry : configs.entrySet()) {
                    content.append(entry.getKey() + "=" + entry.getValue() + "\n");
                }
            } else {
                content.append("{\n");
                int count = 0;
                for (Map.Entry<String, String> entry : configs.entrySet()) {
                    content.append("  \"" + entry.getKey() + ": \"" + escapeJson(entry.getValue()) + "\"");
                    if (count < configs.size() - 1) {
                        content.append(",");
                    }
                    content.append("\n");
                    count++;
                }
                content.append("}\n");
            }
            
            FileWriter writer = new FileWriter(filePath);
            writer.write(content.toString());
            writer.close();
            
            ConfigExportResult result = new ConfigExportResult();
            result.setFileName(fileName);
            result.setConfigCount(configs.size());
            result.setFileSize(new File(filePath).length());
            result.setContent(content.toString());
            result.setTimestamp(System.currentTimeMillis());
            
            return Result.success(result);
        } catch (Exception e) {
            log.error("Failed to export config: {}", e.getMessage(), e);
            return Result.error("Failed to export config: " + e.getMessage());
        }
    }

    @Override
    public Result<Integer> importConfig(String content, String format) {
        log.info("Importing config with format: {}", format);
        
        try {
            int count = 0;
            
            if ("properties".equalsIgnoreCase(format)) {
                String[] lines = content.split("\n");
                for (String line : lines) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("#")) continue;
                    
                    int equalIndex = line.indexOf("=");
                    if (equalIndex > 0) {
                        String key = line.substring(0, equalIndex).trim();
                        String value = line.substring(equalIndex + 1).trim();
                        
                        String oldValue = configStore.put(key, value);
                        addConfigHistory(key, oldValue, value, "import");
                        count++;
                    }
                }
            } else {
                content = content.trim();
                if (content.startsWith("{") && content.endsWith("}")) {
                    content = content.substring(1, content.length() - 1);
                    
                    String[] lines = content.split(",\n");
                    for (String line : lines) {
                        line = line.trim();
                        if (line.isEmpty()) continue;
                        
                        int colonIndex = line.indexOf(":");
                        if (colonIndex > 0) {
                            String key = line.substring(0, colonIndex).trim();
                            String value = line.substring(colonIndex + 1).trim();
                            
                            key = key.replace("\"", "");
                            value = value.replace("\"", "");
                            
                            String oldValue = configStore.put(key, value);
                            addConfigHistory(key, oldValue, value, "import");
                            count++;
                        }
                    }
                }
            }
            
            return Result.success(Integer.valueOf(count));
        } catch (Exception e) {
            log.error("Failed to import config: {}", e.getMessage(), e);
            return Result.error("Failed to import config: " + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> refreshConfig() {
        log.info("Refreshing config");
        return Result.success(true);
    }

    @Override
    public Result<List<ConfigGroup>> getConfigGroups() {
        log.debug("Getting config groups");
        return Result.success(new ArrayList<ConfigGroup>(configGroups.values()));
    }

    @Override
    public Result<ConfigGroup> getConfigGroup(String groupId) {
        log.debug("Getting config group: {}", groupId);
        
        ConfigGroup group = configGroups.get(groupId);
        if (group == null) {
            return Result.error("Config group not found: " + groupId);
        }
        
        return Result.success(group);
    }

    private void addConfigHistory(String key, String oldValue, String newValue, String reason) {
        List<ConfigHistory> history = configHistory.computeIfAbsent(key, k -> new ArrayList<ConfigHistory>());
        
        ConfigHistory entry = new ConfigHistory();
        entry.setHistoryId("history-" + historyIdCounter.incrementAndGet());
        entry.setKey(key);
        entry.setOldValue(oldValue);
        entry.setNewValue(newValue);
        entry.setChangedAt(System.currentTimeMillis());
        entry.setReason(reason);
        
        history.add(entry);
        
        while (history.size() > 100) {
            history.remove(0);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T convertValue(String value, Class<T> type) {
        if (value == null) {
            return null;
        }
        
        if (type == String.class) {
            return (T) value;
        } else if (type == Integer.class || type == int.class) {
            return (T) Integer.valueOf(value);
        } else if (type == Long.class || type == long.class) {
            return (T) Long.valueOf(value);
        } else if (type == Double.class || type == double.class) {
            return (T) Double.valueOf(value);
        } else if (type == Boolean.class || type == boolean.class) {
            return (T) Boolean.valueOf(value);
        } else {
            return (T) value;
        }
    }

    private String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }

    @Override
    public Result<Boolean> saveNetworkConfig(NetworkConfig networkConfig) {
        log.info("Saving network config");
        try {
            if (networkConfig == null) {
                return Result.error("NetworkConfig cannot be null");
            }
            return Result.success(true);
        } catch (Exception e) {
            log.error("Failed to save network config: {}", e.getMessage(), e);
            return Result.error("Failed to save network config: " + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> saveSystemConfig(SystemConfig systemConfig) {
        log.info("Saving system config");
        try {
            if (systemConfig == null) {
                return Result.error("SystemConfig cannot be null");
            }
            return Result.success(true);
        } catch (Exception e) {
            log.error("Failed to save system config: {}", e.getMessage(), e);
            return Result.error("Failed to save system config: " + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> saveServiceConfig(ServiceConfig serviceConfig) {
        log.info("Saving service config");
        try {
            if (serviceConfig == null) {
                return Result.error("ServiceConfig cannot be null");
            }
            return Result.success(true);
        } catch (Exception e) {
            log.error("Failed to save service config: {}", e.getMessage(), e);
            return Result.error("Failed to save service config: " + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> saveTerminalConfig(TerminalConfig terminalConfig) {
        log.info("Saving terminal config");
        try {
            if (terminalConfig == null) {
                return Result.error("TerminalConfig cannot be null");
            }
            return Result.success(true);
        } catch (Exception e) {
            log.error("Failed to save terminal config: {}", e.getMessage(), e);
            return Result.error("Failed to save terminal config: " + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> saveSecurityConfig(SecurityConfig securityConfig) {
        log.info("Saving security config");
        try {
            if (securityConfig == null) {
                return Result.error("SecurityConfig cannot be null");
            }
            return Result.success(true);
        } catch (Exception e) {
            log.error("Failed to save security config: {}", e.getMessage(), e);
            return Result.error("Failed to save security config: " + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> saveAdvancedConfig(AdvancedConfig advancedConfig) {
        log.info("Saving advanced config");
        try {
            if (advancedConfig == null) {
                return Result.error("AdvancedConfig cannot be null");
            }
            return Result.success(true);
        } catch (Exception e) {
            log.error("Failed to save advanced config: {}", e.getMessage(), e);
            return Result.error("Failed to save advanced config: " + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> saveBasicConfig(BasicConfig basicConfig) {
        log.info("Saving basic config");
        try {
            if (basicConfig == null) {
                return Result.error("BasicConfig cannot be null");
            }
            return Result.success(true);
        } catch (Exception e) {
            log.error("Failed to save basic config: {}", e.getMessage(), e);
            return Result.error("Failed to save basic config: " + e.getMessage());
        }
    }

    @Override
    public Result<NetworkConfig> getNetworkConfig() {
        log.info("Getting network config");
        return Result.success(null);
    }

    @Override
    public Result<SystemConfig> getSystemConfig() {
        log.info("Getting system config");
        return Result.success(null);
    }

    @Override
    public Result<ServiceConfig> getServiceConfig() {
        log.info("Getting service config");
        return Result.success(null);
    }

    @Override
    public Result<TerminalConfig> getTerminalConfig() {
        log.info("Getting terminal config");
        return Result.success(null);
    }

    @Override
    public Result<SecurityConfig> getSecurityConfig() {
        log.info("Getting security config");
        return Result.success(null);
    }

    @Override
    public Result<AdvancedConfig> getAdvancedConfig() {
        log.info("Getting advanced config");
        return Result.success(null);
    }

    @Override
    public Result<BasicConfig> getBasicConfig() {
        log.info("Getting basic config");
        return Result.success(null);
    }
}

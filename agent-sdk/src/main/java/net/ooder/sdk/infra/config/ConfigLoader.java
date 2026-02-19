
package net.ooder.sdk.infra.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigLoader {
    
    private static final Logger log = LoggerFactory.getLogger(ConfigLoader.class);
    
    private static final String[] CONFIG_LOCATIONS = {
        "config/sdk.properties",
        "config/sdk.yml",
        "sdk.properties",
        "sdk.yml"
    };
    
    private final Map<String, Object> config;
    private final String configPath;
    
    public ConfigLoader() {
        this.config = new HashMap<>();
        this.configPath = null;
        loadDefaults();
    }
    
    public ConfigLoader(String configPath) {
        this.config = new HashMap<>();
        this.configPath = configPath;
        load(configPath);
    }
    
    private void loadDefaults() {
        config.put("sdk.name", "ooder-sdk");
        config.put("sdk.version", "0.7.0");
        config.put("network.timeout", 5000);
        config.put("network.heartbeat.interval", 30000);
        config.put("storage.base.path", "./data");
        config.put("monitoring.enabled", true);
        config.put("logging.level", "INFO");
    }
    
    public void load(String path) {
        Path filePath = Paths.get(path);
        
        if (!Files.exists(filePath)) {
            log.warn("Config file not found: {}", path);
            return;
        }
        
        String fileName = filePath.getFileName().toString().toLowerCase();
        
        if (fileName.endsWith(".properties")) {
            loadProperties(filePath);
        } else if (fileName.endsWith(".yml") || fileName.endsWith(".yaml")) {
            loadYaml(filePath);
        } else {
            log.warn("Unsupported config file format: {}", fileName);
        }
    }
    
    private void loadProperties(Path path) {
        Properties props = new Properties();
        try (InputStream is = Files.newInputStream(path)) {
            props.load(is);
            
            for (String key : props.stringPropertyNames()) {
                String value = props.getProperty(key);
                config.put(key, parseValue(value));
            }
            
            log.info("Loaded {} properties from {}", props.size(), path);
        } catch (IOException e) {
            log.error("Failed to load properties from: {}", path, e);
        }
    }
    
    private void loadYaml(Path path) {
        try {
            String content = new String(Files.readAllBytes(path));
            parseYaml(content);
            log.info("Loaded YAML config from {}", path);
        } catch (IOException e) {
            log.error("Failed to load YAML from: {}", path, e);
        }
    }
    
    private void parseYaml(String content) {
        String[] lines = content.split("\n");
        String currentPrefix = "";
        int currentIndent = -1;
        
        for (String line : lines) {
            if (line.trim().isEmpty() || line.trim().startsWith("#")) {
                continue;
            }
            
            int indent = getIndentLevel(line);
            
            if (indent <= currentIndent) {
                currentPrefix = adjustPrefix(currentPrefix, indent);
            }
            
            currentIndent = indent;
            
            if (line.contains(":")) {
                String[] parts = line.split(":", 2);
                String key = parts[0].trim();
                String value = parts.length > 1 ? parts[1].trim() : "";
                
                String fullKey = currentPrefix.isEmpty() ? key : currentPrefix + "." + key;
                
                if (!value.isEmpty()) {
                    config.put(fullKey, parseValue(value));
                } else {
                    currentPrefix = fullKey;
                }
            }
        }
    }
    
    private int getIndentLevel(String line) {
        int count = 0;
        for (char c : line.toCharArray()) {
            if (c == ' ') count++;
            else if (c == '\t') count += 2;
            else break;
        }
        return count / 2;
    }
    
    private String adjustPrefix(String prefix, int targetLevel) {
        String[] parts = prefix.split("\\.");
        if (parts.length <= targetLevel) {
            return prefix;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < targetLevel && i < parts.length; i++) {
            if (i > 0) sb.append(".");
            sb.append(parts[i]);
        }
        return sb.toString();
    }
    
    private Object parseValue(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        
        if ("true".equalsIgnoreCase(value)) return true;
        if ("false".equalsIgnoreCase(value)) return false;
        
        try {
            if (value.contains(".")) {
                return Double.parseDouble(value);
            } else {
                return Long.parseLong(value);
            }
        } catch (NumberFormatException e) {
            return value;
        }
    }
    
    public String getString(String key) {
        Object value = config.get(key);
        return value != null ? value.toString() : null;
    }
    
    public String getString(String key, String defaultValue) {
        String value = getString(key);
        return value != null ? value : defaultValue;
    }
    
    public int getInt(String key) {
        Object value = config.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(value.toString());
        } catch (Exception e) {
            return 0;
        }
    }
    
    public int getInt(String key, int defaultValue) {
        Object value = config.get(key);
        if (value == null) return defaultValue;
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(value.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
    public long getLong(String key) {
        Object value = config.get(key);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        try {
            return Long.parseLong(value.toString());
        } catch (Exception e) {
            return 0L;
        }
    }
    
    public boolean getBoolean(String key) {
        Object value = config.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return Boolean.parseBoolean(value.toString());
    }
    
    public boolean getBoolean(String key, boolean defaultValue) {
        Object value = config.get(key);
        if (value == null) return defaultValue;
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return Boolean.parseBoolean(value.toString());
    }
    
    public Map<String, Object> getAll() {
        return new HashMap<>(config);
    }
    
    public void set(String key, Object value) {
        config.put(key, value);
    }
    
    public boolean has(String key) {
        return config.containsKey(key);
    }
    
    public void reload() {
        if (configPath != null) {
            config.clear();
            loadDefaults();
            load(configPath);
        }
    }
}

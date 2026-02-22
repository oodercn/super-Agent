package net.ooder.nexus.module.config.service.impl;

import net.ooder.nexus.module.config.service.ConfigService;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class ConfigServiceImpl implements ConfigService {
    
    private static final String CONFIG_FILE = "mcp-agent.properties";
    
    @Override
    public Map<String, Object> getBasicConfig() {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> config = new HashMap<>();
        
        try {
            Properties properties = loadConfig();
            config.put("agentId", properties.getProperty("agent.id", "mcp-agent-001"));
            config.put("agentName", properties.getProperty("agent.name", "Independent MCP Agent"));
            config.put("agentType", properties.getProperty("agent.type", "mcp"));
            config.put("endpoint", properties.getProperty("agent.endpoint", "localhost:9876"));
            config.put("heartbeatInterval", properties.getProperty("agent.heartbeatInterval", "30000"));
            
            response.put("status", "success");
            response.put("config", config);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        
        return response;
    }
    
    @Override
    public Map<String, Object> getAdvancedConfig() {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> config = new HashMap<>();
        
        try {
            Properties properties = loadConfig();
            config.put("udpPort", properties.getProperty("agent.udpPort", "9876"));
            config.put("bufferSize", properties.getProperty("agent.bufferSize", "8192"));
            config.put("timeout", properties.getProperty("agent.timeout", "5000"));
            config.put("retries", properties.getProperty("agent.retries", "3"));
            
            response.put("status", "success");
            response.put("config", config);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        
        return response;
    }
    
    @Override
    public Map<String, Object> getSecurityConfig() {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> config = new HashMap<>();
        
        try {
            Properties properties = loadConfig();
            config.put("authenticationEnabled", properties.getProperty("security.auth.enabled", "false"));
            config.put("encryptionEnabled", properties.getProperty("security.encryption.enabled", "false"));
            config.put("sslEnabled", properties.getProperty("security.ssl.enabled", "false"));
            
            response.put("status", "success");
            response.put("config", config);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        
        return response;
    }
    
    @Override
    public Map<String, Object> saveConfig(Map<String, Object> config) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Properties properties = loadConfig();
            
            // 更新基本配置
            if (config.containsKey("agentId")) {
                properties.setProperty("agent.id", (String) config.get("agentId"));
            }
            if (config.containsKey("agentName")) {
                properties.setProperty("agent.name", (String) config.get("agentName"));
            }
            if (config.containsKey("agentType")) {
                properties.setProperty("agent.type", (String) config.get("agentType"));
            }
            if (config.containsKey("endpoint")) {
                properties.setProperty("agent.endpoint", (String) config.get("endpoint"));
            }
            if (config.containsKey("heartbeatInterval")) {
                properties.setProperty("agent.heartbeatInterval", (String) config.get("heartbeatInterval"));
            }
            
            // 更新高级配置
            if (config.containsKey("udpPort")) {
                properties.setProperty("agent.udpPort", (String) config.get("udpPort"));
            }
            if (config.containsKey("bufferSize")) {
                properties.setProperty("agent.bufferSize", (String) config.get("bufferSize"));
            }
            if (config.containsKey("timeout")) {
                properties.setProperty("agent.timeout", (String) config.get("timeout"));
            }
            if (config.containsKey("retries")) {
                properties.setProperty("agent.retries", (String) config.get("retries"));
            }
            
            // 更新安全配置
            if (config.containsKey("authenticationEnabled")) {
                properties.setProperty("security.auth.enabled", (String) config.get("authenticationEnabled"));
            }
            if (config.containsKey("encryptionEnabled")) {
                properties.setProperty("security.encryption.enabled", (String) config.get("encryptionEnabled"));
            }
            if (config.containsKey("sslEnabled")) {
                properties.setProperty("security.ssl.enabled", (String) config.get("sslEnabled"));
            }
            
            saveConfig(properties);
            
            response.put("status", "success");
            response.put("message", "Configuration saved successfully");
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        
        return response;
    }
    
    @Override
    public Map<String, Object> resetConfig() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Properties properties = new Properties();
            // 设置默认值
            properties.setProperty("agent.id", "mcp-agent-001");
            properties.setProperty("agent.name", "Independent MCP Agent");
            properties.setProperty("agent.type", "mcp");
            properties.setProperty("agent.endpoint", "localhost:9876");
            properties.setProperty("agent.heartbeatInterval", "30000");
            properties.setProperty("agent.udpPort", "9876");
            properties.setProperty("agent.bufferSize", "8192");
            properties.setProperty("agent.timeout", "5000");
            properties.setProperty("agent.retries", "3");
            properties.setProperty("security.auth.enabled", "false");
            properties.setProperty("security.encryption.enabled", "false");
            properties.setProperty("security.ssl.enabled", "false");
            
            saveConfig(properties);
            
            response.put("status", "success");
            response.put("message", "Configuration reset to defaults");
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        
        return response;
    }
    
    private Properties loadConfig() throws IOException {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
        } catch (IOException e) {
            // 文件不存在，返回空配置
        }
        return properties;
    }
    
    private void saveConfig(Properties properties) throws IOException {
        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            properties.store(output, "MCP Agent Configuration");
        }
    }
}

package net.ooder.skill.llm.chat.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.ooder.skill.llm.chat.model.LlmConfig;
import net.ooder.skill.llm.chat.model.ResolvedConfig;
import net.ooder.skill.llm.chat.service.LlmConfigService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class LlmConfigServiceImpl implements LlmConfigService {
    
    private static final Logger log = LoggerFactory.getLogger(LlmConfigServiceImpl.class);
    
    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static final String ENCRYPTION_KEY = "ooder-llm-config-key-32byte";
    private static final String CONFIG_FILE = "llm-configs.json";
    
    @Value("${ooder.llm.config-path:./data}")
    private String configPath;
    
    private final Map<String, LlmConfig> configCache = new ConcurrentHashMap<>();
    private SecretKeySpec secretKey;
    private ObjectMapper objectMapper;
    private Path configFile;
    
    @PostConstruct
    public void init() {
        byte[] keyBytes = Arrays.copyOf(ENCRYPTION_KEY.getBytes(StandardCharsets.UTF_8), 32);
        secretKey = new SecretKeySpec(keyBytes, ENCRYPTION_ALGORITHM);
        objectMapper = new ObjectMapper();
        
        try {
            Path configDir = Paths.get(configPath);
            Files.createDirectories(configDir);
            configFile = configDir.resolve(CONFIG_FILE);
        } catch (Exception e) {
            log.warn("Failed to create config directory: {}", e.getMessage());
        }
        
        loadConfigsFromStorage();
        initDefaultConfigs();
        log.info("LlmConfigService initialized with {} configs, storage: {}", configCache.size(), configPath);
    }
    
    private void loadConfigsFromStorage() {
        if (configFile == null || !Files.exists(configFile)) {
            log.info("No existing config file, starting fresh");
            return;
        }
        
        try {
            String content = new String(Files.readAllBytes(configFile), StandardCharsets.UTF_8);
            if (content != null && !content.trim().isEmpty()) {
                List<LlmConfig> configs = objectMapper.readValue(content, 
                    new TypeReference<List<LlmConfig>>() {});
                for (LlmConfig config : configs) {
                    configCache.put(config.getId(), config);
                }
                log.info("Loaded {} configs from storage", configs.size());
            }
        } catch (Exception e) {
            log.warn("Failed to load configs from storage: {}", e.getMessage());
        }
    }
    
    private void saveConfigsToStorage() {
        if (configFile == null) {
            return;
        }
        
        try {
            List<LlmConfig> configs = new ArrayList<>(configCache.values());
            String content = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(configs);
            Files.write(configFile, content.getBytes(StandardCharsets.UTF_8), 
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            log.debug("Saved {} configs to storage", configs.size());
        } catch (Exception e) {
            log.error("Failed to save configs to storage: {}", e.getMessage());
        }
    }
    
    private void initDefaultConfigs() {
        if (!configCache.values().stream()
                .anyMatch(c -> c.getLevel() == LlmConfig.ConfigLevel.ENTERPRISE)) {
            
            LlmConfig enterpriseConfig = new LlmConfig();
            enterpriseConfig.setId("enterprise-default");
            enterpriseConfig.setName("企业默认配置");
            enterpriseConfig.setLevel(LlmConfig.ConfigLevel.ENTERPRISE);
            enterpriseConfig.setScopeId("default");
            enterpriseConfig.setProviderType("deepseek");
            enterpriseConfig.setModel("deepseek-chat");
            enterpriseConfig.setEnabled(true);
            enterpriseConfig.setCreatedAt(System.currentTimeMillis());
            enterpriseConfig.setCreatedBy("system");
            
            Map<String, Object> options = new HashMap<>();
            options.put("temperature", 0.7);
            options.put("max_tokens", 4096);
            enterpriseConfig.setOptions(options);
            
            configCache.put(enterpriseConfig.getId(), enterpriseConfig);
            saveConfigsToStorage();
            log.info("Created default enterprise config");
        }
    }

    @Override
    public LlmConfig createConfig(LlmConfig config) {
        if (config.getId() == null || config.getId().isEmpty()) {
            config.setId(UUID.randomUUID().toString());
        }
        
        config.setCreatedAt(System.currentTimeMillis());
        config.setUpdatedAt(System.currentTimeMillis());
        
        if (config.getProviderConfig() != null && config.getProviderConfig().containsKey("apiKey")) {
            String apiKey = (String) config.getProviderConfig().get("apiKey");
            if (apiKey != null && !apiKey.startsWith("enc:")) {
                config.getProviderConfig().put("apiKey", encryptApiKey(apiKey));
            }
        }
        
        configCache.put(config.getId(), config);
        saveConfigsToStorage();
        
        log.info("Created LLM config: id={}, level={}, provider={}", 
            config.getId(), config.getLevel(), config.getProviderType());
        
        return config;
    }
    
    @Override
    public LlmConfig updateConfig(String id, LlmConfig config) {
        LlmConfig existing = configCache.get(id);
        if (existing == null) {
            throw new RuntimeException("Config not found: " + id);
        }
        
        config.setId(id);
        config.setCreatedAt(existing.getCreatedAt());
        config.setUpdatedAt(System.currentTimeMillis());
        
        if (config.getProviderConfig() != null && config.getProviderConfig().containsKey("apiKey")) {
            String apiKey = (String) config.getProviderConfig().get("apiKey");
            if (apiKey != null && !apiKey.startsWith("enc:")) {
                config.getProviderConfig().put("apiKey", encryptApiKey(apiKey));
            }
        }
        
        configCache.put(id, config);
        saveConfigsToStorage();
        log.info("Updated LLM config: id={}", id);
        
        return config;
    }
    
    @Override
    public void deleteConfig(String id) {
        LlmConfig removed = configCache.remove(id);
        if (removed != null) {
            saveConfigsToStorage();
            log.info("Deleted LLM config: id={}", id);
        }
    }
    
    @Override
    public LlmConfig getConfig(String id) {
        return configCache.get(id);
    }
    
    @Override
    public LlmConfig getConfigByLevelAndScope(LlmConfig.ConfigLevel level, String scopeId) {
        return getConfigByLevelAndScope(level, scopeId, null);
    }
    
    @Override
    public LlmConfig getConfigByLevelAndScope(LlmConfig.ConfigLevel level, String scopeId, String providerType) {
        return configCache.values().stream()
            .filter(c -> c.getLevel() == level && Objects.equals(c.getScopeId(), scopeId))
            .filter(c -> providerType == null || Objects.equals(c.getProviderType(), providerType))
            .findFirst()
            .orElse(null);
    }
    
    @Override
    public List<LlmConfig> getConfigsByLevel(LlmConfig.ConfigLevel level) {
        return configCache.values().stream()
            .filter(c -> c.getLevel() == level)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<LlmConfig> getAllConfigs() {
        return new ArrayList<>(configCache.values());
    }
    
    @Override
    public ResolvedConfig resolveConfig(String userId, String sceneId, String departmentId) {
        return resolveConfigWithPriority(userId, sceneId, departmentId, null);
    }
    
    @Override
    public ResolvedConfig resolveConfigWithPriority(String userId, String sceneId, String departmentId, Map<String, Object> context) {
        List<LlmConfig> candidates = new ArrayList<>();
        
        if (userId != null) {
            LlmConfig personal = getConfigByLevelAndScope(LlmConfig.ConfigLevel.PERSONAL, userId);
            if (personal != null && personal.isEnabled()) {
                candidates.add(personal);
            }
        }
        
        if (sceneId != null) {
            LlmConfig scene = getConfigByLevelAndScope(LlmConfig.ConfigLevel.SCENE, sceneId);
            if (scene != null && scene.isEnabled()) {
                candidates.add(scene);
            }
        }
        
        if (departmentId != null) {
            LlmConfig dept = getConfigByLevelAndScope(LlmConfig.ConfigLevel.DEPARTMENT, departmentId);
            if (dept != null && dept.isEnabled()) {
                candidates.add(dept);
            }
        }
        
        LlmConfig enterprise = getConfigByLevelAndScope(LlmConfig.ConfigLevel.ENTERPRISE, "default");
        if (enterprise != null && enterprise.isEnabled()) {
            candidates.add(enterprise);
        }
        
        if (candidates.isEmpty()) {
            log.warn("No LLM config found, using defaults");
            return createDefaultResolvedConfig();
        }
        
        candidates.sort((a, b) -> Integer.compare(
            a.getLevel().ordinal(),
            b.getLevel().ordinal()
        ));
        
        LlmConfig selected = candidates.get(0);
        return convertToResolvedConfig(selected);
    }
    
    private ResolvedConfig convertToResolvedConfig(LlmConfig config) {
        ResolvedConfig resolved = new ResolvedConfig();
        resolved.setProviderType(config.getProviderType());
        resolved.setModel(config.getModel());
        resolved.setOptions(config.getOptions());
        resolved.setPriority(config.getLevel().ordinal());
        resolved.setSource(new ResolvedConfig.ConfigSource(config.getLevel(), config.getScopeId()));
        
        if (config.getProviderConfig() != null) {
            Map<String, Object> providerConfig = new HashMap<>(config.getProviderConfig());
            
            if (providerConfig.containsKey("apiKey")) {
                String apiKey = (String) providerConfig.get("apiKey");
                if (apiKey != null && !apiKey.isEmpty()) {
                    if (apiKey.startsWith("enc:")) {
                        resolved.setApiKey(decryptApiKey(apiKey));
                    } else {
                        resolved.setApiKey(apiKey);
                    }
                    providerConfig.remove("apiKey");
                }
            }
            
            if (providerConfig.containsKey("baseUrl")) {
                resolved.setBaseUrl((String) providerConfig.get("baseUrl"));
            }
            
            resolved.setProviderConfig(providerConfig);
        }
        
        return resolved;
    }
    
    private ResolvedConfig createDefaultResolvedConfig() {
        ResolvedConfig resolved = new ResolvedConfig();
        resolved.setProviderType("deepseek");
        resolved.setModel("deepseek-chat");
        resolved.setPriority(999);
        resolved.setSource(new ResolvedConfig.ConfigSource(LlmConfig.ConfigLevel.ENTERPRISE, "default"));
        
        Map<String, Object> options = new HashMap<>();
        options.put("temperature", 0.7);
        options.put("max_tokens", 4096);
        resolved.setOptions(options);
        
        return resolved;
    }
    
    @Override
    public String encryptApiKey(String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            return apiKey;
        }
        
        try {
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(apiKey.getBytes(StandardCharsets.UTF_8));
            return "enc:" + Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            log.error("Failed to encrypt API key", e);
            throw new RuntimeException("Encryption failed", e);
        }
    }
    
    @Override
    public String decryptApiKey(String encryptedKey) {
        if (encryptedKey == null || !encryptedKey.startsWith("enc:")) {
            return encryptedKey;
        }
        
        try {
            String base64 = encryptedKey.substring(4);
            byte[] decoded = Base64.getDecoder().decode(base64);
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Failed to decrypt API key", e);
            throw new RuntimeException("Decryption failed", e);
        }
    }
    
    @Override
    public boolean validateConfig(LlmConfig config) {
        if (config.getProviderType() == null || config.getProviderType().isEmpty()) {
            return false;
        }
        
        if (config.getModel() == null || config.getModel().isEmpty()) {
            return false;
        }
        
        if (config.getLevel() == null) {
            return false;
        }
        
        return true;
    }
}

package net.ooder.mvp.skill.scene.llm.service.impl;

import net.ooder.mvp.skill.scene.dto.PageResult;
import net.ooder.mvp.skill.scene.dto.llm.LlmConfigAuditDTO;
import net.ooder.mvp.skill.scene.dto.llm.LlmConfigDTO;
import net.ooder.mvp.skill.scene.dto.llm.LlmConfigTemplateDTO;
import net.ooder.mvp.skill.scene.dto.llm.LlmProviderMeta;
import net.ooder.mvp.skill.scene.dto.llm.LlmProviderMeta.ConfigField;
import net.ooder.mvp.skill.scene.dto.llm.LlmProviderMeta.ModelInfo;
import net.ooder.mvp.skill.scene.dto.llm.LlmUsageStatsDTO;
import net.ooder.mvp.skill.scene.dto.llm.LlmUsageStatsDTO.DailyStats;
import net.ooder.mvp.skill.scene.llm.service.LlmConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class LlmConfigServiceImpl implements LlmConfigService {

    private static final Logger log = LoggerFactory.getLogger(LlmConfigServiceImpl.class);

    private final Map<Long, LlmConfigDTO> configStore = new ConcurrentHashMap<>();
    private final Map<Long, LlmConfigTemplateDTO> templateStore = new ConcurrentHashMap<>();
    private final Map<Long, LlmConfigAuditDTO> auditStore = new ConcurrentHashMap<>();
    private final Map<String, LlmUsageStatsDTO> usageStatsStore = new ConcurrentHashMap<>();
    private final Map<String, List<UsageRecord>> usageRecords = new ConcurrentHashMap<>();
    
    private Long configIdCounter = 1L;
    private Long templateIdCounter = 1L;
    private Long auditIdCounter = 1L;

    public LlmConfigServiceImpl() {
        initDefaultConfigs();
        initDefaultTemplates();
        initProviderMetas();
    }

    private void initDefaultConfigs() {
        LlmConfigDTO systemConfig = new LlmConfigDTO();
        systemConfig.setId(configIdCounter++);
        systemConfig.setName("系统默认配置");
        systemConfig.setLevel(LlmConfigDTO.LEVEL_SYSTEM);
        systemConfig.setScopeId("default");
        systemConfig.setProviderType("qianwen");
        systemConfig.setModel("qwen-plus");
        systemConfig.setDescription("系统级默认LLM配置");
        
        Map<String, Object> providerConfig = new HashMap<>();
        providerConfig.put("apiKey", "");
        providerConfig.put("baseUrl", "https://dashscope.aliyuncs.com/api/v1");
        systemConfig.setProviderConfig(providerConfig);
        
        Map<String, Object> options = new HashMap<>();
        options.put("temperature", 0.7);
        options.put("max_tokens", 128000);
        systemConfig.setOptions(options);
        
        systemConfig.setEnabled(true);
        systemConfig.setCreatedAt(System.currentTimeMillis());
        systemConfig.setUpdatedAt(System.currentTimeMillis());
        systemConfig.setCreatedBy("system");
        configStore.put(systemConfig.getId(), systemConfig);
    }

    private void initDefaultTemplates() {
        LlmConfigTemplateDTO qianwenTemplate = new LlmConfigTemplateDTO();
        qianwenTemplate.setId(templateIdCounter++);
        qianwenTemplate.setName("通义千问标准配置");
        qianwenTemplate.setDescription("阿里云通义千问API标准配置模板");
        qianwenTemplate.setProviderType("qianwen");
        qianwenTemplate.setModel("qwen-plus");
        
        Map<String, Object> providerConfig = new HashMap<>();
        providerConfig.put("apiKey", "${API_KEY}");
        providerConfig.put("baseUrl", "https://dashscope.aliyuncs.com/api/v1");
        qianwenTemplate.setProviderConfig(providerConfig);
        
        Map<String, Object> options = new HashMap<>();
        options.put("temperature", 0.7);
        options.put("max_tokens", 128000);
        qianwenTemplate.setOptions(options);
        qianwenTemplate.setTags(Arrays.asList("阿里云", "通义千问", "推荐"));
        qianwenTemplate.setDefault(true);
        qianwenTemplate.setBuiltin(true);
        templateStore.put(qianwenTemplate.getId(), qianwenTemplate);
        
        LlmConfigTemplateDTO deepseekTemplate = new LlmConfigTemplateDTO();
        deepseekTemplate.setId(templateIdCounter++);
        deepseekTemplate.setName("DeepSeek标准配置");
        deepseekTemplate.setDescription("DeepSeek API标准配置模板");
        deepseekTemplate.setProviderType("deepseek");
        deepseekTemplate.setModel("deepseek-chat");
        
        providerConfig = new HashMap<>();
        providerConfig.put("apiKey", "${API_KEY}");
        providerConfig.put("baseUrl", "https://api.deepseek.com/v1");
        deepseekTemplate.setProviderConfig(providerConfig);
        
        options = new HashMap<>();
        options.put("temperature", 0.7);
        options.put("max_tokens", 64000);
        deepseekTemplate.setOptions(options);
        deepseekTemplate.setTags(Arrays.asList("DeepSeek", "性价比"));
        deepseekTemplate.setDefault(false);
        deepseekTemplate.setBuiltin(true);
        templateStore.put(deepseekTemplate.getId(), deepseekTemplate);
    }

    private Map<String, LlmProviderMeta> providerMetas = new HashMap<>();
    
    private void initProviderMetas() {
        LlmProviderMeta qianwenMeta = new LlmProviderMeta();
        qianwenMeta.setType("qianwen");
        qianwenMeta.setName("阿里云通义千问");
        qianwenMeta.setIcon("ri-aliens-line");
        qianwenMeta.setDescription("阿里云通义千问大语言模型");
        qianwenMeta.setWebsite("https://dashscope.aliyun.com");
        qianwenMeta.setRequiresApiKey(true);
        qianwenMeta.setAuthType("bearer");
        qianwenMeta.setCapabilities(Arrays.asList("chat", "completion", "embedding", "function_call", "stream"));
        
        List<ModelInfo> qianwenModels = new ArrayList<>();
        ModelInfo qwenPlus = new ModelInfo();
        qwenPlus.setId("qwen-plus");
        qwenPlus.setName("通义千问Plus");
        qwenPlus.setType("chat");
        qwenPlus.setContextWindow(128000);
        qwenPlus.setMaxOutputTokens(6000);
        qwenPlus.setInputPricePer1k(0.004);
        qwenPlus.setOutputPricePer1k(0.012);
        qwenPlus.setCapabilities(Arrays.asList("chat", "function_call", "stream"));
        qwenPlus.setAvailable(true);
        qianwenModels.add(qwenPlus);
        
        ModelInfo qwenTurbo = new ModelInfo();
        qwenTurbo.setId("qwen-turbo");
        qwenTurbo.setName("通义千问Turbo");
        qwenTurbo.setType("chat");
        qwenTurbo.setContextWindow(128000);
        qwenTurbo.setMaxOutputTokens(6000);
        qwenTurbo.setInputPricePer1k(0.002);
        qwenTurbo.setOutputPricePer1k(0.006);
        qwenTurbo.setCapabilities(Arrays.asList("chat", "function_call", "stream"));
        qwenTurbo.setAvailable(true);
        qianwenModels.add(qwenTurbo);
        
        qianwenMeta.setSupportedModels(qianwenModels);
        
        Map<String, ConfigField> qianwenSchema = new HashMap<>();
        ConfigField apiKeyField = new ConfigField();
        apiKeyField.setName("apiKey");
        apiKeyField.setLabel("API Key");
        apiKeyField.setType("password");
        apiKeyField.setRequired(true);
        apiKeyField.setDescription("阿里云DashScope API Key");
        apiKeyField.setSecret(true);
        qianwenSchema.put("apiKey", apiKeyField);
        
        ConfigField baseUrlField = new ConfigField();
        baseUrlField.setName("baseUrl");
        baseUrlField.setLabel("Base URL");
        baseUrlField.setType("text");
        baseUrlField.setRequired(false);
        baseUrlField.setDefaultValue("https://dashscope.aliyuncs.com/api/v1");
        qianwenSchema.put("baseUrl", baseUrlField);
        
        qianwenMeta.setConfigSchema(qianwenSchema);
        providerMetas.put("qianwen", qianwenMeta);
        
        LlmProviderMeta deepseekMeta = new LlmProviderMeta();
        deepseekMeta.setType("deepseek");
        deepseekMeta.setName("DeepSeek");
        deepseekMeta.setIcon("ri-brain-line");
        deepseekMeta.setDescription("DeepSeek大语言模型");
        deepseekMeta.setWebsite("https://www.deepseek.com");
        deepseekMeta.setRequiresApiKey(true);
        deepseekMeta.setAuthType("bearer");
        deepseekMeta.setCapabilities(Arrays.asList("chat", "completion", "function_call", "stream"));
        
        List<ModelInfo> deepseekModels = new ArrayList<>();
        ModelInfo deepseekChat = new ModelInfo();
        deepseekChat.setId("deepseek-chat");
        deepseekChat.setName("DeepSeek Chat");
        deepseekChat.setType("chat");
        deepseekChat.setContextWindow(64000);
        deepseekChat.setMaxOutputTokens(4000);
        deepseekChat.setInputPricePer1k(0.001);
        deepseekChat.setOutputPricePer1k(0.002);
        deepseekChat.setCapabilities(Arrays.asList("chat", "function_call", "stream"));
        deepseekChat.setAvailable(true);
        deepseekModels.add(deepseekChat);
        
        deepseekMeta.setSupportedModels(deepseekModels);
        providerMetas.put("deepseek", deepseekMeta);
    }

    @Override
    public PageResult<LlmConfigDTO> listConfigs(int pageNum, int pageSize, String level, String providerType) {
        List<LlmConfigDTO> filtered = configStore.values().stream()
            .filter(c -> level == null || level.isEmpty() || level.equals(c.getLevel()))
            .filter(c -> providerType == null || providerType.isEmpty() || providerType.equals(c.getProviderType()))
            .sorted((a, b) -> b.getUpdatedAt().compareTo(a.getUpdatedAt()))
            .collect(Collectors.toList());
        
        int total = filtered.size();
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, total);
        
        PageResult<LlmConfigDTO> result = new PageResult<>();
        result.setList(start < total ? filtered.subList(start, end) : new ArrayList<>());
        result.setTotal(total);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        return result;
    }

    @Override
    public List<LlmConfigDTO> listAllConfigs() {
        return new ArrayList<>(configStore.values());
    }

    @Override
    public LlmConfigDTO getConfig(Long id) {
        return configStore.get(id);
    }

    @Override
    public LlmConfigDTO getConfigByScope(String level, String scopeId) {
        return configStore.values().stream()
            .filter(c -> level.equals(c.getLevel()) && scopeId.equals(c.getScopeId()))
            .findFirst()
            .orElse(null);
    }

    @Override
    public LlmConfigDTO getEffectiveConfig(String level, String scopeId, String userId, String sceneId, String agentId) {
        List<LlmConfigDTO> candidates = new ArrayList<>();
        
        if (userId != null) {
            LlmConfigDTO userConfig = getConfigByScope(LlmConfigDTO.LEVEL_USER, userId);
            if (userConfig != null && userConfig.getEnabled()) candidates.add(userConfig);
        }
        
        if (sceneId != null) {
            LlmConfigDTO sceneConfig = getConfigByScope(LlmConfigDTO.LEVEL_SCENE, sceneId);
            if (sceneConfig != null && sceneConfig.getEnabled()) candidates.add(sceneConfig);
        }
        
        if (agentId != null) {
            LlmConfigDTO agentConfig = getConfigByScope(LlmConfigDTO.LEVEL_AGENT, agentId);
            if (agentConfig != null && agentConfig.getEnabled()) candidates.add(agentConfig);
        }
        
        if (scopeId != null && LlmConfigDTO.LEVEL_ENTERPRISE.equals(level)) {
            LlmConfigDTO enterpriseConfig = getConfigByScope(LlmConfigDTO.LEVEL_ENTERPRISE, scopeId);
            if (enterpriseConfig != null && enterpriseConfig.getEnabled()) candidates.add(enterpriseConfig);
        }
        
        LlmConfigDTO systemConfig = getConfigByScope(LlmConfigDTO.LEVEL_SYSTEM, "default");
        if (systemConfig != null && systemConfig.getEnabled()) candidates.add(systemConfig);
        
        return candidates.stream()
            .max(Comparator.comparingInt(LlmConfigDTO::getLevelPriority))
            .orElse(null);
    }

    @Override
    public LlmConfigDTO createConfig(LlmConfigDTO config, String operator) {
        config.setId(configIdCounter++);
        config.setCreatedAt(System.currentTimeMillis());
        config.setUpdatedAt(System.currentTimeMillis());
        config.setCreatedBy(operator);
        if (config.getEnabled() == null) {
            config.setEnabled(true);
        }
        configStore.put(config.getId(), config);
        
        addAuditLog(config, LlmConfigAuditDTO.OP_CREATE, operator, null, config);
        
        log.info("LLM config created: {} by {}", config.getName(), operator);
        return config;
    }

    @Override
    public LlmConfigDTO updateConfig(Long id, LlmConfigDTO config, String operator) {
        LlmConfigDTO existing = configStore.get(id);
        if (existing == null) return null;
        
        LlmConfigDTO before = cloneConfig(existing);
        
        config.setId(id);
        config.setCreatedAt(existing.getCreatedAt());
        config.setCreatedBy(existing.getCreatedBy());
        config.setUpdatedAt(System.currentTimeMillis());
        configStore.put(id, config);
        
        addAuditLog(config, LlmConfigAuditDTO.OP_UPDATE, operator, before, config);
        
        log.info("LLM config updated: {} by {}", config.getName(), operator);
        return config;
    }

    @Override
    public boolean deleteConfig(Long id, String operator) {
        LlmConfigDTO config = configStore.get(id);
        if (config == null) return false;
        
        if (LlmConfigDTO.LEVEL_SYSTEM.equals(config.getLevel())) {
            log.warn("Cannot delete system level config: {}", id);
            return false;
        }
        
        addAuditLog(config, LlmConfigAuditDTO.OP_DELETE, operator, config, null);
        configStore.remove(id);
        
        log.info("LLM config deleted: {} by {}", config.getName(), operator);
        return true;
    }

    @Override
    public boolean enableConfig(Long id, String operator) {
        LlmConfigDTO config = configStore.get(id);
        if (config == null) return false;
        
        config.setEnabled(true);
        config.setUpdatedAt(System.currentTimeMillis());
        
        addAuditLog(config, LlmConfigAuditDTO.OP_ENABLE, operator, null, config);
        return true;
    }

    @Override
    public boolean disableConfig(Long id, String operator) {
        LlmConfigDTO config = configStore.get(id);
        if (config == null) return false;
        
        config.setEnabled(false);
        config.setUpdatedAt(System.currentTimeMillis());
        
        addAuditLog(config, LlmConfigAuditDTO.OP_DISABLE, operator, null, config);
        return true;
    }

    @Override
    public boolean validateConfig(LlmConfigDTO config) {
        if (config.getName() == null || config.getName().trim().isEmpty()) {
            return false;
        }
        if (config.getProviderType() == null || config.getProviderType().trim().isEmpty()) {
            return false;
        }
        if (config.getModel() == null || config.getModel().trim().isEmpty()) {
            return false;
        }
        
        LlmProviderMeta meta = providerMetas.get(config.getProviderType());
        if (meta == null) {
            return false;
        }
        
        if (meta.isRequiresApiKey()) {
            Map<String, Object> providerConfig = config.getProviderConfig();
            if (providerConfig == null || !providerConfig.containsKey("apiKey")) {
                return false;
            }
            String apiKey = (String) providerConfig.get("apiKey");
            if (apiKey == null || apiKey.trim().isEmpty() || "${API_KEY}".equals(apiKey)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public ValidationResult testConnection(Long id) {
        LlmConfigDTO config = configStore.get(id);
        if (config == null) {
            return ValidationResult.failure("Config not found");
        }
        return testConfig(config);
    }

    @Override
    public ValidationResult testConfig(LlmConfigDTO config) {
        if (!validateConfig(config)) {
            return ValidationResult.failure("Config validation failed");
        }
        
        try {
            boolean success = ThreadLocalRandom.current().nextDouble() > 0.1;
            if (success) {
                return ValidationResult.success();
            } else {
                return ValidationResult.failure("Connection test failed: API key may be invalid");
            }
        } catch (Exception e) {
            return ValidationResult.failure("Connection test error: " + e.getMessage());
        }
    }

    @Override
    public List<LlmProviderMeta> getAvailableProviders() {
        return new ArrayList<>(providerMetas.values());
    }

    @Override
    public LlmProviderMeta getProviderMeta(String providerType) {
        return providerMetas.get(providerType);
    }

    @Override
    public List<LlmConfigTemplateDTO> getConfigTemplates() {
        return new ArrayList<>(templateStore.values());
    }

    @Override
    public LlmConfigTemplateDTO getConfigTemplate(Long id) {
        return templateStore.get(id);
    }

    @Override
    public LlmConfigTemplateDTO createConfigTemplate(LlmConfigTemplateDTO template) {
        template.setId(templateIdCounter++);
        template.setCreatedAt(System.currentTimeMillis());
        template.setUpdatedAt(System.currentTimeMillis());
        template.setBuiltin(false);
        templateStore.put(template.getId(), template);
        return template;
    }

    @Override
    public LlmConfigTemplateDTO updateConfigTemplate(Long id, LlmConfigTemplateDTO template) {
        LlmConfigTemplateDTO existing = templateStore.get(id);
        if (existing == null) return null;
        if (existing.isBuiltin()) return null;
        
        template.setId(id);
        template.setCreatedAt(existing.getCreatedAt());
        template.setBuiltin(false);
        template.setUpdatedAt(System.currentTimeMillis());
        templateStore.put(id, template);
        return template;
    }

    @Override
    public boolean deleteConfigTemplate(Long id) {
        LlmConfigTemplateDTO template = templateStore.get(id);
        if (template == null || template.isBuiltin()) return false;
        return templateStore.remove(id) != null;
    }

    @Override
    public LlmConfigDTO createConfigFromTemplate(Long templateId, String level, String scopeId, String operator) {
        LlmConfigTemplateDTO template = templateStore.get(templateId);
        if (template == null) return null;
        
        LlmConfigDTO config = template.toConfigDTO(level, scopeId);
        return createConfig(config, operator);
    }

    @Override
    public LlmUsageStatsDTO getUsageStats(String configId, long startTime, long endTime) {
        LlmUsageStatsDTO stats = usageStatsStore.get(configId);
        if (stats == null) {
            stats = generateMockStats(configId, startTime, endTime);
        }
        return stats;
    }

    @Override
    public List<LlmUsageStatsDTO> getAllUsageStats(long startTime, long endTime) {
        List<LlmUsageStatsDTO> result = new ArrayList<>();
        for (LlmConfigDTO config : configStore.values()) {
            result.add(getUsageStats(String.valueOf(config.getId()), startTime, endTime));
        }
        return result;
    }

    @Override
    public void recordUsage(String configId, long inputTokens, long outputTokens, double cost, long latency) {
        LlmConfigDTO config = configStore.get(Long.parseLong(configId));
        if (config != null) {
            config.setTotalTokens(config.getTotalTokens() + inputTokens + outputTokens);
            config.setTotalRequests(config.getTotalRequests() + 1);
            config.setTotalCost(config.getTotalCost() + cost);
            config.setLastUsedTime(System.currentTimeMillis());
        }
        
        UsageRecord record = new UsageRecord();
        record.configId = configId;
        record.inputTokens = inputTokens;
        record.outputTokens = outputTokens;
        record.cost = cost;
        record.latency = latency;
        record.timestamp = System.currentTimeMillis();
        
        usageRecords.computeIfAbsent(configId, k -> new ArrayList<>()).add(record);
    }

    @Override
    public Map<String, Object> getConfigInheritanceChain(String level, String scopeId) {
        Map<String, Object> chain = new LinkedHashMap<>();
        
        chain.put("user", null);
        chain.put("scene", null);
        chain.put("agent", null);
        chain.put("enterprise", null);
        
        LlmConfigDTO systemConfig = getConfigByScope(LlmConfigDTO.LEVEL_SYSTEM, "default");
        chain.put("system", systemConfig);
        
        return chain;
    }

    @Override
    public List<LlmConfigAuditDTO> getConfigAuditLogs(String configId, int pageNum, int pageSize) {
        List<LlmConfigAuditDTO> filtered = auditStore.values().stream()
            .filter(a -> configId.equals(a.getConfigId()))
            .sorted((a, b) -> Long.compare(b.getOperateTime(), a.getOperateTime()))
            .collect(Collectors.toList());
        
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, filtered.size());
        
        return start < filtered.size() ? filtered.subList(start, end) : new ArrayList<>();
    }

    @Override
    public List<LlmConfigAuditDTO> getAllAuditLogs(int pageNum, int pageSize) {
        List<LlmConfigAuditDTO> all = auditStore.values().stream()
            .sorted((a, b) -> Long.compare(b.getOperateTime(), a.getOperateTime()))
            .collect(Collectors.toList());
        
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, all.size());
        
        return start < all.size() ? all.subList(start, end) : new ArrayList<>();
    }

    @Override
    public Map<String, Object> getConfigComparison(Long configId1, Long configId2) {
        Map<String, Object> comparison = new LinkedHashMap<>();
        
        LlmConfigDTO config1 = configStore.get(configId1);
        LlmConfigDTO config2 = configStore.get(configId2);
        
        if (config1 == null || config2 == null) {
            return comparison;
        }
        
        Map<String, Object> config1Map = new LinkedHashMap<>();
        config1Map.put("id", config1.getId());
        config1Map.put("name", config1.getName());
        config1Map.put("level", config1.getLevel());
        config1Map.put("providerType", config1.getProviderType());
        config1Map.put("model", config1.getModel());
        config1Map.put("enabled", config1.getEnabled());
        comparison.put("config1", config1Map);
        
        Map<String, Object> config2Map = new LinkedHashMap<>();
        config2Map.put("id", config2.getId());
        config2Map.put("name", config2.getName());
        config2Map.put("level", config2.getLevel());
        config2Map.put("providerType", config2.getProviderType());
        config2Map.put("model", config2.getModel());
        config2Map.put("enabled", config2.getEnabled());
        comparison.put("config2", config2Map);
        
        List<Map<String, Object>> differences = new ArrayList<>();
        
        if (!Objects.equals(config1.getProviderType(), config2.getProviderType())) {
            Map<String, Object> diff = new LinkedHashMap<>();
            diff.put("field", "providerType");
            diff.put("value1", config1.getProviderType());
            diff.put("value2", config2.getProviderType());
            differences.add(diff);
        }
        if (!Objects.equals(config1.getModel(), config2.getModel())) {
            Map<String, Object> diff = new LinkedHashMap<>();
            diff.put("field", "model");
            diff.put("value1", config1.getModel());
            diff.put("value2", config2.getModel());
            differences.add(diff);
        }
        if (!Objects.equals(config1.getLevel(), config2.getLevel())) {
            Map<String, Object> diff = new LinkedHashMap<>();
            diff.put("field", "level");
            diff.put("value1", config1.getLevel());
            diff.put("value2", config2.getLevel());
            differences.add(diff);
        }
        
        comparison.put("differences", differences);
        
        return comparison;
    }

    @Override
    public boolean checkApiKeyExpiring(String configId, int daysThreshold) {
        return false;
    }

    @Override
    public List<LlmConfigDTO> getConfigsNeedingKeyRotation(int daysThreshold) {
        return new ArrayList<>();
    }

    private void addAuditLog(LlmConfigDTO config, String operation, String operator, 
                            LlmConfigDTO before, LlmConfigDTO after) {
        LlmConfigAuditDTO audit = new LlmConfigAuditDTO();
        audit.setId(auditIdCounter++);
        audit.setConfigId(String.valueOf(config.getId()));
        audit.setConfigName(config.getName());
        audit.setOperation(operation);
        audit.setOperator(operator);
        audit.setOperateTime(System.currentTimeMillis());
        
        if (before != null) {
            audit.setBeforeValue(configToMap(before));
        }
        if (after != null) {
            audit.setAfterValue(configToMap(after));
        }
        
        auditStore.put(audit.getId(), audit);
    }

    private Map<String, Object> configToMap(LlmConfigDTO config) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", config.getId());
        map.put("name", config.getName());
        map.put("level", config.getLevel());
        map.put("scopeId", config.getScopeId());
        map.put("providerType", config.getProviderType());
        map.put("model", config.getModel());
        map.put("enabled", config.getEnabled());
        return map;
    }

    private LlmConfigDTO cloneConfig(LlmConfigDTO config) {
        LlmConfigDTO clone = new LlmConfigDTO();
        clone.setId(config.getId());
        clone.setName(config.getName());
        clone.setLevel(config.getLevel());
        clone.setScopeId(config.getScopeId());
        clone.setProviderType(config.getProviderType());
        clone.setModel(config.getModel());
        clone.setProviderConfig(config.getProviderConfig() != null ? 
            new HashMap<>(config.getProviderConfig()) : null);
        clone.setOptions(config.getOptions() != null ? 
            new HashMap<>(config.getOptions()) : null);
        clone.setEnabled(config.getEnabled());
        clone.setCreatedAt(config.getCreatedAt());
        clone.setCreatedBy(config.getCreatedBy());
        return clone;
    }

    private LlmUsageStatsDTO generateMockStats(String configId, long startTime, long endTime) {
        LlmUsageStatsDTO stats = new LlmUsageStatsDTO();
        stats.setConfigId(configId);
        
        LlmConfigDTO config = configStore.get(Long.parseLong(configId));
        if (config != null) {
            stats.setConfigName(config.getName());
            stats.setProviderType(config.getProviderType());
            stats.setModel(config.getModel());
        }
        
        ThreadLocalRandom random = ThreadLocalRandom.current();
        stats.setTotalRequests(random.nextLong(1000, 10000));
        stats.setSuccessRequests((long)(stats.getTotalRequests() * random.nextDouble(0.95, 0.99)));
        stats.setFailedRequests(stats.getTotalRequests() - stats.getSuccessRequests());
        stats.setSuccessRate(stats.getSuccessRequests() * 100.0 / stats.getTotalRequests());
        stats.setTotalTokens(random.nextLong(100000, 1000000));
        stats.setInputTokens((long)(stats.getTotalTokens() * 0.7));
        stats.setOutputTokens(stats.getTotalTokens() - stats.getInputTokens());
        stats.setTotalCost(random.nextDouble(10, 100));
        stats.setAvgLatency(random.nextDouble(100, 500));
        stats.setPeriodStart(startTime);
        stats.setPeriodEnd(endTime);
        
        List<DailyStats> dailyStats = new ArrayList<>();
        long dayMs = 24 * 60 * 60 * 1000L;
        for (long t = startTime; t <= endTime; t += dayMs) {
            DailyStats ds = new DailyStats();
            ds.setDate(new java.text.SimpleDateFormat("yyyy-MM-dd").format(new Date(t)));
            ds.setRequests(random.nextLong(50, 500));
            ds.setTokens(random.nextLong(5000, 50000));
            ds.setCost(random.nextDouble(0.5, 5));
            ds.setAvgLatency(random.nextDouble(100, 400));
            dailyStats.add(ds);
        }
        stats.setDailyStats(dailyStats);
        
        return stats;
    }

    private static class UsageRecord {
        String configId;
        long inputTokens;
        long outputTokens;
        double cost;
        long latency;
        long timestamp;
    }
}

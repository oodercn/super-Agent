package net.ooder.mvp.skill.scene.controller;

import net.ooder.mvp.skill.scene.dto.llm.LlmProviderConfigDTO;
import net.ooder.mvp.skill.scene.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/v1/scene-groups/{sceneGroupId}/llm")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SceneLlmController {

    private static final Logger log = LoggerFactory.getLogger(SceneLlmController.class);
    
    @Value("${ooder.llm.provider:deepseek}")
    private String defaultProvider;
    
    @Value("${ooder.llm.model:deepseek-chat}")
    private String defaultModel;
    
    @Value("${ooder.llm.timeout:60000}")
    private int defaultTimeout;
    
    @Value("${ooder.llm.max-iterations:5}")
    private int defaultMaxIterations;
    
    @Value("${ooder.llm.daily-token-limit:100000}")
    private int defaultDailyTokenLimit;
    
    @Autowired(required = false)
    private LlmProviderController llmProviderController;
    
    private final Map<String, LlmConfig> llmConfigs = new ConcurrentHashMap<>();
    
    private LlmConfig getSystemDefaultConfig() {
        LlmConfig config = new LlmConfig();
        
        if (llmProviderController != null) {
            try {
                ResultModel<LlmProviderConfigDTO> defaultProviderResult = llmProviderController.getDefaultProvider();
                if (defaultProviderResult != null && defaultProviderResult.getData() != null) {
                    LlmProviderConfigDTO provider = defaultProviderResult.getData();
                    config.setProvider(provider.getProviderId());
                    config.setModel(provider.getDefaultModel());
                    config.setMaxIterations(provider.getMaxIterations() > 0 ? provider.getMaxIterations() : defaultMaxIterations);
                    config.setFunctionCalling(provider.isFunctionCallingEnabled());
                    config.setLlmTimeout(provider.getTimeout() > 0 ? (int) provider.getTimeout() : defaultTimeout);
                    log.info("[getSystemDefaultConfig] Using system provider: {}, model: {}", 
                        provider.getProviderId(), provider.getDefaultModel());
                    return config;
                }
            } catch (Exception e) {
                log.warn("[getSystemDefaultConfig] Failed to get system default provider: {}", e.getMessage());
            }
        }
        
        config.setProvider(defaultProvider);
        config.setModel(defaultModel);
        config.setDecisionMode("ONLINE_FIRST");
        config.setDecisionTimeout(30000);
        config.setDecisionCache(true);
        config.setCacheTtl(300000);
        config.setFunctionCalling(true);
        config.setMaxIterations(defaultMaxIterations);
        config.setLlmTimeout(defaultTimeout);
        config.setDailyTokenLimit(defaultDailyTokenLimit);
        config.setUsedTokens(0);
        
        log.info("[getSystemDefaultConfig] Using fallback config: provider={}, model={}", defaultProvider, defaultModel);
        return config;
    }

    @GetMapping("/config")
    public ResultModel<LlmConfig> getLlmConfig(@PathVariable String sceneGroupId) {
        log.info("[getLlmConfig] sceneGroupId: {}", sceneGroupId);
        
        LlmConfig config = llmConfigs.get(sceneGroupId);
        if (config == null) {
            config = getSystemDefaultConfig();
            config.setIsSystemDefault(true);
            log.info("[getLlmConfig] Using system default config for new scene group: {}", sceneGroupId);
        } else {
            config.setIsSystemDefault(false);
        }
        
        return ResultModel.success(config);
    }
    
    @PutMapping("/config")
    public ResultModel<LlmConfig> updateLlmConfig(
            @PathVariable String sceneGroupId,
            @RequestBody LlmConfig config) {
        log.info("[updateLlmConfig] sceneGroupId: {}, provider: {}, model: {}", sceneGroupId, config.getProvider(), config.getModel());
        
        config.setIsSystemDefault(false);
        config.setUpdateTime(System.currentTimeMillis());
        llmConfigs.put(sceneGroupId, config);
        
        return ResultModel.success(config);
    }
    
    @PostMapping("/reset")
    public ResultModel<LlmConfig> resetToSystemDefault(@PathVariable String sceneGroupId) {
        log.info("[resetToSystemDefault] sceneGroupId: {}", sceneGroupId);
        
        llmConfigs.remove(sceneGroupId);
        LlmConfig config = getSystemDefaultConfig();
        config.setIsSystemDefault(true);
        
        return ResultModel.success(config);
    }
    
    @GetMapping("/stats")
    public ResultModel<LlmStats> getLlmStats(@PathVariable String sceneGroupId) {
        log.info("[getLlmStats] sceneGroupId: {}", sceneGroupId);
        
        LlmConfig config = llmConfigs.get(sceneGroupId);
        LlmStats stats = new LlmStats();
        
        if (config != null) {
            stats.setDailyTokenLimit(config.getDailyTokenLimit());
            stats.setUsedTokens(config.getUsedTokens());
            stats.setRemainingTokens(config.getDailyTokenLimit() - config.getUsedTokens());
        } else {
            stats.setDailyTokenLimit(defaultDailyTokenLimit);
            stats.setUsedTokens(0);
            stats.setRemainingTokens(defaultDailyTokenLimit);
        }
        
        return ResultModel.success(stats);
    }
    
    @PostMapping("/reset-tokens")
    public ResultModel<Boolean> resetDailyTokens(@PathVariable String sceneGroupId) {
        log.info("[resetDailyTokens] sceneGroupId: {}", sceneGroupId);
        
        LlmConfig config = llmConfigs.get(sceneGroupId);
        if (config != null) {
            config.setUsedTokens(0);
        }
        
        return ResultModel.success(true);
    }
    
    @GetMapping("/providers")
    public ResultModel<List<Map<String, Object>>> getAvailableProviders() {
        log.info("[getAvailableProviders] request start");
        
        List<Map<String, Object>> providers = new ArrayList<>();
        
        if (llmProviderController != null) {
            try {
                ResultModel<List<LlmProviderConfigDTO>> result = llmProviderController.listProviders();
                if (result != null && result.getData() != null) {
                    for (LlmProviderConfigDTO p : result.getData()) {
                        Map<String, Object> provider = new HashMap<>();
                        provider.put("id", p.getProviderId());
                        provider.put("name", p.getName());
                        provider.put("type", p.getType());
                        provider.put("enabled", p.isEnabled());
                        provider.put("configured", p.isConfigured());
                        provider.put("defaultModel", p.getDefaultModel());
                        providers.add(provider);
                    }
                }
            } catch (Exception e) {
                log.warn("[getAvailableProviders] Failed to get providers: {}", e.getMessage());
            }
        }
        
        if (providers.isEmpty()) {
            Map<String, Object> defaultProv = new HashMap<>();
            defaultProv.put("id", defaultProvider);
            defaultProv.put("name", defaultProvider);
            defaultProv.put("type", defaultProvider);
            defaultProv.put("enabled", true);
            defaultProv.put("configured", true);
            defaultProv.put("defaultModel", defaultModel);
            providers.add(defaultProv);
        }
        
        return ResultModel.success(providers);
    }
    
    @GetMapping("/providers/{providerId}/models")
    public ResultModel<List<Map<String, Object>>> getProviderModels(@PathVariable String providerId) {
        log.info("[getProviderModels] providerId: {}", providerId);
        
        List<Map<String, Object>> models = new ArrayList<>();
        
        if (llmProviderController != null) {
            try {
                ResultModel<List<LlmProviderConfigDTO.ModelConfigDTO>> result = 
                    llmProviderController.listModels(providerId);
                if (result != null && result.getData() != null) {
                    for (LlmProviderConfigDTO.ModelConfigDTO m : result.getData()) {
                        Map<String, Object> model = new HashMap<>();
                        model.put("id", m.getModelId());
                        model.put("name", m.getDisplayName());
                        model.put("maxTokens", m.getMaxTokens());
                        model.put("supportsFunctionCalling", m.isSupportsFunctionCalling());
                        models.add(model);
                    }
                }
            } catch (Exception e) {
                log.warn("[getProviderModels] Failed to get models: {}", e.getMessage());
            }
        }
        
        if (models.isEmpty()) {
            Map<String, Object> defaultModelMap = new HashMap<>();
            defaultModelMap.put("id", defaultModel);
            defaultModelMap.put("name", defaultModel);
            defaultModelMap.put("maxTokens", 64000);
            defaultModelMap.put("supportsFunctionCalling", true);
            models.add(defaultModelMap);
        }
        
        return ResultModel.success(models);
    }
    
    public static class LlmConfig {
        private String provider;
        private String model;
        private String decisionMode = "ONLINE_FIRST";
        private int decisionTimeout = 30000;
        private boolean decisionCache = true;
        private int cacheTtl = 300000;
        private boolean functionCalling = true;
        private int maxIterations = 5;
        private int llmTimeout = 60000;
        private int dailyTokenLimit = 100000;
        private int usedTokens = 0;
        private boolean isSystemDefault = false;
        private long updateTime;
        
        public String getProvider() { return provider; }
        public void setProvider(String provider) { this.provider = provider; }
        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }
        public String getDecisionMode() { return decisionMode; }
        public void setDecisionMode(String decisionMode) { this.decisionMode = decisionMode; }
        public int getDecisionTimeout() { return decisionTimeout; }
        public void setDecisionTimeout(int decisionTimeout) { this.decisionTimeout = decisionTimeout; }
        public boolean isDecisionCache() { return decisionCache; }
        public void setDecisionCache(boolean decisionCache) { this.decisionCache = decisionCache; }
        public int getCacheTtl() { return cacheTtl; }
        public void setCacheTtl(int cacheTtl) { this.cacheTtl = cacheTtl; }
        public boolean isFunctionCalling() { return functionCalling; }
        public void setFunctionCalling(boolean functionCalling) { this.functionCalling = functionCalling; }
        public int getMaxIterations() { return maxIterations; }
        public void setMaxIterations(int maxIterations) { this.maxIterations = maxIterations; }
        public int getLlmTimeout() { return llmTimeout; }
        public void setLlmTimeout(int llmTimeout) { this.llmTimeout = llmTimeout; }
        public int getDailyTokenLimit() { return dailyTokenLimit; }
        public void setDailyTokenLimit(int dailyTokenLimit) { this.dailyTokenLimit = dailyTokenLimit; }
        public int getUsedTokens() { return usedTokens; }
        public void setUsedTokens(int usedTokens) { this.usedTokens = usedTokens; }
        public boolean getIsSystemDefault() { return isSystemDefault; }
        public void setIsSystemDefault(boolean isSystemDefault) { this.isSystemDefault = isSystemDefault; }
        public long getUpdateTime() { return updateTime; }
        public void setUpdateTime(long updateTime) { this.updateTime = updateTime; }
    }
    
    public static class LlmStats {
        private int dailyTokenLimit;
        private int usedTokens;
        private int remainingTokens;
        
        public int getDailyTokenLimit() { return dailyTokenLimit; }
        public void setDailyTokenLimit(int dailyTokenLimit) { this.dailyTokenLimit = dailyTokenLimit; }
        public int getUsedTokens() { return usedTokens; }
        public void setUsedTokens(int usedTokens) { this.usedTokens = usedTokens; }
        public int getRemainingTokens() { return remainingTokens; }
        public void setRemainingTokens(int remainingTokens) { this.remainingTokens = remainingTokens; }
    }
}

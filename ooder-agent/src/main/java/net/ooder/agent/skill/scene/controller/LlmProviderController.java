package net.ooder.mvp.skill.scene.controller;

import net.ooder.mvp.skill.scene.dto.llm.LlmProviderConfigDTO;
import net.ooder.mvp.skill.scene.dto.llm.LlmProviderType;
import net.ooder.mvp.skill.scene.llm.DeepSeekLlmProvider;
import net.ooder.mvp.skill.scene.llm.BaiduLlmProvider;
import net.ooder.mvp.skill.scene.llm.AliyunBailianLlmProvider;
import net.ooder.mvp.skill.scene.model.ResultModel;
import net.ooder.scene.skill.LlmProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/llm-providers")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LlmProviderController {

    private static final Logger log = LoggerFactory.getLogger(LlmProviderController.class);
    
    @Value("${ooder.llm.deepseek.api-key:}")
    private String deepseekApiKey;
    
    @Value("${ooder.llm.baidu.api-key:}")
    private String baiduApiKey;
    
    @Value("${ooder.llm.baidu.secret-key:}")
    private String baiduSecretKey;
    
    @Value("${ooder.llm.qianwen.api-key:}")
    private String qianwenApiKey;
    
    @Value("${ooder.llm.provider:}")
    private String defaultProvider;
    
    @Value("${ooder.llm.model:}")
    private String defaultModel;
    
    private Map<String, LlmProviderConfigDTO> providers = new HashMap<String, LlmProviderConfigDTO>();
    
    public LlmProviderController() {
    }
    
    @jakarta.annotation.PostConstruct
    public void init() {
        initProviders();
    }
    
    private void initProviders() {
        for (LlmProviderType providerType : LlmProviderType.values()) {
            LlmProviderConfigDTO config = createConfigFromType(providerType);
            providers.put(config.getProviderId(), config);
        }
        
        log.info("[initProviders] Initialized {} providers, default: {}, model: {}", 
                providers.size(), defaultProvider, defaultModel);
    }
    
    private LlmProviderConfigDTO createConfigFromType(LlmProviderType providerType) {
        LlmProviderConfigDTO config = new LlmProviderConfigDTO();
        config.setProviderId(providerType.getCode());
        config.setName(providerType.getDisplayName());
        config.setType(providerType.getCode());
        config.setEnabled(providerType.getCode().equals(defaultProvider) || 
                (defaultProvider == null || defaultProvider.isEmpty() && providerType == LlmProviderType.QIANWEN));
        config.setTimeout(60000);
        config.setMaxRetries(3);
        config.setFunctionCallingEnabled(hasFunctionCallingModel(providerType));
        config.setMaxIterations(5);
        config.setCreateTime(System.currentTimeMillis() - 86400000L * 30);
        config.setUpdateTime(System.currentTimeMillis());
        config.setConfigured(isProviderConfigured(providerType.getCode()));
        
        List<LlmProviderConfigDTO.ModelConfigDTO> models = new ArrayList<LlmProviderConfigDTO.ModelConfigDTO>();
        for (LlmProviderType.ModelInfo modelInfo : providerType.getModels()) {
            LlmProviderConfigDTO.ModelConfigDTO modelConfig = new LlmProviderConfigDTO.ModelConfigDTO();
            modelConfig.setModelId(modelInfo.getModelId());
            modelConfig.setDisplayName(modelInfo.getDisplayName());
            modelConfig.setMaxTokens(modelInfo.getMaxTokens());
            modelConfig.setTemperature(modelInfo.getDefaultTemperature());
            modelConfig.setSupportsFunctionCalling(modelInfo.isSupportsFunctionCalling());
            modelConfig.setSupportsMultimodal(modelInfo.isSupportsMultimodal());
            modelConfig.setSupportsEmbedding(modelInfo.isSupportsEmbedding());
            modelConfig.setCostPer1kTokens(modelInfo.getCostPer1kTokens());
            models.add(modelConfig);
        }
        config.setModels(models);
        
        if (!models.isEmpty()) {
            config.setDefaultModel(defaultModel != null && !defaultModel.isEmpty() ? defaultModel : models.get(0).getModelId());
        }
        
        return config;
    }
    
    private boolean hasFunctionCallingModel(LlmProviderType providerType) {
        for (LlmProviderType.ModelInfo modelInfo : providerType.getModels()) {
            if (modelInfo.isSupportsFunctionCalling()) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isProviderConfigured(String providerCode) {
        switch (providerCode) {
            case "deepseek":
                return deepseekApiKey != null && !deepseekApiKey.isEmpty();
            case "baidu":
                return baiduApiKey != null && !baiduApiKey.isEmpty() && 
                       baiduSecretKey != null && !baiduSecretKey.isEmpty();
            case "qianwen":
            case "aliyun-bailian":
                return qianwenApiKey != null && !qianwenApiKey.isEmpty();
            default:
                return false;
        }
    }

    @GetMapping("/providers")
    public ResultModel<List<LlmProviderConfigDTO>> listProviders() {
        log.info("[listProviders] request start");
        return ResultModel.success(new ArrayList<LlmProviderConfigDTO>(providers.values()));
    }
    
    @GetMapping("/providers/{providerId}")
    public ResultModel<LlmProviderConfigDTO> getProvider(@PathVariable String providerId) {
        log.info("[getProvider] providerId: {}", providerId);
        LlmProviderConfigDTO provider = providers.get(providerId);
        if (provider == null) {
            return ResultModel.notFound("Provider not found: " + providerId);
        }
        return ResultModel.success(provider);
    }
    
    @PostMapping("/providers")
    public ResultModel<LlmProviderConfigDTO> createProvider(@RequestBody LlmProviderConfigDTO request) {
        log.info("[createProvider] name: {}", request.getName());
        
        String providerId = "provider-" + System.currentTimeMillis();
        request.setProviderId(providerId);
        request.setCreateTime(System.currentTimeMillis());
        request.setUpdateTime(System.currentTimeMillis());
        
        providers.put(providerId, request);
        
        return ResultModel.success(request);
    }
    
    @PutMapping("/providers/{providerId}")
    public ResultModel<LlmProviderConfigDTO> updateProvider(@PathVariable String providerId, @RequestBody LlmProviderConfigDTO request) {
        log.info("[updateProvider] providerId: {}", providerId);
        
        LlmProviderConfigDTO existing = providers.get(providerId);
        if (existing == null) {
            return ResultModel.notFound("Provider not found: " + providerId);
        }
        
        request.setProviderId(providerId);
        request.setUpdateTime(System.currentTimeMillis());
        request.setCreateTime(existing.getCreateTime());
        providers.put(providerId, request);
        
        return ResultModel.success(request);
    }
    
    @DeleteMapping("/providers/{providerId}")
    public ResultModel<Boolean> deleteProvider(@PathVariable String providerId) {
        log.info("[deleteProvider] providerId: {}", providerId);
        
        LlmProviderConfigDTO removed = providers.remove(providerId);
        if (removed == null) {
            return ResultModel.notFound("Provider not found: " + providerId);
        }
        
        return ResultModel.success(true);
    }
    
    @PostMapping("/providers/{providerId}/test")
    public ResultModel<Map<String, Object>> testProvider(@PathVariable String providerId) {
        log.info("[testProvider] providerId: {}", providerId);
        
        LlmProviderConfigDTO providerConfig = providers.get(providerId);
        if (providerConfig == null) {
            return ResultModel.notFound("Provider not found: " + providerId);
        }
        
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("providerId", providerId);
        result.put("providerName", providerConfig.getName());
        
        try {
            LlmProvider provider = createProviderInstance(providerConfig);
            
            if (provider == null) {
                result.put("success", false);
                result.put("message", "无法创建提供者实例，请检查API Key配置");
                return ResultModel.success(result);
            }
            
            String testModel = providerConfig.getDefaultModel();
            if (testModel == null || testModel.isEmpty()) {
                List<String> supportedModels = provider.getSupportedModels();
                if (!supportedModels.isEmpty()) {
                    testModel = supportedModels.get(0);
                }
            }
            
            if (testModel == null) {
                testModel = "default";
            }
            
            List<Map<String, Object>> messages = new ArrayList<Map<String, Object>>();
            Map<String, Object> userMessage = new HashMap<String, Object>();
            userMessage.put("role", "user");
            userMessage.put("content", "Hello, this is a connection test. Please respond with 'OK'.");
            messages.add(userMessage);
            
            long startTime = System.currentTimeMillis();
            Map<String, Object> chatResult = provider.chat(testModel, messages, null);
            long elapsed = System.currentTimeMillis() - startTime;
            
            String responseContent = (String) chatResult.get("content");
            boolean hasError = chatResult.containsKey("error") && Boolean.TRUE.equals(chatResult.get("error"));
            
            if (hasError) {
                result.put("success", false);
                result.put("message", "连接失败: " + responseContent);
                result.put("elapsed", elapsed);
            } else {
                result.put("success", true);
                result.put("message", "连接成功");
                result.put("model", testModel);
                result.put("elapsed", elapsed);
                result.put("response", responseContent != null && responseContent.length() > 100 
                    ? responseContent.substring(0, 100) + "..." 
                    : responseContent);
            }
            
        } catch (Exception e) {
            log.error("[testProvider] Test failed for provider: {}", providerId, e);
            result.put("success", false);
            result.put("message", "连接测试失败: " + e.getMessage());
        }
        
        return ResultModel.success(result);
    }
    
    private LlmProvider createProviderInstance(LlmProviderConfigDTO config) {
        String type = config.getType();
        
        if ("deepseek".equals(type)) {
            DeepSeekLlmProvider provider = new DeepSeekLlmProvider();
            String apiKey = getApiKeyFromConfig(config, "deepseek");
            if (apiKey == null || apiKey.isEmpty()) {
                return null;
            }
            provider.setApiKey(apiKey);
            return provider;
        } else if ("baidu".equals(type)) {
            BaiduLlmProvider provider = new BaiduLlmProvider();
            String apiKey = getApiKeyFromConfig(config, "baidu");
            String secretKey = getSecretKeyFromConfig(config);
            if (apiKey == null || apiKey.isEmpty() || secretKey == null || secretKey.isEmpty()) {
                return null;
            }
            provider.setAccessKey(apiKey);
            provider.setSecretKey(secretKey);
            return provider;
        } else if ("qianwen".equals(type) || "aliyun-bailian".equals(type)) {
            AliyunBailianLlmProvider provider = new AliyunBailianLlmProvider();
            String apiKey = getApiKeyFromConfig(config, "qianwen");
            if (apiKey == null || apiKey.isEmpty()) {
                return null;
            }
            provider.setApiKey(apiKey);
            return provider;
        }
        
        log.warn("[createProviderInstance] Unknown provider type: {}", type);
        return null;
    }
    
    private String getApiKeyFromConfig(LlmProviderConfigDTO config, String providerCode) {
        Map<String, Object> providerConfig = config.getProviderConfig();
        if (providerConfig != null && providerConfig.containsKey("apiKey")) {
            return (String) providerConfig.get("apiKey");
        }
        
        String apiKey = getApiKeyByProviderCode(providerCode);
        return apiKey;
    }
    
    private String getApiKeyByProviderCode(String providerCode) {
        switch (providerCode) {
            case "deepseek":
                return deepseekApiKey;
            case "baidu":
                return baiduApiKey;
            case "qianwen":
            case "aliyun-bailian":
                return qianwenApiKey;
            default:
                return null;
        }
    }
    
    private String getSecretKeyFromConfig(LlmProviderConfigDTO config) {
        Map<String, Object> providerConfig = config.getProviderConfig();
        if (providerConfig != null && providerConfig.containsKey("secretKey")) {
            return (String) providerConfig.get("secretKey");
        }
        
        if (baiduSecretKey != null && !baiduSecretKey.isEmpty()) {
            return baiduSecretKey;
        }
        
        return null;
    }
    
    @GetMapping("/providers/{providerId}/models")
    public ResultModel<List<LlmProviderConfigDTO.ModelConfigDTO>> listModels(@PathVariable String providerId) {
        log.info("[listModels] providerId: {}", providerId);
        
        LlmProviderConfigDTO provider = providers.get(providerId);
        if (provider == null) {
            return ResultModel.notFound("Provider not found: " + providerId);
        }
        
        return ResultModel.success(provider.getModels());
    }
    
    @GetMapping("/default")
    public ResultModel<LlmProviderConfigDTO> getDefaultProvider() {
        log.info("[getDefaultProvider] request start");
        
        for (LlmProviderConfigDTO provider : providers.values()) {
            if (provider.isEnabled()) {
                return ResultModel.success(provider);
            }
        }
        
        return ResultModel.notFound("No default provider configured");
    }
}

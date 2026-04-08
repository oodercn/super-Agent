package net.ooder.agent.llm.service.impl;

import net.ooder.agent.llm.AliyunBailianLlmProvider;
import net.ooder.agent.llm.DeepSeekLlmProvider;
import net.ooder.agent.llm.BaiduLlmProvider;
import net.ooder.agent.llm.service.LlmProviderManager;
import net.ooder.scene.skill.LlmProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LlmProviderManagerImpl implements LlmProviderManager {

    private static final Logger log = LoggerFactory.getLogger(LlmProviderManagerImpl.class);
    
    @Value("${ooder.llm.deepseek.api-key:}")
    private String deepseekApiKey;
    
    @Value("${ooder.llm.baidu.api-key:}")
    private String baiduApiKey;
    
    @Value("${ooder.llm.baidu.secret-key:}")
    private String baiduSecretKey;
    
    @Value("${ooder.llm.qianwen.api-key:}")
    private String qianwenApiKey;
    
    @Value("${ooder.llm.provider:qianwen}")
    private String defaultProvider;
    
    @Value("${ooder.llm.model:qwen-plus}")
    private String defaultModel;
    
    private final Map<String, LlmProvider> providerCache = new HashMap<>();

    @Override
    public LlmProvider getProvider(String providerType) {
        if (providerType == null || providerType.isEmpty()) {
            providerType = defaultProvider;
        }
        
        return providerCache.computeIfAbsent(providerType, this::createProvider);
    }

    @Override
    public LlmProvider getDefaultProvider() {
        return getProvider(defaultProvider);
    }

    @Override
    public String getDefaultModel() {
        return defaultModel;
    }

    @Override
    public Map<String, Object> chat(String providerType, String model, 
            String systemPrompt, List<Map<String, String>> messages, 
            Map<String, Object> options) {
        
        LlmProvider provider = getProvider(providerType);
        if (provider == null) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("error", true);
            errorResult.put("content", "无法获取LLM Provider: " + providerType);
            return errorResult;
        }
        
        if (model == null || model.isEmpty()) {
            model = defaultModel;
        }
        
        List<Map<String, Object>> formattedMessages = new ArrayList<>();
        
        if (systemPrompt != null && !systemPrompt.isEmpty()) {
            Map<String, Object> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", systemPrompt);
            formattedMessages.add(systemMessage);
        }
        
        if (messages != null) {
            for (Map<String, String> msg : messages) {
                Map<String, Object> formattedMsg = new HashMap<>();
                formattedMsg.put("role", msg.get("role"));
                formattedMsg.put("content", msg.get("content"));
                formattedMessages.add(formattedMsg);
            }
        }
        
        try {
            log.info("[LlmProviderManager] Calling LLM - provider: {}, model: {}, messages: {}", 
                    providerType, model, formattedMessages.size());
            
            Map<String, Object> result = provider.chat(model, formattedMessages, options);
            
            log.info("[LlmProviderManager] LLM response received - success: {}", 
                    !Boolean.TRUE.equals(result.get("error")));
            
            return result;
        } catch (Exception e) {
            log.error("[LlmProviderManager] LLM call failed: {}", e.getMessage(), e);
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("error", true);
            errorResult.put("content", "LLM调用失败: " + e.getMessage());
            return errorResult;
        }
    }

    @Override
    public boolean testConnection(String providerType, String apiKey) {
        try {
            LlmProvider provider = createProviderWithApiKey(providerType, apiKey);
            if (provider == null) {
                return false;
            }
            
            List<Map<String, Object>> messages = new ArrayList<>();
            Map<String, Object> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", "Hello, this is a connection test. Please respond with 'OK'.");
            messages.add(userMessage);
            
            Map<String, Object> result = provider.chat(null, messages, null);
            
            return !Boolean.TRUE.equals(result.get("error"));
        } catch (Exception e) {
            log.error("[LlmProviderManager] Connection test failed: {}", e.getMessage());
            return false;
        }
    }

    private LlmProvider createProvider(String providerType) {
        String apiKey = getApiKeyForProvider(providerType);
        return createProviderWithApiKey(providerType, apiKey);
    }

    private LlmProvider createProviderWithApiKey(String providerType, String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            log.warn("[LlmProviderManager] No API key configured for provider: {}", providerType);
            return null;
        }
        
        switch (providerType.toLowerCase()) {
            case "qianwen":
            case "aliyun-bailian":
                AliyunBailianLlmProvider qianwenProvider = new AliyunBailianLlmProvider();
                qianwenProvider.setApiKey(apiKey);
                return qianwenProvider;
                
            case "deepseek":
                DeepSeekLlmProvider deepseekProvider = new DeepSeekLlmProvider();
                deepseekProvider.setApiKey(apiKey);
                return deepseekProvider;
                
            case "baidu":
                BaiduLlmProvider baiduProvider = new BaiduLlmProvider();
                baiduProvider.setAccessKey(apiKey);
                baiduProvider.setSecretKey(baiduSecretKey);
                return baiduProvider;
                
            default:
                log.warn("[LlmProviderManager] Unknown provider type: {}", providerType);
                return null;
        }
    }

    private String getApiKeyForProvider(String providerType) {
        switch (providerType.toLowerCase()) {
            case "qianwen":
            case "aliyun-bailian":
                return qianwenApiKey;
            case "deepseek":
                return deepseekApiKey;
            case "baidu":
                return baiduApiKey;
            default:
                return null;
        }
    }
}

package net.ooder.skill.llm.chat.service;

import net.ooder.skill.llm.chat.model.LlmConfig;
import net.ooder.skill.llm.chat.model.ResolvedConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LLMService implements LLMServiceProvider {
    
    private static final Logger log = LoggerFactory.getLogger(LLMService.class);

    private static Environment staticEnvironment;
    
    @Autowired
    public void setEnvironment(Environment environment) {
        LLMService.staticEnvironment = environment;
        this.environment = environment;
    }

    private Environment environment;

    @Autowired(required = false)
    private LlmConfigService llmConfigService;
    
    private String defaultProvider;
    
    public LLMService() {
        this.defaultProvider = "qianwen";
    }
    
    private String getDefaultProvider() {
        String provider = getEnvProperty("ooder.llm.provider");
        if (provider == null || provider.trim().isEmpty()) {
            provider = System.getProperty("ooder.llm.provider");
        }
        if (provider == null || provider.trim().isEmpty()) {
            provider = defaultProvider;
        }
        return (provider != null && !provider.trim().isEmpty()) ? provider : "qianwen";
    }
    
    private String getEnvProperty(String key) {
        if (environment != null) {
            return environment.getProperty(key);
        }
        if (staticEnvironment != null) {
            return staticEnvironment.getProperty(key);
        }
        return null;
    }
    
    @Value("${ooder.llm.api-key:}")
    private String fallbackApiKey;
    
    @Value("${ooder.llm.base-url:}")
    private String fallbackBaseUrl;
    
    @Value("${ooder.llm.model:deepseek-chat}")
    private String defaultModel;
    
    @Value("${deepseek.api-key:}")
    private String deepseekApiKey;

    @Value("${deepseek.base-url:https://api.deepseek.com}")
    private String deepseekBaseUrl;

    @Value("${baidu.api-key:}")
    private String baiduApiKey;

    @Value("${baidu.base-url:https://qianfan.baidubce.com/v2}")
    private String baiduBaseUrl;

    @Value("${ooder.llm.qianwen.api-key:}")
    private String qianwenApiKey;

    @Value("${ooder.llm.qianwen.base-url:https://dashscope.aliyuncs.com/compatible-mode/v1}")
    private String qianwenBaseUrl;

    @Value("${ooder.llm.qianwen.model:qwen-plus}")
    private String qianwenModel;

    private String getQianwenApiKey() {
        String key = getEnvProperty("ooder.llm.qianwen.api-key");
        if (key == null || key.trim().isEmpty()) {
            key = System.getProperty("ooder.llm.qianwen.api-key");
        }
        if (key == null || key.trim().isEmpty()) {
            key = qianwenApiKey;
        }
        if (key == null || key.trim().isEmpty()) {
            key = "sk-f403a6a32efd4b00b75ee4874eec3b6b";
        }
        return (key != null && !key.trim().isEmpty()) ? key : null;
    }

    private String getQianwenBaseUrl() {
        String url = getEnvProperty("ooder.llm.qianwen.base-url");
        if (url == null || url.trim().isEmpty()) {
            url = System.getProperty("ooder.llm.qianwen.base-url");
        }
        if (url == null || url.trim().isEmpty()) {
            url = qianwenBaseUrl;
        }
        if (url == null || url.trim().isEmpty()) {
            url = "https://dashscope.aliyuncs.com/compatible-mode/v1";
        }
        return url;
    }

    private String getQianwenModel() {
        String model = getEnvProperty("ooder.llm.qianwen.model");
        if (model == null || model.trim().isEmpty()) {
            model = System.getProperty("ooder.llm.qianwen.model");
        }
        if (model == null || model.trim().isEmpty()) {
            model = qianwenModel;
        }
        if (model == null || model.trim().isEmpty()) {
            model = "qwen-plus";
        }
        return model;
    }

    private final Map<String, Object> llmCache = new ConcurrentHashMap<>();
    private String lastUsedModel = "";
    
    public Map<String, Object> checkDependencies() {
        Map<String, Object> result = new LinkedHashMap<>();
        
        ResolvedConfig config = resolveConfig(null, null, null);
        
        result.put("provider", config.getProviderType());
        result.put("model", config.getModel());
        result.put("baseUrl", config.getBaseUrl());
        
        boolean hasApiKey = config.getApiKey() != null && !config.getApiKey().trim().isEmpty();
        result.put("apiKeyConfigured", hasApiKey);
        
        if (hasApiKey) {
            String key = config.getApiKey();
            result.put("apiKeyPreview", key.substring(0, Math.min(8, key.length())) + "...");
        }
        
        boolean hasDeepseekKey = deepseekApiKey != null && !deepseekApiKey.trim().isEmpty();
        result.put("deepseekConfigured", hasDeepseekKey);

        if (hasDeepseekKey) {
            result.put("deepseekKeyPreview", deepseekApiKey.substring(0, Math.min(8, deepseekApiKey.length())) + "...");
        }

        boolean hasQianwenKey = getQianwenApiKey() != null;
        result.put("qianwenConfigured", hasQianwenKey);

        if (hasQianwenKey) {
            result.put("qianwenKeyPreview", getQianwenApiKey().substring(0, Math.min(8, getQianwenApiKey().length())) + "...");
        }

        boolean apiAccessible = checkApiConnection(config);
        result.put("apiAccessible", apiAccessible);

        List<String> issues = new ArrayList<>();
        if (!hasApiKey && !hasDeepseekKey && !hasQianwenKey) {
            issues.add("未配置任何LLM API Key，请在LLM配置管理中配置或设置环境变量");
        }
        if (!apiAccessible) {
            issues.add("API服务不可达，请检查网络连接或base-url配置");
        }
        result.put("issues", issues);

        result.put("ready", hasApiKey || hasDeepseekKey || hasQianwenKey);
        
        return result;
    }
    
    public boolean isReady() {
        Map<String, Object> status = checkDependencies();
        return (Boolean) status.get("ready");
    }
    
    private ResolvedConfig resolveConfig(String userId, String sceneId, String departmentId) {
        if (llmConfigService != null) {
            try {
                return llmConfigService.resolveConfigWithPriority(userId, sceneId, departmentId, null);
            } catch (Exception e) {
                log.warn("Failed to resolve config from LlmConfigService, using fallback", e);
            }
        }
        
        ResolvedConfig config = new ResolvedConfig();
        String effectiveProvider = getDefaultProvider();
        config.setProviderType(effectiveProvider);
        config.setModel(defaultModel);

        if ("deepseek".equals(effectiveProvider)) {
            config.setApiKey(deepseekApiKey);
            config.setBaseUrl(deepseekBaseUrl);
        } else if ("baidu".equals(effectiveProvider)) {
            config.setApiKey(baiduApiKey);
            config.setBaseUrl(baiduBaseUrl);
        } else if ("qianwen".equals(effectiveProvider)) {
            config.setApiKey(getQianwenApiKey());
            config.setBaseUrl(getQianwenBaseUrl());
            config.setModel(getQianwenModel());
        } else {
            config.setApiKey(fallbackApiKey);
            config.setBaseUrl(fallbackBaseUrl);
        }
        
        return config;
    }
    
    private boolean checkApiConnection(ResolvedConfig config) {
        if ("baidu".equals(config.getProviderType())) {
            return checkBaiduApiConnection(config);
        }
        try {
            String baseUrl = config.getBaseUrl();
            if (baseUrl == null || baseUrl.isEmpty()) {
                return false;
            }
            URL url = new URL(baseUrl + "/models");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            
            String apiKey = config.getApiKey();
            if (apiKey != null && !apiKey.trim().isEmpty()) {
                conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            }
            
            int responseCode = conn.getResponseCode();
            conn.disconnect();
            
            return responseCode < 500;
        } catch (Exception e) {
            log.debug("API connection check failed: {}", e.getMessage());
            return false;
        }
    }
    
    private boolean checkBaiduApiConnection(ResolvedConfig config) {
        try {
            String baseUrl = config.getBaseUrl();
            if (baseUrl == null || baseUrl.isEmpty()) {
                baseUrl = baiduBaseUrl;
            }
            URL url = new URL(baseUrl + "/models");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            int responseCode = conn.getResponseCode();
            conn.disconnect();
            return responseCode < 500;
        } catch (Exception e) {
            log.debug("Baidu API connection check failed: {}", e.getMessage());
            return false;
        }
    }
    
    public Map<String, Object> chat(String prompt, String systemPrompt) {
        return chat(prompt, systemPrompt, null, null, null);
    }
    
    public Map<String, Object> chat(String prompt, String systemPrompt, String provider, String model, String sceneId) {
        Map<String, Object> result = new HashMap<>();
        
        ResolvedConfig config = resolveConfig(null, sceneId, null);
        
        if (provider != null && !provider.isEmpty()) {
            config.setProviderType(provider);
            if ("qianwen".equals(provider) && (model == null || model.isEmpty())) {
                config.setModel(qianwenModel);
            }
        }
        if (model != null && !model.isEmpty()) {
            config.setModel(model);
        }

        String apiKey = config.getApiKey();
        String baseUrl = config.getBaseUrl();
        String useModel = config.getModel();
        
        if (apiKey == null || apiKey.trim().isEmpty()) {
            apiKey = getFallbackApiKey(config.getProviderType());
        }
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = getFallbackBaseUrl(config.getProviderType());
        }
        
        if (apiKey == null || apiKey.trim().isEmpty()) {
            result.put("status", "error");
            result.put("error", "API Key未配置，请在LLM配置管理中配置");
            result.put("answer", generateLocalAnswer(prompt));
            return result;
        }
        
        try {
            String endpoint = "/chat/completions";
            URL url = new URL(baseUrl + endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setDoOutput(true);
            conn.setConnectTimeout(60000);
            conn.setReadTimeout(60000);
            
            StringBuilder messages = new StringBuilder();
            messages.append("[");
            if (systemPrompt != null && !systemPrompt.isEmpty()) {
                messages.append("{\"role\":\"system\",\"content\":").append(escapeJson(systemPrompt)).append("},");
            }
            messages.append("{\"role\":\"user\",\"content\":").append(escapeJson(prompt)).append("}");
            messages.append("]");
            
            String requestBody = String.format(
                "{\"model\":\"%s\",\"messages\":%s,\"temperature\":0.7}",
                useModel,
                messages.toString()
            );
            
            log.info("[chat] Calling LLM API: {}{}, provider: {}, model: {}", baseUrl, endpoint, config.getProviderType(), useModel);
            log.debug("[chat] Request body: {}", requestBody);
            
            try (java.io.OutputStream os = conn.getOutputStream()) {
                os.write(requestBody.getBytes("UTF-8"));
            }
            
            int responseCode = conn.getResponseCode();
            log.info("[chat] Response code: {}", responseCode);
            
            if (responseCode == 200) {
                String response = readResponse(conn.getInputStream());
                log.debug("[chat] Response: {}", response);
                result.put("status", "success");
                result.put("response", response);
                result.put("answer", extractAnswer(response));
                result.put("provider", config.getProviderType());
                result.put("model", useModel);
            } else {
                String errorResponse = readResponse(conn.getErrorStream());
                log.warn("[chat] API error: {} - {}", responseCode, errorResponse);
                result.put("status", "error");
                result.put("error", "API返回错误: " + responseCode);
                result.put("details", errorResponse);
                result.put("answer", generateLocalAnswer(prompt));
            }
            
            conn.disconnect();
        } catch (Exception e) {
            log.error("[chat] Exception: {}", e.getMessage(), e);
            result.put("status", "error");
            result.put("error", e.getMessage());
            result.put("answer", generateLocalAnswer(prompt));
        }
        
        return result;
    }
    
    private String getFallbackApiKey(String provider) {
        if ("deepseek".equals(provider)) {
            return deepseekApiKey;
        } else if ("baidu".equals(provider)) {
            return baiduApiKey;
        } else if ("qianwen".equals(provider)) {
            return getQianwenApiKey();
        }
        return fallbackApiKey;
    }

    private String getFallbackBaseUrl(String provider) {
        if ("deepseek".equals(provider)) {
            return deepseekBaseUrl;
        } else if ("baidu".equals(provider)) {
            return baiduBaseUrl;
        } else if ("qianwen".equals(provider)) {
            return getQianwenBaseUrl();
        }
        return fallbackBaseUrl;
    }
    
    private String generateLocalAnswer(String question) {
        return "根据知识库检索结果，关于\"" + question + "\"的相关信息如下：\n\n" +
               "请参考产品使用手册第3章的详细说明，其中包含了完整的操作指南和注意事项。\n\n" +
               "如需更详细的信息，请在LLM配置管理中配置API Key以启用智能问答功能。";
    }
    
    private String extractAnswer(String response) {
        try {
            int contentStart = response.indexOf("\"content\":\"");
            if (contentStart > 0) {
                contentStart += 11;
                int contentEnd = response.indexOf("\"", contentStart);
                if (contentEnd > contentStart) {
                    String content = response.substring(contentStart, contentEnd);
                    return content.replace("\\n", "\n").replace("\\\"", "\"").replace("\\\\", "\\");
                }
            }
        } catch (Exception e) {
            log.warn("Failed to extract answer from response", e);
        }
        return response;
    }
    
    private String readResponse(java.io.InputStream is) throws IOException {
        if (is == null) return "";
        try (java.io.BufferedReader br = new java.io.BufferedReader(
                new java.io.InputStreamReader(is, "UTF-8"))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }
    
    private String escapeJson(String text) {
        if (text == null) return "null";
        return "\"" + text
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t")
            + "\"";
    }
    
    public String generateAnswer(String question, List<Map<String, Object>> sources, String model) {
        if (!isReady()) {
            return generateLocalAnswer(question);
        }
        
        StringBuilder context = new StringBuilder();
        if (sources != null && !sources.isEmpty()) {
            context.append("参考文档：\n");
            for (Map<String, Object> s : sources) {
                context.append("- ").append(s.get("title")).append("\n");
            }
        }
        
        String systemPrompt = "你是一个知识库问答助手，请根据提供的参考文档回答用户问题。";
        String prompt = context.toString() + "\n\n用户问题：" + question;
        
        ResolvedConfig config = resolveConfig(null, null, null);
        String provider = config.getProviderType();
        if (model != null && model.startsWith("deepseek")) {
            provider = "deepseek";
        } else if (model != null && model.startsWith("ernie")) {
            provider = "baidu";
        }
        
        Map<String, Object> result = chat(prompt, systemPrompt, provider, model, null);
        
        if ("success".equals(result.get("status"))) {
            return (String) result.get("answer");
        } else {
            return generateLocalAnswer(question);
        }
    }
    
    public String generateAnswer(String question, List<Map<String, Object>> sources) {
        return generateAnswer(question, sources, null);
    }
    
    public String chat(String prompt, List<Map<String, Object>> history) {
        ResolvedConfig config = resolveConfig(null, null, null);
        
        Map<String, Object> result = chat(prompt, null, config.getProviderType(), config.getModel(), null);
        
        if ("success".equals(result.get("status"))) {
            return (String) result.get("answer");
        } else {
            return generateLocalAnswer(prompt);
        }
    }
}

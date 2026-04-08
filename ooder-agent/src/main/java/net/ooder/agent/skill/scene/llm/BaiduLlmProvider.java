package net.ooder.mvp.skill.scene.llm;

import net.ooder.scene.skill.LlmProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class BaiduLlmProvider implements LlmProvider {

    private static final Logger log = LoggerFactory.getLogger(BaiduLlmProvider.class);

    private static final String PROVIDER_TYPE = "baidu";
    private static final String DEFAULT_MODEL = "ernie-4.5-8k-preview";
    
    private static final String QIANFAN_API_URL = "https://qianfan.baidubce.com/v2/chat/completions";
    private static final String TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token";
    
    private String accessKey;
    private String secretKey;
    private String cachedAccessToken;
    private long tokenExpireTime;
    
    private final List<String> supportedModels = new ArrayList<String>();
    
    public BaiduLlmProvider() {
        supportedModels.add("ernie-4.5-8k-preview");
        supportedModels.add("ernie-4.5-turbo-vl-32k");
        supportedModels.add("ernie-x1-turbo-32k");
        supportedModels.add("ernie-speed-8k");
        supportedModels.add("ernie-lite-8k");
    }
    
    public BaiduLlmProvider(String accessKey, String secretKey) {
        this();
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }
    
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
    
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public String getProviderType() {
        return PROVIDER_TYPE;
    }

    @Override
    public List<String> getSupportedModels() {
        return supportedModels;
    }

    @Override
    public boolean supportsStreaming() {
        return true;
    }

    @Override
    public boolean supportsFunctionCalling() {
        return false;
    }

    @Override
    public Map<String, Object> chat(String model, List<Map<String, Object>> messages, Map<String, Object> options) {
        log.info("Baidu LLM chat called with model: {}", model);
        
        Map<String, Object> result = new HashMap<String, Object>();
        
        try {
            Map<String, Object> requestBody = new HashMap<String, Object>();
            requestBody.put("model", getModelId(model));
            requestBody.put("messages", messages);
            
            String response = sendRequest(QIANFAN_API_URL, requestBody);
            
            String content = extractNestedJsonValue(response, "choices", "message", "content");
            if (content == null) {
                content = extractJsonValue(response, "result");
            }
            
            result.put("content", content != null ? content : response);
            result.put("model", model);
            result.put("provider", PROVIDER_TYPE);
            
            String usageJson = extractUsageJson(response);
            if (usageJson != null) {
                Map<String, Object> usage = parseUsage(usageJson);
                result.put("usage", usage);
                
                Integer promptTokens = (Integer) usage.get("prompt_tokens");
                Integer completionTokens = (Integer) usage.get("completion_tokens");
                if (promptTokens != null) {
                    result.put("inputTokens", promptTokens);
                }
                if (completionTokens != null) {
                    result.put("outputTokens", completionTokens);
                }
            }
            
        } catch (Exception e) {
            log.error("Baidu LLM chat error", e);
            result.put("content", "Error: " + e.getMessage());
            result.put("error", true);
        }
        
        return result;
    }
    
    private String getModelId(String model) {
        if (model == null || model.isEmpty()) {
            return DEFAULT_MODEL;
        }
        return model;
    }

    @Override
    public String complete(String model, String prompt, Map<String, Object> options) {
        log.info("Baidu LLM complete called with model: {}", model);
        
        List<Map<String, Object>> messages = new ArrayList<Map<String, Object>>();
        Map<String, Object> userMessage = new HashMap<String, Object>();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);
        messages.add(userMessage);
        
        Map<String, Object> result = chat(model, messages, options);
        return (String) result.get("content");
    }

    @Override
    public String translate(String model, String text, String targetLanguage, String sourceLanguage) {
        log.info("Baidu LLM translate called");
        
        String prompt = "请将以下文本翻译成" + targetLanguage + "：\n\n" + text;
        if (sourceLanguage != null && !sourceLanguage.isEmpty()) {
            prompt = "请将以下" + sourceLanguage + "文本翻译成" + targetLanguage + "：\n\n" + text;
        }
        
        return complete(model, prompt, null);
    }

    @Override
    public String summarize(String model, String text, int maxLength) {
        log.info("Baidu LLM summarize called");
        
        String prompt = "请总结以下内容，总结长度不超过" + maxLength + "字：\n\n" + text;
        return complete(model, prompt, null);
    }

    @Override
    public List<double[]> embed(String model, List<String> texts) {
        log.info("Baidu LLM embed called with model: {}, text count: {}", model, texts.size());
        
        List<double[]> result = new ArrayList<double[]>();
        log.info("Embedding not implemented for Baidu");
        
        return result;
    }
    
    private String getAccessToken() {
        if (cachedAccessToken != null && System.currentTimeMillis() < tokenExpireTime) {
            return cachedAccessToken;
        }
        
        try {
            String urlStr = TOKEN_URL + "?grant_type=client_credentials&client_id=" + accessKey + "&client_secret=" + secretKey;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                
                String responseBody = response.toString();
                String accessToken = extractJsonValue(responseBody, "access_token");
                String expiresIn = extractJsonValue(responseBody, "expires_in");
                
                if (accessToken != null) {
                    cachedAccessToken = accessToken;
                    if (expiresIn != null) {
                        tokenExpireTime = System.currentTimeMillis() + (Long.parseLong(expiresIn) - 300) * 1000;
                    } else {
                        tokenExpireTime = System.currentTimeMillis() + 24 * 60 * 60 * 1000;
                    }
                    log.info("Successfully obtained Baidu access token, expires in {} seconds", expiresIn);
                    return accessToken;
                }
            }
            log.error("Failed to get access token, response code: {}", responseCode);
        } catch (Exception e) {
            log.error("Error getting Baidu access token", e);
        }
        return null;
    }
    
    private String sendRequest(String apiUrl, Map<String, Object> requestBody) throws Exception {
        String accessToken = getAccessToken();
        if (accessToken == null) {
            return "Error: Failed to get access token";
        }
        
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        
        conn.setConnectTimeout(60000);
        conn.setReadTimeout(60000);
        conn.setDoOutput(true);
        
        String jsonBody = mapToJson(requestBody);
        log.debug("Request body: {}", jsonBody);
        
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        
        int responseCode = conn.getResponseCode();
        log.info("Baidu API response code: {}", responseCode);
        
        BufferedReader reader;
        if (responseCode == 200) {
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        } else {
            InputStream errorStream = conn.getErrorStream();
            if (errorStream != null) {
                reader = new BufferedReader(new InputStreamReader(errorStream, StandardCharsets.UTF_8));
            } else {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            }
        }
        
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        
        String responseBody = response.toString();
        log.debug("Response body: {}", responseBody);
        
        if (responseCode != 200) {
            log.error("Baidu API error: {} - {}", responseCode, responseBody);
            return "{\"error\": true, \"message\": \"HTTP " + responseCode + ": " + escapeJson(responseBody) + "\"}";
        }
        
        return responseBody;
    }
    
    private String extractUsageJson(String response) {
        String searchKey = "\"usage\":";
        int startIndex = response.indexOf(searchKey);
        if (startIndex == -1) {
            return null;
        }
        
        startIndex += searchKey.length();
        while (startIndex < response.length() && (response.charAt(startIndex) == ' ' || response.charAt(startIndex) == '\t')) {
            startIndex++;
        }
        
        if (startIndex >= response.length() || response.charAt(startIndex) != '{') {
            return null;
        }
        
        int braceCount = 0;
        int endIndex = startIndex;
        while (endIndex < response.length()) {
            char c = response.charAt(endIndex);
            if (c == '{') braceCount++;
            else if (c == '}') braceCount--;
            endIndex++;
            if (braceCount == 0) break;
        }
        
        return response.substring(startIndex, endIndex);
    }
    
    private Map<String, Object> parseUsage(String usageJson) {
        Map<String, Object> usage = new HashMap<String, Object>();
        String promptTokens = extractJsonValue(usageJson, "prompt_tokens");
        String completionTokens = extractJsonValue(usageJson, "completion_tokens");
        String totalTokens = extractJsonValue(usageJson, "total_tokens");
        
        if (promptTokens != null) usage.put("prompt_tokens", Integer.parseInt(promptTokens));
        if (completionTokens != null) usage.put("completion_tokens", Integer.parseInt(completionTokens));
        if (totalTokens != null) usage.put("total_tokens", Integer.parseInt(totalTokens));
        
        return usage;
    }
    
    private String extractNestedJsonValue(String json, String... keys) {
        String current = json;
        for (int i = 0; i < keys.length - 1; i++) {
            String key = keys[i];
            String searchKey = "\"" + key + "\":";
            int startIndex = current.indexOf(searchKey);
            if (startIndex == -1) {
                return null;
            }
            startIndex += searchKey.length();
            while (startIndex < current.length() && (current.charAt(startIndex) == ' ' || current.charAt(startIndex) == '\t')) {
                startIndex++;
            }
            if (startIndex >= current.length()) {
                return null;
            }
            if (current.charAt(startIndex) == '[') {
                startIndex++;
                int bracketCount = 1;
                int endIndex = startIndex;
                while (endIndex < current.length() && bracketCount > 0) {
                    if (current.charAt(endIndex) == '[') bracketCount++;
                    else if (current.charAt(endIndex) == ']') bracketCount--;
                    endIndex++;
                }
                current = current.substring(startIndex, endIndex - 1);
            } else if (current.charAt(startIndex) == '{') {
                startIndex++;
                int braceCount = 1;
                int endIndex = startIndex;
                while (endIndex < current.length() && braceCount > 0) {
                    if (current.charAt(endIndex) == '{') braceCount++;
                    else if (current.charAt(endIndex) == '}') braceCount--;
                    endIndex++;
                }
                current = current.substring(startIndex, endIndex - 1);
            }
        }
        return extractJsonValue(current, keys[keys.length - 1]);
    }
    
    private String extractJsonValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int startIndex = json.indexOf(searchKey);
        if (startIndex == -1) {
            return null;
        }
        
        startIndex += searchKey.length();
        while (startIndex < json.length() && (json.charAt(startIndex) == ' ' || json.charAt(startIndex) == '\t')) {
            startIndex++;
        }
        
        if (startIndex >= json.length()) {
            return null;
        }
        
        if (json.charAt(startIndex) == '"') {
            startIndex++;
            int endIndex = startIndex;
            while (endIndex < json.length()) {
                if (json.charAt(endIndex) == '"' && json.charAt(endIndex - 1) != '\\') {
                    break;
                }
                endIndex++;
            }
            return json.substring(startIndex, endIndex);
        } else {
            int endIndex = startIndex;
            while (endIndex < json.length() && json.charAt(endIndex) != ',' && json.charAt(endIndex) != '}') {
                endIndex++;
            }
            return json.substring(startIndex, endIndex).trim();
        }
    }
    
    private String mapToJson(Map<String, Object> map) {
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) {
                json.append(",");
            }
            first = false;
            
            json.append("\"").append(entry.getKey()).append("\":");
            
            Object value = entry.getValue();
            if (value instanceof String) {
                json.append("\"").append(escapeJson((String) value)).append("\"");
            } else if (value instanceof List) {
                json.append(listToJson((List<?>) value));
            } else if (value instanceof Map) {
                json.append(mapToJson((Map<String, Object>) value));
            } else if (value instanceof Number || value instanceof Boolean) {
                json.append(value);
            } else {
                json.append("\"").append(escapeJson(value.toString())).append("\"");
            }
        }
        
        json.append("}");
        return json.toString();
    }
    
    private String listToJson(List<?> list) {
        StringBuilder json = new StringBuilder("[");
        boolean first = true;
        
        for (Object item : list) {
            if (!first) {
                json.append(",");
            }
            first = false;
            
            if (item instanceof String) {
                json.append("\"").append(escapeJson((String) item)).append("\"");
            } else if (item instanceof Map) {
                json.append(mapToJson((Map<String, Object>) item));
            } else {
                json.append("\"").append(escapeJson(item.toString())).append("\"");
            }
        }
        
        json.append("]");
        return json.toString();
    }
    
    private String escapeJson(String str) {
        if (str == null) {
            return "";
        }
        StringBuilder escaped = new StringBuilder();
        for (char c : str.toCharArray()) {
            switch (c) {
                case '"':
                    escaped.append("\\\"");
                    break;
                case '\\':
                    escaped.append("\\\\");
                    break;
                case '\b':
                    escaped.append("\\b");
                    break;
                case '\f':
                    escaped.append("\\f");
                    break;
                case '\n':
                    escaped.append("\\n");
                    break;
                case '\r':
                    escaped.append("\\r");
                    break;
                case '\t':
                    escaped.append("\\t");
                    break;
                default:
                    if (c < ' ') {
                        escaped.append(String.format("\\u%04x", (int) c));
                    } else {
                        escaped.append(c);
                    }
            }
        }
        return escaped.toString();
    }
}

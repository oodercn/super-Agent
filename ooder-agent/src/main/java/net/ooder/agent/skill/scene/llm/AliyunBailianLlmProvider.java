package net.ooder.mvp.skill.scene.llm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import net.ooder.scene.skill.LlmProvider;
import net.ooder.scene.skill.tool.*;
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
import java.util.function.Consumer;

public class AliyunBailianLlmProvider implements LlmProvider {

    private static final Logger log = LoggerFactory.getLogger(AliyunBailianLlmProvider.class);

    private static final String PROVIDER_TYPE = "aliyun-bailian";
    private static final String DEFAULT_MODEL = "qwen-plus";
    
    private static final String API_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";
    
    private String apiKey;
    private ToolRegistry toolRegistry;
    private ToolOrchestrator toolOrchestrator;
    
    private final List<String> supportedModels = new ArrayList<String>();
    
    public AliyunBailianLlmProvider() {
        supportedModels.add("qwen-turbo");
        supportedModels.add("qwen-plus");
        supportedModels.add("qwen-max");
        supportedModels.add("qwen-long");
    }
    
    public AliyunBailianLlmProvider(String apiKey) {
        this();
        this.apiKey = apiKey;
    }
    
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setToolRegistry(ToolRegistry toolRegistry) {
        this.toolRegistry = toolRegistry;
    }
    
    public void setToolOrchestrator(ToolOrchestrator toolOrchestrator) {
        this.toolOrchestrator = toolOrchestrator;
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
        return true;
    }

    public void chatStream(String model, List<Map<String, Object>> messages, Map<String, Object> options, Consumer<String> onChunk, Consumer<Map<String, Object>> onComplete, Consumer<Exception> onError) {
        log.info("AliyunBailian LLM chatStream called with model: {}", model);
        
        try {
            Map<String, Object> requestBody = new HashMap<String, Object>();
            requestBody.put("model", model != null ? model : DEFAULT_MODEL);
            requestBody.put("messages", messages);
            requestBody.put("stream", true);
            
            if (options != null) {
                if (options.containsKey("temperature")) {
                    requestBody.put("temperature", options.get("temperature"));
                }
                if (options.containsKey("max_tokens")) {
                    requestBody.put("max_tokens", options.get("max_tokens"));
                }
                if (options.containsKey("top_p")) {
                    requestBody.put("top_p", options.get("top_p"));
                }
                
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> tools = (List<Map<String, Object>>) options.get("tools");
                if (tools != null && !tools.isEmpty()) {
                    requestBody.put("tools", tools);
                    Object toolChoice = options.get("tool_choice");
                    if (toolChoice != null) {
                        requestBody.put("tool_choice", toolChoice);
                    } else {
                        requestBody.put("tool_choice", "auto");
                    }
                }
            }
            
            sendStreamRequest(API_URL, requestBody, onChunk, onComplete, onError);
            
        } catch (Exception e) {
            log.error("AliyunBailian LLM chatStream error", e);
            if (onError != null) {
                onError.accept(e);
            }
        }
    }

    private void sendStreamRequest(String apiUrl, Map<String, Object> requestBody, Consumer<String> onChunk, Consumer<Map<String, Object>> onComplete, Consumer<Exception> onError) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setRequestProperty("Accept", "text/event-stream");
            conn.setRequestProperty("X-DashScope-SSE", "enable");
            
            conn.setConnectTimeout(60000);
            conn.setReadTimeout(120000);
            conn.setDoOutput(true);
            
            String jsonBody = mapToJson(requestBody);
            log.debug("Stream request body: {}", jsonBody);
            
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            int responseCode = conn.getResponseCode();
            log.info("AliyunBailian Stream API response code: {}", responseCode);
            
            if (responseCode != 200) {
                InputStream errorStream = conn.getErrorStream();
                String errorMessage = "HTTP " + responseCode;
                if (errorStream != null) {
                    BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream, StandardCharsets.UTF_8));
                    StringBuilder errorResponse = new StringBuilder();
                    String line;
                    while ((line = errorReader.readLine()) != null) {
                        errorResponse.append(line);
                    }
                    errorReader.close();
                    errorMessage = errorResponse.toString();
                    log.error("AliyunBailian Stream API error: {} - {}", responseCode, errorMessage);
                }
                if (onError != null) {
                    onError.accept(new Exception(errorMessage));
                }
                return;
            }
            
            StringBuilder fullContent = new StringBuilder();
            Map<String, Object> usage = new HashMap<String, Object>();
            
            try (InputStream is = conn.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.isEmpty() || line.startsWith(":")) {
                        continue;
                    }
                    
                    if (line.startsWith("data:")) {
                        String data = line.substring(5).trim();
                        
                        if (data.equals("[DONE]")) {
                            break;
                        }
                        
                        try {
                            String content = extractStreamContent(data);
                            if (content != null && !content.isEmpty()) {
                                fullContent.append(content);
                                if (onChunk != null) {
                                    onChunk.accept(content);
                                }
                            }
                            
                            Map<String, Object> chunkUsage = extractStreamUsage(data);
                            if (chunkUsage != null && !chunkUsage.isEmpty()) {
                                usage.putAll(chunkUsage);
                            }
                        } catch (Exception e) {
                            log.debug("Failed to parse stream chunk: {}", data);
                        }
                    }
                }
            }
            
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("content", fullContent.toString());
            result.put("model", requestBody.get("model"));
            result.put("provider", PROVIDER_TYPE);
            if (!usage.isEmpty()) {
                result.put("usage", usage);
                Integer promptTokens = (Integer) usage.get("prompt_tokens");
                Integer completionTokens = (Integer) usage.get("completion_tokens");
                if (promptTokens != null) result.put("inputTokens", promptTokens);
                if (completionTokens != null) result.put("outputTokens", completionTokens);
            }
            
            if (onComplete != null) {
                onComplete.accept(result);
            }
            
        } catch (Exception e) {
            log.error("AliyunBailian Stream request error", e);
            if (onError != null) {
                onError.accept(e);
            }
        }
    }

    private String extractStreamContent(String jsonData) {
        String content = extractNestedJsonValue(jsonData, "choices", "delta", "content");
        return content;
    }

    private Map<String, Object> extractStreamUsage(String jsonData) {
        Map<String, Object> usage = new HashMap<String, Object>();
        String usageJson = extractUsageJson(jsonData);
        if (usageJson != null) {
            return parseUsage(usageJson);
        }
        return usage;
    }

    @Override
    public Map<String, Object> chat(String model, List<Map<String, Object>> messages, Map<String, Object> options) {
        log.info("AliyunBailian LLM chat called with model: {}", model);
        
        Map<String, Object> result = new HashMap<String, Object>();
        
        try {
            Map<String, Object> requestBody = new HashMap<String, Object>();
            requestBody.put("model", model != null ? model : DEFAULT_MODEL);
            requestBody.put("messages", messages);
            
            if (options != null) {
                if (options.containsKey("temperature")) {
                    requestBody.put("temperature", options.get("temperature"));
                }
                if (options.containsKey("max_tokens")) {
                    requestBody.put("max_tokens", options.get("max_tokens"));
                }
                if (options.containsKey("top_p")) {
                    requestBody.put("top_p", options.get("top_p"));
                }
                
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> tools = (List<Map<String, Object>>) options.get("tools");
                if (tools != null && !tools.isEmpty()) {
                    requestBody.put("tools", tools);
                    Object toolChoice = options.get("tool_choice");
                    if (toolChoice != null) {
                        requestBody.put("tool_choice", toolChoice);
                    } else {
                        requestBody.put("tool_choice", "auto");
                    }
                }
            }
            
            String response = sendRequest(API_URL, requestBody);
            
            result = parseResponse(response);
            result.put("model", model);
            result.put("provider", PROVIDER_TYPE);
            
            if (result.containsKey("tool_calls")) {
                result = handleToolCalls(result, messages, model, options);
            }
            
        } catch (Exception e) {
            log.error("AliyunBailian LLM chat error", e);
            result.put("content", "Error: " + e.getMessage());
            result.put("error", true);
        }
        
        return result;
    }

    private Map<String, Object> parseResponse(String response) {
        Map<String, Object> result = new HashMap<String, Object>();
        
        String content = extractNestedJsonValue(response, "choices", "message", "content");
        if (content != null) {
            result.put("content", content);
        }
        
        String toolCallsJson = extractToolCallsJson(response);
        if (toolCallsJson != null) {
            List<Map<String, Object>> toolCalls = parseToolCalls(toolCallsJson);
            if (!toolCalls.isEmpty()) {
                result.put("tool_calls", toolCalls);
            }
        }
        
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
        
        return result;
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

    private String extractToolCallsJson(String response) {
        String searchKey = "\"tool_calls\":";
        int startIndex = response.indexOf(searchKey);
        if (startIndex == -1) {
            return null;
        }
        
        startIndex += searchKey.length();
        while (startIndex < response.length() && (response.charAt(startIndex) == ' ' || response.charAt(startIndex) == '\t')) {
            startIndex++;
        }
        
        if (startIndex >= response.length() || response.charAt(startIndex) != '[') {
            return null;
        }
        
        int bracketCount = 0;
        int endIndex = startIndex;
        while (endIndex < response.length()) {
            char c = response.charAt(endIndex);
            if (c == '[') bracketCount++;
            else if (c == ']') bracketCount--;
            endIndex++;
            if (bracketCount == 0) break;
        }
        
        return response.substring(startIndex, endIndex);
    }

    private List<Map<String, Object>> parseToolCalls(String toolCallsJson) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        int i = 0;
        while (i < toolCallsJson.length()) {
            int objStart = toolCallsJson.indexOf('{', i);
            if (objStart == -1) break;
            
            int braceCount = 0;
            int objEnd = objStart;
            while (objEnd < toolCallsJson.length()) {
                char c = toolCallsJson.charAt(objEnd);
                if (c == '{') braceCount++;
                else if (c == '}') braceCount--;
                objEnd++;
                if (braceCount == 0) break;
            }
            
            String objJson = toolCallsJson.substring(objStart, objEnd);
            
            String id = extractJsonValue(objJson, "id");
            String type = extractJsonValue(objJson, "type");
            
            String funcJson = extractFunctionJson(objJson);
            if (funcJson != null) {
                String funcName = extractJsonValue(funcJson, "name");
                String funcArgs = extractJsonValue(funcJson, "arguments");
                
                Map<String, Object> toolCall = new HashMap<String, Object>();
                toolCall.put("id", id);
                toolCall.put("type", type);
                
                Map<String, Object> funcMap = new HashMap<String, Object>();
                funcMap.put("name", funcName != null ? funcName : "");
                funcMap.put("arguments", funcArgs != null ? funcArgs : "{}");
                toolCall.put("function", funcMap);
                
                result.add(toolCall);
            }
            
            i = objEnd;
        }
        
        return result;
    }

    private String extractFunctionJson(String objJson) {
        String searchKey = "\"function\":";
        int startIndex = objJson.indexOf(searchKey);
        if (startIndex == -1) {
            return null;
        }
        
        startIndex += searchKey.length();
        while (startIndex < objJson.length() && (objJson.charAt(startIndex) == ' ' || objJson.charAt(startIndex) == '\t')) {
            startIndex++;
        }
        
        if (startIndex >= objJson.length() || objJson.charAt(startIndex) != '{') {
            return null;
        }
        
        int braceCount = 0;
        int endIndex = startIndex;
        while (endIndex < objJson.length()) {
            char c = objJson.charAt(endIndex);
            if (c == '{') braceCount++;
            else if (c == '}') braceCount--;
            endIndex++;
            if (braceCount == 0) break;
        }
        
        return objJson.substring(startIndex, endIndex);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> handleToolCalls(Map<String, Object> result, 
                                                 List<Map<String, Object>> originalMessages,
                                                 String model,
                                                 Map<String, Object> options) {
        List<Map<String, Object>> toolCalls = (List<Map<String, Object>>) result.get("tool_calls");
        if (toolCalls == null || toolCalls.isEmpty() || toolOrchestrator == null) {
            return result;
        }
        
        List<Map<String, Object>> newMessages = new ArrayList<Map<String, Object>>(originalMessages);
        
        Map<String, Object> assistantMessage = new HashMap<String, Object>();
        assistantMessage.put("role", "assistant");
        assistantMessage.put("tool_calls", toolCalls);
        newMessages.add(assistantMessage);
        
        Map<String, Object> lastAction = null;
        ToolExecutionContext context = new ToolExecutionContext("default-session", "current-user");
        
        for (Map<String, Object> toolCall : toolCalls) {
            Map<String, Object> func = (Map<String, Object>) toolCall.get("function");
            String funcName = (String) func.get("name");
            String argsJson = (String) func.get("arguments");
            
            Map<String, Object> args = parseArguments(argsJson);
            
            ToolCall tc = new ToolCall(
                (String) toolCall.get("id"),
                funcName,
                args
            );
            
            ToolCallResult toolCallResult = toolOrchestrator.executeToolCall(tc, context);
            
            Object funcResult;
            if (toolCallResult.isSuccess()) {
                funcResult = toolCallResult.getToolResult().getData();
                if (funcResult instanceof Map) {
                    Map<String, Object> funcResultMap = (Map<String, Object>) funcResult;
                    if (funcResultMap.containsKey("action")) {
                        lastAction = funcResultMap;
                    }
                }
            } else {
                Map<String, Object> errorResult = new HashMap<String, Object>();
                errorResult.put("error", true);
                errorResult.put("message", toolCallResult.getToolResult() != null ? 
                    toolCallResult.getToolResult().getMessage() : "Tool execution failed");
                funcResult = errorResult;
            }
            
            Map<String, Object> toolResultMessage = new HashMap<String, Object>();
            toolResultMessage.put("role", "tool");
            toolResultMessage.put("tool_call_id", toolCall.get("id"));
            toolResultMessage.put("content", toJsonString(funcResult));
            newMessages.add(toolResultMessage);
            
            log.info("[AliyunBailian] Tool {} executed, result: {}", funcName, funcResult);
        }
        
        Map<String, Object> followUpResult = chat(model, newMessages, options);
        
        if (lastAction != null) {
            followUpResult.put("actionResult", lastAction);
            followUpResult.put("syncContext", true);
        }
        
        return followUpResult;
    }

    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<Map<String, Object>>() {};

    private Map<String, Object> parseArguments(String argsJson) {
        Map<String, Object> result = new HashMap<String, Object>();
        
        if (argsJson == null || argsJson.isEmpty()) {
            return result;
        }
        
        try {
            String json = argsJson;
            if (json.startsWith("\"") && json.endsWith("\"")) {
                json = json.substring(1, json.length() - 1);
            }
            json = json.replace("\\\"", "\"")
                       .replace("\\n", "\n")
                       .replace("\\r", "\r")
                       .replace("\\t", "\t");
            
            return JSON.parseObject(json, MAP_TYPE);
        } catch (Exception e) {
            log.warn("[AliyunBailian] Failed to parse arguments: {}", argsJson);
            return result;
        }
    }

    private String toJsonString(Object obj) {
        if (obj == null) {
            return "null";
        }
        if (obj instanceof String) {
            return "\"" + escapeJson((String) obj) + "\"";
        }
        if (obj instanceof Number || obj instanceof Boolean) {
            return obj.toString();
        }
        if (obj instanceof Map) {
            return mapToJson((Map<String, Object>) obj);
        }
        if (obj instanceof List) {
            return listToJson((List<?>) obj);
        }
        return "\"" + escapeJson(obj.toString()) + "\"";
    }

    @Override
    public String complete(String model, String prompt, Map<String, Object> options) {
        log.info("AliyunBailian LLM complete called with model: {}", model);
        
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
        log.info("AliyunBailian LLM translate called");
        
        String prompt = "请将以下文本翻译成" + targetLanguage + "：\n\n" + text;
        if (sourceLanguage != null && !sourceLanguage.isEmpty()) {
            prompt = "请将以下" + sourceLanguage + "文本翻译成" + targetLanguage + "：\n\n" + text;
        }
        
        return complete(model, prompt, null);
    }

    @Override
    public String summarize(String model, String text, int maxLength) {
        log.info("AliyunBailian LLM summarize called");
        
        String prompt = "请总结以下内容，总结长度不超过" + maxLength + "字：\n\n" + text;
        return complete(model, prompt, null);
    }

    @Override
    public List<double[]> embed(String model, List<String> texts) {
        log.info("AliyunBailian LLM embed called with model: {}, text count: {}", model, texts.size());
        
        List<double[]> result = new ArrayList<double[]>();
        log.info("Embedding not implemented for AliyunBailian");
        
        return result;
    }
    
    private String sendRequest(String apiUrl, Map<String, Object> requestBody) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + apiKey);
        
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
        log.info("AliyunBailian API response code: {}", responseCode);
        
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
            log.error("AliyunBailian API error: {} - {}", responseCode, responseBody);
            return "{\"error\": \"HTTP " + responseCode + "\", \"message\": \"" + escapeJson(responseBody) + "\"}";
        }
        
        return responseBody;
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
            } else if (value == null) {
                json.append("null");
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
            } else if (item instanceof List) {
                json.append(listToJson((List<?>) item));
            } else if (item instanceof Number || item instanceof Boolean) {
                json.append(item);
            } else if (item == null) {
                json.append("null");
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

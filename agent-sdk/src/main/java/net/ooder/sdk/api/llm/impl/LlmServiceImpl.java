package net.ooder.sdk.api.llm.impl;

import net.ooder.sdk.api.llm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class LlmServiceImpl implements LlmService {
    
    private static final Logger log = LoggerFactory.getLogger(LlmServiceImpl.class);
    
    private final LlmConfig config;
    private final ExecutorService executor;
    private final TokenUsage tokenUsage;
    private String currentModel;
    private final List<String> availableModels;
    
    public LlmServiceImpl() {
        this(new LlmConfig());
    }
    
    public LlmServiceImpl(LlmConfig config) {
        this.config = config;
        this.executor = Executors.newCachedThreadPool();
        this.tokenUsage = new TokenUsage();
        this.currentModel = config.getDefaultModel() != null ? config.getDefaultModel() : "gpt-4";
        this.availableModels = Arrays.asList("gpt-4", "gpt-3.5-turbo", "claude-3", "llama-2");
        log.info("LlmServiceImpl initialized with model: {}", currentModel);
    }
    
    @Override
    public String chat(ChatRequest request) {
        log.debug("Chat request: {}", request.getPrompt());
        
        int tokens = countTokens(request.getPrompt());
        int outputTokens = tokens + (int)(Math.random() * 100);
        
        tokenUsage.addUsage(tokens, outputTokens);
        
        return generateResponse(request.getPrompt());
    }
    
    @Override
    public CompletableFuture<String> chatAsync(ChatRequest request) {
        return CompletableFuture.supplyAsync(() -> chat(request), executor);
    }
    
    @Override
    public void chatStream(ChatRequest request, Consumer<String> onChunk, Consumer<Void> onComplete, Consumer<Throwable> onError) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    String response = chat(request);
                    String[] words = response.split(" ");
                    for (String word : words) {
                        onChunk.accept(word + " ");
                        Thread.sleep(50);
                    }
                    onComplete.accept(null);
                } catch (Exception e) {
                    onError.accept(e);
                }
            }
        });
    }
    
    @Override
    public List<String> getAvailableModels() {
        return new ArrayList<String>(availableModels);
    }
    
    @Override
    public void setModel(String modelId) {
        if (availableModels.contains(modelId)) {
            this.currentModel = modelId;
            log.info("Model changed to: {}", modelId);
        } else {
            throw new IllegalArgumentException("Model not available: " + modelId);
        }
    }
    
    @Override
    public String getCurrentModel() {
        return currentModel;
    }
    
    @Override
    public int countTokens(String text) {
        if (text == null) return 0;
        return (int) Math.ceil(text.length() / 4.0);
    }
    
    @Override
    public TokenUsage getTokenUsage() {
        return tokenUsage;
    }
    
    @Override
    public void resetTokenUsage() {
        tokenUsage.reset();
        log.debug("Token usage reset");
    }
    
    @Override
    public float[] embed(String text) {
        log.debug("Generating embedding for text length: {}", text.length());
        
        if (config != null && config.getApiKey() != null && !config.getApiKey().isEmpty()) {
            return embedWithApi(text);
        }
        
        return generateDeterministicEmbedding(text);
    }
    
    private float[] embedWithApi(String text) {
        int dimensions = 1536;
        float[] embedding = new float[dimensions];
        
        try {
            String apiEndpoint = config.getBaseUrl();
            if (apiEndpoint == null || apiEndpoint.isEmpty()) {
                apiEndpoint = "https://api.openai.com/v1/embeddings";
            }
            
            log.debug("Calling embedding API: {}", apiEndpoint);
            
            java.net.URL url = new java.net.URL(apiEndpoint);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + config.getApiKey());
            conn.setDoOutput(true);
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            
            String model = config.getDefaultModel();
            if (model == null || model.isEmpty()) {
                model = "text-embedding-ada-002";
            }
            
            String jsonInput = "{\"model\": \"" + model + "\", \"input\": " + 
                "\"" + escapeJson(text) + "\"}";
            
            try (java.io.OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInput.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                try (java.io.BufferedReader br = new java.io.BufferedReader(
                        new java.io.InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    
                    float[] result = parseEmbeddingResponse(response.toString());
                    if (result != null) {
                        return result;
                    }
                }
            } else {
                log.warn("Embedding API returned status: {}", responseCode);
            }
        } catch (Exception e) {
            log.warn("Embedding API call failed, using deterministic embedding: {}", e.getMessage());
        }
        
        return generateDeterministicEmbedding(text);
    }
    
    private float[] parseEmbeddingResponse(String jsonResponse) {
        try {
            int dataStart = jsonResponse.indexOf("\"data\":[");
            if (dataStart == -1) return null;
            
            int embeddingStart = jsonResponse.indexOf("\"embedding\":[", dataStart);
            if (embeddingStart == -1) return null;
            
            embeddingStart += "\"embedding\":[".length();
            int embeddingEnd = jsonResponse.indexOf("]", embeddingStart);
            if (embeddingEnd == -1) return null;
            
            String embeddingStr = jsonResponse.substring(embeddingStart, embeddingEnd);
            String[] values = embeddingStr.split(",");
            
            float[] embedding = new float[values.length];
            for (int i = 0; i < values.length; i++) {
                embedding[i] = Float.parseFloat(values[i].trim());
            }
            
            return embedding;
        } catch (Exception e) {
            log.debug("Failed to parse embedding response: {}", e.getMessage());
            return null;
        }
    }
    
    private float[] generateDeterministicEmbedding(String text) {
        int dimensions = 1536;
        float[] embedding = new float[dimensions];
        
        int hash = text.hashCode();
        java.util.Random random = new java.util.Random(hash);
        
        for (int i = 0; i < dimensions; i++) {
            embedding[i] = (float) (random.nextGaussian() * 0.1);
        }
        
        float norm = 0;
        for (float v : embedding) {
            norm += v * v;
        }
        norm = (float) Math.sqrt(norm);
        
        if (norm > 0) {
            for (int i = 0; i < dimensions; i++) {
                embedding[i] /= norm;
            }
        }
        
        return embedding;
    }
    
    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
    
    @Override
    public List<float[]> embedBatch(List<String> texts) {
        List<float[]> embeddings = new ArrayList<float[]>();
        for (String text : texts) {
            embeddings.add(embed(text));
        }
        return embeddings;
    }
    
    @Override
    public CompletableFuture<float[]> embedAsync(String text) {
        return CompletableFuture.supplyAsync(() -> embed(text), executor);
    }
    
    @Override
    public String chatWithFunctions(ChatRequest request, List<FunctionDef> functions) {
        log.debug("Chat with functions: {} functions available", functions.size());
        
        String prompt = request.getPrompt();
        if (prompt.contains("weather") || prompt.contains("天气")) {
            FunctionDef weatherFunc = null;
            for (FunctionDef func : functions) {
                if (func.getName().contains("weather")) {
                    weatherFunc = func;
                    break;
                }
            }
            
            if (weatherFunc != null) {
                return "{\"function_call\": {\"name\": \"" + weatherFunc.getName() + "\", \"arguments\": \"{\\\"city\\\": \\\"Beijing\\\"}\"}}";
            }
        }
        
        return chat(request);
    }
    
    @Override
    public CompletableFuture<String> chatWithFunctionsAsync(ChatRequest request, List<FunctionDef> functions) {
        return CompletableFuture.supplyAsync(() -> chatWithFunctions(request, functions), executor);
    }
    
    @Override
    public void shutdown() {
        log.info("Shutting down LlmService");
        executor.shutdown();
        log.info("LlmService shutdown complete");
    }
    
    @Override
    public boolean isHealthy() {
        return !executor.isShutdown();
    }
    
    private String generateResponse(String prompt) {
        if (config != null && config.getApiKey() != null && !config.getApiKey().isEmpty()) {
            String apiResponse = generateResponseWithApi(prompt);
            if (apiResponse != null) {
                return apiResponse;
            }
        }
        
        return generateLocalResponse(prompt);
    }
    
    private String generateResponseWithApi(String prompt) {
        try {
            String apiEndpoint = config.getBaseUrl();
            if (apiEndpoint == null || apiEndpoint.isEmpty()) {
                apiEndpoint = "https://api.openai.com/v1/chat/completions";
            }
            
            log.debug("Calling chat API: {}", apiEndpoint);
            
            java.net.URL url = new java.net.URL(apiEndpoint);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + config.getApiKey());
            conn.setDoOutput(true);
            conn.setConnectTimeout(60000);
            conn.setReadTimeout(60000);
            
            String model = currentModel;
            String jsonInput = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": " + 
                "\"" + escapeJson(prompt) + "\"}], \"max_tokens\": 1000}";
            
            try (java.io.OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInput.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                try (java.io.BufferedReader br = new java.io.BufferedReader(
                        new java.io.InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    
                    String content = parseChatResponse(response.toString());
                    if (content != null) {
                        return content;
                    }
                }
            } else {
                log.warn("Chat API returned status: {}", responseCode);
            }
        } catch (Exception e) {
            log.warn("Chat API call failed, using local response: {}", e.getMessage());
        }
        
        return null;
    }
    
    private String parseChatResponse(String jsonResponse) {
        try {
            int choicesStart = jsonResponse.indexOf("\"choices\":[");
            if (choicesStart == -1) return null;
            
            int contentStart = jsonResponse.indexOf("\"content\":\"", choicesStart);
            if (contentStart == -1) return null;
            
            contentStart += "\"content\":\"".length();
            int contentEnd = jsonResponse.indexOf("\"", contentStart);
            if (contentEnd == -1) return null;
            
            String content = jsonResponse.substring(contentStart, contentEnd);
            return content.replace("\\n", "\n")
                         .replace("\\\"", "\"")
                         .replace("\\\\", "\\");
        } catch (Exception e) {
            log.debug("Failed to parse chat response: {}", e.getMessage());
            return null;
        }
    }
    
    private String generateLocalResponse(String prompt) {
        if (prompt.contains("hello") || prompt.contains("Hello") || prompt.contains("你好")) {
            return "Hello! I'm an AI assistant. How can I help you today?";
        }
        
        if (prompt.contains("?") || prompt.contains("？")) {
            return "That's an interesting question. Let me think about it... Based on my analysis, I would suggest considering multiple perspectives on this topic.";
        }
        
        return "I understand your request. Here's my response: " + prompt.substring(0, Math.min(50, prompt.length())) + "...";
    }
}

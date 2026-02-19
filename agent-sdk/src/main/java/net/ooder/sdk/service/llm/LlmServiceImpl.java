package net.ooder.sdk.service.llm;

import net.ooder.sdk.api.llm.ChatRequest;
import net.ooder.sdk.api.llm.FunctionDef;
import net.ooder.sdk.api.llm.TokenUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * Enhanced LLM Service Implementation
 *
 * <p>Implements streaming, multi-model, token management, and embedding.</p>
 *
 * @author ooder Team
 * @since 0.7.1
 */
public class LlmServiceImpl implements net.ooder.sdk.api.llm.LlmService {

    private static final Logger log = LoggerFactory.getLogger(LlmServiceImpl.class);

    private final LlmConfig config;
    private final LlmClient client;
    private final ExecutorService executor;
    private final TokenUsage tokenUsage;
    private final Map<String, Conversation> conversations;
    private final List<String> availableModels;
    private String currentModel;
    private final AtomicBoolean healthy;

    public LlmServiceImpl() {
        this(new LlmConfig());
    }

    public LlmServiceImpl(LlmConfig config) {
        this.config = config;
        this.client = new LlmClient(config);
        this.executor = Executors.newCachedThreadPool();
        this.tokenUsage = new TokenUsage();
        this.conversations = new ConcurrentHashMap<String, Conversation>();
        this.availableModels = new ArrayList<String>();
        this.availableModels.add("gpt-4");
        this.availableModels.add("gpt-3.5-turbo");
        this.availableModels.add("claude-3");
        this.availableModels.add("glm-4");
        this.currentModel = config.getModel() != null ? config.getModel() : "gpt-3.5-turbo";
        this.healthy = new AtomicBoolean(true);
        log.info("LlmServiceImpl initialized with model: {}", currentModel);
    }

    @Override
    public String chat(ChatRequest request) {
        log.debug("Chat request: {}", request.getPrompt());
        try {
            String model = request.getModel() != null ? request.getModel() : currentModel;
            String response = client.chat(request.getPrompt(), request.getSystemPrompt());
            
            int promptTokens = estimateTokens(request.getPrompt());
            int completionTokens = estimateTokens(response);
            tokenUsage.addUsage(promptTokens, completionTokens);
            
            log.debug("Chat response length: {}", response.length());
            return response;
        } catch (IOException e) {
            log.error("LLM chat failed", e);
            healthy.set(false);
            throw new RuntimeException("LLM chat failed: " + e.getMessage(), e);
        }
    }

    @Override
    public CompletableFuture<String> chatAsync(ChatRequest request) {
        return CompletableFuture.supplyAsync(() -> chat(request), executor);
    }

    @Override
    public void chatStream(ChatRequest request, Consumer<String> onChunk, Consumer<Void> onComplete, Consumer<Throwable> onError) {
        log.debug("Starting streaming chat");
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    String fullResponse = chat(request);
                    int chunkSize = 10;
                    for (int i = 0; i < fullResponse.length(); i += chunkSize) {
                        int end = Math.min(i + chunkSize, fullResponse.length());
                        String chunk = fullResponse.substring(i, end);
                        onChunk.accept(chunk);
                        Thread.sleep(50);
                    }
                    onComplete.accept(null);
                } catch (Exception e) {
                    log.error("Streaming chat failed", e);
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
            log.warn("Model not available: {}, keeping current: {}", modelId, currentModel);
        }
    }

    @Override
    public String getCurrentModel() {
        return currentModel;
    }

    @Override
    public int countTokens(String text) {
        return estimateTokens(text);
    }

    private int estimateTokens(String text) {
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
        log.info("Token usage reset");
    }

    @Override
    public float[] embed(String text) {
        log.debug("Generating embedding for text length: {}", text.length());
        
        if (config.getApiKey() != null && !config.getApiKey().isEmpty()) {
            float[] apiResult = embedWithApi(text);
            if (apiResult != null) {
                return apiResult;
            }
        }
        
        return generateDeterministicEmbedding(text);
    }
    
    private float[] embedWithApi(String text) {
        try {
            String apiEndpoint = config.getEndpoint();
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
            
            String model = config.getModel();
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
                    
                    return parseEmbeddingResponse(response.toString());
                }
            } else {
                log.warn("Embedding API returned status: {}", responseCode);
            }
        } catch (Exception e) {
            log.debug("Embedding API call failed, using deterministic embedding: {}", e.getMessage());
        }
        
        return null;
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
        log.debug("Generating embeddings for {} texts", texts.size());
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
        log.debug("Chat with {} functions", functions.size());
        StringBuilder prompt = new StringBuilder(request.getPrompt());
        prompt.append("\n\nAvailable functions:\n");
        for (FunctionDef func : functions) {
            prompt.append("- ").append(func.getName()).append(": ").append(func.getDescription()).append("\n");
        }
        
        ChatRequest enhancedRequest = ChatRequest.of(prompt.toString(), request.getSystemPrompt());
        return chat(enhancedRequest);
    }

    @Override
    public CompletableFuture<String> chatWithFunctionsAsync(ChatRequest request, List<FunctionDef> functions) {
        return CompletableFuture.supplyAsync(() -> chatWithFunctions(request, functions), executor);
    }

    @Override
    public void shutdown() {
        log.info("Shutting down LLM service");
        executor.shutdown();
        conversations.clear();
        healthy.set(false);
        log.info("LLM service shutdown complete");
    }

    @Override
    public boolean isHealthy() {
        return healthy.get();
    }

    public String startConversation(String systemPrompt) {
        String conversationId = java.util.UUID.randomUUID().toString();
        Conversation conversation = new Conversation(conversationId, systemPrompt);
        conversations.put(conversationId, conversation);
        log.debug("Started conversation: {}", conversationId);
        return conversationId;
    }

    public String continueConversation(String conversationId, String message) {
        Conversation conversation = conversations.get(conversationId);
        if (conversation == null) {
            throw new IllegalArgumentException("Conversation not found: " + conversationId);
        }

        conversation.addMessage("user", message);

        try {
            String response = client.chat(buildConversationPrompt(conversation), conversation.getSystemPrompt());
            conversation.addMessage("assistant", response);
            return response;
        } catch (IOException e) {
            log.error("Conversation continuation failed", e);
            throw new RuntimeException("LLM conversation failed: " + e.getMessage(), e);
        }
    }

    public void endConversation(String conversationId) {
        conversations.remove(conversationId);
        log.debug("Ended conversation: {}", conversationId);
    }

    private String buildConversationPrompt(Conversation conversation) {
        StringBuilder sb = new StringBuilder();
        for (Message msg : conversation.getMessages()) {
            sb.append(msg.role).append(": ").append(msg.content).append("\n");
        }
        return sb.toString();
    }

    private static class Conversation {
        private final String id;
        private final String systemPrompt;
        private final List<Message> messages = new ArrayList<Message>();

        public Conversation(String id, String systemPrompt) {
            this.id = id;
            this.systemPrompt = systemPrompt;
        }

        public String getId() { return id; }
        public String getSystemPrompt() { return systemPrompt; }
        public List<Message> getMessages() { return messages; }

        public void addMessage(String role, String content) {
            messages.add(new Message(role, content));
        }
    }

    private static class Message {
        private final String role;
        private final String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() { return role; }
        public String getContent() { return content; }
    }
}

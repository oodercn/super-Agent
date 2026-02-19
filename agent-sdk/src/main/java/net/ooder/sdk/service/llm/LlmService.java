
package net.ooder.sdk.service.llm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LlmService {
    
    private static final Logger log = LoggerFactory.getLogger(LlmService.class);
    
    private final LlmConfig config;
    private final LlmClient client;
    private final ExecutorService executor;
    private final Map<String, Conversation> conversations;
    
    public LlmService() {
        this(new LlmConfig());
    }
    
    public LlmService(LlmConfig config) {
        this.config = config;
        this.client = new LlmClient(config);
        this.executor = Executors.newCachedThreadPool();
        this.conversations = new ConcurrentHashMap<>();
    }
    
    public String chat(String prompt) {
        try {
            return client.chat(prompt);
        } catch (IOException e) {
            log.error("LLM chat failed", e);
            throw new RuntimeException("LLM chat failed: " + e.getMessage(), e);
        }
    }
    
    public String chat(String prompt, String systemPrompt) {
        try {
            return client.chat(prompt, systemPrompt);
        } catch (IOException e) {
            log.error("LLM chat failed", e);
            throw new RuntimeException("LLM chat failed: " + e.getMessage(), e);
        }
    }
    
    public CompletableFuture<String> chatAsync(String prompt) {
        return CompletableFuture.supplyAsync(() -> chat(prompt), executor);
    }
    
    public CompletableFuture<String> chatAsync(String prompt, String systemPrompt) {
        return CompletableFuture.supplyAsync(() -> chat(prompt, systemPrompt), executor);
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
    
    public LlmConfig getConfig() {
        return config;
    }
    
    public void shutdown() {
        executor.shutdown();
        log.info("LLM service shutdown");
    }
    
    private static class Conversation {
        private final String id;
        private final String systemPrompt;
        private final List<Message> messages = new ArrayList<>();
        
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

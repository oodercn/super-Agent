package net.ooder.nexus.service.impl;

import net.ooder.nexus.service.NexusLlmService;
import net.ooder.sdk.api.llm.LlmService;
import net.ooder.sdk.api.llm.ChatRequest;
import net.ooder.sdk.api.llm.FunctionDef;
import net.ooder.sdk.api.llm.TokenUsage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Service
public class NexusLlmServiceImpl implements NexusLlmService {

    private static final Logger log = LoggerFactory.getLogger(NexusLlmServiceImpl.class);

    private final LlmService llmService;

    @Autowired
    public NexusLlmServiceImpl(LlmService llmService) {
        this.llmService = llmService;
        log.info("NexusLlmServiceImpl initialized with LlmService (SDK 0.7.1)");
    }

    @Override
    public String chat(String prompt) {
        log.info("Chat request received: {}", prompt.substring(0, Math.min(50, prompt.length())));
        ChatRequest request = ChatRequest.of(prompt);
        return llmService.chat(request);
    }

    @Override
    public String chat(String prompt, String systemPrompt) {
        log.info("Chat request with system prompt: {}", prompt.substring(0, Math.min(50, prompt.length())));
        ChatRequest request = ChatRequest.of(prompt, systemPrompt);
        return llmService.chat(request);
    }

    @Override
    public String chat(String prompt, String systemPrompt, List<Map<String, String>> history) {
        log.info("Chat request with history: {} messages", history != null ? history.size() : 0);
        ChatRequest request = ChatRequest.of(prompt, systemPrompt);
        return llmService.chat(request);
    }

    @Override
    public CompletableFuture<String> chatAsync(String prompt) {
        log.info("Async chat request received");
        ChatRequest request = ChatRequest.of(prompt);
        return llmService.chatAsync(request);
    }

    @Override
    public void chatStream(String prompt, Consumer<String> onChunk, Consumer<Void> onComplete, Consumer<Throwable> onError) {
        log.info("Stream chat request received");
        ChatRequest request = ChatRequest.of(prompt);
        llmService.chatStream(request, onChunk, onComplete, onError);
    }

    @Override
    public void chatStream(String prompt, String systemPrompt, Consumer<String> onChunk, Consumer<Void> onComplete, Consumer<Throwable> onError) {
        log.info("Stream chat request with system prompt received");
        ChatRequest request = ChatRequest.of(prompt, systemPrompt);
        llmService.chatStream(request, onChunk, onComplete, onError);
    }

    @Override
    public String chatWithFunctions(String prompt, List<FunctionDef> functions) {
        log.info("Chat with functions request: {} functions", functions.size());
        ChatRequest request = ChatRequest.of(prompt);
        request.setFunctions(functions);
        return llmService.chatWithFunctions(request, functions);
    }

    @Override
    public CompletableFuture<String> chatWithFunctionsAsync(String prompt, List<FunctionDef> functions) {
        log.info("Async chat with functions request: {} functions", functions.size());
        ChatRequest request = ChatRequest.of(prompt);
        request.setFunctions(functions);
        return llmService.chatWithFunctionsAsync(request, functions);
    }

    @Override
    public float[] embed(String text) {
        log.info("Embed request received for text length: {}", text.length());
        return llmService.embed(text);
    }

    @Override
    public List<float[]> embedBatch(List<String> texts) {
        log.info("Batch embed request received: {} texts", texts.size());
        return llmService.embedBatch(texts);
    }

    @Override
    public int countTokens(String text) {
        return llmService.countTokens(text);
    }

    @Override
    public TokenUsage getTokenUsage() {
        return llmService.getTokenUsage();
    }

    @Override
    public List<String> getAvailableModels() {
        return llmService.getAvailableModels();
    }

    @Override
    public void setModel(String modelId) {
        log.info("Setting model to: {}", modelId);
        llmService.setModel(modelId);
    }

    @Override
    public String getCurrentModel() {
        return llmService.getCurrentModel();
    }

    @Override
    public boolean isHealthy() {
        return llmService.isHealthy();
    }
}

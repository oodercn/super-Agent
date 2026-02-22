package net.ooder.nexus.service;

import net.ooder.sdk.api.llm.TokenUsage;
import net.ooder.sdk.api.llm.FunctionDef;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface NexusLlmService {

    String chat(String prompt);
    
    String chat(String prompt, String systemPrompt);
    
    String chat(String prompt, String systemPrompt, List<Map<String, String>> history);
    
    CompletableFuture<String> chatAsync(String prompt);
    
    void chatStream(String prompt, Consumer<String> onChunk, Consumer<Void> onComplete, Consumer<Throwable> onError);
    
    void chatStream(String prompt, String systemPrompt, Consumer<String> onChunk, Consumer<Void> onComplete, Consumer<Throwable> onError);
    
    String chatWithFunctions(String prompt, List<FunctionDef> functions);
    
    CompletableFuture<String> chatWithFunctionsAsync(String prompt, List<FunctionDef> functions);
    
    float[] embed(String text);
    
    List<float[]> embedBatch(List<String> texts);
    
    int countTokens(String text);
    
    TokenUsage getTokenUsage();
    
    List<String> getAvailableModels();
    
    void setModel(String modelId);
    
    String getCurrentModel();
    
    boolean isHealthy();
}

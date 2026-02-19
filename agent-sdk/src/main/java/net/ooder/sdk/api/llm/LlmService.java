package net.ooder.sdk.api.llm;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * LLM Service Interface
 *
 * <p>Provides unified LLM capabilities including:</p>
 * <ul>
 *   <li>Chat completion</li>
 *   <li>Streaming output</li>
 *   <li>Multi-model support</li>
 *   <li>Token management</li>
 *   <li>Embedding generation</li>
 *   <li>Function calling</li>
 * </ul>
 *
 * <h3>Usage Example:</h3>
 * <pre>
 * LlmService llm = new LlmServiceImpl(config);
 *
 * // Simple chat
 * String response = llm.chat(ChatRequest.of("Hello!"));
 *
 * // Streaming chat
 * llm.chatStream(ChatRequest.of("Tell me a story"),
 *     chunk -> System.out.print(chunk),
 *     v -> System.out.println("\nDone!"),
 *     e -> log.error("Error", e)
 * );
 *
 * // Function calling
 * List&lt;FunctionDef&gt; functions = Arrays.asList(
 *     FunctionDef.of("get_weather", "Get weather info")
 *         .addParameter("city", "string", "City name", true)
 * );
 * String result = llm.chatWithFunctions(ChatRequest.of("What's the weather?"), functions);
 *
 * // Embedding
 * float[] embedding = llm.embed("Hello world");
 * </pre>
 *
 * @author ooder Team
 * @since 0.7.1
 */
public interface LlmService {

    // ==================== Chat Operations ====================

    /**
     * Simple chat completion
     *
     * @param request the chat request
     * @return the response text
     */
    String chat(ChatRequest request);

    /**
     * Async chat completion
     *
     * @param request the chat request
     * @return CompletableFuture with response
     */
    CompletableFuture<String> chatAsync(ChatRequest request);

    /**
     * Streaming chat completion
     *
     * @param request   the chat request
     * @param onChunk   callback for each chunk
     * @param onComplete callback on completion
     * @param onError   callback on error
     */
    void chatStream(ChatRequest request,
                    Consumer<String> onChunk,
                    Consumer<Void> onComplete,
                    Consumer<Throwable> onError);

    // ==================== Model Management ====================

    /**
     * Get available models
     *
     * @return list of model IDs
     */
    List<String> getAvailableModels();

    /**
     * Set current model
     *
     * @param modelId the model ID
     */
    void setModel(String modelId);

    /**
     * Get current model
     *
     * @return current model ID
     */
    String getCurrentModel();

    // ==================== Token Management ====================

    /**
     * Count tokens in text
     *
     * @param text the text
     * @return token count
     */
    int countTokens(String text);

    /**
     * Get token usage statistics
     *
     * @return token usage
     */
    TokenUsage getTokenUsage();

    /**
     * Reset token usage statistics
     */
    void resetTokenUsage();

    // ==================== Embedding ====================

    /**
     * Generate embedding for text
     *
     * @param text the text
     * @return embedding vector
     */
    float[] embed(String text);

    /**
     * Generate embeddings for multiple texts
     *
     * @param texts list of texts
     * @return list of embedding vectors
     */
    List<float[]> embedBatch(List<String> texts);

    /**
     * Async embedding generation
     *
     * @param text the text
     * @return CompletableFuture with embedding
     */
    CompletableFuture<float[]> embedAsync(String text);

    // ==================== Function Calling ====================

    /**
     * Chat with function calling support
     *
     * @param request   the chat request
     * @param functions available functions
     * @return the response
     */
    String chatWithFunctions(ChatRequest request, List<FunctionDef> functions);

    /**
     * Async chat with function calling
     *
     * @param request   the chat request
     * @param functions available functions
     * @return CompletableFuture with response
     */
    CompletableFuture<String> chatWithFunctionsAsync(ChatRequest request, List<FunctionDef> functions);

    // ==================== Lifecycle ====================

    /**
     * Shutdown the service
     */
    void shutdown();

    /**
     * Check if service is healthy
     *
     * @return true if healthy
     */
    boolean isHealthy();
}

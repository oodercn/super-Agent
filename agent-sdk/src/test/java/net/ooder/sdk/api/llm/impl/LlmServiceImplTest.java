package net.ooder.sdk.api.llm.impl;

import net.ooder.sdk.api.llm.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicInteger;

public class LlmServiceImplTest {
    
    private LlmServiceImpl llmService;
    
    @Before
    public void setUp() {
        llmService = new LlmServiceImpl();
    }
    
    @After
    public void tearDown() {
        llmService.shutdown();
    }
    
    @Test
    public void testChat() {
        ChatRequest request = ChatRequest.of("Hello, how are you?");
        String response = llmService.chat(request);
        
        assertNotNull(response);
        assertTrue(response.length() > 0);
    }
    
    @Test
    public void testChatWithChinese() {
        ChatRequest request = ChatRequest.of("你好");
        String response = llmService.chat(request);
        
        assertNotNull(response);
        assertTrue(response.contains("Hello") || response.contains("help"));
    }
    
    @Test
    public void testChatWithQuestion() {
        ChatRequest request = ChatRequest.of("What is the weather like?");
        String response = llmService.chat(request);
        
        assertNotNull(response);
        assertTrue(response.length() > 0);
    }
    
    @Test
    public void testChatAsync() throws Exception {
        ChatRequest request = ChatRequest.of("Hello async");
        CompletableFuture<String> future = llmService.chatAsync(request);
        
        String response = future.get(10, TimeUnit.SECONDS);
        assertNotNull(response);
        assertTrue(response.length() > 0);
    }
    
    @Test
    public void testChatStream() throws Exception {
        ChatRequest request = ChatRequest.of("Tell me a story");
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<String> receivedContent = new AtomicReference<String>("");
        AtomicReference<Throwable> errorRef = new AtomicReference<Throwable>(null);
        
        llmService.chatStream(
            request,
            chunk -> receivedContent.set(receivedContent.get() + chunk),
            v -> latch.countDown(),
            e -> {
                errorRef.set(e);
                latch.countDown();
            }
        );
        
        assertTrue(latch.await(10, TimeUnit.SECONDS));
        assertNull(errorRef.get());
        assertTrue(receivedContent.get().length() > 0);
    }
    
    @Test
    public void testGetAvailableModels() {
        List<String> models = llmService.getAvailableModels();
        
        assertNotNull(models);
        assertTrue(models.size() > 0);
        assertTrue(models.contains("gpt-4"));
        assertTrue(models.contains("gpt-3.5-turbo"));
    }
    
    @Test
    public void testSetModel() {
        llmService.setModel("gpt-3.5-turbo");
        assertEquals("gpt-3.5-turbo", llmService.getCurrentModel());
        
        llmService.setModel("claude-3");
        assertEquals("claude-3", llmService.getCurrentModel());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidModel() {
        llmService.setModel("invalid-model-xyz");
    }
    
    @Test
    public void testGetCurrentModel() {
        String model = llmService.getCurrentModel();
        assertNotNull(model);
        assertEquals("gpt-4", model);
    }
    
    @Test
    public void testCountTokens() {
        int tokens = llmService.countTokens("Hello world");
        assertTrue(tokens > 0);
        
        int emptyTokens = llmService.countTokens("");
        assertEquals(0, emptyTokens);
        
        int nullTokens = llmService.countTokens(null);
        assertEquals(0, nullTokens);
    }
    
    @Test
    public void testGetTokenUsage() {
        TokenUsage usage = llmService.getTokenUsage();
        assertNotNull(usage);
    }
    
    @Test
    public void testTokenUsageAfterChat() {
        llmService.resetTokenUsage();
        
        ChatRequest request = ChatRequest.of("Hello world");
        llmService.chat(request);
        
        TokenUsage usage = llmService.getTokenUsage();
        assertTrue(usage.getPromptTokens() > 0);
        assertTrue(usage.getCompletionTokens() > 0);
        assertTrue(usage.getTotalTokens() > 0);
        assertEquals(1, usage.getRequestCount());
    }
    
    @Test
    public void testResetTokenUsage() {
        ChatRequest request = ChatRequest.of("Test");
        llmService.chat(request);
        
        llmService.resetTokenUsage();
        
        TokenUsage usage = llmService.getTokenUsage();
        assertEquals(0, usage.getPromptTokens());
        assertEquals(0, usage.getCompletionTokens());
        assertEquals(0, usage.getTotalTokens());
        assertEquals(0, usage.getRequestCount());
    }
    
    @Test
    public void testEmbed() {
        float[] embedding = llmService.embed("Hello world");
        
        assertNotNull(embedding);
        assertEquals(1536, embedding.length);
        
        float norm = 0;
        for (float v : embedding) {
            norm += v * v;
        }
        norm = (float) Math.sqrt(norm);
        
        assertTrue(Math.abs(norm - 1.0f) < 0.001f);
    }
    
    @Test
    public void testEmbedConsistency() {
        float[] embedding1 = llmService.embed("Test text");
        float[] embedding2 = llmService.embed("Test text");
        
        assertArrayEquals(embedding1, embedding2, 0.0001f);
    }
    
    @Test
    public void testEmbedDifferent() {
        float[] embedding1 = llmService.embed("Hello");
        float[] embedding2 = llmService.embed("World");
        
        boolean different = false;
        for (int i = 0; i < embedding1.length; i++) {
            if (Math.abs(embedding1[i] - embedding2[i]) > 0.0001f) {
                different = true;
                break;
            }
        }
        assertTrue(different);
    }
    
    @Test
    public void testEmbedBatch() {
        List<String> texts = Arrays.asList("Hello", "World", "Test");
        List<float[]> embeddings = llmService.embedBatch(texts);
        
        assertNotNull(embeddings);
        assertEquals(3, embeddings.size());
        
        for (float[] embedding : embeddings) {
            assertEquals(1536, embedding.length);
        }
    }
    
    @Test
    public void testEmbedAsync() throws Exception {
        CompletableFuture<float[]> future = llmService.embedAsync("Hello async");
        
        float[] embedding = future.get(10, TimeUnit.SECONDS);
        assertNotNull(embedding);
        assertEquals(1536, embedding.length);
    }
    
    @Test
    public void testChatWithFunctions() {
        List<FunctionDef> functions = new ArrayList<FunctionDef>();
        functions.add(FunctionDef.of("get_weather", "Get weather information")
            .addParameter("city", "string", "City name", true));
        
        ChatRequest request = ChatRequest.of("What is the weather like?");
        String response = llmService.chatWithFunctions(request, functions);
        
        assertNotNull(response);
        assertTrue(response.contains("function_call") || response.length() > 0);
    }
    
    @Test
    public void testChatWithFunctionsWeatherTrigger() {
        List<FunctionDef> functions = new ArrayList<FunctionDef>();
        functions.add(FunctionDef.of("get_weather", "Get weather information")
            .addParameter("city", "string", "City name", true));
        
        ChatRequest request = ChatRequest.of("What's the weather today?");
        String response = llmService.chatWithFunctions(request, functions);
        
        assertNotNull(response);
        assertTrue(response.contains("function_call"));
        assertTrue(response.contains("get_weather"));
    }
    
    @Test
    public void testChatWithFunctionsChineseTrigger() {
        List<FunctionDef> functions = new ArrayList<FunctionDef>();
        functions.add(FunctionDef.of("get_weather_info", "Get weather information")
            .addParameter("city", "string", "City name", true));
        
        ChatRequest request = ChatRequest.of("今天天气怎么样？");
        String response = llmService.chatWithFunctions(request, functions);
        
        assertNotNull(response);
        assertTrue(response.contains("function_call"));
    }
    
    @Test
    public void testChatWithFunctionsNoMatch() {
        List<FunctionDef> functions = new ArrayList<FunctionDef>();
        functions.add(FunctionDef.of("get_weather", "Get weather information")
            .addParameter("city", "string", "City name", true));
        
        ChatRequest request = ChatRequest.of("Tell me a joke");
        String response = llmService.chatWithFunctions(request, functions);
        
        assertNotNull(response);
    }
    
    @Test
    public void testChatWithFunctionsAsync() throws Exception {
        List<FunctionDef> functions = new ArrayList<FunctionDef>();
        functions.add(FunctionDef.of("get_weather", "Get weather information")
            .addParameter("city", "string", "City name", true));
        
        ChatRequest request = ChatRequest.of("What's the weather?");
        CompletableFuture<String> future = llmService.chatWithFunctionsAsync(request, functions);
        
        String response = future.get(10, TimeUnit.SECONDS);
        assertNotNull(response);
    }
    
    @Test
    public void testIsHealthy() {
        assertTrue(llmService.isHealthy());
    }
    
    @Test
    public void testIsHealthyAfterShutdown() {
        llmService.shutdown();
        assertFalse(llmService.isHealthy());
    }
    
    @Test
    public void testShutdown() {
        LlmServiceImpl service = new LlmServiceImpl();
        service.shutdown();
        assertFalse(service.isHealthy());
    }
    
    @Test
    public void testConstructorWithConfig() {
        LlmConfig config = new LlmConfig();
        config.setDefaultModel("gpt-3.5-turbo");
        config.setMaxTokens(2048);
        config.setTemperature(0.5);
        
        LlmServiceImpl service = new LlmServiceImpl(config);
        assertEquals("gpt-3.5-turbo", service.getCurrentModel());
        
        service.shutdown();
    }
    
    @Test
    public void testConstructorWithNullConfig() {
        LlmServiceImpl service = new LlmServiceImpl(new LlmConfig());
        assertEquals("gpt-4", service.getCurrentModel());
        
        service.shutdown();
    }
    
    @Test
    public void testMultipleChatRequests() {
        llmService.resetTokenUsage();
        
        for (int i = 0; i < 5; i++) {
            ChatRequest request = ChatRequest.of("Test message " + i);
            llmService.chat(request);
        }
        
        TokenUsage usage = llmService.getTokenUsage();
        assertEquals(5, usage.getRequestCount());
    }
    
    @Test
    public void testChatRequestWithSystemPrompt() {
        ChatRequest request = ChatRequest.of("Hello", "You are a helpful assistant.");
        
        assertNotNull(request.getPrompt());
        assertNotNull(request.getSystemPrompt());
        assertEquals("Hello", request.getPrompt());
        assertEquals("You are a helpful assistant.", request.getSystemPrompt());
    }
    
    @Test
    public void testChatRequestWithParameters() {
        ChatRequest request = ChatRequest.of("Test")
            .addParameter("temperature", 0.8)
            .addParameter("max_tokens", 1000);
        
        assertNotNull(request.getParameters());
        assertEquals(0.8, request.getParameters().get("temperature"));
        assertEquals(1000, request.getParameters().get("max_tokens"));
    }
    
    @Test
    public void testFunctionDefBuilder() {
        FunctionDef func = FunctionDef.of("test_function", "Test description")
            .addParameter("param1", "string", "First parameter", true)
            .addParameter("param2", "integer", "Second parameter", false);
        
        assertEquals("test_function", func.getName());
        assertEquals("Test description", func.getDescription());
        assertNotNull(func.getParameters());
        assertEquals(2, func.getParameters().getProperties().size());
        assertEquals(1, func.getParameters().getRequired().size());
        assertTrue(func.getParameters().getRequired().contains("param1"));
    }
    
    @Test
    public void testTokenUsageAddUsage() {
        TokenUsage usage = new TokenUsage();
        
        usage.addUsage(100, 50);
        assertEquals(100, usage.getPromptTokens());
        assertEquals(50, usage.getCompletionTokens());
        assertEquals(150, usage.getTotalTokens());
        assertEquals(1, usage.getRequestCount());
        
        usage.addUsage(200, 100);
        assertEquals(300, usage.getPromptTokens());
        assertEquals(150, usage.getCompletionTokens());
        assertEquals(450, usage.getTotalTokens());
        assertEquals(2, usage.getRequestCount());
    }
    
    @Test
    public void testTokenUsageReset() {
        TokenUsage usage = new TokenUsage();
        usage.addUsage(100, 50);
        
        usage.reset();
        
        assertEquals(0, usage.getPromptTokens());
        assertEquals(0, usage.getCompletionTokens());
        assertEquals(0, usage.getTotalTokens());
        assertEquals(0, usage.getRequestCount());
    }
    
    @Test
    public void testTokenUsageToString() {
        TokenUsage usage = new TokenUsage();
        usage.addUsage(100, 50);
        
        String str = usage.toString();
        assertTrue(str.contains("promptTokens=100"));
        assertTrue(str.contains("completionTokens=50"));
        assertTrue(str.contains("totalTokens=150"));
    }
    
    @Test
    public void testChatRequestMessage() {
        ChatRequest.Message message = new ChatRequest.Message("user", "Hello");
        
        assertEquals("user", message.getRole());
        assertEquals("Hello", message.getContent());
        
        message.setRole("assistant");
        message.setContent("Hi there");
        message.setName("assistant1");
        
        assertEquals("assistant", message.getRole());
        assertEquals("Hi there", message.getContent());
        assertEquals("assistant1", message.getName());
    }
    
    @Test
    public void testLlmConfigDefaults() {
        LlmConfig config = new LlmConfig();
        
        assertEquals("gpt-4", config.getDefaultModel());
        assertEquals(4096, config.getMaxTokens());
        assertEquals(0.7, config.getTemperature(), 0.001);
        assertEquals(60000, config.getTimeout());
    }
    
    @Test
    public void testLlmConfigSetters() {
        LlmConfig config = new LlmConfig();
        
        config.setApiKey("test-api-key");
        config.setBaseUrl("https://api.example.com");
        config.setDefaultModel("claude-3");
        config.setMaxTokens(8192);
        config.setTemperature(0.9);
        config.setTimeout(30000);
        
        assertEquals("test-api-key", config.getApiKey());
        assertEquals("https://api.example.com", config.getBaseUrl());
        assertEquals("claude-3", config.getDefaultModel());
        assertEquals(8192, config.getMaxTokens());
        assertEquals(0.9, config.getTemperature(), 0.001);
        assertEquals(30000, config.getTimeout());
    }
}

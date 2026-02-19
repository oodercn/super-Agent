package net.ooder.sdk.service.llm;

import net.ooder.sdk.api.llm.ChatRequest;
import net.ooder.sdk.api.llm.TokenUsage;
import net.ooder.sdk.api.llm.FunctionDef;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class LlmServiceImplTest {
    
    private LlmServiceImpl service;
    
    @Before
    public void setUp() {
        service = new LlmServiceImpl();
    }
    
    @After
    public void tearDown() {
        service.shutdown();
    }
    
    @Test
    public void testInitialization() {
        assertNotNull(service);
    }
    
    @Test
    public void testInitializationWithConfig() {
        LlmConfig config = new LlmConfig();
        config.setModel("gpt-4");
        
        LlmServiceImpl llmService = new LlmServiceImpl(config);
        
        assertNotNull(llmService);
        llmService.shutdown();
    }
    
    @Test
    public void testChat() {
        ChatRequest request = new ChatRequest();
        request.setPrompt("Hello, World!");
        
        String response = service.chat(request);
        
        assertNotNull(response);
    }
    
    @Test
    public void testChatAsync() throws Exception {
        ChatRequest request = new ChatRequest();
        request.setPrompt("Hello, World!");
        
        String response = service.chatAsync(request).get(30, TimeUnit.SECONDS);
        
        assertNotNull(response);
    }
    
    @Test
    public void testChatStream() throws Exception {
        ChatRequest request = new ChatRequest();
        request.setPrompt("Tell me a story");
        
        AtomicReference<String> result = new AtomicReference<>("");
        
        service.chatStream(request,
            new java.util.function.Consumer<String>() {
                @Override
                public void accept(String chunk) {
                    result.set(result.get() + chunk);
                }
            },
            new java.util.function.Consumer<Void>() {
                @Override
                public void accept(Void v) {}
            },
            new java.util.function.Consumer<Throwable>() {
                @Override
                public void accept(Throwable e) {}
            }
        );
        
        Thread.sleep(100);
    }
    
    @Test
    public void testGetAvailableModels() {
        List<String> models = service.getAvailableModels();
        
        assertNotNull(models);
        assertFalse(models.isEmpty());
    }
    
    @Test
    public void testGetCurrentModel() {
        String model = service.getCurrentModel();
        
        assertNotNull(model);
    }
    
    @Test
    public void testSetModel() {
        service.setModel("gpt-4");
        
        assertEquals("gpt-4", service.getCurrentModel());
    }
    
    @Test
    public void testGetTokenUsage() {
        TokenUsage usage = service.getTokenUsage();
        
        assertNotNull(usage);
    }
    
    @Test
    public void testCountTokens() {
        int count = service.countTokens("Hello, World!");
        
        assertTrue(count > 0);
    }
    
    @Test
    public void testEmbed() {
        float[] embedding = service.embed("Hello world");
        
        assertNotNull(embedding);
    }
    
    @Test
    public void testEmbedAsync() throws Exception {
        float[] embedding = service.embedAsync("Hello world").get(10, TimeUnit.SECONDS);
        
        assertNotNull(embedding);
    }
    
    @Test
    public void testChatWithFunctions() {
        ChatRequest request = new ChatRequest();
        request.setPrompt("What's the weather?");
        
        List<FunctionDef> functions = new ArrayList<FunctionDef>();
        FunctionDef func = new FunctionDef();
        func.setName("get_weather");
        func.setDescription("Get weather info");
        functions.add(func);
        
        String response = service.chatWithFunctions(request, functions);
        
        assertNotNull(response);
    }
    
    @Test
    public void testIsHealthy() {
        assertTrue(service.isHealthy());
    }
    
    @Test
    public void testShutdown() {
        service.shutdown();
    }
}

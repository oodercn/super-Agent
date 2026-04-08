package net.ooder.skill.llm.chat.controller;

import net.ooder.skill.llm.chat.model.ChatRequest;
import net.ooder.skill.llm.chat.model.SetModelRequest;
import net.ooder.skill.llm.chat.service.LLMServiceProvider;
import net.ooder.skill.llm.chat.service.LLMService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import net.ooder.skill.hotplug.registry.ServiceRegistry;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController("llmChatControllerV1")
@RequestMapping("/api/v1/llm/chat")
@CrossOrigin(originPatterns = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}, allowCredentials = "false")
public class LlmChatController {
    
    private static final Logger log = LoggerFactory.getLogger(LlmChatController.class);
    private static final long SSE_TIMEOUT = 120000L;
    
    private final ExecutorService executor = Executors.newCachedThreadPool();
    
    private final Map<String, SessionInfo> sessions = new ConcurrentHashMap<String, SessionInfo>();
    private final Map<String, ProviderInfo> providers = new ConcurrentHashMap<String, ProviderInfo>();
    
    private String currentProvider = System.getProperty("ooder.llm.provider", "qianwen");
    private String currentModel = System.getProperty("ooder.llm.model", "qwen-plus");
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @Autowired(required = false)
    private LLMServiceProvider llmServiceProvider;
    
    @Autowired(required = false)
    private ServiceRegistry serviceRegistry;
    
    private LLMServiceProvider getLlmService() {
        if (llmServiceProvider != null) {
            return llmServiceProvider;
        }
        
        if (serviceRegistry != null) {
            try {
                Object service = serviceRegistry.getService("skill-llm-chat", "llmServiceProvider", LLMServiceProvider.class);
                if (service instanceof LLMServiceProvider) {
                    llmServiceProvider = (LLMServiceProvider) service;
                    log.info("Got LLMServiceProvider from ServiceRegistry");
                    return llmServiceProvider;
                }
            } catch (Exception e) {
                log.debug("Failed to get LLMServiceProvider from ServiceRegistry: {}", e.getMessage());
            }
        }
        
        try {
            LLMService service = applicationContext.getBean(LLMService.class);
            if (service != null) {
                llmServiceProvider = service;
                return service;
            }
        } catch (Exception e) {
            log.debug("LLMService not found in ApplicationContext: {}", e.getMessage());
        }
        return null;
    }
    
    public LlmChatController() {
        initProviders();
    }
    
    private void initProviders() {
        providers.put("deepseek", new ProviderInfo("deepseek", "DeepSeek", 
            Arrays.asList("deepseek-chat", "deepseek-coder", "deepseek-reasoner"), true, true));
        providers.put("baidu", new ProviderInfo("baidu", "百度千帆", 
            Arrays.asList("ernie-4.0-8k", "ernie-4.0-turbo-8k", "ernie-3.5-8k", "ernie-3.5-turbo-8k", 
                "ernie-speed-8k", "ernie-lite-8k", "ernie-tiny-8k"), true, true));
        providers.put("openai", new ProviderInfo("openai", "OpenAI", 
            Arrays.asList("gpt-4o", "gpt-4-turbo", "gpt-3.5-turbo"), true, true));
        providers.put("qianwen", new ProviderInfo("qianwen", "通义千问", 
            Arrays.asList("qwen-max", "qwen-plus", "qwen-turbo"), true, true));
        providers.put("ollama", new ProviderInfo("ollama", "Ollama", 
            Arrays.asList("llama3", "llama2", "mistral", "codellama", "qwen2"), true, true));
    }
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> chat(@RequestBody ChatRequest request) {
        String message = request.getMessage();
        String sessionId = request.getSessionId();
        String provider = request.getProvider() != null ? request.getProvider() : currentProvider;
        String model = request.getModel() != null ? request.getModel() : currentModel;
        Double temperature = request.getTemperature() != null ? request.getTemperature() : 0.7;
        Integer maxTokens = request.getMaxTokens() != null ? request.getMaxTokens() : 4096;
        
        log.info("[chat] message: {}, sessionId: {}, provider: {}, model: {}", 
            message, sessionId, provider, model);
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            SessionInfo session = getOrCreateSession(sessionId);
            session.addMessage("user", message);
            
            String response;
            int tokensUsed = 0;
            
            LLMServiceProvider llmService = getLlmService();
            if (llmService != null) {
                Map<String, Object> llmStatus = llmService.checkDependencies();
                boolean llmReady = (Boolean) llmStatus.get("ready");
                
                if (llmReady) {
                    response = llmService.generateAnswer(message, null, model);
                    tokensUsed = response.length();
                } else {
                    response = "LLM service not configured, please check API Key settings.";
                    tokensUsed = response.length();
                }
            } else {
                response = "LLM service unavailable, please check service configuration.";
                tokensUsed = response.length();
            }
            
            session.addMessage("assistant", response);
            session.addTokens(tokensUsed);
            
            final int finalTokensUsed = tokensUsed;
            final String finalMessage = message;
            Map<String, Object> usage = new HashMap<>();
            usage.put("promptTokens", finalMessage.length());
            usage.put("completionTokens", finalTokensUsed);
            usage.put("totalTokens", finalMessage.length() + finalTokensUsed);
            
            Map<String, Object> data = new HashMap<>();
            data.put("response", response);
            data.put("sessionId", session.id);
            data.put("model", model);
            data.put("provider", provider);
            data.put("usage", usage);
            
            result.put("status", "success");
            result.put("data", data);
            
        } catch (Exception e) {
            log.error("[chat] Error", e);
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        
        return ResponseEntity.ok(result);
    }
    
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter chatStream(
            @RequestParam(required = false) String message,
            @RequestParam(required = false) String sessionId,
            @RequestParam(required = false) String provider,
            @RequestParam(required = false) String model,
            @RequestParam(required = false, defaultValue = "0.7") Double temperature,
            @RequestParam(required = false, defaultValue = "4096") Integer maxTokens) {
        
        log.info("[chatStream GET] message: {}, sessionId: {}", message, sessionId);
        return doChatStream(message, sessionId, provider, model, temperature, maxTokens);
    }
    
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter chatStreamPost(@RequestBody ChatRequest request) {
        String message = request.getMessage();
        String sessionId = request.getSessionId();
        String provider = request.getProvider() != null ? request.getProvider() : currentProvider;
        String model = request.getModel() != null ? request.getModel() : currentModel;
        Double temperature = request.getTemperature() != null ? request.getTemperature() : 0.7;
        Integer maxTokens = request.getMaxTokens() != null ? request.getMaxTokens() : 4096;
        
        log.info("[chatStream POST] message: {}, sessionId: {}, provider: {}, model: {}", 
            message, sessionId, provider, model);
        return doChatStream(message, sessionId, provider, model, temperature, maxTokens);
    }
    
    private SseEmitter doChatStream(String message, String sessionId, String provider, String model, 
            Double temperature, Integer maxTokens) {
        
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT);
        
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    SessionInfo session = getOrCreateSession(sessionId);
                    session.addMessage("user", message);
                    
                    String fullResponse;
                    LLMServiceProvider llmService = getLlmService();
                    if (llmService != null) {
                        Map<String, Object> llmStatus = llmService.checkDependencies();
                        boolean llmReady = (Boolean) llmStatus.get("ready");
                        
                        if (llmReady) {
                            fullResponse = llmService.generateAnswer(message, null, model);
                        } else {
                            fullResponse = "LLM service not configured, please check API Key settings.";
                        }
                    } else {
                        fullResponse = "LLM service unavailable, please check service configuration.";
                    }
                    
                    int chunkSize = 8;
                    for (int i = 0; i < fullResponse.length(); i += chunkSize) {
                        int end = Math.min(i + chunkSize, fullResponse.length());
                        String chunk = fullResponse.substring(i, end);
                        
                        emitter.send(SseEmitter.event()
                            .name("message")
                            .data(chunk));
                        
                        Thread.sleep(30);
                    }
                    
                    session.addMessage("assistant", fullResponse);
                    session.addTokens(fullResponse.length());
                    
                    emitter.send(SseEmitter.event()
                        .name("done")
                        .data("[DONE]"));
                    emitter.complete();
                    
                } catch (IOException e) {
                    log.error("[chatStream] Error sending SSE chunk", e);
                    emitter.completeWithError(e);
                } catch (InterruptedException e) {
                    log.error("[chatStream] SSE interrupted", e);
                    emitter.completeWithError(e);
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    log.error("[chatStream] Stream execution error", e);
                    emitter.completeWithError(e);
                }
            }
        });
        
        emitter.onCompletion(new Runnable() {
            @Override
            public void run() {
                log.info("[chatStream] SSE completed");
            }
        });
        
        emitter.onTimeout(new Runnable() {
            @Override
            public void run() {
                log.warn("[chatStream] SSE timeout");
                emitter.complete();
            }
        });
        
        return emitter;
    }
    
    @GetMapping("/providers")
    public ResponseEntity<Map<String, Object>> getProviders() {
        log.info("[getProviders] Getting available providers");
        
        Map<String, Object> result = new HashMap<>();
        
        List<Map<String, Object>> providerList = new ArrayList<>();
        for (ProviderInfo info : providers.values()) {
            Map<String, Object> p = new HashMap<>();
            p.put("id", info.id);
            p.put("name", info.name);
            p.put("models", info.models);
            p.put("supportsStreaming", info.supportsStreaming);
            p.put("supportsFunctionCalling", info.supportsFunctionCalling);
            providerList.add(p);
        }
        
        result.put("status", "success");
        result.put("data", providerList);
        
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/models")
    public ResponseEntity<Map<String, Object>> getModels(@RequestParam(required = false) String provider) {
        log.info("[getModels] provider: {}", provider);
        
        Map<String, Object> result = new HashMap<>();
        
        String providerId = provider != null ? provider : currentProvider;
        ProviderInfo providerInfo = providers.get(providerId);
        
        if (providerInfo != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("provider", providerId);
            data.put("models", providerInfo.models);
            data.put("currentModel", currentModel);
            
            result.put("status", "success");
            result.put("data", data);
        } else {
            result.put("status", "error");
            result.put("message", "Provider not found: " + providerId);
        }
        
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/models/set")
    public ResponseEntity<Map<String, Object>> setModel(@RequestBody SetModelRequest request) {
        String modelId = request.getModelId();
        String providerId = request.getProvider();
        
        log.info("[setModel] modelId: {}, provider: {}", modelId, providerId);
        
        Map<String, Object> result = new HashMap<>();
        
        if (providerId != null && providers.containsKey(providerId)) {
            currentProvider = providerId;
        }
        
        if (modelId != null) {
            currentModel = modelId;
        }
        
        result.put("status", "success");
        result.put("data", new HashMap<String, Object>() {{
            put("provider", currentProvider);
            put("model", currentModel);
        }});
        
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/sessions")
    public ResponseEntity<Map<String, Object>> getSessions() {
        log.info("[getSessions] Getting all sessions");
        
        Map<String, Object> result = new HashMap<>();
        
        List<Map<String, Object>> sessionList = new ArrayList<>();
        for (SessionInfo session : sessions.values()) {
            Map<String, Object> s = new HashMap<>();
            s.put("id", session.id);
            s.put("title", session.title);
            s.put("messageCount", session.messages.size());
            s.put("totalTokens", session.totalTokens);
            s.put("createTime", session.createTime);
            s.put("updateTime", session.updateTime);
            sessionList.add(s);
        }
        
        sessionList.sort((a, b) -> Long.compare((Long) b.get("updateTime"), (Long) a.get("updateTime")));
        
        result.put("status", "success");
        result.put("data", sessionList);
        
        return ResponseEntity.ok(result);
    }
    
    @DeleteMapping("/sessions/{sessionId}")
    public ResponseEntity<Map<String, Object>> deleteSession(@PathVariable String sessionId) {
        log.info("[deleteSession] sessionId: {}", sessionId);
        
        Map<String, Object> result = new HashMap<>();
        
        SessionInfo removed = sessions.remove(sessionId);
        
        result.put("status", "success");
        result.put("message", removed != null ? "Session deleted" : "Session not found");
        result.put("sessionId", sessionId);
        
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/sessions/{sessionId}/history")
    public ResponseEntity<Map<String, Object>> getSessionHistory(@PathVariable String sessionId) {
        log.info("[getSessionHistory] sessionId: {}", sessionId);
        
        Map<String, Object> result = new HashMap<>();
        
        SessionInfo session = sessions.get(sessionId);
        
        if (session != null) {
            result.put("status", "success");
            result.put("data", session.messages);
        } else {
            result.put("status", "error");
            result.put("message", "Session not found");
        }
        
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        log.info("[health] LLM health check");
        
        Map<String, Object> result = new HashMap<>();
        
        Map<String, Object> data = new HashMap<>();
        data.put("healthy", true);
        data.put("currentProvider", currentProvider);
        data.put("currentModel", currentModel);
        data.put("providerCount", providers.size());
        data.put("sessionCount", sessions.size());
        data.put("llmServiceReady", getLlmService() != null);
        
        result.put("status", "success");
        result.put("data", data);
        
        return ResponseEntity.ok(result);
    }
    
    private SessionInfo getOrCreateSession(String sessionId) {
        if (sessionId == null || sessionId.isEmpty()) {
            sessionId = "session_" + System.currentTimeMillis();
        }
        
        SessionInfo session = sessions.get(sessionId);
        if (session == null) {
            session = new SessionInfo(sessionId);
            sessions.put(sessionId, session);
        }
        
        return session;
    }
    
    private static class SessionInfo {
        String id;
        String title;
        List<Map<String, Object>> messages;
        long createTime;
        long updateTime;
        int totalTokens;
        
        SessionInfo(String id) {
            this.id = id;
            this.title = "New Chat";
            this.messages = new ArrayList<>();
            this.createTime = System.currentTimeMillis();
            this.updateTime = this.createTime;
            this.totalTokens = 0;
        }
        
        void addMessage(String role, String content) {
            Map<String, Object> msg = new HashMap<>();
            msg.put("role", role);
            msg.put("content", content);
            msg.put("timestamp", System.currentTimeMillis());
            messages.add(msg);
            updateTime = System.currentTimeMillis();
            
            if (messages.size() == 1 && "user".equals(role)) {
                title = content.length() > 20 ? content.substring(0, 20) + "..." : content;
            }
        }
        
        void addTokens(int tokens) {
            totalTokens += tokens;
        }
        
        List<Map<String, Object>> getHistory() {
            return new ArrayList<>(messages);
        }
    }
    
    private static class ProviderInfo {
        String id;
        String name;
        List<String> models;
        boolean supportsStreaming;
        boolean supportsFunctionCalling;
        Map<String, ModelInfo> modelDetails;
        
        ProviderInfo(String id, String name, List<String> models, boolean supportsStreaming, boolean supportsFunctionCalling) {
            this.id = id;
            this.name = name;
            this.models = models;
            this.supportsStreaming = supportsStreaming;
            this.supportsFunctionCalling = supportsFunctionCalling;
            this.modelDetails = new HashMap<>();
            initModelDetails();
        }
        
        private void initModelDetails() {
            for (String modelId : models) {
                ModelInfo info = new ModelInfo(modelId, id);
                modelDetails.put(modelId, info);
            }
        }
    }
    
    private static class ModelInfo {
        String id;
        String providerId;
        String displayName;
        String description;
        int contextWindow;
        int maxOutputTokens;
        boolean supportsVision;
        boolean supportsFunctionCall;
        boolean supportsStreaming;
        boolean supportsRAG;
        double inputPrice;
        double outputPrice;
        String[] capabilities;
        
        ModelInfo(String id, String providerId) {
            this.id = id;
            this.providerId = providerId;
            this.displayName = getDisplayName(id, providerId);
            this.description = getDescription(id, providerId);
            this.contextWindow = getContextWindow(id, providerId);
            this.maxOutputTokens = getMaxOutputTokens(id, providerId);
            this.supportsVision = supportsVision(id, providerId);
            this.supportsFunctionCall = supportsFunctionCall(id, providerId);
            this.supportsStreaming = true;
            this.supportsRAG = supportsRAG(id, providerId);
            this.inputPrice = getInputPrice(id, providerId);
            this.outputPrice = getOutputPrice(id, providerId);
            this.capabilities = getCapabilities(id, providerId);
        }
        
        private static String getDisplayName(String id, String providerId) {
            Map<String, String> names = new HashMap<>();
            names.put("deepseek-chat", "DeepSeek Chat");
            names.put("deepseek-coder", "DeepSeek Coder");
            names.put("deepseek-reasoner", "DeepSeek Reasoner");
            names.put("gpt-4o", "GPT-4o");
            names.put("gpt-4-turbo", "GPT-4 Turbo");
            names.put("gpt-3.5-turbo", "GPT-3.5 Turbo");
            names.put("qwen-max", "通义千问-Max");
            names.put("qwen-plus", "通义千问-Plus");
            names.put("qwen-turbo", "通义千问-Turbo");
            names.put("ernie-4.0-8k", "ERNIE 4.0 (8K)");
            names.put("ernie-4.0-turbo-8k", "ERNIE 4.0 Turbo (8K)");
            names.put("ernie-3.5-8k", "ERNIE 3.5 (8K)");
            names.put("ernie-3.5-turbo-8k", "ERNIE 3.5 Turbo (8K)");
            names.put("ernie-speed-8k", "ERNIE Speed (8K)");
            names.put("ernie-lite-8k", "ERNIE Lite (8K)");
            names.put("ernie-tiny-8k", "ERNIE Tiny (8K)");
            names.put("llama3", "Llama 3");
            names.put("llama2", "Llama 2");
            names.put("mistral", "Mistral");
            names.put("codellama", "Code Llama");
            names.put("qwen2", "Qwen 2");
            return names.getOrDefault(id, id);
        }
        
        private static String getDescription(String id, String providerId) {
            if (id.contains("coder")) return "专为代码生成和理解优化的模型";
            if (id.contains("reasoner")) return "深度推理模型，适合复杂逻辑分析";
            if (id.contains("gpt-4o")) return "OpenAI最新多模态模型，支持视觉和文本";
            if (id.contains("gpt-4")) return "OpenAI最强大的语言模型";
            if (id.contains("gpt-3.5")) return "OpenAI快速高效的语言模型";
            if (id.contains("qwen-max")) return "通义千问最强模型，适合复杂任务";
            if (id.contains("qwen-plus")) return "通义千问平衡性能与成本的模型";
            if (id.contains("qwen-turbo")) return "通义千问快速响应模型";
            if (id.contains("ernie-4")) return "百度最新大语言模型，能力最强";
            if (id.contains("ernie-3.5")) return "百度高性能语言模型";
            if (id.contains("speed")) return "百度高速模型，响应快速";
            if (id.contains("lite")) return "百度轻量级模型，成本优化";
            if (id.contains("tiny")) return "百度超轻量模型，极致性价比";
            if (id.contains("llama3")) return "Meta开源大语言模型最新版本";
            if (id.contains("mistral")) return "Mistral AI高性能开源模型";
            if (id.contains("deepseek")) return "DeepSeek智能对话模型";
            return "通用大语言模型";
        }
        
        private static int getContextWindow(String id, String providerId) {
            if (id.contains("gpt-4o")) return 128000;
            if (id.contains("gpt-4-turbo")) return 128000;
            if (id.contains("gpt-3.5")) return 16385;
            if (id.contains("qwen-max")) return 32000;
            if (id.contains("qwen")) return 8192;
            if (id.contains("8k")) return 8192;
            if (id.contains("llama3")) return 8192;
            if (id.contains("deepseek")) return 64000;
            return 4096;
        }
        
        private static int getMaxOutputTokens(String id, String providerId) {
            if (id.contains("gpt-4")) return 4096;
            if (id.contains("gpt-3.5")) return 4096;
            if (id.contains("qwen-max")) return 8192;
            if (id.contains("deepseek-reasoner")) return 8192;
            return 4096;
        }
        
        private static boolean supportsVision(String id, String providerId) {
            return id.contains("gpt-4o") || id.contains("qwen-vl") || id.contains("ernie-4");
        }
        
        private static boolean supportsFunctionCall(String id, String providerId) {
            return !id.contains("tiny") && !id.contains("lite");
        }
        
        private static boolean supportsRAG(String id, String providerId) {
            return true;
        }
        
        private static double getInputPrice(String id, String providerId) {
            if (id.contains("gpt-4o")) return 0.005;
            if (id.contains("gpt-4-turbo")) return 0.01;
            if (id.contains("gpt-3.5")) return 0.0005;
            if (id.contains("qwen-max")) return 0.02;
            if (id.contains("qwen-plus")) return 0.004;
            if (id.contains("qwen-turbo")) return 0.002;
            if (id.contains("ernie-4")) return 0.03;
            if (id.contains("ernie-3.5")) return 0.012;
            if (id.contains("deepseek")) return 0.001;
            return 0.0;
        }
        
        private static double getOutputPrice(String id, String providerId) {
            if (id.contains("gpt-4o")) return 0.015;
            if (id.contains("gpt-4-turbo")) return 0.03;
            if (id.contains("gpt-3.5")) return 0.0015;
            if (id.contains("qwen-max")) return 0.06;
            if (id.contains("qwen-plus")) return 0.012;
            if (id.contains("qwen-turbo")) return 0.006;
            if (id.contains("ernie-4")) return 0.06;
            if (id.contains("ernie-3.5")) return 0.012;
            if (id.contains("deepseek")) return 0.002;
            return 0.0;
        }
        
        private static String[] getCapabilities(String id, String providerId) {
            List<String> caps = new ArrayList<>();
            caps.add("文本生成");
            caps.add("对话问答");
            if (supportsFunctionCall(id, providerId)) caps.add("函数调用");
            if (supportsVision(id, providerId)) caps.add("视觉理解");
            if (id.contains("coder") || id.contains("code")) caps.add("代码生成");
            if (id.contains("reasoner")) caps.add("深度推理");
            caps.add("RAG检索增强");
            return caps.toArray(new String[0]);
        }
    }
    
    @GetMapping("/model/{providerId}/{modelId}")
    public ResponseEntity<Map<String, Object>> getModelDetail(
            @PathVariable String providerId, 
            @PathVariable String modelId) {
        log.info("[getModelDetail] provider: {}, model: {}", providerId, modelId);
        
        Map<String, Object> result = new HashMap<>();
        
        ProviderInfo provider = providers.get(providerId);
        if (provider == null) {
            result.put("status", "error");
            result.put("message", "Provider not found: " + providerId);
            return ResponseEntity.ok(result);
        }
        
        ModelInfo model = provider.modelDetails.get(modelId);
        if (model == null) {
            result.put("status", "error");
            result.put("message", "Model not found: " + modelId);
            return ResponseEntity.ok(result);
        }
        
        Map<String, Object> data = new HashMap<>();
        data.put("id", model.id);
        data.put("providerId", model.providerId);
        data.put("displayName", model.displayName);
        data.put("description", model.description);
        data.put("contextWindow", model.contextWindow);
        data.put("maxOutputTokens", model.maxOutputTokens);
        data.put("supportsVision", model.supportsVision);
        data.put("supportsFunctionCall", model.supportsFunctionCall);
        data.put("supportsStreaming", model.supportsStreaming);
        data.put("supportsRAG", model.supportsRAG);
        data.put("inputPrice", model.inputPrice);
        data.put("outputPrice", model.outputPrice);
        data.put("capabilities", model.capabilities);
        
        result.put("status", "success");
        result.put("data", data);
        
        return ResponseEntity.ok(result);
    }
}

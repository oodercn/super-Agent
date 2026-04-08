package net.ooder.mvp.skill.scene.controller;

import jakarta.validation.Valid;
import net.ooder.mvp.skill.scene.model.ResultModel;
import net.ooder.scene.skill.LlmProvider;
import net.ooder.scene.skill.tool.ToolOrchestrator;
import net.ooder.scene.skill.tool.ToolRegistry;
import net.ooder.scene.llm.context.*;
import net.ooder.mvp.skill.scene.dto.llm.*;
import net.ooder.mvp.skill.scene.dto.llm.LlmProviderType;
import net.ooder.mvp.skill.scene.llm.BaiduLlmProvider;
import net.ooder.mvp.skill.scene.llm.DeepSeekLlmProvider;
import net.ooder.mvp.skill.scene.llm.AliyunBailianLlmProvider;
import net.ooder.mvp.skill.scene.llm.SkillActivationService;
import net.ooder.mvp.skill.scene.llm.prompt.SkillPromptService;
import net.ooder.mvp.skill.scene.service.LlmCallLogService;
import net.ooder.scene.llm.audit.LlmAuditService;
import net.ooder.scene.llm.audit.LlmCallContext;
import net.ooder.scene.llm.audit.LlmCallResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/v1/llm")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS})
public class LlmController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(LlmController.class);
    private static final long SSE_TIMEOUT = 120000L;
    
    private static final String DEFAULT_ROLE_ID = "discovery-assistant";
    
    @Value("${ooder.mock.enabled:false}")
    private boolean mockEnabled;
    
    @Value("${ooder.llm.provider:mock}")
    private String configProvider;
    
    @Value("${ooder.llm.model:default}")
    private String configModel;
    
    @Value("${ooder.llm.baidu.api-key:}")
    private String baiduApiKey;
    
    @Value("${ooder.llm.baidu.secret-key:}")
    private String baiduSecretKey;
    
    @Value("${ooder.llm.deepseek.api-key:}")
    private String deepseekApiKey;
    
    @Value("${ooder.llm.qianwen.api-key:}")
    private String qianwenApiKey;
    
    private final ExecutorService executor = Executors.newCachedThreadPool();
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private SkillActivationService skillActivationService;
    
    @Autowired
    public void setSkillActivationService(SkillActivationService skillActivationService) {
        this.skillActivationService = skillActivationService;
    }
    
    private SkillPromptService skillPromptService;
    
    @Autowired
    public void setSkillPromptService(SkillPromptService skillPromptService) {
        this.skillPromptService = skillPromptService;
    }
    
    private LlmCallLogService llmCallLogService;
    
    @Autowired
    public void setLlmCallLogService(LlmCallLogService llmCallLogService) {
        this.llmCallLogService = llmCallLogService;
    }
    
    @Autowired(required = false)
    private LlmAuditService llmAuditService;
    
    @Autowired(required = false)
    private net.ooder.mvp.skill.scene.llm.context.MultiLevelContextManager contextManager;
    
    private ToolRegistry toolRegistry;
     
    @Autowired
    public void setToolRegistry(ToolRegistry toolRegistry) {
        this.toolRegistry = toolRegistry;
    }
    
    private ToolOrchestrator toolOrchestrator;
     
    @Autowired(required = false)
    public void setToolOrchestrator(ToolOrchestrator toolOrchestrator) {
         this.toolOrchestrator = toolOrchestrator;
     }
    
    private final Map<String, LlmProvider> providers = new ConcurrentHashMap<String, LlmProvider>();
    private String currentProviderType = "mock";
    private String currentModel = "default";

    public LlmController() {
        loadProviders();
    }
    
    @jakarta.annotation.PostConstruct
    public void init() {
        initBaiduProvider();
        initDeepSeekProvider();
        initQianwenProvider();
        
        if (configProvider != null && !configProvider.isEmpty() && !configProvider.equals("mock")) {
            currentProviderType = configProvider;
        }
        if (configModel != null && !configModel.isEmpty() && !configModel.equals("default")) {
            currentModel = configModel;
        }
        log.info("LLM initialized with provider: {}, model: {}", currentProviderType, currentModel);
        if ("baidu".equals(currentProviderType)) {
            log.info("Baidu LLM API Key configured: {}", baiduApiKey != null && !baiduApiKey.isEmpty() ? "yes" : "no");
        }
        if ("deepseek".equals(currentProviderType)) {
            log.info("DeepSeek LLM API Key configured: {}", deepseekApiKey != null && !deepseekApiKey.isEmpty() ? "yes" : "no");
        }
        if ("qianwen".equals(currentProviderType) || "aliyun-bailian".equals(currentProviderType)) {
            log.info("Qianwen LLM API Key configured: {}", qianwenApiKey != null && !qianwenApiKey.isEmpty() ? "yes" : "no");
        }
    }
    
    private void initBaiduProvider() {
        if (baiduApiKey != null && !baiduApiKey.isEmpty() && baiduSecretKey != null && !baiduSecretKey.isEmpty()) {
            BaiduLlmProvider baiduProvider = new BaiduLlmProvider();
            baiduProvider.setAccessKey(baiduApiKey);
            baiduProvider.setSecretKey(baiduSecretKey);
            providers.put("baidu", baiduProvider);
            log.info("Baidu LLM Provider registered with models: {}", baiduProvider.getSupportedModels());
        }
    }
    
    private void initDeepSeekProvider() {
        if (deepseekApiKey != null && !deepseekApiKey.isEmpty()) {
            DeepSeekLlmProvider deepseekProvider = new DeepSeekLlmProvider();
            deepseekProvider.setApiKey(deepseekApiKey);
            deepseekProvider.setToolRegistry(toolRegistry);
            if (toolOrchestrator != null) {
                deepseekProvider.setToolOrchestrator(toolOrchestrator);
            }
            providers.put("deepseek", deepseekProvider);
            log.info("DeepSeek LLM Provider registered with models: {}", deepseekProvider.getSupportedModels());
        }
    }
    
    private void initQianwenProvider() {
        if (qianwenApiKey != null && !qianwenApiKey.isEmpty()) {
            AliyunBailianLlmProvider qianwenProvider = new AliyunBailianLlmProvider();
            qianwenProvider.setApiKey(qianwenApiKey);
            qianwenProvider.setToolRegistry(toolRegistry);
            if (toolOrchestrator != null) {
                qianwenProvider.setToolOrchestrator(toolOrchestrator);
            }
            providers.put("qianwen", qianwenProvider);
            providers.put("aliyun-bailian", qianwenProvider);
            log.info("Qianwen LLM Provider registered with models: {}", qianwenProvider.getSupportedModels());
        }
    }

    private void loadProviders() {
        try {
            ServiceLoader<LlmProvider> loader = ServiceLoader.load(LlmProvider.class);
            for (LlmProvider provider : loader) {
                String type = provider.getProviderType();
                providers.put(type, provider);
                log.info("Loaded LLM Provider: {} with models: {}", type, provider.getSupportedModels());
            }
            
            if (!providers.isEmpty()) {
                currentProviderType = providers.keySet().iterator().next();
                LlmProvider firstProvider = providers.get(currentProviderType);
                List<String> models = firstProvider.getSupportedModels();
                if (!models.isEmpty()) {
                    currentModel = models.get(0);
                }
            }
            
            log.info("Total LLM Providers loaded: {}", providers.size());
        } catch (Exception e) {
            log.warn("Failed to load LLM providers via SPI: {}", e.getMessage());
        }
    }

    @PostMapping("/chat")
    @ResponseBody
    public ResultModel<ChatResponseDTO> chat(@RequestBody @Valid ChatRequestDTO request) {
        String providerType = request.getProvider() != null ? request.getProvider() : currentProviderType;
        String model = request.getModel() != null ? request.getModel() : currentModel;
        log.info("Chat API called with provider: {}, model: {}", providerType, model);
        ResultModel<ChatResponseDTO> result = new ResultModel<ChatResponseDTO>();
        
        long startTime = System.currentTimeMillis();
        LlmCallLogDTO logEntry = new LlmCallLogDTO();
        logEntry.setProviderId(providerType);
        logEntry.setProviderName(getProviderDisplayName(providerType));
        logEntry.setModel(model);
        logEntry.setRequestType("chat");

        try {
            String prompt = request.getMessage();
            logEntry.setPrompt(prompt);
            
            if (prompt == null || prompt.trim().isEmpty()) {
                result.setCode(400);
                result.setMessage("Message cannot be empty");
                logEntry.setStatus("error");
                logEntry.setErrorMessage("Message cannot be empty");
                recordCallLog(logEntry, startTime);
                return result;
            }
            
            LlmProvider provider = providers.get(providerType);
            
            String response;
            if (provider != null) {
                SkillActivationContext activationContext = null;
                String activationId = null;
                
                if (skillActivationService != null) {
                    ActivationRequest activationRequest = ActivationRequest.builder()
                        .skillId("skill-scene")
                        .userId("current-user")
                        .roleId(DEFAULT_ROLE_ID)
                        .build();
                    
                    activationContext = skillActivationService.activateSkill(activationRequest);
                    activationId = activationContext.getActivationId();
                    log.info("[LlmController] Skill activated: {}", activationId);
                }
                
                List<Map<String, Object>> messages = new ArrayList<Map<String, Object>>();
                
                Map<String, Object> systemMessage = new HashMap<String, Object>();
                systemMessage.put("role", "system");
                
                String systemPrompt;
                if (activationContext != null) {
                    systemPrompt = getSystemPrompt() + "\n\n" + activationContext.buildSystemPrompt();
                } else {
                    systemPrompt = getSystemPrompt();
                }
                log.info("[LlmController] System prompt length: {}, first 100 chars: {}", 
                    systemPrompt.length(), systemPrompt.substring(0, Math.min(100, systemPrompt.length())));
                systemMessage.put("content", systemPrompt);
                messages.add(systemMessage);
                
                Map<String, Object> userMessage = new HashMap<String, Object>();
                userMessage.put("role", "user");
                userMessage.put("content", prompt);
                messages.add(userMessage);
                
                Map<String, Object> options = new HashMap<String, Object>();
                if (request.getTemperature() != null) {
                    options.put("temperature", request.getTemperature());
                }
                if (request.getMaxTokens() != null) {
                    options.put("max_tokens", request.getMaxTokens());
                }
                
                if (provider.supportsFunctionCalling()) {
                    List<Map<String, Object>> tools;
                    if (activationContext != null && activationContext.getFunctionContext() != null) {
                        tools = activationContext.getFunctionContext().toTools();
                        log.debug("Added {} tools from SkillActivationContext for function calling", tools.size());
                    } else if (toolRegistry != null) {
                        tools = toolRegistry.getToolDefinitions();
                        log.debug("Added {} tools from ToolRegistry for function calling", tools.size());
                    } else {
                        tools = Collections.emptyList();
                    }
                    
                    if (!tools.isEmpty()) {
                        options.put("tools", tools);
                        options.put("tool_choice", "auto");
                    }
                }
                
                if (activationId != null) {
                    options.put("activationId", activationId);
                }
                
                Map<String, Object> chatResult = provider.chat(model, messages, options);
                response = (String) chatResult.get("content");
                
                Map<String, Object> actionResult = (Map<String, Object>) chatResult.get("actionResult");
                
                Integer inputTokens = (Integer) chatResult.get("inputTokens");
                Integer outputTokens = (Integer) chatResult.get("outputTokens");
                if (inputTokens != null) {
                    logEntry.setInputTokens(inputTokens);
                }
                if (outputTokens != null) {
                    logEntry.setOutputTokens(outputTokens);
                }
                logEntry.setTotalTokens(logEntry.getInputTokens() + logEntry.getOutputTokens());
                logEntry.setCost(calculateCost(providerType, model, logEntry.getInputTokens(), logEntry.getOutputTokens()));
                logEntry.setResponse(response != null && response.length() > 500 ? response.substring(0, 500) + "..." : response);
                
                log.info("LLM response received from provider: {}", providerType);
                
                if (activationId != null && skillActivationService != null) {
                    skillActivationService.deactivateContext(activationId);
                }
                
                logEntry.setStatus("success");
                recordCallLog(logEntry, startTime);
                
                if (actionResult != null) {
                    ChatResponseDTO responseDTO = ChatResponseDTO.success(response, model, providerType);
                    responseDTO.setAction(actionResult);
                    
                    result.setData(responseDTO);
                    result.setCode(200);
                    result.setMessage("Success");
                    return result;
                }
            } else if (mockEnabled) {
                response = getMockResponse(prompt);
                log.info("Using mock response (mockEnabled=true), provider not available: {}", providerType);
                logEntry.setStatus("success");
                logEntry.setResponse(response != null && response.length() > 500 ? response.substring(0, 500) + "..." : response);
                recordCallLog(logEntry, startTime);
            } else {
                log.warn("No LLM provider available and mock is disabled");
                result.setCode(503);
                result.setMessage("No LLM provider available and mock is disabled");
                logEntry.setStatus("error");
                logEntry.setErrorMessage("No LLM provider available and mock is disabled");
                recordCallLog(logEntry, startTime);
                return result;
            }

            ChatResponseDTO responseDTO = ChatResponseDTO.success(response, model, providerType);
            
            result.setData(responseDTO);
            result.setCode(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Chat API error", e);
            result.setCode(500);
            result.setMessage("Chat failed: " + e.getMessage());
            logEntry.setStatus("error");
            logEntry.setErrorMessage(e.getMessage());
            recordCallLog(logEntry, startTime);
        }

        return result;
    }

    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter chatStream(@RequestBody @Valid ChatRequestDTO request) {
        log.info("Stream Chat API called");
        
        final String prompt = request.getMessage();
        final String model = request.getModel() != null ? request.getModel() : currentModel;
        final String providerType = currentProviderType;
        final long startTime = System.currentTimeMillis();
        
        final LlmCallLogDTO logEntry = new LlmCallLogDTO();
        logEntry.setProviderId(providerType);
        logEntry.setProviderName(getProviderDisplayName(providerType));
        logEntry.setModel(model);
        logEntry.setRequestType("chat-stream");
        logEntry.setPrompt(prompt);
        
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT);
        
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    LlmProvider provider = providers.get(providerType);
                    
                    if (provider != null) {
                        SkillActivationContext activationContext = null;
                        String activationId = null;
                        
                        if (skillActivationService != null) {
                            ActivationRequest activationRequest = ActivationRequest.builder()
                                .skillId("skill-scene")
                                .userId("current-user")
                                .roleId(DEFAULT_ROLE_ID)
                                .build();
                            
                            activationContext = skillActivationService.activateSkill(activationRequest);
                            activationId = activationContext.getActivationId();
                            log.info("[LlmController] Skill activated for stream: {}", activationId);
                        }
                        
                        List<Map<String, Object>> messages = new ArrayList<Map<String, Object>>();
                        
                        Map<String, Object> systemMessage = new HashMap<String, Object>();
                        systemMessage.put("role", "system");
                        String systemPrompt;
                        if (activationContext != null) {
                            systemPrompt = getSystemPrompt() + "\n\n" + activationContext.buildSystemPrompt();
                        } else {
                            systemPrompt = getSystemPrompt();
                        }
                        systemMessage.put("content", systemPrompt);
                        messages.add(systemMessage);
                        
                        Map<String, Object> userMessage = new HashMap<String, Object>();
                        userMessage.put("role", "user");
                        userMessage.put("content", prompt);
                        messages.add(userMessage);
                        
                        Map<String, Object> options = new HashMap<String, Object>();
                        if (request.getTemperature() != null) {
                            options.put("temperature", request.getTemperature());
                        }
                        if (request.getMaxTokens() != null) {
                            options.put("max_tokens", request.getMaxTokens());
                        }
                        
                        if (provider.supportsFunctionCalling()) {
                            List<Map<String, Object>> tools;
                            if (activationContext != null && activationContext.getFunctionContext() != null) {
                                tools = activationContext.getFunctionContext().toTools();
                                log.debug("Added {} tools from SkillActivationContext for stream", tools.size());
                            } else if (toolRegistry != null) {
                                tools = toolRegistry.getToolDefinitions();
                                log.debug("Added {} tools from ToolRegistry for stream", tools.size());
                            } else {
                                tools = Collections.emptyList();
                            }
                            
                            if (!tools.isEmpty()) {
                                options.put("tools", tools);
                                options.put("tool_choice", "auto");
                            }
                        }
                        
                        final StringBuilder fullResponse = new StringBuilder();
                        final SkillActivationContext finalActivationContext = activationContext;
                        final String finalActivationId = activationId;
                        
                        if (provider instanceof AliyunBailianLlmProvider) {
                            AliyunBailianLlmProvider bailianProvider = (AliyunBailianLlmProvider) provider;
                            
                            bailianProvider.chatStream(model, messages, options,
                                new java.util.function.Consumer<String>() {
                                    @Override
                                    public void accept(String chunk) {
                                        try {
                                            fullResponse.append(chunk);
                                            emitter.send(SseEmitter.event()
                                                .name("message")
                                                .data(chunk));
                                        } catch (IOException e) {
                                            log.error("Error sending stream chunk", e);
                                            throw new RuntimeException(e);
                                        }
                                    }
                                },
                                new java.util.function.Consumer<Map<String, Object>>() {
                                    @Override
                                    public void accept(Map<String, Object> result) {
                                        try {
                                            String content = (String) result.get("content");
                                            
                                            Integer inputTokens = (Integer) result.get("inputTokens");
                                            Integer outputTokens = (Integer) result.get("outputTokens");
                                            if (inputTokens != null) {
                                                logEntry.setInputTokens(inputTokens);
                                            }
                                            if (outputTokens != null) {
                                                logEntry.setOutputTokens(outputTokens);
                                            }
                                            logEntry.setTotalTokens(logEntry.getInputTokens() + logEntry.getOutputTokens());
                                            logEntry.setCost(calculateCost(providerType, model, logEntry.getInputTokens(), logEntry.getOutputTokens()));
                                            logEntry.setResponse(content != null && content.length() > 500 ? content.substring(0, 500) + "..." : content);
                                            
                                            if (finalActivationId != null && skillActivationService != null) {
                                                skillActivationService.deactivateContext(finalActivationId);
                                            }
                                            
                                            logEntry.setStatus("success");
                                            recordCallLog(logEntry, startTime);
                                            
                                            emitter.send(SseEmitter.event()
                                                .name("done")
                                                .data("[DONE]"));
                                            emitter.complete();
                                            
                                            log.info("Stream completed successfully, total length: {}", fullResponse.length());
                                        } catch (Exception e) {
                                            log.error("Error completing stream", e);
                                            try {
                                                emitter.completeWithError(e);
                                            } catch (Exception ex) {
                                                log.error("Error completing with error", ex);
                                            }
                                        }
                                    }
                                },
                                new java.util.function.Consumer<Exception>() {
                                    @Override
                                    public void accept(Exception e) {
                                        log.error("Stream error", e);
                                        logEntry.setStatus("error");
                                        logEntry.setErrorMessage(e.getMessage());
                                        recordCallLog(logEntry, startTime);
                                        try {
                                            emitter.send(SseEmitter.event()
                                                .name("error")
                                                .data("Stream error: " + e.getMessage()));
                                            emitter.complete();
                                        } catch (Exception ex) {
                                            log.error("Error sending error event", ex);
                                        }
                                    }
                                }
                            );
                        } else {
                            Map<String, Object> chatResult = provider.chat(model, messages, options);
                            String response = (String) chatResult.get("content");
                            
                            Integer inputTokens = (Integer) chatResult.get("inputTokens");
                            Integer outputTokens = (Integer) chatResult.get("outputTokens");
                            if (inputTokens != null) {
                                logEntry.setInputTokens(inputTokens);
                            }
                            if (outputTokens != null) {
                                logEntry.setOutputTokens(outputTokens);
                            }
                            logEntry.setTotalTokens(logEntry.getInputTokens() + logEntry.getOutputTokens());
                            logEntry.setCost(calculateCost(providerType, model, logEntry.getInputTokens(), logEntry.getOutputTokens()));
                            logEntry.setResponse(response != null && response.length() > 500 ? response.substring(0, 500) + "..." : response);
                            
                            Map<String, Object> actionResult = (Map<String, Object>) chatResult.get("actionResult");
                            
                            if (actionResult != null) {
                                ChatResponseDTO responseDTO = ChatResponseDTO.success(response, model, providerType);
                                responseDTO.setAction(actionResult);
                                
                                String jsonResponse = LlmController.this.objectMapper.writeValueAsString(responseDTO);
                                emitter.send(SseEmitter.event()
                                    .name("action")
                                    .data(jsonResponse));
                                
                                log.info("[LlmController] Action returned in stream response: {}", actionResult);
                            }
                            
                            if (finalActivationId != null && skillActivationService != null) {
                                skillActivationService.deactivateContext(finalActivationId);
                            }
                            
                            logEntry.setStatus("success");
                            recordCallLog(logEntry, startTime);
                            
                            int chunkSize = 8;
                            for (int i = 0; i < response.length(); i += chunkSize) {
                                int end = Math.min(i + chunkSize, response.length());
                                String chunk = response.substring(i, end);
                                
                                emitter.send(SseEmitter.event()
                                    .name("message")
                                    .data(chunk));
                                
                                Thread.sleep(30);
                            }
                            
                            emitter.send(SseEmitter.event()
                                .name("done")
                                .data("[DONE]"));
                            emitter.complete();
                        }
                    } else if (mockEnabled) {
                        String mockResponse = getMockResponse(prompt);
                        logEntry.setStatus("success");
                        logEntry.setResponse(mockResponse != null && mockResponse.length() > 500 ? mockResponse.substring(0, 500) + "..." : mockResponse);
                        recordCallLog(logEntry, startTime);
                        
                        int chunkSize = 8;
                        for (int i = 0; i < mockResponse.length(); i += chunkSize) {
                            int end = Math.min(i + chunkSize, mockResponse.length());
                            String chunk = mockResponse.substring(i, end);
                            
                            emitter.send(SseEmitter.event()
                                .name("message")
                                .data(chunk));
                            
                            Thread.sleep(30);
                        }
                        
                        emitter.send(SseEmitter.event()
                            .name("done")
                            .data("[DONE]"));
                        emitter.complete();
                    } else {
                        String errorMsg = "Error: No LLM provider available and mock is disabled";
                        emitter.send(SseEmitter.event().name("error").data(errorMsg));
                        logEntry.setStatus("error");
                        logEntry.setErrorMessage(errorMsg);
                        recordCallLog(logEntry, startTime);
                        emitter.complete();
                    }
                    
                } catch (IOException e) {
                    log.error("Error sending SSE chunk", e);
                    logEntry.setStatus("error");
                    logEntry.setErrorMessage(e.getMessage());
                    recordCallLog(logEntry, startTime);
                    emitter.completeWithError(e);
                } catch (InterruptedException e) {
                    log.error("SSE interrupted", e);
                    logEntry.setStatus("error");
                    logEntry.setErrorMessage(e.getMessage());
                    recordCallLog(logEntry, startTime);
                    emitter.completeWithError(e);
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    log.error("Stream execution error", e);
                    logEntry.setStatus("error");
                    logEntry.setErrorMessage(e.getMessage());
                    recordCallLog(logEntry, startTime);
                    emitter.completeWithError(e);
                }
            }
        });
        
        emitter.onCompletion(new Runnable() {
            @Override
            public void run() {
                log.info("SSE completed");
            }
        });
        
        emitter.onTimeout(new Runnable() {
            @Override
            public void run() {
                log.warn("SSE timeout");
                emitter.complete();
            }
        });
        
        emitter.onError(new java.util.function.Consumer<Throwable>() {
            @Override
            public void accept(Throwable t) {
                log.error("SSE error: {}", t.getMessage());
            }
        });
        
        return emitter;
    }

    @PostMapping("/models")
    @ResponseBody
    public ResultModel<Map<String, Object>> getAvailableModels() {
        log.info("Get Available Models API called");
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();

        try {
            List<String> allModels = new ArrayList<String>();
            Map<String, List<String>> modelsByProvider = new HashMap<String, List<String>>();
            
            for (Map.Entry<String, LlmProvider> entry : providers.entrySet()) {
                List<String> providerModels = entry.getValue().getSupportedModels();
                modelsByProvider.put(entry.getKey(), providerModels);
                allModels.addAll(providerModels);
            }
            
            if (allModels.isEmpty()) {
                allModels.add("default");
            }

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("models", allModels);
            data.put("modelsByProvider", modelsByProvider);
            data.put("currentModel", currentModel);
            data.put("currentProvider", currentProviderType);
            data.put("providers", new ArrayList<String>(providers.keySet()));
            
            result.setData(data);
            result.setCode(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Get Available Models API error", e);
            result.setCode(500);
            result.setMessage("Get models failed: " + e.getMessage());
        }

        return result;
    }

    @GetMapping("/providers/info")
    @ResponseBody
    public ResultModel<List<Map<String, Object>>> getProvidersInfo() {
        log.info("Get Providers Info API called");
        
        try {
            List<Map<String, Object>> providerList = new ArrayList<Map<String, Object>>();
            
            for (LlmProviderType providerType : LlmProviderType.values()) {
                Map<String, Object> providerInfo = new HashMap<String, Object>();
                providerInfo.put("code", providerType.getCode());
                providerInfo.put("displayName", providerType.getDisplayName());
                providerInfo.put("description", providerType.getDescription());
                providerInfo.put("defaultBaseUrl", providerType.getDefaultBaseUrl());
                
                List<Map<String, Object>> modelsList = new ArrayList<Map<String, Object>>();
                for (LlmProviderType.ModelInfo modelInfo : providerType.getModels()) {
                    Map<String, Object> modelMap = new HashMap<String, Object>();
                    modelMap.put("modelId", modelInfo.getModelId());
                    modelMap.put("displayName", modelInfo.getDisplayName());
                    modelMap.put("maxTokens", modelInfo.getMaxTokens());
                    modelMap.put("defaultTemperature", modelInfo.getDefaultTemperature());
                    modelMap.put("supportsFunctionCalling", modelInfo.isSupportsFunctionCalling());
                    modelMap.put("supportsMultimodal", modelInfo.isSupportsMultimodal());
                    modelMap.put("supportsEmbedding", modelInfo.isSupportsEmbedding());
                    modelMap.put("costPer1kTokens", modelInfo.getCostPer1kTokens());
                    modelsList.add(modelMap);
                }
                providerInfo.put("models", modelsList);
                providerInfo.put("modelCount", modelsList.size());
                providerInfo.put("isCurrent", providerType.getCode().equals(currentProviderType));
                providerInfo.put("isConfigured", providers.containsKey(providerType.getCode()));
                
                providerList.add(providerInfo);
            }
            
            return ResultModel.success(providerList);
        } catch (Exception e) {
            log.error("Get Providers Info API error", e);
            return ResultModel.error("Get providers info failed: " + e.getMessage());
        }
    }

    @PostMapping("/models/set")
    @ResponseBody
    public ResultModel<Boolean> setModel(@RequestBody SetModelRequestDTO request) {
        log.info("Set Model API called: modelId={}, provider={}", request.getModelId(), request.getProvider());
        ResultModel<Boolean> result = new ResultModel<Boolean>();

        try {
            String modelId = request.getModelId();
            String providerType = request.getProvider();
            
            if (providerType != null && providers.containsKey(providerType)) {
                currentProviderType = providerType;
            }
            
            if (modelId != null) {
                currentModel = modelId;
            }
            
            result.setData(true);
            result.setCode(200);
            result.setMessage("Model set successfully");
        } catch (Exception e) {
            log.error("Set Model API error", e);
            result.setCode(500);
            result.setMessage("Set model failed: " + e.getMessage());
            result.setData(false);
        }

        return result;
    }

    @PostMapping("/health")
    @ResponseBody
    public ResultModel<Map<String, Object>> health() {
        log.info("LLM Health check API called");
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();

        try {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("healthy", true);
            data.put("currentModel", currentModel);
            data.put("currentProvider", currentProviderType);
            data.put("availableProviders", providers.keySet());
            data.put("providerCount", providers.size());
            
            result.setData(data);
            result.setCode(200);
            result.setMessage("LLM service is healthy");
        } catch (Exception e) {
            log.error("Health check error", e);
            result.setCode(500);
            result.setMessage("Health check failed: " + e.getMessage());
        }

        return result;
    }

    private String getProviderDisplayName(String providerId) {
        LlmProviderType providerType = LlmProviderType.fromCode(providerId);
        if (providerType != null) {
            return providerType.getDisplayName();
        }
        return providerId;
    }

    private String getModelDisplayName(String modelId) {
        for (LlmProviderType providerType : LlmProviderType.values()) {
            for (LlmProviderType.ModelInfo modelInfo : providerType.getModels()) {
                if (modelInfo.getModelId().equals(modelId)) {
                    return modelInfo.getDisplayName();
                }
            }
        }
        return modelId;
    }

    private List<Map<String, Object>> getDefaultModelsForProvider(String provider) {
        List<Map<String, Object>> models = new ArrayList<Map<String, Object>>();
        
        LlmProviderType providerType = LlmProviderType.fromCode(provider);
        if (providerType != null) {
            for (LlmProviderType.ModelInfo modelInfo : providerType.getModels()) {
                Map<String, Object> model = new HashMap<String, Object>();
                model.put("value", modelInfo.getModelId());
                model.put("label", modelInfo.getDisplayName());
                model.put("provider", provider);
                models.add(model);
            }
        }
        
        return models;
    }

    @PostMapping("/complete")
    @ResponseBody
    public ResultModel<String> complete(@RequestBody @Valid CompleteRequestDTO request) {
        log.info("Complete API called");
        ResultModel<String> result = new ResultModel<String>();
        
        long startTime = System.currentTimeMillis();
        LlmCallLogDTO logEntry = new LlmCallLogDTO();
        logEntry.setProviderId(currentProviderType);
        logEntry.setProviderName(getProviderDisplayName(currentProviderType));
        logEntry.setModel(request.getModel() != null ? request.getModel() : currentModel);
        logEntry.setRequestType("complete");
        logEntry.setPrompt(request.getPrompt());

        try {
            String prompt = request.getPrompt();
            String model = request.getModel() != null ? request.getModel() : currentModel;
            String providerType = currentProviderType;
            
            LlmProvider provider = providers.get(providerType);
            
            String response;
            if (provider != null) {
                Map<String, Object> options = new HashMap<String, Object>();
                if (request.getTemperature() != null) {
                    options.put("temperature", request.getTemperature());
                }
                if (request.getMaxTokens() != null) {
                    options.put("max_tokens", request.getMaxTokens());
                }
                response = provider.complete(model, prompt, options);
                logEntry.setStatus("success");
                logEntry.setResponse(response != null && response.length() > 500 ? response.substring(0, 500) + "..." : response);
            } else {
                response = getMockResponse(prompt);
                logEntry.setStatus("success");
                logEntry.setResponse(response != null && response.length() > 500 ? response.substring(0, 500) + "..." : response);
            }
            
            recordCallLog(logEntry, startTime);
            result.setData(response);
            result.setCode(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Complete API error", e);
            logEntry.setStatus("error");
            logEntry.setErrorMessage(e.getMessage());
            recordCallLog(logEntry, startTime);
            result.setCode(500);
            result.setMessage("Complete failed: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/translate")
    @ResponseBody
    public ResultModel<String> translate(@RequestBody @Valid TranslateRequestDTO request) {
        log.info("Translate API called");
        ResultModel<String> result = new ResultModel<String>();
        
        long startTime = System.currentTimeMillis();
        LlmCallLogDTO logEntry = new LlmCallLogDTO();
        logEntry.setProviderId(currentProviderType);
        logEntry.setProviderName(getProviderDisplayName(currentProviderType));
        logEntry.setModel(request.getModel() != null ? request.getModel() : currentModel);
        logEntry.setRequestType("translate");
        logEntry.setPrompt(request.getText());

        try {
            String text = request.getText();
            String targetLanguage = request.getTargetLang();
            String sourceLanguage = request.getSourceLang();
            String model = request.getModel() != null ? request.getModel() : currentModel;
            String providerType = currentProviderType;
            
            LlmProvider provider = providers.get(providerType);
            
            String response;
            if (provider != null) {
                response = provider.translate(model, text, targetLanguage, sourceLanguage);
                logEntry.setStatus("success");
                logEntry.setResponse(response != null && response.length() > 500 ? response.substring(0, 500) + "..." : response);
            } else {
                response = "[翻译结果] " + text;
                logEntry.setStatus("success");
                logEntry.setResponse(response);
            }
            
            recordCallLog(logEntry, startTime);
            result.setData(response);
            result.setCode(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Translate API error", e);
            logEntry.setStatus("error");
            logEntry.setErrorMessage(e.getMessage());
            recordCallLog(logEntry, startTime);
            result.setCode(500);
            result.setMessage("Translate failed: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/summarize")
    @ResponseBody
    public ResultModel<String> summarize(@RequestBody @Valid SummarizeRequestDTO request) {
        log.info("Summarize API called");
        ResultModel<String> result = new ResultModel<String>();
        
        long startTime = System.currentTimeMillis();
        LlmCallLogDTO logEntry = new LlmCallLogDTO();
        logEntry.setProviderId(currentProviderType);
        logEntry.setProviderName(getProviderDisplayName(currentProviderType));
        logEntry.setModel(request.getModel() != null ? request.getModel() : currentModel);
        logEntry.setRequestType("summarize");
        logEntry.setPrompt(request.getText() != null && request.getText().length() > 200 ? request.getText().substring(0, 200) + "..." : request.getText());

        try {
            String text = request.getText();
            Integer maxLength = request.getMaxLength() != null ? request.getMaxLength() : 200;
            String model = request.getModel() != null ? request.getModel() : currentModel;
            String providerType = currentProviderType;
            
            LlmProvider provider = providers.get(providerType);
            
            String response;
            if (provider != null) {
                response = provider.summarize(model, text, maxLength);
                logEntry.setStatus("success");
                logEntry.setResponse(response != null && response.length() > 500 ? response.substring(0, 500) + "..." : response);
            } else {
                response = text.length() > maxLength ? text.substring(0, maxLength) + "..." : text;
                logEntry.setStatus("success");
                logEntry.setResponse(response);
            }
            
            recordCallLog(logEntry, startTime);
            result.setData(response);
            result.setCode(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Summarize API error", e);
            logEntry.setStatus("error");
            logEntry.setErrorMessage(e.getMessage());
            recordCallLog(logEntry, startTime);
            result.setCode(500);
            result.setMessage("Summarize failed: " + e.getMessage());
        }

        return result;
    }

    private String getSystemPrompt() {
        return getSystemPrompt("skill-scene");
    }
    
    private String getSystemPrompt(String skillId) {
        if (contextManager != null) {
            Map<String, Object> context = contextManager.getCurrentContext();
            log.debug("[LlmController] Current context level: {}", contextManager.getContextLevel());
        }
        
        if (skillPromptService != null) {
            String prompt = skillPromptService.getSystemPrompt(skillId);
            if (prompt != null && !prompt.isEmpty()) {
                return prompt;
            }
        }
        return getBuiltInDefaultPrompt();
    }
    
    private String getBuiltInDefaultPrompt() {
        StringBuilder prompt = new StringBuilder();
        prompt.append("你是Ooder场景技能平台的智能助手。\n\n");
        prompt.append("## 平台简介\n");
        prompt.append("Ooder是一个场景驱动的技能管理平台，用户可以通过发现、安装、配置能力来构建自动化场景。\n\n");
        prompt.append("## 核心概念\n");
        prompt.append("- **能力(Capability)**: 可执行的功能单元，如发送邮件、生成报告等\n");
        prompt.append("- **场景(Scene)**: 由多个能力组成的自动化流程\n\n");
        prompt.append("## 回复要求\n");
        prompt.append("- 用简洁专业的中文回复\n");
        prompt.append("- 不要提及你是DeepSeek或其他AI模型\n");
        return prompt.toString();
    }

    private String getMockResponse(String prompt) {
        return "[Mock Mode] LLM 服务未配置。请在 application.yml 中配置有效的 LLM Provider (deepseek/baidu/qianwen/openai)。";
    }
    
    private void recordCallLog(LlmCallLogDTO logEntry, long startTime) {
        if (llmCallLogService != null) {
            long latency = System.currentTimeMillis() - startTime;
            logEntry.setLatency(latency);
            llmCallLogService.recordCall(logEntry);
        }
        
        if (llmAuditService != null) {
            long latency = System.currentTimeMillis() - startTime;
            
            LlmCallContext context = LlmCallContext.builder()
                .companyId("default")
                .userId(logEntry.getUserId() != null ? logEntry.getUserId() : "anonymous")
                .sceneId(logEntry.getSceneId())
                .capabilityId(logEntry.getCapabilityId())
                .moduleId(logEntry.getModuleId())
                .build();
            
            LlmCallResult result = LlmCallResult.builder()
                .providerId(logEntry.getProviderId())
                .providerName(logEntry.getProviderName())
                .model(logEntry.getModel())
                .requestType(logEntry.getRequestType())
                .inputTokens(logEntry.getInputTokens())
                .outputTokens(logEntry.getOutputTokens())
                .totalTokens(logEntry.getTotalTokens())
                .cost(logEntry.getCost())
                .latency(latency)
                .status(logEntry.getStatus())
                .errorMessage(logEntry.getErrorMessage())
                .build();
            
            llmAuditService.logLlmCall(context, result);
        }
    }
    
    private double calculateCost(String providerId, String model, int inputTokens, int outputTokens) {
        LlmProviderType providerType = LlmProviderType.fromCode(providerId);
        if (providerType != null) {
            for (LlmProviderType.ModelInfo modelInfo : providerType.getModels()) {
                if (modelInfo.getModelId().equals(model)) {
                    double costPer1k = modelInfo.getCostPer1kTokens();
                    return (inputTokens * costPer1k / 1000.0) + (outputTokens * costPer1k / 1000.0);
                }
            }
            if (!providerType.getModels().isEmpty()) {
                double defaultCostPer1k = providerType.getModels().get(0).getCostPer1kTokens();
                return (inputTokens * defaultCostPer1k / 1000.0) + (outputTokens * defaultCostPer1k / 1000.0);
            }
        }
        return 0.0;
    }
    
    @GetMapping("/tools")
    @ResponseBody
    public ResultModel<List<Map<String, Object>>> getToolDefinitions() {
        log.info("Get Tool Definitions API called");
        ResultModel<List<Map<String, Object>>> result = new ResultModel<>();
        
        try {
            List<Map<String, Object>> tools = new ArrayList<>();
            
            if (toolRegistry != null) {
                List<Map<String, Object>> toolDefs = toolRegistry.getToolDefinitions();
                if (toolDefs != null) {
                    for (Map<String, Object> toolDef : toolDefs) {
                        Map<String, Object> tool = new LinkedHashMap<>();
                        tool.put("name", toolDef.get("function") != null ? 
                            ((Map<String, Object>) toolDef.get("function")).get("name") : "unknown");
                        
                        Map<String, Object> function = (Map<String, Object>) toolDef.get("function");
                        if (function != null) {
                            tool.put("description", function.get("description"));
                            Map<String, Object> params = (Map<String, Object>) function.get("parameters");
                            if (params != null) {
                                Map<String, Object> properties = (Map<String, Object>) params.get("properties");
                                tool.put("parameters", properties != null ? properties.keySet() : new ArrayList<>());
                            }
                        }
                        tool.put("type", toolDef.get("type") != null ? toolDef.get("type") : "function");
                        tools.add(tool);
                    }
                }
            }
            
            result.setData(tools);
            result.setCode(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Get Tool Definitions API error", e);
            result.setCode(500);
            result.setMessage("Failed to get tool definitions: " + e.getMessage());
        }
        
        return result;
    }
    
    @GetMapping("/tools/{name}")
    @ResponseBody
    public ResultModel<Map<String, Object>> getToolDefinition(@PathVariable String name) {
        log.info("Get Tool Definition API called for: {}", name);
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        
        try {
            if (toolRegistry != null) {
                List<Map<String, Object>> toolDefs = toolRegistry.getToolDefinitions();
                if (toolDefs != null) {
                    for (Map<String, Object> toolDef : toolDefs) {
                        Map<String, Object> function = (Map<String, Object>) toolDef.get("function");
                        if (function != null && name.equals(function.get("name"))) {
                            result.setData(function);
                            result.setCode(200);
                            result.setMessage("Success");
                            return result;
                        }
                    }
                }
            }
            
            result.setCode(404);
            result.setMessage("Tool not found: " + name);
        } catch (Exception e) {
            log.error("Get Tool Definition API error", e);
            result.setCode(500);
            result.setMessage("Failed to get tool definition: " + e.getMessage());
        }
        
        return result;
    }
    
    @GetMapping("/docs")
    @ResponseBody
    public ResultModel<List<Map<String, Object>>> getLlmDocs() {
        log.info("Get LLM Docs API called");
        ResultModel<List<Map<String, Object>>> result = new ResultModel<>();
        
        try {
            List<Map<String, Object>> docs = new ArrayList<>();
            
            docs.add(createDocItem("llm-config-guide", "LLM 配置指南", 
                "# LLM 配置指南\n\n" +
                "## 配置步骤\n\n" +
                "1. 选择 Provider（如阿里云百炼、OpenAI 等）\n" +
                "2. 配置 API Key\n" +
                "3. 选择模型\n" +
                "4. 测试连接\n\n" +
                "## 支持的 Provider\n\n" +
                "- **阿里云百炼**: 支持 qwen 系列模型\n" +
                "- **OpenAI**: 支持 GPT-4、GPT-3.5 等\n" +
                "- **DeepSeek**: 支持 deepseek-chat、deepseek-coder\n\n" +
                "## Function Calling\n\n" +
                "启用 Function Calling 后，LLM 可以调用预定义的工具函数。\n"));
            
            docs.add(createDocItem("function-calling", "Function Calling 说明",
                "# Function Calling 说明\n\n" +
                "## 什么是 Function Calling\n\n" +
                "Function Calling 允许 LLM 调用外部工具函数，扩展 LLM 的能力。\n\n" +
                "## 工作原理\n\n" +
                "1. 用户发送消息\n" +
                "2. LLM 判断是否需要调用工具\n" +
                "3. 如果需要，返回工具调用请求\n" +
                "4. 系统执行工具并返回结果\n" +
                "5. LLM 基于结果生成最终回复\n\n" +
                "## 已注册的工具\n\n" +
                "查看下方 Function Calling 列表获取已注册的工具。\n"));
            
            docs.add(createDocItem("test-guide", "测试指南",
                "# LLM 测试指南\n\n" +
                "## 如何测试 LLM 配置\n\n" +
                "1. 在测试面板选择 Provider 和 Model\n" +
                "2. 输入测试消息\n" +
                "3. 选择是否启用 Function Calling\n" +
                "4. 点击测试按钮\n\n" +
                "## 查看通讯日志\n\n" +
                "测试完成后，可以在通讯日志区域查看完整的请求和响应数据。\n\n" +
                "## 常见问题\n\n" +
                "- **连接失败**: 检查 API Key 是否正确\n" +
                "- **超时**: 检查网络连接或增加超时时间\n" +
                "- **余额不足**: 检查账户余额\n"));
            
            result.setData(docs);
            result.setCode(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Get LLM Docs API error", e);
            result.setCode(500);
            result.setMessage("Failed to get docs: " + e.getMessage());
        }
        
        return result;
    }
    
    private Map<String, Object> createDocItem(String id, String title, String content) {
        Map<String, Object> doc = new LinkedHashMap<>();
        doc.put("id", id);
        doc.put("title", title);
        doc.put("content", content);
        return doc;
    }
}

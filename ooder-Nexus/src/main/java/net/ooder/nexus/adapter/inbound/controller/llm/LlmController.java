package net.ooder.nexus.adapter.inbound.controller.llm;

import net.ooder.config.ResultModel;
import net.ooder.nexus.service.NexusLlmService;
import net.ooder.sdk.api.llm.FunctionDef;
import net.ooder.sdk.api.llm.TokenUsage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/llm")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class LlmController {

    private static final Logger log = LoggerFactory.getLogger(LlmController.class);
    private static final long SSE_TIMEOUT = 60000L;
    
    private final ExecutorService executor = Executors.newCachedThreadPool();

    @Autowired
    private NexusLlmService llmService;

    @PostMapping("/chat")
    @ResponseBody
    public ResultModel<Map<String, Object>> chat(@RequestBody Map<String, Object> request) {
        log.info("Chat API called");
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();

        try {
            String prompt = (String) request.get("prompt");
            String systemPrompt = (String) request.get("systemPrompt");
            
            String response;
            if (systemPrompt != null && !systemPrompt.isEmpty()) {
                response = llmService.chat(prompt, systemPrompt);
            } else {
                response = llmService.chat(prompt);
            }

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("response", response);
            data.put("model", llmService.getCurrentModel());
            data.put("tokenUsage", llmService.getTokenUsage());
            
            result.setData(data);
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Chat API error", e);
            result.setRequestStatus(500);
            result.setMessage("Chat failed: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/chat/async")
    @ResponseBody
    public ResultModel<Map<String, Object>> chatAsync(@RequestBody Map<String, Object> request) {
        log.info("Async Chat API called");
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();

        try {
            String prompt = (String) request.get("prompt");
            
            CompletableFuture<String> future = llmService.chatAsync(prompt);
            String response = future.get();

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("response", response);
            data.put("model", llmService.getCurrentModel());
            
            result.setData(data);
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Async Chat API error", e);
            result.setRequestStatus(500);
            result.setMessage("Async chat failed: " + e.getMessage());
        }

        return result;
    }

    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter chatStream(@RequestBody Map<String, Object> request) {
        log.info("Stream Chat API called");
        
        String prompt = (String) request.get("prompt");
        String systemPrompt = (String) request.get("systemPrompt");
        
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT);
        
        executor.execute(() -> {
            try {
                if (systemPrompt != null && !systemPrompt.isEmpty()) {
                    llmService.chatStream(
                        prompt, 
                        systemPrompt,
                        chunk -> {
                            try {
                                emitter.send(SseEmitter.event()
                                    .name("message")
                                    .data(chunk));
                            } catch (IOException e) {
                                log.error("Error sending SSE chunk", e);
                                emitter.completeWithError(e);
                            }
                        },
                        v -> {
                            try {
                                emitter.send(SseEmitter.event()
                                    .name("done")
                                    .data("[DONE]"));
                                emitter.complete();
                            } catch (IOException e) {
                                log.error("Error completing SSE", e);
                                emitter.completeWithError(e);
                            }
                        },
                        error -> {
                            log.error("Stream error", error);
                            emitter.completeWithError(error);
                        }
                    );
                } else {
                    llmService.chatStream(
                        prompt,
                        chunk -> {
                            try {
                                emitter.send(SseEmitter.event()
                                    .name("message")
                                    .data(chunk));
                            } catch (IOException e) {
                                log.error("Error sending SSE chunk", e);
                                emitter.completeWithError(e);
                            }
                        },
                        v -> {
                            try {
                                emitter.send(SseEmitter.event()
                                    .name("done")
                                    .data("[DONE]"));
                                emitter.complete();
                            } catch (IOException e) {
                                log.error("Error completing SSE", e);
                                emitter.completeWithError(e);
                            }
                        },
                        error -> {
                            log.error("Stream error", error);
                            emitter.completeWithError(error);
                        }
                    );
                }
            } catch (Exception e) {
                log.error("Stream execution error", e);
                emitter.completeWithError(e);
            }
        });
        
        emitter.onCompletion(() -> log.info("SSE completed"));
        emitter.onTimeout(() -> {
            log.warn("SSE timeout");
            emitter.complete();
        });
        emitter.onError(e -> log.error("SSE error", e));
        
        return emitter;
    }

    @PostMapping("/chat/functions")
    @ResponseBody
    public ResultModel<Map<String, Object>> chatWithFunctions(@RequestBody Map<String, Object> request) {
        log.info("Chat with Functions API called");
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();

        try {
            String prompt = (String) request.get("prompt");
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> functionDefs = (List<Map<String, Object>>) request.get("functions");
            List<FunctionDef> functions = new ArrayList<FunctionDef>();
            
            if (functionDefs != null) {
                for (Map<String, Object> funcDef : functionDefs) {
                    FunctionDef def = FunctionDef.of(
                        (String) funcDef.get("name"),
                        (String) funcDef.get("description")
                    );
                    functions.add(def);
                }
            }

            String response = llmService.chatWithFunctions(prompt, functions);

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("response", response);
            data.put("model", llmService.getCurrentModel());
            
            result.setData(data);
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Chat with Functions API error", e);
            result.setRequestStatus(500);
            result.setMessage("Chat with functions failed: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/embed")
    @ResponseBody
    public ResultModel<Map<String, Object>> embed(@RequestBody Map<String, String> request) {
        log.info("Embed API called");
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();

        try {
            String text = request.get("text");
            float[] embedding = llmService.embed(text);

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("embedding", embedding);
            data.put("dimensions", embedding.length);
            
            result.setData(data);
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Embed API error", e);
            result.setRequestStatus(500);
            result.setMessage("Embed failed: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/embed/batch")
    @ResponseBody
    public ResultModel<Map<String, Object>> embedBatch(@RequestBody Map<String, Object> request) {
        log.info("Batch Embed API called");
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();

        try {
            @SuppressWarnings("unchecked")
            List<String> texts = (List<String>) request.get("texts");
            List<float[]> embeddings = llmService.embedBatch(texts);

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("embeddings", embeddings);
            data.put("count", embeddings.size());
            
            result.setData(data);
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Batch Embed API error", e);
            result.setRequestStatus(500);
            result.setMessage("Batch embed failed: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/tokens/count")
    @ResponseBody
    public ResultModel<Map<String, Object>> countTokens(@RequestBody Map<String, String> request) {
        log.info("Count Tokens API called");
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();

        try {
            String text = request.get("text");
            int count = llmService.countTokens(text);

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("count", count);
            
            result.setData(data);
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Count Tokens API error", e);
            result.setRequestStatus(500);
            result.setMessage("Token count failed: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/usage")
    @ResponseBody
    public ResultModel<TokenUsage> getTokenUsage() {
        log.info("Get Token Usage API called");
        ResultModel<TokenUsage> result = new ResultModel<TokenUsage>();

        try {
            TokenUsage usage = llmService.getTokenUsage();
            result.setData(usage);
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Get Token Usage API error", e);
            result.setRequestStatus(500);
            result.setMessage("Get token usage failed: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/models")
    @ResponseBody
    public ResultModel<Map<String, Object>> getAvailableModels() {
        log.info("Get Available Models API called");
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();

        try {
            List<String> models = llmService.getAvailableModels();
            String currentModel = llmService.getCurrentModel();

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("models", models);
            data.put("currentModel", currentModel);
            
            result.setData(data);
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Get Available Models API error", e);
            result.setRequestStatus(500);
            result.setMessage("Get models failed: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/models/set")
    @ResponseBody
    public ResultModel<Boolean> setModel(@RequestBody Map<String, String> request) {
        log.info("Set Model API called: {}", request.get("modelId"));
        ResultModel<Boolean> result = new ResultModel<Boolean>();

        try {
            String modelId = request.get("modelId");
            llmService.setModel(modelId);
            
            result.setData(true);
            result.setRequestStatus(200);
            result.setMessage("Model set successfully");
        } catch (Exception e) {
            log.error("Set Model API error", e);
            result.setRequestStatus(500);
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
            boolean healthy = llmService.isHealthy();

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("healthy", healthy);
            data.put("currentModel", llmService.getCurrentModel());
            
            result.setData(data);
            result.setRequestStatus(healthy ? 200 : 503);
            result.setMessage(healthy ? "LLM service is healthy" : "LLM service is unhealthy");
        } catch (Exception e) {
            log.error("Health check error", e);
            result.setRequestStatus(500);
            result.setMessage("Health check failed: " + e.getMessage());
        }

        return result;
    }
}

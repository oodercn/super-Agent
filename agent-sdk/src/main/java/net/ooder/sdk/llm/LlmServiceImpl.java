package net.ooder.sdk.llm;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class LlmServiceImpl implements LlmService {
    private static final Logger log = LoggerFactory.getLogger(LlmServiceImpl.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private RestTemplate restTemplate;
    private LlmConfig config;
    private boolean connected = false;

    public LlmServiceImpl() {
    }

    @Override
    public void init(LlmConfig config) {
        this.config = config;
        this.restTemplate = new RestTemplate();
        this.connected = true;
        log.info("LLM Service initialized with endpoint: {}", config.getEndpoint());
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public CompletableFuture<LlmResponse> generateCode(LlmRequest request) {
        if (!connected) {
            CompletableFuture<LlmResponse> future = new CompletableFuture<>();
            future.completeExceptionally(new IllegalStateException("LLM Service not initialized"));
            return future;
        }

        return CompletableFuture.supplyAsync(() -> {
            int retryCount = 0;
            while (retryCount <= config.getMaxRetries()) {
                try {
                    // 构建请求头
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Content-Type", "application/json");
                    headers.set("Authorization", "Bearer " + config.getApiKey());
                    headers.set("X-Model-Name", config.getModelName());

                    // 构建请求实体
                    HttpEntity<LlmRequest> requestEntity = new HttpEntity<>(request, headers);
                    log.debug("Sending LLM request: {}", request);

                    // 发送请求
                    ResponseEntity<String> response = restTemplate.exchange(
                            config.getEndpoint(),
                            HttpMethod.POST,
                            requestEntity,
                            String.class
                    );

                    log.debug("Received LLM response: {}", response.getBody());

                    if (response.getStatusCodeValue() == 200) {
                        return objectMapper.readValue(response.getBody(), LlmResponse.class);
                    } else {
                        log.error("LLM Service returned error status: {}", response.getStatusCodeValue());
                        throw new RuntimeException("LLM Service error: " + response.getStatusCodeValue());
                    }
                } catch (Exception e) {
                    retryCount++;
                    if (retryCount > config.getMaxRetries()) {
                        log.error("Failed to send LLM request after {} retries: {}", config.getMaxRetries(), e.getMessage());
                        LlmResponse errorResponse = new LlmResponse();
                        errorResponse.setStatus("error");
                        errorResponse.setErrorMessage("Failed to generate code: " + e.getMessage());
                        return errorResponse;
                    }
                    log.warn("LLM request failed, retrying in {}ms (attempt {}): {}", 
                            config.getRetryDelay().toMillis(), retryCount, e.getMessage());
                    try {
                        Thread.sleep(config.getRetryDelay().toMillis());
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Retry interrupted", ie);
                    }
                }
            }
            return null;
        });
    }

    @Override
    public void close() {
        if (restTemplate != null) {
            restTemplate = null;
        }
        connected = false;
        log.info("LLM Service connection closed");
    }
}
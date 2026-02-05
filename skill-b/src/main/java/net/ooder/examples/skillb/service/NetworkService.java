package net.ooder.examples.skillb.service;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NetworkService {
    private static final Logger logger = LoggerFactory.getLogger(NetworkService.class);
    private final WebClient.Builder webClientBuilder;
    private final Map<String, Object> submissionHistory = new ConcurrentHashMap<>();

    @Value("${skill.b.target.url:http://localhost:8081/api/v1/submit}")
    private String targetUrl;

    @Value("${skill.b.use.mock.data:true}")
    private boolean useMockData;

    public NetworkService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Map<String, Object> submitData(Map<String, Object> data) {
        if (useMockData) {
            logger.info("Using mock data submission for Network B");
            Map<String, Object> mockResult = simulateDataSubmission(data);
            String submissionId = "SUBMIT_" + System.currentTimeMillis();
            submissionHistory.put(submissionId, mockResult);
            mockResult.put("submissionId", submissionId);
            return mockResult;
        } else {
            try {
                logger.info("Submitting data to target URL: {}", targetUrl);
                logger.info("Data to submit: {}", JSON.toJSONString(data));
                
                Map<String, Object> result = webClientBuilder.baseUrl(targetUrl)
                        .build()
                        .post()
                        .bodyValue(data)
                        .retrieve()
                        .bodyToMono(Map.class)
                        .block();
                
                if (result != null) {
                    String submissionId = result.getOrDefault("submissionId", "SUBMIT_" + System.currentTimeMillis()).toString();
                    submissionHistory.put(submissionId, result);
                    logger.info("Successfully submitted data: {}", JSON.toJSONString(result));
                    return result;
                } else {
                    logger.error("Received null response from target URL");
                    return generateFallbackResult(data);
                }
            } catch (Exception e) {
                logger.error("Failed to submit data to target URL: {}", e.getMessage());
                return generateFallbackResult(data);
            }
        }
    }

    public Mono<Map<String, Object>> submitDataAsync(Map<String, Object> data) {
        if (useMockData) {
            logger.info("Using asynchronous mock data submission for Network B");
            Map<String, Object> mockResult = simulateDataSubmission(data);
            String submissionId = "SUBMIT_" + System.currentTimeMillis();
            mockResult.put("submissionId", submissionId);
            submissionHistory.put(submissionId, mockResult);
            return Mono.just(mockResult);
        } else {
            logger.info("Asynchronously submitting data to target URL: {}", targetUrl);
            logger.info("Data to submit: {}", JSON.toJSONString(data));
            
            return webClientBuilder.baseUrl(targetUrl)
                    .build()
                    .post()
                    .bodyValue(data)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .map(map -> {
                        Map<String, Object> typedMap = new HashMap<>();
                        if (map != null) {
                            typedMap.putAll(map);
                        }
                        return typedMap;
                    })
                    .doOnSuccess(result -> {
                        if (result != null) {
                            String submissionId = result.getOrDefault("submissionId", "SUBMIT_" + System.currentTimeMillis()).toString();
                            submissionHistory.put(submissionId, result);
                            logger.info("Successfully submitted data asynchronously: {}", JSON.toJSONString(result));
                        }
                    })
                    .onErrorResume(error -> {
                        logger.error("Failed to submit data asynchronously: {}", error.getMessage());
                        return Mono.just(generateFallbackResult(data));
                    });
        }
    }

    public Map<String, Object> getSubmissionHistory() {
        return new HashMap<>(submissionHistory);
    }

    private Map<String, Object> simulateDataSubmission(Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "SUCCESS");
        result.put("message", "Data submitted successfully to Network B");
        result.put("timestamp", System.currentTimeMillis());
        result.put("processedData", data);
        result.put("network", "mock_b_network");
        result.put("submissionId", "SUBMIT_" + System.currentTimeMillis());
        result.put("receiptNumber", "RECEIPT_" + (int)(Math.random() * 1000000));
        return result;
    }

    private Map<String, Object> generateFallbackResult(Map<String, Object> originalData) {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "SUCCESS");
        result.put("message", "Fallback: Data submission simulated due to network error");
        result.put("timestamp", System.currentTimeMillis());
        result.put("processedData", originalData);
        result.put("network", "fallback_b_network");
        result.put("submissionId", "SUBMIT_" + System.currentTimeMillis());
        result.put("receiptNumber", "RECEIPT_" + (int)(Math.random() * 1000000));
        return result;
    }
}
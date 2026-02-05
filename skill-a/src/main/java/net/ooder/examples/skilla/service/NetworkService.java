package net.ooder.examples.skilla.service;

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
    private final Map<String, Object> cachedData = new ConcurrentHashMap<>();

    @Value("${skill.a.target.url:http://localhost:8080/api/v1/data}")
    private String targetUrl;

    @Value("${skill.a.use.mock.data:true}")
    private boolean useMockData;

    public NetworkService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Map<String, Object> retrieveInformation() {
        if (useMockData) {
            logger.info("Using mock data for information retrieval");
            Map<String, Object> mockData = generateMockData();
            cachedData.put("lastRetrieved", mockData);
            return mockData;
        } else {
            try {
                logger.info("Retrieving data from target URL: {}", targetUrl);
                Map<String, Object> data = webClientBuilder.baseUrl(targetUrl)
                        .build()
                        .get()
                        .retrieve()
                        .bodyToMono(Map.class)
                        .block();
                
                if (data != null) {
                    cachedData.put("lastRetrieved", data);
                    logger.info("Successfully retrieved data: {}", JSON.toJSONString(data));
                    return data;
                } else {
                    logger.error("Retrieved null data from target URL");
                    return generateFallbackData();
                }
            } catch (Exception e) {
                logger.error("Failed to retrieve data from target URL: {}", e.getMessage());
                return generateFallbackData();
            }
        }
    }

    public Mono<Map<String, Object>> retrieveInformationAsync() {
        if (useMockData) {
            logger.info("Using mock data for asynchronous information retrieval");
            Map<String, Object> mockData = generateMockData();
            cachedData.put("lastRetrieved", mockData);
            return Mono.just(mockData);
        } else {
            logger.info("Retrieving data asynchronously from target URL: {}", targetUrl);
            return webClientBuilder.baseUrl(targetUrl)
                    .build()
                    .get()
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .doOnSuccess(data -> {
                        if (data != null) {
                            cachedData.put("lastRetrieved", data);
                            logger.info("Successfully retrieved data asynchronously: {}", JSON.toJSONString(data));
                        }
                    })
                    .onErrorResume(error -> {
                        logger.error("Failed to retrieve data asynchronously: {}", error.getMessage());
                        return Mono.just(generateFallbackData());
                    });
        }
    }

    public Map<String, Object> getCachedData() {
        return new HashMap<>(cachedData);
    }

    private Map<String, Object> generateMockData() {
        Map<String, Object> mockData = new HashMap<>();
        mockData.put("id", "DATA_001");
        mockData.put("name", "张三");
        mockData.put("age", 30);
        mockData.put("address", "北京市朝阳区");
        mockData.put("phone", "13800138000");
        mockData.put("email", "zhangsan@example.com");
        mockData.put("department", "技术部");
        mockData.put("position", "工程师");
        mockData.put("salary", 15000.00);
        mockData.put("joinDate", "2020-01-01");
        mockData.put("status", "active");
        mockData.put("source", "mock_a_network");
        return mockData;
    }

    private Map<String, Object> generateFallbackData() {
        Map<String, Object> fallbackData = new HashMap<>();
        fallbackData.put("status", "ERROR");
        fallbackData.put("message", "Failed to retrieve data from network A");
        fallbackData.put("source", "fallback");
        return fallbackData;
    }
}
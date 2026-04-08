package net.ooder.skill.discovery.service;

import net.ooder.skill.discovery.dto.discovery.CapabilityDTO;
import net.ooder.skill.discovery.dto.discovery.RepositoryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

@Service
public class RestApiDiscoveryService {

    private static final Logger log = LoggerFactory.getLogger(RestApiDiscoveryService.class);

    @Value("${ooder.discovery.rest-api.enabled:true}")
    private boolean enabled;

    @Value("${ooder.discovery.rest-api.timeout:30}")
    private int defaultTimeoutSeconds;

    @Value("${ooder.discovery.rest-api.max-retries:3}")
    private int maxRetries;

    @Value("${ooder.discovery.rest-api.retry-delay:1000}")
    private int retryDelayMs;

    private final ExecutorService executor = Executors.newCachedThreadPool();

    public List<CapabilityDTO> discover(String apiUrl, String token, int timeoutSeconds) {
        if (!enabled) {
            log.info("[discover] REST API discovery is disabled");
            return Collections.emptyList();
        }

        if (apiUrl == null || apiUrl.isEmpty()) {
            log.warn("[discover] API URL is required");
            return Collections.emptyList();
        }

        int actualTimeout = timeoutSeconds > 0 ? timeoutSeconds : defaultTimeoutSeconds;

        log.info("[discover] Starting REST API discovery from: {}, timeout: {}s", apiUrl, actualTimeout);

        List<CapabilityDTO> capabilities = new ArrayList<>();

        try {
            String endpoint = normalizeApiUrl(apiUrl);
            String response = fetchCapabilities(endpoint, token, actualTimeout);

            if (response != null) {
                capabilities.addAll(parseCapabilities(response, apiUrl));
            }

            log.info("[discover] Discovered {} capabilities from REST API: {}", capabilities.size(), apiUrl);

        } catch (Exception e) {
            log.error("[discover] REST API discovery failed for {}: {}", apiUrl, e.getMessage(), e);
        }

        return capabilities;
    }

    private String normalizeApiUrl(String apiUrl) {
        if (!apiUrl.startsWith("http://") && !apiUrl.startsWith("https://")) {
            apiUrl = "http://" + apiUrl;
        }

        if (!apiUrl.contains("/api/") && !apiUrl.endsWith("/capabilities") && !apiUrl.endsWith("/skills")) {
            if (apiUrl.endsWith("/")) {
                apiUrl = apiUrl.substring(0, apiUrl.length() - 1);
            }
            apiUrl = apiUrl + "/api/v1/capabilities";
        }

        return apiUrl;
    }

    private String fetchCapabilities(String apiUrl, String token, int timeoutSeconds) {
        int retries = 0;
        Exception lastException = null;

        while (retries < maxRetries) {
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Content-Type", "application/json");

                if (token != null && !token.isEmpty()) {
                    connection.setRequestProperty("Authorization", "Bearer " + token);
                }

                connection.setConnectTimeout(timeoutSeconds * 1000);
                connection.setReadTimeout(timeoutSeconds * 1000);

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    return response.toString();
                } else {
                    log.warn("[fetchCapabilities] Unexpected response code {} from {}", responseCode, apiUrl);
                }

            } catch (Exception e) {
                lastException = e;
                log.debug("[fetchCapabilities] Attempt {} failed: {}", retries + 1, e.getMessage());
            }

            retries++;
            if (retries < maxRetries) {
                try {
                    Thread.sleep(retryDelayMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        if (lastException != null) {
            log.error("[fetchCapabilities] All {} attempts failed for {}: {}", maxRetries, apiUrl, lastException.getMessage());
        }

        return null;
    }

    private List<CapabilityDTO> parseCapabilities(String response, String sourceUrl) {
        List<CapabilityDTO> capabilities = new ArrayList<>();
        
        try {
            CapabilityDTO cap = new CapabilityDTO();
            cap.setId("rest-" + UUID.randomUUID().toString().substring(0, 8));
            cap.setSource("rest-api");
            cap.setInstallSource(sourceUrl);
            cap.setStatus("available");
            cap.setName("Remote Skill from " + sourceUrl);
            capabilities.add(cap);
        } catch (Exception e) {
            log.error("[parseCapabilities] Failed to parse capabilities: {}", e.getMessage());
        }

        return capabilities;
    }

    public boolean testConnection(String apiUrl, String token, int timeoutSeconds) {
        try {
            String endpoint = normalizeApiUrl(apiUrl);
            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            if (token != null && !token.isEmpty()) {
                connection.setRequestProperty("Authorization", "Bearer " + token);
            }

            connection.setConnectTimeout(timeoutSeconds * 1000);
            connection.setReadTimeout(timeoutSeconds * 1000);

            int responseCode = connection.getResponseCode();
            return responseCode >= 200 && responseCode < 300;

        } catch (Exception e) {
            log.error("[testConnection] Connection test failed for {}: {}", apiUrl, e.getMessage());
            return false;
        }
    }

    public void shutdown() {
        executor.shutdownNow();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                log.warn("[shutdown] REST API discovery service did not shut down gracefully");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

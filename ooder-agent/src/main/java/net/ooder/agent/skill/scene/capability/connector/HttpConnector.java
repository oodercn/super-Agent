package net.ooder.mvp.skill.scene.capability.connector;

import net.ooder.mvp.skill.scene.capability.model.CapabilityBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpConnector implements Connector {

    private static final Logger log = LoggerFactory.getLogger(HttpConnector.class);

    @Override
    public String getType() {
        return "HTTP";
    }

    @Override
    public Object invoke(CapabilityBinding binding, Map<String, Object> params) throws Exception {
        String endpoint = binding.getEndpoint();
        if (endpoint == null || endpoint.isEmpty()) {
            throw new RuntimeException("No endpoint configured");
        }

        int timeout = binding.getTimeout() > 0 ? binding.getTimeout() : 30000;
        String method = binding.getMethod() != null ? binding.getMethod() : "POST";

        log.debug("[HttpConnector] {} {} with timeout {}ms", method, endpoint, timeout);

        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setConnectTimeout(timeout);
        conn.setReadTimeout(timeout);
        conn.setDoOutput(true);

        if (params != null && !params.isEmpty()) {
            String jsonBody = toJson(params);
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
        }

        int responseCode = conn.getResponseCode();
        
        if (responseCode >= 200 && responseCode < 300) {
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }
            
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("status", responseCode);
            result.put("body", response.toString());
            result.put("success", true);
            return result;
        } else {
            throw new RuntimeException("HTTP request failed with status: " + responseCode);
        }
    }

    @Override
    public boolean isAvailable(CapabilityBinding binding) {
        String endpoint = binding.getEndpoint();
        if (endpoint == null || endpoint.isEmpty()) {
            return false;
        }
        
        try {
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");
            conn.setConnectTimeout(5000);
            int responseCode = conn.getResponseCode();
            return responseCode < 500;
        } catch (Exception e) {
            log.debug("[HttpConnector] Availability check failed: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void close() {
    }

    private String toJson(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) sb.append(",");
            first = false;
            sb.append("\"").append(entry.getKey()).append("\":");
            Object value = entry.getValue();
            if (value == null) {
                sb.append("null");
            } else if (value instanceof String) {
                sb.append("\"").append(escapeJson((String) value)).append("\"");
            } else if (value instanceof Number || value instanceof Boolean) {
                sb.append(value);
            } else if (value instanceof Map) {
                sb.append(toJson((Map<String, Object>) value));
            } else {
                sb.append("\"").append(escapeJson(value.toString())).append("\"");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    private String escapeJson(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}

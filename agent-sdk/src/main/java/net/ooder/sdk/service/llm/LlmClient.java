
package net.ooder.sdk.service.llm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LlmClient {
    
    private static final Logger log = LoggerFactory.getLogger(LlmClient.class);
    
    private final LlmConfig config;
    
    public LlmClient(LlmConfig config) {
        this.config = config;
    }
    
    public String chat(String prompt) throws IOException {
        return chat(prompt, null);
    }
    
    public String chat(String prompt, String systemPrompt) throws IOException {
        String endpoint = config.getEndpoint();
        if (!endpoint.endsWith("/")) {
            endpoint += "/";
        }
        endpoint += "chat/completions";
        
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + config.getApiKey());
        conn.setDoOutput(true);
        conn.setConnectTimeout((int) config.getTimeout());
        conn.setReadTimeout((int) config.getTimeout());
        
        String requestBody = buildRequestBody(prompt, systemPrompt);
        
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        
        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            String errorMessage = readErrorResponse(conn);
            throw new IOException("LLM API error: " + responseCode + " - " + errorMessage);
        }
        
        String response = readResponse(conn);
        conn.disconnect();
        
        return extractContent(response);
    }
    
    private String buildRequestBody(String prompt, String systemPrompt) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"model\":\"").append(config.getModel()).append("\",");
        sb.append("\"messages\":[");
        
        if (systemPrompt != null && !systemPrompt.isEmpty()) {
            sb.append("{\"role\":\"system\",\"content\":");
            sb.append(escapeJson(systemPrompt));
            sb.append("},");
        }
        
        sb.append("{\"role\":\"user\",\"content\":");
        sb.append(escapeJson(prompt));
        sb.append("}],");
        sb.append("\"max_tokens\":").append(config.getMaxTokens()).append(",");
        sb.append("\"temperature\":").append(config.getTemperature());
        sb.append("}");
        
        return sb.toString();
    }
    
    private String escapeJson(String value) {
        return "\"" + value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t")
            + "\"";
    }
    
    private String readResponse(HttpURLConnection conn) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }
    
    private String readErrorResponse(HttpURLConnection conn) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } catch (Exception e) {
            return "Unable to read error response";
        }
    }
    
    private String extractContent(String response) {
        int contentStart = response.indexOf("\"content\":\"");
        if (contentStart == -1) {
            return response;
        }
        
        contentStart += 11;
        int contentEnd = response.indexOf("\"", contentStart);
        if (contentEnd == -1) {
            return response;
        }
        
        String content = response.substring(contentStart, contentEnd);
        return content
            .replace("\\n", "\n")
            .replace("\\r", "\r")
            .replace("\\t", "\t")
            .replace("\\\"", "\"")
            .replace("\\\\", "\\");
    }
}

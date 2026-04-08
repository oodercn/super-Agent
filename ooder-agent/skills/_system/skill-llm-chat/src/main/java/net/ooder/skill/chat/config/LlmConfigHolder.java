package net.ooder.skill.chat.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LlmConfigHolder {
    
    private static final Map<String, String> config = new ConcurrentHashMap<>();
    
    public static void setApiKey(String providerId, String apiKey) {
        config.put(providerId + ".apiKey", apiKey);
    }
    
    public static String getApiKey(String providerId) {
        return config.get(providerId + ".apiKey");
    }
    
    public static void setBaseUrl(String providerId, String baseUrl) {
        config.put(providerId + ".baseUrl", baseUrl);
    }
    
    public static String getBaseUrl(String providerId) {
        return config.getOrDefault(providerId + ".baseUrl", "https://api.deepseek.com/v1");
    }
    
    public static void setModel(String providerId, String model) {
        config.put(providerId + ".model", model);
    }
    
    public static String getModel(String providerId) {
        return config.getOrDefault(providerId + ".model", "deepseek-chat");
    }
    
    public static boolean isConfigured(String providerId) {
        return config.containsKey(providerId + ".apiKey");
    }
}

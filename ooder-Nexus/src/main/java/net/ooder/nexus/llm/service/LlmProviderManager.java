package net.ooder.nexus.llm.service;

import net.ooder.scene.skill.LlmProvider;

import java.util.Map;

public interface LlmProviderManager {
    
    LlmProvider getProvider(String providerType);
    
    LlmProvider getDefaultProvider();
    
    String getDefaultModel();
    
    Map<String, Object> chat(String providerType, String model, 
            String systemPrompt, java.util.List<Map<String, String>> messages, 
            Map<String, Object> options);
    
    boolean testConnection(String providerType, String apiKey);
}

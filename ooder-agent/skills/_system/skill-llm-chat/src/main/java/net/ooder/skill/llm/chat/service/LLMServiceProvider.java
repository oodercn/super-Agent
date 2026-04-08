package net.ooder.skill.llm.chat.service;

import java.util.List;
import java.util.Map;

public interface LLMServiceProvider {
    
    Map<String, Object> checkDependencies();
    
    boolean isReady();
    
    Map<String, Object> chat(String prompt, String systemPrompt);
    
    Map<String, Object> chat(String prompt, String systemPrompt, String provider, String model, String sceneId);
    
    String generateAnswer(String question, List<Map<String, Object>> sources, String model);
    
    String generateAnswer(String question, List<Map<String, Object>> sources);
    
    String chat(String prompt, List<Map<String, Object>> history);
}

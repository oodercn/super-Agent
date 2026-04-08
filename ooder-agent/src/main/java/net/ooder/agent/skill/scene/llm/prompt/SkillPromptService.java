package net.ooder.mvp.skill.scene.llm.prompt;

import java.util.Map;

public interface SkillPromptService {

    String getSystemPrompt(String skillId);
    
    String getSystemPrompt(String skillId, String sceneId);
    
    String getRolePrompt(String skillId, String roleId);
    
    String getDefaultSystemPrompt();
    
    Map<String, Object> getPromptConfig(String skillId);
    
    String resolveVariables(String prompt, Map<String, Object> variables);
}

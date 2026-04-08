package net.ooder.skill.llm;

import java.util.List;

public interface LlmService {
    
    String getSkillId();
    
    List<LlmProviderDTO> getProviders();
    
    List<LlmModelDTO> getModels();
    
    LlmModelDTO getModel(String modelId);
    
    LlmConfigDTO getConfig();
    
    void updateConfig(LlmConfigDTO config);
    
    String getDefaultProvider();
    
    String getDefaultModel();
}

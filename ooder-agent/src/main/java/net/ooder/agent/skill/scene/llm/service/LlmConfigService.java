package net.ooder.mvp.skill.scene.llm.service;

import net.ooder.mvp.skill.scene.dto.PageResult;
import net.ooder.mvp.skill.scene.dto.llm.LlmConfigAuditDTO;
import net.ooder.mvp.skill.scene.dto.llm.LlmConfigDTO;
import net.ooder.mvp.skill.scene.dto.llm.LlmConfigTemplateDTO;
import net.ooder.mvp.skill.scene.dto.llm.LlmProviderMeta;
import net.ooder.mvp.skill.scene.dto.llm.LlmUsageStatsDTO;

import java.util.List;
import java.util.Map;

public interface LlmConfigService {
    
    PageResult<LlmConfigDTO> listConfigs(int pageNum, int pageSize, String level, String providerType);
    
    List<LlmConfigDTO> listAllConfigs();
    
    LlmConfigDTO getConfig(Long id);
    
    LlmConfigDTO getConfigByScope(String level, String scopeId);
    
    LlmConfigDTO getEffectiveConfig(String level, String scopeId, String userId, String sceneId, String agentId);
    
    LlmConfigDTO createConfig(LlmConfigDTO config, String operator);
    
    LlmConfigDTO updateConfig(Long id, LlmConfigDTO config, String operator);
    
    boolean deleteConfig(Long id, String operator);
    
    boolean enableConfig(Long id, String operator);
    
    boolean disableConfig(Long id, String operator);
    
    boolean validateConfig(LlmConfigDTO config);
    
    ValidationResult testConnection(Long id);
    
    ValidationResult testConfig(LlmConfigDTO config);
    
    List<LlmProviderMeta> getAvailableProviders();
    
    LlmProviderMeta getProviderMeta(String providerType);
    
    List<LlmConfigTemplateDTO> getConfigTemplates();
    
    LlmConfigTemplateDTO getConfigTemplate(Long id);
    
    LlmConfigTemplateDTO createConfigTemplate(LlmConfigTemplateDTO template);
    
    LlmConfigTemplateDTO updateConfigTemplate(Long id, LlmConfigTemplateDTO template);
    
    boolean deleteConfigTemplate(Long id);
    
    LlmConfigDTO createConfigFromTemplate(Long templateId, String level, String scopeId, String operator);
    
    LlmUsageStatsDTO getUsageStats(String configId, long startTime, long endTime);
    
    List<LlmUsageStatsDTO> getAllUsageStats(long startTime, long endTime);
    
    void recordUsage(String configId, long inputTokens, long outputTokens, double cost, long latency);
    
    Map<String, Object> getConfigInheritanceChain(String level, String scopeId);
    
    List<LlmConfigAuditDTO> getConfigAuditLogs(String configId, int pageNum, int pageSize);
    
    List<LlmConfigAuditDTO> getAllAuditLogs(int pageNum, int pageSize);
    
    Map<String, Object> getConfigComparison(Long configId1, Long configId2);
    
    boolean checkApiKeyExpiring(String configId, int daysThreshold);
    
    List<LlmConfigDTO> getConfigsNeedingKeyRotation(int daysThreshold);
    
    static class ValidationResult {
        private boolean valid;
        private String message;
        private Map<String, String> errors;
        
        public static ValidationResult success() {
            ValidationResult r = new ValidationResult();
            r.valid = true;
            return r;
        }
        
        public static ValidationResult failure(String message) {
            ValidationResult r = new ValidationResult();
            r.valid = false;
            r.message = message;
            return r;
        }
        
        public static ValidationResult failure(Map<String, String> errors) {
            ValidationResult r = new ValidationResult();
            r.valid = false;
            r.errors = errors;
            return r;
        }
        
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public Map<String, String> getErrors() { return errors; }
        public void setErrors(Map<String, String> errors) { this.errors = errors; }
    }
}

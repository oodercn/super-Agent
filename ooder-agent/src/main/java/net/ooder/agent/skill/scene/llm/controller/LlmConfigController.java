package net.ooder.mvp.skill.scene.llm.controller;

import net.ooder.mvp.skill.scene.dto.PageResult;
import net.ooder.mvp.skill.scene.dto.llm.LlmConfigAuditDTO;
import net.ooder.mvp.skill.scene.dto.llm.LlmConfigDTO;
import net.ooder.mvp.skill.scene.dto.llm.LlmConfigTemplateDTO;
import net.ooder.mvp.skill.scene.dto.llm.LlmProviderMeta;
import net.ooder.mvp.skill.scene.dto.llm.LlmUsageStatsDTO;
import net.ooder.mvp.skill.scene.llm.service.LlmConfigService;
import net.ooder.mvp.skill.scene.llm.service.LlmConfigService.ValidationResult;
import net.ooder.mvp.skill.scene.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/llm-config")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LlmConfigController {

    private static final Logger log = LoggerFactory.getLogger(LlmConfigController.class);
    
    @Value("${ooder.llm.provider:qianwen}")
    private String defaultProvider;
    
    @Value("${ooder.llm.model:qwen-plus}")
    private String defaultModel;
    
    @Autowired
    private LlmConfigService llmConfigService;

    @GetMapping
    public ResultModel<PageResult<LlmConfigDTO>> listConfigs(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String providerType) {
        log.info("[LlmConfigController] List configs called - pageNum: {}, pageSize: {}, level: {}, providerType: {}", 
                 pageNum, pageSize, level, providerType);
        PageResult<LlmConfigDTO> result = llmConfigService.listConfigs(pageNum, pageSize, level, providerType);
        return ResultModel.success(result);
    }

    @GetMapping("/all")
    public ResultModel<List<LlmConfigDTO>> listAllConfigs() {
        log.info("[LlmConfigController] List all configs called");
        List<LlmConfigDTO> configs = llmConfigService.listAllConfigs();
        return ResultModel.success(configs);
    }

    @GetMapping("/{id}")
    public ResultModel<LlmConfigDTO> getConfig(@PathVariable Long id) {
        log.info("[LlmConfigController] Get config called for id: {}", id);
        LlmConfigDTO config = llmConfigService.getConfig(id);
        if (config != null) {
            return ResultModel.success(config);
        }
        return ResultModel.notFound("Config not found");
    }

    @GetMapping("/scope")
    public ResultModel<LlmConfigDTO> getConfigByScope(
            @RequestParam String level,
            @RequestParam String scopeId) {
        log.info("[LlmConfigController] Get config by scope - level: {}, scopeId: {}", level, scopeId);
        LlmConfigDTO config = llmConfigService.getConfigByScope(level, scopeId);
        if (config != null) {
            return ResultModel.success(config);
        }
        return ResultModel.notFound("Config not found for scope");
    }

    @GetMapping("/effective")
    public ResultModel<LlmConfigDTO> getEffectiveConfig(
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String scopeId,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String sceneId,
            @RequestParam(required = false) String agentId) {
        log.info("[LlmConfigController] Get effective config - level: {}, scopeId: {}, userId: {}, sceneId: {}, agentId: {}", 
                 level, scopeId, userId, sceneId, agentId);
        LlmConfigDTO config = llmConfigService.getEffectiveConfig(level, scopeId, userId, sceneId, agentId);
        if (config != null) {
            return ResultModel.success(config);
        }
        return ResultModel.notFound("No effective config found");
    }

    @PostMapping
    public ResultModel<LlmConfigDTO> createConfig(
            @RequestBody LlmConfigDTO config,
            @RequestHeader(value = "X-User-Id", defaultValue = "system") String operator) {
        log.info("[LlmConfigController] Create config called: {}", config.getName());
        
        ValidationResult validation = llmConfigService.testConfig(config);
        if (!validation.isValid()) {
            return ResultModel.error("Config validation failed: " + validation.getMessage());
        }
        
        LlmConfigDTO created = llmConfigService.createConfig(config, operator);
        return ResultModel.success(created);
    }

    @PutMapping("/{id}")
    public ResultModel<LlmConfigDTO> updateConfig(
            @PathVariable Long id,
            @RequestBody LlmConfigDTO config,
            @RequestHeader(value = "X-User-Id", defaultValue = "system") String operator) {
        log.info("[LlmConfigController] Update config called for id: {}", id);
        
        LlmConfigDTO updated = llmConfigService.updateConfig(id, config, operator);
        if (updated != null) {
            return ResultModel.success(updated);
        }
        return ResultModel.notFound("Config not found");
    }

    @DeleteMapping("/{id}")
    public ResultModel<Boolean> deleteConfig(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id", defaultValue = "system") String operator) {
        log.info("[LlmConfigController] Delete config called for id: {}", id);
        
        boolean success = llmConfigService.deleteConfig(id, operator);
        if (success) {
            return ResultModel.success(true);
        }
        return ResultModel.error("Failed to delete config");
    }

    @PostMapping("/{id}/enable")
    public ResultModel<Boolean> enableConfig(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id", defaultValue = "system") String operator) {
        log.info("[LlmConfigController] Enable config called for id: {}", id);
        
        boolean success = llmConfigService.enableConfig(id, operator);
        if (success) {
            return ResultModel.success(true);
        }
        return ResultModel.error("Failed to enable config");
    }

    @PostMapping("/{id}/disable")
    public ResultModel<Boolean> disableConfig(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id", defaultValue = "system") String operator) {
        log.info("[LlmConfigController] Disable config called for id: {}", id);
        
        boolean success = llmConfigService.disableConfig(id, operator);
        if (success) {
            return ResultModel.success(true);
        }
        return ResultModel.error("Failed to disable config");
    }

    @PostMapping("/validate")
    public ResultModel<Map<String, Object>> validateConfig(@RequestBody LlmConfigDTO config) {
        log.info("[LlmConfigController] Validate config called");
        
        boolean valid = llmConfigService.validateConfig(config);
        Map<String, Object> result = new HashMap<>();
        result.put("valid", valid);
        
        if (!valid) {
            ValidationResult vr = llmConfigService.testConfig(config);
            result.put("message", vr.getMessage());
            result.put("errors", vr.getErrors());
        }
        
        return ResultModel.success(result);
    }

    @PostMapping("/{id}/test")
    public ResultModel<Map<String, Object>> testConnection(@PathVariable Long id) {
        log.info("[LlmConfigController] Test connection called for id: {}", id);
        
        ValidationResult result = llmConfigService.testConnection(id);
        Map<String, Object> response = new HashMap<>();
        response.put("success", result.isValid());
        response.put("message", result.isValid() ? "Connection successful" : result.getMessage());
        
        return ResultModel.success(response);
    }

    @PostMapping("/test")
    public ResultModel<Map<String, Object>> testConfig(@RequestBody LlmConfigDTO config) {
        log.info("[LlmConfigController] Test config called");
        
        ValidationResult result = llmConfigService.testConfig(config);
        Map<String, Object> response = new HashMap<>();
        response.put("success", result.isValid());
        response.put("message", result.isValid() ? "Connection successful" : result.getMessage());
        
        return ResultModel.success(response);
    }

    @GetMapping("/providers")
    public ResultModel<List<LlmProviderMeta>> getAvailableProviders() {
        log.info("[LlmConfigController] Get available providers called");
        List<LlmProviderMeta> providers = llmConfigService.getAvailableProviders();
        return ResultModel.success(providers);
    }

    @GetMapping("/providers/{providerType}")
    public ResultModel<LlmProviderMeta> getProviderMeta(@PathVariable String providerType) {
        log.info("[LlmConfigController] Get provider meta called for: {}", providerType);
        LlmProviderMeta meta = llmConfigService.getProviderMeta(providerType);
        if (meta != null) {
            return ResultModel.success(meta);
        }
        return ResultModel.notFound("Provider not found");
    }

    @GetMapping("/templates")
    public ResultModel<List<LlmConfigTemplateDTO>> getConfigTemplates() {
        log.info("[LlmConfigController] Get config templates called");
        List<LlmConfigTemplateDTO> templates = llmConfigService.getConfigTemplates();
        return ResultModel.success(templates);
    }

    @GetMapping("/templates/{id}")
    public ResultModel<LlmConfigTemplateDTO> getConfigTemplate(@PathVariable Long id) {
        log.info("[LlmConfigController] Get config template called for id: {}", id);
        LlmConfigTemplateDTO template = llmConfigService.getConfigTemplate(id);
        if (template != null) {
            return ResultModel.success(template);
        }
        return ResultModel.notFound("Template not found");
    }

    @PostMapping("/templates")
    public ResultModel<LlmConfigTemplateDTO> createConfigTemplate(@RequestBody LlmConfigTemplateDTO template) {
        log.info("[LlmConfigController] Create config template called: {}", template.getName());
        LlmConfigTemplateDTO created = llmConfigService.createConfigTemplate(template);
        return ResultModel.success(created);
    }

    @PutMapping("/templates/{id}")
    public ResultModel<LlmConfigTemplateDTO> updateConfigTemplate(
            @PathVariable Long id,
            @RequestBody LlmConfigTemplateDTO template) {
        log.info("[LlmConfigController] Update config template called for id: {}", id);
        LlmConfigTemplateDTO updated = llmConfigService.updateConfigTemplate(id, template);
        if (updated != null) {
            return ResultModel.success(updated);
        }
        return ResultModel.error("Failed to update template");
    }

    @DeleteMapping("/templates/{id}")
    public ResultModel<Boolean> deleteConfigTemplate(@PathVariable Long id) {
        log.info("[LlmConfigController] Delete config template called for id: {}", id);
        boolean success = llmConfigService.deleteConfigTemplate(id);
        if (success) {
            return ResultModel.success(true);
        }
        return ResultModel.error("Failed to delete template");
    }

    @PostMapping("/templates/{templateId}/create-config")
    public ResultModel<LlmConfigDTO> createConfigFromTemplate(
            @PathVariable Long templateId,
            @RequestParam String level,
            @RequestParam String scopeId,
            @RequestHeader(value = "X-User-Id", defaultValue = "system") String operator) {
        log.info("[LlmConfigController] Create config from template - templateId: {}, level: {}, scopeId: {}", 
                 templateId, level, scopeId);
        LlmConfigDTO config = llmConfigService.createConfigFromTemplate(templateId, level, scopeId, operator);
        if (config != null) {
            return ResultModel.success(config);
        }
        return ResultModel.error("Failed to create config from template");
    }

    @GetMapping("/{id}/usage-stats")
    public ResultModel<LlmUsageStatsDTO> getUsageStats(
            @PathVariable String id,
            @RequestParam(defaultValue = "0") long startTime,
            @RequestParam(defaultValue = "0") long endTime) {
        log.info("[LlmConfigController] Get usage stats called for config: {}", id);
        
        if (startTime == 0) {
            startTime = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000L;
        }
        if (endTime == 0) {
            endTime = System.currentTimeMillis();
        }
        
        LlmUsageStatsDTO stats = llmConfigService.getUsageStats(id, startTime, endTime);
        return ResultModel.success(stats);
    }

    @GetMapping("/usage-stats")
    public ResultModel<List<LlmUsageStatsDTO>> getAllUsageStats(
            @RequestParam(defaultValue = "0") long startTime,
            @RequestParam(defaultValue = "0") long endTime) {
        log.info("[LlmConfigController] Get all usage stats called");
        
        if (startTime == 0) {
            startTime = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000L;
        }
        if (endTime == 0) {
            endTime = System.currentTimeMillis();
        }
        
        List<LlmUsageStatsDTO> stats = llmConfigService.getAllUsageStats(startTime, endTime);
        return ResultModel.success(stats);
    }

    @PostMapping("/{id}/record-usage")
    public ResultModel<Boolean> recordUsage(
            @PathVariable String id,
            @RequestParam long inputTokens,
            @RequestParam long outputTokens,
            @RequestParam double cost,
            @RequestParam long latency) {
        log.info("[LlmConfigController] Record usage called for config: {}", id);
        llmConfigService.recordUsage(id, inputTokens, outputTokens, cost, latency);
        return ResultModel.success(true);
    }

    @GetMapping("/inheritance-chain")
    public ResultModel<Map<String, Object>> getConfigInheritanceChain(
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String scopeId) {
        log.info("[LlmConfigController] Get inheritance chain called - level: {}, scopeId: {}", level, scopeId);
        Map<String, Object> chain = llmConfigService.getConfigInheritanceChain(level, scopeId);
        return ResultModel.success(chain);
    }

    @GetMapping("/audit-logs")
    public ResultModel<List<LlmConfigAuditDTO>> getAllAuditLogs(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {
        log.info("[LlmConfigController] Get all audit logs called");
        List<LlmConfigAuditDTO> logs = llmConfigService.getAllAuditLogs(pageNum, pageSize);
        return ResultModel.success(logs);
    }

    @GetMapping("/{id}/audit-logs")
    public ResultModel<List<LlmConfigAuditDTO>> getConfigAuditLogs(
            @PathVariable String id,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {
        log.info("[LlmConfigController] Get config audit logs called for: {}", id);
        List<LlmConfigAuditDTO> logs = llmConfigService.getConfigAuditLogs(id, pageNum, pageSize);
        return ResultModel.success(logs);
    }

    @GetMapping("/compare")
    public ResultModel<Map<String, Object>> compareConfigs(
            @RequestParam Long configId1,
            @RequestParam Long configId2) {
        log.info("[LlmConfigController] Compare configs called - configId1: {}, configId2: {}", configId1, configId2);
        Map<String, Object> comparison = llmConfigService.getConfigComparison(configId1, configId2);
        return ResultModel.success(comparison);
    }

    @GetMapping("/{id}/key-expiring")
    public ResultModel<Map<String, Object>> checkApiKeyExpiring(
            @PathVariable String id,
            @RequestParam(defaultValue = "30") int daysThreshold) {
        log.info("[LlmConfigController] Check API key expiring for config: {}", id);
        boolean expiring = llmConfigService.checkApiKeyExpiring(id, daysThreshold);
        Map<String, Object> result = new HashMap<>();
        result.put("configId", id);
        result.put("expiring", expiring);
        result.put("daysThreshold", daysThreshold);
        return ResultModel.success(result);
    }

    @GetMapping("/key-rotation-needed")
    public ResultModel<List<LlmConfigDTO>> getConfigsNeedingKeyRotation(
            @RequestParam(defaultValue = "30") int daysThreshold) {
        log.info("[LlmConfigController] Get configs needing key rotation called");
        List<LlmConfigDTO> configs = llmConfigService.getConfigsNeedingKeyRotation(daysThreshold);
        return ResultModel.success(configs);
    }
}

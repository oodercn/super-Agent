package net.ooder.mvp.skill.scene.capability.controller;

import net.ooder.mvp.skill.scene.capability.dto.SceneTypeUpdateRequest;
import net.ooder.mvp.skill.scene.capability.dto.StatusUpdateRequest;
import net.ooder.mvp.skill.scene.capability.model.*;
import net.ooder.mvp.skill.scene.capability.service.CapabilityService;
import net.ooder.mvp.skill.scene.capability.service.CapabilityBindingService;
import net.ooder.mvp.skill.scene.capability.service.SkillCapabilitySyncService;
import net.ooder.mvp.skill.scene.capability.service.SceneSkillCategoryDetector;
import net.ooder.mvp.skill.scene.capability.service.BusinessSemanticsScorer.BusinessSemanticsScore;
import net.ooder.mvp.skill.scene.dto.capability.SceneSkillClassificationResultDTO;
import net.ooder.mvp.skill.scene.dto.PageResult;
import net.ooder.mvp.skill.scene.integration.SceneEngineIntegration;
import net.ooder.mvp.skill.scene.model.ResultModel;
import net.ooder.mvp.skill.scene.capability.model.SceneType;
import net.ooder.mvp.skill.scene.capability.model.SkillForm;
import net.ooder.mvp.skill.scene.capability.model.CapabilityCategory;
import net.ooder.mvp.skill.scene.service.CapabilityStatsService;
import net.ooder.mvp.skill.scene.dto.stats.LogEntryDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController("sceneCapabilityController")
@RequestMapping("/api/v1/scene/capabilities")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CapabilityController {

    private static final Logger log = LoggerFactory.getLogger(CapabilityController.class);

    @Autowired
    private CapabilityService capabilityService;

    @Autowired
    private CapabilityBindingService bindingService;
    
    @Autowired(required = false)
    private SceneEngineIntegration sceneEngineIntegration;
    
    @Autowired(required = false)
    private SkillCapabilitySyncService syncService;
    
    @Autowired
    private SceneSkillCategoryDetector categoryDetector;
    
    @Autowired
    private CapabilityStatsService statsService;

    @GetMapping
    public ResultModel<List<Capability>> listCapabilities(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String sceneType,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String ownership,
            @RequestParam(required = false) String skillForm,
            @RequestParam(required = false) String skillCategory) {
        
        log.info("List capabilities - type: {}, sceneType: {}, keyword: {}, ownership: {}, skillForm: {}, skillCategory: {}", 
            type, sceneType, keyword, ownership, skillForm, skillCategory);
        
        SkillForm form = skillForm != null && !skillForm.isEmpty() ? SkillForm.fromCode(skillForm) : null;
        SceneType sceneTypeEnum = sceneType != null && !sceneType.isEmpty() ? SceneType.fromCode(sceneType) : null;
        CapabilityCategory category = skillCategory != null && !skillCategory.isEmpty() ? CapabilityCategory.fromCode(skillCategory) : null;
        CapabilityOwnership ownershipEnum = ownership != null && !ownership.isEmpty() ? CapabilityOwnership.fromCode(ownership) : null;
        
        List<Capability> capabilities = capabilityService.findByFilters(form, sceneTypeEnum, category, ownershipEnum, keyword);
        
        return ResultModel.success(capabilities);
    }
    
    @GetMapping("/search")
    public ResultModel<List<Capability>> searchCapabilities(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String skillForm,
            @RequestParam(required = false) String sceneType,
            @RequestParam(required = false) String skillCategory,
            @RequestParam(required = false) String ownership) {
        
        log.info("Search capabilities - keyword: {}, skillForm: {}, sceneType: {}, skillCategory: {}, ownership: {}",
            keyword, skillForm, sceneType, skillCategory, ownership);
        
        SkillForm form = skillForm != null && !skillForm.isEmpty() ? SkillForm.fromCode(skillForm) : null;
        SceneType sceneTypeEnum = sceneType != null && !sceneType.isEmpty() ? SceneType.fromCode(sceneType) : null;
        CapabilityCategory category = skillCategory != null && !skillCategory.isEmpty() ? CapabilityCategory.fromCode(skillCategory) : null;
        CapabilityOwnership ownershipEnum = ownership != null && !ownership.isEmpty() ? CapabilityOwnership.fromCode(ownership) : null;
        
        List<Capability> capabilities = capabilityService.findByFilters(form, sceneTypeEnum, category, ownershipEnum, keyword);
        
        return ResultModel.success(capabilities);
    }

    @GetMapping("/types")
    public ResultModel<List<Map<String, Object>>> listCapabilityTypes() {
        List<Map<String, Object>> types = new ArrayList<Map<String, Object>>();
        
        for (CapabilityType type : CapabilityType.values()) {
            Map<String, Object> typeInfo = new HashMap<String, Object>();
            typeInfo.put("id", type.name());
            typeInfo.put("name", type.getName());
            typeInfo.put("description", type.getDescription());
            types.add(typeInfo);
        }
        
        return ResultModel.success(types);
    }

    @GetMapping("/{capabilityId}")
    public ResultModel<Capability> getCapability(@PathVariable String capabilityId) {
        Capability capability = capabilityService.findById(capabilityId);
        if (capability == null) {
            return ResultModel.notFound("Capability not found: " + capabilityId);
        }
        return ResultModel.success(capability);
    }

    @PostMapping
    public ResultModel<Capability> createCapability(@RequestBody Capability capability) {
        log.info("Create capability: {}", capability.getCapabilityId());
        Capability created = capabilityService.register(capability);
        return ResultModel.success(created);
    }

    @PutMapping("/{capabilityId}")
    public ResultModel<Capability> updateCapability(
            @PathVariable String capabilityId,
            @RequestBody Capability capability) {
        capability.setCapabilityId(capabilityId);
        Capability updated = capabilityService.update(capability);
        return ResultModel.success(updated);
    }

    @DeleteMapping("/{capabilityId}")
    public ResultModel<Boolean> deleteCapability(@PathVariable String capabilityId) {
        log.info("Delete capability: {}", capabilityId);
        capabilityService.unregister(capabilityId);
        return ResultModel.success(true);
    }

    @PostMapping("/{capabilityId}/status")
    public ResultModel<Boolean> updateStatus(
            @PathVariable String capabilityId,
            @RequestBody StatusUpdateRequest request) {
        capabilityService.updateStatus(capabilityId, request.getStatus());
        return ResultModel.success(true);
    }

    @GetMapping("/{capabilityId}/bindings")
    public ResultModel<List<CapabilityBinding>> getCapabilityBindings(@PathVariable String capabilityId) {
        List<CapabilityBinding> bindings = bindingService.listByCapability(capabilityId);
        return ResultModel.success(bindings);
    }

    @GetMapping("/bindings")
    public ResultModel<List<CapabilityBinding>> listAllBindings() {
        List<CapabilityBinding> bindings = bindingService.listAll();
        return ResultModel.success(bindings);
    }
    
    @PostMapping("/bindings")
    public ResultModel<CapabilityBinding> createBinding(@RequestBody CapabilityBindingService.CapabilityBindingRequest request) {
        String sceneGroupId = request.getCapabilityId();
        log.info("Create capability binding for scene group: {}", sceneGroupId);
        CapabilityBinding binding = bindingService.bind(sceneGroupId, request);
        return ResultModel.success(binding);
    }

    @GetMapping("/bindings/{bindingId}")
    public ResultModel<CapabilityBinding> getBinding(@PathVariable String bindingId) {
        CapabilityBinding binding = bindingService.findById(bindingId);
        if (binding == null) {
            return ResultModel.notFound("Binding not found: " + bindingId);
        }
        return ResultModel.success(binding);
    }

    @DeleteMapping("/bindings/{bindingId}")
    public ResultModel<Boolean> deleteBinding(@PathVariable String bindingId) {
        log.info("Delete binding: {}", bindingId);
        bindingService.unbind(bindingId);
        return ResultModel.success(true);
    }

    @PostMapping("/bindings/{bindingId}/status")
    public ResultModel<Boolean> updateBindingStatus(
            @PathVariable String bindingId,
            @RequestBody StatusUpdateRequest request) {
        bindingService.updateStatus(bindingId, request.getStatus());
        return ResultModel.success(true);
    }
    
    @GetMapping("/bindings/by-agent/{agentId}")
    public ResultModel<List<CapabilityBinding>> listBindingsByAgent(@PathVariable String agentId) {
        log.info("List bindings by agent: {}", agentId);
        List<CapabilityBinding> bindings = bindingService.listByAgent(agentId);
        return ResultModel.success(bindings);
    }
    
    @GetMapping("/bindings/by-link/{linkId}")
    public ResultModel<List<CapabilityBinding>> listBindingsByLink(@PathVariable String linkId) {
        log.info("List bindings by link: {}", linkId);
        List<CapabilityBinding> bindings = bindingService.listByLink(linkId);
        return ResultModel.success(bindings);
    }

    @PostMapping("/bindings/{bindingId}/test")
    public ResultModel<Map<String, Object>> testBinding(
            @PathVariable String bindingId,
            @RequestBody(required = false) Map<String, Object> params) {
        log.info("Test binding: {}", bindingId);
        
        CapabilityBinding binding = bindingService.findById(bindingId);
        if (binding == null) {
            return ResultModel.notFound("Binding not found: " + bindingId);
        }
        
        if (binding.getStatus() != CapabilityBindingStatus.ACTIVE) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("error", "Binding is not active");
            errorResult.put("status", binding.getStatus());
            return ResultModel.success(errorResult);
        }
        
        String capabilityId = binding.getCapabilityId();
        if (params == null) {
            params = new HashMap<>();
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("bindingId", bindingId);
        result.put("capabilityId", capabilityId);
        result.put("testTime", System.currentTimeMillis());
        
        try {
            if (sceneEngineIntegration != null) {
                Object invokeResult = sceneEngineIntegration.invokeCapability(capabilityId, params);
                result.put("success", true);
                result.put("result", invokeResult);
                result.put("message", "Capability invoked successfully");
            } else {
                result.put("success", true);
                result.put("result", createMockTestResult(capabilityId));
                result.put("message", "SceneEngineIntegration not available, returning mock result");
            }
        } catch (Exception e) {
            log.error("Failed to test binding {}: {}", bindingId, e.getMessage());
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return ResultModel.success(result);
    }
    
    private Map<String, Object> createMockTestResult(String capabilityId) {
        Map<String, Object> mockResult = new HashMap<>();
        mockResult.put("mock", true);
        mockResult.put("capabilityId", capabilityId);
        mockResult.put("error", "SceneEngineIntegration not available");
        mockResult.put("message", "Please configure SE SDK to enable capability testing");
        mockResult.put("timestamp", System.currentTimeMillis());
        return mockResult;
    }

    @PostMapping("/sync")
    public ResultModel<Map<String, Object>> syncCapabilitiesFromSkills() {
        log.info("Sync capabilities from skill.yaml files");
        
        if (syncService == null) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("error", "SkillCapabilitySyncService not available");
            errorResult.put("message", "Please check if the skills directory is configured correctly");
            return ResultModel.error(500, "Sync service not available");
        }
        
        try {
            Map<String, Object> result = syncService.syncAllSkills();
            return ResultModel.success(result);
        } catch (Exception e) {
            log.error("Failed to sync capabilities: {}", e.getMessage());
            return ResultModel.error(500, "Sync failed: " + e.getMessage());
        }
    }

    @GetMapping("/sync/status")
    public ResultModel<Map<String, Object>> getSyncStatus() {
        Map<String, Object> status = new HashMap<>();
        
        if (syncService == null) {
            status.put("available", false);
            status.put("message", "SkillCapabilitySyncService not configured");
        } else {
            status.put("available", true);
            status.put("syncedCount", syncService.getSyncedCount());
            status.put("skippedCount", syncService.getSkippedCount());
            status.put("errorCount", syncService.getErrorCount());
        }
        
        return ResultModel.success(status);
    }
    
    @GetMapping("/{capabilityId}/classify")
    public ResultModel<SceneSkillClassificationResultDTO> classifyCapability(@PathVariable String capabilityId) {
        log.info("Classify capability: {}", capabilityId);
        
        Capability capability = capabilityService.findById(capabilityId);
        if (capability == null) {
            return ResultModel.notFound("Capability not found: " + capabilityId);
        }
        
        SkillForm skillForm = categoryDetector.detectSkillForm(capability);
        SceneType sceneType = categoryDetector.detectSceneType(capability);
        Integer score = capability.getBusinessSemanticsScore();
        
        SceneSkillClassificationResultDTO result = SceneSkillClassificationResultDTO.from(
            capabilityId, 
            skillForm != null ? skillForm.name() : null, 
            sceneType != null ? sceneType.name() : null, 
            score
        );
        
        return ResultModel.success(result);
    }
    
    @GetMapping("/{capabilityId}/dependencies")
    public ResultModel<Map<String, Object>> getCapabilityDependencies(@PathVariable String capabilityId) {
        log.info("Get dependencies for capability: {}", capabilityId);
        
        Capability capability = capabilityService.findById(capabilityId);
        if (capability == null) {
            return ResultModel.notFound("Capability not found: " + capabilityId);
        }
        
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("capabilityId", capabilityId);
        result.put("dependencies", capability.getDependencies() != null ? capability.getDependencies() : new ArrayList<String>());
        result.put("references", new ArrayList<Object>());
        
        return ResultModel.success(result);
    }
    
    @GetMapping("/{capabilityId}/logs")
    public ResultModel<List<Map<String, Object>>> getCapabilityCallLogs(@PathVariable String capabilityId) {
        log.info("Get call logs for capability: {}", capabilityId);
        
        Capability capability = capabilityService.findById(capabilityId);
        if (capability == null) {
            return ResultModel.notFound("Capability not found: " + capabilityId);
        }
        
        List<Map<String, Object>> logs = new ArrayList<Map<String, Object>>();
        
        if (statsService != null) {
            List<LogEntryDTO> recentLogs = statsService.getRecentLogs(50);
            for (LogEntryDTO logEntry : recentLogs) {
                if (logEntry.getCapabilityId() != null && logEntry.getCapabilityId().contains(capabilityId)) {
                    Map<String, Object> logMap = new HashMap<String, Object>();
                    logMap.put("timestamp", logEntry.getTimestamp());
                    logMap.put("capabilityId", logEntry.getCapabilityId());
                    logMap.put("capabilityName", logEntry.getCapabilityName());
                    logMap.put("level", logEntry.getLevel());
                    logMap.put("message", logEntry.getMessage());
                    logMap.put("duration", logEntry.getDuration());
                    logs.add(logMap);
                }
            }
        }
        
        return ResultModel.success(logs);
    }
    
    @GetMapping("/skill-forms")
    public ResultModel<List<Map<String, Object>>> listSkillForms() {
        List<Map<String, Object>> forms = new ArrayList<Map<String, Object>>();
        
        for (SkillForm form : SkillForm.values()) {
            Map<String, Object> info = new HashMap<String, Object>();
            info.put("code", form.name());
            info.put("name", form.name());
            info.put("description", form.name());
            forms.add(info);
        }
        
        return ResultModel.success(forms);
    }
    
    @GetMapping("/scene-types")
    public ResultModel<List<Map<String, Object>>> listSceneTypes() {
        List<Map<String, Object>> types = new ArrayList<Map<String, Object>>();
        
        for (SceneType type : SceneType.values()) {
            Map<String, Object> info = new HashMap<String, Object>();
            info.put("code", type.name());
            info.put("name", type.name());
            info.put("description", type.name());
            types.add(info);
        }
        
        return ResultModel.success(types);
    }
    
    @GetMapping("/ownerships")
    public ResultModel<List<Map<String, Object>>> listCapabilityOwnerships() {
        List<Map<String, Object>> ownerships = new ArrayList<Map<String, Object>>();
        
        for (CapabilityOwnership ownership : CapabilityOwnership.values()) {
            Map<String, Object> info = new HashMap<String, Object>();
            info.put("code", ownership.getCode());
            info.put("name", ownership.getName());
            info.put("description", ownership.getDescription());
            info.put("sort", ownership.getSort());
            ownerships.add(info);
        }
        
        return ResultModel.success(ownerships);
    }
    
    @GetMapping("/by-ownership")
    public ResultModel<List<Capability>> listByOwnership(
            @RequestParam String ownership,
            @RequestParam(required = false) String sceneType) {
        
        CapabilityOwnership ownershipType = CapabilityOwnership.fromCode(ownership);
        if (ownershipType == null) {
            return ResultModel.error(400, "Invalid ownership type: " + ownership);
        }
        
        List<Capability> capabilities;
        if (sceneType != null && !sceneType.isEmpty()) {
            capabilities = capabilityService.findByOwnershipAndSceneType(ownershipType, sceneType);
        } else {
            capabilities = capabilityService.findByOwnership(ownershipType);
        }
        
        return ResultModel.success(capabilities);
    }
    
    @PutMapping("/{capabilityId}/scene-types")
    public ResultModel<Capability> updateSceneTypes(
            @PathVariable String capabilityId,
            @RequestBody SceneTypeUpdateRequest request) {
        
        log.info("Update scene types for capability: {} - action: {}, sceneType: {}", 
            capabilityId, request.getAction(), request.getSceneType());
        
        Capability capability;
        String action = request.getAction();
        String sceneType = request.getSceneType();
        String approvedBy = request.getApprovedBy();
        
        if ("add".equals(action)) {
            capability = capabilityService.addSceneType(capabilityId, sceneType, approvedBy);
        } else if ("remove".equals(action)) {
            capability = capabilityService.removeSceneType(capabilityId, sceneType, approvedBy);
        } else {
            return ResultModel.error(400, "Invalid action: " + action + ". Use 'add' or 'remove'.");
        }
        
        return ResultModel.success(capability);
    }
    
    @GetMapping("/{capabilityId}/scene-types")
    public ResultModel<Map<String, Object>> getSceneTypes(@PathVariable String capabilityId) {
        Capability capability = capabilityService.findById(capabilityId);
        if (capability == null) {
            return ResultModel.notFound("Capability not found: " + capabilityId);
        }
        
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("capabilityId", capabilityId);
        result.put("supportedSceneTypes", capability.getSupportedSceneTypes());
        result.put("dynamicSceneTypes", capability.isDynamicSceneTypes());
        result.put("ownership", capability.getOwnership().getCode());
        
        return ResultModel.success(result);
    }
    
    @GetMapping("/logs")
    public ResultModel<PageResult<Map<String, Object>>> getCapabilityLogs(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String capabilityId,
            @RequestParam(required = false) String level) {
        
        log.info("Get capability logs - pageNum: {}, pageSize: {}, capabilityId: {}, level: {}", 
            pageNum, pageSize, capabilityId, level);
        
        List<LogEntryDTO> logs = statsService.getRecentLogs(pageSize * 10);
        
        List<Map<String, Object>> filteredLogs = new ArrayList<Map<String, Object>>();
        for (LogEntryDTO logEntry : logs) {
            Map<String, Object> logMap = new HashMap<String, Object>();
            logMap.put("timestamp", logEntry.getTimestamp());
            logMap.put("capabilityId", logEntry.getCapabilityId());
            logMap.put("capabilityName", logEntry.getCapabilityName());
            logMap.put("level", logEntry.getLevel());
            logMap.put("message", logEntry.getMessage());
            logMap.put("duration", logEntry.getDuration());
            
            if (capabilityId != null && !capabilityId.isEmpty()) {
                String capName = logEntry.getCapabilityName();
                if (capName == null || !capName.contains(capabilityId)) {
                    continue;
                }
            }
            
            if (level != null && !level.isEmpty()) {
                String logLevel = logEntry.getLevel();
                if (logLevel == null || !logLevel.equalsIgnoreCase(level)) {
                    continue;
                }
            }
            
            filteredLogs.add(logMap);
        }
        
        int total = filteredLogs.size();
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, total);
        
        List<Map<String, Object>> pagedLogs = new ArrayList<Map<String, Object>>();
        if (start < total) {
            pagedLogs = filteredLogs.subList(start, end);
        }
        
        PageResult<Map<String, Object>> result = new PageResult<Map<String, Object>>();
        result.setList(pagedLogs);
        result.setTotal(total);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        
        return ResultModel.success(result);
    }
}

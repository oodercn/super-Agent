package net.ooder.agent.controller;

import net.ooder.agent.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/installs")
@CrossOrigin(originPatterns = "*", allowCredentials = "true", allowedHeaders = "*")
public class InstallApiController {

    private static final Logger log = LoggerFactory.getLogger(InstallApiController.class);

    @GetMapping("/pending-activations")
    public ResultModel<List<Map<String, Object>>> getPendingActivations(
            @RequestParam(required = false) String userId) {
        log.info("[getPendingActivations] Getting pending activations for user: {}", userId);
        
        List<Map<String, Object>> pendingActivations = new ArrayList<>();
        
        return ResultModel.success(pendingActivations);
    }

    @GetMapping("/history")
    public ResultModel<List<Map<String, Object>>> getInstallHistory(
            @RequestParam(required = false) String userId) {
        log.info("[getInstallHistory] Getting install history for user: {}", userId);
        
        List<Map<String, Object>> history = new ArrayList<>();
        
        return ResultModel.success(history);
    }

    @PostMapping("/{skillId}/activate")
    public ResultModel<Map<String, Object>> activateSkill(@PathVariable String skillId) {
        log.info("[activateSkill] Activating skill: {}", skillId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("skillId", skillId);
        result.put("status", "ACTIVATED");
        result.put("message", "Skill activated successfully");
        result.put("activatedAt", System.currentTimeMillis());
        
        return ResultModel.success(result);
    }

    @PostMapping("/{skillId}/deactivate")
    public ResultModel<Map<String, Object>> deactivateSkill(@PathVariable String skillId) {
        log.info("[deactivateSkill] Deactivating skill: {}", skillId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("skillId", skillId);
        result.put("status", "DISABLED");
        result.put("message", "Skill deactivated successfully");
        result.put("deactivatedAt", System.currentTimeMillis());
        
        return ResultModel.success(result);
    }

    @GetMapping("/{skillId}/status")
    public ResultModel<Map<String, Object>> getInstallStatus(@PathVariable String skillId) {
        log.info("[getInstallStatus] Getting install status for: {}", skillId);
        
        Map<String, Object> status = new HashMap<>();
        status.put("skillId", skillId);
        status.put("installed", false);
        status.put("activated", false);
        status.put("status", "REGISTERED");
        
        return ResultModel.success(status);
    }
}

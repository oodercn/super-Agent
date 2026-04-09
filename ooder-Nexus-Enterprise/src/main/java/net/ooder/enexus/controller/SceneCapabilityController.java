package net.ooder.enexus.controller;

import net.ooder.enexus.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/scene-capabilities")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class SceneCapabilityController {

    private static final Logger log = LoggerFactory.getLogger(SceneCapabilityController.class);

    private static final Map<String, Map<String, Object>> capabilities = new HashMap<>();
    
    static {
        String[] skillIds = {"skill-agent", "skill-audit", "skill-config", "skill-dashboard", 
            "skill-dict", "skill-discovery", "skill-history", "skill-install", 
            "skill-knowledge", "skill-notification", "skill-scene"};
        
        for (String skillId : skillIds) {
            Map<String, Object> cap = new HashMap<>();
            cap.put("capabilityId", skillId);
            cap.put("name", skillId.replace("skill-", "").toUpperCase() + " Skill");
            cap.put("status", "ACTIVATED");
            cap.put("category", "SYS");
            cap.put("installed", true);
            cap.put("activated", true);
            capabilities.put(skillId, cap);
        }
    }

    @GetMapping
    public ResultModel<List<Map<String, Object>>> listAll() {
        log.info("[listAll] Listing all scene capabilities");
        return ResultModel.success(new ArrayList<>(capabilities.values()));
    }

    @GetMapping("/{capabilityId}")
    public ResultModel<Map<String, Object>> get(@PathVariable String capabilityId) {
        log.info("[get] Getting capability: {}", capabilityId);
        Map<String, Object> cap = capabilities.get(capabilityId);
        if (cap == null) {
            return ResultModel.error(404, "Capability not found: " + capabilityId);
        }
        return ResultModel.success(cap);
    }

    @PostMapping("/{capabilityId}/activate")
    public ResultModel<Map<String, Object>> activate(
            @PathVariable String capabilityId,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        log.info("[activate] Activating capability: {} by user: {}", capabilityId, userId);
        
        Map<String, Object> cap = capabilities.get(capabilityId);
        if (cap == null) {
            Map<String, Object> newCap = new HashMap<>();
            newCap.put("capabilityId", capabilityId);
            newCap.put("name", capabilityId);
            newCap.put("status", "ACTIVATED");
            newCap.put("category", "SYS");
            newCap.put("installed", true);
            newCap.put("activated", true);
            capabilities.put(capabilityId, newCap);
            return ResultModel.success(newCap);
        }
        
        cap.put("status", "ACTIVATED");
        cap.put("activated", true);
        return ResultModel.success(cap);
    }

    @PostMapping("/{capabilityId}/deactivate")
    public ResultModel<Map<String, Object>> deactivate(
            @PathVariable String capabilityId,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        log.info("[deactivate] Deactivating capability: {} by user: {}", capabilityId, userId);
        
        Map<String, Object> cap = capabilities.get(capabilityId);
        if (cap == null) {
            return ResultModel.error(404, "Capability not found: " + capabilityId);
        }
        
        cap.put("status", "DEACTIVATED");
        cap.put("activated", false);
        return ResultModel.success(cap);
    }

    @PostMapping("/{capabilityId}/pause")
    public ResultModel<Map<String, Object>> pause(
            @PathVariable String capabilityId,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        log.info("[pause] Pausing capability: {} by user: {}", capabilityId, userId);
        
        Map<String, Object> cap = capabilities.get(capabilityId);
        if (cap == null) {
            return ResultModel.error(404, "Capability not found: " + capabilityId);
        }
        
        cap.put("status", "PAUSED");
        return ResultModel.success(cap);
    }

    @PostMapping("/{capabilityId}/resume")
    public ResultModel<Map<String, Object>> resume(
            @PathVariable String capabilityId,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        log.info("[resume] Resuming capability: {} by user: {}", capabilityId, userId);
        
        Map<String, Object> cap = capabilities.get(capabilityId);
        if (cap == null) {
            return ResultModel.error(404, "Capability not found: " + capabilityId);
        }
        
        cap.put("status", "ACTIVATED");
        cap.put("activated", true);
        return ResultModel.success(cap);
    }

    @GetMapping("/{capabilityId}/state")
    public ResultModel<Map<String, Object>> getState(@PathVariable String capabilityId) {
        log.info("[getState] Getting state for capability: {}", capabilityId);
        
        Map<String, Object> cap = capabilities.get(capabilityId);
        if (cap == null) {
            return ResultModel.error(404, "Capability not found: " + capabilityId);
        }
        
        Map<String, Object> state = new HashMap<>();
        state.put("capabilityId", capabilityId);
        state.put("status", cap.get("status"));
        state.put("activated", cap.get("activated"));
        return ResultModel.success(state);
    }

    @GetMapping("/{capabilityId}/transitions")
    public ResultModel<List<Map<String, Object>>> getTransitions(@PathVariable String capabilityId) {
        log.info("[getTransitions] Getting transitions for capability: {}", capabilityId);
        
        List<Map<String, Object>> transitions = new ArrayList<>();
        
        Map<String, Object> cap = capabilities.get(capabilityId);
        String status = cap != null ? (String) cap.get("status") : "ACTIVATED";
        
        if ("ACTIVATED".equals(status)) {
            transitions.add(createTransition("deactivate", "停用", "ri-stop-circle-line"));
            transitions.add(createTransition("pause", "暂停", "ri-pause-circle-line"));
        } else if ("DEACTIVATED".equals(status)) {
            transitions.add(createTransition("activate", "激活", "ri-play-circle-line"));
        } else if ("PAUSED".equals(status)) {
            transitions.add(createTransition("resume", "恢复", "ri-play-circle-line"));
            transitions.add(createTransition("deactivate", "停用", "ri-stop-circle-line"));
        }
        
        return ResultModel.success(transitions);
    }

    private Map<String, Object> createTransition(String action, String name, String icon) {
        Map<String, Object> t = new HashMap<>();
        t.put("action", action);
        t.put("name", name);
        t.put("icon", icon);
        return t;
    }
}

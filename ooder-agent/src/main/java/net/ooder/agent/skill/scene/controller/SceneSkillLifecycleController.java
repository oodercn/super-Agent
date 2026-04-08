package net.ooder.mvp.skill.scene.controller;

import net.ooder.mvp.skill.scene.capability.service.SceneSkillLifecycleService;
import net.ooder.mvp.skill.scene.capability.service.SceneSkillLifecycleService.LifecycleResult;
import net.ooder.mvp.skill.scene.capability.service.SceneSkillLifecycleService.LifecycleState;
import net.ooder.mvp.skill.scene.capability.service.SceneSkillLifecycleService.LifecycleTransition;
import net.ooder.mvp.skill.scene.capability.service.SceneSkillLifecycleService.TriggerRequest;
import net.ooder.mvp.skill.scene.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/scene-capabilities")
public class SceneSkillLifecycleController {

    private static final Logger log = LoggerFactory.getLogger(SceneSkillLifecycleController.class);

    @Autowired
    private SceneSkillLifecycleService lifecycleService;

    @PostMapping("/{id}/activate")
    public ResultModel<LifecycleResult> activate(
            @PathVariable("id") String capabilityId,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        log.info("[activate] Activating scene capability: {}", capabilityId);
        
        LifecycleResult result = lifecycleService.activate(capabilityId, userId);
        
        if (result.isSuccess()) {
            return ResultModel.success(result);
        } else {
            return ResultModel.error(result.getMessage());
        }
    }

    @PostMapping("/{id}/pause")
    public ResultModel<LifecycleResult> pause(
            @PathVariable("id") String capabilityId,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        log.info("[pause] Pausing scene capability: {}", capabilityId);
        
        LifecycleResult result = lifecycleService.pause(capabilityId, userId);
        
        if (result.isSuccess()) {
            return ResultModel.success(result);
        } else {
            return ResultModel.error(result.getMessage());
        }
    }

    @PostMapping("/{id}/deactivate")
    public ResultModel<LifecycleResult> deactivate(
            @PathVariable("id") String capabilityId,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        log.info("[deactivate] Deactivating scene capability: {}", capabilityId);
        
        LifecycleResult result = lifecycleService.deactivate(capabilityId, userId);
        
        if (result.isSuccess()) {
            return ResultModel.success(result);
        } else {
            return ResultModel.error(result.getMessage());
        }
    }

    @PostMapping("/{id}/resume")
    public ResultModel<LifecycleResult> resume(
            @PathVariable("id") String capabilityId,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        log.info("[resume] Resuming scene capability: {}", capabilityId);
        
        LifecycleResult result = lifecycleService.resume(capabilityId, userId);
        
        if (result.isSuccess()) {
            return ResultModel.success(result);
        } else {
            return ResultModel.error(result.getMessage());
        }
    }

    @PostMapping("/{id}/trigger")
    public ResultModel<LifecycleResult> trigger(
            @PathVariable("id") String capabilityId,
            @RequestBody TriggerRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        log.info("[trigger] Triggering scene capability: {} with action: {}", capabilityId, request.getAction());
        
        request.setUserId(userId);
        LifecycleResult result = lifecycleService.trigger(capabilityId, request);
        
        if (result.isSuccess()) {
            return ResultModel.success(result);
        } else {
            return ResultModel.error(result.getMessage());
        }
    }

    @PostMapping("/{id}/archive")
    public ResultModel<LifecycleResult> archive(
            @PathVariable("id") String capabilityId,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        log.info("[archive] Archiving scene capability: {}", capabilityId);
        
        LifecycleResult result = lifecycleService.archive(capabilityId, userId);
        
        if (result.isSuccess()) {
            return ResultModel.success(result);
        } else {
            return ResultModel.error(result.getMessage());
        }
    }

    @GetMapping("/{id}/state")
    public ResultModel<LifecycleState> getState(@PathVariable("id") String capabilityId) {
        log.info("[getState] Getting state for scene capability: {}", capabilityId);
        
        LifecycleState state = lifecycleService.getState(capabilityId);
        
        if (state != null) {
            return ResultModel.success(state);
        } else {
            return ResultModel.error("Capability not found: " + capabilityId);
        }
    }

    @GetMapping("/{id}/transitions")
    public ResultModel<List<LifecycleTransition>> getAvailableTransitions(
            @PathVariable("id") String capabilityId) {
        log.info("[getAvailableTransitions] Getting available transitions for: {}", capabilityId);
        
        List<LifecycleTransition> transitions = lifecycleService.getAvailableTransitions(capabilityId);
        return ResultModel.success(transitions);
    }
}

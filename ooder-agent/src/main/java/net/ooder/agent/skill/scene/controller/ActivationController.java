package net.ooder.mvp.skill.scene.controller;

import net.ooder.mvp.skill.scene.capability.activation.ActivationProcess;
import net.ooder.mvp.skill.scene.capability.activation.ActivationService;
import net.ooder.mvp.skill.scene.capability.activation.ActivationService.KeyResult;
import net.ooder.mvp.skill.scene.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController("sceneActivationController")
@RequestMapping("/api/v1/activations")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ActivationController {

    private static final Logger log = LoggerFactory.getLogger(ActivationController.class);

    @Autowired
    private ActivationService activationService;

    private final ExecutorService executor = Executors.newCachedThreadPool();

    @GetMapping("/{installId}/process")
    public ResultModel<ActivationProcess> getProcess(@PathVariable String installId) {
        log.info("[getProcess] Getting activation process for: {}", installId);
        ActivationProcess process = activationService.getProcess(installId);
        if (process == null) {
            return ResultModel.notFound("Activation process not found for install: " + installId);
        }
        return ResultModel.success(process);
    }

    @PostMapping("/{installId}/start")
    public ResultModel<ActivationProcess> startProcess(
            @PathVariable String installId,
            @RequestBody StartActivationRequest request) {
        log.info("[startProcess] Starting activation for: {}", installId);
        String activator = request.getActivator();
        ActivationProcess process = activationService.startProcess(installId, activator);
        return ResultModel.success(process);
    }

    @PostMapping("/{installId}/start-with-template")
    public ResultModel<ActivationProcess> startProcessWithTemplate(
            @PathVariable String installId,
            @RequestBody TemplateActivationRequest request) {
        log.info("[startProcessWithTemplate] Starting activation for template: {}, role: {}", 
            request.getTemplateId(), request.getRoleName());
        ActivationProcess process = activationService.startProcessWithTemplate(
            installId, 
            request.getTemplateId(),
            request.getSceneGroupId(),
            request.getActivator(),
            request.getRoleName()
        );
        if (process == null) {
            return ResultModel.error(404, "Template not found: " + request.getTemplateId());
        }
        return ResultModel.success(process);
    }

    @PostMapping("/{installId}/steps/{stepId}/execute")
    public ResultModel<ActivationProcess> executeStep(
            @PathVariable String installId,
            @PathVariable String stepId,
            @RequestBody(required = false) StepExecutionRequest request) {
        log.info("[executeStep] Executing step {} for: {}", stepId, installId);
        try {
            Map<String, Object> data = request != null ? request.getData() : null;
            ActivationProcess process = activationService.executeStep(installId, stepId, data);
            return ResultModel.success(process);
        } catch (Exception e) {
            log.error("[executeStep] Failed: {}", e.getMessage());
            return ResultModel.error(500, "Failed to execute step: " + e.getMessage());
        }
    }

    @PostMapping("/{installId}/steps/{stepId}/skip")
    public ResultModel<ActivationProcess> skipStep(
            @PathVariable String installId,
            @PathVariable String stepId) {
        log.info("[skipStep] Skipping step {} for: {}", stepId, installId);
        ActivationProcess process = activationService.skipStep(installId, stepId);
        return ResultModel.success(process);
    }

    @PostMapping("/{installId}/key")
    public ResultModel<KeyResult> getKey(@PathVariable String installId) {
        log.info("[getKey] Getting key for: {}", installId);
        KeyResult result = activationService.getKey(installId);
        return ResultModel.success(result);
    }

    @PostMapping("/{installId}/activate")
    public ResultModel<ActivationProcess> confirmActivation(@PathVariable String installId) {
        log.info("[confirmActivation] Confirming activation for: {}", installId);
        try {
            ActivationProcess process = activationService.confirmActivation(installId);
            return ResultModel.success(process);
        } catch (Exception e) {
            log.error("[confirmActivation] Failed: {}", e.getMessage());
            return ResultModel.error(500, "Failed to confirm activation: " + e.getMessage());
        }
    }

    @PostMapping("/{installId}/cancel")
    public ResultModel<ActivationProcess> cancelActivation(@PathVariable String installId) {
        log.info("[cancelActivation] Cancelling activation for: {}", installId);
        ActivationProcess process = activationService.cancelActivation(installId);
        return ResultModel.success(process);
    }

    @GetMapping("/{installId}/network-actions")
    public ResultModel<List<ActivationProcess.NetworkAction>> getNetworkActions(@PathVariable String installId) {
        log.info("[getNetworkActions] Getting network actions for: {}", installId);
        List<ActivationProcess.NetworkAction> actions = activationService.getNetworkActions(installId);
        return ResultModel.success(actions);
    }

    @PostMapping("/{installId}/network-actions/execute")
    public ResultModel<ActivationProcess> executeNetworkActions(@PathVariable String installId) {
        log.info("[executeNetworkActions] Executing network actions for: {}", installId);
        try {
            ActivationProcess process = activationService.executeNetworkActions(installId).get();
            return ResultModel.success(process);
        } catch (Exception e) {
            log.error("[executeNetworkActions] Failed: {}", e.getMessage());
            return ResultModel.error(500, "Failed to execute network actions: " + e.getMessage());
        }
    }

    @GetMapping("/{installId}/private-capabilities")
    public ResultModel<List<ActivationProcess.PrivateCapabilityConfig>> getPrivateCapabilities(@PathVariable String installId) {
        log.info("[getPrivateCapabilities] Getting private capabilities for: {}", installId);
        List<ActivationProcess.PrivateCapabilityConfig> caps = activationService.getPrivateCapabilities(installId);
        return ResultModel.success(caps);
    }

    @PostMapping("/{installId}/private-capabilities/configure")
    public ResultModel<ActivationProcess> configurePrivateCapabilities(
            @PathVariable String installId,
            @RequestBody PrivateCapabilitiesRequest request) {
        log.info("[configurePrivateCapabilities] Configuring private capabilities for: {}", installId);
        try {
            ActivationProcess process = activationService.configurePrivateCapabilities(
                installId, 
                request.getEnabledCapabilityIds()
            );
            return ResultModel.success(process);
        } catch (Exception e) {
            log.error("[configurePrivateCapabilities] Failed: {}", e.getMessage());
            return ResultModel.error(500, "Failed to configure private capabilities: " + e.getMessage());
        }
    }

    @GetMapping(value = "/{installId}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamActivation(@PathVariable String installId) {
        log.info("[streamActivation] Starting SSE stream for: {}", installId);
        SseEmitter emitter = new SseEmitter(300000L);
        
        executor.execute(() -> {
            try {
                ActivationProcess process = activationService.getProcess(installId);
                if (process != null) {
                    emitter.send(SseEmitter.event().name("process").data(process));
                    
                    while (process.isInProgress()) {
                        Thread.sleep(1000);
                        process = activationService.getProcess(installId);
                        if (process != null) {
                            emitter.send(SseEmitter.event().name("update").data(process));
                        }
                    }
                    
                    emitter.send(SseEmitter.event().name("complete").data(process));
                    emitter.complete();
                } else {
                    emitter.completeWithError(new RuntimeException("Process not found"));
                }
            } catch (Exception e) {
                log.error("[SSE] Error: {}", e.getMessage());
                try {
                    emitter.completeWithError(e);
                } catch (Exception ex) {
                    log.error("[SSE] Error completing: {}", ex.getMessage());
                }
            }
        });
        
        return emitter;
    }

    public static class TemplateActivationRequest {
        private String templateId;
        private String sceneGroupId;
        private String activator;
        private String roleName;

        public String getTemplateId() { return templateId; }
        public void setTemplateId(String templateId) { this.templateId = templateId; }
        public String getSceneGroupId() { return sceneGroupId; }
        public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
        public String getActivator() { return activator; }
        public void setActivator(String activator) { this.activator = activator; }
        public String getRoleName() { return roleName; }
        public void setRoleName(String roleName) { this.roleName = roleName; }
    }

    public static class PrivateCapabilitiesRequest {
        private List<String> enabledCapabilityIds;

        public List<String> getEnabledCapabilityIds() { return enabledCapabilityIds; }
        public void setEnabledCapabilityIds(List<String> enabledCapabilityIds) { this.enabledCapabilityIds = enabledCapabilityIds; }
    }
}

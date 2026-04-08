package net.ooder.mvp.skill.scene.controller;

import net.ooder.mvp.skill.scene.capability.install.InstallConfig;
import net.ooder.mvp.skill.scene.capability.install.InstallService;
import net.ooder.mvp.skill.scene.capability.install.InstallService.*;
import net.ooder.mvp.skill.scene.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/v1/installs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class InstallController {

    private static final Logger log = LoggerFactory.getLogger(InstallController.class);

    @Autowired
    private InstallService installService;

    private final ExecutorService executor = Executors.newCachedThreadPool();

    @PostMapping
    public ResultModel<InstallConfig> createInstall(@RequestBody CreateInstallRequest request) {
        log.info("[createInstall] Creating install for capability: {}", request.getCapabilityId());
        try {
            InstallConfig config = installService.createInstall(request);
            return ResultModel.success(config);
        } catch (Exception e) {
            log.error("[createInstall] Failed: {}", e.getMessage());
            return ResultModel.error(500, "Failed to create install: " + e.getMessage());
        }
    }

    @GetMapping("/{installId}")
    public ResultModel<InstallConfig> getInstall(@PathVariable String installId) {
        log.info("[getInstall] Getting install: {}", installId);
        InstallConfig config = installService.getInstall(installId);
        if (config == null) {
            return ResultModel.notFound("Install not found: " + installId);
        }
        return ResultModel.success(config);
    }

    @PutMapping("/{installId}/driver-condition")
    public ResultModel<InstallConfig> updateDriverCondition(
            @PathVariable String installId,
            @RequestBody DriverConditionRequest request) {
        log.info("[updateDriverCondition] Updating driver condition for: {}", installId);
        String driverCondition = request.getDriverCondition();
        InstallConfig config = installService.updateDriverCondition(installId, driverCondition);
        return ResultModel.success(config);
    }

    @PostMapping("/{installId}/participants")
    public ResultModel<InstallConfig> addParticipant(
            @PathVariable String installId,
            @RequestBody AddParticipantRequest request) {
        log.info("[addParticipant] Adding participant to: {}", installId);
        InstallConfig config = installService.addParticipant(installId, request);
        return ResultModel.success(config);
    }

    @DeleteMapping("/{installId}/participants/{userId}")
    public ResultModel<InstallConfig> removeParticipant(
            @PathVariable String installId,
            @PathVariable String userId) {
        log.info("[removeParticipant] Removing participant {} from: {}", userId, installId);
        InstallConfig config = installService.removeParticipant(installId, userId);
        return ResultModel.success(config);
    }

    @PutMapping("/{installId}/optional-capabilities")
    public ResultModel<InstallConfig> updateOptionalCapabilities(
            @PathVariable String installId,
            @RequestBody OptionalCapabilitiesRequest request) {
        log.info("[updateOptionalCapabilities] Updating optional capabilities for: {}", installId);
        List<String> capabilities = request.getCapabilities();
        InstallConfig config = installService.updateOptionalCapabilities(installId, capabilities);
        return ResultModel.success(config);
    }

    @PostMapping("/{installId}/execute")
    public ResultModel<InstallConfig> executeInstall(@PathVariable String installId) {
        log.info("[executeInstall] Executing install: {}", installId);
        try {
            InstallConfig config = installService.executeInstall(installId).get();
            return ResultModel.success(config);
        } catch (Exception e) {
            log.error("[executeInstall] Failed: {}", e.getMessage());
            return ResultModel.error(500, "Failed to execute install: " + e.getMessage());
        }
    }

    @GetMapping(value = "/{installId}/execute/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter executeInstallWithProgress(@PathVariable String installId) {
        log.info("[executeInstallWithProgress] Starting SSE for: {}", installId);
        SseEmitter emitter = new SseEmitter(300000L);
        
        executor.execute(() -> {
            try {
                installService.executeInstall(installId)
                    .thenAccept(config -> {
                        try {
                            emitter.send(SseEmitter.event().name("complete").data(config));
                            emitter.complete();
                        } catch (Exception e) {
                            log.error("[SSE] Error sending complete: {}", e.getMessage());
                        }
                    });
            } catch (Exception e) {
                log.error("[SSE] Error: {}", e.getMessage());
                try {
                    emitter.send(SseEmitter.event().name("error").data(e.getMessage()));
                    emitter.completeWithError(e);
                } catch (Exception ex) {
                    log.error("[SSE] Error sending error: {}", ex.getMessage());
                }
            }
        });
        
        return emitter;
    }

    @GetMapping("/{installId}/progress")
    public ResultModel<InstallProgress> getInstallProgress(@PathVariable String installId) {
        log.info("[getInstallProgress] Getting progress for: {}", installId);
        InstallProgress progress = installService.getInstallProgress(installId);
        return ResultModel.success(progress);
    }

    @PostMapping("/{installId}/push")
    public ResultModel<InstallConfig> pushToParticipants(
            @PathVariable String installId,
            @RequestBody PushRequest request) {
        log.info("[pushToParticipants] Pushing to participants for: {}", installId);
        InstallConfig config = installService.pushToParticipants(installId, request);
        return ResultModel.success(config);
    }

    @GetMapping("/my")
    public ResultModel<List<InstallConfig>> listMyInstalls(@RequestParam String userId) {
        log.info("[listMyInstalls] Listing installs for user: {}", userId);
        List<InstallConfig> installs = installService.listMyInstalls(userId);
        return ResultModel.success(installs);
    }

    @GetMapping("/pending-activations")
    public ResultModel<List<InstallConfig>> listPendingActivations(@RequestParam String userId) {
        log.info("[listPendingActivations] Listing pending activations for user: {}", userId);
        List<InstallConfig> installs = installService.listPendingActivations(userId);
        return ResultModel.success(installs);
    }
}

package net.ooder.mvp.skill.scene.controller;

import net.ooder.mvp.skill.scene.model.ResultModel;
import net.ooder.mvp.skill.scene.service.DependencyAutoInstallService;
import net.ooder.mvp.skill.scene.service.DependencyAutoInstallService.AutoInstallResult;
import net.ooder.mvp.skill.scene.service.DependencyHealthCheckService;
import net.ooder.mvp.skill.scene.service.DependencyHealthCheckService.HealthCheckResult;
import net.ooder.mvp.skill.scene.service.DependencyHealthCheckService.DependencyStatus;
import net.ooder.mvp.skill.scene.template.SceneTemplate;
import net.ooder.mvp.skill.scene.template.SceneTemplateService;
import net.ooder.mvp.skill.scene.template.SceneTemplateService.DeployResult;
import net.ooder.mvp.skill.scene.template.SceneTemplateService.InstallResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/v1/templates")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SceneTemplateController {

    private static final Logger log = LoggerFactory.getLogger(SceneTemplateController.class);

    @Autowired
    private SceneTemplateService templateService;

    @Autowired
    private DependencyHealthCheckService dependencyHealthCheckService;

    @Autowired
    private DependencyAutoInstallService dependencyAutoInstallService;

    @GetMapping
    public ResultModel<List<SceneTemplate>> listTemplates() {
        log.info("[listTemplates] Listing all templates");
        List<SceneTemplate> templates = templateService.listTemplates();
        return ResultModel.success(templates);
    }

    @GetMapping("/{templateId}")
    public ResultModel<SceneTemplate> getTemplate(@PathVariable String templateId) {
        log.info("[getTemplate] Getting template: {}", templateId);
        SceneTemplate template = templateService.getTemplate(templateId);
        if (template == null) {
            return ResultModel.notFound("Template not found: " + templateId);
        }
        return ResultModel.success(template);
    }

    @PostMapping("/{templateId}/deploy")
    public ResultModel<DeployResult> deployTemplate(
            @PathVariable String templateId,
            @RequestBody(required = false) DeployRequest request) {
        
        log.info("[deployTemplate] Deploying template: {}", templateId);
        
        try {
            DeployResult result = templateService.deployTemplate(templateId);
            
            if (result.isSuccess()) {
                log.info("[deployTemplate] Template {} deployed successfully, sceneId: {}", 
                    templateId, result.getSceneId());
                return ResultModel.success(result);
            } else {
                log.warn("[deployTemplate] Failed to deploy template {}: {}", templateId, result.getMessage());
                return ResultModel.error(500, result.getMessage());
            }
        } catch (Exception e) {
            log.error("[deployTemplate] Error deploying template: {}", templateId, e);
            return ResultModel.error(500, "Failed to deploy template: " + e.getMessage());
        }
    }

    @GetMapping(value = "/{templateId}/deploy/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter deployTemplateWithProgress(@PathVariable String templateId) {
        log.info("[deployTemplateWithProgress] Starting SSE deployment for template: {}", templateId);
        return templateService.deployTemplateWithProgress(templateId);
    }

    @PostMapping("/{templateId}/install")
    public ResultModel<InstallResult> installTemplate(@PathVariable String templateId) {
        log.info("[installTemplate] Installing template dependencies: {}", templateId);
        
        try {
            InstallResult result = templateService.installTemplateDependencies(templateId);
            
            if (result.isSuccess()) {
                log.info("[installTemplate] Template {} dependencies installed successfully", templateId);
                return ResultModel.success(result);
            } else {
                log.warn("[installTemplate] Failed to install template {} dependencies: {}", templateId, result.getMessage());
                return ResultModel.error(500, result.getMessage());
            }
        } catch (Exception e) {
            log.error("[installTemplate] Error installing template dependencies: {}", templateId, e);
            return ResultModel.error(500, "Failed to install dependencies: " + e.getMessage());
        }
    }

    @GetMapping("/{templateId}/dependencies/health")
    public ResultModel<HealthCheckResult> checkDependenciesHealth(@PathVariable String templateId) {
        log.info("[checkDependenciesHealth] Checking dependencies health for template: {}", templateId);
        
        try {
            HealthCheckResult result = dependencyHealthCheckService.checkTemplateDependencies(templateId);
            return ResultModel.success(result);
        } catch (Exception e) {
            log.error("[checkDependenciesHealth] Error checking dependencies for template: {}", templateId, e);
            return ResultModel.error(500, "Failed to check dependencies: " + e.getMessage());
        }
    }

    @GetMapping("/{templateId}/dependencies/missing")
    public ResultModel<List<DependencyStatus>> getMissingDependencies(@PathVariable String templateId) {
        log.info("[getMissingDependencies] Getting missing dependencies for template: {}", templateId);
        
        try {
            List<DependencyStatus> missing = dependencyHealthCheckService.getMissingDependencies(templateId);
            return ResultModel.success(missing);
        } catch (Exception e) {
            log.error("[getMissingDependencies] Error getting missing dependencies for template: {}", templateId, e);
            return ResultModel.error(500, "Failed to get missing dependencies: " + e.getMessage());
        }
    }

    @PostMapping("/{templateId}/dependencies/auto-install")
    public ResultModel<AutoInstallResult> autoInstallDependencies(
            @PathVariable String templateId,
            @RequestParam(required = false, defaultValue = "false") boolean includeOptional) {
        log.info("[autoInstallDependencies] Auto installing dependencies for template: {}, includeOptional: {}", 
            templateId, includeOptional);
        
        try {
            AutoInstallResult result = dependencyAutoInstallService.autoInstallDependencies(templateId, includeOptional);
            return ResultModel.success(result);
        } catch (Exception e) {
            log.error("[autoInstallDependencies] Error auto installing dependencies for template: {}", templateId, e);
            return ResultModel.error(500, "Failed to auto install dependencies: " + e.getMessage());
        }
    }

    @PostMapping("/{templateId}/dependencies/install-missing")
    public ResultModel<AutoInstallResult> installMissingRequired(@PathVariable String templateId) {
        log.info("[installMissingRequired] Installing missing required dependencies for template: {}", templateId);
        
        try {
            AutoInstallResult result = dependencyAutoInstallService.installMissingRequired(templateId);
            return ResultModel.success(result);
        } catch (Exception e) {
            log.error("[installMissingRequired] Error installing missing required dependencies for template: {}", templateId, e);
            return ResultModel.error(500, "Failed to install missing required dependencies: " + e.getMessage());
        }
    }

    public static class DeployRequest {
        private String name;
        private java.util.Map<String, Object> overrides;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public java.util.Map<String, Object> getOverrides() { return overrides; }
        public void setOverrides(java.util.Map<String, Object> overrides) { this.overrides = overrides; }
    }
}

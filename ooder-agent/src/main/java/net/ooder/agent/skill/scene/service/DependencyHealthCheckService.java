package net.ooder.mvp.skill.scene.service;

import net.ooder.mvp.skill.scene.template.DependencyConfig;
import net.ooder.mvp.skill.scene.template.DependenciesConfig;
import net.ooder.mvp.skill.scene.template.SceneTemplate;
import net.ooder.mvp.skill.scene.template.SceneTemplateService;
import net.ooder.skills.api.SkillPackageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class DependencyHealthCheckService {

    private static final Logger log = LoggerFactory.getLogger(DependencyHealthCheckService.class);

    @Autowired(required = false)
    @Qualifier("skillPackageManager")
    private SkillPackageManager skillPackageManager;

    @Autowired
    private SceneTemplateService sceneTemplateService;

    public HealthCheckResult checkTemplateDependencies(String templateId) {
        log.info("[checkTemplateDependencies] Checking dependencies for template: {}", templateId);
        
        SceneTemplate template = sceneTemplateService.getTemplate(templateId);
        if (template == null) {
            HealthCheckResult result = new HealthCheckResult();
            result.setTemplateId(templateId);
            result.setSuccess(false);
            result.setMessage("Template not found: " + templateId);
            return result;
        }

        return checkDependencies(template);
    }

    public HealthCheckResult checkDependencies(SceneTemplate template) {
        HealthCheckResult result = new HealthCheckResult();
        result.setTemplateId(template.getId());
        result.setTemplateName(template.getName());

        DependenciesConfig depsConfig = template.getDependencies();
        if (depsConfig == null) {
            result.setSuccess(true);
            result.setMessage("No dependencies configured");
            return result;
        }

        List<DependencyStatus> allStatuses = new ArrayList<>();
        boolean allHealthy = true;

        List<DependencyConfig> requiredDeps = depsConfig.getRequired();
        if (requiredDeps != null) {
            for (DependencyConfig dep : requiredDeps) {
                DependencyStatus status = checkDependency(dep, true);
                allStatuses.add(status);
                if (!status.isHealthy()) {
                    allHealthy = false;
                }
            }
        }

        List<DependencyConfig> optionalDeps = depsConfig.getOptional();
        if (optionalDeps != null) {
            for (DependencyConfig dep : optionalDeps) {
                DependencyStatus status = checkDependency(dep, false);
                allStatuses.add(status);
            }
        }

        result.setDependencies(allStatuses);
        result.setSuccess(allHealthy);
        
        if (allHealthy) {
            result.setMessage("All dependencies are healthy");
        } else {
            long missingRequired = allStatuses.stream()
                .filter(s -> !s.isHealthy() && s.isRequired())
                .count();
            result.setMessage(missingRequired + " required dependencies are missing or unhealthy");
        }

        return result;
    }

    private DependencyStatus checkDependency(DependencyConfig dep, boolean required) {
        DependencyStatus status = new DependencyStatus();
        status.setSkillId(dep.getSkillId());
        status.setRequired(required);
        status.setVersion(dep.getVersion());
        status.setAutoInstall(dep.isAutoInstall());
        status.setDescription(dep.getDescription());

        try {
            if (skillPackageManager != null) {
                CompletableFuture<Boolean> installedFuture = skillPackageManager.isInstalled(dep.getSkillId());
                boolean installed = installedFuture.get();
                
                status.setInstalled(installed);
                status.setHealthy(installed);
                
                if (installed) {
                    status.setStatus("INSTALLED");
                    status.setMessage("Dependency is installed and healthy");
                } else {
                    status.setStatus("MISSING");
                    status.setMessage("Dependency is not installed");
                }
            } else {
                status.setInstalled(false);
                status.setHealthy(false);
                status.setStatus("UNKNOWN");
                status.setMessage("SkillPackageManager not available");
            }
        } catch (Exception e) {
            log.error("[checkDependency] Error checking dependency: {}", dep.getSkillId(), e);
            status.setInstalled(false);
            status.setHealthy(false);
            status.setStatus("ERROR");
            status.setMessage("Error checking dependency: " + e.getMessage());
        }

        return status;
    }

    public List<DependencyStatus> getMissingDependencies(String templateId) {
        HealthCheckResult result = checkTemplateDependencies(templateId);
        List<DependencyStatus> missing = new ArrayList<>();
        
        for (DependencyStatus status : result.getDependencies()) {
            if (!status.isHealthy() && status.isRequired()) {
                missing.add(status);
            }
        }
        
        return missing;
    }

    public boolean hasMissingRequiredDependencies(String templateId) {
        HealthCheckResult result = checkTemplateDependencies(templateId);
        for (DependencyStatus status : result.getDependencies()) {
            if (!status.isHealthy() && status.isRequired()) {
                return true;
            }
        }
        return false;
    }

    public static class HealthCheckResult {
        private String templateId;
        private String templateName;
        private boolean success;
        private String message;
        private List<DependencyStatus> dependencies;

        public String getTemplateId() { return templateId; }
        public void setTemplateId(String templateId) { this.templateId = templateId; }
        public String getTemplateName() { return templateName; }
        public void setTemplateName(String templateName) { this.templateName = templateName; }
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public List<DependencyStatus> getDependencies() { return dependencies != null ? dependencies : new ArrayList<>(); }
        public void setDependencies(List<DependencyStatus> dependencies) { this.dependencies = dependencies; }
    }

    public static class DependencyStatus {
        private String skillId;
        private String version;
        private boolean required;
        private boolean installed;
        private boolean healthy;
        private boolean autoInstall;
        private String status;
        private String message;
        private String description;

        public String getSkillId() { return skillId; }
        public void setSkillId(String skillId) { this.skillId = skillId; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public boolean isRequired() { return required; }
        public void setRequired(boolean required) { this.required = required; }
        public boolean isInstalled() { return installed; }
        public void setInstalled(boolean installed) { this.installed = installed; }
        public boolean isHealthy() { return healthy; }
        public void setHealthy(boolean healthy) { this.healthy = healthy; }
        public boolean isAutoInstall() { return autoInstall; }
        public void setAutoInstall(boolean autoInstall) { this.autoInstall = autoInstall; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}

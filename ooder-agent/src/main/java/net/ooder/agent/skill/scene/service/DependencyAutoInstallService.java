package net.ooder.mvp.skill.scene.service;

import net.ooder.mvp.skill.scene.template.DependencyConfig;
import net.ooder.mvp.skill.scene.template.DependenciesConfig;
import net.ooder.mvp.skill.scene.template.SceneTemplate;
import net.ooder.mvp.skill.scene.template.SceneTemplateService;
import net.ooder.skills.api.SkillPackageManager;
import net.ooder.skills.api.InstallRequest.InstallMode;
import net.ooder.skills.api.InstallResultWithDependencies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class DependencyAutoInstallService {

    private static final Logger log = LoggerFactory.getLogger(DependencyAutoInstallService.class);

    @Autowired(required = false)
    @Qualifier("skillPackageManager")
    private SkillPackageManager skillPackageManager;

    @Autowired
    private SceneTemplateService sceneTemplateService;

    @Autowired
    private DependencyHealthCheckService healthCheckService;

    public AutoInstallResult autoInstallDependencies(String templateId) {
        return autoInstallDependencies(templateId, false);
    }

    public AutoInstallResult autoInstallDependencies(String templateId, boolean includeOptional) {
        log.info("[autoInstallDependencies] Auto installing dependencies for template: {}, includeOptional: {}", 
            templateId, includeOptional);
        
        SceneTemplate template = sceneTemplateService.getTemplate(templateId);
        if (template == null) {
            AutoInstallResult result = new AutoInstallResult();
            result.setTemplateId(templateId);
            result.setSuccess(false);
            result.setMessage("Template not found: " + templateId);
            return result;
        }

        return autoInstallDependenciesForTemplate(template, includeOptional);
    }

    public AutoInstallResult autoInstallDependenciesForTemplate(SceneTemplate template, boolean includeOptional) {
        AutoInstallResult result = new AutoInstallResult();
        result.setTemplateId(template.getId());
        result.setTemplateName(template.getName());

        DependenciesConfig depsConfig = template.getDependencies();
        if (depsConfig == null) {
            result.setSuccess(true);
            result.setMessage("No dependencies configured");
            return result;
        }

        if (skillPackageManager == null) {
            result.setSuccess(false);
            result.setMessage("SkillPackageManager not available");
            return result;
        }

        List<InstallRecord> installed = new ArrayList<>();
        List<InstallRecord> skipped = new ArrayList<>();
        List<InstallRecord> failed = new ArrayList<>();

        List<DependencyConfig> toInstall = new ArrayList<>();
        
        List<DependencyConfig> requiredDeps = depsConfig.getRequired();
        if (requiredDeps != null) {
            for (DependencyConfig dep : requiredDeps) {
                if (dep.isAutoInstall()) {
                    toInstall.add(dep);
                }
            }
        }

        if (includeOptional) {
            List<DependencyConfig> optionalDeps = depsConfig.getOptional();
            if (optionalDeps != null) {
                for (DependencyConfig dep : optionalDeps) {
                    if (dep.isAutoInstall()) {
                        toInstall.add(dep);
                    }
                }
            }
        }

        for (DependencyConfig dep : toInstall) {
            InstallRecord record = installDependency(dep);
            if ("INSTALLED".equals(record.getStatus())) {
                installed.add(record);
            } else if ("SKIPPED".equals(record.getStatus())) {
                skipped.add(record);
            } else {
                failed.add(record);
            }
        }

        result.setInstalled(installed);
        result.setSkipped(skipped);
        result.setFailed(failed);
        
        if (failed.isEmpty()) {
            result.setSuccess(true);
            result.setMessage("All auto-install dependencies processed successfully");
        } else {
            result.setSuccess(false);
            result.setMessage(failed.size() + " dependencies failed to install");
        }

        return result;
    }

    private InstallRecord installDependency(DependencyConfig dep) {
        InstallRecord record = new InstallRecord();
        record.setSkillId(dep.getSkillId());
        record.setVersion(dep.getVersion());
        record.setDescription(dep.getDescription());

        try {
            CompletableFuture<Boolean> installedFuture = skillPackageManager.isInstalled(dep.getSkillId());
            boolean alreadyInstalled = installedFuture.get();
            
            if (alreadyInstalled) {
                record.setStatus("SKIPPED");
                record.setMessage("Already installed");
                log.info("[installDependency] Dependency {} already installed", dep.getSkillId());
                return record;
            }

            log.info("[installDependency] Installing dependency: {}", dep.getSkillId());
            
            CompletableFuture<InstallResultWithDependencies> installFuture = skillPackageManager.installWithDependencies(
                dep.getSkillId(), 
                InstallMode.FULL_INSTALL
            );
            
            InstallResultWithDependencies installResult = installFuture.get();
            boolean success = installResult != null && installResult.isSuccess();
            
            if (success) {
                record.setStatus("INSTALLED");
                record.setMessage("Successfully installed");
                log.info("[installDependency] Successfully installed: {}", dep.getSkillId());
            } else {
                record.setStatus("FAILED");
                record.setMessage("Installation returned false");
                log.warn("[installDependency] Installation returned false for: {}", dep.getSkillId());
            }
        } catch (Exception e) {
            log.error("[installDependency] Failed to install dependency: {}", dep.getSkillId(), e);
            record.setStatus("FAILED");
            record.setMessage("Error: " + e.getMessage());
        }

        return record;
    }

    public AutoInstallResult installMissingRequired(String templateId) {
        log.info("[installMissingRequired] Installing missing required dependencies for template: {}", templateId);
        
        List<DependencyHealthCheckService.DependencyStatus> missing = 
            healthCheckService.getMissingDependencies(templateId);
        
        if (missing.isEmpty()) {
            AutoInstallResult result = new AutoInstallResult();
            result.setTemplateId(templateId);
            result.setSuccess(true);
            result.setMessage("No missing required dependencies");
            return result;
        }

        SceneTemplate template = sceneTemplateService.getTemplate(templateId);
        if (template == null) {
            AutoInstallResult result = new AutoInstallResult();
            result.setTemplateId(templateId);
            result.setSuccess(false);
            result.setMessage("Template not found");
            return result;
        }

        DependenciesConfig depsConfig = template.getDependencies();
        List<DependencyConfig> toInstall = new ArrayList<>();
        
        if (depsConfig != null && depsConfig.getRequired() != null) {
            for (DependencyConfig dep : depsConfig.getRequired()) {
                for (DependencyHealthCheckService.DependencyStatus status : missing) {
                    if (status.getSkillId().equals(dep.getSkillId())) {
                        toInstall.add(dep);
                        break;
                    }
                }
            }
        }

        AutoInstallResult result = new AutoInstallResult();
        result.setTemplateId(templateId);
        result.setTemplateName(template.getName());

        if (skillPackageManager == null) {
            result.setSuccess(false);
            result.setMessage("SkillPackageManager not available");
            return result;
        }

        List<InstallRecord> installed = new ArrayList<>();
        List<InstallRecord> skipped = new ArrayList<>();
        List<InstallRecord> failed = new ArrayList<>();

        for (DependencyConfig dep : toInstall) {
            InstallRecord record = installDependency(dep);
            if ("INSTALLED".equals(record.getStatus())) {
                installed.add(record);
            } else if ("SKIPPED".equals(record.getStatus())) {
                skipped.add(record);
            } else {
                failed.add(record);
            }
        }

        result.setInstalled(installed);
        result.setSkipped(skipped);
        result.setFailed(failed);
        
        if (failed.isEmpty()) {
            result.setSuccess(true);
            result.setMessage("All missing required dependencies installed");
        } else {
            result.setSuccess(false);
            result.setMessage(failed.size() + " required dependencies failed to install");
        }

        return result;
    }

    public static class AutoInstallResult {
        private String templateId;
        private String templateName;
        private boolean success;
        private String message;
        private List<InstallRecord> installed;
        private List<InstallRecord> skipped;
        private List<InstallRecord> failed;

        public String getTemplateId() { return templateId; }
        public void setTemplateId(String templateId) { this.templateId = templateId; }
        public String getTemplateName() { return templateName; }
        public void setTemplateName(String templateName) { this.templateName = templateName; }
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public List<InstallRecord> getInstalled() { return installed != null ? installed : new ArrayList<>(); }
        public void setInstalled(List<InstallRecord> installed) { this.installed = installed; }
        public List<InstallRecord> getSkipped() { return skipped != null ? skipped : new ArrayList<>(); }
        public void setSkipped(List<InstallRecord> skipped) { this.skipped = skipped; }
        public List<InstallRecord> getFailed() { return failed != null ? failed : new ArrayList<>(); }
        public void setFailed(List<InstallRecord> failed) { this.failed = failed; }
    }

    public static class InstallRecord {
        private String skillId;
        private String version;
        private String status;
        private String message;
        private String description;

        public String getSkillId() { return skillId; }
        public void setSkillId(String skillId) { this.skillId = skillId; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}

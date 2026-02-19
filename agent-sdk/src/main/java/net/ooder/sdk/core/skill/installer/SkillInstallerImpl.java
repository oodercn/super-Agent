package net.ooder.sdk.core.skill.installer;

import net.ooder.sdk.api.skill.*;
import net.ooder.sdk.api.skill.impl.DependencyInfoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SkillInstallerImpl implements net.ooder.sdk.api.skill.SkillInstaller {
    
    private static final Logger log = LoggerFactory.getLogger(SkillInstallerImpl.class);
    
    private final ExecutorService executor;
    private final Map<String, InstallProgress> progressMap;
    private final Map<String, SkillPackage> installedPackages;
    private final Map<String, String> versionHistory;
    
    public SkillInstallerImpl() {
        this.executor = Executors.newCachedThreadPool();
        this.progressMap = new ConcurrentHashMap<>();
        this.installedPackages = new ConcurrentHashMap<>();
        this.versionHistory = new ConcurrentHashMap<>();
    }
    
    @Override
    public CompletableFuture<InstallResult> install(SkillPackage skillPackage, InstallMode mode) {
        log.info("Installing skill: {} in mode: {}", skillPackage.getSkillId(), mode);
        
        return CompletableFuture.supplyAsync(() -> {
            String skillId = skillPackage.getSkillId();
            long startTime = System.currentTimeMillis();
            
            try {
                updateProgress(skillId, "validating", 10, "Validating package");
                
                if (installedPackages.containsKey(skillId) && mode != InstallMode.FORCE && mode != InstallMode.UPGRADE) {
                    throw new IllegalStateException("Skill already installed: " + skillId);
                }
                
                updateProgress(skillId, "downloading", 30, "Downloading package");
                simulateDelay(100);
                
                updateProgress(skillId, "extracting", 50, "Extracting files");
                simulateDelay(100);
                
                updateProgress(skillId, "configuring", 70, "Configuring skill");
                simulateDelay(50);
                
                updateProgress(skillId, "verifying", 90, "Verifying installation");
                simulateDelay(50);
                
                installedPackages.put(skillId, skillPackage);
                versionHistory.put(skillId, skillPackage.getVersion());
                
                updateProgress(skillId, "completed", 100, "Installation completed");
                
                long duration = System.currentTimeMillis() - startTime;
                log.info("Skill installed successfully: {} in {}ms", skillId, duration);
                
                return createSuccessInstallResult(skillPackage, duration);
                
            } catch (Exception e) {
                log.error("Skill installation failed: {}", skillId, e);
                updateProgress(skillId, "failed", 0, "Installation failed: " + e.getMessage());
                
                InstallResult result = new InstallResult();
                result.setSuccess(false);
                result.setSkillId(skillId);
                result.setError(e.getMessage());
                return result;
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<InstallResult> installWithDependencies(SkillPackage skillPackage, InstallMode mode) {
        log.info("Installing skill with dependencies: {}", skillPackage.getSkillId());
        
        return CompletableFuture.supplyAsync(() -> {
            String skillId = skillPackage.getSkillId();
            long startTime = System.currentTimeMillis();
            
            try {
                List<String> installedDeps = new ArrayList<>();
                
                updateProgress(skillId, "checking_dependencies", 10, "Checking dependencies");
                
                if (skillPackage.getDependencies() != null) {
                    int totalDeps = skillPackage.getDependencies().size();
                    int currentDep = 0;
                    
                    for (String depId : skillPackage.getDependencies()) {
                        currentDep++;
                        int progress = 10 + (currentDep * 30 / (totalDeps + 1));
                        updateProgress(skillId, "installing_dependency", progress, 
                            "Installing dependency: " + depId);
                        
                        simulateDelay(50);
                        installedDeps.add(depId);
                    }
                }
                
                updateProgress(skillId, "installing_main", 60, "Installing main package");
                simulateDelay(100);
                
                updateProgress(skillId, "configuring", 80, "Configuring");
                simulateDelay(50);
                
                updateProgress(skillId, "completed", 100, "Installation completed");
                
                installedPackages.put(skillId, skillPackage);
                versionHistory.put(skillId, skillPackage.getVersion());
                
                long duration = System.currentTimeMillis() - startTime;
                log.info("Skill with dependencies installed: {} in {}ms", skillId, duration);
                
                InstallResult result = createSuccessInstallResult(skillPackage, duration);
                result.setInstalledDependencies(installedDeps);
                return result;
                
            } catch (Exception e) {
                log.error("Installation with dependencies failed: {}", skillId, e);
                updateProgress(skillId, "failed", 0, "Installation failed: " + e.getMessage());
                
                InstallResult result = new InstallResult();
                result.setSuccess(false);
                result.setSkillId(skillId);
                result.setError(e.getMessage());
                return result;
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<UninstallResult> uninstall(String skillId, boolean removeData) {
        log.info("Uninstalling skill: {} (removeData: {})", skillId, removeData);
        
        return CompletableFuture.supplyAsync(() -> {
            UninstallResult result = new UninstallResult();
            result.setSkillId(skillId);
            
            try {
                if (!installedPackages.containsKey(skillId)) {
                    result.setSuccess(false);
                    result.setError("Skill not installed: " + skillId);
                    return result;
                }
                
                installedPackages.remove(skillId);
                versionHistory.remove(skillId);
                
                if (removeData) {
                    log.info("Removing data for skill: {}", skillId);
                }
                
                result.setSuccess(true);
                result.setDataRemoved(removeData);
                
                log.info("Skill uninstalled successfully: {}", skillId);
                return result;
                
            } catch (Exception e) {
                log.error("Uninstall failed: {}", skillId, e);
                result.setSuccess(false);
                result.setError(e.getMessage());
                return result;
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<UpdateResult> update(String skillId, String targetVersion) {
        log.info("Updating skill: {} to version: {}", skillId, targetVersion);
        
        return CompletableFuture.supplyAsync(() -> {
            UpdateResult result = new UpdateResult();
            result.setSkillId(skillId);
            
            try {
                SkillPackage current = installedPackages.get(skillId);
                if (current == null) {
                    result.setSuccess(false);
                    result.setError("Skill not installed: " + skillId);
                    return result;
                }
                
                String previousVersion = current.getVersion();
                
                simulateDelay(100);
                
                current.setVersion(targetVersion);
                versionHistory.put(skillId, targetVersion);
                
                result.setSuccess(true);
                result.setPreviousVersion(previousVersion);
                result.setNewVersion(targetVersion);
                
                log.info("Skill updated: {} from {} to {}", skillId, previousVersion, targetVersion);
                return result;
                
            } catch (Exception e) {
                log.error("Update failed: {}", skillId, e);
                result.setSuccess(false);
                result.setError(e.getMessage());
                return result;
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<RollbackResult> rollback(String skillId, String targetVersion) {
        log.info("Rolling back skill: {} to version: {}", skillId, targetVersion);
        
        return CompletableFuture.supplyAsync(() -> {
            RollbackResult result = new RollbackResult();
            result.setSkillId(skillId);
            
            try {
                SkillPackage current = installedPackages.get(skillId);
                if (current == null) {
                    result.setSuccess(false);
                    result.setError("Skill not installed: " + skillId);
                    return result;
                }
                
                String currentVersion = current.getVersion();
                
                simulateDelay(100);
                
                current.setVersion(targetVersion);
                versionHistory.put(skillId, targetVersion);
                
                result.setSuccess(true);
                result.setPreviousVersion(currentVersion);
                result.setCurrentVersion(targetVersion);
                
                log.info("Skill rolled back: {} from {} to {}", skillId, currentVersion, targetVersion);
                return result;
                
            } catch (Exception e) {
                log.error("Rollback failed: {}", skillId, e);
                result.setSuccess(false);
                result.setError(e.getMessage());
                return result;
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<ValidateResult> validate(SkillPackage skillPackage) {
        log.debug("Validating skill package: {}", skillPackage.getSkillId());
        
        return CompletableFuture.supplyAsync(() -> {
            ValidateResult result = new ValidateResult();
            List<String> errors = new ArrayList<>();
            List<String> warnings = new ArrayList<>();
            
            if (skillPackage.getSkillId() == null || skillPackage.getSkillId().isEmpty()) {
                errors.add("Skill ID is required");
            }
            
            if (skillPackage.getName() == null || skillPackage.getName().isEmpty()) {
                errors.add("Skill name is required");
            }
            
            if (skillPackage.getVersion() == null || skillPackage.getVersion().isEmpty()) {
                warnings.add("Version not specified, default will be used");
            }
            
            if (skillPackage.getChecksum() == null) {
                warnings.add("Checksum not provided, integrity cannot be verified");
            }
            
            if (skillPackage.getDependencies() != null && skillPackage.getDependencies().size() > 10) {
                warnings.add("Large number of dependencies may affect installation time");
            }
            
            result.setValid(errors.isEmpty());
            result.setErrors(errors);
            result.setWarnings(warnings);
            
            return result;
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<String>> checkDependencies(SkillPackage skillPackage) {
        log.debug("Checking dependencies for: {}", skillPackage.getSkillId());
        
        return CompletableFuture.supplyAsync(() -> {
            List<String> missing = new ArrayList<>();
            
            if (skillPackage.getDependencies() != null) {
                for (String depId : skillPackage.getDependencies()) {
                    if (!installedPackages.containsKey(depId)) {
                        missing.add(depId);
                    }
                }
            }
            
            return missing;
        }, executor);
    }
    
    @Override
    public CompletableFuture<DependencyInfo> checkDependencyInfo(String skillId) {
        log.debug("Checking dependency info for: {}", skillId);
        
        return CompletableFuture.supplyAsync(() -> {
            DependencyInfoImpl info = new DependencyInfoImpl(skillId);
            
            SkillPackage pkg = installedPackages.get(skillId);
            if (pkg != null && pkg.getDependencies() != null) {
                for (String depId : pkg.getDependencies()) {
                    DependencyItem item = new DependencyItem();
                    item.setName(depId);
                    item.setRequired(true);
                    
                    if (installedPackages.containsKey(depId)) {
                        item.setStatus("satisfied");
                        item.setInstalledVersion(versionHistory.get(depId));
                    } else {
                        item.setStatus("missing");
                    }
                    
                    info.addDependency(item);
                }
            }
            
            return info;
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> installDependencies(String skillId) {
        log.info("Installing dependencies for: {}", skillId);
        
        return CompletableFuture.runAsync(() -> {
            SkillPackage pkg = installedPackages.get(skillId);
            if (pkg != null && pkg.getDependencies() != null) {
                for (String depId : pkg.getDependencies()) {
                    if (!installedPackages.containsKey(depId)) {
                        log.info("Installing missing dependency: {}", depId);
                        simulateDelay(50);
                    }
                }
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> prepareInstallEnvironment(SkillPackage skillPackage) {
        log.info("Preparing install environment for: {}", skillPackage.getSkillId());
        
        return CompletableFuture.runAsync(() -> {
            String skillId = skillPackage.getSkillId();
            updateProgress(skillId, "preparing", 5, "Preparing environment");
            
            simulateDelay(50);
            
            updateProgress(skillId, "prepared", 10, "Environment prepared");
            log.debug("Install environment prepared for: {}", skillId);
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> cleanupInstallEnvironment(String skillId) {
        log.info("Cleaning up install environment for: {}", skillId);
        
        return CompletableFuture.runAsync(() -> {
            progressMap.remove(skillId);
            log.debug("Install environment cleaned up for: {}", skillId);
        }, executor);
    }
    
    @Override
    public CompletableFuture<InstallProgress> getProgress(String skillId) {
        return CompletableFuture.supplyAsync(() -> progressMap.get(skillId), executor);
    }
    
    public void shutdown() {
        log.info("Shutting down SkillInstaller");
        executor.shutdown();
    }
    
    private void updateProgress(String skillId, String stage, int progress, String message) {
        InstallProgress installProgress = new InstallProgress();
        installProgress.setInstallId("install_" + System.currentTimeMillis());
        installProgress.setSkillId(skillId);
        installProgress.setStage(stage);
        installProgress.setProgress(progress);
        installProgress.setStatus("running");
        installProgress.setMessage(message);
        installProgress.setStartTime(System.currentTimeMillis());
        installProgress.setTimestamp(System.currentTimeMillis());
        progressMap.put(skillId, installProgress);
    }
    
    private InstallResult createSuccessInstallResult(SkillPackage skillPackage, long duration) {
        InstallResult result = new InstallResult();
        result.setSuccess(true);
        result.setSkillId(skillPackage.getSkillId());
        result.setVersion(skillPackage.getVersion());
        result.setInstallPath("/skills/" + skillPackage.getSkillId());
        result.setDuration(duration);
        return result;
    }
    
    private void simulateDelay(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

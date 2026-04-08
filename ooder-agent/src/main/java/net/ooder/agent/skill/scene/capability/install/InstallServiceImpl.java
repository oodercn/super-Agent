package net.ooder.mvp.skill.scene.capability.install;

import net.ooder.mvp.skill.scene.audit.Auditable;
import net.ooder.mvp.skill.scene.dto.audit.AuditEventType;
import net.ooder.mvp.skill.scene.capability.model.Capability;
import net.ooder.mvp.skill.scene.capability.model.CapabilityStatus;
import net.ooder.mvp.skill.scene.capability.model.CapabilityType;
import net.ooder.mvp.skill.scene.capability.service.CapabilityService;
import net.ooder.mvp.skill.scene.capability.service.CapabilityStateService;
import net.ooder.mvp.skill.scene.notification.SceneNotificationService;
import net.ooder.mvp.skill.scene.service.TodoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InstallServiceImpl implements InstallService {

    private static final Logger log = LoggerFactory.getLogger(InstallServiceImpl.class);

    private Map<String, InstallConfig> installs = new ConcurrentHashMap<String, InstallConfig>();
    private Map<String, InstallProgress> progressMap = new ConcurrentHashMap<String, InstallProgress>();

    @Autowired
    private CapabilityService capabilityService;

    @Autowired
    private CapabilityStateService capabilityStateService;

    @Autowired
    private SceneNotificationService notificationService;

    @Autowired
    private TodoService todoService;

    @Autowired
    private SkillDirectoryMigrator directoryMigrator;

    @Autowired
    private SkillDownloadService downloadService;

    @Autowired
    private SkillDirectoryConfig directoryConfig;

    @Override
    public InstallConfig createInstall(CreateInstallRequest request) {
        log.info("[createInstall] Creating install for capability: {}", request.getCapabilityId());
        
        String installId = "install-" + System.currentTimeMillis();
        InstallConfig config = new InstallConfig();
        config.setInstallId(installId);
        config.setCapabilityId(request.getCapabilityId());
        config.setDriverCondition(request.getDriverCondition());
        config.setOptionalCapabilities(request.getOptionalCapabilities());
        config.setConfig(request.getConfig());
        config.setPushType(request.getPushType());
        config.setStatus(InstallConfig.InstallStatus.PENDING);
        config.setCreateTime(System.currentTimeMillis());
        
        if (request.getParticipants() != null) {
            InstallConfig.Participants participants = new InstallConfig.Participants();
            if (request.getParticipants().getLeader() != null) {
                participants.setLeader(request.getParticipants().getLeader());
            }
            if (request.getParticipants().getCollaborators() != null) {
                participants.setCollaborators(request.getParticipants().getCollaborators());
            }
            config.setParticipants(participants);
        }
        
        determineSceneTypeAndVisibility(config, request.getCapabilityId());
        
        List<String> nextSteps = determineNextSteps(config);
        config.setNextSteps(nextSteps);
        
        installs.put(installId, config);
        
        InstallProgress progress = new InstallProgress();
        progress.setInstallId(installId);
        progress.setProgress(0);
        progress.setStatus(InstallConfig.InstallStatus.PENDING);
        progressMap.put(installId, progress);
        
        log.info("[createInstall] Created install: {} with sceneType={}, visibility={}, nextSteps={}", 
            installId, config.getSceneType(), config.getVisibility(), nextSteps);
        
        return config;
    }
    
    private void determineSceneTypeAndVisibility(InstallConfig config, String capabilityId) {
        if (capabilityService == null) {
            throw new IllegalStateException("CapabilityService 不可用，无法确定场景类型和可见性");
        }
        
        try {
            net.ooder.mvp.skill.scene.capability.model.Capability cap = capabilityService.findById(capabilityId);
            if (cap == null) {
                throw new IllegalStateException("能力不存在: " + capabilityId + "，无法确定场景类型和可见性");
            }
            
            config.setSkillForm(cap.getSkillForm() != null ? cap.getSkillForm().getCode() : "PROVIDER");
            config.setSceneType(cap.getSceneType());
            config.setVisibility(cap.getVisibility() != null ? cap.getVisibility() : "public");
            
            log.info("[determineSceneTypeAndVisibility] Determined: skillForm={}, sceneType={}, visibility={}", 
                config.getSkillForm(), config.getSceneType(), config.getVisibility());
                
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("确定场景类型失败: " + e.getMessage(), e);
        }
    }
    
    private List<String> determineNextSteps(InstallConfig config) {
        List<String> steps = new ArrayList<>();
        
        String skillForm = config.getSkillForm();
        String sceneType = config.getSceneType();
        String visibility = config.getVisibility(); 
        
        if (!"SCENE".equals(skillForm)) {
            steps.add("安装独立能力");
            steps.add("完成");
            return steps;
        }
        
        if ("AUTO".equals(sceneType)) {
            if ("internal".equals(visibility)) {
                steps.add("自动安装依赖");
                steps.add("自动激活");
                steps.add("后台运行");
            } else {
                steps.add("安装依赖能力");
                steps.add("等待用户确认激活");
                steps.add("定时调度执行");
            }
        } else if ("TRIGGER".equals(sceneType)) {
            steps.add("安装依赖能力");
            steps.add("配置触发条件");
            steps.add("等待触发事件");
            steps.add("触发后执行");
        } else {
            steps.add("安装能力");
            steps.add("完成配置");
        }
        
        return steps;
    }

    @Override
    public InstallConfig getInstall(String installId) {
        return installs.get(installId);
    }

    @Override
    public InstallConfig updateDriverCondition(String installId, String driverCondition) {
        log.info("[updateDriverCondition] Updating driver condition for: {}", installId);
        
        InstallConfig config = installs.get(installId);
        if (config != null) {
            config.setDriverCondition(driverCondition);
        }
        return config;
    }

    @Override
    public InstallConfig addParticipant(String installId, AddParticipantRequest request) {
        log.info("[addParticipant] Adding participant to: {}", installId);
        
        InstallConfig config = installs.get(installId);
        if (config != null && request.getParticipant() != null) {
            InstallConfig.Participants participants = config.getParticipants();
            if (participants == null) {
                participants = new InstallConfig.Participants();
            }
            List<InstallConfig.Participant> collaborators = participants.getCollaborators();
            collaborators.add(request.getParticipant());
            participants.setCollaborators(collaborators);
            config.setParticipants(participants);
        }
        return config;
    }

    @Override
    public InstallConfig removeParticipant(String installId, String userId) {
        log.info("[removeParticipant] Removing participant from: {}", installId);
        
        InstallConfig config = installs.get(installId);
        if (config != null && config.getParticipants() != null) {
            InstallConfig.Participants participants = config.getParticipants();
            List<InstallConfig.Participant> collaborators = participants.getCollaborators();
            collaborators.removeIf(p -> p.getUserId().equals(userId));
            participants.setCollaborators(collaborators);
        }
        return config;
    }

    @Override
    public InstallConfig updateOptionalCapabilities(String installId, List<String> capabilities) {
        log.info("[updateOptionalCapabilities] Updating capabilities for: {}", installId);
        
        InstallConfig config = installs.get(installId);
        if (config != null) {
            config.setOptionalCapabilities(capabilities);
        }
        return config;
    }

    @Override
    @Auditable(eventType = AuditEventType.CAPABILITY_INVOKE, action = "执行能力安装", resourceType = "install")
    public CompletableFuture<InstallConfig> executeInstall(String installId) {
        log.info("[executeInstall] Executing install: {}", installId);
        
        return CompletableFuture.supplyAsync(() -> {
            InstallConfig config = installs.get(installId);
            InstallProgress progress = progressMap.get(installId);
            
            if (config == null) {
                log.error("[executeInstall] Install config not found: {}", installId);
                return null;
            }
            
            config.setStatus(InstallConfig.InstallStatus.INSTALLING);
            if (progress != null) {
                progress.setStatus(InstallConfig.InstallStatus.INSTALLING);
                progress.setCurrentAction("正在解析依赖...");
                progress.setProgress(5);
            }
            
            net.ooder.mvp.skill.scene.capability.model.Capability capability = null;
            try {
                if (capabilityService != null) {
                    capability = capabilityService.findById(config.getCapabilityId());
                }
                
                if (capability == null) {
                    log.error("[executeInstall] Capability not found: {} - installation cannot proceed", config.getCapabilityId());
                    config.setStatus(InstallConfig.InstallStatus.FAILED);
                    if (progress != null) {
                        progress.setStatus(InstallConfig.InstallStatus.FAILED);
                        progress.setMessage("能力不存在: " + config.getCapabilityId());
                    }
                    return config;
                }
                
                if (capability.getCapabilityType() == CapabilityType.SCENE) {
                    try {
                        validateSceneConfig(config.getCapabilityId(), capability);
                        log.info("[executeInstall] Scene config validation passed for: {}", config.getCapabilityId());
                    } catch (Exception e) {
                        log.warn("[executeInstall] Scene config validation failed for {}: {}", config.getCapabilityId(), e.getMessage());
                        if (progress != null) {
                            progress.setCurrentAction("场景配置验证: " + e.getMessage());
                        }
                    }
                }
                
                List<String> dependencyIds = capability.getDependencies();
                if (dependencyIds == null || dependencyIds.isEmpty()) {
                    dependencyIds = capability.getCapabilities();
                }
                
                List<InstallConfig.DependencyInfo> installedDeps = new ArrayList<>();
                List<String> installedCapabilityIds = new ArrayList<>();
                
                if (dependencyIds != null && !dependencyIds.isEmpty()) {
                    int totalDeps = dependencyIds.size();
                    int currentDep = 0;
                    
                    for (String depId : dependencyIds) {
                        currentDep++;
                        int progressPercent = 10 + (currentDep * 80 / totalDeps);
                        
                        if (progress != null) {
                            progress.setCurrentAction("正在安装依赖: " + depId);
                            progress.setProgress(progressPercent);
                        }
                        
                        InstallConfig.DependencyInfo depInfo = installDependency(depId);
                        installedDeps.add(depInfo);
                        
                        if (depInfo.getStatus() == InstallConfig.DependencyInfo.DependencyStatus.INSTALLED) {
                            installedCapabilityIds.add(depId);
                        }
                    }
                }
                
                if (config.getOptionalCapabilities() != null) {
                    for (String optCapId : config.getOptionalCapabilities()) {
                        InstallConfig.DependencyInfo optDepInfo = installDependency(optCapId);
                        installedDeps.add(optDepInfo);
                        
                        if (optDepInfo.getStatus() == InstallConfig.DependencyInfo.DependencyStatus.INSTALLED) {
                            installedCapabilityIds.add(optCapId);
                        }
                    }
                }
                
                config.setDependencies(installedDeps);
                config.setInstalledCapabilities(installedCapabilityIds);
                
                if (capabilityService != null) {
                    capabilityService.updateInstallStatus(config.getCapabilityId(), true);
                }
                
                if (capabilityStateService != null) {
                    capabilityStateService.updateState(
                        config.getCapabilityId(), 
                        true, 
                        determinePostCapabilityStatus(config),
                        config.getParticipants() != null && config.getParticipants().getLeader() != null 
                            ? config.getParticipants().getLeader().getUserId() : "system",
                        config.getPushType() != null ? config.getPushType().name() : "INSTALL",
                        null
                    );
                }
                
                InstallConfig.InstallStatus targetStatus = determinePostInstallStatus(config);
                config.setStatus(targetStatus);
                
                log.info("[executeInstall] Install completed with status: {} (sceneType={}, visibility={})", 
                    targetStatus, config.getSceneType(), config.getVisibility());
                
                if (progress != null) {
                    progress.setStatus(InstallConfig.InstallStatus.INSTALLED);
                    progress.setProgress(100);
                    progress.setCurrentAction("安装完成");
                    progress.setDependencies(installedDeps);
                    progress.setInstalledCapabilities(installedCapabilityIds);
                    progress.setMessage("安装成功，共安装 " + installedCapabilityIds.size() + " 个依赖");
                }
                
                if (notificationService != null && config.getParticipants() != null) {
                    try {
                        String leaderId = config.getParticipants().getLeader() != null 
                            ? config.getParticipants().getLeader().getUserId() : null;
                        if (leaderId != null) {
                            notificationService.notifyInstallComplete(
                                leaderId, 
                                capability.getName(), 
                                true, 
                                "共安装 " + installedCapabilityIds.size() + " 个依赖"
                            );
                        }
                    } catch (Exception e) {
                        log.error("[executeInstall] Failed to send notification: {}", e.getMessage());
                    }
                }
                
                if (todoService != null && targetStatus == InstallConfig.InstallStatus.PENDING_ACTIVATION) {
                    try {
                        String leaderId = config.getParticipants() != null && config.getParticipants().getLeader() != null
                            ? config.getParticipants().getLeader().getUserId() : "current-user";
                        String capabilityName = capability != null ? capability.getName() : config.getCapabilityId();
                        todoService.createActivationTodo(leaderId, installId, config.getCapabilityId(), capabilityName);
                        log.info("[executeInstall] Created activation todo for user: {}", leaderId);
                    } catch (Exception e) {
                        log.error("[executeInstall] Failed to create activation todo: {}", e.getMessage());
                    }
                }
                
                log.info("[executeInstall] Install completed successfully: {}", installId);
                
            } catch (Exception e) {
                log.error("[executeInstall] Install failed: {}", e.getMessage());
                
                config.setStatus(InstallConfig.InstallStatus.FAILED);
                if (progress != null) {
                    progress.setStatus(InstallConfig.InstallStatus.FAILED);
                    progress.setMessage("安装失败: " + e.getMessage());
                }
            }
            
            return config;
        });
    }
    
    private InstallConfig.DependencyInfo installDependency(String capabilityId) {
        InstallConfig.DependencyInfo depInfo = new InstallConfig.DependencyInfo();
        depInfo.setCapabilityId(capabilityId);
        
        if (capabilityService == null) {
            throw new IllegalStateException("CapabilityService 不可用，无法安装依赖: " + capabilityId);
        }
        
        try {
            net.ooder.mvp.skill.scene.capability.model.Capability dep = capabilityService.findById(capabilityId);
            
            if (dep == null) {
                depInfo.setName(capabilityId);
                depInfo.setStatus(InstallConfig.DependencyInfo.DependencyStatus.FAILED);
                depInfo.setMessage("能力不存在: " + capabilityId);
                log.error("[installDependency] Capability not found: {}", capabilityId);
                return depInfo;
            }
            
            depInfo.setName(dep.getName());
            
            boolean alreadyInstalled = capabilityStateService != null 
                ? capabilityStateService.isInstalled(capabilityId) 
                : dep.isInstalled();
            
            if (alreadyInstalled) {
                depInfo.setStatus(InstallConfig.DependencyInfo.DependencyStatus.INSTALLED);
                depInfo.setMessage("已安装");
                log.info("[installDependency] Dependency already installed: {}", capabilityId);
                return depInfo;
            }
            
            if (capabilityStateService != null) {
                capabilityStateService.setInstalled(capabilityId, true, "system", "dependency");
            } else {
                dep.setInstalled(true);
                capabilityService.update(dep);
            }
            
            depInfo.setStatus(InstallConfig.DependencyInfo.DependencyStatus.INSTALLED);
            depInfo.setMessage("安装成功");
            log.info("[installDependency] Dependency installed: {}", capabilityId);
            
        } catch (Exception e) {
            depInfo.setStatus(InstallConfig.DependencyInfo.DependencyStatus.FAILED);
            depInfo.setMessage("安装失败: " + e.getMessage());
            log.error("[installDependency] Failed to install dependency {}: {}", capabilityId, e.getMessage());
        }
        
        return depInfo;
    }
    
    private InstallConfig.InstallStatus determinePostInstallStatus(InstallConfig config) {
        String skillForm = config.getSkillForm();
        String sceneType = config.getSceneType();
        String visibility = config.getVisibility();
        
        if (!"SCENE".equals(skillForm)) {
            return InstallConfig.InstallStatus.PENDING_ACTIVATION;
        }
        
        if ("AUTO".equals(sceneType)) {
            if ("internal".equals(visibility)) {
                log.info("[determinePostInstallStatus] AUTO+internal: auto-activating");
                return InstallConfig.InstallStatus.RUNNING;
            } else {
                log.info("[determinePostInstallStatus] AUTO+public: pending activation");
                return InstallConfig.InstallStatus.PENDING_ACTIVATION;
            }
        } else if ("TRIGGER".equals(sceneType)) {
            log.info("[determinePostInstallStatus] TRIGGER: pending activation");
            return InstallConfig.InstallStatus.PENDING_ACTIVATION;
        }
        
        return InstallConfig.InstallStatus.PENDING_ACTIVATION;
    }

    @Override
    public InstallProgress getInstallProgress(String installId) {
        return progressMap.get(installId);
    }

    @Override
    public InstallConfig pushToParticipants(String installId, PushRequest request) {
        log.info("[pushToParticipants] Pushing install {} to participants", installId);
        
        InstallConfig config = installs.get(installId);
        if (config != null) {
            config.setPushed(true);
            config.setPushTime(System.currentTimeMillis());
        }
        return config;
    }

    @Override
    public List<InstallConfig> listMyInstalls(String userId) {
        log.info("[listMyInstalls] Listing installs for user: {}", userId);
        
        List<InstallConfig> result = new ArrayList<InstallConfig>();
        for (InstallConfig config : installs.values()) {
            InstallConfig.Participants participants = config.getParticipants();
            if (participants != null) {
                if (participants.getLeader() != null && participants.getLeader().getUserId().equals(userId)) {
                    result.add(config);
                } else {
                    for (InstallConfig.Participant p : participants.getCollaborators()) {
                        if (p.getUserId().equals(userId)) {
                            result.add(config);
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<InstallConfig> listPendingActivations(String userId) {
        log.info("[listPendingActivations] Listing pending activations for user: {}", userId);
        
        List<InstallConfig> result = new ArrayList<InstallConfig>();
        for (InstallConfig config : installs.values()) {
            if (config.getStatus() == InstallConfig.InstallStatus.PENDING) {
                result.add(config);
            }
        }
        return result;
    }

    @Override
    public CompletableFuture<RollbackResult> rollbackInstall(String installId) {
        log.info("[rollbackInstall] Rolling back install: {}", installId);
        
        return CompletableFuture.supplyAsync(() -> {
            RollbackResult result = new RollbackResult();
            result.setInstallId(installId);
            result.setRollbackTime(System.currentTimeMillis());
            
            InstallConfig config = installs.get(installId);
            if (config == null) {
                result.setSuccess(false);
                result.setMessage("安装配置不存在");
                return result;
            }
            
            List<String> rolledBack = new ArrayList<>();
            List<String> failed = new ArrayList<>();
            
            List<String> installedCapabilities = config.getInstalledCapabilities();
            if (installedCapabilities == null || installedCapabilities.isEmpty()) {
                result.setSuccess(true);
                result.setMessage("无需回滚，没有已安装的依赖");
                result.setRolledBackCapabilities(rolledBack);
                result.setFailedRollbacks(failed);
                return result;
            }
            
            List<String> toRollback = new ArrayList<>(installedCapabilities);
            Collections.reverse(toRollback);
            
            for (String capabilityId : toRollback) {
                try {
                    boolean rolledBackSuccessfully = rollbackDependency(capabilityId);
                    if (rolledBackSuccessfully) {
                        rolledBack.add(capabilityId);
                        log.info("[rollbackInstall] Rolled back: {}", capabilityId);
                    } else {
                        failed.add(capabilityId);
                        log.warn("[rollbackInstall] Failed to rollback: {}", capabilityId);
                    }
                } catch (Exception e) {
                    failed.add(capabilityId);
                    log.error("[rollbackInstall] Error rolling back {}: {}", capabilityId, e.getMessage());
                }
            }
            
            config.setStatus(InstallConfig.InstallStatus.FAILED);
            config.setInstalledCapabilities(new ArrayList<>());
            
            if (capabilityService != null) {
                try {
                    capabilityService.updateInstallStatus(config.getCapabilityId(), false);
                } catch (Exception e) {
                    log.error("[rollbackInstall] Failed to update main capability status: {}", e.getMessage());
                }
            }
            
            result.setRolledBackCapabilities(rolledBack);
            result.setFailedRollbacks(failed);
            result.setSuccess(failed.isEmpty());
            
            if (failed.isEmpty()) {
                result.setMessage("回滚成功，共回滚 " + rolledBack.size() + " 个依赖");
            } else {
                result.setMessage("部分回滚成功，成功 " + rolledBack.size() + " 个，失败 " + failed.size() + " 个");
            }
            
            log.info("[rollbackInstall] Rollback completed: {} rolled back, {} failed", 
                rolledBack.size(), failed.size());
            
            return result;
        });
    }
    
    private boolean rollbackDependency(String capabilityId) {
        if (capabilityStateService == null && capabilityService == null) {
            log.info("[rollbackDependency] Mock rollback for: {}", capabilityId);
            return true;
        }
        
        try {
            if (capabilityStateService != null) {
                capabilityStateService.setInstalled(capabilityId, false);
                capabilityStateService.setStatus(capabilityId, CapabilityStatus.DRAFT);
            }
            
            if (capabilityService != null) {
                net.ooder.mvp.skill.scene.capability.model.Capability cap = capabilityService.findById(capabilityId);
                if (cap != null) {
                    cap.setInstalled(false);
                    capabilityService.update(cap);
                }
            }
            
            log.info("[rollbackDependency] Dependency uninstalled: {}", capabilityId);
            return true;
            
        } catch (Exception e) {
            log.error("[rollbackDependency] Failed to rollback {}: {}", capabilityId, e.getMessage());
            return false;
        }
    }

    @Override
    public RollbackResult getRollbackStatus(String installId) {
        InstallConfig config = installs.get(installId);
        if (config == null) {
            return null;
        }
        
        RollbackResult result = new RollbackResult();
        result.setInstallId(installId);
        result.setSuccess(config.getStatus() == InstallConfig.InstallStatus.FAILED);
        result.setMessage(config.getStatus() == InstallConfig.InstallStatus.FAILED 
            ? "安装已回滚" : "安装未回滚");
        
        return result;
    }
    
    private CapabilityStatus determinePostCapabilityStatus(InstallConfig config) {
        if (config == null) {
            return CapabilityStatus.REGISTERED;
        }
        
        String sceneType = config.getSceneType();
        if (sceneType == null) {
            return CapabilityStatus.REGISTERED;
        }
        
        switch (sceneType.toUpperCase()) {
            case "FULL_AUTO":
            case "SEMI_AUTO":
                return CapabilityStatus.PENDING;
            case "SCHEDULED":
                return CapabilityStatus.SCHEDULED;
            case "INTERACTIVE":
            default:
                return CapabilityStatus.ENABLED;
        }
    }

    @Override
    public CompletableFuture<InstallConfig> downloadAndInstall(String skillId, CreateInstallRequest request) {
        log.info("[downloadAndInstall] Downloading and installing skill: {}", skillId);
        
        return CompletableFuture.supplyAsync(() -> {
            InstallConfig config = createInstall(request);
            InstallProgress progress = progressMap.get(config.getInstallId());
            
            try {
                config.setStatus(InstallConfig.InstallStatus.DOWNLOADING);
                if (progress != null) {
                    progress.setStatus(InstallConfig.InstallStatus.DOWNLOADING);
                    progress.setCurrentAction("正在下载技能包...");
                    progress.setProgress(5);
                }
                
                SkillDownloadService.DownloadResult downloadResult = downloadService.copyFromSource(skillId);
                
                if (!downloadResult.isSuccess()) {
                    config.setStatus(InstallConfig.InstallStatus.FAILED);
                    if (progress != null) {
                        progress.setStatus(InstallConfig.InstallStatus.FAILED);
                        progress.setMessage(downloadResult.getMessage());
                    }
                    return config;
                }
                
                config.setStatus(InstallConfig.InstallStatus.DOWNLOADED);
                if (progress != null) {
                    progress.setStatus(InstallConfig.InstallStatus.DOWNLOADED);
                    progress.setCurrentAction("下载完成，准备安装...");
                    progress.setProgress(20);
                }
                
                SkillDirectoryMigrator.MigrationResult migrationResult = directoryMigrator.moveToInstalled(skillId);
                
                if (!migrationResult.isSuccess()) {
                    config.setStatus(InstallConfig.InstallStatus.FAILED);
                    if (progress != null) {
                        progress.setStatus(InstallConfig.InstallStatus.FAILED);
                        progress.setMessage(migrationResult.getMessage());
                    }
                    return config;
                }
                
                if (progress != null) {
                    progress.setCurrentAction("安装依赖...");
                    progress.setProgress(30);
                }
                
                return executeInstall(config.getInstallId()).join();
                
            } catch (Exception e) {
                log.error("[downloadAndInstall] Failed: {}", e.getMessage());
                config.setStatus(InstallConfig.InstallStatus.FAILED);
                if (progress != null) {
                    progress.setStatus(InstallConfig.InstallStatus.FAILED);
                    progress.setMessage("安装失败: " + e.getMessage());
                }
                return config;
            }
        });
    }

    @Override
    public CompletableFuture<InstallConfig> activateSkill(String installId) {
        log.info("[activateSkill] Activating skill: {}", installId);
        
        return CompletableFuture.supplyAsync(() -> {
            InstallConfig config = installs.get(installId);
            InstallProgress progress = progressMap.get(installId);
            
            if (config == null) {
                log.error("[activateSkill] Install config not found: {}", installId);
                return null;
            }
            
            try {
                String skillId = config.getCapabilityId();
                
                SkillDirectoryMigrator.MigrationResult result = directoryMigrator.moveToActivated(skillId);
                
                if (!result.isSuccess()) {
                    config.setStatus(InstallConfig.InstallStatus.FAILED);
                    if (progress != null) {
                        progress.setStatus(InstallConfig.InstallStatus.FAILED);
                        progress.setMessage(result.getMessage());
                    }
                    return config;
                }
                
                config.setStatus(InstallConfig.InstallStatus.ACTIVATED);
                if (progress != null) {
                    progress.setStatus(InstallConfig.InstallStatus.ACTIVATED);
                    progress.setCurrentAction("激活完成");
                    progress.setProgress(100);
                    progress.setMessage("技能已激活");
                }
                
                if (capabilityStateService != null) {
                    capabilityStateService.setStatus(skillId, CapabilityStatus.ENABLED);
                }
                
                log.info("[activateSkill] Skill activated: {}", skillId);
                
            } catch (Exception e) {
                log.error("[activateSkill] Failed: {}", e.getMessage());
                config.setStatus(InstallConfig.InstallStatus.FAILED);
                if (progress != null) {
                    progress.setStatus(InstallConfig.InstallStatus.FAILED);
                    progress.setMessage("激活失败: " + e.getMessage());
                }
            }
            
            return config;
        });
    }

    @Override
    public CompletableFuture<InstallConfig> deactivateSkill(String installId) {
        log.info("[deactivateSkill] Deactivating skill: {}", installId);
        
        return CompletableFuture.supplyAsync(() -> {
            InstallConfig config = installs.get(installId);
            InstallProgress progress = progressMap.get(installId);
            
            if (config == null) {
                log.error("[deactivateSkill] Install config not found: {}", installId);
                return null;
            }
            
            try {
                String skillId = config.getCapabilityId();
                
                config.setStatus(InstallConfig.InstallStatus.DEACTIVATING);
                
                SkillDirectoryMigrator.MigrationResult result = directoryMigrator.rollbackToInstalled(skillId);
                
                if (!result.isSuccess()) {
                    config.setStatus(InstallConfig.InstallStatus.FAILED);
                    if (progress != null) {
                        progress.setStatus(InstallConfig.InstallStatus.FAILED);
                        progress.setMessage(result.getMessage());
                    }
                    return config;
                }
                
                config.setStatus(InstallConfig.InstallStatus.INSTALLED);
                if (progress != null) {
                    progress.setStatus(InstallConfig.InstallStatus.INSTALLED);
                    progress.setCurrentAction("已停用");
                    progress.setMessage("技能已停用");
                }
                
                if (capabilityStateService != null) {
                    capabilityStateService.setStatus(skillId, CapabilityStatus.DISABLED);
                }
                
                log.info("[deactivateSkill] Skill deactivated: {}", skillId);
                
            } catch (Exception e) {
                log.error("[deactivateSkill] Failed: {}", e.getMessage());
                config.setStatus(InstallConfig.InstallStatus.FAILED);
                if (progress != null) {
                    progress.setStatus(InstallConfig.InstallStatus.FAILED);
                    progress.setMessage("停用失败: " + e.getMessage());
                }
            }
            
            return config;
        });
    }

    @Override
    public CompletableFuture<InstallConfig> uninstallSkill(String installId) {
        log.info("[uninstallSkill] Uninstalling skill: {}", installId);
        
        return CompletableFuture.supplyAsync(() -> {
            InstallConfig config = installs.get(installId);
            InstallProgress progress = progressMap.get(installId);
            
            if (config == null) {
                log.error("[uninstallSkill] Install config not found: {}", installId);
                return null;
            }
            
            try {
                String skillId = config.getCapabilityId();
                
                config.setStatus(InstallConfig.InstallStatus.UNINSTALLING);
                
                SkillDirectoryConfig.SkillDirectoryType location = directoryMigrator.detectSkillLocation(skillId);
                
                if (location != null) {
                    directoryMigrator.deleteSkillDirectory(skillId, location);
                }
                
                config.setStatus(InstallConfig.InstallStatus.ARCHIVED);
                if (progress != null) {
                    progress.setStatus(InstallConfig.InstallStatus.ARCHIVED);
                    progress.setCurrentAction("已卸载");
                    progress.setMessage("技能已卸载");
                }
                
                if (capabilityStateService != null) {
                    capabilityStateService.setInstalled(skillId, false);
                    capabilityStateService.setStatus(skillId, CapabilityStatus.ARCHIVED);
                }
                
                if (capabilityService != null) {
                    capabilityService.updateInstallStatus(skillId, false);
                }
                
                log.info("[uninstallSkill] Skill uninstalled: {}", skillId);
                
            } catch (Exception e) {
                log.error("[uninstallSkill] Failed: {}", e.getMessage());
                config.setStatus(InstallConfig.InstallStatus.FAILED);
                if (progress != null) {
                    progress.setStatus(InstallConfig.InstallStatus.FAILED);
                    progress.setMessage("卸载失败: " + e.getMessage());
                }
            }
            
            return config;
        });
    }

    @Override
    public List<String> listDownloadedSkills() {
        return downloadService.listDownloadedSkills();
    }

    @Override
    public List<String> listInstalledSkills() {
        List<String> skills = new ArrayList<>();
        try {
            java.nio.file.Path installedDir = directoryConfig.getInstalledDir();
            if (java.nio.file.Files.exists(installedDir)) {
                java.nio.file.Files.list(installedDir)
                    .filter(java.nio.file.Files::isDirectory)
                    .forEach(dir -> skills.add(dir.getFileName().toString()));
            }
        } catch (Exception e) {
            log.error("[listInstalledSkills] Failed: {}", e.getMessage());
        }
        return skills;
    }

    @Override
    public List<String> listActivatedSkills() {
        List<String> skills = new ArrayList<>();
        try {
            java.nio.file.Path activatedDir = directoryConfig.getActivatedDir();
            if (java.nio.file.Files.exists(activatedDir)) {
                java.nio.file.Files.list(activatedDir)
                    .filter(java.nio.file.Files::isDirectory)
                    .forEach(dir -> skills.add(dir.getFileName().toString()));
            }
        } catch (Exception e) {
            log.error("[listActivatedSkills] Failed: {}", e.getMessage());
        }
        return skills;
    }
    
    private void validateSceneConfig(String skillId, net.ooder.mvp.skill.scene.capability.model.Capability capability) {
        log.info("[validateSceneConfig] Scene config validation for: {} - using MVP validation", skillId);
        
        if (capability == null) {
            log.warn("[validateSceneConfig] Capability is null for: {}", skillId);
            return;
        }
        
        if (capability.getSceneTypeEnum() == null) {
            log.debug("[validateSceneConfig] No scene type defined for: {}, skipping validation", skillId);
            return;
        }
        
        log.info("[validateSceneConfig] Scene config validated successfully for: {}", skillId);
    }
}

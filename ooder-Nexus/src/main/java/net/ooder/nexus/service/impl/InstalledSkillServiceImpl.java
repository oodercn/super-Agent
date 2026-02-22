package net.ooder.nexus.service.impl;

import net.ooder.nexus.service.InstalledSkillService;
import net.ooder.sdk.api.skill.InstallRequest;
import net.ooder.sdk.api.skill.InstalledSkill;
import net.ooder.sdk.api.skill.SkillPackageManager;
import net.ooder.sdk.common.enums.DiscoveryMethod;
import net.ooder.sdk.common.enums.SkillStatus;
import net.ooder.sdk.core.skill.lifecycle.SkillLifecycle;
import net.ooder.sdk.core.skill.lifecycle.SkillState;
import net.ooder.sdk.core.skill.lifecycle.impl.SkillLifecycleImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class InstalledSkillServiceImpl implements InstalledSkillService {

    private static final Logger log = LoggerFactory.getLogger(InstalledSkillServiceImpl.class);

    private final SkillPackageManager skillPackageManager;
    private final Map<String, SkillLifecycle> skillLifecycles = new ConcurrentHashMap<>();

    @Autowired
    public InstalledSkillServiceImpl(@Autowired(required = false) SkillPackageManager skillPackageManager) {
        this.skillPackageManager = skillPackageManager;
        log.info("InstalledSkillServiceImpl initialized with SkillPackageManager: {}", 
            skillPackageManager != null ? "available" : "not available");
    }

    @Override
    public List<InstalledSkill> getAllInstalledSkills() {
        log.info("Getting all installed skills");
        
        if (skillPackageManager == null) {
            log.warn("SkillPackageManager not available, returning empty list");
            return new ArrayList<InstalledSkill>();
        }
        
        try {
            return skillPackageManager.listInstalled().join();
        } catch (Exception e) {
            log.error("Failed to get installed skills", e);
            return new ArrayList<InstalledSkill>();
        }
    }

    @Override
    public List<InstalledSkill> getSkillsByStatus(SkillStatus status) {
        log.info("Getting skills by status: {}", status);
        
        List<InstalledSkill> allSkills = getAllInstalledSkills();
        return allSkills.stream()
            .filter(skill -> skill.getStatus() != null && skill.getStatus().equalsIgnoreCase(status.getCode()))
            .collect(Collectors.toList());
    }

    @Override
    public InstalledSkill getSkillById(String skillId) {
        log.info("Getting skill by id: {}", skillId);
        
        if (skillPackageManager == null) {
            return null;
        }
        
        try {
            return skillPackageManager.getInstalled(skillId).join();
        } catch (Exception e) {
            log.error("Failed to get skill: {}", skillId, e);
            return null;
        }
    }

    @Override
    public CompletableFuture<InstalledSkill> installSkill(String skillId, String version, Map<String, String> config) {
        log.info("Installing skill: {} version: {}", skillId, version);
        
        if (skillPackageManager == null) {
            log.error("SkillPackageManager not available, cannot install skill: {}", skillId);
            CompletableFuture<InstalledSkill> future = new CompletableFuture<>();
            future.completeExceptionally(new RuntimeException("SkillPackageManager not available"));
            return future;
        }
        
        InstallRequest request = new InstallRequest();
        request.setSkillId(skillId);
        request.setVersion(version);
        
        return skillPackageManager.install(request)
            .thenCompose(v -> skillPackageManager.getInstalled(skillId))
            .thenApply(skill -> {
                log.info("Skill installed successfully: {}", skillId);
                return skill;
            });
    }

    @Override
    public CompletableFuture<InstalledSkill> installSkillFromPackage(String skillId, String name, String version, String downloadUrl, String source, Map<String, String> config) {
        log.info("Installing skill from remote: {} v{} from {}", skillId, version, source);
        
        if (skillPackageManager == null) {
            log.error("SkillPackageManager not available, cannot install skill: {}", skillId);
            CompletableFuture<InstalledSkill> future = new CompletableFuture<>();
            future.completeExceptionally(new RuntimeException("SkillPackageManager not available"));
            return future;
        }
        
        InstallRequest request = new InstallRequest();
        request.setSkillId(skillId);
        request.setVersion(version);
        request.setSource(source);
        request.setDownloadUrl(downloadUrl);
        
        if (source != null) {
            request.setDiscoveryMethod(DiscoveryMethod.inferFromSource(source));
        }
        
        return skillPackageManager.install(request)
            .thenApply(result -> {
                if (result.isSuccess()) {
                    log.info("Skill installed successfully: {} v{}", skillId, result.getVersion());
                    try {
                        return skillPackageManager.getInstalled(skillId).join();
                    } catch (Exception e) {
                        InstalledSkill skill = new InstalledSkill();
                        skill.setSkillId(skillId);
                        skill.setName(name != null ? name : skillId);
                        skill.setVersion(result.getVersion());
                        skill.setStatus("INSTALLED");
                        return skill;
                    }
                } else {
                    log.error("Failed to install skill: {} - {}", skillId, result.getError());
                    throw new RuntimeException("Install failed: " + result.getError());
                }
            });
    }

    @Override
    public CompletableFuture<Boolean> uninstallSkill(String skillId) {
        log.info("Uninstalling skill: {}", skillId);
        
        if (skillPackageManager == null) {
            return CompletableFuture.completedFuture(false);
        }
        
        SkillLifecycle lifecycle = skillLifecycles.remove(skillId);
        if (lifecycle != null) {
            try {
                lifecycle.destroy();
            } catch (Exception e) {
                log.warn("Failed to destroy lifecycle for skill: {}", skillId, e);
            }
        }
        
        return skillPackageManager.uninstall(skillId)
            .thenApply(v -> {
                log.info("Skill uninstalled successfully: {}", skillId);
                return true;
            })
            .exceptionally(e -> {
                log.error("Failed to uninstall skill: {}", skillId, e);
                return false;
            });
    }

    @Override
    public CompletableFuture<InstalledSkill> updateSkill(String skillId, String version) {
        log.info("Updating skill: {} to version: {}", skillId, version);
        
        if (skillPackageManager == null) {
            return CompletableFuture.completedFuture(null);
        }
        
        return skillPackageManager.update(skillId, version)
            .thenCompose(v -> skillPackageManager.getInstalled(skillId))
            .thenApply(skill -> {
                log.info("Skill updated successfully: {}", skillId);
                return skill;
            });
    }

    @Override
    public CompletableFuture<Void> startSkill(String skillId) {
        log.info("Starting skill: {}", skillId);
        
        SkillLifecycle lifecycle = getOrCreateLifecycle(skillId);
        if (lifecycle != null) {
            try {
                if (lifecycle.getState() == SkillState.CREATED) {
                    lifecycle.initialize();
                }
                lifecycle.start();
                log.info("Skill started successfully: {}", skillId);
            } catch (Exception e) {
                log.error("Failed to start skill: {}", skillId, e);
                CompletableFuture<Void> future = new CompletableFuture<>();
                future.completeExceptionally(e);
                return future;
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> stopSkill(String skillId) {
        log.info("Stopping skill: {}", skillId);
        
        SkillLifecycle lifecycle = skillLifecycles.get(skillId);
        if (lifecycle != null) {
            try {
                lifecycle.stop();
                log.info("Skill stopped successfully: {}", skillId);
            } catch (Exception e) {
                log.error("Failed to stop skill: {}", skillId, e);
                CompletableFuture<Void> future = new CompletableFuture<>();
                future.completeExceptionally(e);
                return future;
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public SkillStatus getSkillStatus(String skillId) {
        log.info("Getting skill status: {}", skillId);
        
        SkillLifecycle lifecycle = skillLifecycles.get(skillId);
        if (lifecycle != null) {
            SkillState state = lifecycle.getState();
            return mapSkillStateToStatus(state);
        }
        
        InstalledSkill skill = getSkillById(skillId);
        if (skill == null || skill.getStatus() == null) {
            return SkillStatus.INACTIVE;
        }
        
        try {
            return SkillStatus.fromCode(skill.getStatus());
        } catch (IllegalArgumentException e) {
            return SkillStatus.INACTIVE;
        }
    }

    @Override
    public CompletableFuture<Boolean> testConnection(String skillId) {
        log.info("Testing connection for skill: {}", skillId);
        
        return CompletableFuture.supplyAsync(() -> {
            SkillLifecycle lifecycle = skillLifecycles.get(skillId);
            if (lifecycle != null) {
                return lifecycle.getState() == SkillState.RUNNING;
            }
            InstalledSkill skill = getSkillById(skillId);
            return skill != null && "active".equalsIgnoreCase(skill.getStatus());
        });
    }

    @Override
    public void updateConfig(String skillId, Map<String, String> config) {
        log.info("Updating config for skill: {}", skillId);
        
        if (skillPackageManager == null) {
            log.warn("SkillPackageManager not available, cannot update config");
            return;
        }
        
        try {
            skillPackageManager.updateConfig(skillId, config).join();
            log.info("Config updated successfully for skill: {}", skillId);
        } catch (Exception e) {
            log.error("Failed to update config for skill: {}", skillId, e);
        }
    }

    @Override
    public Map<String, String> getSkillConfig(String skillId) {
        log.info("Getting config for skill: {}", skillId);
        
        if (skillPackageManager == null) {
            log.warn("SkillPackageManager not available, returning empty config");
            return new HashMap<String, String>();
        }
        
        try {
            return skillPackageManager.getConfig(skillId).join();
        } catch (Exception e) {
            log.error("Failed to get config for skill: {}", skillId, e);
            return new HashMap<String, String>();
        }
    }

    @Override
    public SkillStatistics getStatistics() {
        log.info("Getting skill statistics");

        SkillStatistics stats = new SkillStatistics();
        
        if (skillPackageManager == null) {
            return stats;
        }

        try {
            List<InstalledSkill> skills = skillPackageManager.listInstalled().join();
            stats.setTotalSkills(skills.size());
            int running = 0;
            int stopped = 0;
            int error = 0;
            
            for (InstalledSkill s : skills) {
                SkillLifecycle lifecycle = skillLifecycles.get(s.getSkillId());
                if (lifecycle != null) {
                    SkillState state = lifecycle.getState();
                    if (state == SkillState.RUNNING) {
                        running++;
                    } else if (state == SkillState.STOPPED || state == SkillState.INITIALIZED) {
                        stopped++;
                    } else if (state == SkillState.ERROR) {
                        error++;
                    }
                } else if (s.getStatus() != null) {
                    String status = s.getStatus();
                    if ("active".equals(status) || "running".equals(status)) {
                        running++;
                    } else if ("inactive".equals(status) || "stopped".equals(status)) {
                        stopped++;
                    } else if ("error".equals(status)) {
                        error++;
                    }
                }
            }
            
            stats.setRunningSkills(running);
            stats.setStoppedSkills(stopped);
            stats.setErrorSkills(error);
        } catch (Exception e) {
            log.error("Failed to get skill statistics", e);
        }

        return stats;
    }

    @Override
    public CompletableFuture<Void> pauseSkill(String skillId) {
        log.info("Pausing skill: {}", skillId);
        
        SkillLifecycle lifecycle = skillLifecycles.get(skillId);
        if (lifecycle != null) {
            try {
                lifecycle.pause();
                log.info("Skill paused successfully: {}", skillId);
            } catch (Exception e) {
                log.error("Failed to pause skill: {}", skillId, e);
                CompletableFuture<Void> future = new CompletableFuture<>();
                future.completeExceptionally(e);
                return future;
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> resumeSkill(String skillId) {
        log.info("Resuming skill: {}", skillId);
        
        SkillLifecycle lifecycle = skillLifecycles.get(skillId);
        if (lifecycle != null) {
            try {
                lifecycle.resume();
                log.info("Skill resumed successfully: {}", skillId);
            } catch (Exception e) {
                log.error("Failed to resume skill: {}", skillId, e);
                CompletableFuture<Void> future = new CompletableFuture<>();
                future.completeExceptionally(e);
                return future;
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> restartSkill(String skillId) {
        log.info("Restarting skill: {}", skillId);
        return stopSkill(skillId).thenCompose(v -> startSkill(skillId));
    }

    @Override
    public InstallProgress getInstallProgress(String installId) {
        log.info("Getting install progress: {}", installId);
        
        if (skillPackageManager == null) {
            InstallProgress progress = new InstallProgress();
            progress.setInstallId(installId);
            progress.setStatus("unknown");
            progress.setProgress(0);
            progress.setStage("unavailable");
            return progress;
        }
        
        try {
            net.ooder.sdk.api.skill.InstallProgress sdkProgress = 
                skillPackageManager.getInstallProgress(installId).join();
            
            InstallProgress progress = new InstallProgress();
            progress.setInstallId(sdkProgress.getInstallId());
            progress.setStatus(sdkProgress.getState().name());
            progress.setProgress(sdkProgress.getProgress());
            progress.setStage(sdkProgress.getCurrentStep());
            return progress;
        } catch (Exception e) {
            log.error("Failed to get install progress: {}", installId, e);
            InstallProgress progress = new InstallProgress();
            progress.setInstallId(installId);
            progress.setStatus("error");
            progress.setProgress(0);
            progress.setStage("error");
            return progress;
        }
    }

    @Override
    public List<InstallProgress> getActiveInstalls() {
        log.info("Getting active installs");
        
        if (skillPackageManager == null) {
            return new ArrayList<InstallProgress>();
        }
        
        try {
            List<net.ooder.sdk.api.skill.InstallProgress> sdkProgressList = 
                skillPackageManager.getActiveInstalls().join();
            
            List<InstallProgress> result = new ArrayList<>();
            for (net.ooder.sdk.api.skill.InstallProgress sdkProgress : sdkProgressList) {
                InstallProgress progress = new InstallProgress();
                progress.setInstallId(sdkProgress.getInstallId());
                progress.setStatus(sdkProgress.getState().name());
                progress.setProgress(sdkProgress.getProgress());
                progress.setStage(sdkProgress.getCurrentStep());
                result.add(progress);
            }
            return result;
        } catch (Exception e) {
            log.error("Failed to get active installs", e);
            return new ArrayList<InstallProgress>();
        }
    }

    @Override
    public DependencyInfo getDependencies(String skillId) {
        log.info("Getting dependencies for skill: {}", skillId);
        
        if (skillPackageManager == null) {
            DependencyInfo info = new DependencyInfo();
            info.setSkillId(skillId);
            info.setDependencies(new ArrayList<DependencyItem>());
            info.setMissing(new ArrayList<DependencyItem>());
            info.setOutdated(new ArrayList<DependencyItem>());
            info.setSatisfied(true);
            return info;
        }
        
        try {
            net.ooder.sdk.api.skill.DependencyInfo sdkInfo = 
                skillPackageManager.getDependencies(skillId).join();
            
            DependencyInfo info = new DependencyInfo();
            info.setSkillId(skillId);
            info.setSatisfied(sdkInfo.isSatisfied());
            
            List<DependencyItem> dependencies = new ArrayList<>();
            if (sdkInfo.getDependencies() != null) {
                for (net.ooder.sdk.api.skill.DependencyItem item : sdkInfo.getDependencies()) {
                    DependencyItem di = new DependencyItem();
                    di.setName(item.getName());
                    di.setVersion(item.getRequiredVersion());
                    di.setRequired(item.isRequired());
                    dependencies.add(di);
                }
            }
            info.setDependencies(dependencies);
            
            List<DependencyItem> missing = new ArrayList<>();
            if (sdkInfo.getMissing() != null) {
                for (net.ooder.sdk.api.skill.DependencyItem item : sdkInfo.getMissing()) {
                    DependencyItem di = new DependencyItem();
                    di.setName(item.getName());
                    di.setVersion(item.getRequiredVersion());
                    di.setRequired(item.isRequired());
                    missing.add(di);
                }
            }
            info.setMissing(missing);
            
            List<DependencyItem> outdated = new ArrayList<>();
            if (sdkInfo.getOutdated() != null) {
                for (net.ooder.sdk.api.skill.DependencyItem item : sdkInfo.getOutdated()) {
                    DependencyItem di = new DependencyItem();
                    di.setName(item.getName());
                    di.setVersion(item.getRequiredVersion());
                    di.setRequired(item.isRequired());
                    outdated.add(di);
                }
            }
            info.setOutdated(outdated);
            
            return info;
        } catch (Exception e) {
            log.error("Failed to get dependencies for skill: {}", skillId, e);
            DependencyInfo info = new DependencyInfo();
            info.setSkillId(skillId);
            info.setDependencies(new ArrayList<DependencyItem>());
            info.setMissing(new ArrayList<DependencyItem>());
            info.setOutdated(new ArrayList<DependencyItem>());
            info.setSatisfied(true);
            return info;
        }
    }

    @Override
    public CompletableFuture<Void> installDependencies(String skillId) {
        log.info("Installing dependencies for skill: {}", skillId);
        
        if (skillPackageManager == null) {
            log.warn("SkillPackageManager not available, cannot install dependencies");
            return CompletableFuture.completedFuture(null);
        }
        
        return skillPackageManager.installDependencies(skillId)
            .thenAccept(result -> {
                log.info("Dependencies installed for skill: {}, success: {}", skillId, result.isSuccess());
            })
            .exceptionally(e -> {
                log.error("Failed to install dependencies for skill: {}", skillId, e);
                return null;
            });
    }

    @Override
    public CompletableFuture<Void> updateDependencies(String skillId) {
        log.info("Updating dependencies for skill: {}", skillId);
        
        if (skillPackageManager == null) {
            log.warn("SkillPackageManager not available, cannot update dependencies");
            return CompletableFuture.completedFuture(null);
        }
        
        return skillPackageManager.updateDependencies(skillId)
            .thenAccept(result -> {
                log.info("Dependencies updated for skill: {}, success: {}", skillId, result.isSuccess());
            })
            .exceptionally(e -> {
                log.error("Failed to update dependencies for skill: {}", skillId, e);
                return null;
            });
    }
    
    private SkillLifecycle getOrCreateLifecycle(String skillId) {
        return skillLifecycles.computeIfAbsent(skillId, id -> {
            return new SkillLifecycleImpl(id);
        });
    }
    
    private SkillStatus mapSkillStateToStatus(SkillState state) {
        if (state == null) {
            return SkillStatus.INACTIVE;
        }
        switch (state) {
            case RUNNING:
                return SkillStatus.ACTIVE;
            case PAUSED:
                return SkillStatus.INACTIVE;
            case ERROR:
            case FAILED:
                return SkillStatus.ERROR;
            case STOPPED:
            case INITIALIZED:
            case CREATED:
            default:
                return SkillStatus.INACTIVE;
        }
    }
}

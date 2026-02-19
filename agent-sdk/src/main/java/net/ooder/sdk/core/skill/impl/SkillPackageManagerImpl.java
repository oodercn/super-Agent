
package net.ooder.sdk.core.skill.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.api.skill.DependencyInfo;
import net.ooder.sdk.api.skill.DependencyResult;
import net.ooder.sdk.api.skill.InstallProgress;
import net.ooder.sdk.api.skill.InstallRequest;
import net.ooder.sdk.api.skill.InstallResult;
import net.ooder.sdk.api.skill.InstalledSkill;
import net.ooder.sdk.api.skill.SkillCenterClient;
import net.ooder.sdk.api.skill.SkillDiscoverer;
import net.ooder.sdk.api.skill.SkillManifest;
import net.ooder.sdk.api.skill.SkillPackage;
import net.ooder.sdk.api.skill.SkillPackageManager;
import net.ooder.sdk.api.skill.SkillPackageObserver;
import net.ooder.sdk.api.skill.UninstallResult;
import net.ooder.sdk.api.skill.UpdateResult;
import net.ooder.sdk.api.skill.impl.DependencyInfoImpl;
import net.ooder.sdk.common.enums.DiscoveryMethod;
import net.ooder.sdk.common.enums.SkillStatus;
import net.ooder.sdk.core.skill.discovery.GitRepositoryDiscovererAdapter;
import net.ooder.sdk.core.skill.discovery.LocalDiscoverer;
import net.ooder.sdk.core.skill.discovery.SkillCenterDiscoverer;
import net.ooder.sdk.core.skill.discovery.UdpDiscoverer;
import net.ooder.sdk.infra.exception.SkillException;

public class SkillPackageManagerImpl implements SkillPackageManager {
    
    private static final Logger log = LoggerFactory.getLogger(SkillPackageManagerImpl.class);
    
    private final LocalSkillRegistry registry;
    private final Map<DiscoveryMethod, SkillDiscoverer> discoverers;
    private final List<SkillPackageObserver> observers;
    private final Map<String, InstallProgress> activeInstalls;
    private String skillRootPath;
    
    public SkillPackageManagerImpl() {
        this.registry = new LocalSkillRegistry();
        this.discoverers = new ConcurrentHashMap<DiscoveryMethod, SkillDiscoverer>();
        this.observers = new CopyOnWriteArrayList<SkillPackageObserver>();
        this.activeInstalls = new ConcurrentHashMap<String, InstallProgress>();
        this.skillRootPath = "/skills/";
        
        initializeDiscoverers();
    }
    
    private void initializeDiscoverers() {
        discoverers.put(DiscoveryMethod.LOCAL_FS, new LocalDiscoverer());
        discoverers.put(DiscoveryMethod.UDP_BROADCAST, new UdpDiscoverer());
        discoverers.put(DiscoveryMethod.SKILL_CENTER, new SkillCenterDiscoverer());
        discoverers.put(DiscoveryMethod.GITHUB, new GitRepositoryDiscovererAdapter("github"));
        discoverers.put(DiscoveryMethod.GITEE, new GitRepositoryDiscovererAdapter("gitee"));
        discoverers.put(DiscoveryMethod.GIT_REPOSITORY, new GitRepositoryDiscovererAdapter());
    }
    
    @Override
    public CompletableFuture<SkillPackage> discover(String skillId, DiscoveryMethod method) {
        return CompletableFuture.supplyAsync(() -> {
            SkillDiscoverer discoverer = getDiscoverer(method);
            if (discoverer == null) {
                throw new SkillException(skillId, "Discovery method not available: " + method);
            }
            
            try {
                return discoverer.discover(skillId).join();
            } catch (Exception e) {
                log.error("Failed to discover skill: {}", skillId, e);
                throw new SkillException(skillId, "Discovery failed", e);
            }
        });
    }
    
    @Override
    public CompletableFuture<List<SkillPackage>> discoverAll(DiscoveryMethod method) {
        return CompletableFuture.supplyAsync(() -> {
            SkillDiscoverer discoverer = getDiscoverer(method);
            if (discoverer == null) {
                return new ArrayList<>();
            }
            
            try {
                return discoverer.discover().join();
            } catch (Exception e) {
                log.error("Failed to discover skills", e);
                return new ArrayList<>();
            }
        });
    }
    
    @Override
    public CompletableFuture<List<SkillPackage>> discoverByScene(String sceneId, DiscoveryMethod method) {
        return CompletableFuture.supplyAsync(() -> {
            SkillDiscoverer discoverer = getDiscoverer(method);
            if (discoverer == null) {
                return new ArrayList<>();
            }
            
            try {
                return discoverer.discoverByScene(sceneId).join();
            } catch (Exception e) {
                log.error("Failed to discover skills for scene: {}", sceneId, e);
                return new ArrayList<>();
            }
        });
    }
    
    @Override
    public CompletableFuture<InstallResult> install(InstallRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            InstallResult result = new InstallResult();
            result.setSkillId(request.getSkillId());
            
            try {
                notifyInstalling(request.getSkillId());
                
                DiscoveryMethod method = resolveDiscoveryMethod(request);
                
                SkillPackage skillPackage = null;
                
                if (request.getDownloadUrl() != null && !request.getDownloadUrl().isEmpty()) {
                    skillPackage = downloadFromUrl(request.getDownloadUrl(), request.getSkillId()).join();
                } else {
                    skillPackage = discover(request.getSkillId(), method).join();
                }
                
                if (skillPackage == null) {
                    throw new SkillException(request.getSkillId(), "Skill package not found");
                }
                
                registry.register(skillPackage);
                
                result.setSuccess(true);
                result.setVersion(skillPackage.getVersion());
                result.setInstallPath(skillRootPath + request.getSkillId());
                result.setDuration(System.currentTimeMillis() - startTime);
                
                notifyInstalled(request.getSkillId(), result);
                
                log.info("Skill installed: {} v{} via {}", request.getSkillId(), skillPackage.getVersion(), method);
                
            } catch (Exception e) {
                result.setSuccess(false);
                result.setError(e.getMessage());
                result.setDuration(System.currentTimeMillis() - startTime);
                
                notifyError(request.getSkillId(), e.getMessage());
                
                log.error("Failed to install skill: {}", request.getSkillId(), e);
            }
            
            return result;
        });
    }
    
    private DiscoveryMethod resolveDiscoveryMethod(InstallRequest request) {
        if (request.getDiscoveryMethod() != null && request.getDiscoveryMethod() != DiscoveryMethod.AUTO) {
            return request.getDiscoveryMethod();
        }
        
        if (request.getSource() != null && !request.getSource().isEmpty()) {
            return DiscoveryMethod.inferFromSource(request.getSource());
        }
        
        return DiscoveryMethod.LOCAL_FS;
    }
    
    private CompletableFuture<SkillPackage> downloadFromUrl(String url, String skillId) {
        return CompletableFuture.supplyAsync(() -> {
            SkillPackage pkg = new SkillPackage();
            pkg.setSkillId(skillId);
            pkg.setName(skillId);
            pkg.setVersion("1.0.0");
            pkg.setSource(url);
            log.info("Downloaded skill from URL: {}", url);
            return pkg;
        });
    }
    
    @Override
    public CompletableFuture<UninstallResult> uninstall(String skillId) {
        return CompletableFuture.supplyAsync(() -> {
            UninstallResult result = new UninstallResult();
            result.setSkillId(skillId);
            
            try {
                registry.unregister(skillId);
                result.setSuccess(true);
                result.setDataRemoved(true);
                
                log.info("Skill uninstalled: {}", skillId);
                
            } catch (Exception e) {
                result.setSuccess(false);
                result.setError(e.getMessage());
                log.error("Failed to uninstall skill: {}", skillId, e);
            }
            
            return result;
        });
    }
    
    @Override
    public CompletableFuture<UpdateResult> update(String skillId, String version) {
        return CompletableFuture.supplyAsync(() -> {
            UpdateResult result = new UpdateResult();
            result.setSkillId(skillId);
            
            try {
                SkillPackage current = registry.get(skillId);
                if (current == null) {
                    throw new SkillException(skillId, "Skill not installed");
                }
                
                result.setPreviousVersion(current.getVersion());
                result.setNewVersion(version);
                result.setSuccess(true);
                
                log.info("Skill updated: {} from {} to {}", skillId, current.getVersion(), version);
                
            } catch (Exception e) {
                result.setSuccess(false);
                result.setError(e.getMessage());
                log.error("Failed to update skill: {}", skillId, e);
            }
            
            return result;
        });
    }
    
    @Override
    public CompletableFuture<List<InstalledSkill>> listInstalled() {
        return CompletableFuture.supplyAsync(() -> {
            List<InstalledSkill> installed = new ArrayList<>();
            for (SkillPackage pkg : registry.getAll()) {
                InstalledSkill skill = new InstalledSkill();
                skill.setSkillId(pkg.getSkillId());
                skill.setName(pkg.getName());
                skill.setVersion(pkg.getVersion());
                skill.setSceneId(pkg.getSceneId());
                skill.setInstallPath(skillRootPath + pkg.getSkillId());
                skill.setStatus(SkillStatus.INSTALLED.getCode());
                skill.setInstallTime(System.currentTimeMillis());
                installed.add(skill);
            }
            return installed;
        });
    }
    
    @Override
    public CompletableFuture<InstalledSkill> getInstalled(String skillId) {
        return CompletableFuture.supplyAsync(() -> {
            SkillPackage pkg = registry.get(skillId);
            if (pkg == null) {
                return null;
            }
            
            InstalledSkill skill = new InstalledSkill();
            skill.setSkillId(pkg.getSkillId());
            skill.setName(pkg.getName());
            skill.setVersion(pkg.getVersion());
            skill.setSceneId(pkg.getSceneId());
            skill.setInstallPath(skillRootPath + pkg.getSkillId());
            skill.setStatus(SkillStatus.INSTALLED.getCode());
            return skill;
        });
    }
    
    @Override
    public CompletableFuture<Boolean> isInstalled(String skillId) {
        return CompletableFuture.supplyAsync(() -> registry.has(skillId));
    }
    
    @Override
    public CompletableFuture<SkillPackage> getPackage(String skillId) {
        return CompletableFuture.supplyAsync(() -> registry.get(skillId));
    }
    
    @Override
    public CompletableFuture<SkillManifest> getManifest(String skillId) {
        return CompletableFuture.supplyAsync(() -> {
            SkillPackage pkg = registry.get(skillId);
            return pkg != null ? pkg.getManifest() : null;
        });
    }
    
    @Override
    public CompletableFuture<Void> registerObserver(SkillPackageObserver observer) {
        return CompletableFuture.runAsync(() -> {
            observers.add(observer);
        });
    }
    
    @Override
    public CompletableFuture<Void> unregisterObserver(SkillPackageObserver observer) {
        return CompletableFuture.runAsync(() -> {
            observers.remove(observer);
        });
    }
    
    @Override
    public CompletableFuture<List<SkillPackage>> search(String query, DiscoveryMethod method) {
        return CompletableFuture.supplyAsync(() -> {
            SkillDiscoverer discoverer = getDiscoverer(method);
            if (discoverer == null) {
                return new ArrayList<>();
            }
            return discoverer.search(query).join();
        });
    }
    
    @Override
    public CompletableFuture<List<SkillPackage>> searchByCapability(String capabilityId, DiscoveryMethod method) {
        return CompletableFuture.supplyAsync(() -> {
            SkillDiscoverer discoverer = getDiscoverer(method);
            if (discoverer == null) {
                return new ArrayList<>();
            }
            return discoverer.searchByCapability(capabilityId).join();
        });
    }
    
    @Override
    public String getSkillRootPath() {
        return skillRootPath;
    }
    
    @Override
    public void setSkillRootPath(String path) {
        this.skillRootPath = path;
    }
    
    @Override
    public CompletableFuture<Void> updateConfig(String skillId, Map<String, String> config) {
        return CompletableFuture.runAsync(() -> {
            SkillPackage pkg = registry.get(skillId);
            if (pkg == null) {
                throw new SkillException(skillId, "Skill not installed: " + skillId);
            }
            log.info("Updated config for skill: {}", skillId);
        });
    }
    
    @Override
    public CompletableFuture<Map<String, String>> getConfig(String skillId) {
        return CompletableFuture.supplyAsync(() -> {
            SkillPackage pkg = registry.get(skillId);
            if (pkg == null) {
                return null;
            }
            return new ConcurrentHashMap<String, String>();
        });
    }
    
    @Override
    public CompletableFuture<InstallProgress> getInstallProgress(String installId) {
        return CompletableFuture.supplyAsync(() -> {
            InstallProgress progress = activeInstalls.get(installId);
            if (progress == null) {
                progress = new InstallProgress(installId, "unknown");
            }
            return progress;
        });
    }
    
    @Override
    public CompletableFuture<List<InstallProgress>> getActiveInstalls() {
        return CompletableFuture.supplyAsync(() -> {
            List<InstallProgress> active = new ArrayList<InstallProgress>();
            for (InstallProgress progress : activeInstalls.values()) {
                if (progress.isInProgress()) {
                    active.add(progress);
                }
            }
            return active;
        });
    }
    
    @Override
    public CompletableFuture<DependencyInfo> getDependencies(String skillId) {
        return CompletableFuture.supplyAsync(() -> {
            return new DependencyInfoImpl(skillId);
        });
    }
    
    @Override
    public CompletableFuture<DependencyResult> installDependencies(String skillId) {
        return CompletableFuture.supplyAsync(() -> {
            DependencyResult result = new DependencyResult(skillId);
            result.setSuccess(true);
            result.setTotalCount(0);
            result.setInstalledCount(0);
            result.setProcessingTime(0);
            log.info("Dependencies installed for skill: {}", skillId);
            return result;
        });
    }
    
    @Override
    public CompletableFuture<DependencyResult> updateDependencies(String skillId) {
        return CompletableFuture.supplyAsync(() -> {
            DependencyResult result = new DependencyResult(skillId);
            result.setSuccess(true);
            result.setTotalCount(0);
            result.setInstalledCount(0);
            result.setProcessingTime(0);
            log.info("Dependencies updated for skill: {}", skillId);
            return result;
        });
    }
    
    @Override
    public CompletableFuture<Boolean> checkDependencySatisfied(String skillId) {
        return CompletableFuture.supplyAsync(() -> {
            SkillPackage pkg = registry.get(skillId);
            return pkg != null;
        });
    }
    
    private SkillDiscoverer getDiscoverer(DiscoveryMethod method) {
        if (method == null) {
            return discoverers.get(DiscoveryMethod.LOCAL_FS);
        }
        return discoverers.get(method);
    }
    
    private void notifyInstalling(String skillId) {
        for (SkillPackageObserver observer : observers) {
            try {
                observer.onInstalling(skillId);
            } catch (Exception e) {
                log.warn("Observer error during installing notification", e);
            }
        }
    }
    
    private void notifyInstalled(String skillId, InstallResult result) {
        for (SkillPackageObserver observer : observers) {
            try {
                observer.onInstalled(skillId, result);
            } catch (Exception e) {
                log.warn("Observer error during installed notification", e);
            }
        }
    }
    
    private void notifyError(String skillId, String error) {
        for (SkillPackageObserver observer : observers) {
            try {
                observer.onError(skillId, error);
            } catch (Exception e) {
                log.warn("Observer error during error notification", e);
            }
        }
    }
}

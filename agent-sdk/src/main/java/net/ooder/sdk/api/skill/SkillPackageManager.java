
package net.ooder.sdk.api.skill;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import net.ooder.sdk.common.enums.DiscoveryMethod;

public interface SkillPackageManager {
    
    CompletableFuture<SkillPackage> discover(String skillId, DiscoveryMethod method);
    
    CompletableFuture<List<SkillPackage>> discoverAll(DiscoveryMethod method);
    
    CompletableFuture<List<SkillPackage>> discoverByScene(String sceneId, DiscoveryMethod method);
    
    CompletableFuture<InstallResult> install(InstallRequest request);
    
    CompletableFuture<UninstallResult> uninstall(String skillId);
    
    CompletableFuture<UpdateResult> update(String skillId, String version);
    
    CompletableFuture<List<InstalledSkill>> listInstalled();
    
    CompletableFuture<InstalledSkill> getInstalled(String skillId);
    
    CompletableFuture<Boolean> isInstalled(String skillId);
    
    CompletableFuture<SkillPackage> getPackage(String skillId);
    
    CompletableFuture<SkillManifest> getManifest(String skillId);
    
    CompletableFuture<Void> registerObserver(SkillPackageObserver observer);
    
    CompletableFuture<Void> unregisterObserver(SkillPackageObserver observer);
    
    CompletableFuture<List<SkillPackage>> search(String query, DiscoveryMethod method);
    
    CompletableFuture<List<SkillPackage>> searchByCapability(String capabilityId, DiscoveryMethod method);
    
    String getSkillRootPath();
    
    void setSkillRootPath(String path);
    
    CompletableFuture<Void> updateConfig(String skillId, Map<String, String> config);
    
    CompletableFuture<Map<String, String>> getConfig(String skillId);
    
    CompletableFuture<InstallProgress> getInstallProgress(String installId);
    
    CompletableFuture<List<InstallProgress>> getActiveInstalls();
    
    CompletableFuture<DependencyInfo> getDependencies(String skillId);
    
    CompletableFuture<DependencyResult> installDependencies(String skillId);
    
    CompletableFuture<DependencyResult> updateDependencies(String skillId);
    
    CompletableFuture<Boolean> checkDependencySatisfied(String skillId);
}

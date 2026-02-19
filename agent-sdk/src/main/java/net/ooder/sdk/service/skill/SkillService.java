
package net.ooder.sdk.service.skill;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.api.skill.InstallRequest;
import net.ooder.sdk.api.skill.InstallResult;
import net.ooder.sdk.api.skill.InstalledSkill;
import net.ooder.sdk.api.skill.SkillManifest;
import net.ooder.sdk.api.skill.SkillPackage;
import net.ooder.sdk.api.skill.SkillPackageManager;
import net.ooder.sdk.api.skill.UninstallResult;
import net.ooder.sdk.api.skill.UpdateResult;
import net.ooder.sdk.common.enums.DiscoveryMethod;

public class SkillService {
    
    private static final Logger log = LoggerFactory.getLogger(SkillService.class);
    
    private final SkillPackageManager packageManager;
    
    public SkillService(SkillPackageManager packageManager) {
        this.packageManager = packageManager;
    }
    
    public CompletableFuture<SkillPackage> discoverSkill(String skillId) {
        log.info("Discovering skill: {}", skillId);
        return packageManager.discover(skillId, DiscoveryMethod.LOCAL_FS);
    }
    
    public CompletableFuture<List<SkillPackage>> discoverAllSkills() {
        log.info("Discovering all skills");
        return packageManager.discoverAll(DiscoveryMethod.LOCAL_FS);
    }
    
    public CompletableFuture<InstallResult> installSkill(String skillId) {
        log.info("Installing skill: {}", skillId);
        InstallRequest request = new InstallRequest();
        request.setSkillId(skillId);
        return packageManager.install(request);
    }
    
    public CompletableFuture<UninstallResult> uninstallSkill(String skillId) {
        log.info("Uninstalling skill: {}", skillId);
        return packageManager.uninstall(skillId);
    }
    
    public CompletableFuture<UpdateResult> updateSkill(String skillId, String version) {
        log.info("Updating skill {} to version {}", skillId, version);
        return packageManager.update(skillId, version);
    }
    
    public CompletableFuture<List<InstalledSkill>> listInstalledSkills() {
        return packageManager.listInstalled();
    }
    
    public CompletableFuture<InstalledSkill> getInstalledSkill(String skillId) {
        return packageManager.getInstalled(skillId);
    }
    
    public CompletableFuture<Boolean> isSkillInstalled(String skillId) {
        return packageManager.isInstalled(skillId);
    }
    
    public CompletableFuture<SkillPackage> getSkillPackage(String skillId) {
        return packageManager.getPackage(skillId);
    }
    
    public CompletableFuture<SkillManifest> getSkillManifest(String skillId) {
        return packageManager.getManifest(skillId);
    }
    
    public CompletableFuture<List<SkillPackage>> searchSkills(String query) {
        log.info("Searching skills with query: {}", query);
        return packageManager.search(query, DiscoveryMethod.LOCAL_FS);
    }
    
    public String getSkillRootPath() {
        return packageManager.getSkillRootPath();
    }
    
    public void setSkillRootPath(String path) {
        packageManager.setSkillRootPath(path);
    }
}

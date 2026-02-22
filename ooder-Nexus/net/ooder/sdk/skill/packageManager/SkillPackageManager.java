package net.ooder.sdk.skill.packageManager;

import net.ooder.sdk.skill.packageManager.model.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface SkillPackageManager {

    CompletableFuture<InstallResult> installSkill(InstallRequest request);

    CompletableFuture<UninstallResult> uninstallSkill(String skillId);

    CompletableFuture<UpdateResult> updateSkill(String skillId, String version);

    List<InstalledSkill> getInstalledSkills();

    InstalledSkill getInstalledSkill(String skillId);

    CompletableFuture<List<SkillPackage>> discoverSkills(DiscoveryFilter filter);

    CompletableFuture<SkillPackage> getSkillInfo(String skillId);

    CompletableFuture<SceneJoinResult> requestScene(SceneRequest request);

    SkillConnectionInfo getSkillConnection(String skillId);

    SkillStatus getSkillStatus(String skillId);

    CompletableFuture<Boolean> testConnection(String skillId);

    CompletableFuture<Void> startSkill(String skillId);

    CompletableFuture<Void> stopSkill(String skillId);

    void registerObserver(SkillPackageObserver observer);

    void unregisterObserver(SkillPackageObserver observer);
}

package net.ooder.nexus.service.impl;

import net.ooder.nexus.service.SkillPackageService;
import net.ooder.sdk.api.skill.SkillPackage;
import net.ooder.sdk.api.skill.InstalledSkill;
import net.ooder.sdk.api.skill.InstallRequest;
import net.ooder.sdk.api.skill.InstallResult;
import net.ooder.sdk.api.skill.UninstallResult;
import net.ooder.sdk.api.skill.UpdateResult;
import net.ooder.sdk.api.skill.SkillPackageManager;
import net.ooder.sdk.common.enums.DiscoveryMethod;
import net.ooder.sdk.common.enums.DiscoveryMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class SkillPackageServiceImpl implements SkillPackageService {

    private static final Logger log = LoggerFactory.getLogger(SkillPackageServiceImpl.class);

    private final SkillPackageManager skillPackageManager;

    @Autowired
    public SkillPackageServiceImpl(SkillPackageManager skillPackageManager) {
        this.skillPackageManager = skillPackageManager;
        log.info("SkillPackageServiceImpl initialized with SDK 0.7.1");
    }

    @Override
    public CompletableFuture<Map<String, Object>> installSkill(String skillId, String version, Map<String, String> config) {
        log.info("Installing skill: {}, version: {}", skillId, version);

        if (skillPackageManager == null) {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("success", false);
            result.put("error", "SkillPackageManager not initialized");
            return CompletableFuture.completedFuture(result);
        }

        InstallRequest request = new InstallRequest();
        request.setSkillId(skillId);
        if (version != null && !version.isEmpty()) {
            request.setVersion(version);
        }
        request.setInstallDependencies(true);

        return skillPackageManager.install(request)
            .thenApply(result -> {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("success", result.isSuccess());
                map.put("skillId", result.getSkillId());
                map.put("version", result.getVersion());
                map.put("installPath", result.getInstallPath());
                map.put("installedDependencies", result.getInstalledDependencies());
                map.put("joinedScenes", result.getJoinedScenes());
                map.put("duration", result.getDuration());
                if (!result.isSuccess()) {
                    map.put("error", result.getError());
                }
                return map;
            });
    }

    @Override
    public CompletableFuture<Map<String, Object>> uninstallSkill(String skillId) {
        log.info("Uninstalling skill: {}", skillId);

        if (skillPackageManager == null) {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("success", false);
            result.put("error", "SkillPackageManager not initialized");
            return CompletableFuture.completedFuture(result);
        }

        return skillPackageManager.uninstall(skillId)
            .thenApply(result -> {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("success", result.isSuccess());
                map.put("skillId", result.getSkillId());
                if (!result.isSuccess()) {
                    map.put("error", result.getError());
                }
                return map;
            });
    }

    @Override
    public CompletableFuture<Map<String, Object>> updateSkill(String skillId, String version) {
        log.info("Updating skill: {} to version: {}", skillId, version);

        if (skillPackageManager == null) {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("success", false);
            result.put("error", "SkillPackageManager not initialized");
            return CompletableFuture.completedFuture(result);
        }

        return skillPackageManager.update(skillId, version)
            .thenApply(result -> {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("success", result.isSuccess());
                map.put("skillId", result.getSkillId());
                map.put("previousVersion", result.getPreviousVersion());
                map.put("newVersion", result.getNewVersion());
                if (!result.isSuccess()) {
                    map.put("error", result.getError());
                }
                return map;
            });
    }

    @Override
    public CompletableFuture<List<SkillPackage>> discoverSkills(String scene, String capability, String keyword) {
        log.info("Discovering skills: scene={}, capability={}, keyword={}", scene, capability, keyword);

        if (skillPackageManager == null) {
            return CompletableFuture.completedFuture(new ArrayList<SkillPackage>());
        }

        if (keyword != null && !keyword.isEmpty()) {
            return skillPackageManager.search(keyword, DiscoveryMethod.LOCAL_FS);
        }

        if (capability != null && !capability.isEmpty()) {
            return skillPackageManager.searchByCapability(capability, DiscoveryMethod.LOCAL_FS);
        }

        if (scene != null && !scene.isEmpty()) {
            return skillPackageManager.discoverByScene(scene, DiscoveryMethod.LOCAL_FS);
        }

        return skillPackageManager.discoverAll(DiscoveryMethod.LOCAL_FS);
    }

    @Override
    public CompletableFuture<SkillPackage> getSkillInfo(String skillId) {
        log.info("Getting skill info: {}", skillId);

        if (skillPackageManager == null) {
            return CompletableFuture.completedFuture(null);
        }

        return skillPackageManager.getPackage(skillId);
    }

    @Override
    public Map<String, Object> getSkillConnection(String skillId) {
        log.info("Getting skill connection: {}", skillId);

        Map<String, Object> result = new HashMap<String, Object>();

        if (skillPackageManager == null) {
            result.put("error", "SkillPackageManager not initialized");
            return result;
        }

        try {
            InstalledSkill skill = skillPackageManager.getInstalled(skillId).join();
            if (skill != null) {
                result.put("skillId", skill.getSkillId());
                result.put("name", skill.getName());
                result.put("status", skill.getStatus());
            }
        } catch (Exception e) {
            result.put("error", e.getMessage());
        }

        return result;
    }

    @Override
    public String getSkillStatus(String skillId) {
        log.info("Getting skill status: {}", skillId);

        if (skillPackageManager == null) {
            return "UNKNOWN";
        }

        try {
            InstalledSkill skill = skillPackageManager.getInstalled(skillId).join();
            if (skill != null && skill.getStatus() != null) {
                return skill.getStatus();
            }
            return "UNKNOWN";
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }

    @Override
    public CompletableFuture<Boolean> testConnection(String skillId) {
        log.info("Testing skill connection: {}", skillId);

        if (skillPackageManager == null) {
            return CompletableFuture.completedFuture(Boolean.FALSE);
        }

        return skillPackageManager.isInstalled(skillId);
    }

    @Override
    public CompletableFuture<Void> startSkill(String skillId) {
        log.info("Starting skill: {}", skillId);

        if (skillPackageManager == null) {
            CompletableFuture<Void> future = new CompletableFuture<Void>();
            future.completeExceptionally(new RuntimeException("SkillPackageManager not initialized"));
            return future;
        }

        return skillPackageManager.getInstalled(skillId)
            .thenCompose(skill -> {
                if (skill != null) {
                    log.info("Skill {} found, starting...", skillId);
                    return CompletableFuture.completedFuture(null);
                } else {
                    CompletableFuture<Void> f = new CompletableFuture<Void>();
                    f.completeExceptionally(new RuntimeException("Skill not found: " + skillId));
                    return f;
                }
            });
    }

    @Override
    public CompletableFuture<Void> stopSkill(String skillId) {
        log.info("Stopping skill: {}", skillId);

        if (skillPackageManager == null) {
            CompletableFuture<Void> future = new CompletableFuture<Void>();
            future.completeExceptionally(new RuntimeException("SkillPackageManager not initialized"));
            return future;
        }

        return skillPackageManager.getInstalled(skillId)
            .thenCompose(skill -> {
                if (skill != null) {
                    log.info("Skill {} found, stopping...", skillId);
                    return CompletableFuture.completedFuture(null);
                } else {
                    CompletableFuture<Void> f = new CompletableFuture<Void>();
                    f.completeExceptionally(new RuntimeException("Skill not found: " + skillId));
                    return f;
                }
            });
    }

    @Override
    public List<InstalledSkill> getInstalledSkills() {
        log.info("Getting all installed skills");

        if (skillPackageManager == null) {
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
    public InstalledSkill getInstalledSkill(String skillId) {
        log.info("Getting installed skill: {}", skillId);

        if (skillPackageManager == null) {
            return null;
        }

        try {
            return skillPackageManager.getInstalled(skillId).join();
        } catch (Exception e) {
            log.error("Failed to get installed skill: {}", skillId, e);
            return null;
        }
    }
}

package net.ooder.nexus.infrastructure.management;

import net.ooder.sdk.api.OoderSDK;
import net.ooder.sdk.api.skill.SkillPackageManager;
import net.ooder.sdk.api.skill.InstalledSkill;
import net.ooder.sdk.api.skill.SkillPackage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Nexus Skill Manager
 * Wraps SDK 0.7.1 SkillPackageManager
 *
 * @author ooder Team
 * @version 0.7.1
 */
public class NexusSkillManager {

    private static final Logger log = LoggerFactory.getLogger(NexusSkillManager.class);

    private OoderSDK sdk;
    private SkillPackageManager skillPackageManager;

    public NexusSkillManager() {
        log.info("NexusSkillManager initializing with SDK 0.7.1");
    }

    public void initialize(OoderSDK sdk) {
        this.sdk = sdk;
        this.skillPackageManager = sdk.getSkillPackageManager();
        log.info("NexusSkillManager initialized with OoderSDK");
    }

    public void setSkillPackageManager(SkillPackageManager packageManager) {
        this.skillPackageManager = packageManager;
        log.info("SkillPackageManager set");
    }

    public List<InstalledSkill> getInstalledSkills() {
        log.info("Getting installed skills");
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
                    CompletableFuture<Void> f = new CompletableFuture<>();
                    f.completeExceptionally(new RuntimeException("Skill not found: " + skillId));
                    return f;
                }
            });
    }

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
                    CompletableFuture<Void> f = new CompletableFuture<>();
                    f.completeExceptionally(new RuntimeException("Skill not found: " + skillId));
                    return f;
                }
            });
    }

    public String getSkillStatus(String skillId) {
        log.debug("Getting skill status: {}", skillId);
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
            log.error("Failed to get skill status: {}", skillId, e);
            return "UNKNOWN";
        }
    }

    public CompletableFuture<Boolean> testConnection(String skillId) {
        log.info("Testing skill connection: {}", skillId);
        if (skillPackageManager == null) {
            return CompletableFuture.completedFuture(Boolean.FALSE);
        }
        return skillPackageManager.isInstalled(skillId)
            .thenApply(installed -> installed != null && installed);
    }

    public SkillStatistics getStatistics() {
        log.info("Getting skill statistics");
        SkillStatistics stats = new SkillStatistics();
        
        if (skillPackageManager == null) {
            return stats;
        }

        try {
            List<InstalledSkill> skills = skillPackageManager.listInstalled().join();
            stats.totalSkills = skills.size();
            int running = 0;
            int stopped = 0;
            for (InstalledSkill s : skills) {
                if (s.getStatus() != null) {
                    String status = s.getStatus();
                    if ("RUNNING".equals(status) || "ACTIVE".equals(status)) {
                        running++;
                    } else if ("STOPPED".equals(status) || "INACTIVE".equals(status)) {
                        stopped++;
                    }
                }
            }
            stats.runningSkills = running;
            stats.stoppedSkills = stopped;
        } catch (Exception e) {
            log.error("Failed to get skill statistics", e);
        }

        return stats;
    }

    public CompletableFuture<SkillPackage> discoverSkill(String skillId) {
        log.info("Discovering skill: {}", skillId);
        if (skillPackageManager == null) {
            CompletableFuture<SkillPackage> future = new CompletableFuture<>();
            future.completeExceptionally(new RuntimeException("SkillPackageManager not initialized"));
            return future;
        }
        return skillPackageManager.discover(skillId, null);
    }

    public static class SkillStatistics {
        public int totalSkills;
        public int runningSkills;
        public int stoppedSkills;
        public int errorSkills;
    }
}

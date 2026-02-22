package net.ooder.nexus.service;

import net.ooder.sdk.api.skill.InstalledSkill;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Admin Skill Service Interface
 * Provides skill management functions for admin module
 *
 * @author ooder Team
 * @version 0.7.1
 */
public interface AdminSkillService {

    List<InstalledSkill> getAllSkills();

    InstalledSkill getSkillById(String skillId);

    CompletableFuture<InstalledSkill> approveSkill(String skillId);

    CompletableFuture<InstalledSkill> rejectSkill(String skillId);

    CompletableFuture<InstalledSkill> publishSkill(String skillId, Map<String, String> config);

    CompletableFuture<Boolean> unpublishSkill(String skillId);

    CompletableFuture<Boolean> deleteSkill(String skillId);

    List<InstalledSkill> getPendingSkills();

    List<InstalledSkill> getPublishedSkills();

    SkillStatistics getStatistics();

    public static class SkillStatistics {
        private int totalSkills;
        private int pendingSkills;
        private int publishedSkills;
        private int rejectedSkills;
        private int runningSkills;
        private int stoppedSkills;

        public int getTotalSkills() { return totalSkills; }
        public void setTotalSkills(int totalSkills) { this.totalSkills = totalSkills; }
        public int getPendingSkills() { return pendingSkills; }
        public void setPendingSkills(int pendingSkills) { this.pendingSkills = pendingSkills; }
        public int getPublishedSkills() { return publishedSkills; }
        public void setPublishedSkills(int publishedSkills) { this.publishedSkills = publishedSkills; }
        public int getRejectedSkills() { return rejectedSkills; }
        public void setRejectedSkills(int rejectedSkills) { this.rejectedSkills = rejectedSkills; }
        public int getRunningSkills() { return runningSkills; }
        public void setRunningSkills(int runningSkills) { this.runningSkills = runningSkills; }
        public int getStoppedSkills() { return stoppedSkills; }
        public void setStoppedSkills(int stoppedSkills) { this.stoppedSkills = stoppedSkills; }
    }
}

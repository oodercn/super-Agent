package net.ooder.nexus.service.impl;

import net.ooder.nexus.service.AdminSkillService;
import net.ooder.sdk.api.skill.InstalledSkill;
import net.ooder.sdk.api.skill.SkillPackageManager;

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

/**
 * Admin Skill Service Implementation
 *
 * @author ooder Team
 * @version 0.7.1
 */
@Service
public class AdminSkillServiceImpl implements AdminSkillService {

    private static final Logger log = LoggerFactory.getLogger(AdminSkillServiceImpl.class);

    private final SkillPackageManager skillPackageManager;
    private final Map<String, String> skillApprovalStatus = new ConcurrentHashMap<String, String>();
    private final Map<String, Map<String, Object>> skillMetadata = new ConcurrentHashMap<String, Map<String, Object>>();

    @Autowired
    public AdminSkillServiceImpl(SkillPackageManager skillPackageManager) {
        this.skillPackageManager = skillPackageManager;
        log.info("AdminSkillServiceImpl initialized with SDK 0.7.1 SkillPackageManager");
    }

    @Override
    public List<InstalledSkill> getAllSkills() {
        log.info("Getting all skills for admin");
        if (skillPackageManager == null) {
            return new ArrayList<InstalledSkill>();
        }
        try {
            return skillPackageManager.listInstalled().join();
        } catch (Exception e) {
            log.error("Failed to get all skills", e);
            return new ArrayList<InstalledSkill>();
        }
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
    public CompletableFuture<InstalledSkill> approveSkill(String skillId) {
        log.info("Approving skill: {}", skillId);
        skillApprovalStatus.put(skillId, "APPROVED");
        
        return skillPackageManager.getInstalled(skillId)
            .thenApply(skill -> skill);
    }

    @Override
    public CompletableFuture<InstalledSkill> rejectSkill(String skillId) {
        log.info("Rejecting skill: {}", skillId);
        skillApprovalStatus.put(skillId, "REJECTED");
        
        return skillPackageManager.getInstalled(skillId)
            .thenApply(skill -> skill);
    }

    @Override
    public CompletableFuture<InstalledSkill> publishSkill(String skillId, Map<String, String> config) {
        log.info("Publishing skill: {}", skillId);
        skillApprovalStatus.put(skillId, "PUBLISHED");
        skillMetadata.put(skillId, new HashMap<String, Object>(config));
        
        return skillPackageManager.getInstalled(skillId)
            .thenApply(skill -> skill);
    }

    @Override
    public CompletableFuture<Boolean> unpublishSkill(String skillId) {
        log.info("Unpublishing skill: {}", skillId);
        skillApprovalStatus.put(skillId, "UNPUBLISHED");
        return CompletableFuture.completedFuture(Boolean.TRUE);
    }

    @Override
    public CompletableFuture<Boolean> deleteSkill(String skillId) {
        log.info("Deleting skill: {}", skillId);
        skillApprovalStatus.remove(skillId);
        skillMetadata.remove(skillId);
        
        if (skillPackageManager != null) {
            return skillPackageManager.uninstall(skillId)
                .thenApply(result -> Boolean.valueOf(result.isSuccess()));
        }
        return CompletableFuture.completedFuture(Boolean.TRUE);
    }

    @Override
    public List<InstalledSkill> getPendingSkills() {
        log.info("Getting pending skills");
        List<InstalledSkill> pending = new ArrayList<InstalledSkill>();
        for (InstalledSkill skill : getAllSkills()) {
            String status = skillApprovalStatus.get(skill.getSkillId());
            if (status == null || "PENDING".equals(status)) {
                pending.add(skill);
            }
        }
        return pending;
    }

    @Override
    public List<InstalledSkill> getPublishedSkills() {
        log.info("Getting published skills");
        List<InstalledSkill> published = new ArrayList<InstalledSkill>();
        for (InstalledSkill skill : getAllSkills()) {
            String status = skillApprovalStatus.get(skill.getSkillId());
            if ("PUBLISHED".equals(status) || "APPROVED".equals(status)) {
                published.add(skill);
            }
        }
        return published;
    }

    @Override
    public SkillStatistics getStatistics() {
        log.info("Getting skill statistics");
        SkillStatistics stats = new SkillStatistics();
        
        List<InstalledSkill> skills = getAllSkills();
        stats.setTotalSkills(skills.size());
        
        int running = 0;
        int stopped = 0;
        int pending = 0;
        int published = 0;
        int rejected = 0;
        
        for (InstalledSkill skill : skills) {
            String approvalStatus = skillApprovalStatus.get(skill.getSkillId());
            if ("PENDING".equals(approvalStatus)) {
                pending++;
            } else if ("PUBLISHED".equals(approvalStatus) || "APPROVED".equals(approvalStatus)) {
                published++;
            } else if ("REJECTED".equals(approvalStatus)) {
                rejected++;
            }
            
            if (skill.getStatus() != null) {
                String status = skill.getStatus();
                if ("RUNNING".equals(status) || "ACTIVE".equals(status)) {
                    running++;
                } else if ("STOPPED".equals(status) || "INACTIVE".equals(status)) {
                    stopped++;
                }
            }
        }
        
        stats.setRunningSkills(running);
        stats.setStoppedSkills(stopped);
        stats.setPendingSkills(pending);
        stats.setPublishedSkills(published);
        stats.setRejectedSkills(rejected);
        
        return stats;
    }
}

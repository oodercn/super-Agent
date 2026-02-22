package net.ooder.nexus.service.impl;

import net.ooder.nexus.service.AdminDashboardService;
import net.ooder.nexus.service.AdminSkillService;
import net.ooder.nexus.service.AdminGroupService;
import net.ooder.sdk.api.skill.SkillPackageManager;
import net.ooder.sdk.api.skill.InstalledSkill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.*;

@Service
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private static final Logger log = LoggerFactory.getLogger(AdminDashboardServiceImpl.class);

    private final SkillPackageManager skillPackageManager;
    private final AdminSkillService adminSkillService;
    private final AdminGroupService adminGroupService;

    @Autowired
    public AdminDashboardServiceImpl(
            @Autowired(required = false) SkillPackageManager skillPackageManager,
            AdminSkillService adminSkillService,
            AdminGroupService adminGroupService) {
        this.skillPackageManager = skillPackageManager;
        this.adminSkillService = adminSkillService;
        this.adminGroupService = adminGroupService;
        log.info("AdminDashboardServiceImpl initialized with SDK 0.7.1");
    }

    @Override
    public DashboardStatistics getDashboardStats() {
        log.info("Getting dashboard stats");
        DashboardStatistics stats = new DashboardStatistics();

        try {
            if (skillPackageManager != null) {
                List<InstalledSkill> skills = skillPackageManager.listInstalled().get();
                stats.setTotalSkills(skills.size());
            }
            stats.setTotalGroups(adminGroupService.getStatistics().getTotalGroups());
        } catch (Exception e) {
            log.error("Error getting dashboard stats", e);
        }

        return stats;
    }

    @Override
    public List<Map<String, Object>> getRecentActivities(int limit) {
        return new ArrayList<Map<String, Object>>();
    }

    @Override
    public List<Map<String, Object>> getSystemAlerts() {
        return new ArrayList<Map<String, Object>>();
    }

    @Override
    public List<Map<String, Object>> getTopSkills(int limit) {
        return new ArrayList<Map<String, Object>>();
    }

    @Override
    public List<Map<String, Object>> getActiveUsers(int limit) {
        return new ArrayList<Map<String, Object>>();
    }

    @Override
    public SystemHealth getSystemHealth() {
        SystemHealth health = new SystemHealth();

        try {
            OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
            Runtime runtime = Runtime.getRuntime();

            long totalMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            long maxMemory = runtime.maxMemory();
            
            health.setStatus("healthy");
            health.setMemoryUsage((double) (totalMemory - freeMemory) / maxMemory * 100);
            health.setCpuUsage(osBean.getSystemLoadAverage());
        } catch (Exception e) {
            log.error("Error getting system health", e);
            health.setStatus("error");
        }

        return health;
    }
}

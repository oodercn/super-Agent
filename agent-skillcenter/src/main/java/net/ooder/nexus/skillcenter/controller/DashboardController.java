package net.ooder.nexus.skillcenter.controller;

import net.ooder.skillcenter.manager.SkillManager;
import net.ooder.skillcenter.market.SkillMarketManager;
import net.ooder.nexus.skillcenter.model.ResultModel;
import net.ooder.nexus.skillcenter.dto.dashboard.*;
import org.springframework.web.bind.annotation.*;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class DashboardController extends BaseController {

    private final SkillManager skillManager;
    private final SkillMarketManager marketManager;

    public DashboardController() {
        this.skillManager = SkillManager.getInstance();
        this.marketManager = SkillMarketManager.getInstance();
    }

    public DashboardController(SkillManager skillManager, SkillMarketManager marketManager) {
        this.skillManager = skillManager;
        this.marketManager = marketManager;
    }

    @PostMapping
    public ResultModel<DashboardStatsDTO> getDashboardStats() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getDashboardStats", null);

        try {
            DashboardStatsDTO stats = new DashboardStatsDTO();
            stats.setTotalSkills(skillManager.getAllSkills().size());
            stats.setTotalMarketSkills(marketManager.getAllSkills().size());

            DashboardStatsDTO.SystemInfoDTO systemInfo = new DashboardStatsDTO.SystemInfoDTO();
            systemInfo.setJavaVersion(System.getProperty("java.version"));
            systemInfo.setOsName(System.getProperty("os.name"));
            systemInfo.setOsVersion(System.getProperty("os.version"));
            systemInfo.setOsArch(System.getProperty("os.arch"));
            stats.setSystemInfo(systemInfo);

            DashboardStatsDTO.AppInfoDTO appInfo = new DashboardStatsDTO.AppInfoDTO();
            appInfo.setVersion("2.0");
            appInfo.setName("SkillCenter");
            appInfo.setDescription("技能中心管理系统");
            stats.setAppInfo(appInfo);

            logRequestEnd("getDashboardStats", stats, System.currentTimeMillis() - startTime);
            return ResultModel.success(stats);
        } catch (Exception e) {
            logRequestError("getDashboardStats", e);
            return ResultModel.error(500, "获取系统概览统计数据失败: " + e.getMessage());
        }
    }

    @PostMapping("/execution-stats")
    public ResultModel<ExecutionStatsDTO> getExecutionStats() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getExecutionStats", null);

        try {
            ExecutionStatsDTO stats = new ExecutionStatsDTO();
            stats.setTotalExecutions(1250);
            stats.setSuccessfulExecutions(1120);
            stats.setFailedExecutions(130);
            stats.setSuccessRate(89.6);
            stats.setAverageExecutionTime(2.5);

            Map<String, Integer> topExecutedSkills = new HashMap<>();
            topExecutedSkills.put("text-to-uppercase-skill", 320);
            topExecutedSkills.put("code-generation-skill", 280);
            topExecutedSkills.put("local-deployment-skill", 190);
            stats.setTopExecutedSkills(topExecutedSkills);

            Map<String, Integer> executionTrend = new HashMap<>();
            executionTrend.put("today", 45);
            executionTrend.put("yesterday", 38);
            executionTrend.put("last7Days", 280);
            executionTrend.put("last30Days", 1250);
            stats.setExecutionTrend(executionTrend);

            logRequestEnd("getExecutionStats", stats, System.currentTimeMillis() - startTime);
            return ResultModel.success(stats);
        } catch (Exception e) {
            logRequestError("getExecutionStats", e);
            return ResultModel.error(500, "获取技能执行统计数据失败: " + e.getMessage());
        }
    }

    @PostMapping("/market-stats")
    public ResultModel<MarketStatsDTO> getMarketStats() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getMarketStats", null);

        try {
            MarketStatsDTO stats = new MarketStatsDTO();
            stats.setTotalMarketSkills(marketManager.getAllSkills().size());
            stats.setTotalDownloads(3500);
            stats.setTotalReviews(890);
            stats.setAverageRating(4.2);

            Map<String, Integer> topDownloadedSkills = new HashMap<>();
            topDownloadedSkills.put("text-to-uppercase-skill", 850);
            topDownloadedSkills.put("code-generation-skill", 720);
            topDownloadedSkills.put("local-deployment-skill", 580);
            stats.setTopDownloadedSkills(topDownloadedSkills);

            Map<String, Integer> marketTrend = new HashMap<>();
            marketTrend.put("today", 120);
            marketTrend.put("yesterday", 105);
            marketTrend.put("last7Days", 780);
            marketTrend.put("last30Days", 3500);
            stats.setMarketTrend(marketTrend);

            Map<String, Integer> categoryDistribution = new HashMap<>();
            categoryDistribution.put("text-processing", 15);
            categoryDistribution.put("development", 25);
            categoryDistribution.put("deployment", 10);
            categoryDistribution.put("media", 8);
            categoryDistribution.put("storage", 5);
            stats.setCategoryDistribution(categoryDistribution);

            logRequestEnd("getMarketStats", stats, System.currentTimeMillis() - startTime);
            return ResultModel.success(stats);
        } catch (Exception e) {
            logRequestError("getMarketStats", e);
            return ResultModel.error(500, "获取市场活跃度统计数据失败: " + e.getMessage());
        }
    }

    @PostMapping("/system-stats")
    public ResultModel<SystemStatsDTO> getSystemStats() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getSystemStats", null);

        try {
            SystemStatsDTO stats = new SystemStatsDTO();

            com.sun.management.OperatingSystemMXBean sunOsBean = 
                    (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

            double cpuUsage = sunOsBean.getSystemCpuLoad() * 100;
            stats.setCpuUsage(Math.round(cpuUsage * 10) / 10.0);

            long totalMemory = sunOsBean.getTotalPhysicalMemorySize();
            long freeMemory = sunOsBean.getFreePhysicalMemorySize();
            long usedMemory = totalMemory - freeMemory;
            double memoryUsage = (double) usedMemory / totalMemory * 100;

            stats.setMemoryUsage(Math.round(memoryUsage * 10) / 10.0);
            stats.setTotalMemory(formatFileSize(totalMemory));
            stats.setUsedMemory(formatFileSize(usedMemory));
            stats.setFreeMemory(formatFileSize(freeMemory));

            double systemLoad = ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();
            stats.setSystemLoad(Math.round(systemLoad * 10) / 10.0);

            int availableProcessors = ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
            stats.setAvailableProcessors(availableProcessors);

            long uptime = ManagementFactory.getRuntimeMXBean().getUptime() / 1000;
            stats.setUptime(formatUptime(uptime));

            logRequestEnd("getSystemStats", stats, System.currentTimeMillis() - startTime);
            return ResultModel.success(stats);
        } catch (Exception e) {
            logRequestError("getSystemStats", e);
            return ResultModel.error(500, "获取系统资源使用统计数据失败: " + e.getMessage());
        }
    }

    private String formatFileSize(long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return (size / 1024) + " KB";
        } else if (size < 1024 * 1024 * 1024) {
            return (size / (1024 * 1024)) + " MB";
        } else {
            return (size / (1024 * 1024 * 1024)) + " GB";
        }
    }

    private String formatUptime(long seconds) {
        long days = seconds / (24 * 3600);
        long hours = (seconds % (24 * 3600)) / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;

        if (days > 0) {
            return days + "d " + hours + "h " + minutes + "m " + secs + "s";
        } else if (hours > 0) {
            return hours + "h " + minutes + "m " + secs + "s";
        } else if (minutes > 0) {
            return minutes + "m " + secs + "s";
        } else {
            return secs + "s";
        }
    }
}

package net.ooder.skillcenter.controller;

import net.ooder.skillcenter.manager.SkillManager;
import net.ooder.skillcenter.market.SkillMarketManager;
import net.ooder.skillcenter.model.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.HashMap;
import java.util.Map;

/**
 * 仪表盘REST API控制器
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final SkillManager skillManager;
    private final SkillMarketManager marketManager;

    /**
     * 构造方法，初始化管理器
     */
    public DashboardController() {
        this.skillManager = SkillManager.getInstance();
        this.marketManager = SkillMarketManager.getInstance();
    }

    /**
     * 构造方法，用于依赖注入（测试用）
     */
    public DashboardController(SkillManager skillManager, SkillMarketManager marketManager) {
        this.skillManager = skillManager;
        this.marketManager = marketManager;
    }

    /**
     * 获取系统概览统计数据
     * @return 系统概览统计数据
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        try {
            // 技能统计
            int totalSkills = skillManager.getAllSkills().size();
            stats.put("totalSkills", totalSkills);

            // 市场统计
            int totalMarketSkills = marketManager.getAllSkills().size();
            stats.put("totalMarketSkills", totalMarketSkills);

            // 系统信息
            Map<String, String> systemInfo = new HashMap<>();
            systemInfo.put("javaVersion", System.getProperty("java.version"));
            systemInfo.put("osName", System.getProperty("os.name"));
            systemInfo.put("osVersion", System.getProperty("os.version"));
            systemInfo.put("osArch", System.getProperty("os.arch"));
            stats.put("systemInfo", systemInfo);

            // 应用信息
            Map<String, String> appInfo = new HashMap<>();
            appInfo.put("version", "0.6.5");
            appInfo.put("name", "SkillCenter");
            appInfo.put("description", "技能中心管理系统");
            stats.put("appInfo", appInfo);

            return ResponseEntity.ok(ApiResponse.success(stats));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "获取系统概览统计数据失败: " + e.getMessage()));
        }
    }

    /**
     * 获取技能执行统计数据
     * @return 技能执行统计数据
     */
    @GetMapping("/execution-stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getExecutionStats() {
        Map<String, Object> stats = new HashMap<>();

        try {
            // 模拟执行统计数据
            // 实际项目中，这里应该从执行历史记录中获取真实数据
            stats.put("totalExecutions", 1250);
            stats.put("successfulExecutions", 1120);
            stats.put("failedExecutions", 130);
            stats.put("successRate", 89.6);
            stats.put("averageExecutionTime", 2.5); // 平均执行时间（秒）
            // 最常执行的技能
            Map<String, Integer> topExecutedSkills = new HashMap<>();
            topExecutedSkills.put("text-to-uppercase-skill", 320);
            topExecutedSkills.put("code-generation-skill", 280);
            topExecutedSkills.put("local-deployment-skill", 190);
            stats.put("topExecutedSkills", topExecutedSkills);
            
            // 执行趋势
            Map<String, Integer> executionTrend = new HashMap<>();
            executionTrend.put("today", 45);
            executionTrend.put("yesterday", 38);
            executionTrend.put("last7Days", 280);
            executionTrend.put("last30Days", 1250);
            stats.put("executionTrend", executionTrend);

            return ResponseEntity.ok(ApiResponse.success(stats));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "获取技能执行统计数据失败: " + e.getMessage()));
        }
    }

    /**
     * 获取市场活跃度统计数据
     * @return 市场活跃度统计数据
     */
    @GetMapping("/market-stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMarketStats() {
        Map<String, Object> stats = new HashMap<>();

        try {
            // 市场技能统计
            int totalMarketSkills = marketManager.getAllSkills().size();
            stats.put("totalMarketSkills", totalMarketSkills);

            // 模拟市场统计数据
            stats.put("totalDownloads", 3500);
            stats.put("totalReviews", 890);
            stats.put("averageRating", 4.2);
            // 最常下载的技能
            Map<String, Integer> topDownloadedSkills = new HashMap<>();
            topDownloadedSkills.put("text-to-uppercase-skill", 850);
            topDownloadedSkills.put("code-generation-skill", 720);
            topDownloadedSkills.put("local-deployment-skill", 580);
            stats.put("topDownloadedSkills", topDownloadedSkills);
            
            // 市场趋势
            Map<String, Integer> marketTrend = new HashMap<>();
            marketTrend.put("today", 120);
            marketTrend.put("yesterday", 105);
            marketTrend.put("last7Days", 780);
            marketTrend.put("last30Days", 3500);
            stats.put("marketTrend", marketTrend);
            
            // 分类分布
            Map<String, Integer> categoryDistribution = new HashMap<>();
            categoryDistribution.put("text-processing", 15);
            categoryDistribution.put("development", 25);
            categoryDistribution.put("deployment", 10);
            categoryDistribution.put("media", 8);
            categoryDistribution.put("storage", 5);
            stats.put("categoryDistribution", categoryDistribution);

            return ResponseEntity.ok(ApiResponse.success(stats));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "获取市场活跃度统计数据失败: " + e.getMessage()));
        }
    }

    /**
     * 获取系统资源使用统计数据
     * @return 系统资源使用统计数据
     */
    @GetMapping("/system-stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSystemStats() {
        Map<String, Object> stats = new HashMap<>();

        try {
            // 获取系统资源使用情况
            OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
            com.sun.management.OperatingSystemMXBean sunOsBean = (com.sun.management.OperatingSystemMXBean) osBean;

            // CPU使用率
            double cpuUsage = sunOsBean.getSystemCpuLoad() * 100;
            stats.put("cpuUsage", Math.round(cpuUsage * 10) / 10.0);

            // 内存使用情况
            long totalMemory = sunOsBean.getTotalPhysicalMemorySize();
            long freeMemory = sunOsBean.getFreePhysicalMemorySize();
            long usedMemory = totalMemory - freeMemory;
            double memoryUsage = (double) usedMemory / totalMemory * 100;

            stats.put("memoryUsage", Math.round(memoryUsage * 10) / 10.0);
            stats.put("totalMemory", formatFileSize(totalMemory));
            stats.put("usedMemory", formatFileSize(usedMemory));
            stats.put("freeMemory", formatFileSize(freeMemory));

            // 系统负载
            double systemLoad = osBean.getSystemLoadAverage();
            stats.put("systemLoad", Math.round(systemLoad * 10) / 10.0);

            // 可用处理器数量
            int availableProcessors = osBean.getAvailableProcessors();
            stats.put("availableProcessors", availableProcessors);

            // 系统运行时间
            long uptime = ManagementFactory.getRuntimeMXBean().getUptime() / 1000;
            stats.put("uptime", formatUptime(uptime));

            return ResponseEntity.ok(ApiResponse.success(stats));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "获取系统资源使用统计数据失败: " + e.getMessage()));
        }
    }

    /**
     * 格式化文件大小
     * @param size 字节大小
     * @return 格式化后的大小
     */
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

    /**
     * 格式化运行时间
     * @param seconds 秒数
     * @return 格式化后的时间
     */
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

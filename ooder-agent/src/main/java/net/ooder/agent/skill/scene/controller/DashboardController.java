package net.ooder.mvp.skill.scene.controller;

import net.ooder.mvp.skill.scene.model.ResultModel;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/dashboard")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class DashboardController extends BaseController {

    @PostMapping("/stats")
    public ResultModel<Map<String, Object>> getStats() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getStats", "dashboard");

        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalSkills", 12);
            stats.put("activeSkills", 8);
            stats.put("totalScenes", 5);
            stats.put("activeScenes", 3);
            stats.put("totalUsers", 1);
            stats.put("activeUsers", 1);
            
            logRequestEnd("getStats", stats, System.currentTimeMillis() - startTime);
            return ResultModel.success(stats);
        } catch (Exception e) {
            logRequestError("getStats", e);
            return ResultModel.error(500, "获取统计数据失败: " + e.getMessage());
        }
    }

    @PostMapping("/execution-stats")
    public ResultModel<Map<String, Object>> getExecutionStats() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getExecutionStats", "dashboard");

        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalExecutions", 156);
            stats.put("successCount", 142);
            stats.put("failureCount", 14);
            stats.put("successRate", 91);
            stats.put("avgExecutionTime", 2350);
            
            logRequestEnd("getExecutionStats", stats, System.currentTimeMillis() - startTime);
            return ResultModel.success(stats);
        } catch (Exception e) {
            logRequestError("getExecutionStats", e);
            return ResultModel.error(500, "获取执行统计失败: " + e.getMessage());
        }
    }

    @PostMapping("/market-stats")
    public ResultModel<Map<String, Object>> getMarketStats() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getMarketStats", "dashboard");

        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalDownloads", 89);
            stats.put("totalReviews", 23);
            stats.put("avgRating", 4.5);
            stats.put("trendingSkills", Arrays.asList("daily-report", "data-analysis", "code-review"));
            
            logRequestEnd("getMarketStats", stats, System.currentTimeMillis() - startTime);
            return ResultModel.success(stats);
        } catch (Exception e) {
            logRequestError("getMarketStats", e);
            return ResultModel.error(500, "获取市场统计失败: " + e.getMessage());
        }
    }

    @PostMapping("/system-stats")
    public ResultModel<Map<String, Object>> getSystemStats() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getSystemStats", "dashboard");

        try {
            Runtime runtime = Runtime.getRuntime();
            long totalMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            long usedMemory = totalMemory - freeMemory;
            long maxMemory = runtime.maxMemory();
            
            int memoryUsage = (int) ((usedMemory * 100) / maxMemory);
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("cpuUsage", 25);
            stats.put("memoryUsage", memoryUsage);
            stats.put("totalMemory", totalMemory / (1024 * 1024));
            stats.put("usedMemory", usedMemory / (1024 * 1024));
            stats.put("maxMemory", maxMemory / (1024 * 1024));
            stats.put("threadCount", Thread.activeCount());
            stats.put("uptime", System.currentTimeMillis());
            
            logRequestEnd("getSystemStats", stats, System.currentTimeMillis() - startTime);
            return ResultModel.success(stats);
        } catch (Exception e) {
            logRequestError("getSystemStats", e);
            return ResultModel.error(500, "获取系统统计失败: " + e.getMessage());
        }
    }
}

package net.ooder.mvp.skill.scene.controller;

import net.ooder.mvp.skill.scene.dto.PageResult;
import net.ooder.mvp.skill.scene.dto.llm.*;
import net.ooder.mvp.skill.scene.model.ResultModel;
import net.ooder.mvp.skill.scene.integration.SceneEngineIntegration;
import net.ooder.mvp.skill.scene.service.LlmCallLogService;
import net.ooder.mvp.skill.scene.service.LlmStatsService;
import net.ooder.mvp.skill.scene.service.LlmStatsService.LlmLogQuery;
import net.ooder.scene.llm.audit.LlmAuditService;
import net.ooder.scene.llm.stats.LlmTrendStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.*;

@RestController
@RequestMapping("/api/v1/llm/monitor")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LlmMonitorController {

    private static final Logger log = LoggerFactory.getLogger(LlmMonitorController.class);
    
    @Autowired(required = false)
    private SceneEngineIntegration engineIntegration;
    
    @Autowired
    private LlmCallLogService llmCallLogService;
    
    @Autowired
    private LlmStatsService llmStatsService;
    
    @Autowired(required = false)
    private LlmAuditService llmAuditService;

    @GetMapping("/stats")
    public ResultModel<Map<String, Object>> getStats(
            @RequestParam(required = false) String timeRange,
            @RequestParam(required = false) String providerId) {
        log.info("[getStats] timeRange: {}, providerId: {}", timeRange, providerId);
        
        Map<String, Object> stats = new HashMap<String, Object>();
        
        if (llmAuditService != null) {
            try {
                long endTime = System.currentTimeMillis();
                long startTime = endTime - parseTimeRange(timeRange);
                
                LlmTrendStats trendStats = llmAuditService.getTrendStats("default", startTime, endTime).join();
                if (trendStats != null) {
                    stats.put("totalCalls", trendStats.getTotalCalls());
                    stats.put("totalTokens", trendStats.getTotalTokens());
                    stats.put("totalCost", trendStats.getTotalCost());
                    stats.put("avgLatency", trendStats.getAvgLatency());
                    stats.put("successRate", trendStats.getSuccessRate());
                    stats.put("callsTrend", trendStats.getCallsTrend());
                    stats.put("tokensTrend", trendStats.getTokensTrend());
                    stats.put("costTrend", trendStats.getCostTrend());
                    stats.put("source", "sdk");
                    return ResultModel.success(stats);
                }
            } catch (Exception e) {
                log.warn("Failed to get stats from SDK: {}", e.getMessage());
            }
        }
        
        if (engineIntegration != null && engineIntegration.isSdkAvailable()) {
            try {
                Map<String, Object> engineStats = getEngineMonitorStats();
                if (engineStats != null && !engineStats.isEmpty()) {
                    stats.putAll(engineStats);
                    stats.put("source", "engine");
                }
            } catch (Exception e) {
                log.warn("Failed to get engine monitor stats: {}", e.getMessage());
            }
        }
        
        if (stats.isEmpty()) {
            LlmStatsSummaryDTO localStats = llmCallLogService.getStats(providerId);
            stats.put("totalCalls", localStats.getTotalCalls());
            stats.put("totalTokens", localStats.getTotalTokens());
            stats.put("totalCost", localStats.getTotalCost());
            stats.put("avgLatency", localStats.getAvgLatency());
            stats.put("successRate", localStats.getSuccessRate());
            stats.put("errorCount", localStats.getErrorCount());
            stats.put("source", "local");
        }
        
        return ResultModel.success(stats);
    }
    
    private long parseTimeRange(String timeRange) {
        if (timeRange == null || timeRange.isEmpty()) {
            return 7 * 24 * 60 * 60 * 1000L;
        }
        switch (timeRange) {
            case "24h": return 24 * 60 * 60 * 1000L;
            case "7d": return 7 * 24 * 60 * 60 * 1000L;
            case "30d": return 30L * 24 * 60 * 60 * 1000L;
            default: return 7 * 24 * 60 * 60 * 1000L;
        }
    }
    
    private Map<String, Object> getEngineMonitorStats() {
        Map<String, Object> stats = new HashMap<String, Object>();
        
        try {
            Object sceneMonitor = engineIntegration.getSdkService("sceneMonitor");
            if (sceneMonitor != null) {
                Method getPerformanceMonitor = sceneMonitor.getClass().getMethod("getPerformanceMonitor");
                Object performanceMonitor = getPerformanceMonitor.invoke(sceneMonitor);
                
                if (performanceMonitor != null) {
                    Method getCurrentMetrics = performanceMonitor.getClass().getMethod("getCurrentMetrics", String.class);
                    Object metrics = getCurrentMetrics.invoke(performanceMonitor, "default");
                    
                    if (metrics != null) {
                        stats.put("engineMetrics", convertToMap(metrics));
                    }
                }
            }
        } catch (Exception e) {
            log.debug("Engine monitor not available: {}", e.getMessage());
        }
        
        return stats;
    }
    
    private Map<String, Object> convertToMap(Object obj) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Method[] methods = obj.getClass().getMethods();
            for (Method method : methods) {
                String name = method.getName();
                if (name.startsWith("get") && method.getParameterCount() == 0 && !name.equals("getClass")) {
                    String key = Character.toLowerCase(name.charAt(3)) + name.substring(4);
                    Object value = method.invoke(obj);
                    if (value != null && !value.getClass().getName().startsWith("java.lang.Class")) {
                        map.put(key, value);
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Failed to convert object to map: {}", e.getMessage());
        }
        return map;
    }
    
    @GetMapping("/logs")
    public ResultModel<PageResult<LlmCallLogDTO>> getLogs(
            @RequestParam(required = false) String providerId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String model,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {
        log.info("[getLogs] providerId: {}, status: {}, pageNum: {}, pageSize: {}", providerId, status, pageNum, pageSize);
        
        List<LlmCallLogDTO> logs = llmCallLogService.getLogs(providerId, status, model, pageNum, pageSize);
        int total = llmCallLogService.getTotalCount(providerId, status, model);
        
        PageResult<LlmCallLogDTO> result = new PageResult<LlmCallLogDTO>();
        result.setList(logs);
        result.setTotal(total);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        
        return ResultModel.success(result);
    }
    
    @GetMapping("/logs/{logId}")
    public ResultModel<LlmCallLogDTO> getLogDetail(@PathVariable String logId) {
        log.info("[getLogDetail] logId: {}", logId);
        
        LlmCallLogDTO logEntry = llmCallLogService.getLogById(logId);
        if (logEntry != null) {
            return ResultModel.success(logEntry);
        }
        
        return ResultModel.notFound("Log not found: " + logId);
    }
    
    @GetMapping("/provider-stats")
    public ResultModel<List<ProviderStatsDTO>> getProviderStats() {
        log.info("[getProviderStats] request start");
        
        List<ProviderStatsDTO> result = llmCallLogService.getProviderStats();
        
        return ResultModel.success(result);
    }
    
    @GetMapping("/engine/status")
    public ResultModel<Map<String, Object>> getEngineStatus() {
        log.info("[getEngineStatus] request start");
        
        Map<String, Object> status = new HashMap<String, Object>();
        
        boolean sdkAvailable = engineIntegration != null && engineIntegration.isSdkAvailable();
        status.put("sdkAvailable", sdkAvailable);
        status.put("monitorEnabled", sdkAvailable);
        status.put("dataSource", sdkAvailable ? "engine" : "local");
        
        return ResultModel.success(status);
    }
    
    @DeleteMapping("/logs")
    public ResultModel<Boolean> clearLogs() {
        log.info("[clearLogs] request start");
        
        llmCallLogService.clearLogs();
        
        return ResultModel.success(true);
    }
    
    // ==================== 四级统计 API ====================
    
    @GetMapping("/stats/company/{companyId}")
    public ResultModel<CompanyLlmStatsDTO> getCompanyStats(
            @PathVariable String companyId,
            @RequestParam(required = false) Long startTime,
            @RequestParam(required = false) Long endTime) {
        log.info("[getCompanyStats] companyId: {}", companyId);
        
        StatsTimeRange range = buildTimeRange(startTime, endTime);
        CompanyLlmStatsDTO stats = llmStatsService.getCompanyStats(companyId, range);
        
        return ResultModel.success(stats);
    }
    
    @GetMapping("/stats/department/{departmentId}")
    public ResultModel<DepartmentLlmStatsDTO> getDepartmentStats(
            @PathVariable String departmentId,
            @RequestParam(required = false) Long startTime,
            @RequestParam(required = false) Long endTime) {
        log.info("[getDepartmentStats] departmentId: {}", departmentId);
        
        StatsTimeRange range = buildTimeRange(startTime, endTime);
        DepartmentLlmStatsDTO stats = llmStatsService.getDepartmentStats(departmentId, range);
        
        return ResultModel.success(stats);
    }
    
    @GetMapping("/stats/user/{userId}")
    public ResultModel<UserLlmStatsDTO> getUserStats(
            @PathVariable String userId,
            @RequestParam(required = false) Long startTime,
            @RequestParam(required = false) Long endTime) {
        log.info("[getUserStats] userId: {}", userId);
        
        StatsTimeRange range = buildTimeRange(startTime, endTime);
        UserLlmStatsDTO stats = llmStatsService.getUserStats(userId, range);
        
        return ResultModel.success(stats);
    }
    
    @GetMapping("/stats/module/{moduleId}")
    public ResultModel<ModuleLlmStatsDTO> getModuleStats(
            @PathVariable String moduleId,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) Long startTime,
            @RequestParam(required = false) Long endTime) {
        log.info("[getModuleStats] moduleId: {}, userId: {}", moduleId, userId);
        
        StatsTimeRange range = buildTimeRange(startTime, endTime);
        ModuleLlmStatsDTO stats = llmStatsService.getModuleStats(moduleId, userId, range);
        
        return ResultModel.success(stats);
    }
    
    @GetMapping("/ranking/departments")
    public ResultModel<List<DepartmentLlmStatsDTO>> getDepartmentRanking(
            @RequestParam(required = false) String companyId,
            @RequestParam(defaultValue = "10") int topN,
            @RequestParam(defaultValue = "calls") String orderBy) {
        log.info("[getDepartmentRanking] companyId: {}, topN: {}, orderBy: {}", companyId, topN, orderBy);
        
        List<DepartmentLlmStatsDTO> ranking = llmStatsService.getDepartmentRanking(companyId, topN, orderBy);
        
        return ResultModel.success(ranking);
    }
    
    @GetMapping("/ranking/users")
    public ResultModel<List<UserLlmStatsDTO>> getUserRanking(
            @RequestParam(required = false) String departmentId,
            @RequestParam(defaultValue = "10") int topN,
            @RequestParam(defaultValue = "calls") String orderBy) {
        log.info("[getUserRanking] departmentId: {}, topN: {}, orderBy: {}", departmentId, topN, orderBy);
        
        List<UserLlmStatsDTO> ranking = llmStatsService.getUserRanking(departmentId, topN, orderBy);
        
        return ResultModel.success(ranking);
    }
    
    @GetMapping("/ranking/modules")
    public ResultModel<List<ModuleLlmStatsDTO>> getModuleRanking(
            @RequestParam(required = false) String userId,
            @RequestParam(defaultValue = "10") int topN,
            @RequestParam(defaultValue = "calls") String orderBy) {
        log.info("[getModuleRanking] userId: {}, topN: {}, orderBy: {}", userId, topN, orderBy);
        
        List<ModuleLlmStatsDTO> ranking = llmStatsService.getModuleRanking(userId, topN, orderBy);
        
        return ResultModel.success(ranking);
    }
    
    @GetMapping("/logs-v2")
    public ResultModel<PageResult<LlmCallLogDTO>> getLogsV2(
            @RequestParam(required = false) String companyId,
            @RequestParam(required = false) String departmentId,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String moduleId,
            @RequestParam(required = false) String providerId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) Long startTime,
            @RequestParam(required = false) Long endTime,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {
        log.info("[getLogsV2] multi-dimension query");
        
        LlmLogQuery query = new LlmLogQuery();
        query.setCompanyId(companyId);
        query.setDepartmentId(departmentId);
        query.setUserId(userId);
        query.setModuleId(moduleId);
        query.setProviderId(providerId);
        query.setStatus(status);
        query.setModel(model);
        query.setStartTime(startTime);
        query.setEndTime(endTime);
        query.setPageNum(pageNum);
        query.setPageSize(pageSize);
        
        List<LlmCallLogDTO> logs = llmStatsService.getLogs(query);
        int total = llmStatsService.getTotalCount(query);
        
        PageResult<LlmCallLogDTO> result = new PageResult<>();
        result.setList(logs);
        result.setTotal(total);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        
        return ResultModel.success(result);
    }
    
    @GetMapping("/overall-stats")
    public ResultModel<OverallStatsDTO> getOverallStats(
            @RequestParam(required = false) String companyId,
            @RequestParam(required = false) Long startTime,
            @RequestParam(required = false) Long endTime) {
        log.info("[getOverallStats] companyId: {}", companyId);
        
        StatsTimeRange range = buildTimeRange(startTime, endTime);
        OverallStatsDTO stats = llmStatsService.getOverallStats(companyId, range);
        
        return ResultModel.success(stats);
    }
    
    private StatsTimeRange buildTimeRange(Long startTime, Long endTime) {
        if (startTime == null && endTime == null) {
            return null;
        }
        
        long end = endTime != null ? endTime : System.currentTimeMillis();
        long start = startTime != null ? startTime : end - 7 * 24 * 60 * 60 * 1000L;
        
        return new StatsTimeRange(start, end);
    }
}

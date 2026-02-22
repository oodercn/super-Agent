package net.ooder.nexus.skillcenter.controller;

import net.ooder.nexus.skillcenter.dto.monitor.*;
import net.ooder.nexus.skillcenter.dto.PageResult;
import net.ooder.nexus.skillcenter.model.ResultModel;
import net.ooder.skillcenter.manager.SkillManager;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/monitor")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class MonitorController extends BaseController {

    private final SkillManager skillManager;
    private final Map<String, SkillMonitorDTO> monitorCache;
    private final Map<String, List<AlertDTO>> alertStore;

    public MonitorController() {
        this.skillManager = SkillManager.getInstance();
        this.monitorCache = new ConcurrentHashMap<>();
        this.alertStore = new ConcurrentHashMap<>();
        initMockData();
    }

    private void initMockData() {
        List<net.ooder.skillcenter.model.Skill> skills = skillManager.getAllSkills();
        for (net.ooder.skillcenter.model.Skill skill : skills) {
            SkillMonitorDTO monitor = createMockMonitor(skill.getId(), skill.getName());
            monitorCache.put(skill.getId(), monitor);
        }
    }

    private SkillMonitorDTO createMockMonitor(String skillId, String name) {
        SkillMonitorDTO monitor = new SkillMonitorDTO();
        monitor.setSkillId(skillId);
        monitor.setInstanceId("inst-" + skillId);
        monitor.setName(name);
        monitor.setStatus("running");
        monitor.setStartTime(System.currentTimeMillis() - 3600000);
        monitor.setUptime(3600000);

        SkillMonitorDTO.ResourceMetrics resources = new SkillMonitorDTO.ResourceMetrics();
        resources.setCpuUsage(15.0 + Math.random() * 30);
        resources.setMemoryUsed((long)(128 + Math.random() * 256) * 1024 * 1024);
        resources.setMemoryLimit(512L * 1024 * 1024);
        resources.setMemoryUsagePercent(resources.getMemoryUsed() * 100.0 / resources.getMemoryLimit());
        resources.setNetworkIn((long)(Math.random() * 1024 * 1024));
        resources.setNetworkOut((long)(Math.random() * 512 * 1024));
        resources.setDiskRead((long)(Math.random() * 10 * 1024 * 1024));
        resources.setDiskWrite((long)(Math.random() * 5 * 1024 * 1024));
        resources.setFileDescriptors((int)(20 + Math.random() * 30));
        monitor.setResources(resources);

        SkillMonitorDTO.ExecutionMetrics execution = new SkillMonitorDTO.ExecutionMetrics();
        execution.setTotalExecutions((long)(100 + Math.random() * 500));
        execution.setSuccessCount((long)(execution.getTotalExecutions() * 0.95));
        execution.setFailureCount(execution.getTotalExecutions() - execution.getSuccessCount());
        execution.setSuccessRate(95.0 + Math.random() * 4);
        execution.setAvgExecutionTime(50 + Math.random() * 150);
        execution.setMaxExecutionTime((long)(200 + Math.random() * 300));
        execution.setMinExecutionTime((long)(10 + Math.random() * 20));
        execution.setCurrentConcurrency((int)(Math.random() * 5));
        execution.setQueueSize((int)(Math.random() * 10));
        execution.setThroughput(10 + Math.random() * 20);
        monitor.setExecution(execution);

        SkillMonitorDTO.HealthStatus health = new SkillMonitorDTO.HealthStatus();
        health.setStatus("healthy");
        health.setMessage("Service is running normally");
        health.setLastCheckTime(System.currentTimeMillis());
        health.setConsecutiveFailures(0);
        health.setLastHealthyTime(System.currentTimeMillis());
        monitor.setHealth(health);

        List<SkillMonitorDTO.LogEntry> logs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            SkillMonitorDTO.LogEntry entry = new SkillMonitorDTO.LogEntry();
            entry.setTimestamp(System.currentTimeMillis() - i * 60000);
            entry.setLevel(i % 3 == 0 ? "INFO" : (i % 3 == 1 ? "DEBUG" : "WARN"));
            entry.setMessage("Sample log message " + i);
            entry.setSource(skillId);
            logs.add(entry);
        }
        monitor.setRecentLogs(logs);

        return monitor;
    }

    @PostMapping("/list")
    public ResultModel<MonitorListDTO> getMonitorList() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getMonitorList", null);

        try {
            MonitorListDTO result = new MonitorListDTO();
            List<SkillMonitorDTO> monitors = new ArrayList<>(monitorCache.values());
            result.setMonitors(monitors);
            result.setTotal(monitors.size());
            result.setRunning((int) monitors.stream().filter(m -> "running".equals(m.getStatus())).count());
            result.setStopped((int) monitors.stream().filter(m -> "stopped".equals(m.getStatus())).count());
            result.setError((int) monitors.stream().filter(m -> "error".equals(m.getStatus())).count());
            result.setAvgCpuUsage(monitors.stream()
                .mapToDouble(m -> m.getResources() != null ? m.getResources().getCpuUsage() : 0)
                .average().orElse(0));
            result.setAvgMemoryUsage(monitors.stream()
                .mapToDouble(m -> m.getResources() != null ? m.getResources().getMemoryUsagePercent() : 0)
                .average().orElse(0));

            logRequestEnd("getMonitorList", result.getTotal() + " monitors", System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("getMonitorList", e);
            return ResultModel.error(500, "获取监控列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/realtime/{skillId}")
    public ResultModel<SkillMonitorDTO> getRealtimeMonitor(@PathVariable String skillId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getRealtimeMonitor", skillId);

        try {
            SkillMonitorDTO monitor = monitorCache.get(skillId);
            if (monitor == null) {
                net.ooder.skillcenter.model.Skill skill = skillManager.getSkill(skillId);
                if (skill != null) {
                    monitor = createMockMonitor(skillId, skill.getName());
                    monitorCache.put(skillId, monitor);
                }
            }
            
            if (monitor == null) {
                logRequestEnd("getRealtimeMonitor", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("技能不存在");
            }

            updateMetrics(monitor);

            logRequestEnd("getRealtimeMonitor", monitor.getName(), System.currentTimeMillis() - startTime);
            return ResultModel.success(monitor);
        } catch (Exception e) {
            logRequestError("getRealtimeMonitor", e);
            return ResultModel.error(500, "获取实时监控失败: " + e.getMessage());
        }
    }

    private void updateMetrics(SkillMonitorDTO monitor) {
        if (monitor.getResources() != null) {
            double newCpu = Math.max(5, Math.min(95, monitor.getResources().getCpuUsage() + (Math.random() - 0.5) * 10));
            monitor.getResources().setCpuUsage(newCpu);
            
            long newMemory = (long)(monitor.getResources().getMemoryUsed() * (0.9 + Math.random() * 0.2));
            monitor.getResources().setMemoryUsed(newMemory);
            monitor.getResources().setMemoryUsagePercent(newMemory * 100.0 / monitor.getResources().getMemoryLimit());
        }
        
        monitor.setUptime(System.currentTimeMillis() - monitor.getStartTime());
    }

    @PostMapping("/metrics/{skillId}")
    public ResultModel<MetricsHistoryDTO> getMetricsHistory(
            @PathVariable String skillId,
            @RequestBody TimeRangeRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getMetricsHistory", skillId);

        try {
            MetricsHistoryDTO history = new MetricsHistoryDTO();
            history.setSkillId(skillId);
            history.setStartTime(request.getStartTime() > 0 ? request.getStartTime() : System.currentTimeMillis() - 3600000);
            history.setEndTime(request.getEndTime() > 0 ? request.getEndTime() : System.currentTimeMillis());
            history.setResolution(request.getResolution() != null ? request.getResolution() : "1m");

            List<MetricsHistoryDTO.MetricPoint> cpuMetrics = new ArrayList<>();
            List<MetricsHistoryDTO.MetricPoint> memoryMetrics = new ArrayList<>();
            List<MetricsHistoryDTO.MetricPoint> throughputMetrics = new ArrayList<>();

            long step = 60000;
            for (long t = history.getStartTime(); t <= history.getEndTime(); t += step) {
                MetricsHistoryDTO.MetricPoint cpuPoint = new MetricsHistoryDTO.MetricPoint();
                cpuPoint.setTimestamp(t);
                cpuPoint.setValue(20 + Math.random() * 40);
                cpuMetrics.add(cpuPoint);

                MetricsHistoryDTO.MetricPoint memPoint = new MetricsHistoryDTO.MetricPoint();
                memPoint.setTimestamp(t);
                memPoint.setValue(30 + Math.random() * 30);
                memoryMetrics.add(memPoint);

                MetricsHistoryDTO.MetricPoint throughputPoint = new MetricsHistoryDTO.MetricPoint();
                throughputPoint.setTimestamp(t);
                throughputPoint.setValue(10 + Math.random() * 20);
                throughputMetrics.add(throughputPoint);
            }

            history.setCpuMetrics(cpuMetrics);
            history.setMemoryMetrics(memoryMetrics);
            history.setThroughputMetrics(throughputMetrics);

            MetricsHistoryDTO.MetricSummary summary = new MetricsHistoryDTO.MetricSummary();
            summary.setAvg(35.0);
            summary.setMin(15.0);
            summary.setMax(65.0);
            summary.setP50(32.0);
            summary.setP90(55.0);
            summary.setP99(62.0);
            summary.setStdDev(12.5);
            history.setSummary(summary);

            logRequestEnd("getMetricsHistory", cpuMetrics.size() + " points", System.currentTimeMillis() - startTime);
            return ResultModel.success(history);
        } catch (Exception e) {
            logRequestError("getMetricsHistory", e);
            return ResultModel.error(500, "获取历史指标失败: " + e.getMessage());
        }
    }

    @PostMapping("/alerts")
    public ResultModel<PageResult<AlertDTO>> getAlerts(@RequestBody AlertQueryRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getAlerts", request);

        try {
            List<AlertDTO> allAlerts = new ArrayList<>();
            for (List<AlertDTO> alerts : alertStore.values()) {
                allAlerts.addAll(alerts);
            }

            if (allAlerts.isEmpty()) {
                AlertDTO sampleAlert = new AlertDTO();
                sampleAlert.setAlertId("alert-001");
                sampleAlert.setSkillId("skill-weather");
                sampleAlert.setSeverity("warning");
                sampleAlert.setType("cpu_high");
                sampleAlert.setTitle("CPU使用率过高");
                sampleAlert.setMessage("技能 skill-weather 的CPU使用率超过80%");
                sampleAlert.setTimestamp(System.currentTimeMillis() - 300000);
                sampleAlert.setStatus("active");
                sampleAlert.setSuggestedActions(Arrays.asList("检查是否有异常请求", "考虑扩容实例"));
                allAlerts.add(sampleAlert);
            }

            int total = allAlerts.size();
            int pageNum = request.getPageNum() > 0 ? request.getPageNum() : 1;
            int pageSize = request.getPageSize() > 0 ? request.getPageSize() : 10;
            int fromIndex = (pageNum - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, total);

            List<AlertDTO> pagedAlerts = fromIndex < total ? 
                allAlerts.subList(fromIndex, toIndex) : new ArrayList<>();

            PageResult<AlertDTO> result = new PageResult<>(pagedAlerts, total, pageNum, pageSize);

            logRequestEnd("getAlerts", total + " alerts", System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("getAlerts", e);
            return ResultModel.error(500, "获取告警列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/logs/{skillId}")
    public ResultModel<PageResult<SkillMonitorDTO.LogEntry>> getLogs(
            @PathVariable String skillId,
            @RequestBody LogQueryRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getLogs", skillId);

        try {
            List<SkillMonitorDTO.LogEntry> logs = new ArrayList<>();
            String[] levels = {"INFO", "DEBUG", "WARN", "ERROR"};
            String[] messages = {
                "技能启动成功",
                "等待请求...",
                "收到执行请求",
                "执行完成",
                "连接外部服务",
                "数据处理中",
                "缓存更新"
            };

            for (int i = 0; i < 50; i++) {
                SkillMonitorDTO.LogEntry entry = new SkillMonitorDTO.LogEntry();
                entry.setTimestamp(System.currentTimeMillis() - i * 60000);
                entry.setLevel(levels[i % levels.length]);
                entry.setMessage(messages[i % messages.length] + " [" + i + "]");
                entry.setSource(skillId);
                entry.setContext(new HashMap<>());
                entry.getContext().put("thread", "skill-exec-" + (i % 5));
                logs.add(entry);
            }

            if (request.getLevel() != null && !request.getLevel().isEmpty()) {
                logs.removeIf(l -> !request.getLevel().equalsIgnoreCase(l.getLevel()));
            }

            int total = logs.size();
            int pageNum = request.getPageNum() > 0 ? request.getPageNum() : 1;
            int pageSize = request.getPageSize() > 0 ? request.getPageSize() : 20;
            int fromIndex = (pageNum - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, total);

            List<SkillMonitorDTO.LogEntry> pagedLogs = fromIndex < total ? 
                logs.subList(fromIndex, toIndex) : new ArrayList<>();

            PageResult<SkillMonitorDTO.LogEntry> result = new PageResult<>(pagedLogs, total, pageNum, pageSize);

            logRequestEnd("getLogs", total + " logs", System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("getLogs", e);
            return ResultModel.error(500, "获取日志失败: " + e.getMessage());
        }
    }

    @PostMapping("/stats")
    public ResultModel<MonitorStatsDTO> getMonitorStats() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getMonitorStats", null);

        try {
            MonitorStatsDTO stats = new MonitorStatsDTO();
            stats.setTotalSkills(monitorCache.size());
            stats.setRunningSkills((int) monitorCache.values().stream()
                .filter(m -> "running".equals(m.getStatus())).count());
            stats.setTotalExecutions(monitorCache.values().stream()
                .mapToLong(m -> m.getExecution() != null ? m.getExecution().getTotalExecutions() : 0)
                .sum());
            stats.setSuccessRate(95.5);
            stats.setAvgCpuUsage(monitorCache.values().stream()
                .mapToDouble(m -> m.getResources() != null ? m.getResources().getCpuUsage() : 0)
                .average().orElse(0));
            stats.setAvgMemoryUsage(monitorCache.values().stream()
                .mapToDouble(m -> m.getResources() != null ? m.getResources().getMemoryUsagePercent() : 0)
                .average().orElse(0));
            stats.setActiveAlerts(3);

            logRequestEnd("getMonitorStats", stats, System.currentTimeMillis() - startTime);
            return ResultModel.success(stats);
        } catch (Exception e) {
            logRequestError("getMonitorStats", e);
            return ResultModel.error(500, "获取监控统计失败: " + e.getMessage());
        }
    }
}

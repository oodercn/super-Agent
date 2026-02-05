package net.ooder.nexus.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import net.ooder.nexus.model.Result;
import net.ooder.nexus.model.HealthResult;
import net.ooder.nexus.model.SystemMetricsResult;
import net.ooder.nexus.model.ServiceStatusResult;
import net.ooder.nexus.model.ServiceCheckResult;
import net.ooder.nexus.model.AllServicesCheckResult;
import net.ooder.nexus.model.AlertClearResult;
import net.ooder.nexus.service.INexusService;
import net.ooder.nexus.service.NexusServiceFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/health")
public class HealthCheckController {

    private static final Logger logger = LoggerFactory.getLogger(HealthCheckController.class);

    @Autowired
    private NexusServiceFactory serviceFactory;

    // 健康检查历史记录
    private final List<HealthCheckRecord> healthCheckHistory = new ArrayList<>();
    
    // 告警信息
    private final List<Alert> alerts = new ArrayList<>();
    
    // 告警ID生成器
    private final AtomicInteger alertIdGenerator = new AtomicInteger(1);

    // 初始化模拟数据
    public HealthCheckController() {
        initMockData();
    }

    private INexusService getService() {
        return serviceFactory.getService();
    }

    /**
     * 初始化模拟数据
     */
    private void initMockData() {
        // 初始化健康检查历史
        for (int i = 0; i < 3; i++) {
            Date checkTime = new Date(System.currentTimeMillis() - (i * 30 * 60 * 1000)); // 每30分钟一条记录
            Map<String, Object> metrics = new ConcurrentHashMap<>();
            metrics.put("cpuUsage", 25 + i * 2);
            metrics.put("memoryUsage", 45 + i * 3);
            metrics.put("diskUsage", 32 + i * 1);
            metrics.put("systemLoad", 0.8 + i * 0.1);
            metrics.put("networkConnections", 12 + i);
            metrics.put("networkLatency", 15 + i * 2);
            metrics.put("bandwidthUsage", 20 + i * 3);
            metrics.put("packetLoss", 0.0 + i * 0.1);
            
            HealthCheckRecord record = new HealthCheckRecord(
                    checkTime,
                    "正常",
                    "正常",
                    "全部运行",
                    95 - i,
                    metrics
            );
            healthCheckHistory.add(record);
        }
    }

    /**
     * 获取健康检查概览
     * @return 健康检查概览
     */
    @GetMapping("/overview")
    public Result<HealthResult> getHealthOverview() {
        try {
            logger.info("获取健康检查概览请求");
            
            // 生成模拟数据
            String systemStatus = "正常";
            String networkStatus = "正常";
            String serviceStatus = "5/5";
            int healthScore = 95;
            
            HealthResult result = new HealthResult();
            result.setStatus("healthy");
            result.setSystemStatus(systemStatus);
            result.setNetworkStatus(networkStatus);
            result.setServiceStatus(serviceStatus);
            result.setActiveAlerts(0);
            result.setLastCheckTime(new Date().toString());
            result.setMessage("获取健康检查概览成功");
            
            logger.info("获取健康检查概览成功");
            return Result.success("获取健康检查概览成功", result);
        } catch (Exception e) {
            logger.error("获取健康检查概览失败", e);
            return Result.error("获取健康检查概览失败: " + e.getMessage());
        }
    }

    /**
     * 获取系统状态
     * @return 系统状态
     */
    @GetMapping("/system/status")
    public Result<HealthResult> getSystemStatus() {
        try {
            logger.info("获取系统状态请求");
            
            // 生成模拟数据
            double cpuUsage = 25 + Math.random() * 10;
            double memoryUsage = 45 + Math.random() * 15;
            double diskUsage = 32 + Math.random() * 10;
            double systemLoad = 0.8 + Math.random() * 0.5;
            String uptime = "24 小时";
            int systemTemp = 45 + (int)(Math.random() * 10);
            
            HealthResult result = new HealthResult();
            result.setStatus("healthy");
            result.setSystemStatus("正常");
            result.setNetworkStatus("正常");
            result.setServiceStatus("全部运行");
            result.setActiveAlerts(0);
            result.setLastCheckTime(new Date().toString());
            result.setMessage("获取系统状态成功");
            
            logger.info("获取系统状态成功");
            return Result.success("获取系统状态成功", result);
        } catch (Exception e) {
            logger.error("获取系统状态失败", e);
            return Result.error("获取系统状态失败: " + e.getMessage());
        }
    }

    /**
     * 获取网络状态
     * @return 网络状态
     */
    @GetMapping("/network/status")
    public Result<HealthResult> getNetworkStatus() {
        try {
            logger.info("获取网络状态请求");
            
            // 生成模拟数据
            int networkConnections = 12 + (int)(Math.random() * 8);
            int networkLatency = 15 + (int)(Math.random() * 20);
            double bandwidthUsage = 20 + Math.random() * 30;
            double packetLoss = Math.random() * 1.0;
            
            HealthResult result = new HealthResult();
            result.setStatus("healthy");
            result.setSystemStatus("正常");
            result.setNetworkStatus("正常");
            result.setServiceStatus("全部运行");
            result.setActiveAlerts(0);
            result.setLastCheckTime(new Date().toString());
            result.setMessage("获取网络状态成功");
            
            logger.info("获取网络状态成功");
            return Result.success("获取网络状态成功", result);
        } catch (Exception e) {
            logger.error("获取网络状态失败", e);
            return Result.error("获取网络状态失败: " + e.getMessage());
        }
    }

    /**
     * 获取服务状态
     * @return 服务状态
     */
    @GetMapping("/service/status")
    public Result<ServiceStatusResult> getServiceStatus() {
        try {
            logger.info("获取服务状态请求");
            
            // 生成模拟数据
            List<ServiceStatus> services = new ArrayList<>();
            services.add(new ServiceStatus("MCP Agent", "运行中", "v0.6.5", new Date()));
            services.add(new ServiceStatus("Route Agent", "运行中", "v0.6.5", new Date()));
            services.add(new ServiceStatus("End Agent", "运行中", "v0.6.5", new Date()));
            services.add(new ServiceStatus("Agent SDK", "运行中", "v0.6.5", new Date()));
            services.add(new ServiceStatus("Web Console", "运行中", "v0.6.5", new Date()));
            
            ServiceStatusResult result = new ServiceStatusResult();
            result.setServices(services);
            result.setTotal(services.size());
            result.setRunning(services.size());
            
            logger.info("获取服务状态成功");
            return Result.success("获取服务状态成功", result);
        } catch (Exception e) {
            logger.error("获取服务状态失败", e);
            return Result.error("获取服务状态失败: " + e.getMessage());
        }
    }

    /**
     * 检查服务状态
     * @param serviceType 服务类型
     * @return 检查结果
     */
    @GetMapping("/service/check/{serviceType}")
    public Result<ServiceCheckResult> checkServiceStatus(@PathVariable String serviceType) {
        try {
            logger.info("检查服务状态请求，服务类型: {}", serviceType);
            
            // 模拟服务检查
            boolean isHealthy = Math.random() > 0.1; // 90% 的成功率
            String status = isHealthy ? "healthy" : "unhealthy";
            
            ServiceCheckResult result = new ServiceCheckResult(serviceType, status, isHealthy ? "服务状态正常" : "服务状态异常");
            
            logger.info("检查服务状态成功，服务类型: {}, 状态: {}", serviceType, status);
            return Result.success(isHealthy ? "服务检查成功" : "服务检查失败", result);
        } catch (Exception e) {
            logger.error("检查服务状态失败，服务类型: {}", serviceType, e);
            return Result.error("检查服务状态失败: " + e.getMessage());
        }
    }

    /**
     * 检查所有服务
     * @return 检查结果
     */
    @PostMapping("/service/check-all")
    public Result<AllServicesCheckResult> checkAllServices() {
        try {
            logger.info("检查所有服务请求");
            
            // 模拟检查所有服务
            List<ServiceCheckResult> results = new ArrayList<>();
            results.add(new ServiceCheckResult("MCP Agent", "healthy", "服务状态正常"));
            results.add(new ServiceCheckResult("Route Agent", "healthy", "服务状态正常"));
            results.add(new ServiceCheckResult("End Agent", "healthy", "服务状态正常"));
            results.add(new ServiceCheckResult("Agent SDK", "healthy", "服务状态正常"));
            results.add(new ServiceCheckResult("Web Console", "healthy", "服务状态正常"));
            
            int healthyCount = (int) results.stream().filter(r -> "healthy".equals(r.getStatus())).count();
            int totalCount = results.size();
            
            AllServicesCheckResult result = new AllServicesCheckResult();
            result.setResults(results);
            result.setHealthyCount(healthyCount);
            result.setTotalCount(totalCount);
            result.setCheckTime(new Date());
            
            logger.info("检查所有服务成功，健康服务: {}/{}，检查时间: {}", healthyCount, totalCount, new Date());
            return Result.success("检查所有服务成功", result);
        } catch (Exception e) {
            logger.error("检查所有服务失败", e);
            return Result.error("检查所有服务失败: " + e.getMessage());
        }
    }

    /**
     * 获取健康检查历史
     * @param limit 限制数量
     * @return 健康检查历史
     */
    @GetMapping("/history")
    public Result<List<HealthCheckRecord>> getHealthCheckHistory(@RequestParam(defaultValue = "10") int limit) {
        try {
            logger.info("获取健康检查历史请求，限制数量: {}", limit);
            
            // 限制返回数量
            List<HealthCheckRecord> history = healthCheckHistory.stream()
                    .limit(limit)
                    .collect(java.util.stream.Collectors.toList());
            
            logger.info("获取健康检查历史成功，返回 {} 条记录", history.size());
            return Result.success("获取健康检查历史成功", history);
        } catch (Exception e) {
            logger.error("获取健康检查历史失败", e);
            return Result.error("获取健康检查历史失败: " + e.getMessage());
        }
    }

    /**
     * 获取告警信息
     * @return 告警信息
     */
    @GetMapping("/alerts")
    public Result<List<Alert>> getAlerts() {
        try {
            logger.info("获取告警信息请求");
            
            // 模拟告警信息
            if (alerts.isEmpty()) {
                // 生成一些模拟告警
                Alert alert1 = new Alert(
                        "alert-" + alertIdGenerator.getAndIncrement(),
                        "warning",
                        "CPU 使用率超过 80%",
                        new Date(System.currentTimeMillis() - 3600000) // 1小时前
                );
                Alert alert2 = new Alert(
                        "alert-" + alertIdGenerator.getAndIncrement(),
                        "error",
                        "网络连接异常",
                        new Date(System.currentTimeMillis() - 7200000) // 2小时前
                );
                alerts.add(alert1);
                alerts.add(alert2);
            }
            
            logger.info("获取告警信息成功，返回 {} 条告警", alerts.size());
            return Result.success("获取告警信息成功", alerts);
        } catch (Exception e) {
            logger.error("获取告警信息失败", e);
            return Result.error("获取告警信息失败: " + e.getMessage());
        }
    }

    /**
     * 清除所有告警
     * @return 清除结果
     */
    @PostMapping("/alerts/clear")
    public Result<AlertClearResult> clearAlerts() {
        try {
            logger.info("清除所有告警请求");
            
            int clearedCount = alerts.size();
            alerts.clear();
            
            AlertClearResult result = new AlertClearResult();
            result.setClearedCount(clearedCount);
            result.setRemainingCount(alerts.size());
            
            logger.info("清除所有告警成功，清除了 {} 条告警", clearedCount);
            return Result.success("清除所有告警成功", result);
        } catch (Exception e) {
            logger.error("清除所有告警失败", e);
            return Result.error("清除所有告警失败: " + e.getMessage());
        }
    }

    /**
     * 获取系统监控指标
     * @return 系统监控指标
     */
    @GetMapping("/metrics")
    public Result<SystemMetricsResult> getSystemMetrics() {
        try {
            logger.info("获取系统监控指标请求");
            
            // 生成模拟数据
            double cpuUsage = 25 + Math.random() * 10;
            double memoryUsage = 45 + Math.random() * 15;
            double diskUsage = 32 + Math.random() * 10;
            double systemLoad = 0.8 + Math.random() * 0.5;
            
            SystemMetricsResult metrics = new SystemMetricsResult();
            metrics.setCpuUsage(cpuUsage);
            metrics.setMemoryUsage(memoryUsage);
            metrics.setDiskUsage(diskUsage);
            metrics.setNetworkIn(0);
            metrics.setNetworkOut(0);
            metrics.setProcessCount(0);
            metrics.setTimestamp(new Date().toString());
            metrics.setMessage("success");
            
            logger.info("获取系统监控指标成功");
            return Result.success("获取系统监控指标成功", metrics);
        } catch (Exception e) {
            logger.error("获取系统监控指标失败", e);
            return Result.error("获取系统监控指标失败: " + e.getMessage());
        }
    }

    /**
     * 服务状态模型类
     */
    public static class ServiceStatus {
        private String name;
        private String status;
        private String version;
        private Date lastCheck;

        public ServiceStatus(String name, String status, String version, Date lastCheck) {
            this.name = name;
            this.status = status;
            this.version = version;
            this.lastCheck = lastCheck;
        }

        // Getter方法
        public String getName() {
            return name;
        }

        public String getStatus() {
            return status;
        }

        public String getVersion() {
            return version;
        }

        public Date getLastCheck() {
            return lastCheck;
        }
    }

    /**
     * 服务检查结果模型类
     */
    public static class ServiceCheckResult {
        private String serviceType;
        private String status;
        private String message;

        public ServiceCheckResult(String serviceType, String status, String message) {
            this.serviceType = serviceType;
            this.status = status;
            this.message = message;
        }

        // Getter方法
        public String getServiceType() {
            return serviceType;
        }

        public String getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }

    /**
     * 健康检查记录模型类
     */
    public static class HealthCheckRecord {
        private Date checkTime;
        private String systemStatus;
        private String networkStatus;
        private String serviceStatus;
        private int healthScore;
        private Map<String, Object> metrics;

        public HealthCheckRecord(Date checkTime, String systemStatus, String networkStatus, String serviceStatus, int healthScore, Map<String, Object> metrics) {
            this.checkTime = checkTime;
            this.systemStatus = systemStatus;
            this.networkStatus = networkStatus;
            this.serviceStatus = serviceStatus;
            this.healthScore = healthScore;
            this.metrics = metrics;
        }

        // Getter方法
        public Date getCheckTime() {
            return checkTime;
        }

        public String getSystemStatus() {
            return systemStatus;
        }

        public String getNetworkStatus() {
            return networkStatus;
        }

        public String getServiceStatus() {
            return serviceStatus;
        }

        public int getHealthScore() {
            return healthScore;
        }

        public Map<String, Object> getMetrics() {
            return metrics;
        }
    }

    /**
     * 告警模型类
     */
    public static class Alert {
        private String id;
        private String level;
        private String message;
        private Date timestamp;

        public Alert(String id, String level, String message, Date timestamp) {
            this.id = id;
            this.level = level;
            this.message = message;
            this.timestamp = timestamp;
        }

        // Getter方法
        public String getId() {
            return id;
        }

        public String getLevel() {
            return level;
        }

        public String getMessage() {
            return message;
        }

        public Date getTimestamp() {
            return timestamp;
        }
    }
}

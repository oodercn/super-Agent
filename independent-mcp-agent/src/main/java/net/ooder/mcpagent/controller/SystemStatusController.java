package net.ooder.mcpagent.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/system")
public class SystemStatusController {

    private static final Logger log = LoggerFactory.getLogger(SystemStatusController.class);

    // 系统基本信息
    private final Map<String, Object> systemInfo = new ConcurrentHashMap<>();

    // 服务状态信息
    private final Map<String, ServiceStatus> serviceStatuses = new ConcurrentHashMap<>();

    // 系统资源使用情况
    private final Map<String, ResourceUsage> resourceUsage = new ConcurrentHashMap<>();

    // 系统启动时间
    private final long startTime;

    // 初始化系统状态数据
    public SystemStatusController() {
        this.startTime = System.currentTimeMillis();
        initializeSystemInfo();
        initializeServiceStatuses();
        initializeResourceUsage();
    }

    private void initializeSystemInfo() {
        systemInfo.put("version", "0.6.5");
        systemInfo.put("name", "MCP Agent");
        systemInfo.put("description", "Ooder Master Control Plane Agent");
        systemInfo.put("startTime", startTime);
        systemInfo.put("uptime", 0); // 运行时间会动态计算
        systemInfo.put("environment", "production");
        systemInfo.put("javaVersion", System.getProperty("java.version"));
        systemInfo.put("osName", System.getProperty("os.name"));
        systemInfo.put("osVersion", System.getProperty("os.version"));
        systemInfo.put("hostname", "mcp-agent-01");
        systemInfo.put("ipAddress", "192.168.1.1");
    }

    private void initializeServiceStatuses() {
        serviceStatuses.put("api", new ServiceStatus(
                "api",
                "API服务",
                "running",
                "正常运行中",
                startTime,
                System.currentTimeMillis(),
                0
        ));

        serviceStatuses.put("network", new ServiceStatus(
                "network",
                "网络服务",
                "running",
                "网络连接正常",
                startTime + 5000,
                System.currentTimeMillis() - 60000,
                0
        ));

        serviceStatuses.put("security", new ServiceStatus(
                "security",
                "安全服务",
                "running",
                "安全防护已启用",
                startTime + 10000,
                System.currentTimeMillis() - 30000,
                0
        ));

        serviceStatuses.put("command", new ServiceStatus(
                "command",
                "命令服务",
                "running",
                "命令执行正常",
                startTime + 15000,
                System.currentTimeMillis() - 120000,
                0
        ));

        serviceStatuses.put("monitoring", new ServiceStatus(
                "monitoring",
                "监控服务",
                "running",
                "监控功能正常",
                startTime + 20000,
                System.currentTimeMillis() - 90000,
                0
        ));
    }

    private void initializeResourceUsage() {
        resourceUsage.put("cpu", new ResourceUsage(
                "cpu",
                "CPU使用率",
                "percentage",
                25.5,
                0,
                100,
                20.0,
                30.0,
                System.currentTimeMillis()
        ));

        resourceUsage.put("memory", new ResourceUsage(
                "memory",
                "内存使用率",
                "percentage",
                62.3,
                0,
                100,
                55.0,
                65.0,
                System.currentTimeMillis()
        ));

        resourceUsage.put("disk", new ResourceUsage(
                "disk",
                "磁盘使用率",
                "percentage",
                45.8,
                0,
                100,
                40.0,
                50.0,
                System.currentTimeMillis()
        ));

        resourceUsage.put("network", new ResourceUsage(
                "network",
                "网络带宽使用率",
                "percentage",
                38.2,
                0,
                100,
                30.0,
                45.0,
                System.currentTimeMillis()
        ));
    }

    /**
     * 获取系统基本信息
     */
    @GetMapping("/info")
    public Map<String, Object> getSystemInfo() {
        log.info("Get system info requested");
        Map<String, Object> response = new HashMap<>();

        try {
            // 更新运行时间
            systemInfo.put("uptime", System.currentTimeMillis() - startTime);

            response.put("status", "success");
            response.put("message", "System info retrieved successfully");
            response.put("data", systemInfo);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting system info: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to get system info: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 获取系统健康检查
     */
    @GetMapping("/health")
    public Map<String, Object> getSystemHealth() {
        log.info("Get system health requested");
        Map<String, Object> response = new HashMap<>();

        try {
            Map<String, Object> healthData = new HashMap<>();
            healthData.put("status", calculateOverallHealthStatus());
            healthData.put("timestamp", System.currentTimeMillis());
            healthData.put("uptime", System.currentTimeMillis() - startTime);
            healthData.put("serviceStatuses", serviceStatuses);
            healthData.put("resourceUsage", resourceUsage);
            healthData.put("details", calculateHealthDetails());

            response.put("status", "success");
            response.put("message", "System health retrieved successfully");
            response.put("data", healthData);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting system health: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to get system health: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 获取服务状态列表
     */
    @GetMapping("/services")
    public Map<String, Object> getServiceStatuses() {
        log.info("Get service statuses requested");
        Map<String, Object> response = new HashMap<>();

        try {
            // 更新服务状态时间戳
            serviceStatuses.forEach((key, status) -> {
                status.setLastUpdated(System.currentTimeMillis());
            });

            response.put("status", "success");
            response.put("message", "Service statuses retrieved successfully");
            response.put("data", serviceStatuses);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting service statuses: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to get service statuses: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 获取服务状态详情
     */
    @GetMapping("/services/{serviceId}")
    public Map<String, Object> getServiceStatus(@PathVariable String serviceId) {
        log.info("Get service status requested: serviceId={}", serviceId);
        Map<String, Object> response = new HashMap<>();

        try {
            ServiceStatus status = serviceStatuses.get(serviceId);
            if (status == null) {
                response.put("status", "error");
                response.put("message", "Service not found: " + serviceId);
                response.put("code", 404);
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }

            // 更新服务状态时间戳
            status.setLastUpdated(System.currentTimeMillis());

            response.put("status", "success");
            response.put("message", "Service status retrieved successfully");
            response.put("data", status);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting service status: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to get service status: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 获取系统资源使用情况
     */
    @GetMapping("/resources")
    public Map<String, Object> getResourceUsage() {
        log.info("Get resource usage requested");
        Map<String, Object> response = new HashMap<>();

        try {
            // 模拟更新资源使用情况
            updateResourceUsage();

            response.put("status", "success");
            response.put("message", "Resource usage retrieved successfully");
            response.put("data", resourceUsage);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting resource usage: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to get resource usage: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 获取系统负载信息
     */
    @GetMapping("/load")
    public Map<String, Object> getSystemLoad() {
        log.info("Get system load requested");
        Map<String, Object> response = new HashMap<>();

        try {
            Map<String, Object> loadData = new HashMap<>();
            loadData.put("cpuLoad", calculateCpuLoad());
            loadData.put("memoryUsage", resourceUsage.get("memory").getValue());
            loadData.put("diskUsage", resourceUsage.get("disk").getValue());
            loadData.put("networkLoad", resourceUsage.get("network").getValue());
            loadData.put("processCount", 125); // 模拟进程数量
            loadData.put("threadCount", 512); // 模拟线程数量
            loadData.put("timestamp", System.currentTimeMillis());

            response.put("status", "success");
            response.put("message", "System load retrieved successfully");
            response.put("data", loadData);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting system load: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to get system load: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 重启服务
     */
    @PostMapping("/services/{serviceId}/restart")
    public Map<String, Object> restartService(@PathVariable String serviceId) {
        log.info("Restart service requested: serviceId={}", serviceId);
        Map<String, Object> response = new HashMap<>();

        try {
            ServiceStatus status = serviceStatuses.get(serviceId);
            if (status == null) {
                response.put("status", "error");
                response.put("message", "Service not found: " + serviceId);
                response.put("code", 404);
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }

            // 模拟服务重启
            status.setStatus("restarting");
            status.setMessage("服务正在重启...");
            status.setRestartCount(status.getRestartCount() + 1);

            // 模拟重启完成
            new Thread(() -> {
                try {
                    Thread.sleep(3000); // 模拟重启时间
                    status.setStatus("running");
                    status.setMessage("服务重启成功");
                    status.setLastUpdated(System.currentTimeMillis());
                } catch (InterruptedException e) {
                    log.error("Service restart simulation interrupted: {}", e.getMessage());
                }
            }).start();

            response.put("status", "success");
            response.put("message", "Service restart initiated successfully");
            response.put("data", status);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error restarting service: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to restart service: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    // 辅助方法：计算整体健康状态
    private String calculateOverallHealthStatus() {
        int runningServices = 0;
        for (ServiceStatus status : serviceStatuses.values()) {
            if ("running".equals(status.getStatus())) {
                runningServices++;
            }
        }

        double serviceHealth = (double) runningServices / serviceStatuses.size();
        double resourceHealth = calculateResourceHealth();
        double overallHealth = (serviceHealth * 0.6) + (resourceHealth * 0.4);

        if (overallHealth >= 0.9) {
            return "healthy";
        } else if (overallHealth >= 0.7) {
            return "degraded";
        } else if (overallHealth >= 0.5) {
            return "unhealthy";
        } else {
            return "critical";
        }
    }

    // 辅助方法：计算资源健康度
    private double calculateResourceHealth() {
        double cpuHealth = 1.0 - (resourceUsage.get("cpu").getValue() / 100);
        double memoryHealth = 1.0 - (resourceUsage.get("memory").getValue() / 100);
        double diskHealth = 1.0 - (resourceUsage.get("disk").getValue() / 100);
        double networkHealth = 1.0 - (resourceUsage.get("network").getValue() / 100);

        return (cpuHealth + memoryHealth + diskHealth + networkHealth) / 4;
    }

    // 辅助方法：计算健康详情
    private Map<String, Object> calculateHealthDetails() {
        Map<String, Object> details = new HashMap<>();
        details.put("serviceHealth", calculateServiceHealthDetails());
        details.put("resourceHealth", calculateResourceHealthDetails());
        details.put("uptimeStatus", calculateUptimeStatus());
        return details;
    }

    // 辅助方法：计算服务健康详情
    private Map<String, Object> calculateServiceHealthDetails() {
        Map<String, Object> serviceDetails = new HashMap<>();
        serviceDetails.put("totalServices", serviceStatuses.size());
        serviceDetails.put("runningServices", serviceStatuses.values().stream().filter(s -> "running".equals(s.getStatus())).count());
        serviceDetails.put("restartingServices", serviceStatuses.values().stream().filter(s -> "restarting".equals(s.getStatus())).count());
        serviceDetails.put("stoppedServices", serviceStatuses.values().stream().filter(s -> "stopped".equals(s.getStatus())).count());
        serviceDetails.put("failedServices", serviceStatuses.values().stream().filter(s -> "failed".equals(s.getStatus())).count());
        return serviceDetails;
    }

    // 辅助方法：计算资源健康详情
    private Map<String, Object> calculateResourceHealthDetails() {
        Map<String, Object> resourceDetails = new HashMap<>();
        resourceDetails.put("cpuStatus", resourceUsage.get("cpu").getValue() > 80 ? "warning" : "normal");
        resourceDetails.put("memoryStatus", resourceUsage.get("memory").getValue() > 85 ? "warning" : "normal");
        resourceDetails.put("diskStatus", resourceUsage.get("disk").getValue() > 90 ? "warning" : "normal");
        resourceDetails.put("networkStatus", resourceUsage.get("network").getValue() > 95 ? "warning" : "normal");
        return resourceDetails;
    }

    // 辅助方法：计算运行时间状态
    private String calculateUptimeStatus() {
        long uptime = System.currentTimeMillis() - startTime;
        if (uptime < 3600000) { // 小于1小时
            return "starting";
        } else if (uptime < 86400000) { // 小于24小时
            return "stable";
        } else {
            return "established";
        }
    }

    // 辅助方法：更新资源使用情况
    private void updateResourceUsage() {
        // 模拟CPU使用率波动
        double cpuUsage = 25.5 + (Math.random() * 10 - 5);
        cpuUsage = Math.max(5, Math.min(95, cpuUsage));
        resourceUsage.get("cpu").setValue(cpuUsage);
        resourceUsage.get("cpu").setLastUpdated(System.currentTimeMillis());

        // 模拟内存使用率波动
        double memoryUsage = 62.3 + (Math.random() * 5 - 2.5);
        memoryUsage = Math.max(40, Math.min(90, memoryUsage));
        resourceUsage.get("memory").setValue(memoryUsage);
        resourceUsage.get("memory").setLastUpdated(System.currentTimeMillis());

        // 模拟磁盘使用率（缓慢增长）
        double diskUsage = 45.8 + (Math.random() * 0.5);
        diskUsage = Math.max(40, Math.min(80, diskUsage));
        resourceUsage.get("disk").setValue(diskUsage);
        resourceUsage.get("disk").setLastUpdated(System.currentTimeMillis());

        // 模拟网络带宽使用率波动
        double networkUsage = 38.2 + (Math.random() * 15 - 7.5);
        networkUsage = Math.max(10, Math.min(90, networkUsage));
        resourceUsage.get("network").setValue(networkUsage);
        resourceUsage.get("network").setLastUpdated(System.currentTimeMillis());
    }

    // 辅助方法：计算CPU负载
    private double calculateCpuLoad() {
        return resourceUsage.get("cpu").getValue();
    }

    // 服务状态类
    private static class ServiceStatus {
        private final String id;
        private final String name;
        private String status;
        private String message;
        private final long startTime;
        private long lastUpdated;
        private int restartCount;

        public ServiceStatus(String id, String name, String status, String message, long startTime, long lastUpdated, int restartCount) {
            this.id = id;
            this.name = name;
            this.status = status;
            this.message = message;
            this.startTime = startTime;
            this.lastUpdated = lastUpdated;
            this.restartCount = restartCount;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public long getStartTime() {
            return startTime;
        }

        public long getLastUpdated() {
            return lastUpdated;
        }

        public void setLastUpdated(long lastUpdated) {
            this.lastUpdated = lastUpdated;
        }

        public int getRestartCount() {
            return restartCount;
        }

        public void setRestartCount(int restartCount) {
            this.restartCount = restartCount;
        }
    }

    // 资源使用情况类
    private static class ResourceUsage {
        private final String id;
        private final String name;
        private final String unit;
        private double value;
        private final double minValue;
        private final double maxValue;
        private final double warningThreshold;
        private final double criticalThreshold;
        private long lastUpdated;

        public ResourceUsage(String id, String name, String unit, double value, double minValue, double maxValue, double warningThreshold, double criticalThreshold, long lastUpdated) {
            this.id = id;
            this.name = name;
            this.unit = unit;
            this.value = value;
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.warningThreshold = warningThreshold;
            this.criticalThreshold = criticalThreshold;
            this.lastUpdated = lastUpdated;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getUnit() {
            return unit;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public double getMinValue() {
            return minValue;
        }

        public double getMaxValue() {
            return maxValue;
        }

        public double getWarningThreshold() {
            return warningThreshold;
        }

        public double getCriticalThreshold() {
            return criticalThreshold;
        }

        public long getLastUpdated() {
            return lastUpdated;
        }

        public void setLastUpdated(long lastUpdated) {
            this.lastUpdated = lastUpdated;
        }
    }
}

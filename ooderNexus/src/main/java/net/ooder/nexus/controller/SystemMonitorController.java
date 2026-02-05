package net.ooder.nexus.controller;

import net.ooder.nexus.model.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/system/monitor")
public class SystemMonitorController {

    private final Map<String, Map<String, Object>> metricsHistory;
    private final Map<String, Map<String, Object>> alerts;
    private final AtomicInteger alertIdCounter;

    public SystemMonitorController() {
        this.metricsHistory = new ConcurrentHashMap<>();
        this.alerts = new ConcurrentHashMap<>();
        this.alertIdCounter = new AtomicInteger(1);
        initializeSampleData();
    }

    private void initializeSampleData() {
        // 初始化一些示例告警
        Map<String, Object> alert1 = new HashMap<>();
        alert1.put("id", "1");
        alert1.put("type", "memory");
        alert1.put("level", "warning");
        alert1.put("message", "内存使用率超过80%");
        alert1.put("timestamp", System.currentTimeMillis() - 3600000);
        alert1.put("status", "active");
        alerts.put("1", alert1);

        Map<String, Object> alert2 = new HashMap<>();
        alert2.put("id", "2");
        alert2.put("type", "disk");
        alert2.put("level", "critical");
        alert2.put("message", "磁盘空间不足");
        alert2.put("timestamp", System.currentTimeMillis() - 7200000);
        alert2.put("status", "active");
        alerts.put("2", alert2);

        // 初始化一些示例指标历史数据
        for (int i = 0; i < 24; i++) {
            long timestamp = System.currentTimeMillis() - (i * 3600000);
            Map<String, Object> metrics = new HashMap<>();
            metrics.put("memoryUsage", 60 + (i % 20));
            metrics.put("cpuUsage", 40 + (i % 30));
            metrics.put("diskUsage", 50 + (i % 15));
            metrics.put("networkIn", 1000 + (i * 50));
            metrics.put("networkOut", 800 + (i * 40));
            metricsHistory.put(String.valueOf(timestamp), metrics);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSystemHealth() {
        Map<String, Object> healthStatus = new HashMap<>();

        try {
            // 获取JVM内存信息
            MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
            MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
            MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();

            // 获取操作系统信息
            OperatingSystemMXBean osMXBean = ManagementFactory.getOperatingSystemMXBean();

            // 系统整体状态
            healthStatus.put("status", "UP");
            healthStatus.put("timestamp", System.currentTimeMillis());
            healthStatus.put("uptime", ManagementFactory.getRuntimeMXBean().getUptime());

            // 组件状态
            Map<String, Object> components = new HashMap<>();

            // 内存状态
            Map<String, Object> memory = new HashMap<>();
            memory.put("status", "UP");
            memory.put("heapUsed", heapMemoryUsage.getUsed() / (1024 * 1024));
            memory.put("heapMax", heapMemoryUsage.getMax() / (1024 * 1024));
            memory.put("heapUsage", Math.round((double) heapMemoryUsage.getUsed() / heapMemoryUsage.getMax() * 100));
            memory.put("nonHeapUsed", nonHeapMemoryUsage.getUsed() / (1024 * 1024));
            memory.put("nonHeapMax", nonHeapMemoryUsage.getMax() / (1024 * 1024));
            components.put("memory", memory);

            // CPU状态
            Map<String, Object> cpu = new HashMap<>();
            cpu.put("status", "UP");
            cpu.put("availableProcessors", osMXBean.getAvailableProcessors());
            cpu.put("systemLoadAverage", osMXBean.getSystemLoadAverage());
            components.put("cpu", cpu);

            // 磁盘状态
            Map<String, Object> disk = new HashMap<>();
            disk.put("status", "UP");
            disk.put("totalSpace", 500000);
            disk.put("usedSpace", 250000);
            disk.put("freeSpace", 250000);
            disk.put("usage", 50);
            components.put("disk", disk);

            // 网络状态
            Map<String, Object> network = new HashMap<>();
            network.put("status", "UP");
            network.put("inboundTraffic", 10240);
            network.put("outboundTraffic", 5120);
            components.put("network", network);

            // 服务状态
            Map<String, Object> services = new HashMap<>();
            services.put("skillManager", "UP");
            services.put("p2pService", "UP");
            services.put("storageService", "UP");
            components.put("services", services);

            healthStatus.put("components", components);

            // 系统信息
            Map<String, Object> systemInfo = new HashMap<>();
            systemInfo.put("osName", osMXBean.getName());
            systemInfo.put("osVersion", osMXBean.getVersion());
            systemInfo.put("osArch", osMXBean.getArch());
            systemInfo.put("javaVersion", System.getProperty("java.version"));
            systemInfo.put("javaVendor", System.getProperty("java.vendor"));
            healthStatus.put("systemInfo", systemInfo);

            return ResponseEntity.ok(ApiResponse.success(healthStatus));
        } catch (Exception e) {
            healthStatus.put("status", "DOWN");
            healthStatus.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "获取系统健康状态失败: " + e.getMessage()));
        }
    }

    @GetMapping("/metrics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSystemMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        try {
            // 获取JVM内存信息
            MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
            MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
            MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();

            // 获取操作系统信息
            OperatingSystemMXBean osMXBean = ManagementFactory.getOperatingSystemMXBean();

            // 内存指标
            Map<String, Object> memoryMetrics = new HashMap<>();
            memoryMetrics.put("heapUsed", heapMemoryUsage.getUsed() / (1024 * 1024));
            memoryMetrics.put("heapMax", heapMemoryUsage.getMax() / (1024 * 1024));
            memoryMetrics.put("heapUsage", Math.round((double) heapMemoryUsage.getUsed() / heapMemoryUsage.getMax() * 100));
            memoryMetrics.put("nonHeapUsed", nonHeapMemoryUsage.getUsed() / (1024 * 1024));
            memoryMetrics.put("nonHeapMax", nonHeapMemoryUsage.getMax() / (1024 * 1024));
            metrics.put("memory", memoryMetrics);

            // CPU指标
            Map<String, Object> cpuMetrics = new HashMap<>();
            cpuMetrics.put("availableProcessors", osMXBean.getAvailableProcessors());
            cpuMetrics.put("systemLoadAverage", osMXBean.getSystemLoadAverage());
            cpuMetrics.put("cpuUsage", 45.5); // 模拟CPU使用率
            metrics.put("cpu", cpuMetrics);

            // 磁盘指标
            Map<String, Object> diskMetrics = new HashMap<>();
            diskMetrics.put("totalSpace", 500000);
            diskMetrics.put("usedSpace", 250000);
            diskMetrics.put("freeSpace", 250000);
            diskMetrics.put("usage", 50);
            metrics.put("disk", diskMetrics);

            // 网络指标
            Map<String, Object> networkMetrics = new HashMap<>();
            networkMetrics.put("inboundTraffic", 10240);
            networkMetrics.put("outboundTraffic", 5120);
            networkMetrics.put("packetsIn", 15000);
            networkMetrics.put("packetsOut", 12000);
            metrics.put("network", networkMetrics);

            // 应用指标
            Map<String, Object> appMetrics = new HashMap<>();
            appMetrics.put("totalSkills", 25);
            appMetrics.put("totalExecutions", 156);
            appMetrics.put("activeConnections", 8);
            appMetrics.put("requestCount", 1200);
            appMetrics.put("errorCount", 5);
            metrics.put("application", appMetrics);

            // 时间戳
            metrics.put("timestamp", System.currentTimeMillis());

            // 保存到历史记录
            metricsHistory.put(String.valueOf(System.currentTimeMillis()), metrics);

            return ResponseEntity.ok(ApiResponse.success(metrics));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "获取系统指标失败: " + e.getMessage()));
        }
    }

    @GetMapping("/metrics/history")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMetricsHistory() {
        List<Map<String, Object>> history = new ArrayList<>();

        try {
            // 转换metricsHistory为列表格式
            for (Map.Entry<String, Map<String, Object>> entry : metricsHistory.entrySet()) {
                Map<String, Object> metricData = new HashMap<>(entry.getValue());
                metricData.put("timestamp", Long.parseLong(entry.getKey()));
                history.add(metricData);
            }

            // 按时间戳排序
            history.sort((a, b) -> Long.compare((Long) a.get("timestamp"), (Long) b.get("timestamp")));

            return ResponseEntity.ok(ApiResponse.success(history));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "获取历史指标数据失败: " + e.getMessage()));
        }
    }

    @GetMapping("/alerts")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getAlerts() {
        List<Map<String, Object>> alertList = new ArrayList<>(alerts.values());

        try {
            // 按时间戳排序，最新的告警在前面
            alertList.sort((a, b) -> Long.compare((Long) b.get("timestamp"), (Long) a.get("timestamp")));

            return ResponseEntity.ok(ApiResponse.success(alertList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "获取告警列表失败: " + e.getMessage()));
        }
    }

    @PostMapping("/alerts/clear")
    public ResponseEntity<ApiResponse<Boolean>> clearAlerts() {
        try {
            // 清除所有告警
            alerts.clear();
            return ResponseEntity.ok(ApiResponse.success(true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "清除告警失败: " + e.getMessage()));
        }
    }

    @PostMapping("/alerts/clear/{alertId}")
    public ResponseEntity<ApiResponse<Boolean>> clearAlert(@PathVariable String alertId) {
        try {
            // 清除指定告警
            boolean removed = alerts.remove(alertId) != null;
            if (removed) {
                return ResponseEntity.ok(ApiResponse.success(true));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "告警不存在"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "清除告警失败: " + e.getMessage()));
        }
    }

    @PostMapping("/alerts/test")
    public ResponseEntity<ApiResponse<Map<String, Object>>> createTestAlert() {
        try {
            // 创建测试告警
            String alertId = String.valueOf(alertIdCounter.getAndIncrement());
            Map<String, Object> alert = new HashMap<>();
            alert.put("id", alertId);
            alert.put("type", "test");
            alert.put("level", "info");
            alert.put("message", "测试告警 - 系统运行正常");
            alert.put("timestamp", System.currentTimeMillis());
            alert.put("status", "active");
            alerts.put(alertId, alert);

            return ResponseEntity.ok(ApiResponse.success(alert));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "创建测试告警失败: " + e.getMessage()));
        }
    }
}

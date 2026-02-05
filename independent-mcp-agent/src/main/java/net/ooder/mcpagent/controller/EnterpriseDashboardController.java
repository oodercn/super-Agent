package net.ooder.mcpagent.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/enterprise/dashboard")
public class EnterpriseDashboardController {

    private static final Logger log = LoggerFactory.getLogger(EnterpriseDashboardController.class);

    // 企业业务指标数据
    private final Map<String, EnterpriseMetric> enterpriseMetrics = new ConcurrentHashMap<>();

    // 企业网络拓扑数据
    private final List<NetworkTopologyNode> networkTopology = new ArrayList<>();

    // 企业设备状态数据
    private final Map<String, DeviceStatusSummary> deviceStatusSummary = new ConcurrentHashMap<>();

    // 企业安全事件数据
    private final List<SecurityEvent> securityEvents = new ArrayList<>();

    // 初始化默认数据
    public EnterpriseDashboardController() {
        initializeDefaultData();
    }

    private void initializeDefaultData() {
        // 初始化企业业务指标
        enterpriseMetrics.put("network_health", new EnterpriseMetric(
                "network_health",
                "网络健康度",
                "percentage",
                92.5,
                85.0,
                95.0,
                "上升",
                7.5,
                System.currentTimeMillis()
        ));

        enterpriseMetrics.put("device_uptime", new EnterpriseMetric(
                "device_uptime",
                "设备在线率",
                "percentage",
                98.3,
                97.0,
                99.0,
                "稳定",
                1.3,
                System.currentTimeMillis()
        ));

        enterpriseMetrics.put("security_risk", new EnterpriseMetric(
                "security_risk",
                "安全风险指数",
                "score",
                15.2,
                20.0,
                10.0,
                "下降",
                -4.8,
                System.currentTimeMillis()
        ));

        enterpriseMetrics.put("bandwidth_usage", new EnterpriseMetric(
                "bandwidth_usage",
                "带宽使用率",
                "percentage",
                67.8,
                60.0,
                75.0,
                "上升",
                7.8,
                System.currentTimeMillis()
        ));

        enterpriseMetrics.put("command_execution", new EnterpriseMetric(
                "command_execution",
                "命令执行成功率",
                "percentage",
                94.7,
                90.0,
                98.0,
                "上升",
                4.7,
                System.currentTimeMillis()
        ));

        // 初始化网络拓扑数据
        networkTopology.add(new NetworkTopologyNode(
                "mcp-central",
                "中央控制节点",
                "online",
                "192.168.1.1",
                100,
                Arrays.asList("route-east", "route-west", "route-north", "route-south")
        ));

        networkTopology.add(new NetworkTopologyNode(
                "route-east",
                "东部路由节点",
                "online",
                "192.168.1.10",
                85,
                Arrays.asList("end-east-1", "end-east-2", "end-east-3")
        ));

        networkTopology.add(new NetworkTopologyNode(
                "route-west",
                "西部路由节点",
                "online",
                "192.168.1.20",
                90,
                Arrays.asList("end-west-1", "end-west-2")
        ));

        networkTopology.add(new NetworkTopologyNode(
                "route-north",
                "北部路由节点",
                "online",
                "192.168.1.30",
                95,
                Arrays.asList("end-north-1", "end-north-2", "end-north-3", "end-north-4")
        ));

        networkTopology.add(new NetworkTopologyNode(
                "route-south",
                "南部路由节点",
                "degraded",
                "192.168.1.40",
                70,
                Arrays.asList("end-south-1", "end-south-2")
        ));

        // 初始化设备状态摘要
        deviceStatusSummary.put("total", new DeviceStatusSummary(
                "total",
                "所有设备",
                125,
                123,
                2,
                0,
                98.4
        ));

        deviceStatusSummary.put("network", new DeviceStatusSummary(
                "network",
                "网络设备",
                35,
                34,
                1,
                0,
                97.1
        ));

        deviceStatusSummary.put("security", new DeviceStatusSummary(
                "security",
                "安全设备",
                20,
                20,
                0,
                0,
                100.0
        ));

        deviceStatusSummary.put("iot", new DeviceStatusSummary(
                "iot",
                "物联网设备",
                70,
                69,
                1,
                0,
                98.6
        ));

        // 初始化安全事件数据
        long now = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            securityEvents.add(new SecurityEvent(
                    "event-" + i,
                    i % 3 == 0 ? "warning" : i % 5 == 0 ? "critical" : "info",
                    i % 3 == 0 ? "异常登录尝试" : i % 5 == 0 ? "网络入侵检测" : "正常安全审计",
                    "192.168.1." + (100 + i),
                    "route-" + ("east,west,north,south").split(",")[i % 4],
                    now - (i * 3600000), // 过去10小时，每小时一条数据
                    i % 3 == 0 ? false : i % 5 == 0 ? false : true
            ));
        }
    }

    /**
     * 获取企业仪表盘概览
     */
    @GetMapping("/overview")
    public Map<String, Object> getEnterpriseDashboardOverview() {
        log.info("Get enterprise dashboard overview requested");
        Map<String, Object> response = new HashMap<>();

        try {
            Map<String, Object> overview = new HashMap<>();
            overview.put("metrics", enterpriseMetrics);
            overview.put("deviceSummary", deviceStatusSummary);
            overview.put("networkHealth", calculateNetworkHealth());
            overview.put("securityStatus", calculateSecurityStatus());
            overview.put("topAlerts", getTopAlerts());

            response.put("status", "success");
            response.put("message", "Enterprise dashboard overview retrieved successfully");
            response.put("data", overview);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting enterprise dashboard overview: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to get enterprise dashboard overview: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 获取网络拓扑数据
     */
    @GetMapping("/network-topology")
    public Map<String, Object> getNetworkTopology() {
        log.info("Get network topology requested");
        Map<String, Object> response = new HashMap<>();

        try {
            response.put("status", "success");
            response.put("message", "Network topology retrieved successfully");
            response.put("data", networkTopology);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting network topology: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to get network topology: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 获取设备状态详情
     */
    @GetMapping("/device-status")
    public Map<String, Object> getDeviceStatus(@RequestParam(required = false) String deviceType) {
        log.info("Get device status requested: deviceType={}", deviceType);
        Map<String, Object> response = new HashMap<>();

        try {
            Map<String, DeviceStatusSummary> filteredStatus = new HashMap<>();
            if (deviceType != null && deviceStatusSummary.containsKey(deviceType)) {
                filteredStatus.put(deviceType, deviceStatusSummary.get(deviceType));
            } else {
                filteredStatus.putAll(deviceStatusSummary);
            }

            response.put("status", "success");
            response.put("message", "Device status retrieved successfully");
            response.put("data", filteredStatus);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting device status: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to get device status: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 获取安全事件列表
     */
    @GetMapping("/security-events")
    public Map<String, Object> getSecurityEvents(
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(required = false) String severity) {
        log.info("Get security events requested: limit={}, severity={}", limit, severity);
        Map<String, Object> response = new HashMap<>();

        try {
            List<SecurityEvent> filteredEvents = securityEvents.stream()
                    .filter(event -> severity == null || event.getSeverity().equals(severity))
                    .sorted(Comparator.comparingLong(SecurityEvent::getTimestamp).reversed())
                    .limit(limit)
                    .collect(java.util.stream.Collectors.toList());

            response.put("status", "success");
            response.put("message", "Security events retrieved successfully");
            response.put("data", filteredEvents);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting security events: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to get security events: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 获取业务指标趋势
     */
    @GetMapping("/metrics/trend")
    public Map<String, Object> getMetricsTrend(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(required = false) List<String> metricIds) {
        log.info("Get metrics trend requested: days={}, metricIds={}", days, metricIds);
        Map<String, Object> response = new HashMap<>();

        try {
            Map<String, List<Map<String, Object>>> trendData = new HashMap<>();

            // 模拟生成趋势数据
            long now = System.currentTimeMillis();
            List<String> targetMetrics = metricIds != null && !metricIds.isEmpty() ? metricIds : new ArrayList<>(enterpriseMetrics.keySet());

            for (String metricId : targetMetrics) {
                EnterpriseMetric baseMetric = enterpriseMetrics.get(metricId);
                if (baseMetric != null) {
                    List<Map<String, Object>> metricTrend = new ArrayList<>();
                    for (int i = 0; i < days; i++) {
                        long timestamp = now - (i * 24 * 60 * 60 * 1000);
                        double value = baseMetric.getValue() + (Math.random() * 10 - 5); // 添加一些随机波动
                        value = Math.max(0, Math.min(100, value)); // 确保值在合理范围内

                        Map<String, Object> dataPoint = new HashMap<>();
                        dataPoint.put("timestamp", timestamp);
                        dataPoint.put("value", value);
                        metricTrend.add(dataPoint);
                    }
                    trendData.put(metricId, metricTrend);
                }
            }

            response.put("status", "success");
            response.put("message", "Metrics trend retrieved successfully");
            response.put("data", trendData);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting metrics trend: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to get metrics trend: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 获取网络健康度
     */
    private Map<String, Object> calculateNetworkHealth() {
        Map<String, Object> networkHealth = new HashMap<>();
        double healthScore = enterpriseMetrics.get("network_health").getValue();
        int onlineNodes = (int) networkTopology.stream().filter(node -> "online".equals(node.getStatus())).count();
        int totalNodes = networkTopology.size();

        networkHealth.put("score", healthScore);
        networkHealth.put("status", healthScore >= 90 ? "excellent" : healthScore >= 75 ? "good" : healthScore >= 60 ? "fair" : "poor");
        networkHealth.put("onlineNodes", onlineNodes);
        networkHealth.put("totalNodes", totalNodes);
        networkHealth.put("nodeOnlineRate", (double) onlineNodes / totalNodes * 100);

        return networkHealth;
    }

    /**
     * 获取安全状态
     */
    private Map<String, Object> calculateSecurityStatus() {
        Map<String, Object> securityStatus = new HashMap<>();
        double riskScore = enterpriseMetrics.get("security_risk").getValue();
        int criticalEvents = (int) securityEvents.stream().filter(event -> "critical".equals(event.getSeverity()) && !event.isResolved()).count();
        int warningEvents = (int) securityEvents.stream().filter(event -> "warning".equals(event.getSeverity()) && !event.isResolved()).count();

        securityStatus.put("riskScore", riskScore);
        securityStatus.put("status", riskScore <= 10 ? "secure" : riskScore <= 25 ? "low_risk" : riskScore <= 50 ? "medium_risk" : "high_risk");
        securityStatus.put("criticalEvents", criticalEvents);
        securityStatus.put("warningEvents", warningEvents);
        securityStatus.put("totalEvents", securityEvents.size());

        return securityStatus;
    }

    /**
     * 获取顶级告警
     */
    private List<Map<String, Object>> getTopAlerts() {
        List<Map<String, Object>> topAlerts = new ArrayList<>();

        // 从安全事件中获取未解决的严重事件
        securityEvents.stream()
                .filter(event -> ("critical".equals(event.getSeverity()) || "warning".equals(event.getSeverity())) && !event.isResolved())
                .sorted(Comparator.comparingLong(SecurityEvent::getTimestamp).reversed())
                .limit(5)
                .forEach(event -> {
                    Map<String, Object> alert = new HashMap<>();
                    alert.put("id", event.getId());
                    alert.put("severity", event.getSeverity());
                    alert.put("message", event.getMessage());
                    alert.put("sourceIp", event.getSourceIp());
                    alert.put("deviceId", event.getDeviceId());
                    alert.put("timestamp", event.getTimestamp());
                    topAlerts.add(alert);
                });

        return topAlerts;
    }

    // 企业指标类
    private static class EnterpriseMetric {
        private final String id;
        private final String name;
        private final String unit;
        private final double value;
        private final double minValue;
        private final double maxValue;
        private final String trend;
        private final double change;
        private final long timestamp;

        public EnterpriseMetric(String id, String name, String unit, double value, double minValue, double maxValue, String trend, double change, long timestamp) {
            this.id = id;
            this.name = name;
            this.unit = unit;
            this.value = value;
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.trend = trend;
            this.change = change;
            this.timestamp = timestamp;
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

        public double getMinValue() {
            return minValue;
        }

        public double getMaxValue() {
            return maxValue;
        }

        public String getTrend() {
            return trend;
        }

        public double getChange() {
            return change;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    // 网络拓扑节点类
    private static class NetworkTopologyNode {
        private final String id;
        private final String name;
        private final String status;
        private final String ipAddress;
        private final int healthScore;
        private final List<String> connectedDevices;

        public NetworkTopologyNode(String id, String name, String status, String ipAddress, int healthScore, List<String> connectedDevices) {
            this.id = id;
            this.name = name;
            this.status = status;
            this.ipAddress = ipAddress;
            this.healthScore = healthScore;
            this.connectedDevices = connectedDevices;
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

        public String getIpAddress() {
            return ipAddress;
        }

        public int getHealthScore() {
            return healthScore;
        }

        public List<String> getConnectedDevices() {
            return connectedDevices;
        }
    }

    // 设备状态摘要类
    private static class DeviceStatusSummary {
        private final String id;
        private final String name;
        private final int totalDevices;
        private final int onlineDevices;
        private final int offlineDevices;
        private final int errorDevices;
        private final double onlineRate;

        public DeviceStatusSummary(String id, String name, int totalDevices, int onlineDevices, int offlineDevices, int errorDevices, double onlineRate) {
            this.id = id;
            this.name = name;
            this.totalDevices = totalDevices;
            this.onlineDevices = onlineDevices;
            this.offlineDevices = offlineDevices;
            this.errorDevices = errorDevices;
            this.onlineRate = onlineRate;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getTotalDevices() {
            return totalDevices;
        }

        public int getOnlineDevices() {
            return onlineDevices;
        }

        public int getOfflineDevices() {
            return offlineDevices;
        }

        public int getErrorDevices() {
            return errorDevices;
        }

        public double getOnlineRate() {
            return onlineRate;
        }
    }

    // 安全事件类
    private static class SecurityEvent {
        private final String id;
        private final String severity;
        private final String message;
        private final String sourceIp;
        private final String deviceId;
        private final long timestamp;
        private final boolean resolved;

        public SecurityEvent(String id, String severity, String message, String sourceIp, String deviceId, long timestamp, boolean resolved) {
            this.id = id;
            this.severity = severity;
            this.message = message;
            this.sourceIp = sourceIp;
            this.deviceId = deviceId;
            this.timestamp = timestamp;
            this.resolved = resolved;
        }

        public String getId() {
            return id;
        }

        public String getSeverity() {
            return severity;
        }

        public String getMessage() {
            return message;
        }

        public String getSourceIp() {
            return sourceIp;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public boolean isResolved() {
            return resolved;
        }
    }
}

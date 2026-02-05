package net.ooder.mcpagent.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/network/route")
public class RouteController {

    private static final Logger log = LoggerFactory.getLogger(RouteController.class);

    // 路由数据存储
    private final Map<String, NetworkRoute> routes = new ConcurrentHashMap<>();

    // 路由发现状态
    private volatile boolean discoveryInProgress = false;
    private volatile long lastDiscoveryTime = 0;

    // 初始化默认路由数据
    public RouteController() {
        initializeDefaultRoutes();
    }

    private void initializeDefaultRoutes() {
        routes.put("route-001", new NetworkRoute(
                "route-001",
                "direct",
                "mcp-agent-01",
                "route-agent-east",
                "active",
                10,
                99.5,
                "稳定",
                System.currentTimeMillis() - 3600000,
                System.currentTimeMillis()
        ));

        routes.put("route-002", new NetworkRoute(
                "route-002",
                "direct",
                "mcp-agent-01",
                "route-agent-west",
                "active",
                8,
                99.2,
                "稳定",
                System.currentTimeMillis() - 7200000,
                System.currentTimeMillis()
        ));

        routes.put("route-003", new NetworkRoute(
                "route-003",
                "direct",
                "mcp-agent-01",
                "route-agent-north",
                "active",
                12,
                98.8,
                "稳定",
                System.currentTimeMillis() - 10800000,
                System.currentTimeMillis()
        ));

        routes.put("route-004", new NetworkRoute(
                "route-004",
                "direct",
                "mcp-agent-01",
                "route-agent-south",
                "degraded",
                35,
                75.3,
                "性能下降",
                System.currentTimeMillis() - 14400000,
                System.currentTimeMillis() - 300000
        ));

        routes.put("route-005", new NetworkRoute(
                "route-005",
                "indirect",
                "mcp-agent-01",
                "end-agent-east-01",
                "active",
                25,
                96.7,
                "稳定",
                System.currentTimeMillis() - 18000000,
                System.currentTimeMillis()
        ));

        routes.put("route-006", new NetworkRoute(
                "route-006",
                "indirect",
                "mcp-agent-01",
                "end-agent-west-01",
                "active",
                20,
                98.1,
                "稳定",
                System.currentTimeMillis() - 21600000,
                System.currentTimeMillis()
        ));

        routes.put("route-007", new NetworkRoute(
                "route-007",
                "indirect",
                "mcp-agent-01",
                "end-agent-north-01",
                "active",
                28,
                97.5,
                "稳定",
                System.currentTimeMillis() - 25200000,
                System.currentTimeMillis()
        ));

        routes.put("route-008", new NetworkRoute(
                "route-008",
                "indirect",
                "mcp-agent-01",
                "end-agent-south-01",
                "pending",
                0,
                0.0,
                "路由初始化中",
                System.currentTimeMillis() - 3600000,
                System.currentTimeMillis() - 3600000
        ));
    }

    /**
     * 获取路由列表
     */
    @GetMapping("/list")
    public Map<String, Object> getRoutes(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String destination) {
        log.info("Get routes requested: status={}, type={}, source={}, destination={}", status, type, source, destination);
        Map<String, Object> response = new HashMap<>();

        try {
            List<NetworkRoute> filteredRoutes = new ArrayList<>();
            for (NetworkRoute route : routes.values()) {
                if ((status == null || route.getStatus().equals(status)) &&
                    (type == null || route.getType().equals(type)) &&
                    (source == null || route.getSource().equals(source)) &&
                    (destination == null || route.getDestination().equals(destination))) {
                    filteredRoutes.add(route);
                }
            }

            // 按最后更新时间排序
            filteredRoutes.sort(Comparator.comparingLong(NetworkRoute::getLastUpdated).reversed());

            Map<String, Object> data = new HashMap<>();
            data.put("routes", filteredRoutes);
            data.put("total", filteredRoutes.size());
            data.put("statusSummary", calculateStatusSummary());
            data.put("typeSummary", calculateTypeSummary());
            data.put("lastDiscoveryTime", lastDiscoveryTime);
            data.put("discoveryInProgress", discoveryInProgress);

            response.put("status", "success");
            response.put("message", "Routes retrieved successfully");
            response.put("data", data);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting routes: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to get routes: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 获取路由详情
     */
    @GetMapping("/detail/{routeId}")
    public Map<String, Object> getRouteDetail(@PathVariable String routeId) {
        log.info("Get route detail requested: routeId={}", routeId);
        Map<String, Object> response = new HashMap<>();

        try {
            NetworkRoute route = routes.get(routeId);
            if (route == null) {
                response.put("status", "error");
                response.put("message", "Route not found: " + routeId);
                response.put("code", 404);
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }

            // 更新最后检查时间
            route.setLastUpdated(System.currentTimeMillis());

            // 生成路由历史数据（模拟）
            List<Map<String, Object>> history = generateRouteHistory(routeId);

            Map<String, Object> data = new HashMap<>();
            data.put("route", route);
            data.put("history", history);
            data.put("healthScore", calculateRouteHealthScore(route));

            response.put("status", "success");
            response.put("message", "Route detail retrieved successfully");
            response.put("data", data);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting route detail: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to get route detail: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 发现路由
     */
    @PostMapping("/discover")
    public Map<String, Object> discoverRoutes() {
        log.info("Discover routes requested");
        Map<String, Object> response = new HashMap<>();

        try {
            if (discoveryInProgress) {
                response.put("status", "error");
                response.put("message", "Route discovery is already in progress");
                response.put("code", 409);
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }

            // 标记开始发现
            discoveryInProgress = true;

            // 异步执行路由发现
            discoverRoutesAsync();

            response.put("status", "success");
            response.put("message", "Route discovery initiated successfully");
            Map<String, Object> data = new ConcurrentHashMap<>();
            data.put("status", "discovering");
            data.put("message", "路由发现正在进行中，请稍后查询结果");
            response.put("data", data);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error initiating route discovery: {}", e.getMessage());
            discoveryInProgress = false;
            response.put("status", "error");
            response.put("message", "Failed to initiate route discovery: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 获取路由发现状态
     */
    @GetMapping("/discover/status")
    public Map<String, Object> getDiscoveryStatus() {
        log.info("Get discovery status requested");
        Map<String, Object> response = new HashMap<>();

        try {
            Map<String, Object> status = new HashMap<>();
            status.put("inProgress", discoveryInProgress);
            status.put("lastDiscoveryTime", lastDiscoveryTime);
            status.put("lastDiscoveryFormatted", lastDiscoveryTime > 0 ? new Date(lastDiscoveryTime).toString() : "Never");
            status.put("routeCount", routes.size());

            response.put("status", "success");
            response.put("message", "Discovery status retrieved successfully");
            response.put("data", status);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting discovery status: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to get discovery status: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 添加路由
     */
    @PostMapping("/add")
    public Map<String, Object> addRoute(@RequestBody Map<String, Object> routeData) {
        log.info("Add route requested: {}", routeData);
        Map<String, Object> response = new HashMap<>();

        try {
            // 验证必要字段
            if (!routeData.containsKey("source") || !routeData.containsKey("destination")) {
                response.put("status", "error");
                response.put("message", "Missing required fields: source and destination are required");
                response.put("code", 400);
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }

            // 生成路由ID
            String routeId = routeData.containsKey("routeId") ? (String) routeData.get("routeId") : "route-" + System.currentTimeMillis();

            // 检查路由是否已存在
            if (routes.containsKey(routeId)) {
                response.put("status", "error");
                response.put("message", "Route already exists: " + routeId);
                response.put("code", 409);
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }

            // 创建新路由
            NetworkRoute newRoute = new NetworkRoute(
                    routeId,
                    routeData.containsKey("type") ? (String) routeData.get("type") : "direct",
                    (String) routeData.get("source"),
                    (String) routeData.get("destination"),
                    "pending",
                    0,
                    0.0,
                    "路由初始化中",
                    System.currentTimeMillis(),
                    System.currentTimeMillis()
            );

            // 添加到存储
            routes.put(routeId, newRoute);

            // 模拟路由激活过程
            activateRouteAsync(routeId);

            response.put("status", "success");
            response.put("message", "Route added successfully");
            response.put("data", newRoute);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error adding route: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to add route: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 删除路由
     */
    @DeleteMapping("/delete/{routeId}")
    public Map<String, Object> deleteRoute(@PathVariable String routeId) {
        log.info("Delete route requested: routeId={}", routeId);
        Map<String, Object> response = new HashMap<>();

        try {
            NetworkRoute route = routes.get(routeId);
            if (route == null) {
                response.put("status", "error");
                response.put("message", "Route not found: " + routeId);
                response.put("code", 404);
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }

            // 从存储中删除
            routes.remove(routeId);

            response.put("status", "success");
            response.put("message", "Route deleted successfully");
            Map<String, Object> data = new ConcurrentHashMap<>();
            data.put("routeId", routeId);
            response.put("data", data);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error deleting route: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to delete route: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 更新路由状态
     */
    @PutMapping("/update/{routeId}")
    public Map<String, Object> updateRoute(@PathVariable String routeId, @RequestBody Map<String, Object> updateData) {
        log.info("Update route requested: routeId={}, data={}", routeId, updateData);
        Map<String, Object> response = new HashMap<>();

        try {
            NetworkRoute route = routes.get(routeId);
            if (route == null) {
                response.put("status", "error");
                response.put("message", "Route not found: " + routeId);
                response.put("code", 404);
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }

            // 更新字段
            if (updateData.containsKey("status")) {
                route.setStatus((String) updateData.get("status"));
            }
            if (updateData.containsKey("type")) {
                route.setType((String) updateData.get("type"));
            }
            if (updateData.containsKey("description")) {
                route.setDescription((String) updateData.get("description"));
            }

            // 更新时间戳
            route.setLastUpdated(System.currentTimeMillis());

            response.put("status", "success");
            response.put("message", "Route updated successfully");
            response.put("data", route);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error updating route: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to update route: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 获取路由统计
     */
    @GetMapping("/stats")
    public Map<String, Object> getRouteStats() {
        log.info("Get route stats requested");
        Map<String, Object> response = new HashMap<>();

        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalRoutes", routes.size());
            stats.put("statusSummary", calculateStatusSummary());
            stats.put("typeSummary", calculateTypeSummary());
            stats.put("averageLatency", calculateAverageLatency());
            stats.put("averageReliability", calculateAverageReliability());
            stats.put("healthScore", calculateOverallHealthScore());
            stats.put("lastDiscoveryTime", lastDiscoveryTime);

            response.put("status", "success");
            response.put("message", "Route stats retrieved successfully");
            response.put("data", stats);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting route stats: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to get route stats: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    // 辅助方法：计算状态摘要
    private Map<String, Integer> calculateStatusSummary() {
        Map<String, Integer> statusSummary = new HashMap<>();
        statusSummary.put("active", 0);
        statusSummary.put("degraded", 0);
        statusSummary.put("pending", 0);
        statusSummary.put("error", 0);

        for (NetworkRoute route : routes.values()) {
            String status = route.getStatus();
            statusSummary.put(status, statusSummary.getOrDefault(status, 0) + 1);
        }

        return statusSummary;
    }

    // 辅助方法：计算类型摘要
    private Map<String, Integer> calculateTypeSummary() {
        Map<String, Integer> typeSummary = new HashMap<>();
        typeSummary.put("direct", 0);
        typeSummary.put("indirect", 0);

        for (NetworkRoute route : routes.values()) {
            String type = route.getType();
            typeSummary.put(type, typeSummary.getOrDefault(type, 0) + 1);
        }

        return typeSummary;
    }

    // 辅助方法：计算平均延迟
    private double calculateAverageLatency() {
        if (routes.isEmpty()) {
            return 0;
        }
        return routes.values().stream().mapToInt(NetworkRoute::getLatency).average().orElse(0);
    }

    // 辅助方法：计算平均可靠性
    private double calculateAverageReliability() {
        if (routes.isEmpty()) {
            return 0;
        }
        return routes.values().stream().mapToDouble(NetworkRoute::getReliability).average().orElse(0);
    }

    // 辅助方法：计算路由健康分数
    private double calculateRouteHealthScore(NetworkRoute route) {
        double reliabilityScore = route.getReliability() * 0.6;
        double latencyScore = Math.max(0, 40 - (route.getLatency() * 2)) * 0.4;
        return reliabilityScore + latencyScore;
    }

    // 辅助方法：计算整体健康分数
    private double calculateOverallHealthScore() {
        if (routes.isEmpty()) {
            return 0;
        }
        return routes.values().stream().mapToDouble(this::calculateRouteHealthScore).average().orElse(0);
    }

    // 辅助方法：生成路由历史数据
    private List<Map<String, Object>> generateRouteHistory(String routeId) {
        List<Map<String, Object>> history = new ArrayList<>();
        long now = System.currentTimeMillis();

        for (int i = 0; i < 24; i++) {
            long timestamp = now - (i * 3600000); // 过去24小时，每小时一条数据
            Map<String, Object> dataPoint = new HashMap<>();
            dataPoint.put("timestamp", timestamp);
            dataPoint.put("reliability", 90 + Math.random() * 10); // 90-100之间的随机值
            dataPoint.put("latency", 5 + Math.random() * 15); // 5-20之间的随机值
            history.add(dataPoint);
        }

        return history;
    }

    // 异步执行路由发现
    private void discoverRoutesAsync() {
        new Thread(() -> {
            try {
                log.info("Route discovery started");
                
                // 模拟发现过程
                Thread.sleep(3000); // 模拟3秒的发现时间

                // 模拟发现新路由
                String newRouteId = "route-discovered-" + System.currentTimeMillis();
                NetworkRoute newRoute = new NetworkRoute(
                        newRouteId,
                        "indirect",
                        "mcp-agent-01",
                        "end-agent-new-01",
                        "active",
                        15,
                        97.8,
                        "新发现的路由",
                        System.currentTimeMillis(),
                        System.currentTimeMillis()
                );

                // 添加新路由
                routes.put(newRouteId, newRoute);

                // 更新现有路由状态
                for (NetworkRoute route : routes.values()) {
                    if ("pending".equals(route.getStatus())) {
                        route.setStatus("active");
                        route.setLatency(10 + (int)(Math.random() * 10));
                        route.setReliability(95 + Math.random() * 5);
                        route.setDescription("路由已激活");
                        route.setLastUpdated(System.currentTimeMillis());
                    }
                }

                // 标记完成
                lastDiscoveryTime = System.currentTimeMillis();
                log.info("Route discovery completed successfully");

            } catch (InterruptedException e) {
                log.error("Route discovery interrupted: {}", e.getMessage());
            } finally {
                discoveryInProgress = false;
            }
        }).start();
    }

    // 异步激活路由
    private void activateRouteAsync(String routeId) {
        new Thread(() -> {
            try {
                Thread.sleep(2000); // 模拟激活过程
                NetworkRoute route = routes.get(routeId);
                if (route != null) {
                    route.setStatus("active");
                    route.setLatency(10 + (int)(Math.random() * 10));
                    route.setReliability(95 + Math.random() * 5);
                    route.setDescription("路由激活成功");
                    route.setLastUpdated(System.currentTimeMillis());
                }
            } catch (InterruptedException e) {
                log.error("Route activation simulation interrupted: {}", e.getMessage());
            }
        }).start();
    }

    // 网络路由类
    private static class NetworkRoute {
        private final String routeId;
        private String type;
        private final String source;
        private final String destination;
        private String status;
        private int latency;
        private double reliability;
        private String description;
        private final long createdAt;
        private long lastUpdated;

        public NetworkRoute(String routeId, String type, String source, String destination, String status, int latency, double reliability, String description, long createdAt, long lastUpdated) {
            this.routeId = routeId;
            this.type = type;
            this.source = source;
            this.destination = destination;
            this.status = status;
            this.latency = latency;
            this.reliability = reliability;
            this.description = description;
            this.createdAt = createdAt;
            this.lastUpdated = lastUpdated;
        }

        public String getRouteId() {
            return routeId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSource() {
            return source;
        }

        public String getDestination() {
            return destination;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getLatency() {
            return latency;
        }

        public void setLatency(int latency) {
            this.latency = latency;
        }

        public double getReliability() {
            return reliability;
        }

        public void setReliability(double reliability) {
            this.reliability = reliability;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public long getCreatedAt() {
            return createdAt;
        }

        public long getLastUpdated() {
            return lastUpdated;
        }

        public void setLastUpdated(long lastUpdated) {
            this.lastUpdated = lastUpdated;
        }
    }
}

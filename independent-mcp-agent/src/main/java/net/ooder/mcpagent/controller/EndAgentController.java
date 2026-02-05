package net.ooder.mcpagent.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/network/endagent")
public class EndAgentController {

    private static final Logger log = LoggerFactory.getLogger(EndAgentController.class);

    // 终端数据存储
    private final Map<String, EndAgent> endAgents = new ConcurrentHashMap<>();

    // 终端发现状态
    private volatile boolean discoveryInProgress = false;
    private volatile long lastDiscoveryTime = 0;

    // 初始化默认终端数据
    public EndAgentController() {
        initializeDefaultEndAgents();
    }

    private void initializeDefaultEndAgents() {
        endAgents.put("end-agent-east-01", new EndAgent(
                "end-agent-east-01",
                "东部终端-01",
                "iot",
                "active",
                "192.168.1.101",
                "route-agent-east",
                "1.0.0",
                "在线",
                System.currentTimeMillis() - 3600000,
                System.currentTimeMillis()
        ));

        endAgents.put("end-agent-east-02", new EndAgent(
                "end-agent-east-02",
                "东部终端-02",
                "sensor",
                "active",
                "192.168.1.102",
                "route-agent-east",
                "1.0.0",
                "在线",
                System.currentTimeMillis() - 7200000,
                System.currentTimeMillis()
        ));

        endAgents.put("end-agent-west-01", new EndAgent(
                "end-agent-west-01",
                "西部终端-01",
                "iot",
                "active",
                "192.168.1.201",
                "route-agent-west",
                "1.0.0",
                "在线",
                System.currentTimeMillis() - 10800000,
                System.currentTimeMillis()
        ));

        endAgents.put("end-agent-west-02", new EndAgent(
                "end-agent-west-02",
                "西部终端-02",
                "camera",
                "degraded",
                "192.168.1.202",
                "route-agent-west",
                "1.0.0",
                "性能下降",
                System.currentTimeMillis() - 14400000,
                System.currentTimeMillis() - 300000
        ));

        endAgents.put("end-agent-north-01", new EndAgent(
                "end-agent-north-01",
                "北部终端-01",
                "sensor",
                "active",
                "192.168.1.301",
                "route-agent-north",
                "1.0.0",
                "在线",
                System.currentTimeMillis() - 18000000,
                System.currentTimeMillis()
        ));

        endAgents.put("end-agent-south-01", new EndAgent(
                "end-agent-south-01",
                "南部终端-01",
                "iot",
                "inactive",
                "192.168.1.401",
                "route-agent-south",
                "1.0.0",
                "离线",
                System.currentTimeMillis() - 21600000,
                System.currentTimeMillis() - 3600000
        ));
    }

    /**
     * 获取终端列表
     */
    @GetMapping("/list")
    public Map<String, Object> getEndAgents(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String routeAgentId) {
        log.info("Get end agents requested: status={}, type={}, routeAgentId={}", status, type, routeAgentId);
        Map<String, Object> response = new HashMap<>();

        try {
            List<EndAgent> filteredAgents = new ArrayList<>();
            for (EndAgent agent : endAgents.values()) {
                if ((status == null || agent.getStatus().equals(status)) &&
                    (type == null || agent.getType().equals(type)) &&
                    (routeAgentId == null || agent.getRouteAgentId().equals(routeAgentId))) {
                    filteredAgents.add(agent);
                }
            }

            // 按最后更新时间排序
            filteredAgents.sort(Comparator.comparingLong(EndAgent::getLastUpdated).reversed());

            Map<String, Object> data = new HashMap<>();
            data.put("agents", filteredAgents);
            data.put("total", filteredAgents.size());
            data.put("statusSummary", calculateStatusSummary());
            data.put("typeSummary", calculateTypeSummary());
            data.put("lastDiscoveryTime", lastDiscoveryTime);
            data.put("discoveryInProgress", discoveryInProgress);

            response.put("status", "success");
            response.put("message", "End agents retrieved successfully");
            response.put("data", data);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting end agents: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to get end agents: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 获取终端详情
     */
    @GetMapping("/detail/{agentId}")
    public Map<String, Object> getEndAgentDetail(@PathVariable String agentId) {
        log.info("Get end agent detail requested: agentId={}", agentId);
        Map<String, Object> response = new HashMap<>();

        try {
            EndAgent agent = endAgents.get(agentId);
            if (agent == null) {
                response.put("status", "error");
                response.put("message", "End agent not found: " + agentId);
                response.put("code", 404);
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }

            // 更新最后检查时间
            agent.setLastUpdated(System.currentTimeMillis());

            // 生成终端历史数据（模拟）
            List<Map<String, Object>> history = generateAgentHistory(agentId);

            Map<String, Object> data = new HashMap<>();
            data.put("agent", agent);
            data.put("history", history);

            response.put("status", "success");
            response.put("message", "End agent detail retrieved successfully");
            response.put("data", data);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting end agent detail: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to get end agent detail: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 添加终端
     */
    @PostMapping("/add")
    public Map<String, Object> addEndAgent(@RequestBody Map<String, Object> agentData) {
        log.info("Add end agent requested: {}", agentData);
        Map<String, Object> response = new HashMap<>();

        try {
            // 验证必要字段
            if (!agentData.containsKey("name") || !agentData.containsKey("type") || !agentData.containsKey("ipAddress")) {
                response.put("status", "error");
                response.put("message", "Missing required fields: name, type, and ipAddress are required");
                response.put("code", 400);
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }

            // 生成终端ID
            String agentId = agentData.containsKey("agentId") ? (String) agentData.get("agentId") : "end-agent-" + System.currentTimeMillis();

            // 检查终端是否已存在
            if (endAgents.containsKey(agentId)) {
                response.put("status", "error");
                response.put("message", "End agent already exists: " + agentId);
                response.put("code", 409);
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }

            // 创建新终端
            EndAgent newAgent = new EndAgent(
                    agentId,
                    (String) agentData.get("name"),
                    (String) agentData.get("type"),
                    "pending",
                    (String) agentData.get("ipAddress"),
                    agentData.containsKey("routeAgentId") ? (String) agentData.get("routeAgentId") : "",
                    agentData.containsKey("version") ? (String) agentData.get("version") : "1.0.0",
                    "终端初始化中",
                    System.currentTimeMillis(),
                    System.currentTimeMillis()
            );

            // 添加到存储
            endAgents.put(agentId, newAgent);

            // 模拟终端激活过程
            activateAgentAsync(agentId);

            response.put("status", "success");
            response.put("message", "End agent added successfully");
            response.put("data", newAgent);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error adding end agent: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to add end agent: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 删除终端
     */
    @DeleteMapping("/delete/{agentId}")
    public Map<String, Object> deleteEndAgent(@PathVariable String agentId) {
        log.info("Delete end agent requested: agentId={}", agentId);
        Map<String, Object> response = new HashMap<>();

        try {
            EndAgent agent = endAgents.get(agentId);
            if (agent == null) {
                response.put("status", "error");
                response.put("message", "End agent not found: " + agentId);
                response.put("code", 404);
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }

            // 从存储中删除
            endAgents.remove(agentId);

            response.put("status", "success");
            response.put("message", "End agent deleted successfully");
            Map<String, Object> data = new ConcurrentHashMap<>();
            data.put("agentId", agentId);
            response.put("data", data);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error deleting end agent: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to delete end agent: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 更新终端状态
     */
    @PutMapping("/update/{agentId}")
    public Map<String, Object> updateEndAgent(@PathVariable String agentId, @RequestBody Map<String, Object> updateData) {
        log.info("Update end agent requested: agentId={}, data={}", agentId, updateData);
        Map<String, Object> response = new HashMap<>();

        try {
            EndAgent agent = endAgents.get(agentId);
            if (agent == null) {
                response.put("status", "error");
                response.put("message", "End agent not found: " + agentId);
                response.put("code", 404);
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }

            // 更新字段
            if (updateData.containsKey("name")) {
                agent.setName((String) updateData.get("name"));
            }
            if (updateData.containsKey("type")) {
                agent.setType((String) updateData.get("type"));
            }
            if (updateData.containsKey("status")) {
                agent.setStatus((String) updateData.get("status"));
            }
            if (updateData.containsKey("ipAddress")) {
                agent.setIpAddress((String) updateData.get("ipAddress"));
            }
            if (updateData.containsKey("routeAgentId")) {
                agent.setRouteAgentId((String) updateData.get("routeAgentId"));
            }
            if (updateData.containsKey("description")) {
                agent.setDescription((String) updateData.get("description"));
            }

            // 更新时间戳
            agent.setLastUpdated(System.currentTimeMillis());

            response.put("status", "success");
            response.put("message", "End agent updated successfully");
            response.put("data", agent);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error updating end agent: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to update end agent: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 发现终端
     */
    @PostMapping("/discover")
    public Map<String, Object> discoverEndAgents() {
        log.info("Discover end agents requested");
        Map<String, Object> response = new HashMap<>();

        try {
            if (discoveryInProgress) {
                response.put("status", "error");
                response.put("message", "End agent discovery is already in progress");
                response.put("code", 409);
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }

            // 标记开始发现
            discoveryInProgress = true;

            // 异步执行终端发现
            discoverAgentsAsync();

            response.put("status", "success");
            response.put("message", "End agent discovery initiated successfully");
            Map<String, Object> data = new ConcurrentHashMap<>();
            data.put("status", "discovering");
            data.put("message", "终端发现正在进行中，请稍后查询结果");
            response.put("data", data);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error initiating end agent discovery: {}", e.getMessage());
            discoveryInProgress = false;
            response.put("status", "error");
            response.put("message", "Failed to initiate end agent discovery: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 获取终端发现状态
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
            status.put("agentCount", endAgents.size());

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
     * 获取终端统计
     */
    @GetMapping("/stats")
    public Map<String, Object> getEndAgentStats() {
        log.info("Get end agent stats requested");
        Map<String, Object> response = new HashMap<>();

        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalAgents", endAgents.size());
            stats.put("statusSummary", calculateStatusSummary());
            stats.put("typeSummary", calculateTypeSummary());
            stats.put("onlineRate", calculateOnlineRate());
            stats.put("lastDiscoveryTime", lastDiscoveryTime);

            response.put("status", "success");
            response.put("message", "End agent stats retrieved successfully");
            response.put("data", stats);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting end agent stats: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to get end agent stats: " + e.getMessage());
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
        statusSummary.put("inactive", 0);
        statusSummary.put("pending", 0);

        for (EndAgent agent : endAgents.values()) {
            String status = agent.getStatus();
            statusSummary.put(status, statusSummary.getOrDefault(status, 0) + 1);
        }

        return statusSummary;
    }

    // 辅助方法：计算类型摘要
    private Map<String, Integer> calculateTypeSummary() {
        Map<String, Integer> typeSummary = new HashMap<>();

        for (EndAgent agent : endAgents.values()) {
            String type = agent.getType();
            typeSummary.put(type, typeSummary.getOrDefault(type, 0) + 1);
        }

        return typeSummary;
    }

    // 辅助方法：计算在线率
    private double calculateOnlineRate() {
        if (endAgents.isEmpty()) {
            return 0;
        }
        int activeAgents = (int) endAgents.values().stream().filter(agent -> "active".equals(agent.getStatus())).count();
        return (double) activeAgents / endAgents.size() * 100;
    }

    // 辅助方法：生成终端历史数据
    private List<Map<String, Object>> generateAgentHistory(String agentId) {
        List<Map<String, Object>> history = new ArrayList<>();
        long now = System.currentTimeMillis();

        for (int i = 0; i < 24; i++) {
            long timestamp = now - (i * 3600000); // 过去24小时，每小时一条数据
            Map<String, Object> dataPoint = new HashMap<>();
            dataPoint.put("timestamp", timestamp);
            dataPoint.put("status", i % 10 == 0 ? "degraded" : "active");
            dataPoint.put("description", i % 10 == 0 ? "性能下降" : "在线");
            history.add(dataPoint);
        }

        return history;
    }

    // 异步执行终端发现
    private void discoverAgentsAsync() {
        new Thread(() -> {
            try {
                log.info("End agent discovery started");
                
                // 模拟发现过程
                Thread.sleep(3000); // 模拟3秒的发现时间

                // 模拟发现新终端
                String newAgentId = "end-agent-discovered-" + System.currentTimeMillis();
                EndAgent newAgent = new EndAgent(
                        newAgentId,
                        "新发现的终端",
                        "sensor",
                        "active",
                        "192.168.1.150",
                        "route-agent-north",
                        "1.0.0",
                        "新发现",
                        System.currentTimeMillis(),
                        System.currentTimeMillis()
                );

                // 添加新终端
                endAgents.put(newAgentId, newAgent);

                // 更新现有终端状态
                for (EndAgent agent : endAgents.values()) {
                    if ("pending".equals(agent.getStatus())) {
                        agent.setStatus("active");
                        agent.setDescription("终端已激活");
                        agent.setLastUpdated(System.currentTimeMillis());
                    }
                }

                // 标记完成
                lastDiscoveryTime = System.currentTimeMillis();
                log.info("End agent discovery completed successfully");

            } catch (InterruptedException e) {
                log.error("End agent discovery interrupted: {}", e.getMessage());
            } finally {
                discoveryInProgress = false;
            }
        }).start();
    }

    // 异步激活终端
    private void activateAgentAsync(String agentId) {
        new Thread(() -> {
            try {
                Thread.sleep(2000); // 模拟激活过程
                EndAgent agent = endAgents.get(agentId);
                if (agent != null) {
                    agent.setStatus("active");
                    agent.setDescription("终端激活成功");
                    agent.setLastUpdated(System.currentTimeMillis());
                }
            } catch (InterruptedException e) {
                log.error("End agent activation simulation interrupted: {}", e.getMessage());
            }
        }).start();
    }

    // 终端类
    private static class EndAgent {
        private final String agentId;
        private String name;
        private String type;
        private String status;
        private String ipAddress;
        private String routeAgentId;
        private final String version;
        private String description;
        private final long createdAt;
        private long lastUpdated;

        public EndAgent(String agentId, String name, String type, String status, String ipAddress, String routeAgentId, String version, String description, long createdAt, long lastUpdated) {
            this.agentId = agentId;
            this.name = name;
            this.type = type;
            this.status = status;
            this.ipAddress = ipAddress;
            this.routeAgentId = routeAgentId;
            this.version = version;
            this.description = description;
            this.createdAt = createdAt;
            this.lastUpdated = lastUpdated;
        }

        public String getAgentId() {
            return agentId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }

        public String getRouteAgentId() {
            return routeAgentId;
        }

        public void setRouteAgentId(String routeAgentId) {
            this.routeAgentId = routeAgentId;
        }

        public String getVersion() {
            return version;
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

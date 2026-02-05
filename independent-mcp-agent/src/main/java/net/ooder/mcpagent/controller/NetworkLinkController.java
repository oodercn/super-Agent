package net.ooder.mcpagent.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/network/link")
public class NetworkLinkController {

    private static final Logger log = LoggerFactory.getLogger(NetworkLinkController.class);

    // 网络链路数据存储
    private final Map<String, NetworkLink> networkLinks = new ConcurrentHashMap<>();

    // 初始化默认链路数据
    public NetworkLinkController() {
        initializeDefaultLinks();
    }

    private void initializeDefaultLinks() {
        networkLinks.put("link-001", new NetworkLink(
                "link-001",
                "mcp-agent-01",
                "route-agent-east",
                "direct",
                "active",
                98.5,
                1000,
                10,
                "稳定",
                System.currentTimeMillis() - 3600000,
                System.currentTimeMillis()
        ));

        networkLinks.put("link-002", new NetworkLink(
                "link-002",
                "mcp-agent-01",
                "route-agent-west",
                "direct",
                "active",
                99.2,
                1200,
                8,
                "稳定",
                System.currentTimeMillis() - 7200000,
                System.currentTimeMillis()
        ));

        networkLinks.put("link-003", new NetworkLink(
                "link-003",
                "mcp-agent-01",
                "route-agent-north",
                "direct",
                "active",
                97.8,
                950,
                12,
                "稳定",
                System.currentTimeMillis() - 10800000,
                System.currentTimeMillis()
        ));

        networkLinks.put("link-004", new NetworkLink(
                "link-004",
                "mcp-agent-01",
                "route-agent-south",
                "direct",
                "degraded",
                75.3,
                600,
                35,
                "丢包率较高",
                System.currentTimeMillis() - 14400000,
                System.currentTimeMillis() - 300000
        ));

        networkLinks.put("link-005", new NetworkLink(
                "link-005",
                "route-agent-east",
                "end-agent-east-01",
                "indirect",
                "active",
                96.7,
                800,
                15,
                "稳定",
                System.currentTimeMillis() - 18000000,
                System.currentTimeMillis()
        ));

        networkLinks.put("link-006", new NetworkLink(
                "link-006",
                "route-agent-west",
                "end-agent-west-01",
                "indirect",
                "active",
                98.1,
                900,
                12,
                "稳定",
                System.currentTimeMillis() - 21600000,
                System.currentTimeMillis()
        ));
    }

    /**
     * 获取网络链路列表
     */
    @GetMapping("/list")
    public Map<String, Object> getNetworkLinks(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String sourceAgentId,
            @RequestParam(required = false) String targetAgentId) {
        log.info("Get network links requested: status={}, type={}, sourceAgentId={}, targetAgentId={}", status, type, sourceAgentId, targetAgentId);
        Map<String, Object> response = new HashMap<>();

        try {
            List<NetworkLink> filteredLinks = new ArrayList<>();
            for (NetworkLink link : networkLinks.values()) {
                if ((status == null || link.getStatus().equals(status)) &&
                    (type == null || link.getType().equals(type)) &&
                    (sourceAgentId == null || link.getSourceAgentId().equals(sourceAgentId)) &&
                    (targetAgentId == null || link.getTargetAgentId().equals(targetAgentId))) {
                    filteredLinks.add(link);
                }
            }

            // 按最后更新时间排序
            filteredLinks.sort(Comparator.comparingLong(NetworkLink::getLastUpdated).reversed());

            Map<String, Object> data = new HashMap<>();
            data.put("links", filteredLinks);
            data.put("total", filteredLinks.size());
            data.put("statusSummary", calculateStatusSummary());

            response.put("status", "success");
            response.put("message", "Network links retrieved successfully");
            response.put("data", data);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting network links: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to get network links: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 获取网络链路详情
     */
    @GetMapping("/detail/{linkId}")
    public Map<String, Object> getNetworkLinkDetail(@PathVariable String linkId) {
        log.info("Get network link detail requested: linkId={}", linkId);
        Map<String, Object> response = new HashMap<>();

        try {
            NetworkLink link = networkLinks.get(linkId);
            if (link == null) {
                response.put("status", "error");
                response.put("message", "Network link not found: " + linkId);
                response.put("code", 404);
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }

            // 更新最后检查时间
            link.setLastUpdated(System.currentTimeMillis());

            // 生成链路历史数据（模拟）
            List<Map<String, Object>> history = generateLinkHistory(linkId);

            Map<String, Object> data = new HashMap<>();
            data.put("link", link);
            data.put("history", history);
            data.put("healthScore", calculateLinkHealthScore(link));

            response.put("status", "success");
            response.put("message", "Network link detail retrieved successfully");
            response.put("data", data);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting network link detail: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to get network link detail: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 添加网络链路
     */
    @PostMapping("/add")
    public Map<String, Object> addNetworkLink(@RequestBody Map<String, Object> linkData) {
        log.info("Add network link requested: {}", linkData);
        Map<String, Object> response = new HashMap<>();

        try {
            // 验证必要字段
            if (!linkData.containsKey("sourceAgentId") || !linkData.containsKey("targetAgentId")) {
                response.put("status", "error");
                response.put("message", "Missing required fields: sourceAgentId and targetAgentId are required");
                response.put("code", 400);
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }

            // 生成链路ID
            String linkId = linkData.containsKey("linkId") ? (String) linkData.get("linkId") : "link-" + System.currentTimeMillis();

            // 检查链路是否已存在
            if (networkLinks.containsKey(linkId)) {
                response.put("status", "error");
                response.put("message", "Network link already exists: " + linkId);
                response.put("code", 409);
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }

            // 创建新链路
            NetworkLink newLink = new NetworkLink(
                    linkId,
                    (String) linkData.get("sourceAgentId"),
                    (String) linkData.get("targetAgentId"),
                    linkData.containsKey("type") ? (String) linkData.get("type") : "direct",
                    "pending",
                    0.0,
                    0,
                    0,
                    "链路初始化中",
                    System.currentTimeMillis(),
                    System.currentTimeMillis()
            );

            // 添加到存储
            networkLinks.put(linkId, newLink);

            // 模拟链路激活过程
            activateLinkAsync(linkId);

            response.put("status", "success");
            response.put("message", "Network link added successfully");
            response.put("data", newLink);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error adding network link: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to add network link: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 删除网络链路
     */
    @DeleteMapping("/delete/{linkId}")
    public Map<String, Object> deleteNetworkLink(@PathVariable String linkId) {
        log.info("Delete network link requested: linkId={}", linkId);
        Map<String, Object> response = new HashMap<>();

        try {
            NetworkLink link = networkLinks.get(linkId);
            if (link == null) {
                response.put("status", "error");
                response.put("message", "Network link not found: " + linkId);
                response.put("code", 404);
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }

            // 从存储中删除
            networkLinks.remove(linkId);

            response.put("status", "success");
            response.put("message", "Network link deleted successfully");
            Map<String, Object> data = new ConcurrentHashMap<>();
            data.put("linkId", linkId);
            response.put("data", data);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error deleting network link: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to delete network link: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 更新网络链路状态
     */
    @PutMapping("/update/{linkId}")
    public Map<String, Object> updateNetworkLink(@PathVariable String linkId, @RequestBody Map<String, Object> updateData) {
        log.info("Update network link requested: linkId={}, data={}", linkId, updateData);
        Map<String, Object> response = new HashMap<>();

        try {
            NetworkLink link = networkLinks.get(linkId);
            if (link == null) {
                response.put("status", "error");
                response.put("message", "Network link not found: " + linkId);
                response.put("code", 404);
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }

            // 更新字段
            if (updateData.containsKey("status")) {
                link.setStatus((String) updateData.get("status"));
            }
            if (updateData.containsKey("type")) {
                link.setType((String) updateData.get("type"));
            }
            if (updateData.containsKey("description")) {
                link.setDescription((String) updateData.get("description"));
            }

            // 更新时间戳
            link.setLastUpdated(System.currentTimeMillis());

            response.put("status", "success");
            response.put("message", "Network link updated successfully");
            response.put("data", link);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error updating network link: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to update network link: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 刷新网络链路状态
     */
    @PostMapping("/refresh/{linkId}")
    public Map<String, Object> refreshNetworkLinkStatus(@PathVariable String linkId) {
        log.info("Refresh network link status requested: linkId={}", linkId);
        Map<String, Object> response = new HashMap<>();

        try {
            NetworkLink link = networkLinks.get(linkId);
            if (link == null) {
                response.put("status", "error");
                response.put("message", "Network link not found: " + linkId);
                response.put("code", 404);
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }

            // 模拟刷新链路状态
            refreshLinkStatusAsync(linkId);

            response.put("status", "success");
            response.put("message", "Network link status refresh initiated successfully");
            Map<String, Object> data = new ConcurrentHashMap<>();
            data.put("linkId", linkId);
            data.put("status", "refreshing");
            response.put("data", data);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error refreshing network link status: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to refresh network link status: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 获取网络链路统计
     */
    @GetMapping("/stats")
    public Map<String, Object> getNetworkLinkStats() {
        log.info("Get network link stats requested");
        Map<String, Object> response = new HashMap<>();

        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalLinks", networkLinks.size());
            stats.put("statusSummary", calculateStatusSummary());
            stats.put("typeSummary", calculateTypeSummary());
            stats.put("averageLatency", calculateAverageLatency());
            stats.put("averageBandwidth", calculateAverageBandwidth());
            stats.put("averageReliability", calculateAverageReliability());
            stats.put("healthScore", calculateOverallHealthScore());

            response.put("status", "success");
            response.put("message", "Network link stats retrieved successfully");
            response.put("data", stats);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting network link stats: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to get network link stats: " + e.getMessage());
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
        statusSummary.put("error", 0);

        for (NetworkLink link : networkLinks.values()) {
            String status = link.getStatus();
            statusSummary.put(status, statusSummary.getOrDefault(status, 0) + 1);
        }

        return statusSummary;
    }

    // 辅助方法：计算类型摘要
    private Map<String, Integer> calculateTypeSummary() {
        Map<String, Integer> typeSummary = new HashMap<>();
        typeSummary.put("direct", 0);
        typeSummary.put("indirect", 0);

        for (NetworkLink link : networkLinks.values()) {
            String type = link.getType();
            typeSummary.put(type, typeSummary.getOrDefault(type, 0) + 1);
        }

        return typeSummary;
    }

    // 辅助方法：计算平均延迟
    private double calculateAverageLatency() {
        if (networkLinks.isEmpty()) {
            return 0;
        }
        return networkLinks.values().stream().mapToInt(NetworkLink::getLatency).average().orElse(0);
    }

    // 辅助方法：计算平均带宽
    private double calculateAverageBandwidth() {
        if (networkLinks.isEmpty()) {
            return 0;
        }
        return networkLinks.values().stream().mapToInt(NetworkLink::getBandwidth).average().orElse(0);
    }

    // 辅助方法：计算平均可靠性
    private double calculateAverageReliability() {
        if (networkLinks.isEmpty()) {
            return 0;
        }
        return networkLinks.values().stream().mapToDouble(NetworkLink::getReliability).average().orElse(0);
    }

    // 辅助方法：计算链路健康分数
    private double calculateLinkHealthScore(NetworkLink link) {
        double reliabilityScore = link.getReliability() * 0.5;
        double latencyScore = Math.max(0, 50 - (link.getLatency() / 100.0)) * 0.3;
        double bandwidthScore = Math.min(50, link.getBandwidth() / 20.0) * 0.2;
        return reliabilityScore + latencyScore + bandwidthScore;
    }

    // 辅助方法：计算整体健康分数
    private double calculateOverallHealthScore() {
        if (networkLinks.isEmpty()) {
            return 0;
        }
        return networkLinks.values().stream().mapToDouble(this::calculateLinkHealthScore).average().orElse(0);
    }

    // 辅助方法：生成链路历史数据
    private List<Map<String, Object>> generateLinkHistory(String linkId) {
        List<Map<String, Object>> history = new ArrayList<>();
        long now = System.currentTimeMillis();

        for (int i = 0; i < 24; i++) {
            long timestamp = now - (i * 3600000); // 过去24小时，每小时一条数据
            Map<String, Object> dataPoint = new HashMap<>();
            dataPoint.put("timestamp", timestamp);
            dataPoint.put("reliability", 90 + Math.random() * 10); // 90-100之间的随机值
            dataPoint.put("latency", 5 + Math.random() * 15); // 5-20之间的随机值
            dataPoint.put("bandwidth", 800 + Math.random() * 400); // 800-1200之间的随机值
            history.add(dataPoint);
        }

        return history;
    }

    // 异步激活链路
    private void activateLinkAsync(String linkId) {
        new Thread(() -> {
            try {
                Thread.sleep(2000); // 模拟激活过程
                NetworkLink link = networkLinks.get(linkId);
                if (link != null) {
                    link.setStatus("active");
                    link.setReliability(95 + Math.random() * 5); // 95-100之间的随机值
                    link.setBandwidth(800 + (int)(Math.random() * 400)); // 800-1200之间的随机值
                    link.setLatency(5 + (int)(Math.random() * 15)); // 5-20之间的随机值
                    link.setDescription("链路激活成功");
                    link.setLastUpdated(System.currentTimeMillis());
                }
            } catch (InterruptedException e) {
                log.error("Link activation simulation interrupted: {}", e.getMessage());
            }
        }).start();
    }

    // 异步刷新链路状态
    private void refreshLinkStatusAsync(String linkId) {
        new Thread(() -> {
            try {
                Thread.sleep(1000); // 模拟刷新过程
                NetworkLink link = networkLinks.get(linkId);
                if (link != null) {
                    // 模拟状态波动
                    link.setReliability(Math.max(70, Math.min(100, link.getReliability() + (Math.random() * 6 - 3))));
                    link.setBandwidth(Math.max(500, Math.min(1500, link.getBandwidth() + (int)(Math.random() * 200 - 100))));
                    link.setLatency(Math.max(1, Math.min(50, link.getLatency() + (int)(Math.random() * 10 - 5))));
                    
                    // 更新状态
                    if (link.getReliability() >= 90) {
                        link.setStatus("active");
                        link.setDescription("稳定");
                    } else if (link.getReliability() >= 70) {
                        link.setStatus("degraded");
                        link.setDescription("性能下降");
                    } else {
                        link.setStatus("error");
                        link.setDescription("链路异常");
                    }
                    
                    link.setLastUpdated(System.currentTimeMillis());
                }
            } catch (InterruptedException e) {
                log.error("Link status refresh simulation interrupted: {}", e.getMessage());
            }
        }).start();
    }

    // 网络链路类
    private static class NetworkLink {
        private final String linkId;
        private final String sourceAgentId;
        private final String targetAgentId;
        private String type;
        private String status;
        private double reliability;
        private int bandwidth;
        private int latency;
        private String description;
        private final long createdAt;
        private long lastUpdated;

        public NetworkLink(String linkId, String sourceAgentId, String targetAgentId, String type, String status, double reliability, int bandwidth, int latency, String description, long createdAt, long lastUpdated) {
            this.linkId = linkId;
            this.sourceAgentId = sourceAgentId;
            this.targetAgentId = targetAgentId;
            this.type = type;
            this.status = status;
            this.reliability = reliability;
            this.bandwidth = bandwidth;
            this.latency = latency;
            this.description = description;
            this.createdAt = createdAt;
            this.lastUpdated = lastUpdated;
        }

        public String getLinkId() {
            return linkId;
        }

        public String getSourceAgentId() {
            return sourceAgentId;
        }

        public String getTargetAgentId() {
            return targetAgentId;
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

        public double getReliability() {
            return reliability;
        }

        public void setReliability(double reliability) {
            this.reliability = reliability;
        }

        public int getBandwidth() {
            return bandwidth;
        }

        public void setBandwidth(int bandwidth) {
            this.bandwidth = bandwidth;
        }

        public int getLatency() {
            return latency;
        }

        public void setLatency(int latency) {
            this.latency = latency;
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

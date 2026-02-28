package net.ooder.nexus.adapter.inbound.controller.network;

import net.ooder.nexus.dto.network.*;
import net.ooder.nexus.model.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import net.ooder.nexus.infrastructure.management.NexusManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/network/link")
public class NetworkLinkController {

    private static final Logger log = LoggerFactory.getLogger(NetworkLinkController.class);

    @Autowired
    private NexusManager nexusManager;

    private final Map<String, NetworkLinkEntity> networkLinks = new ConcurrentHashMap<>();

    public NetworkLinkController() {
        initializeDefaultLinks();
    }

    private void initializeDefaultLinks() {
        networkLinks.put("link-001", new NetworkLinkEntity(
                "link-001", "mcp-agent-01", "route-agent-east", "direct", "active",
                98.5, 1000, 10, "Stable",
                System.currentTimeMillis() - 3600000, System.currentTimeMillis()));

        networkLinks.put("link-002", new NetworkLinkEntity(
                "link-002", "mcp-agent-01", "route-agent-west", "direct", "active",
                99.2, 1200, 8, "Stable",
                System.currentTimeMillis() - 7200000, System.currentTimeMillis()));

        networkLinks.put("link-003", new NetworkLinkEntity(
                "link-003", "mcp-agent-01", "route-agent-north", "direct", "active",
                97.8, 950, 12, "Stable",
                System.currentTimeMillis() - 10800000, System.currentTimeMillis()));

        networkLinks.put("link-004", new NetworkLinkEntity(
                "link-004", "mcp-agent-01", "route-agent-south", "direct", "degraded",
                75.3, 600, 35, "High packet loss",
                System.currentTimeMillis() - 14400000, System.currentTimeMillis() - 300000));

        networkLinks.put("link-005", new NetworkLinkEntity(
                "link-005", "route-agent-east", "end-agent-east-01", "indirect", "active",
                96.7, 800, 15, "Stable",
                System.currentTimeMillis() - 18000000, System.currentTimeMillis()));

        networkLinks.put("link-006", new NetworkLinkEntity(
                "link-006", "route-agent-west", "end-agent-west-01", "indirect", "active",
                98.1, 900, 12, "Stable",
                System.currentTimeMillis() - 21600000, System.currentTimeMillis()));
    }

    @GetMapping("/list")
    public ApiResponse<NetworkLinkListDTO> getNetworkLinks(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String sourceAgentId,
            @RequestParam(required = false) String targetAgentId) {
        return getNetworkLinksInternal(status, type, sourceAgentId, targetAgentId);
    }

    @GetMapping("")
    public ApiResponse<NetworkLinkListDTO> getNetworkLinksAlias(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String sourceAgentId,
            @RequestParam(required = false) String targetAgentId) {
        return getNetworkLinksInternal(status, type, sourceAgentId, targetAgentId);
    }

    private ApiResponse<NetworkLinkListDTO> getNetworkLinksInternal(
            String status, String type, String sourceAgentId, String targetAgentId) {
        log.info("Get network links requested: status={}, type={}, sourceAgentId={}, targetAgentId={}", status, type, sourceAgentId, targetAgentId);

        try {
            List<NetworkLinkDTO> filteredLinks = new ArrayList<>();
            for (NetworkLinkEntity link : networkLinks.values()) {
                if ((status == null || link.getStatus().equals(status)) &&
                    (type == null || link.getType().equals(type)) &&
                    (sourceAgentId == null || link.getSourceAgentId().equals(sourceAgentId)) &&
                    (targetAgentId == null || link.getTargetAgentId().equals(targetAgentId))) {
                    filteredLinks.add(convertToDTO(link));
                }
            }

            filteredLinks.sort(Comparator.comparingLong(NetworkLinkDTO::getLastUpdated).reversed());

            NetworkLinkListDTO data = new NetworkLinkListDTO();
            data.setLinks(filteredLinks);
            data.setTotal(filteredLinks.size());
            data.setStatusSummary(calculateStatusSummary());

            return ApiResponse.success(data);
        } catch (Exception e) {
            log.error("Error getting network links: {}", e.getMessage());
            return ApiResponse.error("Failed to get network links: " + e.getMessage());
        }
    }

    @GetMapping("/detail/{linkId}")
    public ApiResponse<NetworkLinkDetailDTO> getNetworkLinkDetail(@PathVariable String linkId) {
        log.info("Get network link detail requested: linkId={}", linkId);

        try {
            NetworkLinkEntity link = networkLinks.get(linkId);
            if (link == null) {
                return ApiResponse.notFound("Network link not found: " + linkId);
            }

            link.setLastUpdated(System.currentTimeMillis());

            NetworkLinkDetailDTO data = new NetworkLinkDetailDTO();
            data.setLink(convertToDTO(link));
            data.setHistory(generateLinkHistory(linkId));
            data.setHealthScore(calculateLinkHealthScore(link));

            return ApiResponse.success(data);
        } catch (Exception e) {
            log.error("Error getting network link detail: {}", e.getMessage());
            return ApiResponse.error("Failed to get network link detail: " + e.getMessage());
        }
    }

    @PostMapping("/add")
    public ApiResponse<NetworkLinkDTO> addNetworkLink(@RequestBody NetworkLinkCreateDTO request) {
        log.info("Add network link requested: {}", request.getSourceAgentId());

        try {
            if (request.getSourceAgentId() == null || request.getTargetAgentId() == null) {
                return ApiResponse.badRequest("Missing required fields: sourceAgentId and targetAgentId are required");
            }

            String linkId = request.getLinkId() != null ? request.getLinkId() : "link-" + System.currentTimeMillis();

            if (networkLinks.containsKey(linkId)) {
                return ApiResponse.error("Network link already exists: " + linkId);
            }

            NetworkLinkEntity newLink = new NetworkLinkEntity(
                    linkId,
                    request.getSourceAgentId(),
                    request.getTargetAgentId(),
                    request.getType() != null ? request.getType() : "direct",
                    "pending", 0.0, 0, 0, "Link initializing",
                    System.currentTimeMillis(), System.currentTimeMillis());

            networkLinks.put(linkId, newLink);

            if (nexusManager != null) {
                try {
                    Map<String, Object> connectionInfo = new HashMap<>();
                    connectionInfo.put("linkId", linkId);
                    connectionInfo.put("type", request.getType() != null ? request.getType() : "direct");
                    connectionInfo.put("status", "pending");
                    connectionInfo.put("description", "Link initializing");
                    nexusManager.createNetworkConnection(
                            request.getSourceAgentId(),
                            request.getTargetAgentId(),
                            connectionInfo);
                    log.info("Network link registered with SDK: {}", linkId);
                } catch (Exception sdkEx) {
                    log.warn("Failed to register network link with SDK: {}", sdkEx.getMessage());
                }
            }

            activateLinkAsync(linkId);

            return ApiResponse.success("Network link added successfully", convertToDTO(newLink));
        } catch (Exception e) {
            log.error("Error adding network link: {}", e.getMessage());
            return ApiResponse.error("Failed to add network link: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{linkId}")
    public ApiResponse<String> deleteNetworkLink(@PathVariable String linkId) {
        log.info("Delete network link requested: linkId={}", linkId);

        try {
            NetworkLinkEntity link = networkLinks.get(linkId);
            if (link == null) {
                return ApiResponse.notFound("Network link not found: " + linkId);
            }

            networkLinks.remove(linkId);

            if (nexusManager != null) {
                try {
                    Map<String, Object> topology = nexusManager.getNetworkTopology();
                    if (topology.containsKey("connections")) {
                        Map<String, Map<String, Object>> connections = (Map<String, Map<String, Object>>) topology.get("connections");
                        connections.entrySet().removeIf(entry -> {
                            Map<String, Object> connInfo = entry.getValue();
                            return linkId.equals(connInfo.get("linkId"));
                        });
                    }
                    log.info("Network link removed from SDK: {}", linkId);
                } catch (Exception sdkEx) {
                    log.warn("Failed to remove network link from SDK: {}", sdkEx.getMessage());
                }
            }

            return ApiResponse.success("Network link deleted successfully", linkId);
        } catch (Exception e) {
            log.error("Error deleting network link: {}", e.getMessage());
            return ApiResponse.error("Failed to delete network link: " + e.getMessage());
        }
    }

    @PutMapping("/update/{linkId}")
    public ApiResponse<NetworkLinkDTO> updateNetworkLink(@PathVariable String linkId, @RequestBody NetworkLinkUpdateDTO request) {
        log.info("Update network link requested: linkId={}", linkId);

        try {
            NetworkLinkEntity link = networkLinks.get(linkId);
            if (link == null) {
                return ApiResponse.notFound("Network link not found: " + linkId);
            }

            if (request.getStatus() != null) link.setStatus(request.getStatus());
            if (request.getType() != null) link.setType(request.getType());
            if (request.getDescription() != null) link.setDescription(request.getDescription());

            link.setLastUpdated(System.currentTimeMillis());

            return ApiResponse.success("Network link updated successfully", convertToDTO(link));
        } catch (Exception e) {
            log.error("Error updating network link: {}", e.getMessage());
            return ApiResponse.error("Failed to update network link: " + e.getMessage());
        }
    }

    @PostMapping("/refresh/{linkId}")
    public ApiResponse<String> refreshNetworkLinkStatus(@PathVariable String linkId) {
        log.info("Refresh network link status requested: linkId={}", linkId);

        try {
            NetworkLinkEntity link = networkLinks.get(linkId);
            if (link == null) {
                return ApiResponse.notFound("Network link not found: " + linkId);
            }

            refreshLinkStatusAsync(linkId);

            return ApiResponse.success("Network link status refresh initiated successfully", linkId);
        } catch (Exception e) {
            log.error("Error refreshing network link status: {}", e.getMessage());
            return ApiResponse.error("Failed to refresh network link status: " + e.getMessage());
        }
    }

    @GetMapping("/stats")
    public ApiResponse<NetworkLinkStatsDTO> getNetworkLinkStats() {
        log.info("Get network link stats requested");

        try {
            NetworkLinkStatsDTO stats = new NetworkLinkStatsDTO();
            stats.setTotalLinks(networkLinks.size());
            stats.setStatusSummary(calculateStatusSummary());
            stats.setTypeSummary(calculateTypeSummary());
            stats.setAverageLatency(calculateAverageLatency());
            stats.setAverageBandwidth(calculateAverageBandwidth());
            stats.setAverageReliability(calculateAverageReliability());
            stats.setHealthScore(calculateOverallHealthScore());

            return ApiResponse.success(stats);
        } catch (Exception e) {
            log.error("Error getting network link stats: {}", e.getMessage());
            return ApiResponse.error("Failed to get network link stats: " + e.getMessage());
        }
    }

    private Map<String, Integer> calculateStatusSummary() {
        Map<String, Integer> statusSummary = new HashMap<>();
        statusSummary.put("active", 0);
        statusSummary.put("degraded", 0);
        statusSummary.put("inactive", 0);
        statusSummary.put("pending", 0);
        statusSummary.put("error", 0);

        for (NetworkLinkEntity link : networkLinks.values()) {
            String status = link.getStatus();
            statusSummary.put(status, statusSummary.getOrDefault(status, 0) + 1);
        }

        return statusSummary;
    }

    private Map<String, Integer> calculateTypeSummary() {
        Map<String, Integer> typeSummary = new HashMap<>();
        typeSummary.put("direct", 0);
        typeSummary.put("indirect", 0);

        for (NetworkLinkEntity link : networkLinks.values()) {
            String type = link.getType();
            typeSummary.put(type, typeSummary.getOrDefault(type, 0) + 1);
        }

        return typeSummary;
    }

    private double calculateAverageLatency() {
        if (networkLinks.isEmpty()) return 0;
        return networkLinks.values().stream().mapToInt(NetworkLinkEntity::getLatency).average().orElse(0);
    }

    private double calculateAverageBandwidth() {
        if (networkLinks.isEmpty()) return 0;
        return networkLinks.values().stream().mapToInt(NetworkLinkEntity::getBandwidth).average().orElse(0);
    }

    private double calculateAverageReliability() {
        if (networkLinks.isEmpty()) return 0;
        return networkLinks.values().stream().mapToDouble(NetworkLinkEntity::getReliability).average().orElse(0);
    }

    private double calculateLinkHealthScore(NetworkLinkEntity link) {
        double reliabilityScore = link.getReliability() * 0.5;
        double latencyScore = Math.max(0, 50 - (link.getLatency() / 100.0)) * 0.3;
        double bandwidthScore = Math.min(50, link.getBandwidth() / 20.0) * 0.2;
        return reliabilityScore + latencyScore + bandwidthScore;
    }

    private double calculateOverallHealthScore() {
        if (networkLinks.isEmpty()) return 0;
        return networkLinks.values().stream().mapToDouble(this::calculateLinkHealthScore).average().orElse(0);
    }

    private List<NetworkLinkDetailDTO.NetworkLinkHistoryDTO> generateLinkHistory(String linkId) {
        List<NetworkLinkDetailDTO.NetworkLinkHistoryDTO> history = new ArrayList<>();
        long now = System.currentTimeMillis();

        for (int i = 0; i < 24; i++) {
            long timestamp = now - (i * 3600000);
            NetworkLinkDetailDTO.NetworkLinkHistoryDTO dataPoint = new NetworkLinkDetailDTO.NetworkLinkHistoryDTO();
            dataPoint.setTimestamp(timestamp);
            dataPoint.setReliability(90 + Math.random() * 10);
            dataPoint.setLatency(5 + Math.random() * 15);
            dataPoint.setBandwidth(800 + Math.random() * 400);
            history.add(dataPoint);
        }

        return history;
    }

    private void activateLinkAsync(String linkId) {
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                NetworkLinkEntity link = networkLinks.get(linkId);
                if (link != null) {
                    link.setStatus("active");
                    link.setReliability(95 + Math.random() * 5);
                    link.setBandwidth(800 + (int)(Math.random() * 400));
                    link.setLatency(5 + (int)(Math.random() * 15));
                    link.setDescription("Link activated successfully");
                    link.setLastUpdated(System.currentTimeMillis());
                }
            } catch (InterruptedException e) {
                log.error("Link activation simulation interrupted: {}", e.getMessage());
            }
        }).start();
    }

    private void refreshLinkStatusAsync(String linkId) {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                NetworkLinkEntity link = networkLinks.get(linkId);
                if (link != null) {
                    link.setReliability(Math.max(70, Math.min(100, link.getReliability() + (Math.random() * 6 - 3))));
                    link.setBandwidth(Math.max(500, Math.min(1500, link.getBandwidth() + (int)(Math.random() * 200 - 100))));
                    link.setLatency(Math.max(1, Math.min(50, link.getLatency() + (int)(Math.random() * 10 - 5))));

                    if (link.getReliability() >= 90) {
                        link.setStatus("active");
                        link.setDescription("Stable");
                    } else if (link.getReliability() >= 70) {
                        link.setStatus("degraded");
                        link.setDescription("Performance degraded");
                    } else {
                        link.setStatus("error");
                        link.setDescription("Link error");
                    }

                    link.setLastUpdated(System.currentTimeMillis());
                }
            } catch (InterruptedException e) {
                log.error("Link status refresh simulation interrupted: {}", e.getMessage());
            }
        }).start();
    }

    private NetworkLinkDTO convertToDTO(NetworkLinkEntity entity) {
        NetworkLinkDTO dto = new NetworkLinkDTO();
        dto.setLinkId(entity.getLinkId());
        dto.setSourceAgentId(entity.getSourceAgentId());
        dto.setTargetAgentId(entity.getTargetAgentId());
        dto.setType(entity.getType());
        dto.setStatus(entity.getStatus());
        dto.setReliability(entity.getReliability());
        dto.setBandwidth(entity.getBandwidth());
        dto.setLatency(entity.getLatency());
        dto.setDescription(entity.getDescription());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setLastUpdated(entity.getLastUpdated());
        return dto;
    }

    private static class NetworkLinkEntity {
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

        public NetworkLinkEntity(String linkId, String sourceAgentId, String targetAgentId, String type, String status, double reliability, int bandwidth, int latency, String description, long createdAt, long lastUpdated) {
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

        public String getLinkId() { return linkId; }
        public String getSourceAgentId() { return sourceAgentId; }
        public String getTargetAgentId() { return targetAgentId; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public double getReliability() { return reliability; }
        public void setReliability(double reliability) { this.reliability = reliability; }
        public int getBandwidth() { return bandwidth; }
        public void setBandwidth(int bandwidth) { this.bandwidth = bandwidth; }
        public int getLatency() { return latency; }
        public void setLatency(int latency) { this.latency = latency; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public long getCreatedAt() { return createdAt; }
        public long getLastUpdated() { return lastUpdated; }
        public void setLastUpdated(long lastUpdated) { this.lastUpdated = lastUpdated; }
    }
}

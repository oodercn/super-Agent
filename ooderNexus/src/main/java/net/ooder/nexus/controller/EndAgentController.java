package net.ooder.nexus.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import net.ooder.nexus.management.NexusManager;
import net.ooder.nexus.model.Result;
import net.ooder.nexus.model.network.EndAgent;
import net.ooder.nexus.model.endagent.EndAgentListResult;
import net.ooder.nexus.model.endagent.EndAgentDetailResult;
import net.ooder.nexus.model.endagent.EndAgentDeleteResult;
import net.ooder.nexus.model.endagent.EndAgentDiscoverResult;
import net.ooder.nexus.model.endagent.EndAgentDiscoveryStatusResult;
import net.ooder.nexus.model.endagent.EndAgentStatsResult;
import net.ooder.nexus.service.INexusService;
import net.ooder.nexus.service.NexusServiceFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/network/endagent")
public class EndAgentController {

    private static final Logger log = LoggerFactory.getLogger(EndAgentController.class);

    @Autowired
    private NexusManager nexusManager;

    @Autowired
    private NexusServiceFactory serviceFactory;

    private final Map<String, EndAgent> endAgents = new ConcurrentHashMap<>();

    private volatile boolean discoveryInProgress = false;
    private volatile long lastDiscoveryTime = 0;

    public EndAgentController() {
        initializeDefaultEndAgents();
    }

    private INexusService getService() {
        return serviceFactory.getService();
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

    @GetMapping("/list")
    public Result<EndAgentListResult> getEndAgents(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String routeAgentId) {
        log.info("Get end agents requested: status={}, type={}, routeAgentId={}", status, type, routeAgentId);

        try {
            List<EndAgent> filteredAgents = new ArrayList<>();
            for (EndAgent agent : endAgents.values()) {
                if ((status == null || agent.getStatus().equals(status)) &&
                    (type == null || agent.getType().equals(type)) &&
                    (routeAgentId == null || agent.getRouteAgentId().equals(routeAgentId))) {
                    filteredAgents.add(agent);
                }
            }

            filteredAgents.sort(Comparator.comparingLong(EndAgent::getLastUpdated).reversed());

            EndAgentListResult result = new EndAgentListResult();
            result.setAgents(filteredAgents);
            result.setTotal(filteredAgents.size());
            result.setStatusSummary(calculateStatusSummary());
            result.setTypeSummary(calculateTypeSummary());
            result.setLastDiscoveryTime(lastDiscoveryTime);
            result.setDiscoveryInProgress(discoveryInProgress);

            return Result.success("End agents retrieved successfully", result);
        } catch (Exception e) {
            log.error("Error getting end agents: {}", e.getMessage());
            return Result.error("Failed to get end agents: " + e.getMessage());
        }
    }

    @GetMapping("/detail/{agentId}")
    public Result<EndAgentDetailResult> getEndAgentDetail(@PathVariable String agentId) {
        log.info("Get end agent detail requested: agentId={}", agentId);

        try {
            EndAgent agent = endAgents.get(agentId);
            if (agent == null) {
                return Result.error("End agent not found: " + agentId);
            }

            agent.setLastUpdated(System.currentTimeMillis());

            List<Map<String, Object>> history = generateAgentHistory(agentId);

            EndAgentDetailResult result = new EndAgentDetailResult();
            result.setAgent(agent);
            result.setHistory(history);

            return Result.success("End agent detail retrieved successfully", result);
        } catch (Exception e) {
            log.error("Error getting end agent detail: {}", e.getMessage());
            return Result.error("Failed to get end agent detail: " + e.getMessage());
        }
    }

    @PostMapping("/add")
    public Result<EndAgent> addEndAgent(@RequestBody Map<String, Object> agentData) {
        log.info("Add end agent requested: {}", agentData);

        try {
            if (!agentData.containsKey("name") || !agentData.containsKey("type") || !agentData.containsKey("ipAddress")) {
                return Result.error("Missing required fields: name, type, and ipAddress are required");
            }

            String agentId = agentData.containsKey("agentId") ? (String) agentData.get("agentId") : "end-agent-" + System.currentTimeMillis();

            if (endAgents.containsKey(agentId)) {
                return Result.error("End agent already exists: " + agentId);
            }

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

            endAgents.put(agentId, newAgent);

            if (nexusManager != null) {
                try {
                    Map<String, Object> nodeInfo = new HashMap<>();
                    nodeInfo.put("agentId", agentId);
                    nodeInfo.put("name", agentData.get("name"));
                    nodeInfo.put("type", agentData.get("type"));
                    nodeInfo.put("ipAddress", agentData.get("ipAddress"));
                    nodeInfo.put("routeAgentId", agentData.containsKey("routeAgentId") ? agentData.get("routeAgentId") : "");
                    nodeInfo.put("version", agentData.containsKey("version") ? agentData.get("version") : "1.0.0");
                    nodeInfo.put("status", "pending");
                    nodeInfo.put("description", "终端初始化中");
                    nexusManager.registerNetworkNode(agentId, nodeInfo);
                    log.info("End agent registered with SDK: {}", agentId);
                } catch (Exception sdkEx) {
                    log.warn("Failed to register end agent with SDK: {}", sdkEx.getMessage());
                }
            }

            activateAgentAsync(agentId);

            return Result.success("End agent added successfully", newAgent);
        } catch (Exception e) {
            log.error("Error adding end agent: {}", e.getMessage());
            return Result.error("Failed to add end agent: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{agentId}")
    public Result<EndAgentDeleteResult> deleteEndAgent(@PathVariable String agentId) {
        log.info("Delete end agent requested: agentId={}", agentId);

        try {
            EndAgent agent = endAgents.get(agentId);
            if (agent == null) {
                return Result.error("End agent not found: " + agentId);
            }

            endAgents.remove(agentId);

            if (nexusManager != null) {
                try {
                    nexusManager.removeNetworkNode(agentId);
                    log.info("End agent removed from SDK: {}", agentId);
                } catch (Exception sdkEx) {
                    log.warn("Failed to remove end agent from SDK: {}", sdkEx.getMessage());
                }
            }

            EndAgentDeleteResult result = new EndAgentDeleteResult();
            result.setAgentId(agentId);
            return Result.success("End agent deleted successfully", result);
        } catch (Exception e) {
            log.error("Error deleting end agent: {}", e.getMessage());
            return Result.error("Failed to delete end agent: " + e.getMessage());
        }
    }

    @PutMapping("/update/{agentId}")
    public Result<EndAgent> updateEndAgent(@PathVariable String agentId, @RequestBody Map<String, Object> updateData) {
        log.info("Update end agent requested: agentId={}, data={}", agentId, updateData);

        try {
            EndAgent agent = endAgents.get(agentId);
            if (agent == null) {
                return Result.error("End agent not found: " + agentId);
            }

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

            agent.setLastUpdated(System.currentTimeMillis());

            if (nexusManager != null) {
                try {
                    Map<String, Map<String, Object>> nodes = nexusManager.getNetworkNodes();
                    if (nodes.containsKey(agentId)) {
                        Map<String, Object> nodeInfo = nodes.get(agentId);
                        if (updateData.containsKey("name")) {
                            nodeInfo.put("name", updateData.get("name"));
                        }
                        if (updateData.containsKey("type")) {
                            nodeInfo.put("type", updateData.get("type"));
                        }
                        if (updateData.containsKey("status")) {
                            nodeInfo.put("status", updateData.get("status"));
                        }
                        if (updateData.containsKey("ipAddress")) {
                            nodeInfo.put("ipAddress", updateData.get("ipAddress"));
                        }
                        if (updateData.containsKey("routeAgentId")) {
                            nodeInfo.put("routeAgentId", updateData.get("routeAgentId"));
                        }
                        if (updateData.containsKey("description")) {
                            nodeInfo.put("description", updateData.get("description"));
                        }
                        log.info("End agent updated in SDK: {}", agentId);
                    }
                } catch (Exception sdkEx) {
                    log.warn("Failed to update end agent in SDK: {}", sdkEx.getMessage());
                }
            }

            return Result.success("End agent updated successfully", agent);
        } catch (Exception e) {
            log.error("Error updating end agent: {}", e.getMessage());
            return Result.error("Failed to update end agent: " + e.getMessage());
        }
    }

    @PostMapping("/discover")
    public Result<EndAgentDiscoverResult> discoverEndAgents() {
        log.info("Discover end agents requested");

        try {
            if (discoveryInProgress) {
                return Result.error("End agent discovery is already in progress");
            }

            discoveryInProgress = true;

            discoverAgentsAsync();

            EndAgentDiscoverResult result = new EndAgentDiscoverResult();
            result.setStatus("discovering");
            result.setMessage("终端发现正在进行中，请稍后查询结果");
            return Result.success("End agent discovery initiated successfully", result);
        } catch (Exception e) {
            log.error("Error initiating end agent discovery: {}", e.getMessage());
            discoveryInProgress = false;
            return Result.error("Failed to initiate end agent discovery: " + e.getMessage());
        }
    }

    @GetMapping("/discover/status")
    public Result<EndAgentDiscoveryStatusResult> getDiscoveryStatus() {
        log.info("Get discovery status requested");

        try {
            EndAgentDiscoveryStatusResult result = new EndAgentDiscoveryStatusResult();
            result.setInProgress(discoveryInProgress);
            result.setLastDiscoveryTime(lastDiscoveryTime);
            result.setLastDiscoveryFormatted(lastDiscoveryTime > 0 ? new Date(lastDiscoveryTime).toString() : "Never");
            result.setAgentCount(endAgents.size());

            return Result.success("Discovery status retrieved successfully", result);
        } catch (Exception e) {
            log.error("Error getting discovery status: {}", e.getMessage());
            return Result.error("Failed to get discovery status: " + e.getMessage());
        }
    }

    @GetMapping("/stats")
    public Result<EndAgentStatsResult> getEndAgentStats() {
        log.info("Get end agent stats requested");

        try {
            EndAgentStatsResult result = new EndAgentStatsResult();
            result.setTotalAgents(endAgents.size());
            result.setStatusSummary(calculateStatusSummary());
            result.setTypeSummary(calculateTypeSummary());
            result.setOnlineRate(calculateOnlineRate());
            result.setLastDiscoveryTime(lastDiscoveryTime);

            if (nexusManager != null) {
                try {
                    Map<String, Map<String, Object>> nodes = nexusManager.getNetworkNodes();
                    result.setNetworkNodes(nodes);
                } catch (Exception sdkEx) {
                    log.warn("Failed to get network nodes from SDK: {}", sdkEx.getMessage());
                }
            }

            return Result.success("End agent stats retrieved successfully", result);
        } catch (Exception e) {
            log.error("Error getting end agent stats: {}", e.getMessage());
            return Result.error("Failed to get end agent stats: " + e.getMessage());
        }
    }

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

    private Map<String, Integer> calculateTypeSummary() {
        Map<String, Integer> typeSummary = new HashMap<>();

        for (EndAgent agent : endAgents.values()) {
            String type = agent.getType();
            typeSummary.put(type, typeSummary.getOrDefault(type, 0) + 1);
        }

        return typeSummary;
    }

    private double calculateOnlineRate() {
        if (endAgents.isEmpty()) {
            return 0;
        }
        int activeAgents = (int) endAgents.values().stream().filter(agent -> "active".equals(agent.getStatus())).count();
        return (double) activeAgents / endAgents.size() * 100;
    }

    private List<Map<String, Object>> generateAgentHistory(String agentId) {
        List<Map<String, Object>> history = new ArrayList<>();
        long now = System.currentTimeMillis();

        for (int i = 0; i < 24; i++) {
            long timestamp = now - (i * 3600000);
            Map<String, Object> dataPoint = new HashMap<>();
            dataPoint.put("timestamp", timestamp);
            dataPoint.put("status", i % 10 == 0 ? "degraded" : "active");
            dataPoint.put("description", i % 10 == 0 ? "性能下降" : "在线");
            history.add(dataPoint);
        }

        return history;
    }

    private void discoverAgentsAsync() {
        new Thread(() -> {
            try {
                log.info("End agent discovery started");

                if (nexusManager != null) {
                    try {
                        Map<String, Map<String, Object>> nodes = nexusManager.getNetworkNodes();
                        log.info("Retrieved network nodes from SDK: {} nodes", nodes.size());
                    } catch (Exception sdkEx) {
                        log.warn("Failed to get network nodes from SDK: {}", sdkEx.getMessage());
                    }
                }

                Thread.sleep(3000);

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

                endAgents.put(newAgentId, newAgent);

                for (EndAgent agent : endAgents.values()) {
                    if ("pending".equals(agent.getStatus())) {
                        agent.setStatus("active");
                        agent.setDescription("终端已激活");
                        agent.setLastUpdated(System.currentTimeMillis());
                    }
                }

                lastDiscoveryTime = System.currentTimeMillis();
                log.info("End agent discovery completed successfully");

            } catch (InterruptedException e) {
                log.error("End agent discovery interrupted: {}", e.getMessage());
            } finally {
                discoveryInProgress = false;
            }
        }).start();
    }

    private void activateAgentAsync(String agentId) {
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                EndAgent agent = endAgents.get(agentId);
                if (agent != null) {
                    agent.setStatus("active");
                    agent.setDescription("终端激活成功");
                    agent.setLastUpdated(System.currentTimeMillis());

                    if (nexusManager != null) {
                        try {
                            Map<String, Map<String, Object>> nodes = nexusManager.getNetworkNodes();
                            if (nodes.containsKey(agentId)) {
                                Map<String, Object> nodeInfo = nodes.get(agentId);
                                nodeInfo.put("status", "active");
                                nodeInfo.put("description", "终端激活成功");
                            }
                        } catch (Exception sdkEx) {
                            log.warn("Failed to update end agent status in SDK: {}", sdkEx.getMessage());
                        }
                    }
                }
            } catch (InterruptedException e) {
                log.error("End agent activation simulation interrupted: {}", e.getMessage());
            }
        }).start();
    }


}

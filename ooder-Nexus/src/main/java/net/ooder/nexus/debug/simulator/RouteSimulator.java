package net.ooder.nexus.debug.simulator;

import net.ooder.nexus.common.constant.ProtocolConstants;
import net.ooder.nexus.debug.model.ExecutionResult;
import net.ooder.nexus.debug.model.ScenarioStep;
import net.ooder.nexus.debug.model.Simulator;
import net.ooder.nexus.debug.model.SimulatorType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 路由节点模拟器实现
 */
public class RouteSimulator extends AbstractSimulator {

    private final Map<String, RouteEntry> routeTable = new ConcurrentHashMap<>();
    private final Map<String, RouteNode> routeNodes = new ConcurrentHashMap<>();

    public RouteSimulator() {
        this.type = SimulatorType.ROUTE_NODE;
        this.protocolType = ProtocolConstants.PROTOCOL_TYPE_ROUTE;
    }

    @Override
    public void initialize(Simulator config) {
        super.initialize(config);
        log("INFO", "Route Simulator initialized");
    }

    @Override
    protected Map<String, Object> doExecute(String action, ScenarioStep step) throws Exception {
        switch (action) {
            case ProtocolConstants.ROUTE_REGISTER:
                return handleRegister(step);
            case ProtocolConstants.ROUTE_DEREGISTER:
                return handleDeregister(step);
            case ProtocolConstants.ROUTE_UPDATE:
                return handleUpdate(step);
            case ProtocolConstants.ROUTE_QUERY:
                return handleQuery(step);
            case ProtocolConstants.ROUTE_STATUS:
                return handleStatus(step);
            case ProtocolConstants.ROUTE_HEARTBEAT:
                return handleHeartbeat(step);
            default:
                throw new IllegalArgumentException("Unknown action: " + action);
        }
    }

    private Map<String, Object> handleRegister(ScenarioStep step) {
        Map<String, Object> payload = getPayload(step);
        String nodeId = (String) payload.getOrDefault("nodeId", generateNodeId());

        RouteNode node = new RouteNode();
        node.nodeId = nodeId;
        node.nodeName = (String) payload.getOrDefault("nodeName", "Route Node " + nodeId);
        node.registerTime = System.currentTimeMillis();
        node.status = "ONLINE";

        routeNodes.put(nodeId, node);

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "Success");
        response.put("nodeId", nodeId);
        response.put("status", "REGISTERED");

        log("INFO", "Route node registered: " + nodeId);

        return response;
    }

    private Map<String, Object> handleDeregister(ScenarioStep step) {
        Map<String, Object> payload = getPayload(step);
        String nodeId = (String) payload.get("nodeId");

        routeNodes.remove(nodeId);
        routeTable.entrySet().removeIf(entry -> nodeId.equals(entry.getValue().nextHop));

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "Success");
        response.put("nodeId", nodeId);
        response.put("status", "DEREGISTERED");

        log("INFO", "Route node deregistered: " + nodeId);

        return response;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> handleUpdate(ScenarioStep step) {
        Map<String, Object> payload = getPayload(step);
        String nodeId = (String) payload.getOrDefault("nodeId", generateNodeId());

        List<Map<String, Object>> routes = (List<Map<String, Object>>) payload.get("routes");

        if (routes != null) {
            for (Map<String, Object> route : routes) {
                String destination = (String) route.get("destination");
                String nextHop = (String) route.get("nextHop");
                Integer metric = (Integer) route.getOrDefault("metric", 1);

                RouteEntry entry = new RouteEntry();
                entry.destination = destination;
                entry.nextHop = nextHop;
                entry.metric = metric;
                entry.updateTime = System.currentTimeMillis();

                routeTable.put(destination, entry);
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "Success");
        response.put("nodeId", nodeId);
        response.put("routesUpdated", routes != null ? routes.size() : 0);

        log("INFO", "Route table updated for node: " + nodeId);

        return response;
    }

    private Map<String, Object> handleQuery(ScenarioStep step) {
        Map<String, Object> payload = getPayload(step);
        String targetId = (String) payload.get("targetId");

        Map<String, Object> response = new HashMap<>();

        RouteEntry entry = routeTable.get(targetId);
        if (entry != null) {
            response.put("found", true);
            response.put("targetId", targetId);
            response.put("nextHop", entry.nextHop);
            response.put("metric", entry.metric);
        } else if (routeNodes.containsKey(targetId)) {
            response.put("found", true);
            response.put("targetId", targetId);
            response.put("nextHop", targetId);
            response.put("metric", 0);
            response.put("direct", true);
        } else {
            response.put("found", false);
            response.put("targetId", targetId);
        }

        response.put("code", 200);
        response.put("message", "Success");

        return response;
    }

    private Map<String, Object> handleStatus(ScenarioStep step) {
        Map<String, Object> payload = getPayload(step);
        String nodeId = (String) payload.get("nodeId");

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "Success");

        if (nodeId != null) {
            RouteNode node = routeNodes.get(nodeId);
            if (node != null) {
                response.put("nodeId", nodeId);
                response.put("status", node.status);
                response.put("routeCount", countRoutes(nodeId));
            } else {
                response.put("error", "Node not found");
            }
        } else {
            response.put("totalNodes", routeNodes.size());
            response.put("routeTableSize", routeTable.size());
        }

        return response;
    }

    private Map<String, Object> handleHeartbeat(ScenarioStep step) {
        Map<String, Object> payload = getPayload(step);
        String nodeId = (String) payload.getOrDefault("nodeId", generateNodeId());

        RouteNode node = routeNodes.get(nodeId);
        if (node != null) {
            node.lastHeartbeat = System.currentTimeMillis();
            node.status = "ONLINE";
        }

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "Success");
        response.put("nodeId", nodeId);
        response.put("status", "ALIVE");
        response.put("timestamp", System.currentTimeMillis());

        return response;
    }

    private Map<String, Object> getPayload(ScenarioStep step) {
        if (step.getRequest() != null && step.getRequest().containsKey("payload")) {
            @SuppressWarnings("unchecked")
            Map<String, Object> payload = (Map<String, Object>) step.getRequest().get("payload");
            return payload;
        }
        return new HashMap<>();
    }

    private int countRoutes(String nodeId) {
        return (int) routeTable.values().stream()
                .filter(e -> nodeId.equals(e.nextHop))
                .count();
    }

    @Override
    public ExecutionResult executeCommand(String commandType, Map<String, Object> payload) {
        ExecutionResult result = new ExecutionResult();
        result.setExecutionId(generateExecutionId());
        result.setSimulatorId(simulatorId);
        result.setStartTime(System.currentTimeMillis());

        try {
            ScenarioStep step = new ScenarioStep();
            step.setAction(commandType);
            Map<String, Object> request = new HashMap<>();
            request.put("payload", payload);
            step.setRequest(request);

            Map<String, Object> response = doExecute(commandType, step);
            result.setStatus("COMPLETED");
            result.setResponse(response);
        } catch (Exception e) {
            result.setStatus("FAILED");
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", e.getMessage());
            result.setResponse(errorMap);
        }

        result.setEndTime(System.currentTimeMillis());
        return result;
    }

    private static class RouteEntry {
        String destination;
        String nextHop;
        int metric;
        long updateTime;
    }

    private static class RouteNode {
        String nodeId;
        String nodeName;
        String status;
        long registerTime;
        long lastHeartbeat;
    }
}

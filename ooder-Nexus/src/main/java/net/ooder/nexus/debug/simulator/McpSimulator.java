package net.ooder.nexus.debug.simulator;

import net.ooder.nexus.common.constant.ProtocolConstants;
import net.ooder.nexus.debug.model.ExecutionResult;
import net.ooder.nexus.debug.model.ScenarioStep;
import net.ooder.nexus.debug.model.Simulator;
import net.ooder.nexus.debug.model.SimulatorType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MCP节点模拟器实现
 */
public class McpSimulator extends AbstractSimulator {

    private final Map<String, Map<String, Object>> registeredNodes = new ConcurrentHashMap<>();
    private String currentNodeId;

    public McpSimulator() {
        this.type = SimulatorType.MCP_NODE;
        this.protocolType = ProtocolConstants.PROTOCOL_TYPE_MCP;
    }

    @Override
    public void initialize(Simulator config) {
        super.initialize(config);
        currentNodeId = generateNodeId();
        log("INFO", "MCP Simulator initialized with node ID: " + currentNodeId);
    }

    @Override
    protected Map<String, Object> doExecute(String action, ScenarioStep step) throws Exception {
        switch (action) {
            case ProtocolConstants.MCP_REGISTER:
                return handleRegister(step);
            case ProtocolConstants.MCP_DEREGISTER:
                return handleDeregister(step);
            case ProtocolConstants.MCP_HEARTBEAT:
                return handleHeartbeat(step);
            case ProtocolConstants.MCP_STATUS:
                return handleStatus(step);
            case ProtocolConstants.MCP_DISCOVER:
                return handleDiscover(step);
            case ProtocolConstants.MCP_CONFIG:
                return handleConfig(step);
            default:
                throw new IllegalArgumentException("Unknown action: " + action);
        }
    }

    private Map<String, Object> handleRegister(ScenarioStep step) {
        Map<String, Object> payload = step.getRequest() != null ?
                (Map<String, Object>) step.getRequest().get("payload") : new HashMap<>();

        String nodeId = (String) payload.getOrDefault("nodeId", currentNodeId);
        String nodeName = (String) payload.getOrDefault("nodeName", "MCP Node " + nodeId);

        Map<String, Object> nodeInfo = new HashMap<>();
        nodeInfo.put("nodeId", nodeId);
        nodeInfo.put("nodeName", nodeName);
        nodeInfo.put("status", "REGISTERED");
        nodeInfo.put("registerTime", System.currentTimeMillis());

        registeredNodes.put(nodeId, nodeInfo);
        currentNodeId = nodeId;

        log("INFO", "Node registered: " + nodeId);

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "Success");
        response.put("nodeId", nodeId);
        response.put("status", "REGISTERED");

        return response;
    }

    private Map<String, Object> handleDeregister(ScenarioStep step) {
        Map<String, Object> payload = step.getRequest() != null ?
                (Map<String, Object>) step.getRequest().get("payload") : new HashMap<>();

        String nodeId = (String) payload.get("nodeId");
        if (nodeId == null) {
            nodeId = currentNodeId;
        }

        registeredNodes.remove(nodeId);

        log("INFO", "Node deregistered: " + nodeId);

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "Success");
        response.put("nodeId", nodeId);
        response.put("status", "DEREGISTERED");

        return response;
    }

    private Map<String, Object> handleHeartbeat(ScenarioStep step) {
        Map<String, Object> payload = step.getRequest() != null ?
                (Map<String, Object>) step.getRequest().get("payload") : new HashMap<>();

        String nodeId = (String) payload.getOrDefault("nodeId", currentNodeId);

        Map<String, Object> nodeInfo = registeredNodes.get(nodeId);
        if (nodeInfo != null) {
            nodeInfo.put("lastHeartbeat", System.currentTimeMillis());
            nodeInfo.put("status", "ONLINE");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "Success");
        response.put("nodeId", nodeId);
        response.put("status", "ALIVE");
        response.put("timestamp", System.currentTimeMillis());

        return response;
    }

    private Map<String, Object> handleStatus(ScenarioStep step) {
        Map<String, Object> payload = step.getRequest() != null ?
                (Map<String, Object>) step.getRequest().get("payload") : new HashMap<>();

        String nodeId = (String) payload.getOrDefault("nodeId", currentNodeId);

        Map<String, Object> nodeInfo = registeredNodes.get(nodeId);
        if (nodeInfo == null) {
            nodeInfo = new HashMap<>();
            nodeInfo.put("nodeId", nodeId);
            nodeInfo.put("status", "UNKNOWN");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "Success");
        response.put("nodeId", nodeId);
        response.put("status", nodeInfo.getOrDefault("status", "ONLINE"));
        response.put("totalNodes", registeredNodes.size());
        response.put("onlineNodes", countOnlineNodes());

        return response;
    }

    private Map<String, Object> handleDiscover(ScenarioStep step) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "Success");
        response.put("discoveredNodes", registeredNodes.size());
        response.put("nodes", registeredNodes.keySet());

        return response;
    }

    private Map<String, Object> handleConfig(ScenarioStep step) {
        Map<String, Object> payload = step.getRequest() != null ?
                (Map<String, Object>) step.getRequest().get("payload") : new HashMap<>();

        String nodeId = (String) payload.getOrDefault("nodeId", currentNodeId);
        Map<String, Object> config = (Map<String, Object>) payload.get("config");

        if (config != null) {
            Map<String, Object> nodeInfo = registeredNodes.get(nodeId);
            if (nodeInfo != null) {
                nodeInfo.put("config", config);
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "Success");
        response.put("nodeId", nodeId);
        response.put("configApplied", config != null);

        return response;
    }

    private int countOnlineNodes() {
        return (int) registeredNodes.values().stream()
                .filter(n -> "ONLINE".equals(n.get("status")) || "REGISTERED".equals(n.get("status")))
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
}

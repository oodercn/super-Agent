package net.ooder.nexus.core.protocol.adapter;

import net.ooder.nexus.core.protocol.model.CommandPacket;
import net.ooder.nexus.core.protocol.model.CommandResult;
import net.ooder.nexus.infrastructure.management.NexusManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * End Protocol Adapter
 * Handles End (Terminal Protocol) related commands
 */
@Component
public class EndProtocolAdapter extends AbstractProtocolAdapter {

    public static final String PROTOCOL_TYPE = "END";

    private final Map<String, EndNodeInfo> endNodes = new ConcurrentHashMap<>();
    private final Map<String, List<Map<String, Object>>> capabilityRegistry = new ConcurrentHashMap<>();

    @Autowired
    private NexusManager nexusManager;

    public EndProtocolAdapter() {
        super(PROTOCOL_TYPE);
        setDescription("End Protocol Adapter - Handles end device registration, capability reporting");
    }

    @PostConstruct
    public void postConstruct() {
        logger.info("EndProtocolAdapter constructed");
    }

    @Override
    protected void initializeSupportedCommands() {
        addSupportedCommand("END_REGISTER");
        addSupportedCommand("END_DEREGISTER");
        addSupportedCommand("END_CAPABILITY");
        addSupportedCommand("END_STATUS");
        addSupportedCommand("END_COMMAND");
        addSupportedCommand("END_RESULT");
        addSupportedCommand("END_HEARTBEAT");
    }

    @Override
    protected void doInitialize() {
        logger.info("End adapter initializing with {} supported commands", getSupportedCommands().size());
    }

    @Override
    protected void doDestroy() {
        endNodes.clear();
        capabilityRegistry.clear();
        logger.info("End adapter destroyed");
    }

    @Override
    protected boolean doValidateCommand(CommandPacket packet) {
        String commandType = packet.getCommandType();
        Map<String, Object> payload = packet.getPayload();

        switch (commandType) {
            case "END_REGISTER":
                return validateRegisterCommand(payload);
            case "END_HEARTBEAT":
                return validateHeartbeatCommand(payload);
            case "END_COMMAND":
                return validateCommandPayload(payload);
            default:
                return true;
        }
    }

    private boolean validateRegisterCommand(Map<String, Object> payload) {
        if (payload == null) {
            logger.warn("END_REGISTER payload is null");
            return false;
        }
        Object nodeId = payload.get("nodeId");
        if (nodeId == null || nodeId.toString().isEmpty()) {
            logger.warn("END_REGISTER missing nodeId");
            return false;
        }
        return true;
    }

    private boolean validateHeartbeatCommand(Map<String, Object> payload) {
        if (payload == null) {
            logger.warn("END_HEARTBEAT payload is null");
            return false;
        }
        Object nodeId = payload.get("nodeId");
        if (nodeId == null || nodeId.toString().isEmpty()) {
            logger.warn("END_HEARTBEAT missing nodeId");
            return false;
        }
        return true;
    }

    private boolean validateCommandPayload(Map<String, Object> payload) {
        if (payload == null) {
            logger.warn("END_COMMAND payload is null");
            return false;
        }
        Object nodeId = payload.get("nodeId");
        if (nodeId == null || nodeId.toString().isEmpty()) {
            logger.warn("END_COMMAND missing nodeId");
            return false;
        }
        Object command = payload.get("command");
        if (command == null) {
            logger.warn("END_COMMAND missing command");
            return false;
        }
        return true;
    }

    @Override
    protected CommandResult doHandleCommand(CommandPacket packet) {
        String commandType = packet.getCommandType();
        String commandId = packet.getHeader().getCommandId();
        Map<String, Object> payload = packet.getPayload();

        switch (commandType) {
            case "END_REGISTER":
                return handleEndRegister(commandId, payload);
            case "END_DEREGISTER":
                return handleEndDeregister(commandId, payload);
            case "END_CAPABILITY":
                return handleEndCapability(commandId, payload);
            case "END_STATUS":
                return handleEndStatus(commandId, payload);
            case "END_COMMAND":
                return handleEndCommand(commandId, payload);
            case "END_RESULT":
                return handleEndResult(commandId, payload);
            case "END_HEARTBEAT":
                return handleEndHeartbeat(commandId, payload);
            default:
                return CommandResult.error(commandId, 400, "Unknown command type: " + commandType);
        }
    }

    private CommandResult handleEndRegister(String commandId, Map<String, Object> payload) {
        String nodeId = payload.get("nodeId").toString();
        String nodeName = payload.get("nodeName") != null ? payload.get("nodeName").toString() : nodeId;
        String deviceType = payload.get("deviceType") != null ? payload.get("deviceType").toString() : "UNKNOWN";

        EndNodeInfo nodeInfo = new EndNodeInfo();
        nodeInfo.setNodeId(nodeId);
        nodeInfo.setNodeName(nodeName);
        nodeInfo.setDeviceType(deviceType);
        nodeInfo.setRegisterTime(System.currentTimeMillis());
        nodeInfo.setLastHeartbeatTime(System.currentTimeMillis());
        nodeInfo.setStatus("ONLINE");
        nodeInfo.setProperties((Map<String, Object>) payload.get("properties"));

        endNodes.put(nodeId, nodeInfo);

        logger.info("End node registered: {} ({}, type: {})", nodeId, nodeName, deviceType);

        if (nexusManager != null) {
            nexusManager.registerNetworkNode(nodeId, nodeInfo.toMap());
        }

        Map<String, Object> resultData = new HashMap<>();
        resultData.put("nodeId", nodeId);
        resultData.put("status", "REGISTERED");
        resultData.put("timestamp", System.currentTimeMillis());
        return CommandResult.success(commandId, resultData);
    }

    private CommandResult handleEndDeregister(String commandId, Map<String, Object> payload) {
        String nodeId = payload.get("nodeId").toString();

        EndNodeInfo removed = endNodes.remove(nodeId);
        if (removed != null) {
            logger.info("End node deregistered: {}", nodeId);

            capabilityRegistry.remove(nodeId);

            if (nexusManager != null) {
                nexusManager.removeNetworkNode(nodeId);
            }

            Map<String, Object> resultData = new HashMap<>();
            resultData.put("nodeId", nodeId);
            resultData.put("status", "DEREGISTERED");
            return CommandResult.success(commandId, resultData);
        } else {
            return CommandResult.error(commandId, 404, "Node not found: " + nodeId);
        }
    }

    private CommandResult handleEndCapability(String commandId, Map<String, Object> payload) {
        String nodeId = payload.get("nodeId").toString();

        if (!endNodes.containsKey(nodeId)) {
            return CommandResult.error(commandId, 404, "End node not registered: " + nodeId);
        }

        List<Map<String, Object>> capabilities = (List<Map<String, Object>>) payload.get("capabilities");
        if (capabilities != null) {
            capabilityRegistry.put(nodeId, capabilities);
            logger.info("Capabilities registered for node: {}, count: {}", nodeId, capabilities.size());
        }

        Map<String, Object> resultData = new HashMap<>();
        resultData.put("nodeId", nodeId);
        resultData.put("capabilitiesRegistered", capabilities != null ? capabilities.size() : 0);
        resultData.put("timestamp", System.currentTimeMillis());
        return CommandResult.success(commandId, resultData);
    }

    private CommandResult handleEndStatus(String commandId, Map<String, Object> payload) {
        if (payload != null && payload.get("nodeId") != null) {
            String nodeId = payload.get("nodeId").toString();
            EndNodeInfo nodeInfo = endNodes.get(nodeId);
            if (nodeInfo == null) {
                return CommandResult.error(commandId, 404, "Node not found: " + nodeId);
            }

            Map<String, Object> status = (Map<String, Object>) payload.get("status");
            if (status != null) {
                nodeInfo.setStatus((String) status.get("state"));
                nodeInfo.setProperties((Map<String, Object>) status.get("properties"));
            }

            Map<String, Object> resultData = new HashMap<>();
            resultData.put("nodeId", nodeId);
            resultData.put("status", nodeInfo.getStatus());
            resultData.put("deviceType", nodeInfo.getDeviceType());
            return CommandResult.success(commandId, resultData);
        } else {
            long onlineCount = endNodes.values().stream()
                    .filter(n -> "ONLINE".equals(n.getStatus()))
                    .count();

            Map<String, Object> resultData = new HashMap<>();
            resultData.put("totalNodes", endNodes.size());
            resultData.put("onlineNodes", onlineCount);
            resultData.put("nodes", new ArrayList<>(endNodes.keySet()));
            return CommandResult.success(commandId, resultData);
        }
    }

    private CommandResult handleEndCommand(String commandId, Map<String, Object> payload) {
        String nodeId = payload.get("nodeId").toString();
        String command = payload.get("command").toString();
        Map<String, Object> params = (Map<String, Object>) payload.get("params");

        if (!endNodes.containsKey(nodeId)) {
            return CommandResult.error(commandId, 404, "End node not found: " + nodeId);
        }

        logger.info("Command sent to end node: {}, command: {}", nodeId, command);

        Map<String, Object> resultData = new HashMap<>();
        resultData.put("nodeId", nodeId);
        resultData.put("command", command);
        resultData.put("status", "SENT");
        resultData.put("timestamp", System.currentTimeMillis());
        return CommandResult.success(commandId, resultData);
    }

    private CommandResult handleEndResult(String commandId, Map<String, Object> payload) {
        String nodeId = payload.get("nodeId").toString();
        String originalCommandId = payload.get("originalCommandId") != null ?
                payload.get("originalCommandId").toString() : null;
        Boolean success = (Boolean) payload.get("success");
        Map<String, Object> result = (Map<String, Object>) payload.get("result");

        logger.info("Command result from end node: {}, success: {}", nodeId, success);

        Map<String, Object> resultData = new HashMap<>();
        resultData.put("nodeId", nodeId);
        resultData.put("originalCommandId", originalCommandId);
        resultData.put("received", true);
        resultData.put("timestamp", System.currentTimeMillis());
        return CommandResult.success(commandId, resultData);
    }

    private CommandResult handleEndHeartbeat(String commandId, Map<String, Object> payload) {
        String nodeId = payload.get("nodeId").toString();

        EndNodeInfo nodeInfo = endNodes.get(nodeId);
        if (nodeInfo == null) {
            return CommandResult.error(commandId, 404, "Node not registered: " + nodeId);
        }

        nodeInfo.setLastHeartbeatTime(System.currentTimeMillis());
        nodeInfo.setStatus("ONLINE");

        if (nexusManager != null) {
            nexusManager.registerNetworkNode(nodeId, nodeInfo.toMap());
        }

        Map<String, Object> resultData = new HashMap<>();
        resultData.put("nodeId", nodeId);
        resultData.put("status", "HEARTBEAT_OK");
        resultData.put("timestamp", System.currentTimeMillis());
        return CommandResult.success(commandId, resultData);
    }

    public EndNodeInfo getEndNode(String nodeId) {
        return endNodes.get(nodeId);
    }

    public Map<String, EndNodeInfo> getAllEndNodes() {
        return new ConcurrentHashMap<>(endNodes);
    }

    public List<Map<String, Object>> getNodeCapabilities(String nodeId) {
        return capabilityRegistry.get(nodeId);
    }

    public static class EndNodeInfo {
        private String nodeId;
        private String nodeName;
        private String deviceType;
        private long registerTime;
        private long lastHeartbeatTime;
        private String status;
        private Map<String, Object> properties;

        public Map<String, Object> toMap() {
            Map<String, Object> map = new ConcurrentHashMap<>();
            map.put("nodeId", nodeId);
            map.put("nodeName", nodeName);
            map.put("deviceType", deviceType);
            map.put("registerTime", registerTime);
            map.put("lastHeartbeatTime", lastHeartbeatTime);
            map.put("status", status);
            map.put("properties", properties);
            return map;
        }

        public String getNodeId() { return nodeId; }
        public void setNodeId(String nodeId) { this.nodeId = nodeId; }
        public String getNodeName() { return nodeName; }
        public void setNodeName(String nodeName) { this.nodeName = nodeName; }
        public String getDeviceType() { return deviceType; }
        public void setDeviceType(String deviceType) { this.deviceType = deviceType; }
        public long getRegisterTime() { return registerTime; }
        public void setRegisterTime(long registerTime) { this.registerTime = registerTime; }
        public long getLastHeartbeatTime() { return lastHeartbeatTime; }
        public void setLastHeartbeatTime(long lastHeartbeatTime) { this.lastHeartbeatTime = lastHeartbeatTime; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public Map<String, Object> getProperties() { return properties; }
        public void setProperties(Map<String, Object> properties) { this.properties = properties; }
    }
}

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
 * Route Protocol Adapter
 * Handles Route (Routing Protocol) related commands
 */
@Component
public class RouteProtocolAdapter extends AbstractProtocolAdapter {

    public static final String PROTOCOL_TYPE = "ROUTE";

    private final Map<String, RouteNodeInfo> routeNodes = new ConcurrentHashMap<>();
    private final Map<String, RouteEntry> routeTable = new ConcurrentHashMap<>();

    @Autowired
    private NexusManager nexusManager;

    public RouteProtocolAdapter() {
        super(PROTOCOL_TYPE);
        setDescription("Route Protocol Adapter - Handles route node registration, routing table management");
    }

    @PostConstruct
    public void postConstruct() {
        logger.info("RouteProtocolAdapter constructed");
    }

    @Override
    protected void initializeSupportedCommands() {
        addSupportedCommand("ROUTE_REGISTER");
        addSupportedCommand("ROUTE_DEREGISTER");
        addSupportedCommand("ROUTE_UPDATE");
        addSupportedCommand("ROUTE_QUERY");
        addSupportedCommand("ROUTE_STATUS");
        addSupportedCommand("ROUTE_HEARTBEAT");
    }

    @Override
    protected void doInitialize() {
        logger.info("Route adapter initializing with {} supported commands", getSupportedCommands().size());
    }

    @Override
    protected void doDestroy() {
        routeNodes.clear();
        routeTable.clear();
        logger.info("Route adapter destroyed");
    }

    @Override
    protected boolean doValidateCommand(CommandPacket packet) {
        String commandType = packet.getCommandType();
        Map<String, Object> payload = packet.getPayload();

        switch (commandType) {
            case "ROUTE_REGISTER":
                return validateRegisterCommand(payload);
            case "ROUTE_HEARTBEAT":
                return validateHeartbeatCommand(payload);
            case "ROUTE_QUERY":
                return validateQueryCommand(payload);
            default:
                return true;
        }
    }

    private boolean validateRegisterCommand(Map<String, Object> payload) {
        if (payload == null) {
            logger.warn("ROUTE_REGISTER payload is null");
            return false;
        }
        Object nodeId = payload.get("nodeId");
        if (nodeId == null || nodeId.toString().isEmpty()) {
            logger.warn("ROUTE_REGISTER missing nodeId");
            return false;
        }
        return true;
    }

    private boolean validateHeartbeatCommand(Map<String, Object> payload) {
        if (payload == null) {
            logger.warn("ROUTE_HEARTBEAT payload is null");
            return false;
        }
        Object nodeId = payload.get("nodeId");
        if (nodeId == null || nodeId.toString().isEmpty()) {
            logger.warn("ROUTE_HEARTBEAT missing nodeId");
            return false;
        }
        return true;
    }

    private boolean validateQueryCommand(Map<String, Object> payload) {
        if (payload == null) {
            logger.warn("ROUTE_QUERY payload is null");
            return false;
        }
        Object targetId = payload.get("targetId");
        if (targetId == null || targetId.toString().isEmpty()) {
            logger.warn("ROUTE_QUERY missing targetId");
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
            case "ROUTE_REGISTER":
                return handleRouteRegister(commandId, payload);
            case "ROUTE_DEREGISTER":
                return handleRouteDeregister(commandId, payload);
            case "ROUTE_UPDATE":
                return handleRouteUpdate(commandId, payload);
            case "ROUTE_QUERY":
                return handleRouteQuery(commandId, payload);
            case "ROUTE_STATUS":
                return handleRouteStatus(commandId, payload);
            case "ROUTE_HEARTBEAT":
                return handleRouteHeartbeat(commandId, payload);
            default:
                return CommandResult.error(commandId, 400, "Unknown command type: " + commandType);
        }
    }

    private CommandResult handleRouteRegister(String commandId, Map<String, Object> payload) {
        String nodeId = payload.get("nodeId").toString();
        String nodeName = payload.get("nodeName") != null ? payload.get("nodeName").toString() : nodeId;

        RouteNodeInfo nodeInfo = new RouteNodeInfo();
        nodeInfo.setNodeId(nodeId);
        nodeInfo.setNodeName(nodeName);
        nodeInfo.setRegisterTime(System.currentTimeMillis());
        nodeInfo.setLastHeartbeatTime(System.currentTimeMillis());
        nodeInfo.setStatus("ONLINE");
        nodeInfo.setConnectedNodes((List<String>) payload.get("connectedNodes"));

        routeNodes.put(nodeId, nodeInfo);

        logger.info("Route node registered: {} ({})", nodeId, nodeName);

        if (nexusManager != null) {
            nexusManager.registerNetworkNode(nodeId, nodeInfo.toMap());
        }

        Map<String, Object> resultData = new HashMap<>();
        resultData.put("nodeId", nodeId);
        resultData.put("status", "REGISTERED");
        resultData.put("timestamp", System.currentTimeMillis());
        return CommandResult.success(commandId, resultData);
    }

    private CommandResult handleRouteDeregister(String commandId, Map<String, Object> payload) {
        String nodeId = payload.get("nodeId").toString();

        RouteNodeInfo removed = routeNodes.remove(nodeId);
        if (removed != null) {
            logger.info("Route node deregistered: {}", nodeId);

            routeTable.entrySet().removeIf(entry -> entry.getValue().getNextHop().equals(nodeId));

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

    private CommandResult handleRouteUpdate(String commandId, Map<String, Object> payload) {
        String nodeId = payload.get("nodeId").toString();

        if (!routeNodes.containsKey(nodeId)) {
            return CommandResult.error(commandId, 404, "Route node not registered: " + nodeId);
        }

        List<Map<String, Object>> routes = (List<Map<String, Object>>) payload.get("routes");
        if (routes != null) {
            for (Map<String, Object> route : routes) {
                String destination = (String) route.get("destination");
                String nextHop = (String) route.get("nextHop");
                Integer metric = (Integer) route.get("metric");

                RouteEntry entry = new RouteEntry();
                entry.setDestination(destination);
                entry.setNextHop(nextHop);
                entry.setMetric(metric != null ? metric : 1);
                entry.setUpdateTime(System.currentTimeMillis());

                routeTable.put(destination, entry);
            }
        }

        logger.info("Route table updated for node: {}, routes: {}", nodeId, routes != null ? routes.size() : 0);

        Map<String, Object> resultData = new HashMap<>();
        resultData.put("nodeId", nodeId);
        resultData.put("routesUpdated", routes != null ? routes.size() : 0);
        resultData.put("timestamp", System.currentTimeMillis());
        return CommandResult.success(commandId, resultData);
    }

    private CommandResult handleRouteQuery(String commandId, Map<String, Object> payload) {
        String targetId = payload.get("targetId").toString();
        String sourceId = payload.get("sourceId") != null ? payload.get("sourceId").toString() : null;

        RouteEntry entry = routeTable.get(targetId);

        if (entry != null) {
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("sourceId", sourceId);
            resultData.put("targetId", targetId);
            resultData.put("nextHop", entry.getNextHop());
            resultData.put("metric", entry.getMetric());
            resultData.put("found", true);
            return CommandResult.success(commandId, resultData);
        } else {
            if (routeNodes.containsKey(targetId)) {
                Map<String, Object> resultData = new HashMap<>();
                resultData.put("sourceId", sourceId);
                resultData.put("targetId", targetId);
                resultData.put("nextHop", targetId);
                resultData.put("metric", 0);
                resultData.put("found", true);
                resultData.put("direct", true);
                return CommandResult.success(commandId, resultData);
            }

            Map<String, Object> resultData = new HashMap<>();
            resultData.put("sourceId", sourceId);
            resultData.put("targetId", targetId);
            resultData.put("found", false);
            return CommandResult.success(commandId, resultData);
        }
    }

    private CommandResult handleRouteStatus(String commandId, Map<String, Object> payload) {
        if (payload != null && payload.get("nodeId") != null) {
            String nodeId = payload.get("nodeId").toString();
            RouteNodeInfo nodeInfo = routeNodes.get(nodeId);
            if (nodeInfo == null) {
                return CommandResult.error(commandId, 404, "Node not found: " + nodeId);
            }
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("nodeId", nodeId);
            resultData.put("status", nodeInfo.getStatus());
            resultData.put("connectedNodes", nodeInfo.getConnectedNodes());
            return CommandResult.success(commandId, resultData);
        } else {
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("totalNodes", routeNodes.size());
            resultData.put("routeTableSize", routeTable.size());
            resultData.put("nodes", new ArrayList<>(routeNodes.keySet()));
            return CommandResult.success(commandId, resultData);
        }
    }

    private CommandResult handleRouteHeartbeat(String commandId, Map<String, Object> payload) {
        String nodeId = payload.get("nodeId").toString();

        RouteNodeInfo nodeInfo = routeNodes.get(nodeId);
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

    public RouteNodeInfo getRouteNode(String nodeId) {
        return routeNodes.get(nodeId);
    }

    public Map<String, RouteNodeInfo> getAllRouteNodes() {
        return new ConcurrentHashMap<>(routeNodes);
    }

    public RouteEntry getRouteEntry(String destination) {
        return routeTable.get(destination);
    }

    public static class RouteNodeInfo {
        private String nodeId;
        private String nodeName;
        private long registerTime;
        private long lastHeartbeatTime;
        private String status;
        private List<String> connectedNodes;

        public Map<String, Object> toMap() {
            Map<String, Object> map = new ConcurrentHashMap<>();
            map.put("nodeId", nodeId);
            map.put("nodeName", nodeName);
            map.put("registerTime", registerTime);
            map.put("lastHeartbeatTime", lastHeartbeatTime);
            map.put("status", status);
            map.put("connectedNodes", connectedNodes);
            return map;
        }

        public String getNodeId() { return nodeId; }
        public void setNodeId(String nodeId) { this.nodeId = nodeId; }
        public String getNodeName() { return nodeName; }
        public void setNodeName(String nodeName) { this.nodeName = nodeName; }
        public long getRegisterTime() { return registerTime; }
        public void setRegisterTime(long registerTime) { this.registerTime = registerTime; }
        public long getLastHeartbeatTime() { return lastHeartbeatTime; }
        public void setLastHeartbeatTime(long lastHeartbeatTime) { this.lastHeartbeatTime = lastHeartbeatTime; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public List<String> getConnectedNodes() { return connectedNodes; }
        public void setConnectedNodes(List<String> connectedNodes) { this.connectedNodes = connectedNodes; }
    }

    public static class RouteEntry {
        private String destination;
        private String nextHop;
        private int metric;
        private long updateTime;

        public String getDestination() { return destination; }
        public void setDestination(String destination) { this.destination = destination; }
        public String getNextHop() { return nextHop; }
        public void setNextHop(String nextHop) { this.nextHop = nextHop; }
        public int getMetric() { return metric; }
        public void setMetric(int metric) { this.metric = metric; }
        public long getUpdateTime() { return updateTime; }
        public void setUpdateTime(long updateTime) { this.updateTime = updateTime; }
    }
}

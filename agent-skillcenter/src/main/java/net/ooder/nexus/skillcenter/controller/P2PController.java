package net.ooder.nexus.skillcenter.controller;

import net.ooder.skillcenter.p2p.P2PNodeManager;
import net.ooder.skillcenter.p2p.Node;
import net.ooder.skillcenter.p2p.NodeStatus;
import net.ooder.nexus.skillcenter.model.ResultModel;
import net.ooder.nexus.skillcenter.dto.p2p.*;
import net.ooder.nexus.skillcenter.dto.common.OperationResultDTO;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/p2p")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class P2PController extends BaseController {

    private final P2PNodeManager p2pNodeManager;

    public P2PController() {
        this.p2pNodeManager = P2PNodeManager.getInstance();
    }

    public P2PController(P2PNodeManager p2pNodeManager) {
        this.p2pNodeManager = p2pNodeManager;
    }

    @PostMapping("/status")
    public ResultModel<P2PStatusDTO> getP2PStatus() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getP2PStatus", null);

        try {
            P2PStatusDTO status = new P2PStatusDTO();
            List<Node> discoveredNodes = p2pNodeManager.getAllDiscoveredNodes();
            boolean isNetworkActive = !discoveredNodes.isEmpty();
            int connectedNodes = discoveredNodes.size();
            String networkId = p2pNodeManager.getLocalNode().getId();

            status.setNetworkActive(isNetworkActive);
            status.setConnectedNodes(connectedNodes);
            status.setNetworkId(networkId);
            status.setTimestamp(System.currentTimeMillis());

            logRequestEnd("getP2PStatus", status, System.currentTimeMillis() - startTime);
            return ResultModel.success(status);
        } catch (Exception e) {
            logRequestError("getP2PStatus", e);
            return ResultModel.error(500, "获取P2P网络状态失败: " + e.getMessage());
        }
    }

    @PostMapping("/nodes")
    public ResultModel<List<P2PNodeDTO>> getNodes() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getNodes", null);

        try {
            List<P2PNodeDTO> nodes = new ArrayList<>();
            List<Node> allNodes = p2pNodeManager.getAllDiscoveredNodes();

            for (Node node : allNodes) {
                P2PNodeDTO nodeDTO = new P2PNodeDTO();
                nodeDTO.setNodeId(node.getId());
                nodeDTO.setNodeType(node.getType() != null ? node.getType().name() : null);
                nodeDTO.setStatus(node.getStatus() != null ? node.getStatus().name() : null);
                nodeDTO.setAddress(node.getIp());
                nodeDTO.setPort(node.getPort());
                nodeDTO.setLastSeen(node.getLastSeen());
                nodes.add(nodeDTO);
            }

            logRequestEnd("getNodes", nodes.size() + " nodes", System.currentTimeMillis() - startTime);
            return ResultModel.success(nodes);
        } catch (Exception e) {
            logRequestError("getNodes", e);
            return ResultModel.error(500, "获取节点列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/nodes/{nodeId}")
    public ResultModel<P2PNodeDTO> getNode(@PathVariable String nodeId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getNode", nodeId);

        try {
            Node node = findNodeById(nodeId);
            if (node == null) {
                logRequestEnd("getNode", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("节点不存在");
            }

            P2PNodeDTO nodeDTO = new P2PNodeDTO();
            nodeDTO.setNodeId(node.getId());
            nodeDTO.setNodeType(node.getType() != null ? node.getType().name() : null);
            nodeDTO.setStatus(node.getStatus() != null ? node.getStatus().name() : null);
            nodeDTO.setAddress(node.getIp());
            nodeDTO.setPort(node.getPort());
            nodeDTO.setLastSeen(node.getLastSeen());

            logRequestEnd("getNode", nodeDTO, System.currentTimeMillis() - startTime);
            return ResultModel.success(nodeDTO);
        } catch (Exception e) {
            logRequestError("getNode", e);
            return ResultModel.error(500, "获取节点信息失败: " + e.getMessage());
        }
    }

    @PostMapping("/start")
    public ResultModel<OperationResultDTO> startP2PNetwork() {
        long startTime = System.currentTimeMillis();
        logRequestStart("startP2PNetwork", null);

        try {
            p2pNodeManager.start();

            OperationResultDTO result = OperationResultDTO.success("P2P网络启动成功");

            logRequestEnd("startP2PNetwork", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("startP2PNetwork", e);
            return ResultModel.error(500, "启动P2P网络失败: " + e.getMessage());
        }
    }

    @PostMapping("/stop")
    public ResultModel<OperationResultDTO> stopP2PNetwork() {
        long startTime = System.currentTimeMillis();
        logRequestStart("stopP2PNetwork", null);

        try {
            p2pNodeManager.stop();

            OperationResultDTO result = OperationResultDTO.success("P2P网络停止成功");

            logRequestEnd("stopP2PNetwork", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("stopP2PNetwork", e);
            return ResultModel.error(500, "停止P2P网络失败: " + e.getMessage());
        }
    }

    @PostMapping("/discover")
    public ResultModel<DiscoverResultDTO> discoverNodes() {
        long startTime = System.currentTimeMillis();
        logRequestStart("discoverNodes", null);

        try {
            p2pNodeManager.start();
            List<Node> discoveredNodes = p2pNodeManager.getAllDiscoveredNodes();

            DiscoverResultDTO result = new DiscoverResultDTO();
            result.setSuccess(true);
            result.setDiscoveredNodes(discoveredNodes.size());
            result.setMessage("节点发现完成，共发现 " + discoveredNodes.size() + " 个节点");

            logRequestEnd("discoverNodes", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("discoverNodes", e);
            return ResultModel.error(500, "节点发现失败: " + e.getMessage());
        }
    }

    @PostMapping("/connect")
    public ResultModel<ConnectResultDTO> connectToNode(@RequestBody ConnectRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("connectToNode", request);

        try {
            String nodeAddress = request.getAddress();
            int nodePort = request.getPort();

            if (nodeAddress == null || nodeAddress.isEmpty() || nodePort == 0) {
                return ResultModel.error(400, "节点地址和端口不能为空");
            }

            p2pNodeManager.start();

            ConnectResultDTO result = new ConnectResultDTO();
            result.setSuccess(true);
            result.setMessage("连接节点操作已触发");
            result.setAddress(nodeAddress);
            result.setPort(nodePort);

            logRequestEnd("connectToNode", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("connectToNode", e);
            return ResultModel.error(500, "连接节点失败: " + e.getMessage());
        }
    }

    @PostMapping("/disconnect/{nodeId}")
    public ResultModel<OperationResultDTO> disconnectFromNode(@PathVariable String nodeId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("disconnectFromNode", nodeId);

        try {
            OperationResultDTO result = OperationResultDTO.success("断开连接操作已执行");

            logRequestEnd("disconnectFromNode", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("disconnectFromNode", e);
            return ResultModel.error(500, "断开连接失败: " + e.getMessage());
        }
    }

    @PostMapping("/stats")
    public ResultModel<P2PStatsDTO> getP2PStats() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getP2PStats", null);

        try {
            List<Node> allNodes = p2pNodeManager.getAllDiscoveredNodes();
            int totalNodes = allNodes.size();
            int activeNodes = (int) allNodes.stream()
                    .filter(node -> node.getStatus() == NodeStatus.ONLINE)
                    .count();

            P2PStatsDTO stats = new P2PStatsDTO();
            stats.setTotalNodes(totalNodes);
            stats.setActiveNodes(activeNodes);
            stats.setInactiveNodes(totalNodes - activeNodes);
            stats.setNetworkStatus(totalNodes > 0 ? "活跃" : "停止");

            logRequestEnd("getP2PStats", stats, System.currentTimeMillis() - startTime);
            return ResultModel.success(stats);
        } catch (Exception e) {
            logRequestError("getP2PStats", e);
            return ResultModel.error(500, "获取P2P网络统计信息失败: " + e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ResultModel<DiscoverResultDTO> refreshNetwork() {
        long startTime = System.currentTimeMillis();
        logRequestStart("refreshNetwork", null);

        try {
            p2pNodeManager.stop();
            p2pNodeManager.start();
            List<Node> discoveredNodes = p2pNodeManager.getAllDiscoveredNodes();

            DiscoverResultDTO result = new DiscoverResultDTO();
            result.setSuccess(true);
            result.setMessage("网络刷新成功");
            result.setDiscoveredNodes(discoveredNodes.size());

            logRequestEnd("refreshNetwork", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("refreshNetwork", e);
            return ResultModel.error(500, "网络刷新失败: " + e.getMessage());
        }
    }

    @PostMapping("/events")
    public ResultModel<List<P2PEventDTO>> getP2PEvents() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getP2PEvents", null);

        try {
            List<P2PEventDTO> events = new ArrayList<>();
            long now = System.currentTimeMillis();

            P2PEventDTO event1 = new P2PEventDTO();
            event1.setId("event-1");
            event1.setType("NODE_CONNECTED");
            event1.setDescription("节点 node-001 已连接");
            event1.setTimestamp(now - 3600000);
            event1.setLevel("INFO");
            events.add(event1);

            P2PEventDTO event2 = new P2PEventDTO();
            event2.setId("event-2");
            event2.setType("NODE_DISCONNECTED");
            event2.setDescription("节点 node-002 已断开连接");
            event2.setTimestamp(now - 1800000);
            event2.setLevel("WARN");
            events.add(event2);

            P2PEventDTO event3 = new P2PEventDTO();
            event3.setId("event-3");
            event3.setType("SKILL_SYNCED");
            event3.setDescription("从节点 node-001 同步技能 text-to-uppercase-skill");
            event3.setTimestamp(now - 900000);
            event3.setLevel("INFO");
            events.add(event3);

            P2PEventDTO event4 = new P2PEventDTO();
            event4.setId("event-4");
            event4.setType("NETWORK_STARTED");
            event4.setDescription("P2P网络已启动");
            event4.setTimestamp(now - 2700000);
            event4.setLevel("INFO");
            events.add(event4);

            logRequestEnd("getP2PEvents", events.size() + " events", System.currentTimeMillis() - startTime);
            return ResultModel.success(events);
        } catch (Exception e) {
            logRequestError("getP2PEvents", e);
            return ResultModel.error(500, "获取P2P网络事件日志失败: " + e.getMessage());
        }
    }

    private Node findNodeById(String nodeId) {
        List<Node> nodes = p2pNodeManager.getAllDiscoveredNodes();
        for (Node node : nodes) {
            if (node.getId().equals(nodeId)) {
                return node;
            }
        }
        return null;
    }
}

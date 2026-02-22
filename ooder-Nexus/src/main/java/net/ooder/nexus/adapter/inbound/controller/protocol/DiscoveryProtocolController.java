package net.ooder.nexus.adapter.inbound.controller.protocol;

import net.ooder.config.ResultModel;
import net.ooder.sdk.southbound.protocol.DiscoveryProtocol;
import net.ooder.sdk.southbound.protocol.model.DiscoveryRequest;
import net.ooder.sdk.southbound.protocol.model.DiscoveryResult;
import net.ooder.sdk.southbound.protocol.model.DiscoveryType;
import net.ooder.sdk.southbound.protocol.model.PeerInfo;
import net.ooder.sdk.southbound.protocol.model.PeerType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/protocol/discovery")
public class DiscoveryProtocolController {

    private static final Logger log = LoggerFactory.getLogger(DiscoveryProtocolController.class);

    private final DiscoveryProtocol discoveryProtocol;

    @Autowired
    public DiscoveryProtocolController(@Autowired(required = false) DiscoveryProtocol discoveryProtocol) {
        this.discoveryProtocol = discoveryProtocol;
        log.info("DiscoveryProtocolController initialized: {}", 
            discoveryProtocol != null ? "SDK protocol available" : "using mock");
    }

    @PostMapping("/scan")
    @ResponseBody
    public ResultModel<DiscoveryResult> scan(@RequestBody(required = false) Map<String, Object> params) {
        log.info("Discovery scan requested: {}", params);
        ResultModel<DiscoveryResult> result = new ResultModel<>();

        try {
            if (discoveryProtocol == null) {
                result.setData(mockDiscoveryResult());
                result.setRequestStatus(200);
                result.setMessage("Mock discovery result (SDK not available)");
            } else {
                DiscoveryRequest request = new DiscoveryRequest();
                if (params != null) {
                    String typeStr = (String) params.getOrDefault("type", "ALL");
                    request.setType(DiscoveryType.valueOf(typeStr.toUpperCase()));
                    request.setTimeout((Integer) params.getOrDefault("timeout", 5000));
                }
                
                CompletableFuture<DiscoveryResult> future = discoveryProtocol.discover(request);
                DiscoveryResult discoveryResult = future.get();
                
                result.setData(discoveryResult);
                result.setRequestStatus(200);
                result.setMessage("Discovery completed");
            }
        } catch (Exception e) {
            log.error("Discovery scan failed", e);
            result.setRequestStatus(500);
            result.setMessage("Discovery failed: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/peers")
    @ResponseBody
    public ResultModel<List<PeerInfo>> listPeers() {
        log.info("List discovered peers requested");
        ResultModel<List<PeerInfo>> result = new ResultModel<>();

        try {
            if (discoveryProtocol == null) {
                result.setData(mockPeerList());
                result.setRequestStatus(200);
            } else {
                CompletableFuture<List<PeerInfo>> future = discoveryProtocol.discoverPeers();
                List<PeerInfo> peers = future.get();
                result.setData(peers);
                result.setRequestStatus(200);
            }
        } catch (Exception e) {
            log.error("List peers failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to list peers: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/mcp")
    @ResponseBody
    public ResultModel<PeerInfo> discoverMcp() {
        log.info("Discover MCP requested");
        ResultModel<PeerInfo> result = new ResultModel<>();

        try {
            if (discoveryProtocol == null) {
                result.setData(mockMcpPeer());
                result.setRequestStatus(200);
            } else {
                CompletableFuture<PeerInfo> future = discoveryProtocol.discoverMcp();
                PeerInfo mcp = future.get();
                result.setData(mcp);
                result.setRequestStatus(200);
            }
        } catch (Exception e) {
            log.error("Discover MCP failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to discover MCP: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/broadcast/start")
    @ResponseBody
    public ResultModel<Void> startBroadcast() {
        log.info("Start broadcast requested");
        ResultModel<Void> result = new ResultModel<>();

        try {
            if (discoveryProtocol != null) {
                discoveryProtocol.startBroadcast();
            }
            result.setRequestStatus(200);
            result.setMessage("Broadcast started");
        } catch (Exception e) {
            log.error("Start broadcast failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to start broadcast: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/broadcast/stop")
    @ResponseBody
    public ResultModel<Void> stopBroadcast() {
        log.info("Stop broadcast requested");
        ResultModel<Void> result = new ResultModel<>();

        try {
            if (discoveryProtocol != null) {
                discoveryProtocol.stopBroadcast();
            }
            result.setRequestStatus(200);
            result.setMessage("Broadcast stopped");
        } catch (Exception e) {
            log.error("Stop broadcast failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to stop broadcast: " + e.getMessage());
        }

        return result;
    }

    @GetMapping("/broadcast/status")
    @ResponseBody
    public ResultModel<Map<String, Object>> getBroadcastStatus() {
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        
        Map<String, Object> status = new HashMap<>();
        status.put("broadcasting", discoveryProtocol != null && discoveryProtocol.isBroadcasting());
        status.put("sdkAvailable", discoveryProtocol != null);
        
        result.setData(status);
        result.setRequestStatus(200);
        return result;
    }

    @GetMapping("/status")
    @ResponseBody
    public ResultModel<Map<String, Object>> getStatus() {
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        
        Map<String, Object> status = new HashMap<>();
        status.put("sdkAvailable", discoveryProtocol != null);
        status.put("broadcasting", discoveryProtocol != null && discoveryProtocol.isBroadcasting());
        status.put("protocolType", "DiscoveryProtocol");
        
        result.setData(status);
        result.setRequestStatus(200);
        return result;
    }

    private DiscoveryResult mockDiscoveryResult() {
        DiscoveryResult result = new DiscoveryResult();
        result.setSuccess(true);
        result.setPeers(mockPeerList());
        result.setMcp(mockMcpPeer());
        return result;
    }

    private List<PeerInfo> mockPeerList() {
        List<PeerInfo> peers = new ArrayList<>();
        
        PeerInfo peer1 = new PeerInfo();
        peer1.setPeerId("mock-peer-001");
        peer1.setPeerName("Mock Peer 1");
        peer1.setPeerType(PeerType.MCP_AGENT);
        peer1.setIpAddress("192.168.1.100");
        peer1.setPort(8080);
        peers.add(peer1);
        
        PeerInfo peer2 = new PeerInfo();
        peer2.setPeerId("mock-peer-002");
        peer2.setPeerName("Mock Peer 2");
        peer2.setPeerType(PeerType.ROUTE_AGENT);
        peer2.setIpAddress("192.168.1.101");
        peer2.setPort(8080);
        peers.add(peer2);
        
        return peers;
    }

    private PeerInfo mockMcpPeer() {
        PeerInfo mcp = new PeerInfo();
        mcp.setPeerId("mock-mcp-001");
        mcp.setPeerName("Mock MCP Agent");
        mcp.setPeerType(PeerType.MCP_AGENT);
        mcp.setIpAddress("192.168.1.100");
        mcp.setPort(8080);
        return mcp;
    }
}

package net.ooder.nexus.service.protocol;

import net.ooder.sdk.southbound.protocol.model.DiscoveryRequest;
import net.ooder.sdk.southbound.protocol.model.DiscoveryResult;
import net.ooder.sdk.southbound.protocol.model.PeerInfo;
import net.ooder.sdk.southbound.protocol.model.PeerType;
import net.ooder.sdk.southbound.protocol.DiscoveryProtocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class DiscoveryProtocolService {

    private static final Logger log = LoggerFactory.getLogger(DiscoveryProtocolService.class);

    private final DiscoveryProtocol discoveryProtocol;

    @Autowired
    public DiscoveryProtocolService(@Autowired(required = false) DiscoveryProtocol discoveryProtocol) {
        this.discoveryProtocol = discoveryProtocol;
        log.info("DiscoveryProtocolService initialized: {}", 
            discoveryProtocol != null ? "SDK protocol available" : "using mock");
    }

    public boolean isAvailable() {
        return discoveryProtocol != null;
    }

    public CompletableFuture<DiscoveryResult> discover(DiscoveryRequest request) {
        log.info("Discovering peers with request: {}", request);
        
        if (discoveryProtocol == null) {
            return CompletableFuture.completedFuture(mockDiscoveryResult());
        }
        
        return discoveryProtocol.discover(request);
    }

    public CompletableFuture<List<PeerInfo>> discoverPeers() {
        log.info("Discovering all peers");
        
        if (discoveryProtocol == null) {
            return CompletableFuture.completedFuture(mockPeerList());
        }
        
        return discoveryProtocol.discoverPeers();
    }

    public CompletableFuture<PeerInfo> discoverMcp() {
        log.info("Discovering MCP agent");
        
        if (discoveryProtocol == null) {
            return CompletableFuture.completedFuture(mockMcpPeer());
        }
        
        return discoveryProtocol.discoverMcp();
    }

    public void startBroadcast() {
        log.info("Starting discovery broadcast");
        if (discoveryProtocol != null) {
            discoveryProtocol.startBroadcast();
        }
    }

    public void stopBroadcast() {
        log.info("Stopping discovery broadcast");
        if (discoveryProtocol != null) {
            discoveryProtocol.stopBroadcast();
        }
    }

    public boolean isBroadcasting() {
        if (discoveryProtocol == null) {
            return false;
        }
        return discoveryProtocol.isBroadcasting();
    }

    private DiscoveryResult mockDiscoveryResult() {
        DiscoveryResult result = new DiscoveryResult();
        result.setPeers(mockPeerList());
        result.setSuccess(true);
        return result;
    }

    private List<PeerInfo> mockPeerList() {
        List<PeerInfo> peers = new ArrayList<PeerInfo>();
        
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

package net.ooder.nexus.service.south;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DiscoveryProtocol {

    CompletableFuture<DiscoveryResult> discover(DiscoveryRequest request);

    CompletableFuture<List<PeerInfo>> discoverPeers();

    CompletableFuture<PeerInfo> discoverMcp();

    void addDiscoveryListener(DiscoveryListener listener);

    void removeDiscoveryListener(DiscoveryListener listener);

    void startBroadcast();

    void stopBroadcast();

    boolean isBroadcasting();

    class DiscoveryRequest {
        private String requestId;
        private String type;
        private int timeout;
        private String targetNetwork;

        public String getRequestId() { return requestId; }
        public void setRequestId(String requestId) { this.requestId = requestId; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public int getTimeout() { return timeout; }
        public void setTimeout(int timeout) { this.timeout = timeout; }
        public String getTargetNetwork() { return targetNetwork; }
        public void setTargetNetwork(String targetNetwork) { this.targetNetwork = targetNetwork; }
    }

    class DiscoveryResult {
        private String requestId;
        private boolean success;
        private List<PeerInfo> peers;
        private PeerInfo mcp;
        private String errorMessage;

        public String getRequestId() { return requestId; }
        public void setRequestId(String requestId) { this.requestId = requestId; }
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public List<PeerInfo> getPeers() { return peers; }
        public void setPeers(List<PeerInfo> peers) { this.peers = peers; }
        public PeerInfo getMcp() { return mcp; }
        public void setMcp(PeerInfo mcp) { this.mcp = mcp; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    }

    class PeerInfo {
        private String peerId;
        private String peerName;
        private String peerType;
        private String ipAddress;
        private int port;
        private long lastSeen;

        public String getPeerId() { return peerId; }
        public void setPeerId(String peerId) { this.peerId = peerId; }
        public String getPeerName() { return peerName; }
        public void setPeerName(String peerName) { this.peerName = peerName; }
        public String getPeerType() { return peerType; }
        public void setPeerType(String peerType) { this.peerType = peerType; }
        public String getIpAddress() { return ipAddress; }
        public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }
        public long getLastSeen() { return lastSeen; }
        public void setLastSeen(long lastSeen) { this.lastSeen = lastSeen; }
    }

    interface DiscoveryListener {
        void onPeerDiscovered(PeerInfo peer);
        void onPeerLost(String peerId);
        void onMcpDiscovered(PeerInfo mcp);
        void onDiscoveryComplete(DiscoveryResult result);
    }
}

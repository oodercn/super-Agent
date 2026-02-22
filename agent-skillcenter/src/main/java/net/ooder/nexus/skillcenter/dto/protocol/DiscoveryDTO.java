package net.ooder.nexus.skillcenter.dto.protocol;

import java.util.List;

public class DiscoveryDTO {

    public static class DiscoveryRequestDTO {
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

    public static class DiscoveryResultDTO {
        private String requestId;
        private boolean success;
        private List<PeerDTO> peers;
        private PeerDTO mcp;
        private String errorMessage;

        public String getRequestId() { return requestId; }
        public void setRequestId(String requestId) { this.requestId = requestId; }
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public List<PeerDTO> getPeers() { return peers; }
        public void setPeers(List<PeerDTO> peers) { this.peers = peers; }
        public PeerDTO getMcp() { return mcp; }
        public void setMcp(PeerDTO mcp) { this.mcp = mcp; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    }

    public static class PeerDTO {
        private String peerId;
        private String peerName;
        private String peerType;
        private String ipAddress;
        private int port;
        private long lastSeen;
        private String domainId;

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
        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
    }
}

package net.ooder.nexus.service.south;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 发现协议适配器接口
 *
 * <p>提供网络发现能力，支持 UDP 广播发现局域网节点。</p>
 *
 * @author ooder Team
 * @version 2.0
 * @since SDK 0.7.2
 */
public interface DiscoveryProtocolAdapter {

    /**
     * 发现节点
     *
     * @param request 发现请求
     * @return 发现结果
     */
    CompletableFuture<DiscoveryResultDTO> discoverPeers(DiscoveryRequestDTO request);

    /**
     * 获取已发现的节点列表
     *
     * @return 节点列表
     */
    CompletableFuture<List<PeerDTO>> listDiscoveredPeers();

    /**
     * 发现 MCP 节点
     *
     * @return MCP 节点信息
     */
    CompletableFuture<PeerDTO> discoverMcp();

    /**
     * 添加发现事件监听器
     *
     * @param listener 监听器
     */
    void addDiscoveryListener(DiscoveryEventListener listener);

    /**
     * 移除发现事件监听器
     *
     * @param listener 监听器
     */
    void removeDiscoveryListener(DiscoveryEventListener listener);

    /**
     * 开始广播
     */
    void startBroadcast();

    /**
     * 停止广播
     */
    void stopBroadcast();

    /**
     * 检查是否正在广播
     *
     * @return 是否正在广播
     */
    boolean isBroadcasting();

    /**
     * 发现请求 DTO
     */
    class DiscoveryRequestDTO {
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

    /**
     * 发现结果 DTO
     */
    class DiscoveryResultDTO {
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

    /**
     * 对等节点 DTO
     */
    class PeerDTO {
        private String peerId;
        private String peerName;
        private String peerType;
        private String ipAddress;
        private int port;
        private long lastSeen;
        private String domainId;
        private boolean online;
        private java.util.Map<String, String> capabilities;

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
        public boolean isOnline() { return online; }
        public void setOnline(boolean online) { this.online = online; }
        public java.util.Map<String, String> getCapabilities() { return capabilities; }
        public void setCapabilities(java.util.Map<String, String> capabilities) { this.capabilities = capabilities; }
    }

    /**
     * 发现事件监听器
     */
    interface DiscoveryEventListener {
        void onPeerDiscovered(PeerDTO peer);
        void onPeerLost(String peerId);
        void onMcpDiscovered(PeerDTO mcp);
        void onDiscoveryComplete(DiscoveryResultDTO result);
    }
}

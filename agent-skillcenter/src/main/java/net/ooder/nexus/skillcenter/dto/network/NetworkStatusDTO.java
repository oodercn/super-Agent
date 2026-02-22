package net.ooder.nexus.skillcenter.dto.network;

public class NetworkStatusDTO {

    private String status;
    private String nodeId;
    private String nodeType;
    private boolean isOnline;
    private int connectedPeers;
    private String localAddress;
    private int localPort;
    private Long uptime;
    private Long timestamp;

    public NetworkStatusDTO() {}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public int getConnectedPeers() {
        return connectedPeers;
    }

    public void setConnectedPeers(int connectedPeers) {
        this.connectedPeers = connectedPeers;
    }

    public String getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public Long getUptime() {
        return uptime;
    }

    public void setUptime(Long uptime) {
        this.uptime = uptime;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}

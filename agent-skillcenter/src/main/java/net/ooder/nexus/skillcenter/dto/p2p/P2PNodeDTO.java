package net.ooder.nexus.skillcenter.dto.p2p;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class P2PNodeDTO extends BaseDTO {

    private String nodeId;
    private String nodeType;
    private String status;
    private String address;
    private int port;
    private Long lastSeen;

    public P2PNodeDTO() {}

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Long lastSeen) {
        this.lastSeen = lastSeen;
    }
}

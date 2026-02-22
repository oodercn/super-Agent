package net.ooder.nexus.skillcenter.dto.p2p;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class P2PStatusDTO extends BaseDTO {

    private boolean networkActive;
    private int connectedNodes;
    private String networkId;
    private Long timestamp;

    public P2PStatusDTO() {
        this.timestamp = System.currentTimeMillis();
    }

    public boolean isNetworkActive() {
        return networkActive;
    }

    public void setNetworkActive(boolean networkActive) {
        this.networkActive = networkActive;
    }

    public int getConnectedNodes() {
        return connectedNodes;
    }

    public void setConnectedNodes(int connectedNodes) {
        this.connectedNodes = connectedNodes;
    }

    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}

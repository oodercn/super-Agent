package net.ooder.nexus.skillcenter.dto.p2p;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class P2PStatsDTO extends BaseDTO {

    private int totalNodes;
    private int activeNodes;
    private int inactiveNodes;
    private String networkStatus;
    private Long timestamp;

    public P2PStatsDTO() {
        this.timestamp = System.currentTimeMillis();
    }

    public int getTotalNodes() {
        return totalNodes;
    }

    public void setTotalNodes(int totalNodes) {
        this.totalNodes = totalNodes;
    }

    public int getActiveNodes() {
        return activeNodes;
    }

    public void setActiveNodes(int activeNodes) {
        this.activeNodes = activeNodes;
    }

    public int getInactiveNodes() {
        return inactiveNodes;
    }

    public void setInactiveNodes(int inactiveNodes) {
        this.inactiveNodes = inactiveNodes;
    }

    public String getNetworkStatus() {
        return networkStatus;
    }

    public void setNetworkStatus(String networkStatus) {
        this.networkStatus = networkStatus;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}

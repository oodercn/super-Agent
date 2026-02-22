package net.ooder.nexus.skillcenter.dto.p2p;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class DiscoverResultDTO extends BaseDTO {

    private boolean success;
    private int discoveredNodes;
    private String message;
    private Long timestamp;

    public DiscoverResultDTO() {
        this.timestamp = System.currentTimeMillis();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getDiscoveredNodes() {
        return discoveredNodes;
    }

    public void setDiscoveredNodes(int discoveredNodes) {
        this.discoveredNodes = discoveredNodes;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}

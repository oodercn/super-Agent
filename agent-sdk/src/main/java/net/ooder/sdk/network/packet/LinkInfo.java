package net.ooder.sdk.network.packet;

public class LinkInfo {
    private String linkStatus;
    private Long lastConnected;
    private Long disconnectedAt;

    // Getter and Setter methods
    public String getLinkStatus() {
        return linkStatus;
    }

    public void setLinkStatus(String linkStatus) {
        this.linkStatus = linkStatus;
    }

    public Long getLastConnected() {
        return lastConnected;
    }

    public void setLastConnected(Long lastConnected) {
        this.lastConnected = lastConnected;
    }

    public Long getDisconnectedAt() {
        return disconnectedAt;
    }

    public void setDisconnectedAt(Long disconnectedAt) {
        this.disconnectedAt = disconnectedAt;
    }
}

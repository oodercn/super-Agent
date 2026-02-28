package net.ooder.nexus.dto.p2p;

import java.io.Serializable;

public class LinkQualityUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String linkId;
    private Integer latency;
    private Double packetLoss;

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public Integer getLatency() {
        return latency;
    }

    public void setLatency(Integer latency) {
        this.latency = latency;
    }

    public Double getPacketLoss() {
        return packetLoss;
    }

    public void setPacketLoss(Double packetLoss) {
        this.packetLoss = packetLoss;
    }
}

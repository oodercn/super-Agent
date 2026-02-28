package net.ooder.nexus.dto.p2p;

import java.io.Serializable;

public class NetworkStatsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer totalLinks;
    private Integer activeLinks;
    private Integer degradedLinks;
    private Integer failedLinks;
    private Double averageLatency;
    private Double averagePacketLoss;

    public Integer getTotalLinks() {
        return totalLinks;
    }

    public void setTotalLinks(Integer totalLinks) {
        this.totalLinks = totalLinks;
    }

    public Integer getActiveLinks() {
        return activeLinks;
    }

    public void setActiveLinks(Integer activeLinks) {
        this.activeLinks = activeLinks;
    }

    public Integer getDegradedLinks() {
        return degradedLinks;
    }

    public void setDegradedLinks(Integer degradedLinks) {
        this.degradedLinks = degradedLinks;
    }

    public Integer getFailedLinks() {
        return failedLinks;
    }

    public void setFailedLinks(Integer failedLinks) {
        this.failedLinks = failedLinks;
    }

    public Double getAverageLatency() {
        return averageLatency;
    }

    public void setAverageLatency(Double averageLatency) {
        this.averageLatency = averageLatency;
    }

    public Double getAveragePacketLoss() {
        return averagePacketLoss;
    }

    public void setAveragePacketLoss(Double averagePacketLoss) {
        this.averagePacketLoss = averagePacketLoss;
    }
}

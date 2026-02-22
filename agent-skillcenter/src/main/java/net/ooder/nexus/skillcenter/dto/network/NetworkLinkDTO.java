package net.ooder.nexus.skillcenter.dto.network;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class NetworkLinkDTO extends BaseDTO {

    private String linkId;
    private String sourceNode;
    private String targetNode;
    private String status;
    private String linkType;
    private int latency;
    private int bandwidth;
    private Long establishedAt;
    private Long lastActive;

    public NetworkLinkDTO() {}

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getSourceNode() {
        return sourceNode;
    }

    public void setSourceNode(String sourceNode) {
        this.sourceNode = sourceNode;
    }

    public String getTargetNode() {
        return targetNode;
    }

    public void setTargetNode(String targetNode) {
        this.targetNode = targetNode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public int getLatency() {
        return latency;
    }

    public void setLatency(int latency) {
        this.latency = latency;
    }

    public int getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(int bandwidth) {
        this.bandwidth = bandwidth;
    }

    public Long getEstablishedAt() {
        return establishedAt;
    }

    public void setEstablishedAt(Long establishedAt) {
        this.establishedAt = establishedAt;
    }

    public Long getLastActive() {
        return lastActive;
    }

    public void setLastActive(Long lastActive) {
        this.lastActive = lastActive;
    }
}

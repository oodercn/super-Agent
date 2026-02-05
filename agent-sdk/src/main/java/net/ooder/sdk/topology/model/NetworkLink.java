package net.ooder.sdk.topology.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Map;
import java.util.UUID;

public class NetworkLink {
    @JSONField(name = "linkId")
    private String linkId;
    
    @JSONField(name = "sourceNodeId")
    private String sourceNodeId;
    
    @JSONField(name = "destinationNodeId")
    private String destinationNodeId;
    
    @JSONField(name = "linkType")
    private LinkType linkType;
    
    @JSONField(name = "status")
    private LinkStatus status;
    
    @JSONField(name = "metrics")
    private LinkMetrics metrics;
    
    @JSONField(name = "properties")
    private Map<String, Object> properties;
    
    @JSONField(name = "createdAt")
    private long createdAt;
    
    @JSONField(name = "updatedAt")
    private long updatedAt;
    
    public NetworkLink() {
        this.linkId = UUID.randomUUID().toString();
        this.status = LinkStatus.UNKNOWN;
        this.metrics = new LinkMetrics();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }
    
    public NetworkLink(String sourceNodeId, String destinationNodeId, LinkType linkType) {
        this();
        this.sourceNodeId = sourceNodeId;
        this.destinationNodeId = destinationNodeId;
        this.linkType = linkType;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getSourceNodeId() {
        return sourceNodeId;
    }

    public void setSourceNodeId(String sourceNodeId) {
        this.sourceNodeId = sourceNodeId;
        this.updatedAt = System.currentTimeMillis();
    }

    public String getDestinationNodeId() {
        return destinationNodeId;
    }

    public void setDestinationNodeId(String destinationNodeId) {
        this.destinationNodeId = destinationNodeId;
        this.updatedAt = System.currentTimeMillis();
    }

    public LinkType getLinkType() {
        return linkType;
    }

    public void setLinkType(LinkType linkType) {
        this.linkType = linkType;
        this.updatedAt = System.currentTimeMillis();
    }

    public LinkStatus getStatus() {
        return status;
    }

    public void setStatus(LinkStatus status) {
        this.status = status;
        this.updatedAt = System.currentTimeMillis();
    }

    public LinkMetrics getMetrics() {
        return metrics;
    }

    public void setMetrics(LinkMetrics metrics) {
        this.metrics = metrics;
        this.updatedAt = System.currentTimeMillis();
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
        this.updatedAt = System.currentTimeMillis();
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "NetworkLink{" +
                "linkId='" + linkId + '\'' +
                ", sourceNodeId='" + sourceNodeId + '\'' +
                ", destinationNodeId='" + destinationNodeId + '\'' +
                ", linkType=" + linkType +
                ", status=" + status +
                '}';
    }
}

package net.ooder.nexus.model.network;

import java.io.Serializable;

public class EndAgent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String agentId;
    private String name;
    private String type;
    private String status;
    private String ipAddress;
    private String routeAgentId;
    private String version;
    private String description;
    private long createdAt;
    private long lastUpdated;

    public EndAgent() {
    }

    public EndAgent(String agentId, String name, String type, String status, String ipAddress, String routeAgentId, String version, String description, long createdAt, long lastUpdated) {
        this.agentId = agentId;
        this.name = name;
        this.type = type;
        this.status = status;
        this.ipAddress = ipAddress;
        this.routeAgentId = routeAgentId;
        this.version = version;
        this.description = description;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getRouteAgentId() {
        return routeAgentId;
    }

    public void setRouteAgentId(String routeAgentId) {
        this.routeAgentId = routeAgentId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
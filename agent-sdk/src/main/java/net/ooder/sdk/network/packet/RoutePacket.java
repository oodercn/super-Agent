package net.ooder.sdk.network.packet;

import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Map;

public class RoutePacket extends UDPPacket {
    private String routeId;
    private String routeType;
    private String sourceId;
    private String destinationId;
    private List<String> path;
    private Map<String, Object> routeParams;
    private List<RouteEntry> routeEntries;
    private String status;

    public RoutePacket() {
        super();
    }
    
    public static RoutePacket fromJson(String json) {
        return JSON.parseObject(json, RoutePacket.class);
    }
    
    @Override
    public String getType() {
        return "route";
    }
    
    // RouteEntry inner class
    public static class RouteEntry {
        private String agentId;
        private String agentType;
        private String endpoint;
        private Map<String, Object> capabilities;
        private long lastUpdated;

        // Getter and Setter methods
        public String getAgentId() {
            return agentId;
        }

        public void setAgentId(String agentId) {
            this.agentId = agentId;
        }

        public String getAgentType() {
            return agentType;
        }

        public void setAgentType(String agentType) {
            this.agentType = agentType;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public Map<String, Object> getCapabilities() {
            return capabilities;
        }

        public void setCapabilities(Map<String, Object> capabilities) {
            this.capabilities = capabilities;
        }

        public long getLastUpdated() {
            return lastUpdated;
        }

        public void setLastUpdated(long lastUpdated) {
            this.lastUpdated = lastUpdated;
        }
    }

    // Getter and Setter methods
    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

    public Map<String, Object> getRouteParams() {
        return routeParams;
    }

    public void setRouteParams(Map<String, Object> routeParams) {
        this.routeParams = routeParams;
    }

    public List<RouteEntry> getRouteEntries() {
        return routeEntries;
    }

    public void setRouteEntries(List<RouteEntry> routeEntries) {
        this.routeEntries = routeEntries;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Builder class
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private RoutePacket packet = new RoutePacket();

        public Builder routeId(String routeId) {
            packet.setRouteId(routeId);
            return this;
        }

        public Builder routeType(String routeType) {
            packet.setRouteType(routeType);
            return this;
        }

        public Builder sourceId(String sourceId) {
            packet.setSourceId(sourceId);
            return this;
        }

        public Builder destinationId(String destinationId) {
            packet.setDestinationId(destinationId);
            return this;
        }

        public Builder path(List<String> path) {
            packet.setPath(path);
            return this;
        }

        public Builder routeParams(Map<String, Object> routeParams) {
            packet.setRouteParams(routeParams);
            return this;
        }

        public Builder routeEntries(List<RouteEntry> routeEntries) {
            packet.setRouteEntries(routeEntries);
            return this;
        }

        public Builder status(String status) {
            packet.setStatus(status);
            return this;
        }

        public Builder senderId(String senderId) {
            packet.setSenderId(senderId);
            return this;
        }

        public Builder receiverId(String receiverId) {
            packet.setReceiverId(receiverId);
            return this;
        }

        public RoutePacket build() {
            return packet;
        }
    }
}
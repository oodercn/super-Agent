package net.ooder.nexus.skillcenter.dto.network;

import net.ooder.nexus.skillcenter.dto.BaseDTO;
import java.util.List;

public class NetworkRouteDTO extends BaseDTO {

    private String routeId;
    private String sourceNode;
    private String targetNode;
    private List<String> hops;
    private int totalLatency;
    private int hopCount;
    private String status;
    private String routeType;

    public NetworkRouteDTO() {}

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
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

    public List<String> getHops() {
        return hops;
    }

    public void setHops(List<String> hops) {
        this.hops = hops;
    }

    public int getTotalLatency() {
        return totalLatency;
    }

    public void setTotalLatency(int totalLatency) {
        this.totalLatency = totalLatency;
    }

    public int getHopCount() {
        return hopCount;
    }

    public void setHopCount(int hopCount) {
        this.hopCount = hopCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }
}

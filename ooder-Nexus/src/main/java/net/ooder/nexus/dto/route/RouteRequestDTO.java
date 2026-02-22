package net.ooder.nexus.dto.route;

import java.io.Serializable;

/**
 * Route request DTO
 */
public class RouteRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Route name
     */
    private String name;

    /**
     * Source node ID
     */
    private String sourceNodeId;

    /**
     * Target node ID
     */
    private String targetNodeId;

    /**
     * Route type (direct, relay, proxy)
     */
    private String routeType;

    /**
     * Priority (1-100)
     */
    private Integer priority;

    /**
     * Weight for load balancing
     */
    private Integer weight;

    /**
     * Whether route is enabled
     */
    private Boolean enabled;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSourceNodeId() { return sourceNodeId; }
    public void setSourceNodeId(String sourceNodeId) { this.sourceNodeId = sourceNodeId; }
    public String getTargetNodeId() { return targetNodeId; }
    public void setTargetNodeId(String targetNodeId) { this.targetNodeId = targetNodeId; }
    public String getRouteType() { return routeType; }
    public void setRouteType(String routeType) { this.routeType = routeType; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public Integer getWeight() { return weight; }
    public void setWeight(Integer weight) { this.weight = weight; }
    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
}

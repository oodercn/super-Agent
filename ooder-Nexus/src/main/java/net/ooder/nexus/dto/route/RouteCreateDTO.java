package net.ooder.nexus.dto.route;

import java.io.Serializable;

/**
 * Route create request DTO
 */
public class RouteCreateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Route ID (optional, auto-generated if not provided)
     */
    private String routeId;

    /**
     * Route type (optional, default: direct)
     */
    private String type;

    /**
     * Source node ID (required)
     */
    private String source;

    /**
     * Destination node ID (required)
     */
    private String destination;

    public String getRouteId() { return routeId; }
    public void setRouteId(String routeId) { this.routeId = routeId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
}

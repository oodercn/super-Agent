package net.ooder.nexus.dto.end;

import java.io.Serializable;

/**
 * End agent update request DTO
 */
public class EndAgentUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Agent name
     */
    private String name;

    /**
     * Agent type
     */
    private String type;

    /**
     * Agent status
     */
    private String status;

    /**
     * IP address
     */
    private String ipAddress;

    /**
     * Route agent ID
     */
    private String routeAgentId;

    /**
     * Description
     */
    private String description;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getRouteAgentId() { return routeAgentId; }
    public void setRouteAgentId(String routeAgentId) { this.routeAgentId = routeAgentId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

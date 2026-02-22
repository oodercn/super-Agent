package net.ooder.nexus.dto.end;

import java.io.Serializable;

/**
 * End agent create request DTO
 */
public class EndAgentCreateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Agent ID (optional, auto-generated if not provided)
     */
    private String agentId;

    /**
     * Agent name (required)
     */
    private String name;

    /**
     * Agent type (required)
     */
    private String type;

    /**
     * IP address (required)
     */
    private String ipAddress;

    /**
     * Route agent ID (optional)
     */
    private String routeAgentId;

    /**
     * Version (optional, default: 1.0.0)
     */
    private String version;

    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getRouteAgentId() { return routeAgentId; }
    public void setRouteAgentId(String routeAgentId) { this.routeAgentId = routeAgentId; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
}

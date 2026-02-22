package net.ooder.nexus.dto.mcp;

import java.io.Serializable;

/**
 * MCP Agent create request DTO
 */
public class McpAgentCreateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String type;
    private String endpoint;
    private Integer heartbeatInterval;
    private String description;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    public Integer getHeartbeatInterval() { return heartbeatInterval; }
    public void setHeartbeatInterval(Integer heartbeatInterval) { this.heartbeatInterval = heartbeatInterval; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

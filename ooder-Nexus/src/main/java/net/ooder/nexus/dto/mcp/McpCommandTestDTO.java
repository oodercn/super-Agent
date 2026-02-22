package net.ooder.nexus.dto.mcp;

import java.io.Serializable;
import java.util.Map;

/**
 * MCP command test request DTO
 */
public class McpCommandTestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Command type (MCP_STATUS, MCP_REGISTER, MCP_DEREGISTER, MCP_HEARTBEAT)
     */
    private String commandType;

    /**
     * Agent ID
     */
    private String agentId;

    /**
     * Command data
     */
    private Map<String, Object> data;

    public String getCommandType() { return commandType; }
    public void setCommandType(String commandType) { this.commandType = commandType; }
    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }
    public Map<String, Object> getData() { return data; }
    public void setData(Map<String, Object> data) { this.data = data; }
}

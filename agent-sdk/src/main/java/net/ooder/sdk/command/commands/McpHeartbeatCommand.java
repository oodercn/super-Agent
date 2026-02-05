package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

import java.util.Map;

/**
 * MCP_HEARTBEAT
 * MCP
 */
public class McpHeartbeatCommand extends Command {
    /**
     * MCPID
     */
    @JSONField(name = "mcpAgentId")
    private String mcpAgentId;

    /**
     * MCP
     */
    @JSONField(name = "status")
    private String status;

    /**
     * 
     */
    @JSONField(name = "systemLoad")
    private Map<String, Object> systemLoad;

    /**
     * 
     */
    @JSONField(name = "connectedRouteCount")
    private int connectedRouteCount;

    /**
     * 
     */
    @JSONField(name = "connectedEndagentCount")
    private int connectedEndagentCount;

    /**
     * 
     */
    @JSONField(name = "lastHeartbeatTime")
    private long lastHeartbeatTime;

    /**
     * 
     */
    public McpHeartbeatCommand() {
        super(CommandType.MCP_HEARTBEAT);
    }

    /**
     * 
     * @param mcpAgentId MCPID
     * @param status MCP
     * @param systemLoad 
     * @param connectedRouteCount 
     * @param connectedEndagentCount 
     * @param lastHeartbeatTime 
     */
    public McpHeartbeatCommand(String mcpAgentId, String status, Map<String, Object> systemLoad, int connectedRouteCount, int connectedEndagentCount, long lastHeartbeatTime) {
        super(CommandType.MCP_HEARTBEAT);
        this.mcpAgentId = mcpAgentId;
        this.status = status;
        this.systemLoad = systemLoad;
        this.connectedRouteCount = connectedRouteCount;
        this.connectedEndagentCount = connectedEndagentCount;
        this.lastHeartbeatTime = lastHeartbeatTime;
    }

    // GetterSetter
    public String getMcpAgentId() {
        return mcpAgentId;
    }

    public void setMcpAgentId(String mcpAgentId) {
        this.mcpAgentId = mcpAgentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Object> getSystemLoad() {
        return systemLoad;
    }

    public void setSystemLoad(Map<String, Object> systemLoad) {
        this.systemLoad = systemLoad;
    }

    public int getConnectedRouteCount() {
        return connectedRouteCount;
    }

    public void setConnectedRouteCount(int connectedRouteCount) {
        this.connectedRouteCount = connectedRouteCount;
    }

    public int getConnectedEndagentCount() {
        return connectedEndagentCount;
    }

    public void setConnectedEndagentCount(int connectedEndagentCount) {
        this.connectedEndagentCount = connectedEndagentCount;
    }

    public long getLastHeartbeatTime() {
        return lastHeartbeatTime;
    }

    public void setLastHeartbeatTime(long lastHeartbeatTime) {
        this.lastHeartbeatTime = lastHeartbeatTime;
    }

    @Override
    public String toString() {
        return "McpHeartbeatCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", mcpAgentId='" + mcpAgentId + "'" +
                ", status='" + status + "'" +
                ", systemLoad=" + systemLoad +
                ", connectedRouteCount=" + connectedRouteCount +
                ", connectedEndagentCount=" + connectedEndagentCount +
                ", lastHeartbeatTime=" + lastHeartbeatTime +
                '}';
    }
}
















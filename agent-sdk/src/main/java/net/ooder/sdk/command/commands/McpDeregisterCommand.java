package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

/**
 * MCP_DEREGISTER
 * MCP
 */
public class McpDeregisterCommand extends Command {
    /**
     * MCPID
     */
    @JSONField(name = "mcpAgentId")
    private String mcpAgentId;

    /**
     * 
     */
    @JSONField(name = "force")
    private boolean force;

    /**
     * 
     */
    @JSONField(name = "reason")
    private String reason;

    /**
     * 
     */
    public McpDeregisterCommand() {
        super(CommandType.MCP_DEREGISTER);
    }

    /**
     * 
     * @param mcpAgentId MCPID
     * @param force 
     * @param reason 
     */
    public McpDeregisterCommand(String mcpAgentId, boolean force, String reason) {
        super(CommandType.MCP_DEREGISTER);
        this.mcpAgentId = mcpAgentId;
        this.force = force;
        this.reason = reason;
    }

    // GetterSetter
    public String getMcpAgentId() {
        return mcpAgentId;
    }

    public void setMcpAgentId(String mcpAgentId) {
        this.mcpAgentId = mcpAgentId;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "McpDeregisterCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", mcpAgentId='" + mcpAgentId + "'" +
                ", force=" + force +
                ", reason='" + reason + "'" +
                '}';
    }
}














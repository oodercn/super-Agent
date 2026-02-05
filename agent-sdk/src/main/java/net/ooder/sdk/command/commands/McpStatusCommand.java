package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;import net.ooder.sdk.command.model.CommandType;

/**
 * MCP_STATUS
 * MCP
 */
public class McpStatusCommand extends Command {
    /**
     * MCPIDMCP
     */
    @JSONField(name = "mcpAgentId")
    private String mcpAgentId;

    /**
     * 
     */
    @JSONField(name = "detailed")
    private boolean detailed;

    /**
     * 
     */
    public McpStatusCommand() {
        super(CommandType.MCP_STATUS);
    }

    /**
     * 
     * @param mcpAgentId MCPID
     * @param detailed 
     */
    public McpStatusCommand(String mcpAgentId, boolean detailed) {
        super(CommandType.MCP_STATUS);
        this.mcpAgentId = mcpAgentId;
        this.detailed = detailed;
    }

    // GetterSetter
    public String getMcpAgentId() {
        return mcpAgentId;
    }

    public void setMcpAgentId(String mcpAgentId) {
        this.mcpAgentId = mcpAgentId;
    }

    public boolean isDetailed() {
        return detailed;
    }

    public void setDetailed(boolean detailed) {
        this.detailed = detailed;
    }

    @Override
    public String toString() {
        return "McpStatusCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", mcpAgentId='" + mcpAgentId + "'" +
                ", detailed=" + detailed +
                '}';
    }
}
















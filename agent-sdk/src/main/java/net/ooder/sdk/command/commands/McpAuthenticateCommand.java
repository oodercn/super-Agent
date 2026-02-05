package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

import java.util.Map;

/**
 * MCP_AUTHENTICATE
 * MCP
 */
public class McpAuthenticateCommand extends Command {
    /**
     * MCPID
     */
    @JSONField(name = "mcpAgentId")
    private String mcpAgentId;

    /**
     * 
     */
    @JSONField(name = "authInfo")
    private Map<String, Object> authInfo;

    /**
     * 
     */
    @JSONField(name = "expiresIn")
    private long expiresIn;

    /**
     * 
     */
    public McpAuthenticateCommand() {
        super(CommandType.MCP_AUTHENTICATE);
    }

    /**
     * 
     * @param mcpAgentId MCPID
     * @param authInfo 
     * @param expiresIn 
     */
    public McpAuthenticateCommand(String mcpAgentId, Map<String, Object> authInfo, long expiresIn) {
        super(CommandType.MCP_AUTHENTICATE);
        this.mcpAgentId = mcpAgentId;
        this.authInfo = authInfo;
        this.expiresIn = expiresIn;
    }

    // GetterSetter
    public String getMcpAgentId() {
        return mcpAgentId;
    }

    public void setMcpAgentId(String mcpAgentId) {
        this.mcpAgentId = mcpAgentId;
    }

    public Map<String, Object> getAuthInfo() {
        return authInfo;
    }

    public void setAuthInfo(Map<String, Object> authInfo) {
        this.authInfo = authInfo;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    @Override
    public String toString() {
        return "McpAuthenticateCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", mcpAgentId='" + mcpAgentId + "'" +
                ", authInfo=" + authInfo +
                ", expiresIn=" + expiresIn +
                '}';
    }
}














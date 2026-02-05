package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

import java.util.Map;

/**
 * MCP_REGISTER
 * MCP
 */
public class McpRegisterCommand extends Command {
    /**
     * MCPID
     */
    @JSONField(name = "mcpAgentId")
    private String mcpAgentId;

    /**
     * MCP
     */
    @JSONField(name = "mcpAgentAddress")
    private String mcpAgentAddress;

    /**
     * MCP
     */
    @JSONField(name = "mcpAgentInfo")
    private Map<String, Object> mcpAgentInfo;

    /**
     * 
     */
    @JSONField(name = "supportedFeatures")
    private Map<String, Object> supportedFeatures;

    /**
     * 
     */
    public McpRegisterCommand() {
        super(CommandType.MCP_REGISTER);
    }

    /**
     * 
     * @param mcpAgentId MCPID
     * @param mcpAgentAddress MCP
     * @param mcpAgentInfo MCP
     * @param supportedFeatures 
     */
    public McpRegisterCommand(String mcpAgentId, String mcpAgentAddress, Map<String, Object> mcpAgentInfo, Map<String, Object> supportedFeatures) {
        super(CommandType.MCP_REGISTER);
        this.mcpAgentId = mcpAgentId;
        this.mcpAgentAddress = mcpAgentAddress;
        this.mcpAgentInfo = mcpAgentInfo;
        this.supportedFeatures = supportedFeatures;
    }

    // GetterSetter
    public String getMcpAgentId() {
        return mcpAgentId;
    }

    public void setMcpAgentId(String mcpAgentId) {
        this.mcpAgentId = mcpAgentId;
    }

    public String getMcpAgentAddress() {
        return mcpAgentAddress;
    }

    public void setMcpAgentAddress(String mcpAgentAddress) {
        this.mcpAgentAddress = mcpAgentAddress;
    }

    public Map<String, Object> getMcpAgentInfo() {
        return mcpAgentInfo;
    }

    public void setMcpAgentInfo(Map<String, Object> mcpAgentInfo) {
        this.mcpAgentInfo = mcpAgentInfo;
    }

    public Map<String, Object> getSupportedFeatures() {
        return supportedFeatures;
    }

    public void setSupportedFeatures(Map<String, Object> supportedFeatures) {
        this.supportedFeatures = supportedFeatures;
    }

    @Override
    public String toString() {
        return "McpRegisterCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", mcpAgentId='" + mcpAgentId + "'" +
                ", mcpAgentAddress='" + mcpAgentAddress + "'" +
                ", mcpAgentInfo=" + mcpAgentInfo +
                ", supportedFeatures=" + supportedFeatures +
                '}';
    }
}














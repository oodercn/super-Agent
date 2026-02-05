package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

import java.util.List;

/**
 * MCP_DISCOVER
 * MCP
 */
public class McpDiscoverCommand extends Command {
    /**
     * MCP
     */
    @JSONField(name = "agentType")
    private String agentType;

    /**
     * MCP
     */
    @JSONField(name = "features")
    private List<String> features;

    /**
     * 
     */
    @JSONField(name = "detailed")
    private boolean detailed;

    /**
     * 
     */
    public McpDiscoverCommand() {
        super(CommandType.MCP_DISCOVER);
    }

    /**
     * 
     * @param agentType MCP
     * @param features MCP
     * @param detailed 
     */
    public McpDiscoverCommand(String agentType, List<String> features, boolean detailed) {
        super(CommandType.MCP_DISCOVER);
        this.agentType = agentType;
        this.features = features;
        this.detailed = detailed;
    }

    // GetterSetter
    public String getAgentType() {
        return agentType;
    }

    public void setAgentType(String agentType) {
        this.agentType = agentType;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public boolean isDetailed() {
        return detailed;
    }

    public void setDetailed(boolean detailed) {
        this.detailed = detailed;
    }

    @Override
    public String toString() {
        return "McpDiscoverCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", agentType='" + agentType + "'" +
                ", features=" + features +
                ", detailed=" + detailed +
                '}';
    }
}














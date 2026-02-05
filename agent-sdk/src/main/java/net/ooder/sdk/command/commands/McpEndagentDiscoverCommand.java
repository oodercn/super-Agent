package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;import net.ooder.sdk.command.model.CommandType;

import java.util.List;

/**
 * MCP_ENDAGENT_DISCOVER
 * MCP
 */
public class McpEndagentDiscoverCommand extends Command {
    /**
     * 
     */
    @JSONField(name = "endagentType")
    private String endagentType;

    /**
     * 
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
    public McpEndagentDiscoverCommand() {
        super(CommandType.MCP_ENDAGENT_DISCOVER);
    }

    /**
     * 
     * @param endagentType 
     * @param features 
     * @param detailed 
     */
    public McpEndagentDiscoverCommand(String endagentType, List<String> features, boolean detailed) {
        super(CommandType.MCP_ENDAGENT_DISCOVER);
        this.endagentType = endagentType;
        this.features = features;
        this.detailed = detailed;
    }

    // GetterSetter
    public String getEndagentType() {
        return endagentType;
    }

    public void setEndagentType(String endagentType) {
        this.endagentType = endagentType;
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
        return "McpEndagentDiscoverCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", endagentType='" + endagentType + "'" +
                ", features=" + features +
                ", detailed=" + detailed +
                '}';
    }
}
















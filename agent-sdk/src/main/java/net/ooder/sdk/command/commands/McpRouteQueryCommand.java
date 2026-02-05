package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

import java.util.List;

/**
 * MCP_ROUTE_QUERY
 * MCP
 */
public class McpRouteQueryCommand extends Command {
    /**
     * ID
     */
    @JSONField(name = "routeIds")
    private List<String> routeIds;

    /**
     * 
     */
    @JSONField(name = "queryCondition")
    private String queryCondition;

    /**
     * 
     */
    @JSONField(name = "detailed")
    private boolean detailed;

    /**
     * 
     */
    public McpRouteQueryCommand() {
        super(CommandType.MCP_ROUTE_QUERY);
    }

    /**
     * 
     * @param routeIds ID
     * @param queryCondition 
     * @param detailed 
     */
    public McpRouteQueryCommand(List<String> routeIds, String queryCondition, boolean detailed) {
        super(CommandType.MCP_ROUTE_QUERY);
        this.routeIds = routeIds;
        this.queryCondition = queryCondition;
        this.detailed = detailed;
    }

    // GetterSetter
    public List<String> getRouteIds() {
        return routeIds;
    }

    public void setRouteIds(List<String> routeIds) {
        this.routeIds = routeIds;
    }

    public String getQueryCondition() {
        return queryCondition;
    }

    public void setQueryCondition(String queryCondition) {
        this.queryCondition = queryCondition;
    }

    public boolean isDetailed() {
        return detailed;
    }

    public void setDetailed(boolean detailed) {
        this.detailed = detailed;
    }

    @Override
    public String toString() {
        return "McpRouteQueryCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", routeIds=" + routeIds +
                ", queryCondition='" + queryCondition + "'" +
                ", detailed=" + detailed +
                '}';
    }
}














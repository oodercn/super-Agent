package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

import java.util.Map;

/**
 * MCP_ROUTE_UPDATE
 * MCP
 */
public class McpRouteUpdateCommand extends Command {
    /**
     * ID
     */
    @JSONField(name = "routeId")
    private String routeId;

    /**
     * 
     */
    @JSONField(name = "routeInfo")
    private Map<String, Object> routeInfo;

    /**
     * 
     */
    @JSONField(name = "needConfirmation")
    private boolean needConfirmation;

    /**
     * 
     */
    public McpRouteUpdateCommand() {
        super(CommandType.MCP_ROUTE_UPDATE);
    }

    /**
     * 
     * @param routeId ID
     * @param routeInfo 
     * @param needConfirmation 
     */
    public McpRouteUpdateCommand(String routeId, Map<String, Object> routeInfo, boolean needConfirmation) {
        super(CommandType.MCP_ROUTE_UPDATE);
        this.routeId = routeId;
        this.routeInfo = routeInfo;
        this.needConfirmation = needConfirmation;
    }

    // GetterSetter
    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public Map<String, Object> getRouteInfo() {
        return routeInfo;
    }

    public void setRouteInfo(Map<String, Object> routeInfo) {
        this.routeInfo = routeInfo;
    }

    public boolean isNeedConfirmation() {
        return needConfirmation;
    }

    public void setNeedConfirmation(boolean needConfirmation) {
        this.needConfirmation = needConfirmation;
    }

    @Override
    public String toString() {
        return "McpRouteUpdateCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", routeId='" + routeId + "'" +
                ", routeInfo=" + routeInfo +
                ", needConfirmation=" + needConfirmation +
                '}';
    }
}














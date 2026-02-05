package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

import java.util.List;

/**
 * ROUTE_LIST
 * 
 */
public class RouteListCommand extends Command {
    /**
     * ID
     */
    @JSONField(name = "routeIds")
    private List<String> routeIds;

    /**
     * 
     */
    @JSONField(name = "detailed")
    private boolean detailed;

    /**
     * 
     */
    public RouteListCommand() {
        super(CommandType.ROUTE_LIST);
    }

    /**
     * 
     * @param routeIds ID
     * @param detailed 
     */
    public RouteListCommand(List<String> routeIds, boolean detailed) {
        super(CommandType.ROUTE_LIST);
        this.routeIds = routeIds;
        this.detailed = detailed;
    }

    // GetterSetter
    public List<String> getRouteIds() {
        return routeIds;
    }

    public void setRouteIds(List<String> routeIds) {
        this.routeIds = routeIds;
    }

    public boolean isDetailed() {
        return detailed;
    }

    public void setDetailed(boolean detailed) {
        this.detailed = detailed;
    }

    @Override
    public String toString() {
        return "RouteListCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", routeIds=" + routeIds +
                ", detailed=" + detailed +
                '}';
    }
}














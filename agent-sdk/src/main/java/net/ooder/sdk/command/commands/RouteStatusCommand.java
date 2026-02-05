package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

/**
 * ROUTE_STATUS
 * 
 */
public class RouteStatusCommand extends Command {
    /**
     * ID
     */
    @JSONField(name = "routeId")
    private String routeId;

    /**
     * 
     */
    @JSONField(name = "detailed")
    private boolean detailed;

    /**
     * 
     */
    public RouteStatusCommand() {
        super(CommandType.ROUTE_STATUS);
    }

    /**
     * 
     * @param routeId ID
     * @param detailed 
     */
    public RouteStatusCommand(String routeId, boolean detailed) {
        super(CommandType.ROUTE_STATUS);
        this.routeId = routeId;
        this.detailed = detailed;
    }

    // GetterSetter
    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public boolean isDetailed() {
        return detailed;
    }

    public void setDetailed(boolean detailed) {
        this.detailed = detailed;
    }

    @Override
    public String toString() {
        return "RouteStatusCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", routeId='" + routeId + "'" +
                ", detailed=" + detailed +
                '}';
    }
}
















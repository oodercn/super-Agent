package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

/**
 * ROUTE_REMOVE
 * 
 */
public class RouteRemoveCommand extends Command {
    /**
     * ID
     */
    @JSONField(name = "routeId")
    private String routeId;

    /**
     * 
     */
    @JSONField(name = "force")
    private boolean force;

    /**
     * 
     */
    public RouteRemoveCommand() {
        super(CommandType.ROUTE_REMOVE);
    }

    /**
     * 
     * @param routeId ID
     * @param force 
     */
    public RouteRemoveCommand(String routeId, boolean force) {
        super(CommandType.ROUTE_REMOVE);
        this.routeId = routeId;
        this.force = force;
    }

    // GetterSetter
    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    @Override
    public String toString() {
        return "RouteRemoveCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", routeId='" + routeId + "'" +
                ", force=" + force +
                '}';
    }
}














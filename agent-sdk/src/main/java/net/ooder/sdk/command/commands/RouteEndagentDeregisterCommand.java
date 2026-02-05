package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

/**
 * ROUTE_ENDAGENT_DEREGISTER
 * 
 */
public class RouteEndagentDeregisterCommand extends Command {
    /**
     * ID
     */
    @JSONField(name = "endagentId")
    private String endagentId;

    /**
     * 
     */
    @JSONField(name = "force")
    private boolean force;

    /**
     * 
     */
    @JSONField(name = "reason")
    private String reason;

    /**
     * 
     */
    public RouteEndagentDeregisterCommand() {
        super(CommandType.ROUTE_ENDAGENT_DEREGISTER);
    }

    /**
     * 
     * @param endagentId ID
     * @param force 
     * @param reason 
     */
    public RouteEndagentDeregisterCommand(String endagentId, boolean force, String reason) {
        super(CommandType.ROUTE_ENDAGENT_DEREGISTER);
        this.endagentId = endagentId;
        this.force = force;
        this.reason = reason;
    }

    // GetterSetter
    public String getEndagentId() {
        return endagentId;
    }

    public void setEndagentId(String endagentId) {
        this.endagentId = endagentId;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "RouteEndagentDeregisterCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", endagentId='" + endagentId + "'" +
                ", force=" + force +
                ", reason='" + reason + "'" +
                '}';
    }
}














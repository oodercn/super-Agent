package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

import java.util.Map;

/**
 * ROUTE_ENDAGENT_REGISTER
 * 
 */
public class RouteEndagentRegisterCommand extends Command {
    /**
     * ID
     */
    @JSONField(name = "endagentId")
    private String endagentId;

    /**
     * 
     */
    @JSONField(name = "endagentAddress")
    private String endagentAddress;

    /**
     * 
     */
    @JSONField(name = "endagentInfo")
    private Map<String, Object> endagentInfo;

    /**
     * 
     */
    @JSONField(name = "heartbeatInterval")
    private int heartbeatInterval;

    /**
     * 
     */
    public RouteEndagentRegisterCommand() {
        super(CommandType.ROUTE_ENDAGENT_REGISTER);
    }

    /**
     * 
     * @param endagentId ID
     * @param endagentAddress 
     * @param endagentInfo 
     * @param heartbeatInterval 
     */
    public RouteEndagentRegisterCommand(String endagentId, String endagentAddress, Map<String, Object> endagentInfo, int heartbeatInterval) {
        super(CommandType.ROUTE_ENDAGENT_REGISTER);
        this.endagentId = endagentId;
        this.endagentAddress = endagentAddress;
        this.endagentInfo = endagentInfo;
        this.heartbeatInterval = heartbeatInterval;
    }

    // GetterSetter
    public String getEndagentId() {
        return endagentId;
    }

    public void setEndagentId(String endagentId) {
        this.endagentId = endagentId;
    }

    public String getEndagentAddress() {
        return endagentAddress;
    }

    public void setEndagentAddress(String endagentAddress) {
        this.endagentAddress = endagentAddress;
    }

    public Map<String, Object> getEndagentInfo() {
        return endagentInfo;
    }

    public void setEndagentInfo(Map<String, Object> endagentInfo) {
        this.endagentInfo = endagentInfo;
    }

    public int getHeartbeatInterval() {
        return heartbeatInterval;
    }

    public void setHeartbeatInterval(int heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
    }

    @Override
    public String toString() {
        return "RouteEndagentRegisterCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", endagentId='" + endagentId + "'" +
                ", endagentAddress='" + endagentAddress + "'" +
                ", endagentInfo=" + endagentInfo +
                ", heartbeatInterval=" + heartbeatInterval +
                '}';
    }
}














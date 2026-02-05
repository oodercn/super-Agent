package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

import java.util.Map;

/**
 * ROUTE_AUTH_REQUEST
 * 
 */
public class RouteAuthRequestCommand extends Command {
    /**
     * ID
     */
    @JSONField(name = "requesterId")
    private String requesterId;

    /**
     * 
     */
    @JSONField(name = "authInfo")
    private Map<String, Object> authInfo;

    /**
     * 
     */
    @JSONField(name = "expiresIn")
    private long expiresIn;

    /**
     * 
     */
    public RouteAuthRequestCommand() {
        super(CommandType.ROUTE_AUTH_REQUEST);
    }

    /**
     * 
     * @param requesterId ID
     * @param authInfo 
     * @param expiresIn 
     */
    public RouteAuthRequestCommand(String requesterId, Map<String, Object> authInfo, long expiresIn) {
        super(CommandType.ROUTE_AUTH_REQUEST);
        this.requesterId = requesterId;
        this.authInfo = authInfo;
        this.expiresIn = expiresIn;
    }

    // GetterSetter
    public String getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(String requesterId) {
        this.requesterId = requesterId;
    }

    public Map<String, Object> getAuthInfo() {
        return authInfo;
    }

    public void setAuthInfo(Map<String, Object> authInfo) {
        this.authInfo = authInfo;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    @Override
    public String toString() {
        return "RouteAuthRequestCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", requesterId='" + requesterId + "'" +
                ", authInfo=" + authInfo +
                ", expiresIn=" + expiresIn +
                '}';
    }
}
















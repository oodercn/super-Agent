package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

import java.util.Map;

/**
 * ROUTE_AUTH_RESPONSE
 * 
 */
public class RouteAuthResponseCommand extends Command {
    /**
     * ID
     */
    @JSONField(name = "requestCommandId")
    private String requestCommandId;

    /**
     * 
     */
    @JSONField(name = "success")
    private boolean success;

    /**
     * 
     */
    @JSONField(name = "responseInfo")
    private Map<String, Object> responseInfo;

    /**
     * 
     */
    @JSONField(name = "errorMessage")
    private String errorMessage;

    /**
     * 
     */
    public RouteAuthResponseCommand() {
        super(CommandType.ROUTE_AUTH_RESPONSE);
    }

    /**
     * 
     * @param requestCommandId ID
     * @param success 
     * @param responseInfo 
     * @param errorMessage 
     */
    public RouteAuthResponseCommand(String requestCommandId, boolean success, Map<String, Object> responseInfo, String errorMessage) {
        super(CommandType.ROUTE_AUTH_RESPONSE);
        this.requestCommandId = requestCommandId;
        this.success = success;
        this.responseInfo = responseInfo;
        this.errorMessage = errorMessage;
    }

    // GetterSetter
    public String getRequestCommandId() {
        return requestCommandId;
    }

    public void setRequestCommandId(String requestCommandId) {
        this.requestCommandId = requestCommandId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Map<String, Object> getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(Map<String, Object> responseInfo) {
        this.responseInfo = responseInfo;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "RouteAuthResponseCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", requestCommandId='" + requestCommandId + "'" +
                ", success=" + success +
                ", responseInfo=" + responseInfo +
                ", errorMessage='" + errorMessage + "'" +
                '}';
    }
}














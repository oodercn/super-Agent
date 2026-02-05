package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

import java.util.Map;

/**
 * MCP_AUTH_RESPONSE
 * MCP
 */
public class McpAuthResponseCommand extends Command {
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
    public McpAuthResponseCommand() {
        super(CommandType.MCP_AUTH_RESPONSE);
    }

    /**
     * 
     * @param requestCommandId ID
     * @param success 
     * @param responseInfo 
     * @param errorMessage 
     */
    public McpAuthResponseCommand(String requestCommandId, boolean success, Map<String, Object> responseInfo, String errorMessage) {
        super(CommandType.MCP_AUTH_RESPONSE);
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
        return "McpAuthResponseCommand{" +
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














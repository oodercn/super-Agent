package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

import java.util.Map;

/**
 * COMMAND_RESPONSE
 * 
 */
public class CommandResponseCommand extends Command {
    /**
     * ID
     */
    @JSONField(name = "requestCommandId")
    private String requestCommandId;
    
    /**
     * success, execution_error, parameter_error, timeout, forbidden, not_found
     */
    @JSONField(name = "status")
    private String status;
    
    /**
     * 
     */
    @JSONField(name = "message")
    private String message;
    
    /**
     * 
     */
    @JSONField(name = "data")
    private Map<String, Object> data;
    
    /**
     * 
     */
    @JSONField(name = "error")
    private String error;
    
    /**
     * 
     */
    @JSONField(name = "executionTime")
    private long executionTime;

    /**
     * 
     */
    public CommandResponseCommand() {
        super(CommandType.COMMAND_RESPONSE);
    }

    /**
     * 
     * @param requestCommandId ID
     * @param status 
     * @param message 
     */
    public CommandResponseCommand(String requestCommandId, String status, String message) {
        super(CommandType.COMMAND_RESPONSE);
        this.requestCommandId = requestCommandId;
        this.status = status;
        this.message = message;
    }
    
    /**
     * 
     * @param requestCommandId ID
     * @param status 
     * @param message 
     * @param data 
     * @param executionTime 
     */
    public CommandResponseCommand(String requestCommandId, String status, String message, Map<String, Object> data, long executionTime) {
        super(CommandType.COMMAND_RESPONSE);
        this.requestCommandId = requestCommandId;
        this.status = status;
        this.message = message;
        this.data = data;
        this.executionTime = executionTime;
    }
    
    /**
     * 
     * @param requestCommandId ID
     * @param status 
     * @param message 
     * @param error 
     */
    public CommandResponseCommand(String requestCommandId, String status, String message, String error) {
        super(CommandType.COMMAND_RESPONSE);
        this.requestCommandId = requestCommandId;
        this.status = status;
        this.message = message;
        this.error = error;
    }

    // GetterSetter
    public String getRequestCommandId() {
        return requestCommandId;
    }

    public void setRequestCommandId(String requestCommandId) {
        this.requestCommandId = requestCommandId;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Map<String, Object> getData() {
        return data;
    }
    
    public void setData(Map<String, Object> data) {
        this.data = data;
    }
    
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
    
    public long getExecutionTime() {
        return executionTime;
    }
    
    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    @Override
    public String toString() {
        return "CommandResponseCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", requestCommandId='" + requestCommandId + "'" +
                ", status='" + status + "'" +
                ", message='" + message + "'" +
                ", executionTime=" + executionTime +
                ", data=" + data +
                ", error='" + error + "'" +
                '}';
    }
}














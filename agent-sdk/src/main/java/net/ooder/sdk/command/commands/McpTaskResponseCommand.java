package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

import java.util.Map;

/**
 * MCP_TASK_RESPONSE
 * MCP
 */
public class McpTaskResponseCommand extends Command {
    /**
     * ID
     */
    @JSONField(name = "taskId")
    private String taskId;

    /**
     * 
     */
    @JSONField(name = "success")
    private boolean success;

    /**
     * 
     */
    @JSONField(name = "result")
    private Map<String, Object> result;

    /**
     * 
     */
    @JSONField(name = "errorMessage")
    private String errorMessage;

    /**
     * 
     */
    @JSONField(name = "executionTime")
    private long executionTime;

    /**
     * 
     */
    public McpTaskResponseCommand() {
        super(CommandType.MCP_TASK_RESPONSE);
    }

    /**
     * 
     * @param taskId ID
     * @param success 
     * @param result 
     * @param errorMessage 
     * @param executionTime 
     */
    public McpTaskResponseCommand(String taskId, boolean success, Map<String, Object> result, String errorMessage, long executionTime) {
        super(CommandType.MCP_TASK_RESPONSE);
        this.taskId = taskId;
        this.success = success;
        this.result = result;
        this.errorMessage = errorMessage;
        this.executionTime = executionTime;
    }

    // GetterSetter
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    @Override
    public String toString() {
        return "McpTaskResponseCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", taskId='" + taskId + "'" +
                ", success=" + success +
                ", result=" + result +
                ", errorMessage='" + errorMessage + "'" +
                ", executionTime=" + executionTime +
                '}';
    }
}
















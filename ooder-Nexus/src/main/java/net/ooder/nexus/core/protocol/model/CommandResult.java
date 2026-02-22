package net.ooder.nexus.core.protocol.model;

import java.io.Serializable;
import java.util.Map;

/**
 * Command Execution Result
 */
public class CommandResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;
    private String message;
    private Map<String, Object> data;
    private long executionTime;
    private String commandId;
    private long timestamp;

    public CommandResult() {
        this.timestamp = System.currentTimeMillis();
    }

    public static CommandResult success(String commandId) {
        CommandResult result = new CommandResult();
        result.setCode(200);
        result.setMessage("Success");
        result.setCommandId(commandId);
        return result;
    }

    public static CommandResult success(String commandId, Map<String, Object> data) {
        CommandResult result = success(commandId);
        result.setData(data);
        return result;
    }

    public static CommandResult error(String commandId, String message) {
        CommandResult result = new CommandResult();
        result.setCode(500);
        result.setMessage(message);
        result.setCommandId(commandId);
        return result;
    }

    public static CommandResult error(String commandId, int code, String message) {
        CommandResult result = new CommandResult();
        result.setCode(code);
        result.setMessage(message);
        result.setCommandId(commandId);
        return result;
    }

    public boolean isSuccess() { return code == 200; }
    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Map<String, Object> getData() { return data; }
    public void setData(Map<String, Object> data) { this.data = data; }
    public long getExecutionTime() { return executionTime; }
    public void setExecutionTime(long executionTime) { this.executionTime = executionTime; }
    public String getCommandId() { return commandId; }
    public void setCommandId(String commandId) { this.commandId = commandId; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "CommandResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", executionTime=" + executionTime +
                ", commandId='" + commandId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

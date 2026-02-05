package net.ooder.nexus.model.mcp;

import java.util.Date;
import java.util.Map;

public class TestCommandResult {
    private String id;
    private String commandType;
    private String result;
    private String message;
    private Map<String, Object> data;
    private long duration;
    private Date timestamp;
    private boolean success;

    public TestCommandResult() {
    }

    public TestCommandResult(String id, String commandType, String result, String message, Map<String, Object> data, long duration, Date timestamp, boolean success) {
        this.id = id;
        this.commandType = commandType;
        this.result = result;
        this.message = message;
        this.data = data;
        this.duration = duration;
        this.timestamp = timestamp;
        this.success = success;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
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

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
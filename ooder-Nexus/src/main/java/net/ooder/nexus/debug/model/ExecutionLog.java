package net.ooder.nexus.debug.model;

import java.io.Serializable;

/**
 * 执行日志
 */
public class ExecutionLog implements Serializable {

    private static final long serialVersionUID = 1L;

    private long timestamp;
    private String level;
    private String message;
    private String commandType;
    private long duration;

    public ExecutionLog() {
        this.timestamp = System.currentTimeMillis();
    }

    public ExecutionLog(String level, String message) {
        this();
        this.level = level;
        this.message = message;
    }

    public ExecutionLog(String level, String message, String commandType, long duration) {
        this(level, message);
        this.commandType = commandType;
        this.duration = duration;
    }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getCommandType() { return commandType; }
    public void setCommandType(String commandType) { this.commandType = commandType; }
    public long getDuration() { return duration; }
    public void setDuration(long duration) { this.duration = duration; }
}

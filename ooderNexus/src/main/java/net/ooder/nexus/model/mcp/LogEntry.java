package net.ooder.nexus.model.mcp;

import java.io.Serializable;

public class LogEntry implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String level;
    private String message;
    private String source;
    private long timestamp;

    public LogEntry() {
    }

    public LogEntry(String level, String message, String source) {
        this.id = "log_" + System.currentTimeMillis();
        this.level = level;
        this.message = message;
        this.source = source;
        this.timestamp = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
